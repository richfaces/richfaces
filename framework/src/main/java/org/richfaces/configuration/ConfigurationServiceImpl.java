/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.richfaces.util.URLToStreamHelper;
import org.richfaces.el.ELUtils;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

import com.google.common.base.Strings;

/**
 * @author Nick Belaevski
 */
public class ConfigurationServiceImpl implements ConfigurationService {
    private static final Logger LOGGER = RichfacesLogger.APPLICATION.getLogger();
    private static final String JNDI_COMP_PREFIX = "java:comp/env/";
    private Map<Enum<?>, ValueExpressionHolder> itemsMap = new ConcurrentHashMap<Enum<?>, ValueExpressionHolder>();
    private AtomicBoolean webEnvironmentUnavailableLogged = new AtomicBoolean();

    private ConfigurationItem getConfigurationItem(Enum<?> enumKey) {
        try {
            ConfigurationItem item = enumKey.getClass().getField(enumKey.name()).getAnnotation(ConfigurationItem.class);
            if (item != null) {
                return item;
            }
        } catch (Exception e) {
            throw new IllegalStateException(MessageFormat.format(
                    "Cannot read @ConfigurationItem annotation from {0}.{1} because of {2}", enumKey.getClass().getName(),
                    enumKey.name(), e.getMessage()));
        }

        throw new IllegalStateException(MessageFormat.format("Annotation @ConfigurationItem is not set at {0}.{1}", enumKey
                .getClass().getName(), enumKey.name()));
    }

    protected ValueExpressionHolder createValueExpressionHolder(FacesContext context, ValueExpression expression,
                                                                String defaultValueString, Class<?> returnType) {
        Object defaultValue = null;

        if (expression == null || !expression.isLiteralText()) {
            if (!Strings.isNullOrEmpty(defaultValueString)) {
                defaultValue = ELUtils.coerce(defaultValueString, returnType);
            }
        }

        return new ValueExpressionHolder(expression, defaultValue);
    }

    private String getInitParameterValue(FacesContext context, ConfigurationItem configurationItem) {
        for (String name : configurationItem.names()) {
            String value = (String) context.getExternalContext().getInitParameter(name);

            if (!Strings.isNullOrEmpty(value)) {
                return value;
            }
        }

        return null;
    }

    private String getWebEnvironmentEntryValue(ConfigurationItem configurationItem) {
        Context context = null;

        try {
            context = new InitialContext();
        } catch (Throwable e) {
            // Throwable is caught here due to GAE requirements
            if (!webEnvironmentUnavailableLogged.getAndSet(true)) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        if (context != null) {
            for (String envName : configurationItem.names()) {
                String qualifiedName;

                if (!envName.startsWith(JNDI_COMP_PREFIX)) {
                    qualifiedName = JNDI_COMP_PREFIX + envName;
                } else {
                    qualifiedName = envName;
                }

                String value = null;
                try {
                    value = (String) context.lookup(qualifiedName);
                } catch (NamingException e) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug(e.getMessage(), e);
                    }
                }

                if (!Strings.isNullOrEmpty(value)) {
                    return value;
                }
            }
        }

        return null;
    }

    private ValueExpression createContextValueExpression(FacesContext context, ConfigurationItem annotation, Class<?> targetType) {
        ConfigurationItemSource source = annotation.source();

        if (source == ConfigurationItemSource.defaultSource) {
            source = ConfigurationItemSource.contextInitParameter;
        }

        String parameterValue = null;

        if (source == ConfigurationItemSource.contextInitParameter) {
            parameterValue = getInitParameterValue(context, annotation);
        } else if (source == ConfigurationItemSource.webEnvironmentEntry) {
            parameterValue = getWebEnvironmentEntryValue(annotation);
        } else {
            throw new IllegalArgumentException(source.toString());
        }

        if (!Strings.isNullOrEmpty(parameterValue)) {
            return ELUtils.createValueExpression(context, parameterValue, annotation.literal(), targetType);
        }

        return null;
    }

    protected <T> T getValue(FacesContext facesContext, Enum<?> key, Class<T> returnType) {
        ValueExpressionHolder holder = itemsMap.get(key);

        if (holder == null) {
            ConfigurationItemsBundle configurationItemsBundle = getConfigurationItemsBundle(key);

            if (configurationItemsBundle == null) {
                ConfigurationItem item = getConfigurationItem(key);
                ValueExpression expression = createContextValueExpression(facesContext, item, returnType);
                holder = createValueExpressionHolder(facesContext, expression, item.defaultValue(), returnType);
                itemsMap.put(key, holder);
            } else {
                synchronized (key.getClass()) {
                    Properties properties = loadProperties(configurationItemsBundle.propertiesFile());

                    Iterator<? extends Enum> keys = EnumSet.allOf(key.getClass()).iterator();
                    while (keys.hasNext()) {
                        Enum<?> nextBundleKey = (Enum<?>) keys.next();

                        ConfigurationItem item = getConfigurationItem(nextBundleKey);

                        if (item.source() != ConfigurationItemSource.defaultSource) {
                            throw new IllegalArgumentException(item.toString());
                        }

                        String parameterValue = null;

                        for (String propertyName : item.names()) {
                            parameterValue = properties.getProperty(propertyName);

                            if (parameterValue != null) {
                                break;
                            }
                        }

                        ValueExpression expression = null;

                        if (parameterValue != null) {
                            expression = ELUtils.createValueExpression(facesContext, parameterValue, item.literal(), returnType);
                        }

                        ValueExpressionHolder siblingHolder = createValueExpressionHolder(facesContext, expression,
                                item.defaultValue(), returnType);

                        itemsMap.put(nextBundleKey, siblingHolder);

                        if (key == nextBundleKey) {
                            holder = siblingHolder;
                        }
                    }
                }
            }
        }

        return returnType.cast(holder.getValue(facesContext));
    }

    private Properties loadProperties(String resourceName) {
        Properties properties = new Properties();

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader != null) {
            URL url = classLoader.getResource(resourceName);
            if (url != null) {
                InputStream is = null;
                try {
                    is = URLToStreamHelper.urlToStream(url);
                    properties.load(is);
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                } finally {
                    try {
                        is.close();
                    } catch (IOException e) {
                        // Swallow
                    }
                }
            }
        }

        return properties;
    }

    private ConfigurationItemsBundle getConfigurationItemsBundle(Enum<?> key) {
        ConfigurationItem item = getConfigurationItem(key);
        ConfigurationItemSource source = item.source();
        if (source == ConfigurationItemSource.defaultSource) {
            Class<?> enclosingClass = key.getClass();
            return enclosingClass.getAnnotation(ConfigurationItemsBundle.class);
        }

        return null;
    }

    public Boolean getBooleanValue(FacesContext facesContext, Enum<?> key) {
        return getValue(facesContext, key, Boolean.class);
    }

    public Integer getIntValue(FacesContext facesContext, Enum<?> key) {
        return getValue(facesContext, key, Integer.class);
    }

    public Long getLongValue(FacesContext facesContext, Enum<?> key) {
        return getValue(facesContext, key, Long.class);
    }

    public String getStringValue(FacesContext facesContext, Enum<?> key) {
        return getValue(facesContext, key, String.class);
    }

    public <T extends Enum<T>> T getEnumValue(FacesContext facesContext, Enum<?> key, Class<T> enumClass) {
        return getValue(facesContext, key, enumClass);
    }

    public Object getValue(FacesContext facesContext, Enum<?> key) {
        return getValue(facesContext, key, Object.class);
    }
}

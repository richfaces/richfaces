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
package org.richfaces.skin;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import org.ajax4jsf.Messages;
import org.richfaces.el.util.ELUtils;
import org.richfaces.util.PropertiesUtil;

/**
 * @author Nick Belaevski
 *
 */
public abstract class AbstractSkinFactory extends SkinFactory {
    private final class SkinBuilder implements Callable<Skin> {
        private String skinName;

        SkinBuilder(String skinName) {
            super();
            this.skinName = skinName;
        }

        public Skin call() throws Exception {
            return buildSkin(FacesContext.getCurrentInstance(), skinName);
        }
    }

    /**
     * Resource Uri for properties file with default values of skin parameters.
     */
    private static final String DEFAULT_SKIN_PATH = "META-INF/skins/%s.skin.properties";
    // private static final String[] DEFAULT_SKIN_PATHS = { DEFAULT_SKIN_PATH };
    private static final String USER_SKIN_PATH = "%s.skin.properties";
    /**
     * Path in jar to pre-defined vendor and custom user-defined skins definitions. in this realisation "META-INF/skins/" for
     * vendor , "" - user-defined.
     */
    private static final String[] SKINS_PATHS = { DEFAULT_SKIN_PATH, USER_SKIN_PATH };
    private ConcurrentMap<String, FutureTask<Skin>> skins = new ConcurrentHashMap<String, FutureTask<Skin>>();

    protected void processProperties(FacesContext context, Map<Object, Object> properties) {
        ELContext elContext = context.getELContext();

        // replace all EL-expressions by prepared ValueBinding ?
        Application app = context.getApplication();

        for (Entry<Object, Object> entry : properties.entrySet()) {
            Object propertyObject = entry.getValue();

            if (propertyObject instanceof String) {
                String property = (String) propertyObject;

                if (ELUtils.isValueReference(property)) {
                    ExpressionFactory expressionFactory = app.getExpressionFactory();

                    entry.setValue(expressionFactory.createValueExpression(elContext, property, Object.class));
                } else {
                    entry.setValue(property);
                }
            }
        }
    }

    /**
     * Factory method for build skin from properties files. for given skin name, search in classpath all resources with name
     * 'name'.skin.properties and append in content to default properties. First, get it from META-INF/skins/ , next - from root
     * package. for any place search order determined by {@link java.lang.ClassLoader } realisation.
     *
     * @param name name for builded skin.
     * @param context
     * @return skin instance for current name
     * @throws SkinNotFoundException - if no skin properies found for name.
     */
    protected Skin buildSkin(FacesContext context, String name) throws SkinNotFoundException {
        Properties skinParams = loadProperties(name, SKINS_PATHS);
        processProperties(context, skinParams);

        return new SkinImpl(skinParams, name);
    }

    /**
     * @param name
     * @param paths
     * @return properties
     * @throws SkinNotFoundException
     */
    protected Properties loadProperties(String name, String[] paths) throws SkinNotFoundException {
        // Get properties for concrete skin.
        Properties skinProperties = new Properties();
        int loadedPropertiesCount = 0;

        for (int i = 0; i < paths.length; i++) {
            String skinPropertiesLocation = paths[i].replaceAll("%s", name);

            if (PropertiesUtil.loadProperties(skinProperties, skinPropertiesLocation)) {
                loadedPropertiesCount++;
            }
        }

        if (loadedPropertiesCount == 0) {
            throw new SkinNotFoundException(Messages.getMessage(Messages.SKIN_NOT_FOUND_ERROR, name));
        }

        return skinProperties;
    }

    @Override
    public Skin getSkin(FacesContext context, String name) {
        if (null == name) {
            throw new SkinNotFoundException(Messages.getMessage(Messages.NULL_SKIN_NAME_ERROR));
        }

        FutureTask<Skin> skinFuture = skins.get(name);
        if (skinFuture == null) {
            FutureTask<Skin> newSkinFuture = new FutureTask<Skin>(new SkinBuilder(name));
            skinFuture = skins.putIfAbsent(name, newSkinFuture);

            if (skinFuture == null) {
                skinFuture = newSkinFuture;
            }
        }

        try {
            skinFuture.run();
            return skinFuture.get();
        } catch (InterruptedException e) {
            throw new SkinNotFoundException(Messages.getMessage(Messages.SKIN_NOT_FOUND_ERROR, name), e);
        } catch (ExecutionException e) {
            throw new SkinNotFoundException(Messages.getMessage(Messages.SKIN_NOT_FOUND_ERROR, name), e);
        }
    }
}

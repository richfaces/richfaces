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
package org.richfaces.el;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import com.google.common.base.Function;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * @author Nick Belaevski
 *
 */
public class GenericsIntrospectionServiceImpl implements GenericsIntrospectionService {

    private static final class GenericsCacheEntry {

        private Class<?> beanClass;

        private LoadingCache<String, Class<?>> containerClassesMap = CacheBuilder.newBuilder().initialCapacity(2)
            .build(CacheLoader.from(new Function<String, Class<?>>() {
                public Class<?> apply(String input) {
                    PropertyDescriptor propertyDescriptor = getPropertyDescriptor(input);
                    return getGenericContainerClass(propertyDescriptor);
                }
            }));

        GenericsCacheEntry(Class<?> beanClass) {
            this.beanClass = beanClass;
        }

        private Class<?> resolveType(Type type) {
            Class<?> result = Object.class;

            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type[] types = parameterizedType.getActualTypeArguments();

                if (types != null && types.length != 0) {
                    Type actualType = types[0];

                    if (actualType instanceof Class) {
                        result = (Class<?>) actualType;
                    }
                }
            }

            return result;
        }

        private Class<?> getGenericContainerClass(PropertyDescriptor pd) {
            if (pd == null) {
                return null;
            }

            Method readMethod = pd.getReadMethod();
            if (readMethod == null) {
                return null;
            }

            return resolveType(readMethod.getGenericReturnType());
        }

        private PropertyDescriptor getPropertyDescriptor(String propertyName) {
            BeanInfo beanInfo = null;
            try {
                beanInfo = Introspector.getBeanInfo(beanClass);
            } catch (IntrospectionException e) {
                throw new FacesException(e.getMessage(), e);
            } finally {
                Introspector.flushFromCaches(beanClass);
            }

            if (beanInfo == null) {
                return null;
            }

            PropertyDescriptor[] propertyDescriptorsArray = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor pd : propertyDescriptorsArray) {
                if (propertyName.equals(pd.getName())) {
                    return pd;
                }
            }

            return null;
        }

        public Class<?> getContainerClass(String propertyName) throws ExecutionException {
            return containerClassesMap.get(propertyName);
        }
    }

    private final LoadingCache<Class<?>, GenericsCacheEntry> cache = CacheBuilder.newBuilder().weakKeys().softValues()
        .build(CacheLoader.from(new Function<Class<?>, GenericsCacheEntry>() {
            public GenericsCacheEntry apply(java.lang.Class<?> input) {
                return new GenericsCacheEntry(input);
            }
        }));

    private Class<?> getGenericCollectionType(FacesContext context, Object base, String propertyName) {
        Class<?> genericPropertyClass = null;

        if ((base != null) && (propertyName != null)) {
            Class<? extends Object> beanClass = base.getClass();

            // Map and ResourceBundle have special resolvers that we doesn't support
            if (!Map.class.isAssignableFrom(beanClass) && !ResourceBundle.class.isAssignableFrom(beanClass)) {
                try {
                    return cache.get(beanClass).getContainerClass(propertyName);
                } catch (ExecutionException e) {
                    throw new FacesException(String.format("Can't resolve the beanClass '%s' from propertyName '%s'", beanClass, propertyName), e);
                }
            }
        }

        return genericPropertyClass;
    }

    public Class<?> getContainerClass(FacesContext facesContext, ValueExpression expression) {
        ELContext initialELContext = facesContext.getELContext();
        CapturingELResolver capturingELResolver = new CapturingELResolver(initialELContext.getELResolver());
        Class<?> type = expression.getType(new ELContextWrapper(initialELContext, capturingELResolver));
        Class<?> containerType = type.getComponentType();

        if ((containerType == null) && (type != null)) {
            if (Collection.class.isAssignableFrom(type)) {
                Object base = capturingELResolver.getBase();
                Object property = capturingELResolver.getProperty();

                if ((base != null) && (property != null)) {
                    containerType = getGenericCollectionType(facesContext, base, property.toString());
                }
            }
        }

        return containerType;
    }
}

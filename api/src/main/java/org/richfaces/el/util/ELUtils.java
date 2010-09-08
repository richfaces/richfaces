/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package org.richfaces.el.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import org.richfaces.el.ELContextWrapper;
import org.richfaces.el.util.GenericsIntrospectionCache.GenericsCacheEntry;

/**
 * @author asmirnov
 */
public final class ELUtils {
    private ELUtils() {

        // Utility class with static methods only - do not instantiate.
    }

    /**
     * Get EL-enabled value. Return same string, if not el-expression.
     * Otherthise, return parsed and evaluated expression.
     *
     * @param context -
     *                current Faces Context.
     * @param value   -
     *                string to parse.
     * @return - interpreted el or unmodified value.
     */
    public static boolean isValueReference(String value) {
        if (value == null) {
            return false;
        }

        int start = value.indexOf("#{");

        if (start >= 0) {
            int end = value.lastIndexOf('}');

            if ((end >= 0) && (start < end)) {
                return true;
            }
        }

        return false;
    }

    public static Object evaluateValueExpression(ValueExpression expression, ELContext elContext) {

        if (expression.isLiteralText()) {
            return expression.getExpressionString();
        } else {
            return expression.getValue(elContext);
        }

    }
    
    public static ValueExpression createValueExpression(String expression) {

        return createValueExpression(expression, Object.class);

    }

    public static ValueExpression createValueExpression(String expression, Class<?> expectedType) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), expression, expectedType);
    }
    
    private static Class<?> resolveType(Type type) {
        Class<?> result = Object.class;

        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] types = parameterizedType.getActualTypeArguments();

            if ((types != null) && (types.length != 0)) {
                Type actualType = types[0];

                if (actualType instanceof Class) {
                    result = (Class<?>) actualType;
                }
            }
        }

        return result;
    }

    private static BeanInfo getBeanInfo(Class<?> beanClass, GenericsCacheEntry entry) {
        BeanInfo beanInfo = null;
        SoftReference<BeanInfo> beanInfoReference = entry.beanInfoReference;

        if (beanInfoReference != null) {
            beanInfo = beanInfoReference.get();
        }

        if (beanInfo == null) {
            try {
                beanInfo = Introspector.getBeanInfo(beanClass);
                entry.beanInfoReference = new SoftReference<BeanInfo>(beanInfo);
            } catch (IntrospectionException e) {
                throw new FacesException(e.getMessage(), e);
            }
        }

        return beanInfo;
    }

    private static Class<?> getGenericCollectionType(FacesContext context, Object base, String propertyName) {
        Class<?> genericPropertyClass = null;

        if ((base != null) && (propertyName != null)) {
            Class<? extends Object> beanClass = base.getClass();

            // Map and ResourceBundle have special resolvers that we doesn't support
            if (!Map.class.isAssignableFrom(beanClass) && !ResourceBundle.class.isAssignableFrom(beanClass)) {
                GenericsIntrospectionCache introspectionCache = GenericsIntrospectionCache.getInstance(context);

                synchronized (introspectionCache) {
                    GenericsCacheEntry cacheEntry = introspectionCache.getGenericCacheEntry(beanClass);

                    if (cacheEntry.genericPropertiesClasses == null) {
                        cacheEntry.genericPropertiesClasses = new HashMap<String, Class<?>>();
                    } else {
                        genericPropertyClass = cacheEntry.genericPropertiesClasses.get(propertyName);
                    }

                    if (genericPropertyClass == null) {
                        if (!cacheEntry.genericPropertiesClasses.containsKey(propertyName)) {
                            BeanInfo beanInfo = getBeanInfo(beanClass, cacheEntry);
                            PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();

                            for (PropertyDescriptor pd : descriptors) {
                                if (propertyName.equals(pd.getName())) {
                                    Method readMethod = pd.getReadMethod();

                                    genericPropertyClass = resolveType(readMethod.getGenericReturnType());

                                    break;
                                }
                            }

                            cacheEntry.genericPropertiesClasses.put(propertyName, genericPropertyClass);
                        } else {

                            // property Class has been already resolved as null
                        }
                    }
                }
            }
        }

        return genericPropertyClass;
    }

    public static Class<?> getContainerClass(FacesContext facesContext, ValueExpression expression) {
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

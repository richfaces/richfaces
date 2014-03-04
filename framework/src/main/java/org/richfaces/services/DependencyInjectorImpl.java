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
package org.richfaces.services;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.resource.PostConstructResource;
import org.richfaces.resource.ResourceParameter;
import org.richfaces.resource.ResourceParameterELResolver;

/**
 * @author Nick Belaevski
 *
 */
public class DependencyInjectorImpl implements DependencyInjector {

    private static final Logger LOGGER = RichfacesLogger.APPLICATION.getLogger();

    private ConcurrentMap<Class<?>, IntrospectionData> classesCache = new ConcurrentHashMap<Class<?>, IntrospectionData>();

    private void invokeMethod(Object bean, Method method) throws IllegalArgumentException, IllegalAccessException,
        InvocationTargetException {

        if (method != null) {
            method.setAccessible(true);
            method.invoke(bean);
        }
    }

    private boolean isUncheckedException(Class<?> type) {
        // JLS 2nd edition - 11.2 Compile-Time Checking of Exceptions
        return RuntimeException.class.isAssignableFrom(type) || Error.class.isAssignableFrom(type);
    }

    private void verifyPostConstructMethod(Method method) {
        // TODO - allow FacesContext to be passed
        if (method.getParameterTypes().length != 0) {
            throw new IllegalStateException(MessageFormat.format("Post-construction method {0} has one or more parameters",
                method.toString()));
        }

        if (!Void.TYPE.equals(method.getReturnType())) {
            throw new IllegalStateException(MessageFormat.format("Post-construction method {0} has incorrect return type",
                method.toString()));
        }

        if ((method.getModifiers() & Modifier.STATIC) != 0) {
            throw new IllegalStateException(MessageFormat.format("Post-construction method {0} is static", method.toString()));
        }

        Class<?>[] exceptionTypes = method.getExceptionTypes();
        for (Class<?> exceptionType : exceptionTypes) {
            if (isUncheckedException(exceptionType)) {
                continue;
            }

            throw new IllegalStateException(MessageFormat.format("Post-construction method {0} throws checked exception",
                method.toString()));
        }
    }

    private void inspectMethod(Method method, Class<? extends Annotation> annotationClass, IntrospectionData introspectionData) {

        Annotation annotation = method.getAnnotation(annotationClass);
        if (annotation != null) {
            verifyPostConstructMethod(method);

            if (introspectionData.getPostConstructMethod() != null) {
                throw new IllegalStateException(MessageFormat.format(
                    "There are two conflicting post-construction methods: {0} and {1}", method.toString(), introspectionData
                        .getPostConstructMethod().toString()));
            }

            introspectionData.setPostConstructMethod(method);
        }
    }

    private void locatePostConstructMethods(Class<?> clazz, IntrospectionData introspectionData) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            inspectMethod(method, PostConstructResource.class, introspectionData);
        }

        Class<?> superclass = clazz.getSuperclass();
        if (!Object.class.equals(superclass)) {
            locatePostConstructMethods(superclass, introspectionData);
        }
    }

    private void locateManagedPropertyFields(Class<?> clazz, Map<String, ResourceParameter> fieldsMap) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ResourceParameter dependency = field.getAnnotation(ResourceParameter.class);

            if (dependency != null) {
                String propertyName = field.getName();

                if (!fieldsMap.containsKey(propertyName)) {
                    fieldsMap.put(propertyName, dependency);
                }
            }
        }

        Class<?> superclass = clazz.getSuperclass();
        if (!Object.class.equals(superclass)) {
            locateManagedPropertyFields(superclass, fieldsMap);
        }
    }

    private <T extends Annotation> T getAnnotation(PropertyDescriptor descriptor, Class<T> annotationClass) {
        T annotation = null;

        Method writeMethod = descriptor.getWriteMethod();
        if (writeMethod != null) {
            annotation = writeMethod.getAnnotation(annotationClass);
        }

        if (annotation == null) {
            Method readMethod = descriptor.getReadMethod();
            if (readMethod != null) {
                annotation = readMethod.getAnnotation(annotationClass);
            }
        }

        return annotation;
    }

    private void locateManagedPropertyDescriptors(Class<?> clazz, IntrospectionData introspectionData,
        Map<String, ResourceParameter> injectableFields) {

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            if (beanInfo != null) {
                PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
                if (descriptors != null) {
                    for (PropertyDescriptor descriptor : descriptors) {
                        String propertyName = descriptor.getName();

                        ResourceParameter dependency = injectableFields.get(propertyName);

                        if (dependency == null) {
                            dependency = getAnnotation(descriptor, ResourceParameter.class);
                        }

                        if (dependency != null) {
                            Injector<?> injector = new PropertyDependencyInjector(descriptor, dependency);
                            introspectionData.addInjector(propertyName, injector);
                        }
                    }
                }
            }
        } catch (IntrospectionException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(e.getMessage(), e);
            }
        } finally {
            Introspector.flushFromCaches(clazz);
        }
    }

    protected IntrospectionData createIntrospectionData(Class<?> beanClass) {
        IntrospectionData introspectionData = new IntrospectionData();

        Map<String, ResourceParameter> injectableFields = new HashMap<String, ResourceParameter>();
        locateManagedPropertyFields(beanClass, injectableFields);

        locateManagedPropertyDescriptors(beanClass, introspectionData, injectableFields);

        locatePostConstructMethods(beanClass, introspectionData);

        return introspectionData;
    }

    public void inject(FacesContext context, Object bean) {
        Class<?> beanClass = bean.getClass();

        IntrospectionData introspectionData = classesCache.get(beanClass);
        if (introspectionData == null) {
            introspectionData = createIntrospectionData(beanClass);
            classesCache.put(beanClass, introspectionData);
        }

        try {
            Map<String, Injector<?>> injectorsMap = introspectionData.getInjectorsMap();
            if (!injectorsMap.isEmpty()) {
                for (Injector<?> injector : injectorsMap.values()) {
                    injector.inject(context, bean);
                }
            }

            Method postConstructMethod = introspectionData.getPostConstructMethod();
            if (postConstructMethod != null) {
                invokeMethod(bean, postConstructMethod);
            }
        } catch (IllegalArgumentException e) {
            throw new FacesException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new FacesException(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new FacesException(e.getMessage(), e);
        }
    }

    private abstract static class Injector<T extends Annotation> {
        private PropertyDescriptor propertyDescriptor;
        private T dependency;

        public Injector(PropertyDescriptor propertyDescriptor, T dependency) {
            super();
            this.propertyDescriptor = propertyDescriptor;
            this.dependency = dependency;
        }

        protected T getDependency() {
            return dependency;
        }

        protected PropertyDescriptor getPropertyDescriptor() {
            return propertyDescriptor;
        }

        protected abstract Object evaluateProperty(FacesContext context, Class<?> propertyType);

        public void inject(FacesContext context, Object bean) throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {

            Method writeMethod = propertyDescriptor.getWriteMethod();

            if (writeMethod != null) {
                writeMethod.invoke(bean, evaluateProperty(context, propertyDescriptor.getPropertyType()));
            } else {
                throw new IllegalStateException(MessageFormat.format("Write method for property {0} doesn't exist",
                    propertyDescriptor.getName()));
            }
        }
    }

    private static final class PropertyDependencyInjector extends Injector<ResourceParameter> {
        public PropertyDependencyInjector(PropertyDescriptor propertyDescriptor, ResourceParameter dependency) {
            super(propertyDescriptor, dependency);
        }

        private Object getExpressionValue(FacesContext context, String expressionString, Class<?> expectedType) {
            ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
            ValueExpression expression = expressionFactory.createValueExpression(context.getELContext(), expressionString,
                expectedType);
            return expression.getValue(context.getELContext());
        }

        protected Object evaluateProperty(FacesContext context, Class<?> propertyType) {
            Class<?> expectedType;
            if (!propertyType.isPrimitive()) {
                expectedType = Object.class;
            } else {
                expectedType = propertyType;
            }

            ResourceParameter resourceParameter = getDependency();

            String expression = resourceParameter.expression();
            String name = resourceParameter.name();

            if (expression.length() != 0 && name.length() != 0) {
                throw new IllegalStateException(MessageFormat.format(
                    "'name' and 'expression' should not be specified simultaneously: {0}", resourceParameter));
            }

            Object propertyValue = null;
            if (expression.length() != 0) {
                propertyValue = getExpressionValue(context, expression, expectedType);
            } else {
                if (name.length() == 0) {
                    name = getPropertyDescriptor().getName();
                }

                Map<String, Object> parameters = (Map<String, Object>) context.getAttributes().get(
                    ResourceParameterELResolver.CONTEXT_ATTRIBUTE_NAME);

                propertyValue = parameters.get(name);
            }

            if (propertyValue == null || "".equals(propertyValue)) {
                String defaultValue = resourceParameter.defaultValue();
                if (defaultValue != null && defaultValue.length() != 0) {
                    propertyValue = getExpressionValue(context, defaultValue, expectedType);
                }
            }

            if (propertyValue != null) {
                propertyValue = context.getApplication().getExpressionFactory().coerceToType(propertyValue, propertyType);
            }

            return propertyValue;
        }
    }

    private static final class IntrospectionData {
        private Method postConstructMethod = null;
        private Map<String, Injector<?>> injectorsMap = null;

        public Map<String, Injector<?>> getInjectorsMap() {
            if (injectorsMap != null) {
                return injectorsMap;
            }

            return Collections.emptyMap();
        }

        public void addInjector(String propertyName, Injector<?> injector) {
            if (injectorsMap == null) {
                injectorsMap = new HashMap<String, Injector<?>>();
            }

            injectorsMap.put(propertyName, injector);
        }

        public Method getPostConstructMethod() {
            return postConstructMethod;
        }

        public void setPostConstructMethod(Method postConstructMethod) {
            this.postConstructMethod = postConstructMethod;
        }
    }
}

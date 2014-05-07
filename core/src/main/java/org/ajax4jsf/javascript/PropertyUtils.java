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
package org.ajax4jsf.javascript;

import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
final class PropertyUtils {
    private static final PropertyDescriptor[] EMPTY_DESCRIPTORS_ARRAY = new PropertyDescriptor[0];
    private static final Logger LOGGER = RichfacesLogger.APPLICATION.getLogger();

    private PropertyUtils() {

        // private constructor of pure utility methods class
    }

    public static PropertyDescriptor[] getPropertyDescriptors(Object bean) {
        if (bean == null) {
            throw new IllegalArgumentException("argument is null");
        }

        PropertyDescriptor[] descriptors = null;

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());

            descriptors = beanInfo.getPropertyDescriptors();
        } catch (IntrospectionException e) {
            LOGGER.error(e.getMessage(), e);
        }

        if (descriptors == null) {
            descriptors = EMPTY_DESCRIPTORS_ARRAY;
        }

        return descriptors;
    }

    public static Object readPropertyValue(Object bean, PropertyDescriptor descriptor) throws Exception {
        Method readMethod = descriptor.getReadMethod();

        if (readMethod == null) {
            throw new NoSuchMethodException(MessageFormat.format("Read method for property ''{0}'' not found",
                descriptor.getName()));
        }

        try {
            return readMethod.invoke(bean);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();

            if (cause instanceof Exception) {
                throw (Exception) cause;
            } else if (cause instanceof Error) {
                throw (Error) cause;
            } else {
                throw e;
            }
        }
    }
}

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
package org.richfaces.application.configuration;

import javax.faces.context.FacesContext;

import org.richfaces.application.ServiceTracker;
import org.richfaces.configuration.ConfigurationService;

/**
 * @author Nick Belaevski
 *
 */
public final class ConfigurationServiceHelper {
    private ConfigurationServiceHelper() {
    }

    public static Boolean getBooleanConfigurationValue(FacesContext facesContext, Enum<?> key) {
        return ServiceTracker.getService(ConfigurationService.class).getBooleanValue(facesContext, key);
    }

    public static Integer getIntConfigurationValue(FacesContext facesContext, Enum<?> key) {
        return ServiceTracker.getService(ConfigurationService.class).getIntValue(facesContext, key);
    }

    public static Long getLongConfigurationValue(FacesContext facesContext, Enum<?> key) {
        return ServiceTracker.getService(ConfigurationService.class).getLongValue(facesContext, key);
    }

    public static String getStringConfigurationValue(FacesContext facesContext, Enum<?> key) {
        return ServiceTracker.getService(ConfigurationService.class).getStringValue(facesContext, key);
    }

    public static Object getConfigurationValue(FacesContext facesContext, Enum<?> key) {
        return ServiceTracker.getService(ConfigurationService.class).getValue(facesContext, key);
    }

    public static <T extends Enum<T>> T getEnumConfigurationValue(FacesContext facesContext, Enum<?> key, Class<T> enumClass) {
        return ServiceTracker.getService(ConfigurationService.class).getEnumValue(facesContext, key, enumClass);
    }
}
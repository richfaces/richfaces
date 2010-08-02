/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.resource;

import java.text.MessageFormat;

import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;

import org.richfaces.log.RichfacesLogger;
import org.slf4j.Logger;
import org.slf4j.Marker;

/**
 * @author Nick Belaevski
 * 
 */
final class ResourceLogger implements Logger {

    static final ResourceLogger INSTANCE = new ResourceLogger();

    private static final Logger LOGGER = RichfacesLogger.RESOURCE.getLogger();
    
    private ResourceLogger() {}

    void logResourceProblem(FacesContext context, Throwable throwable, String messagePattern,
        Object... arguments) {
        boolean isProductionStage = context.isProjectStage(ProjectStage.Production);

        if (LOGGER.isWarnEnabled() || (!isProductionStage && LOGGER.isInfoEnabled())) {
            String formattedMessage = MessageFormat.format(messagePattern, arguments);

            if (throwable != null) {
                LOGGER.warn(formattedMessage, throwable);
            } else {
                if (isProductionStage) {
                    LOGGER.info(formattedMessage);
                } else {
                    LOGGER.warn(formattedMessage);
                }
            }
        }
    }

    void logMissingResource(FacesContext context, String resourceData) {
        logResourceProblem(context, null, "Resource {0} was not found", resourceData);
    }

    public String getName() {
        return LOGGER.getName();
    }

    public boolean isTraceEnabled() {
        return LOGGER.isTraceEnabled();
    }

    public void trace(String msg) {
        LOGGER.trace(msg);
    }

    public void trace(String format, Object arg) {
        LOGGER.trace(format, arg);
    }

    public void trace(String format, Object arg1, Object arg2) {
        LOGGER.trace(format, arg1, arg2);
    }

    public void trace(String format, Object[] argArray) {
        LOGGER.trace(format, argArray);
    }

    public void trace(String msg, Throwable t) {
        LOGGER.trace(msg, t);
    }

    public boolean isTraceEnabled(Marker marker) {
        return LOGGER.isTraceEnabled(marker);
    }

    public void trace(Marker marker, String msg) {
        LOGGER.trace(marker, msg);
    }

    public void trace(Marker marker, String format, Object arg) {
        LOGGER.trace(marker, format, arg);
    }

    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        LOGGER.trace(marker, format, arg1, arg2);
    }

    public void trace(Marker marker, String format, Object[] argArray) {
        LOGGER.trace(marker, format, argArray);
    }

    public void trace(Marker marker, String msg, Throwable t) {
        LOGGER.trace(marker, msg, t);
    }

    public boolean isDebugEnabled() {
        return LOGGER.isDebugEnabled();
    }

    public void debug(String msg) {
        LOGGER.debug(msg);
    }

    public void debug(String format, Object arg) {
        LOGGER.debug(format, arg);
    }

    public void debug(String format, Object arg1, Object arg2) {
        LOGGER.debug(format, arg1, arg2);
    }

    public void debug(String format, Object[] argArray) {
        LOGGER.debug(format, argArray);
    }

    public void debug(String msg, Throwable t) {
        LOGGER.debug(msg, t);
    }

    public boolean isDebugEnabled(Marker marker) {
        return LOGGER.isDebugEnabled(marker);
    }

    public void debug(Marker marker, String msg) {
        LOGGER.debug(marker, msg);
    }

    public void debug(Marker marker, String format, Object arg) {
        LOGGER.debug(marker, format, arg);
    }

    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        LOGGER.debug(marker, format, arg1, arg2);
    }

    public void debug(Marker marker, String format, Object[] argArray) {
        LOGGER.debug(marker, format, argArray);
    }

    public void debug(Marker marker, String msg, Throwable t) {
        LOGGER.debug(marker, msg, t);
    }

    public boolean isInfoEnabled() {
        return LOGGER.isInfoEnabled();
    }

    public void info(String msg) {
        LOGGER.info(msg);
    }

    public void info(String format, Object arg) {
        LOGGER.info(format, arg);
    }

    public void info(String format, Object arg1, Object arg2) {
        LOGGER.info(format, arg1, arg2);
    }

    public void info(String format, Object[] argArray) {
        LOGGER.info(format, argArray);
    }

    public void info(String msg, Throwable t) {
        LOGGER.info(msg, t);
    }

    public boolean isInfoEnabled(Marker marker) {
        return LOGGER.isInfoEnabled(marker);
    }

    public void info(Marker marker, String msg) {
        LOGGER.info(marker, msg);
    }

    public void info(Marker marker, String format, Object arg) {
        LOGGER.info(marker, format, arg);
    }

    public void info(Marker marker, String format, Object arg1, Object arg2) {
        LOGGER.info(marker, format, arg1, arg2);
    }

    public void info(Marker marker, String format, Object[] argArray) {
        LOGGER.info(marker, format, argArray);
    }

    public void info(Marker marker, String msg, Throwable t) {
        LOGGER.info(marker, msg, t);
    }

    public boolean isWarnEnabled() {
        return LOGGER.isWarnEnabled();
    }

    public void warn(String msg) {
        LOGGER.warn(msg);
    }

    public void warn(String format, Object arg) {
        LOGGER.warn(format, arg);
    }

    public void warn(String format, Object[] argArray) {
        LOGGER.warn(format, argArray);
    }

    public void warn(String format, Object arg1, Object arg2) {
        LOGGER.warn(format, arg1, arg2);
    }

    public void warn(String msg, Throwable t) {
        LOGGER.warn(msg, t);
    }

    public boolean isWarnEnabled(Marker marker) {
        return LOGGER.isWarnEnabled(marker);
    }

    public void warn(Marker marker, String msg) {
        LOGGER.warn(marker, msg);
    }

    public void warn(Marker marker, String format, Object arg) {
        LOGGER.warn(marker, format, arg);
    }

    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        LOGGER.warn(marker, format, arg1, arg2);
    }

    public void warn(Marker marker, String format, Object[] argArray) {
        LOGGER.warn(marker, format, argArray);
    }

    public void warn(Marker marker, String msg, Throwable t) {
        LOGGER.warn(marker, msg, t);
    }

    public boolean isErrorEnabled() {
        return LOGGER.isErrorEnabled();
    }

    public void error(String msg) {
        LOGGER.error(msg);
    }

    public void error(String format, Object arg) {
        LOGGER.error(format, arg);
    }

    public void error(String format, Object arg1, Object arg2) {
        LOGGER.error(format, arg1, arg2);
    }

    public void error(String format, Object[] argArray) {
        LOGGER.error(format, argArray);
    }

    public void error(String msg, Throwable t) {
        LOGGER.error(msg, t);
    }

    public boolean isErrorEnabled(Marker marker) {
        return LOGGER.isErrorEnabled(marker);
    }

    public void error(Marker marker, String msg) {
        LOGGER.error(marker, msg);
    }

    public void error(Marker marker, String format, Object arg) {
        LOGGER.error(marker, format, arg);
    }

    public void error(Marker marker, String format, Object arg1, Object arg2) {
        LOGGER.error(marker, format, arg1, arg2);
    }

    public void error(Marker marker, String format, Object[] argArray) {
        LOGGER.error(marker, format, argArray);
    }

    public void error(Marker marker, String msg, Throwable t) {
        LOGGER.error(marker, msg, t);
    }


}

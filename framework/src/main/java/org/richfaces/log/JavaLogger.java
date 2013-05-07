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
package org.richfaces.log;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.LogRecord;

import org.richfaces.l10n.BundleLoader;
import org.richfaces.l10n.InterpolationException;
import org.richfaces.l10n.MessageInterpolator;

/**
 * <p class="changed_added_4_0">
 * That logger delegates all calls to the JDK {@link java.util.logging.Logger}
 * </p>
 *
 * @author asmirnov@exadel.com
 * @author nick
 */
public class JavaLogger implements Logger {
    public static final String RICHFACES_LOG = "org.richfaces";
    static final Map<Level, java.util.logging.Level> LEVELS_MAP = new EnumMap<Level, java.util.logging.Level>(Level.class);

    static {
        LEVELS_MAP.put(Level.ERROR, java.util.logging.Level.SEVERE);
        LEVELS_MAP.put(Level.INFO, java.util.logging.Level.INFO);
        LEVELS_MAP.put(Level.WARNING, java.util.logging.Level.WARNING);
        LEVELS_MAP.put(Level.DEBUG, java.util.logging.Level.FINE);
    }

    private static final String CLASS_NAME = JavaLogger.class.getName();
    private final java.util.logging.Logger jdkLogger;
    private MessageInterpolator messageInterpolator;

    JavaLogger(String category) {
        jdkLogger = java.util.logging.Logger.getLogger(category);
        messageInterpolator = new MessageInterpolator(new BundleLoader());
    }

    JavaLogger() {
        this(RICHFACES_LOG);
    }

    private void fillCallerData(String fqn, LogRecord record) {
        StackTraceElement[] stackTrace = new Exception().getStackTrace();

        int i = 0;

        for (; i < stackTrace.length; i++) {
            if (fqn.equals(stackTrace[i].getClassName())) {
                break;
            }
        }

        int idx = i + 1;

        for (; idx < stackTrace.length; idx++) {
            if (!fqn.equals(stackTrace[idx].getClassName())) {
                break;
            }
        }

        if (idx < stackTrace.length) {
            record.setSourceMethodName(stackTrace[idx].getMethodName());
            record.setSourceClassName(stackTrace[idx].getClassName());
        }
    }

    private String interpolate(Enum<?> messageKey, Object... args) {
        try {
            return messageInterpolator.interpolate(Locale.getDefault(), messageKey, args);
        } catch (InterpolationException e) {
            return "???" + e.getMessageKey() + "???";
        }
    }

    private LogRecord createRecord(java.util.logging.Level level, CharSequence message, Throwable thrown) {
        // millis and thread are filled by the constructor
        LogRecord record = new LogRecord(level, message != null ? message.toString() : null);

        // TODO resource bundle?
        record.setLoggerName(jdkLogger.getName());
        record.setThrown(thrown);
        fillCallerData(CLASS_NAME, record);

        return record;
    }

    public boolean isDebugEnabled() {
        return isLogEnabled(Level.DEBUG);
    }

    public void debug(CharSequence content) {
        log(Level.DEBUG, content);
    }

    public void debug(Enum<?> messageKey, Object... args) {
        log(Level.DEBUG, messageKey, args);
    }

    public void debug(CharSequence content, Throwable thrown) {
        log(Level.DEBUG, content, thrown);
    }

    public void debug(Throwable error, Enum<?> messageKey, Object... args) {
        log(Level.DEBUG, error, messageKey, args);
    }

    public void debug(Throwable thrown) {
        log(Level.DEBUG, thrown);
    }

    public boolean isInfoEnabled() {
        return isLogEnabled(Level.INFO);
    }

    public void info(CharSequence content) {
        log(Level.INFO, content);
    }

    public void info(Enum<?> messageKey, Object... args) {
        log(Level.INFO, messageKey, args);
    }

    public void info(CharSequence content, Throwable thrown) {
        log(Level.INFO, content, thrown);
    }

    public void info(Throwable error, Enum<?> messageKey, Object... args) {
        log(Level.INFO, error, messageKey, args);
    }

    public void info(Throwable thrown) {
        log(Level.INFO, thrown);
    }

    public boolean isWarnEnabled() {
        return isLogEnabled(Level.WARNING);
    }

    public void warn(CharSequence content) {
        log(Level.WARNING, content);
    }

    public void warn(Enum<?> messageKey, Object... args) {
        log(Level.WARNING, messageKey, args);
    }

    public void warn(CharSequence content, Throwable thrown) {
        log(Level.WARNING, content, thrown);
    }

    public void warn(Throwable error, Enum<?> messageKey, Object... args) {
        log(Level.WARNING, error, messageKey, args);
    }

    public void warn(Throwable thrown) {
        log(Level.WARNING, thrown);
    }

    public boolean isErrorEnabled() {
        return isLogEnabled(Level.ERROR);
    }

    public void error(CharSequence content) {
        log(Level.ERROR, content);
    }

    public void error(Enum<?> messageKey, Object... args) {
        log(Level.ERROR, messageKey, args);
    }

    public void error(CharSequence content, Throwable thrown) {
        log(Level.ERROR, content, thrown);
    }

    public void error(Throwable error, Enum<?> messageKey, Object... args) {
        log(Level.ERROR, error, messageKey, args);
    }

    public void error(Throwable thrown) {
        log(Level.ERROR, thrown);
    }

    public boolean isLogEnabled(Level level) {
        return jdkLogger.isLoggable(LEVELS_MAP.get(level));
    }

    public void log(Level level, CharSequence content) {
        java.util.logging.Level julLevel = LEVELS_MAP.get(level);
        if (jdkLogger.isLoggable(julLevel)) {
            jdkLogger.log(createRecord(julLevel, content, null));
        }
    }

    public void log(Level level, Enum<?> messageKey, Object... args) {
        java.util.logging.Level julLevel = LEVELS_MAP.get(level);
        if (jdkLogger.isLoggable(julLevel)) {
            jdkLogger.log(createRecord(julLevel, interpolate(messageKey, args), null));
        }
    }

    public void log(Level level, CharSequence content, Throwable thrown) {
        java.util.logging.Level julLevel = LEVELS_MAP.get(level);
        if (jdkLogger.isLoggable(julLevel)) {
            jdkLogger.log(createRecord(julLevel, content, thrown));
        }
    }

    public void log(Level level, Throwable thrown, Enum<?> messageKey, Object... args) {
        java.util.logging.Level julLevel = LEVELS_MAP.get(level);
        if (jdkLogger.isLoggable(julLevel)) {
            jdkLogger.log(createRecord(julLevel, interpolate(messageKey, args), thrown));
        }
    }

    public void log(Level level, Throwable thrown) {
        java.util.logging.Level julLevel = LEVELS_MAP.get(level);
        if (jdkLogger.isLoggable(julLevel)) {
            jdkLogger.log(createRecord(julLevel, null, thrown));
        }
    }
}

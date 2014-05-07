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

/**
 * That interface hides current logging system from classes. Concrete implementation should provide appropriate logger instance
 * that delegates messages to the current log system.
 *
 * @author shura
 */
public interface Logger {
    public enum Level {
        DEBUG,
        INFO,
        WARNING,
        ERROR
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return
     */
    boolean isDebugEnabled();

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param content
     */
    void debug(CharSequence content);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param messageKey
     * @param args
     */
    void debug(Enum<?> messageKey, Object... args);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param content
     * @param error
     */
    void debug(CharSequence content, Throwable error);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param error
     * @param messageKey
     * @param args
     */
    void debug(Throwable error, Enum<?> messageKey, Object... args);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param error
     */
    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param error
     */
    void debug(Throwable error);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return
     */
    boolean isInfoEnabled();

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param content
     */
    void info(CharSequence content);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param messageKey
     * @param args
     */
    void info(Enum<?> messageKey, Object... args);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param content
     * @param error
     */
    void info(CharSequence content, Throwable error);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param error
     * @param messageKey
     * @param args
     */
    void info(Throwable error, Enum<?> messageKey, Object... args);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param error
     */
    void info(Throwable error);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return
     */
    boolean isWarnEnabled();

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param content
     */
    void warn(CharSequence content);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param messageKey
     * @param args
     */
    void warn(Enum<?> messageKey, Object... args);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param content
     * @param error
     */
    void warn(CharSequence content, Throwable error);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param error
     * @param messageKey
     * @param args
     */
    void warn(Throwable error, Enum<?> messageKey, Object... args);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param error
     */
    void warn(Throwable error);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return
     */
    boolean isErrorEnabled();

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param content
     */
    void error(CharSequence content);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param messageKey
     * @param args
     */
    void error(Enum<?> messageKey, Object... args);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param content
     * @param error
     */
    void error(CharSequence content, Throwable error);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param error
     * @param messageKey
     * @param args
     */
    void error(Throwable error, Enum<?> messageKey, Object... args);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param error
     */
    void error(Throwable error);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param level
     * @return
     */
    boolean isLogEnabled(Level level);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param level
     * @param content
     */
    void log(Level level, CharSequence content);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param level
     * @param content
     * @param error
     */
    void log(Level level, CharSequence content, Throwable error);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param level
     * @param error
     */
    void log(Level level, Throwable error);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param level
     * @param messageKey
     * @param args
     */
    void log(Level level, Enum<?> messageKey, Object... args);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param level
     * @param error
     * @param messageKey
     * @param args
     */
    void log(Level level, Throwable error, Enum<?> messageKey, Object... args);
}

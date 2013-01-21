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

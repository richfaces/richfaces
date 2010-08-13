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

package org.richfaces;


/**
 * That interface hides current logging system from classes.
 * Concrete implementation should provide appropriate logger instance that delegates
 * messages to the current log system.
 *
 * @author shura
 */
public interface Logger {
    
    public enum Level {
        ERROR,
        INFO,
        WARNING,
        DEBUG
    }
    
    /**
     * <p class="changed_added_4_0"></p>
     * @return
     */
    public boolean isDebugEnabled();

    /**
     * <p class="changed_added_4_0"></p>
     * @param content
     */
    public void debug(CharSequence content);

    /**
     * <p class="changed_added_4_0"></p>
     * @param content
     * @param error
     */
    public void debug(CharSequence content, Throwable error);

    /**
     * <p class="changed_added_4_0"></p>
     * @param error
     */
    /**
     * <p class="changed_added_4_0"></p>
     * @param error
     */
    public void debug(Throwable error);

    /**
     * <p class="changed_added_4_0"></p>
     * @return
     */
    public boolean isInfoEnabled();

    /**
     * <p class="changed_added_4_0"></p>
     * @param content
     */
    public void info(CharSequence content);

    /**
     * <p class="changed_added_4_0"></p>
     * @param content
     * @param error
     */
    public void info(CharSequence content, Throwable error);

    /**
     * <p class="changed_added_4_0"></p>
     * @param error
     */
    public void info(Throwable error);

    /**
     * <p class="changed_added_4_0"></p>
     * @return
     */
    public boolean isWarnEnabled();

    /**
     * <p class="changed_added_4_0"></p>
     * @param content
     */
    public void warn(CharSequence content);

    /**
     * <p class="changed_added_4_0"></p>
     * @param content
     * @param error
     */
    public void warn(CharSequence content, Throwable error);

    /**
     * <p class="changed_added_4_0"></p>
     * @param error
     */
    public void warn(Throwable error);

    /**
     * <p class="changed_added_4_0"></p>
     * @return
     */
    public boolean isErrorEnabled();

    /**
     * <p class="changed_added_4_0"></p>
     * @param content
     */
    public void error(CharSequence content);

    /**
     * <p class="changed_added_4_0"></p>
     * @param content
     * @param error
     */
    public void error(CharSequence content, Throwable error);

    /**
     * <p class="changed_added_4_0"></p>
     * @param error
     */
    public void error(Throwable error);

    /**
     * <p class="changed_added_4_0"></p>
     * @param level
     * @return
     */
    public boolean isLogEnabled(Level level);
    
    /**
     * <p class="changed_added_4_0"></p>
     * @param level
     * @param content
     */
    public void log(Level level,CharSequence content);

    /**
     * <p class="changed_added_4_0"></p>
     * @param level
     * @param content
     * @param error
     */
    public void log(Level level,CharSequence content, Throwable error);

    /**
     * <p class="changed_added_4_0"></p>
     * @param level
     * @param error
     */
    public void log(Level level, Throwable error);
    
}

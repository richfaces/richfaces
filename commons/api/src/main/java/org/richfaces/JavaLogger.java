/*
 * $Id: JavaLogger.java 16812 2010-04-26 20:43:19Z alexsmirnov $
 *
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
 * <p class="changed_added_4_0">That logger delegates all calls to the JDK {@link java.util.logging.Logger}</p>
 *
 * @author asmirnov@exadel.com
 */
public class JavaLogger implements Logger {
    
    public static final String RICHFACES_LOG = "org.richfaces.cdk";


    private static final String DEFAULT_MESSAGE = "Exception";

    
    private final java.util.logging.Logger jdkLogger;

    JavaLogger(String category) {
        jdkLogger = java.util.logging.Logger.getLogger(category);
    }
    
    JavaLogger() {
        this(RICHFACES_LOG);
    }
    /*
     *  (non-Javadoc)
     * @see org.richfaces.cdk.Logger#debug(java.lang.CharSequence)
     */
    public void debug(CharSequence content) {
        jdkLogger.fine(String.valueOf(content));
    }

    /*
     *  (non-Javadoc)
     * @see org.richfaces.cdk.Logger#debug(java.lang.CharSequence, java.lang.Throwable)
     */
    public void debug(CharSequence content, Throwable error) {
        jdkLogger.log(java.util.logging.Level.FINE, String.valueOf(content), error);
    }

    /*
     *  (non-Javadoc)
     * @see org.richfaces.cdk.Logger#debug(java.lang.Throwable)
     */
    public void debug(Throwable error) {
        jdkLogger.log(java.util.logging.Level.FINE, "", error);
    }

    /*
     *  (non-Javadoc)
     * @see org.richfaces.cdk.Logger#error(java.lang.CharSequence)
     */
    public void error(CharSequence content) {
        jdkLogger.severe(String.valueOf(content));
    }

    /*
     *  (non-Javadoc)
     * @see org.richfaces.cdk.Logger#error(java.lang.CharSequence, java.lang.Throwable)
     */
    public void error(CharSequence content, Throwable error) {
        
        jdkLogger.log(java.util.logging.Level.SEVERE, String.valueOf(content), error);
    }

    /*
     *  (non-Javadoc)
     * @see org.richfaces.cdk.Logger#error(java.lang.Throwable)
     */
    public void error(Throwable error) {
        
        jdkLogger.log(java.util.logging.Level.SEVERE, "", error);
    }


    /*
     *  (non-Javadoc)
     * @see org.richfaces.cdk.Logger#info(java.lang.CharSequence)
     */
    public void info(CharSequence content) {
        jdkLogger.info(String.valueOf(content));
    }

    /*
     *  (non-Javadoc)
     * @see org.richfaces.cdk.Logger#info(java.lang.CharSequence, java.lang.Throwable)
     */
    public void info(CharSequence content, Throwable error) {
        jdkLogger.log(java.util.logging.Level.INFO, String.valueOf(content), error);
    }

    /*
     *  (non-Javadoc)
     * @see org.richfaces.cdk.Logger#info(java.lang.Throwable)
     */
    public void info(Throwable error) {
        jdkLogger.log(java.util.logging.Level.INFO, "", error);
    }

    /*
     *  (non-Javadoc)
     * @see org.richfaces.cdk.Logger#isDebugEnabled()
     */
    public boolean isDebugEnabled() {
        return jdkLogger.isLoggable(java.util.logging.Level.FINE);
    }

    /*
     *  (non-Javadoc)
     * @see org.richfaces.cdk.Logger#isErrorEnabled()
     */
    public boolean isErrorEnabled() {
        return jdkLogger.isLoggable(java.util.logging.Level.SEVERE);
    }

    /*
     *  (non-Javadoc)
     * @see org.richfaces.cdk.Logger#isInfoEnabled()
     */
    public boolean isInfoEnabled() {
        return jdkLogger.isLoggable(java.util.logging.Level.INFO);
    }

    /*
     *  (non-Javadoc)
     * @see org.richfaces.cdk.Logger#isWarnEnabled()
     */
    public boolean isWarnEnabled() {
        return jdkLogger.isLoggable(java.util.logging.Level.WARNING);
    }

    /*
     *  (non-Javadoc)
     * @see org.richfaces.cdk.Logger#warn(java.lang.CharSequence)
     */
    public void warn(CharSequence content) {
        jdkLogger.warning(String.valueOf(content));
    }

    /*
     *  (non-Javadoc)
     * @see org.richfaces.cdk.Logger#warn(java.lang.CharSequence, java.lang.Throwable)
     */
    public void warn(CharSequence content, Throwable error) {
        jdkLogger.log(java.util.logging.Level.WARNING, String.valueOf(content), error);
    }

    /*
     *  (non-Javadoc)
     * @see org.richfaces.cdk.Logger#warn(java.lang.Throwable)
     */
    public void warn(Throwable error) {
        jdkLogger.log(java.util.logging.Level.WARNING, "", error);
    }

    public boolean isLogEnabled(Logger.Level level) {
        return jdkLogger.isLoggable(toJavaLevel(level));
    }

    public void log(org.richfaces.Logger.Level level, CharSequence content) {
        jdkLogger.log(toJavaLevel(level), String.valueOf(content));
        
    }

    public void log(org.richfaces.Logger.Level level, CharSequence content, Throwable error) {
        jdkLogger.log(toJavaLevel(level), String.valueOf(content),error);
    }

    public void log(org.richfaces.Logger.Level level, Throwable error) {
        jdkLogger.log(toJavaLevel(level),DEFAULT_MESSAGE, error);
    }
    
    private java.util.logging.Level toJavaLevel(Logger.Level level){
        switch (level) {
            case ERROR:
                return java.util.logging.Level.SEVERE;
            case INFO:
                return java.util.logging.Level.INFO;
            case WARNING:
                return java.util.logging.Level.WARNING;
            case DEBUG:
                return java.util.logging.Level.FINE;
            default :{
                //No level set
                throw new IllegalArgumentException("Logging level must be set to obtain a logger");
            }
        }
    }
}

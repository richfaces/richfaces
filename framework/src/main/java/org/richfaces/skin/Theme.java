/**
 *
 */
package org.richfaces.skin;

/**
 * @author asmirnov
 *
 */
public interface Theme {
    String getRendererType();

    String getStyle();

    String getScript();

    Object getProperty(String name);
}

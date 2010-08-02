
/**
 *
 */
package org.richfaces.skin;

/**
 * @author asmirnov
 *
 */
public interface Theme {
    public String getRendererType();

    public String getStyle();

    public String getScript();

    public Object getProperty(String name);
}

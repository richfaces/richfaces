package org.richfaces.log;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;

/**
 * @author Anton Belevich
 *
 */
public enum RichfacesLogger {
    RESOURCE("Resource"),
    RENDERKIT("Renderkit"),
    CONFIG("Config"),
    CONNECTION("Connection"),
    APPLICATION("Application"),
    CACHE("Cache"),
    CONTEXT("Context"),
    COMPONENTS("Components"),
    WEBAPP("Webapp"),
    UTIL("Util"),
    MODEL("Model");
    private static final String LOGGER_NAME_PREFIX = "org.richfaces.log.";
    private String loggerName;

    private RichfacesLogger(String loggerName) {
        this.loggerName = LOGGER_NAME_PREFIX + loggerName;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public Logger getLogger() {
        return LogFactory.getLogger(loggerName);
    }

    /**
     *
     * Return string which contains formated path from view root to component.
     *
     * @param component
     * @return string
     */
    public static String getComponentPath(UIComponent component) {
        StringBuilder builder = new StringBuilder("Component path: ");
        if (component == null) {
            builder.append("null");
        } else {
            getComponentPath(component, builder);
        }
        return builder.toString();
    }

    private static void getComponentPath(UIComponent component, StringBuilder builder) {
        if (component != null) {
            getComponentPath(component.getParent(), builder);
            builder.append("/").append(component.getClass().getName());
            if (component instanceof UIViewRoot) {
                builder.append("[viewId=");
                builder.append(((UIViewRoot) component).getViewId());
            } else {
                builder.append("[id=");
                builder.append(component.getId());
            }
            builder.append("]");
        }
    }
}

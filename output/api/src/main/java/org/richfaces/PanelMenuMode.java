package org.richfaces;

/**
 * @author akolonitsky
 * @since Oct 19, 2010
 */
public enum PanelMenuMode {
    ajax,
    server,
    client;

    public static final PanelMenuMode DEFAULT = ajax;
}

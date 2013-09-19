package org.richfaces;

/**
 * @author akolonitsky
 * @since Oct 19, 2010
 */
// TODO nick - having separate enum for each component is a bad idea
public enum PanelMenuMode {
    ajax,
    server,
    client;
    public static final PanelMenuMode DEFAULT = ajax;
}

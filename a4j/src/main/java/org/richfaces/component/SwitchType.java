package org.richfaces.component;

/**
 * @author akolonitsky
 * @since Jun 15, 2010
 */
public enum SwitchType {
    /**
     * value for tab change method for - client-side tabs.
     */
    client,
    /**
     * value for tab change method - server-side tabs
     */
    server,
    /**
     * value for tab change method - ajax tabs
     */
    ajax;
    public static final SwitchType DEFAULT = SwitchType.ajax;
}

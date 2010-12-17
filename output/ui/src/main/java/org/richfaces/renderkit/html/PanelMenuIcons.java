package org.richfaces.renderkit.html;

public enum PanelMenuIcons {
    none("rf-pm-none"),
    disc("rf-pm-disc"),
    grid("rf-pm-grid"),
    chevron("rf-pm-chevron"),
    chevronUp("rf-pm-chevron-up"),
    chevronDown("rf-pm-chevron-down"),
    triangle("rf-pm-triangle"),
    triangleUp("rf-pm-triangle-up"),
    triangleDown("rf-pm-triangle-down");

    public static final PanelMenuIcons DEFAULT = none;

    private final String cssClass;

    private PanelMenuIcons(String cssClass) {
        this.cssClass = cssClass;
    }

    public String cssClass() {
        return cssClass;
    }
}

package org.richfaces.renderkit.util;

public enum PanelIcons {
    none("rf-ico-none"),
    disc("rf-ico-disc"),
    grid("rf-ico-grid"),
    chevron("rf-ico-chevron"),
    chevronUp("rf-ico-chevron-up"),
    chevronDown("rf-ico-chevron-down"),
    triangle("rf-ico-triangle"),
    triangleUp("rf-ico-triangle-up"),
    triangleDown("rf-ico-triangle-down");

    public static final PanelIcons DEFAULT = none;

    private final String cssClass;

    private PanelIcons(String cssClass) {
        this.cssClass = cssClass;
    }

    public String cssClass() {
        return cssClass;
    }

    public static PanelIcons getIcon(String attrIconCollapsedValue) {
        if (attrIconCollapsedValue == null) {
            return null;
        }

        try {
            return PanelIcons.valueOf(attrIconCollapsedValue);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}

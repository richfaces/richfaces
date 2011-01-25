package org.richfaces.renderkit.util;

public enum PanelIcons {
    none("rf-ico-none", null),
    chevron("rf-ico-chevron", "rf-ico-hdr-chevron"),
    chevronLeft("rf-ico-chevron-left", "rf-ico-hdr-chevron-left"),
    chevronUp("rf-ico-chevron-up", "rf-ico-hdr-chevron-up"),
    chevronDown("rf-ico-chevron-down", "rf-ico-hdr-chevron-down"),
    disc("rf-ico-disc", "rf-ico-hdr-disc"),
    grid("rf-ico-grid", "rf-ico-hdr-grid"),
    transparent("rf-ico-transparent", "rf-ico-hdr-transparent"),
    triangle("rf-ico-triangle", "rf-ico-hdr-triangle"),
    triangleLeft("rf-ico-triangle-left", "rf-ico-hdr-triangle-left"),
    triangleUp("rf-ico-triangle-up", "rf-ico-hdr-triangle-up"),
    triangleDown("rf-ico-triangle-down", "rf-ico-hdr-triangle-down");

    public static final PanelIcons DEFAULT = none;

    private final String cssClass;

    private final String headerClass;
    
    private PanelIcons(String cssClass, String headerClass) {
        this.cssClass = cssClass;
        this.headerClass = headerClass;
    }

    public String cssClass() {
        return cssClass;
    }

    public String headerClass() {
        return headerClass;
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

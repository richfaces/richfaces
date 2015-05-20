package org.richfaces.renderkit.util;

public enum PanelIcons {

    none("rf-ico-none"),
    chevron("rf-ico-chevron"),
    chevronLeft("rf-ico-chevron-left"),
    chevronUp("rf-ico-chevron-up"),
    chevronDown("rf-ico-chevron-down"),
    disc("rf-ico-disc"),
    grid("rf-ico-grid"),
    transparent("rf-ico-transparent"),
    triangle("rf-ico-triangle"),
    triangleLeft("rf-ico-triangle-left"),
    triangleUp("rf-ico-triangle-up"),
    triangleDown("rf-ico-triangle-down");

    public enum State {
        common {
            @Override
            public String getCssClass(PanelIcons icons) {
                return icons.cssClass();
            }
        },
        commonDisabled {
            @Override
            public String getCssClass(PanelIcons icons) {
                return icons.disabledCssClass();
            }
        },
        header {
            @Override
            public String getCssClass(PanelIcons icons) {
                return icons.headerClass();
            }
        },
        headerDisabled {
            @Override
            public String getCssClass(PanelIcons icons) {
                return icons.disabledHeaderClass();
            }
        };

        public abstract String getCssClass(PanelIcons icons);
    }

    public static final PanelIcons DEFAULT = none;
    private final String cssClass;
    private final String headerClass;
    private final String disabledCssClass;
    private final String disabledHeaderClass;

    private PanelIcons(String baseClass) {
        this.cssClass = baseClass;

        this.headerClass = baseClass + " rf-ico-t-hdr";
        this.disabledCssClass = cssClass + " rf-ico-t-dis";
        this.disabledHeaderClass = headerClass + " rf-ico-t-dis";
    }

    private String cssClass() {
        return cssClass;
    }

    private String headerClass() {
        return headerClass;
    }

    private String disabledCssClass() {
        return disabledCssClass;
    }

    private String disabledHeaderClass() {
        return disabledHeaderClass;
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

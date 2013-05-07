/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.richfaces.ui.common;

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

        this.headerClass = baseClass + "-hdr";
        this.disabledCssClass = baseClass + "-dis";
        this.disabledHeaderClass = baseClass + "-hdr-dis";
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

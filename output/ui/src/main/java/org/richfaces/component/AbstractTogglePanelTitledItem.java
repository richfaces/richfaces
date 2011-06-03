/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
package org.richfaces.component;

import static org.richfaces.renderkit.html.DivPanelRenderer.capitalize;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;

/**
 * @author akolonitsky
 * @since 2010-08-05
 */
public interface AbstractTogglePanelTitledItem extends AbstractTogglePanelItemInterface {
    public enum HeaderStates {
        active("act"),
        inactive("inact"),
        disabled("dis");
        private final String abbreviation;

        HeaderStates(String abbreviation) {
            this.abbreviation = abbreviation;
        }

        public String abbreviation() {
            return abbreviation;
        }

        public String headerClass() {
            return new StringBuilder("header").append(capitalize(this.toString())).append("Class").toString();
        }
    }

    // ------------------------------------------------ Component Attributes

    @Attribute
    boolean isDisabled();

    String getHeader();

    // ------------------------------------------------ Html Attributes

    @Attribute
    String getHeaderActiveClass();

    @Attribute
    String getHeaderDisabledClass();

    @Attribute
    String getHeaderInactiveClass();

    @Attribute
    String getHeaderClass();

    @Attribute
    String getHeaderStyle();

    @Attribute
    String getContentClass();

    @Attribute(events = @EventName("headerclick"))
    String getOnheaderclick();

    @Attribute(events = @EventName("headerdblclick"))
    String getOnheaderdblclick();

    @Attribute(events = @EventName("headermousedown"))
    String getOnheadermousedown();

    @Attribute(events = @EventName("headermousemove"))
    String getOnheadermousemove();

    @Attribute(events = @EventName("headermouseup"))
    String getOnheadermouseup();
}

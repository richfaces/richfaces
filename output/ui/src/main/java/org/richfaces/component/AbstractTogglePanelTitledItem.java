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

import org.richfaces.renderkit.html.DivPanelRenderer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author akolonitsky
 * @since 2010-08-05
 */
public abstract class AbstractTogglePanelTitledItem extends UITogglePanelItem {

    public static final String COMPONENT_TYPE = "org.richfaces.TogglePanelTitledItem";

    public static final String COMPONENT_FAMILY = "org.richfaces.TogglePanelTitledItem";

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
    }

    protected AbstractTogglePanelTitledItem() {
        setRendererType("org.richfaces.TogglePanelTitledItem");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public abstract boolean isDisabled();

    public abstract String getHeader();

    @Override
    public String getName() {
        return (String) getStateHelper().eval(PropertyKeys.name, getId());
    }

    @Override
    public void encodeAll(FacesContext context) throws IOException {
        if (!isRendered()) {
            return;
        }

        encodeBegin(context);
        if (!isDisabled()) {
            if (getRendersChildren()) {
                encodeChildren(context);
            } else if (this.getChildCount() > 0) {
                for (UIComponent kid : getChildren()) {
                    kid.encodeAll(context);
                }
            }
        }
            
        encodeEnd(context);
    }

    public UIComponent getHeaderFacet(Enum<?> state) {
        return getHeaderFacet(this, state);
    }

    public static UIComponent getHeaderFacet(UIComponent component, Enum<?> state) {
        UIComponent headerFacet = null;
        if (state != null) {
            headerFacet = component.getFacet("header" + DivPanelRenderer.capitalize(state.toString()));
        }

        if (headerFacet == null) {
            headerFacet = component.getFacet("header");
        }
        return headerFacet;
    }
}

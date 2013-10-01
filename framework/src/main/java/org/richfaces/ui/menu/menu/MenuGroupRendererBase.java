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

package org.richfaces.ui.menu.menu;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.richfaces.renderkit.RendererBase;
import org.richfaces.ui.menu.dropDownMenu.AbstractDropDownMenu;

public abstract class MenuGroupRendererBase extends RendererBase {
    public static final String RENDERER_TYPE = "org.richfaces.ui.MenuGroupRenderer";
    public static final int DEFAULT_MIN_POPUP_WIDTH = 250;

    protected boolean isDisabled(FacesContext facesContext, UIComponent component) {
        if (component instanceof AbstractMenuGroup) {
            return ((AbstractMenuGroup) component).isDisabled();
        }
        return false;
    }

    @Override
    public void renderChildren(FacesContext facesContext, UIComponent component) throws IOException {
        AbstractMenuGroup menuGroup = (AbstractMenuGroup) component;

        for (UIComponent child : menuGroup.getChildren()) {
            if (child.isRendered()) {
                child.encodeAll(facesContext);
            }
        }
    }

    protected UIComponent getIconFacet(FacesContext facesContext, UIComponent component) {
        UIComponent facet = null;
        AbstractMenuGroup menuGroup = (AbstractMenuGroup) component;
        if (menuGroup != null) {

            if (menuGroup.isDisabled()) {
                facet = menuGroup.getFacet(AbstractMenuGroup.Facets.iconDisabled.toString());
            } else {
                facet = menuGroup.getFacet(AbstractMenuGroup.Facets.icon.toString());
            }
        }
        return facet;
    }

    protected String getIconAttribute(FacesContext facesContext, UIComponent component) {
        String icon = null;
        AbstractMenuGroup menuGroup = (AbstractMenuGroup) component;
        if (menuGroup != null) {

            if (menuGroup.isDisabled()) {
                icon = menuGroup.getIconDisabled();
            } else {
                icon = menuGroup.getIcon();
            }
        }
        return icon;
    }

    protected String getStyleClass(FacesContext facesContext, UIComponent component, String styleDDMenu, String styleMenuGroup) {
        UIComponent ddMenu = getDDMenu(facesContext, component);
        String styleClass = "";
        if (ddMenu != null) {
            if (ddMenu.getAttributes().get(styleDDMenu) != null) {
                styleClass = ddMenu.getAttributes().get(styleDDMenu).toString();
            }
        }

        return concatClasses(styleClass, component.getAttributes().get(styleMenuGroup));
    }

    protected UIComponent getDDMenu(FacesContext facesContext, UIComponent component) {
        UIComponent parent = component.getParent();
        while (parent != null) {
            if (parent instanceof AbstractDropDownMenu) {
                return parent;
            }
            parent = parent.getParent();
        }
        return null;
    }

    protected int getMinPopupWidth(FacesContext facesContext, UIComponent component) {
        UIComponent parent = getDDMenu(facesContext, component);
        int width = 0;
        if (parent != null) {
            width = ((AbstractDropDownMenu) parent).getPopupWidth();
            if (width <= 0) {
                width = DEFAULT_MIN_POPUP_WIDTH;
            }
        }
        return width;
    }

    /**
     * It is introduced due to RF-10004 CDK: isEmpty method is generated incorrectly
     *
     * @param str
     */
    protected boolean isStringEmpty(String str) {
        if (str != null && str.trim().length() > 0) {
            return false;
        }
        return true;
    }
}

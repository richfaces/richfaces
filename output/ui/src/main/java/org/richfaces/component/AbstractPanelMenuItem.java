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

import org.richfaces.PanelMenuMode;

import javax.faces.component.UIComponent;

/**
 * @author akolonitsky
 * @since 2010-10-25
 */
public abstract class AbstractPanelMenuItem extends AbstractActionComponent {

    public static final String COMPONENT_TYPE = "org.richfaces.PanelMenuItem";

    public static final String COMPONENT_FAMILY = "org.richfaces.PanelMenuItem";

    protected AbstractPanelMenuItem() {
        setRendererType("org.richfaces.PanelMenuItem");
    }

    public boolean isTopItem() {
        return getParentItem() instanceof AbstractPanelMenu;
    }

    //TODO nick - this can be replaced with ComponentIterators.parents(UIComponent) + Iterators.find(...)
    public AbstractPanelMenu getPanelMenu() { // TODO refactor
        UIComponent parentItem = getParent();
        while (parentItem != null) {
            if (parentItem instanceof AbstractPanelMenu) {
                return (AbstractPanelMenu) parentItem;
            }

            parentItem = parentItem.getParent();
        }

        return null;
    }

    //TODO nick - this can be replaced with ComponentIterators.parents(UIComponent) + Iterators.find(...)
    public UIComponent getParentItem() {
        UIComponent parentItem = getParent();
        while (parentItem != null) {
            if (parentItem instanceof AbstractPanelMenuGroup
                || parentItem instanceof AbstractPanelMenu) {
                return parentItem;
            }

            parentItem = parentItem.getParent();
        }

        return null;
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public abstract PanelMenuMode getMode();

    public abstract String getLabel();

    public abstract String getName();

    public abstract boolean isDisabled();

    public abstract boolean isLimitRender();

    public abstract Object getData();

    public abstract String getStatus();

    public abstract Object getExecute();

    public abstract Object getRender();
}

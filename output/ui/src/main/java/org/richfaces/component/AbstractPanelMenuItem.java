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

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;

import org.richfaces.PanelMenuMode;
import org.richfaces.event.ItemChangeListener;
import org.richfaces.event.ItemChangeSource;

/**
 * @author akolonitsky
 * @since 2010-10-25
 */
//TODO nick - use org.richfaces.component.AbstractActionComponent as super class
public abstract class AbstractPanelMenuItem extends UICommand implements ItemChangeSource{

    public static final String COMPONENT_TYPE = "org.richfaces.PanelMenuItem";

    public static final String COMPONENT_FAMILY = "org.richfaces.PanelMenuItem";

    //TODO nick - move this enum to renderkit package
    public enum Icons {
        none("rf-pm-none"),
        disc("rf-pm-disc"),
        grid("rf-pm-grid"),
        chevron("rf-pm-chevron"),
        chevronUp("rf-pm-chevron-up"),
        chevronDown("rf-pm-chevron-down"),
        triangle("rf-pm-triangle"),
        triangleUp("rf-pm-triangle-up"),
        triangleDown("rf-pm-triangle-down");

        public static final Icons DEFAULT = none;

        private final String cssClass;

        Icons(String cssClass) {
            this.cssClass = cssClass;
        }

        //TODO nick - should this become getCssClass()?
        public String cssClass() {
            return cssClass;
        }
    }

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

    public abstract boolean isBypassUpdates();

    public abstract boolean isDisableImplicitRender();

    public abstract Object getData();

    public abstract String getStatus();

    public abstract Object getExecute();

    public abstract Object getRender();

    // ------------------------------------------------ Event Processing Methods

    //TODO nick - it seems these listeners are never triggered
    public void addItemChangeListener(ItemChangeListener listener) {
        addFacesListener(listener);
    }

    public ItemChangeListener[] getItemChangeListeners() {
        return (ItemChangeListener[]) getFacesListeners(ItemChangeListener.class);
    }

    public void removeItemChangeListener(ItemChangeListener listener) {
        removeFacesListener(listener);
    }

}

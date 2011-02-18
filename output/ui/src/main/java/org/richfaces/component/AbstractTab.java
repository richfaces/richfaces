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

import org.richfaces.cdk.annotations.*;
import org.richfaces.renderkit.html.DivPanelRenderer;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;

/**
 * @author akolonitsky
 * @since 2010-10-19
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets), renderer = @JsfRenderer(type = "org.richfaces.TabRenderer"))
public abstract class AbstractTab extends AbstractActionComponent implements AbstractTogglePanelTitledItem, AjaxProps, ClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "org.richfaces.Tab";

    public static final String COMPONENT_FAMILY = "org.richfaces.Tab";

    public AbstractTab() {
        setRendererType("org.richfaces.TabRenderer");
    }

    // ------------------------------------------------ Html Attributes
    enum Properties {
        headerDisabledClass, 
        headerInactiveClass, 
        headerClass, 
        contentClass, 
        execute, 
        headerActiveClass, 
        header,
        switchType
    }

    @Attribute(generate = false)
    public String getHeaderActiveClass() {
        String value = (String) getStateHelper().eval(Properties.headerActiveClass);
        if (value != null) {
            return value;
        }

        return getTabPanel().getTabActiveHeaderClass();
    }

    public void setHeaderActiveClass(String headerActiveClass) {
        getStateHelper().put(Properties.headerActiveClass, headerActiveClass);
    }


    @Attribute(generate = false)
    public String getHeaderDisabledClass() {
        String value = (String) getStateHelper().eval(Properties.headerDisabledClass);
        if (value != null) {
            return value;
        }

        return getTabPanel().getTabDisabledHeaderClass();
    }

    public void setHeaderDisabledClass(String headerDisabledClass) {
        getStateHelper().put(Properties.headerDisabledClass, headerDisabledClass);
    }

    @Attribute(generate = false)
    public String getHeaderInactiveClass() {
        String value = (String) getStateHelper().eval(Properties.headerInactiveClass);
        if (value != null) {
            return value;
        }

        return getTabPanel().getTabInactiveHeaderClass();
    }

    public void setHeaderInactiveClass(String headerInactiveClass) {
        getStateHelper().put(Properties.headerInactiveClass, headerInactiveClass);
    }

    @Attribute(generate = false)
    public String getHeaderClass() {
        String value = (String) getStateHelper().eval(Properties.headerClass);
        if (value != null) {
            return value;
        }

        return getTabPanel().getTabHeaderClass();
    }

    public void setHeaderClass(String headerClass) {
        getStateHelper().put(Properties.headerClass, headerClass);
    }

    @Attribute(generate = false)
    public String getContentClass() {
        String value = (String) getStateHelper().eval(Properties.contentClass);
        if (value != null) {
            return value;
        }

        return getTabPanel().getTabContentClass();
    }

    public void setContentClass(String contentClass) {
        getStateHelper().put(Properties.contentClass, contentClass);
    }

    @Attribute(generate = false)
    public Object getExecute() {
        Object execute = getStateHelper().eval(Properties.execute);
        if (execute == null) {
            execute = "";
        }
        return execute + " " + getTabPanel().getId();
    }

    public void setExecute(Object execute) {
        getStateHelper().put(Properties.execute, execute);
    }


    /////////////////////////////////////////////////////////////////////////

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

    // ------------------------------------------------ Component Attributes

    @Attribute(generate = false)
    public String getHeader() {
        return (String) getStateHelper().eval(Properties.header, getName());
    }

    public void setHeader(String header) {
        getStateHelper().put(Properties.header, header);
    }

    // ------------------------------------------------ AbstractTogglePanelItemInterface

    public AbstractTabPanel getParentPanel() {
        return ComponentIterators.getParent(this, AbstractTabPanel.class);
    }

    public AbstractTabPanel getTabPanel() {
        return getParentPanel();
    }

    public boolean isActive() {
        return getTabPanel().isActiveItem(this);
    }

    public boolean shouldProcess() {
        return isActive() || getSwitchType() == SwitchType.client;
    }

    @Attribute(generate = false)
    public String getName() {
        return (String) getStateHelper().eval(AbstractTogglePanelItem.NAME, getId());
    }

    public void setName(String name) {
        getStateHelper().put(AbstractTogglePanelItem.NAME, name);
    }

    public String toString() {
        return "TogglePanelItem {name: " + getName() + ", switchType: " + getSwitchType() + '}';
    }
    
    @Attribute(generate = false)
    public SwitchType getSwitchType() {
        SwitchType switchType = (SwitchType) getStateHelper().eval(Properties.switchType);
        if (switchType == null) {
            switchType = getParentPanel().getSwitchType();
        }
        if (switchType == null) {
            switchType = SwitchType.DEFAULT;
        }
        return switchType; 
    }

    public void setSwitchType(SwitchType switchType) {
        getStateHelper().put(Properties.switchType, switchType);
    }    
}


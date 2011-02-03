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
import org.richfaces.cdk.annotations.*;

import javax.faces.component.UIComponent;

/**
 * @author akolonitsky
 * @since 2010-10-25
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets))
public abstract class AbstractPanelMenuItem extends AbstractActionComponent {

    public static final String COMPONENT_TYPE = "org.richfaces.PanelMenuItem";

    public static final String COMPONENT_FAMILY = "org.richfaces.PanelMenuItem";

    protected AbstractPanelMenuItem() {
        setRendererType("org.richfaces.PanelMenuItemRenderer");
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

    // ------------------------------------------------ Component Attributes

    @Attribute(defaultValue = "Boolean.TRUE")
    public abstract Boolean isSelectable();

    @Attribute(defaultValue = "Boolean.FALSE", hidden = true)
    public abstract Boolean isUnselectable();

    @Attribute(defaultValue = "getPanelMenu().getItemMode()")
    public abstract PanelMenuMode getMode();

    @Attribute(generate = false)
    public String getName() {
        return (String) getStateHelper().eval(Properties.name, getId());
    }

    public void setName(String name) {
        getStateHelper().put(Properties.name, name);
    }

    @Attribute
    public abstract String getLabel();

    @Attribute
    public abstract boolean isDisabled();

    @Attribute
    public abstract boolean isLimitRender();

    @Attribute
    public abstract Object getData();

    @Attribute
    public abstract String getStatus();

    @Attribute
    public abstract Object getExecute();

    @Attribute
    public abstract Object getRender();

    // ------------------------------------------------ Html Attributes
    enum Properties {
        leftIcon, leftDisabledIcon, iconRight, rightDisabledIcon, styleClass, disabledClass, name

    }

    @Attribute(generate = false)
    public String getDisabledClass() {
        return (String) getStateHelper().eval(Properties.disabledClass,
                isTopItem() ? getPanelMenu().getTopItemDisabledClass() : getPanelMenu().getItemDisabledClass());
    }

    public void setDisabledClass(String disabledClass) {
        getStateHelper().put(Properties.disabledClass, disabledClass);
    }

    @Attribute(generate = false)
    public String getLeftIcon() {
        return (String) getStateHelper().eval(Properties.leftIcon,
                isTopItem() ? getPanelMenu().getTopItemLeftIcon() : getPanelMenu().getItemLeftIcon());
    }

    public void setLeftIcon(String leftIcon) {
        getStateHelper().put(Properties.leftIcon, leftIcon);
    }

    @Attribute
    public abstract String getLeftIconClass();

    @Attribute(generate = false)
    public String getLeftDisabledIcon() {
        return (String) getStateHelper().eval(Properties.leftDisabledIcon,
                isTopItem() ? getPanelMenu().getTopItemDisabledLeftIcon() : getPanelMenu().getItemDisabledLeftIcon());
    }

    public void setLeftDisabledIcon(String leftDisabledIcon) {
        getStateHelper().put(Properties.leftDisabledIcon, leftDisabledIcon);
    }

    @Attribute(generate = false)
    public String getRightIcon() {
        return (String) getStateHelper().eval(Properties.iconRight,
                isTopItem() ? getPanelMenu().getTopItemRightIcon() : getPanelMenu().getItemRightIcon());
    }

    public void setRightIcon(String iconRight) {
        getStateHelper().put(Properties.iconRight, iconRight);
    }

    @Attribute
    public abstract String getRightIconClass();

    @Attribute(generate = false)
    public String getRightDisabledIcon() {
        return (String) getStateHelper().eval(Properties.rightDisabledIcon,
                isTopItem() ? getPanelMenu().getTopItemDisabledRightIcon() : getPanelMenu().getItemDisabledRightIcon());
    }

    public void setRightDisabledIcon(String rightDisabledIcon) {
        getStateHelper().put(Properties.rightDisabledIcon, rightDisabledIcon);
    }

    @Attribute
    public abstract String getStyle();

    @Attribute(generate = false)
    public String getStyleClass() {
        return (String) getStateHelper().eval(Properties.styleClass,
                isTopItem() ? getPanelMenu().getTopItemClass() : getPanelMenu().getItemClass());
    }

    public void setStyleClass(String styleClass) {
        getStateHelper().put(Properties.styleClass, styleClass);
    }

    @Attribute(events = @EventName("beforedomupdate"))
    public abstract String getOnbeforedomupdate();

    @Attribute(events = @EventName("complete"))
    public abstract String getOncomplete();

    @Attribute(events = @EventName("click"))
    public abstract String getOnclick();

    @Attribute(events = @EventName("dblclick"))
    public abstract String getOndblclick();

    @Attribute(events = @EventName("mousedown"))
    public abstract String getOnmousedown();

    @Attribute(events = @EventName("mousemove"))
    public abstract String getOnmousemove();

    @Attribute(events = @EventName("mouseout"))
    public abstract String getOnmouseout();

    @Attribute(events = @EventName("mouseover"))
    public abstract String getOnmouseover();

    @Attribute(events = @EventName("mouseup"))
    public abstract String getOnmouseup();

    @Attribute(events = @EventName("unselect"))
    public abstract String getOnunselect();

    @Attribute(events = @EventName("select"))
    public abstract String getOnselect();

    @Attribute(events = @EventName("beforeselect"))
    public abstract String getOnbeforeselect();
}

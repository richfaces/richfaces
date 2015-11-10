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

import javax.faces.component.UIComponent;

import org.richfaces.PanelMenuMode;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.component.attribute.AjaxProps;

import com.google.common.base.Predicate;

import org.richfaces.component.attribute.BypassProps;
import org.richfaces.component.attribute.DisabledProps;
import org.richfaces.component.attribute.EventsMouseProps;
import org.richfaces.component.attribute.StyleClassProps;
import org.richfaces.component.attribute.StyleProps;

/**
 * <p>The &lt;rich:panelMenuItem&gt; component represents a single item inside a &lt;rich:panelMenuGroup&gt; component,
 * which is in turn part of a &lt;rich:panelMenu&gt; component.</p>
 *
 * @author akolonitsky
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets))
public abstract class AbstractPanelMenuItem extends AbstractActionComponent implements AjaxProps, BypassProps, DisabledProps, EventsMouseProps, StyleProps, StyleClassProps {
    public static final String COMPONENT_TYPE = "org.richfaces.PanelMenuItem";
    public static final String COMPONENT_FAMILY = "org.richfaces.PanelMenuItem";
    private static final ParentItemPredicate PARENT_ITEM_PREDICATE = new ParentItemPredicate();
    private static final DisabledParentItemPredicate DISABLED_PARENT_ITEM_PREDICATE = new DisabledParentItemPredicate();

    protected AbstractPanelMenuItem() {
        setRendererType("org.richfaces.PanelMenuItemRenderer");
    }

    public boolean isActiveItem() {
        return this.getName().equals(this.getPanelMenu().getActiveItem());
    }

    public boolean isTopItem() {
        return getParentItem() instanceof AbstractPanelMenu;
    }

    public AbstractPanelMenu getPanelMenu() {
        return ComponentIterators.getParent(this, AbstractPanelMenu.class);
    }

    public UIComponent getParentItem() {
        return ComponentIterators.getParent(this, PARENT_ITEM_PREDICATE);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    // ------------------------------------------------ Component Attributes

    /**
     * Defines whenever the item is selectable. Default: true.
     */
    @Attribute(defaultValue = "Boolean.TRUE")
    public abstract Boolean getSelectable();

    @Attribute(defaultValue = "Boolean.FALSE", hidden = true)
    public abstract Boolean getUnselectable();

    /**
     * Mode used for selecting of this item: default value is inherited from panelMenu
     */
    @Attribute(defaultValue = "getPanelMenu().getItemMode()")
    public abstract PanelMenuMode getMode();

    /**
     * The name of this component
     */
    @Attribute(generate = false)
    public String getName() {
        return (String) getStateHelper().eval(Properties.name, getId());
    }

    public void setName(String name) {
        getStateHelper().put(Properties.name, name);
    }

    /**
     * A localized user presentable name for this component.
     */
    @Attribute
    public abstract String getLabel();

    public boolean isParentDisabled() {
        return ComponentIterators.getParent(this, DISABLED_PARENT_ITEM_PREDICATE) != null;
    }

    @Attribute(generate = false)
    public Object getExecute() {
        Object execute = getStateHelper().eval(Properties.execute);
        if (execute == null) {
            execute = "";
        }
        return execute + " " + getPanelMenu().getId();
    }

    public void setExecute(Object execute) {
        getStateHelper().put(Properties.execute, execute);
    }

    // ------------------------------------------------ Html Attributes
    enum Properties {
        leftIcon, leftDisabledIcon, rightIcon, rightDisabledIcon, styleClass, disabledClass, execute, name, value
    }

    /**
     * Space-separated list of CSS style class(es) to be applied to the panel menu item when it is disabled.
     */
    @Attribute(generate = false)
    public String getDisabledClass() {
        return (String) getStateHelper().eval(Properties.disabledClass,
                isTopItem() ? getPanelMenu().getTopItemDisabledClass() : getPanelMenu().getItemDisabledClass());
    }

    public void setDisabledClass(String disabledClass) {
        getStateHelper().put(Properties.disabledClass, disabledClass);
    }

    /**
     * The icon displayed on the left of the menu item label
     */
    @Attribute(generate = false)
    public String getLeftIcon() {
        return (String) getStateHelper().eval(Properties.leftIcon,
                isTopItem() ? getPanelMenu().getTopItemLeftIcon() : getPanelMenu().getItemLeftIcon());
    }

    public void setLeftIcon(String leftIcon) {
        getStateHelper().put(Properties.leftIcon, leftIcon);
    }

    /**
     * Space-separated list of CSS style class(es) to be applied to the left icon of the panel menu item.
     */
    @Attribute
    public abstract String getLeftIconClass();

    /**
     * The icon displayed on the left of the menu item when it is disabled
     */
    @Attribute(generate = false)
    public String getLeftDisabledIcon() {
        return (String) getStateHelper().eval(Properties.leftDisabledIcon,
                isTopItem() ? getPanelMenu().getTopItemDisabledLeftIcon() : getPanelMenu().getItemDisabledLeftIcon());
    }

    public void setLeftDisabledIcon(String leftDisabledIcon) {
        getStateHelper().put(Properties.leftDisabledIcon, leftDisabledIcon);
    }

    /**
     * The icon displayed on the right of the menu item label
     */
    @Attribute(generate = false)
    public String getRightIcon() {
        return (String) getStateHelper().eval(Properties.rightIcon,
                isTopItem() ? getPanelMenu().getTopItemRightIcon() : getPanelMenu().getItemRightIcon());
    }

    public void setRightIcon(String iconRight) {
        getStateHelper().put(Properties.rightIcon, iconRight);
    }

    /**
     * Space-separated list of CSS style class(es) to be applied to the right icon of the panel menu item.
     */
    @Attribute
    public abstract String getRightIconClass();

    /**
     * The icon displayed on the right of the menu item when it is disabled
     */
    @Attribute(generate = false)
    public String getRightDisabledIcon() {
        return (String) getStateHelper().eval(Properties.rightDisabledIcon,
                isTopItem() ? getPanelMenu().getTopItemDisabledRightIcon() : getPanelMenu().getItemDisabledRightIcon());
    }

    public void setRightDisabledIcon(String rightDisabledIcon) {
        getStateHelper().put(Properties.rightDisabledIcon, rightDisabledIcon);
    }

    @Attribute(generate = false)
    public String getStyleClass() {
        return (String) getStateHelper().eval(Properties.styleClass,
                isTopItem() ? getPanelMenu().getTopItemClass() : getPanelMenu().getItemClass());
    }

    public void setStyleClass(String styleClass) {
        getStateHelper().put(Properties.styleClass, styleClass);
    }

    /**
     * The client-side script method to be called after the menu group is selected
     */
    @Attribute(events = @EventName("select"))
    public abstract String getOnselect();

    /**
     * The client-side script method to be called before the menu group is selected
     */
    @Attribute(events = @EventName("beforeselect"))
    public abstract String getOnbeforeselect();

    private static class ParentItemPredicate implements Predicate<UIComponent> {
        public boolean apply(UIComponent comp) {
            return comp instanceof AbstractPanelMenuGroup || comp instanceof AbstractPanelMenu;
        }
    }

    private static class DisabledParentItemPredicate implements Predicate<UIComponent> {
        public boolean apply(UIComponent comp) {
            if (comp instanceof AbstractPanelMenuGroup && ((AbstractPanelMenuGroup)comp).isDisabled()) {
                return true;
            }
            if (comp instanceof AbstractPanelMenu && ((AbstractPanelMenu)comp).isDisabled()) {
                return true;
            }
            return false;
        }
    }

    @Attribute(generate = false, hidden = true)
    public Object getValue() {
        return getStateHelper().eval(Properties.value);
    }

    public void setValue(Object value) {
        getStateHelper().put(Properties.value, value);
    }
}

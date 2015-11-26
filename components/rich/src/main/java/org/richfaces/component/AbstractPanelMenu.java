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

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;

import org.richfaces.PanelMenuMode;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.component.attribute.DisabledProps;
import org.richfaces.component.attribute.EventsMouseProps;
import org.richfaces.component.attribute.ImmediateProps;
import org.richfaces.component.attribute.StyleClassProps;
import org.richfaces.component.attribute.StyleProps;
import org.richfaces.event.ItemChangeEvent;
import org.richfaces.event.ItemChangeListener;
import org.richfaces.event.ItemChangeSource;
import org.richfaces.renderkit.util.PanelIcons;
import org.richfaces.view.facelets.html.PanelMenuTagHandler;

/**
 * <p>The &lt;rich:panelMenu&gt; component is used in conjunction with &lt;rich:panelMenuItem&gt; and
 * &lt;rich:panelMenuGroup&gt; to create an expanding, hierarchical menu. The &lt;rich:panelMenu&gt; component's
 * appearance can be highly customized, and the hierarchy can stretch to any number of sub-levels.</p>
 *
 * @author akolonitsky
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets, handlerClass = PanelMenuTagHandler.class),
        renderer = @JsfRenderer(type = "org.richfaces.PanelMenuRenderer"))
public abstract class AbstractPanelMenu extends UIOutput implements ItemChangeSource, DisabledProps, EventsMouseProps, ImmediateProps, StyleProps, StyleClassProps {
    public static final String COMPONENT_TYPE = "org.richfaces.PanelMenu";
    public static final String COMPONENT_FAMILY = "org.richfaces.PanelMenu";
    private String submittedActiveItem;

    private enum PropertyKeys {
        immediate
    }

    protected AbstractPanelMenu() {
        setRendererType("org.richfaces.PanelMenuRenderer");
    }

    @Override
    public void processDecodes(FacesContext context) {
        super.processDecodes(context);

        // TODO nick - is component immediate = true only?
        // TODO nick - processValue should be executed in context of component, i.e. when 'component' EL variable is set

        ItemChangeEvent event = createItemChangeEvent(context);
        if (event != null) {
            event.queue();
        }
    }

    public void queueEvent(FacesEvent event) {
        if ((event instanceof ItemChangeEvent) && (event.getComponent() == this)) {
            setEventPhase((ItemChangeEvent) event);
        }
        super.queueEvent(event);
    }

    public void setEventPhase(FacesEvent event) {
        if (event instanceof ItemChangeEvent) {
            AbstractPanelMenuItem actItm = (AbstractPanelMenuItem) ((ItemChangeEvent) event).getNewItem();
            if (isImmediate() || (actItm != null && actItm.isImmediate())) {
                event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
            } else if (actItm != null && actItm.isBypassUpdates()) {
                event.setPhaseId(PhaseId.PROCESS_VALIDATIONS);
            } else {
                event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
            }
        }
    }

    private ItemChangeEvent createItemChangeEvent(FacesContext context) {

        // Submitted value == null means "the component was not submitted at all".
        String activeItem = getSubmittedActiveItem();
        if (activeItem == null) {
            return null;
        }

        String previous = (String) getValue();
        if (previous == null || !previous.equalsIgnoreCase(activeItem)) {
            AbstractPanelMenuItem prevItm = null;
            AbstractPanelMenuItem actItm = null;
            if (previous != null) {
                prevItm = getItem(previous);
            }
            if (activeItem != null) {
                actItm = getItem(activeItem);
            }

            return new ItemChangeEvent(this, previous, prevItm, activeItem, actItm);
        }
        return null;
    }

    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        if (event instanceof ItemChangeEvent) {
            setValue(((ItemChangeEvent) event).getNewItemName());
            setSubmittedActiveItem(null);
            if (event.getPhaseId() != PhaseId.UPDATE_MODEL_VALUES) {
                FacesContext.getCurrentInstance().renderResponse();
            }
        }
        super.broadcast(event);
    }

    public String getSubmittedActiveItem() {
        return this.submittedActiveItem;
    }

    public void setSubmittedActiveItem(String submittedValue) {
        this.submittedActiveItem = String.valueOf(submittedValue);
    }

    @Attribute(hidden = true)
    public abstract Converter getConverter();

    /**
     * Holds the active panel name. This name is a reference to the name identifier of the active child
     * &lt;rich:panelMenuItem&gt; or &lt;rich:panelMenuGroup&gt; component.
     */
    @Attribute(generate = false)
    public String getActiveItem() {
        return (String) getValue();
    }

    // TODO nick - where is EL-expression updated?
    public void setActiveItem(String value) {
        setValue(value);
    }

    @Override
    public void setValueExpression(String name, ValueExpression binding) {
        if ("activeItem".equals(name)) {
            super.setValueExpression("value", binding);
        } else {
            super.setValueExpression(name, binding);
        }
    }

    @Attribute(generate = false)
    public boolean isImmediate() {
        return (Boolean) getStateHelper().eval(PropertyKeys.immediate, false);
    }

    public void setImmediate(boolean immediate) {
        getStateHelper().put(PropertyKeys.immediate, immediate);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    // ------------------------------------------------ Component Attributes

    /**
     * The mouse event used for expansion.
     */
    @Attribute
    public abstract String getExpandEvent();

    /**
     * The mouse event used for collapsing.
     */
    @Attribute
    public abstract String getCollapseEvent();

    /**
     * Mode used for expanding/collapsing groups: client (default), ajax, server
     */
    @Attribute(defaultValue = "PanelMenuMode.client")
    public abstract PanelMenuMode getGroupMode();

    /**
     * If true (default), only one group can be expanded at the time. If false, many groups can be expanded.
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isExpandSingle();

    /**
     * The mode user for selecting items: client, ajax (default), server
     */
    @Attribute(defaultValue = "PanelMenuMode.DEFAULT")
    public abstract PanelMenuMode getItemMode();

    /**
     * If true (default), selection of any item of group will cause selection of groups - predecessors - in the hierarchy. If
     * false, only given item is selected.
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isBubbleSelection();

    /**
     * Method expression referencing a method that will be called when an ItemChangeEvent has been broadcast for the listener.
     */
    @Attribute
    public abstract MethodExpression getItemChangeListener();

    // ------------------------------------------------ Html Attributes
    enum Properties {
        itemRightIcon, itemDisabledLeftIcon, itemDisabledRightIcon, topItemLeftIcon, topItemRightIcon, topItemDisabledLeftIcon, topItemDisabledRightIcon, groupExpandedLeftIcon, groupExpandedRightIcon, groupCollapsedLeftIcon, groupCollapsedRightIcon, groupDisabledLeftIcon, groupDisabledRightIcon, topGroupExpandedLeftIcon, topGroupExpandedRightIcon, topGroupCollapsedLeftIcon, topGroupCollapsedRightIcon, topGroupDisabledLeftIcon, topGroupDisabledRightIcon, itemLeftIcon, value,
        topItemClass, topItemDisabledClass, topGroupClass, topGroupDisabledClass
    }

    /**
     * The width of the panel menu in pixels.
     */
    @Attribute
    public abstract String getWidth();

    /**
     * Space-separated list of CSS style class(es) to be applied to the panel menu items.
     */
    @Attribute
    public abstract String getItemClass();

    /**
     * Space-separated list of CSS style class(es) to be applied to disables panel menu items.
     */
    @Attribute
    public abstract String getItemDisabledClass();

    /**
     * The left icon for panel menu items
     */
    @Attribute(generate = false)
    public String getItemLeftIcon() {
        return (String) getStateHelper().eval(Properties.itemLeftIcon, PanelIcons.transparent.toString());
    }

    public void setItemLeftIcon(String itemLeftIcon) {
        getStateHelper().put(Properties.itemLeftIcon, itemLeftIcon);
    }

    /**
     * The left icon for right menu items
     */
    @Attribute(generate = false)
    public String getItemRightIcon() {
        return (String) getStateHelper().eval(Properties.itemRightIcon, PanelIcons.transparent.toString());
    }

    public void setItemRightIcon(String itemRightIcon) {
        getStateHelper().put(Properties.itemRightIcon, itemRightIcon);
    }

    /**
     * The left icon for disabled panel menu items
     */
    @Attribute(generate = false)
    public String getItemDisabledLeftIcon() {
        return (String) getStateHelper().eval(Properties.itemDisabledLeftIcon, PanelIcons.transparent.toString());
    }

    public void setItemDisabledLeftIcon(String itemDisabledLeftIcon) {
        getStateHelper().put(Properties.itemDisabledLeftIcon, itemDisabledLeftIcon);
    }

    /**
     * The right icon for panel menu items
     */
    @Attribute(generate = false)
    public String getItemDisabledRightIcon() {
        return (String) getStateHelper().eval(Properties.itemDisabledRightIcon, PanelIcons.transparent.toString());
    }

    public void setItemDisabledRightIcon(String itemDisabledRightIcon) {
        getStateHelper().put(Properties.itemDisabledRightIcon, itemDisabledRightIcon);
    }

    /**
     * Space-separated list of CSS style class(es) to be applied to top-level panel menu items
     */
    @Attribute
    public String getTopItemClass() {
        return (String) getStateHelper().eval(Properties.topItemClass, getItemClass());
    }

    public void setTopItemClass(String topItemClass) {
        getStateHelper().put(Properties.topItemClass, topItemClass);
    }

    /**
     * Space-separated list of CSS style class(es) to be applied to disabled top-level panel menu items
     */
    @Attribute
    public String getTopItemDisabledClass() {
        return (String) getStateHelper().eval(Properties.topItemDisabledClass, getItemDisabledClass());
    }

    public void setTopItemDisabledClass(String topItemDisabledClass) {
        getStateHelper().put(Properties.topItemDisabledClass, topItemDisabledClass);
    }

    /**
     * The left icon for top-level panel menu items
     */
    @Attribute(generate = false)
    public String getTopItemLeftIcon() {
        return (String) getStateHelper().eval(Properties.topItemLeftIcon, getItemLeftIcon());
    }

    public void setTopItemLeftIcon(String topItemLeftIcon) {
        getStateHelper().put(Properties.topItemLeftIcon, topItemLeftIcon);
    }

    /**
     * The right icon for top-level panel menu items
     */
    @Attribute(generate = false)
    public String getTopItemRightIcon() {
        return (String) getStateHelper().eval(Properties.topItemRightIcon, getItemRightIcon());
    }

    public void setTopItemRightIcon(String topItemRightIcon) {
        getStateHelper().put(Properties.topItemRightIcon, topItemRightIcon);
    }

    /**
     * The left icon for disabled top-level panel menu items
     */
    @Attribute(generate = false)
    public String getTopItemDisabledLeftIcon() {
        return (String) getStateHelper().eval(Properties.topItemDisabledLeftIcon, getItemDisabledLeftIcon());
    }

    public void setTopItemDisabledLeftIcon(String topItemDisabledLeftIcon) {
        getStateHelper().put(Properties.topItemDisabledLeftIcon, topItemDisabledLeftIcon);
    }

    /**
     * The right icon for disabled top-level panel menu items
     */
    @Attribute(generate = false)
    public String getTopItemDisabledRightIcon() {
        return (String) getStateHelper().eval(Properties.topItemDisabledRightIcon, getItemDisabledRightIcon());
    }

    public void setTopItemDisabledRightIcon(String topItemDisabledRightIcon) {
        getStateHelper().put(Properties.topItemDisabledRightIcon, topItemDisabledRightIcon);
    }

    /**
     * Space-separated list of CSS style class(es) to be applied to panel menu groups
     */
    @Attribute
    public abstract String getGroupClass();

    /**
     * Space-separated list of CSS style class(es) to be applied to disabled panel menu groups
     */
    @Attribute
    public abstract String getGroupDisabledClass();

    /**
     * The left icon for expanded panel menu groups
     */
    @Attribute(generate = false)
    public String getGroupExpandedLeftIcon() {
        return (String) getStateHelper().eval(Properties.groupExpandedLeftIcon, PanelIcons.transparent.toString());
    }

    public void setGroupExpandedLeftIcon(String groupExpandedLeftIcon) {
        getStateHelper().put(Properties.groupExpandedLeftIcon, groupExpandedLeftIcon);
    }

    /**
     * The right icon for expanded panel menu groups
     */
    @Attribute(generate = false)
    public String getGroupExpandedRightIcon() {
        return (String) getStateHelper().eval(Properties.groupExpandedRightIcon, PanelIcons.transparent.toString());
    }

    public void setGroupExpandedRightIcon(String groupExpandedRightIcon) {
        getStateHelper().put(Properties.groupExpandedRightIcon, groupExpandedRightIcon);
    }

    /**
     * The left icon for collapsed panel menu groups
     */
    @Attribute(generate = false)
    public String getGroupCollapsedLeftIcon() {
        return (String) getStateHelper().eval(Properties.groupCollapsedLeftIcon, PanelIcons.transparent.toString());
    }

    public void setGroupCollapsedLeftIcon(String groupCollapsedLeftIcon) {
        getStateHelper().put(Properties.groupCollapsedLeftIcon, groupCollapsedLeftIcon);
    }

    /**
     * The right icon for collapsed panel menu groups
     */
    @Attribute(generate = false)
    public String getGroupCollapsedRightIcon() {
        return (String) getStateHelper().eval(Properties.groupCollapsedRightIcon, PanelIcons.transparent.toString());
    }

    public void setGroupCollapsedRightIcon(String groupCollapsedRightIcon) {
        getStateHelper().put(Properties.groupCollapsedRightIcon, groupCollapsedRightIcon);
    }

    /**
     * The left icon for disabled panel menu groups
     */
    @Attribute(generate = false)
    public String getGroupDisabledLeftIcon() {
        return (String) getStateHelper().eval(Properties.groupDisabledLeftIcon, PanelIcons.transparent.toString());
    }

    public void setGroupDisabledLeftIcon(String groupDisabledLeftIcon) {
        getStateHelper().put(Properties.groupDisabledLeftIcon, groupDisabledLeftIcon);
    }

    /**
     * The right icon for disabled panel menu groups
     */
    @Attribute(generate = false)
    public String getGroupDisabledRightIcon() {
        return (String) getStateHelper().eval(Properties.groupDisabledRightIcon, PanelIcons.transparent.toString());
    }

    public void setGroupDisabledRightIcon(String groupDisabledRightIcon) {
        getStateHelper().put(Properties.groupDisabledRightIcon, groupDisabledRightIcon);
    }

    /**
     * Space-separated list of CSS style class(es) to be applied to top-level panel menu groups
     */
    @Attribute
    public String getTopGroupClass() {
        return (String) getStateHelper().eval(Properties.topGroupClass, getGroupClass());
    }

    public void setTopGroupClass(String topGroupClass) {
        getStateHelper().put(Properties.topGroupClass, topGroupClass);
    }

    /**
     * Space-separated list of CSS style class(es) to be applied to disabled top-level panel menu groups
     */
    @Attribute
    public String getTopGroupDisabledClass() {
        return (String) getStateHelper().eval(Properties.topGroupDisabledClass, getGroupDisabledClass());
    }

    public void setTopGroupDisabledClass(String topGroupDisabledClass) {
        getStateHelper().put(Properties.topGroupDisabledClass, topGroupDisabledClass);
    }

    /**
     * The left icon for expanded top-level panel menu groups
     */
    @Attribute(generate = false)
    public String getTopGroupExpandedLeftIcon() {
        return (String) getStateHelper().eval(Properties.topGroupExpandedLeftIcon, getGroupExpandedLeftIcon());
    }

    public void setTopGroupExpandedLeftIcon(String topGroupExpandedLeftIcon) {
        getStateHelper().put(Properties.topGroupExpandedLeftIcon, topGroupExpandedLeftIcon);
    }

    /**
     * The right icon for expanded top-level panel menu groups
     */
    @Attribute(generate = false)
    public String getTopGroupExpandedRightIcon() {
        return (String) getStateHelper().eval(Properties.topGroupExpandedRightIcon, getGroupExpandedRightIcon());
    }

    public void setTopGroupExpandedRightIcon(String topGroupExpandedRightIcon) {
        getStateHelper().put(Properties.topGroupExpandedRightIcon, topGroupExpandedRightIcon);
    }

    /**
     * The left icon for collapsed top-level panel menu groups
     */
    @Attribute(generate = false)
    public String getTopGroupCollapsedLeftIcon() {
        return (String) getStateHelper().eval(Properties.topGroupCollapsedLeftIcon, getGroupCollapsedLeftIcon());
    }

    public void setTopGroupCollapsedLeftIcon(String topGroupCollapsedLeftIcon) {
        getStateHelper().put(Properties.topGroupCollapsedLeftIcon, topGroupCollapsedLeftIcon);
    }

    /**
     * The right icon for collapsed top-level panel menu groups
     */
    @Attribute(generate = false)
    public String getTopGroupCollapsedRightIcon() {
        return (String) getStateHelper().eval(Properties.topGroupCollapsedRightIcon, getGroupCollapsedRightIcon());
    }

    public void setTopGroupCollapsedRightIcon(String topGroupCollapsedRightIcon) {
        getStateHelper().put(Properties.topGroupCollapsedRightIcon, topGroupCollapsedRightIcon);
    }

    /**
     * The left icon for disabled top-level panel menu groups
     */

    @Attribute(generate = false)
    public String getTopGroupDisabledLeftIcon() {
        return (String) getStateHelper().eval(Properties.topGroupDisabledLeftIcon, getGroupDisabledLeftIcon());
    }

    public void setTopGroupDisabledLeftIcon(String topGroupDisabledLeftIcon) {
        getStateHelper().put(Properties.topGroupDisabledLeftIcon, topGroupDisabledLeftIcon);
    }

    /**
     * The right icon for disabled top-level panel menu groups
     */
    @Attribute(generate = false)
    public String getTopGroupDisabledRightIcon() {
        return (String) getStateHelper().eval(Properties.topGroupDisabledRightIcon, getGroupDisabledRightIcon());
    }

    public void setTopGroupDisabledRightIcon(String topGroupDisabledRightIcon) {
        getStateHelper().put(Properties.topGroupDisabledRightIcon, topGroupDisabledRightIcon);
    }

    public AbstractPanelMenuItem getItem(String itemName) {
        if (itemName == null) {
            throw new IllegalArgumentException("Icon name can't be null");
        }

        return getItem(itemName, this);
    }

    private AbstractPanelMenuItem getItem(String itemName, UIComponent comp) {
        if (this != comp && comp instanceof AbstractPanelMenu) {
            return null;
        }

        if (comp instanceof AbstractPanelMenuItem && itemName.equals(((AbstractPanelMenuItem) comp).getName())) {
            return (AbstractPanelMenuItem) comp;
        }

        for (UIComponent item : comp.getChildren()) {
            AbstractPanelMenuItem resItem = getItem(itemName, item);
            if (resItem != null) {
                return resItem;
            }
        }

        return null;
    }

    // ------------------------------------------------ Event Processing Methods

    public void addItemChangeListener(ItemChangeListener listener) {
        addFacesListener(listener);
    }

    public ItemChangeListener[] getItemChangeListeners() {
        return (ItemChangeListener[]) getFacesListeners(ItemChangeListener.class);
    }

    public void removeItemChangeListener(ItemChangeListener listener) {
        removeFacesListener(listener);
    }

    @Attribute(generate = false, hidden = true)
    public Object getValue() {
        return getStateHelper().eval(Properties.value);
    }

    public void setValue(Object value) {
        getStateHelper().put(Properties.value, value);
    }
}

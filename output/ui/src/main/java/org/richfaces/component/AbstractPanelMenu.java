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
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;

import org.richfaces.PanelMenuMode;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.event.ItemChangeEvent;
import org.richfaces.event.ItemChangeListener;
import org.richfaces.event.ItemChangeSource;
import org.richfaces.renderkit.util.PanelIcons;

/**
 * @author akolonitsky
 * @since 2010-10-25
 */
@JsfComponent(
        tag = @Tag(type = TagType.Facelets, handler = "org.richfaces.view.facelets.html.PanelMenuTagHandler"),
        renderer = @JsfRenderer(type = "org.richfaces.PanelMenuRenderer")
)
public abstract class AbstractPanelMenu extends UIOutput implements ItemChangeSource {

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

        //TODO nick - is component immediate = true only?
        //TODO nick - processValue should be executed in context of component, i.e. when 'component' EL variable is set

        processValue(context);
    }

    private void processValue(FacesContext context) {
        try {
            if (context == null) {
                throw new NullPointerException();
            }

            // Submitted value == null means "the component was not submitted at all".
            String activeItem = getSubmittedActiveItem();
            if (activeItem == null) {
                return;
            }

            String previous = (String) getValue();
            setActiveItem(activeItem);
            setSubmittedActiveItem(null);

            if (previous == null || !previous.equalsIgnoreCase(activeItem)) {
                queueEvent(new ItemChangeEvent(this, previous, activeItem));
            }
        } catch (RuntimeException e) {
            context.renderResponse();
            throw e;
        }
    }

    @Override
    public void queueEvent(FacesEvent event) {
        if ((event instanceof ItemChangeEvent) && (event.getComponent() == this)) {
            setEventPhase(event);
        }

        super.queueEvent(event);
    }

    protected void setEventPhase(FacesEvent event) {
        if (isImmediate()) {
            event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
        } else if (isBypassUpdates()) {
            event.setPhaseId(PhaseId.PROCESS_VALIDATIONS);
        } else {
            event.setPhaseId(PhaseId.INVOKE_APPLICATION);
        }
    }

    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        super.broadcast(event);

        //TODO nick - check for (isBypassUpdates() || isImmediate()) can be removed
        if (event instanceof ItemChangeEvent && (isBypassUpdates() || isImmediate())) {
            getFacesContext().renderResponse();
        }
    }



    public String getSubmittedActiveItem() {
        return this.submittedActiveItem;
    }

    public void setSubmittedActiveItem(String submittedValue) {
        this.submittedActiveItem = String.valueOf(submittedValue);
    }

    public String getActiveItem() {
        return (String) getValue();
    }

    //TODO nick - where is EL-expression updated?
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

    @Attribute
    public abstract boolean isDisabled();

    @Attribute
    public abstract String getExpandEvent();

    @Attribute
    public abstract String getCollapseEvent();

    @Attribute(defaultValue = "PanelMenuMode.client")
    public abstract PanelMenuMode getGroupMode();

    @Attribute(defaultValue = "true")
    public abstract boolean isExpandSingle();

    @Attribute(defaultValue = "PanelMenuMode.DEFAULT")
    public abstract PanelMenuMode getItemMode();

    @Attribute(defaultValue = "true")
    public abstract boolean isBubbleSelection();

    @Attribute
    public abstract MethodExpression getItemChangeListener();

    @Attribute
    public abstract boolean isBypassUpdates();

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
        itemRightIcon, itemLeftIconDisabled, itemRightIconDisabled, topItemLeftIcon, topItemRightIcon, topItemLeftIconDisabled, topItemRightIconDisabled, groupLeftIconExpanded, groupRightIconExpanded, groupLeftIconCollapsed, groupRightIconCollapsed, groupLeftIconDisabled, groupRightIconDisabled, topGroupLeftIconExpanded, topGroupRightIconExpanded, topGroupLeftIconCollapsed, topGroupRightIconCollapsed, topGroupLeftIconDisabled, topGroupRightIconDisabled, itemLeftIcon

    }

    @Attribute
    public abstract String getStyle();

    @Attribute
    public abstract String getStyleClass();

    @Attribute
    public abstract String getWidth();

    @Attribute
    public abstract String getItemClass();

    @Attribute
    public abstract String getItemDisabledClass();

    @Attribute(generate = false)
    public String getItemLeftIcon() {
        return (String) getStateHelper().eval(Properties.itemLeftIcon, PanelIcons.transparent.toString());
    }

    public void setItemLeftIcon(String itemLeftIcon) {
        getStateHelper().put(Properties.itemLeftIcon, itemLeftIcon);
    }

    @Attribute(generate = false)
    public String getItemRightIcon() {
        return (String) getStateHelper().eval(Properties.itemRightIcon, PanelIcons.transparent.toString());
    }

    public void setItemRightIcon(String itemRightIcon) {
        getStateHelper().put(Properties.itemRightIcon, itemRightIcon);
    }

    @Attribute(generate = false)
    public String getItemLeftIconDisabled() {
        return (String) getStateHelper().eval(Properties.itemLeftIconDisabled, PanelIcons.transparent.toString());
    }

    public void setItemLeftIconDisabled(String itemLeftIconDisabled) {
        getStateHelper().put(Properties.itemLeftIconDisabled, itemLeftIconDisabled);
    }

    @Attribute(generate = false)
    public String getItemRightIconDisabled() {
        return (String) getStateHelper().eval(Properties.itemRightIconDisabled, PanelIcons.transparent.toString());
    }

    public void setItemRightIconDisabled(String itemRightIconDisabled) {
        getStateHelper().put(Properties.itemRightIconDisabled, itemRightIconDisabled);
    }

    @Attribute
    public abstract String getTopItemClass();

    @Attribute
    public abstract String getTopItemDisabledClass();

    @Attribute(generate = false)
    public String getTopItemLeftIcon() {
        return (String) getStateHelper().eval(Properties.topItemLeftIcon, PanelIcons.transparent.toString());
    }

    public void setTopItemLeftIcon(String topItemLeftIcon) {
        getStateHelper().put(Properties.topItemLeftIcon, topItemLeftIcon);
    }

    @Attribute(generate = false)
    public String getTopItemRightIcon() {
        return (String) getStateHelper().eval(Properties.topItemRightIcon, PanelIcons.transparent.toString());
    }

    public void setTopItemRightIcon(String topItemRightIcon) {
        getStateHelper().put(Properties.topItemRightIcon, topItemRightIcon);
    }

    @Attribute(generate = false)
    public String getTopItemLeftIconDisabled() {
        return (String) getStateHelper().eval(Properties.topItemLeftIconDisabled, PanelIcons.transparent.toString());
    }

    public void setTopItemLeftIconDisabled(String topItemLeftIconDisabled) {
        getStateHelper().put(Properties.topItemLeftIconDisabled, topItemLeftIconDisabled);
    }

    @Attribute(generate = false)
    public String getTopItemRightIconDisabled() {
        return (String) getStateHelper().eval(Properties.topItemRightIconDisabled, PanelIcons.transparent.toString());
    }

    public void setTopItemRightIconDisabled(String topItemRightIconDisabled) {
        getStateHelper().put(Properties.topItemRightIconDisabled, topItemRightIconDisabled);
    }

    @Attribute
    public abstract String getGroupClass();

    @Attribute
    public abstract String getGroupDisabledClass();

    @Attribute(generate = false)
    public String getGroupLeftIconExpanded() {
        return (String) getStateHelper().eval(Properties.groupLeftIconExpanded, PanelIcons.transparent.toString());
    }

    public void setGroupLeftIconExpanded(String groupLeftIconExpanded) {
        getStateHelper().put(Properties.groupLeftIconExpanded, groupLeftIconExpanded);
    }

    @Attribute(generate = false)
    public String getGroupRightIconExpanded() {
        return (String) getStateHelper().eval(Properties.groupRightIconExpanded, PanelIcons.transparent.toString());
    }

    public void setGroupRightIconExpanded(String groupRightIconExpanded) {
        getStateHelper().put(Properties.groupRightIconExpanded, groupRightIconExpanded);
    }

    @Attribute(generate = false)
    public String getGroupLeftIconCollapsed() {
        return (String) getStateHelper().eval(Properties.groupLeftIconCollapsed, PanelIcons.transparent.toString());
    }

    public void setGroupLeftIconCollapsed(String groupLeftIconCollapsed) {
        getStateHelper().put(Properties.groupLeftIconCollapsed, groupLeftIconCollapsed);
    }

    @Attribute(generate = false)
    public String getGroupRightIconCollapsed() {
        return (String) getStateHelper().eval(Properties.groupRightIconCollapsed, PanelIcons.transparent.toString());
    }

    public void setGroupRightIconCollapsed(String groupRightIconCollapsed) {
        getStateHelper().put(Properties.groupRightIconCollapsed, groupRightIconCollapsed);
    }

    @Attribute(generate = false)
    public String getGroupLeftIconDisabled() {
        return (String) getStateHelper().eval(Properties.groupLeftIconDisabled, PanelIcons.transparent.toString());
    }

    public void setGroupLeftIconDisabled(String groupLeftIconDisabled) {
        getStateHelper().put(Properties.groupLeftIconDisabled, groupLeftIconDisabled);
    }

    @Attribute(generate = false)
    public String getGroupRightIconDisabled() {
        return (String) getStateHelper().eval(Properties.groupRightIconDisabled, PanelIcons.transparent.toString());
    }

    public void setGroupRightIconDisabled(String groupRightIconDisabled) {
        getStateHelper().put(Properties.groupRightIconDisabled, groupRightIconDisabled);
    }

    @Attribute
    public abstract String getTopGroupClass();

    @Attribute
    public abstract String getTopGroupDisabledClass();

    @Attribute(generate = false)
    public String getTopGroupLeftIconExpanded() {
        return (String) getStateHelper().eval(Properties.topGroupLeftIconExpanded, PanelIcons.transparent.toString());
    }

    public void setTopGroupLeftIconExpanded(String topGroupLeftIconExpanded) {
        getStateHelper().put(Properties.topGroupLeftIconExpanded, topGroupLeftIconExpanded);
    }

    @Attribute(generate = false)
    public String getTopGroupRightIconExpanded() {
        return (String) getStateHelper().eval(Properties.topGroupRightIconExpanded, PanelIcons.transparent.toString());
    }

    public void setTopGroupRightIconExpanded(String topGroupRightIconExpanded) {
        getStateHelper().put(Properties.topGroupRightIconExpanded, topGroupRightIconExpanded);
    }

    @Attribute(generate = false)
    public String getTopGroupLeftIconCollapsed() {
        return (String) getStateHelper().eval(Properties.topGroupLeftIconCollapsed, PanelIcons.transparent.toString());
    }

    public void setTopGroupLeftIconCollapsed(String topGroupLeftIconCollapsed) {
        getStateHelper().put(Properties.topGroupLeftIconCollapsed, topGroupLeftIconCollapsed);
    }

    @Attribute(generate = false)
    public String getTopGroupRightIconCollapsed() {
        return (String) getStateHelper().eval(Properties.topGroupRightIconCollapsed, PanelIcons.transparent.toString());
    }

    public void setTopGroupRightIconCollapsed(String topGroupRightIconCollapsed) {
        getStateHelper().put(Properties.topGroupRightIconCollapsed, topGroupRightIconCollapsed);
    }

    @Attribute(generate = false)
    public String getTopGroupLeftIconDisabled() {
        return (String) getStateHelper().eval(Properties.topGroupLeftIconDisabled, PanelIcons.transparent.toString());
    }

    public void setTopGroupLeftIconDisabled(String topGroupLeftIconDisabled) {
        getStateHelper().put(Properties.topGroupLeftIconDisabled, topGroupLeftIconDisabled);
    }

    @Attribute(generate = false)
    public String getTopGroupRightIconDisabled() {
        return (String) getStateHelper().eval(Properties.topGroupRightIconDisabled, PanelIcons.transparent.toString());
    }

    public void setTopGroupRightIconDisabled(String topGroupRightIconDisabled) {
        getStateHelper().put(Properties.topGroupRightIconDisabled, topGroupRightIconDisabled);
    }

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

        if (comp instanceof AbstractPanelMenuItem
                && itemName.equals(((AbstractPanelMenuItem) comp).getName())) {
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
}

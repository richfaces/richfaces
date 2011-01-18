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
import org.richfaces.event.ItemChangeEvent;
import org.richfaces.event.ItemChangeListener;
import org.richfaces.event.ItemChangeSource;
import org.richfaces.renderkit.util.PanelIcons;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;

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

    @Attribute(defaultValue = "click")
    public abstract String getExpandEvent();

    @Attribute(defaultValue = "click")
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
        itemIconRight, itemDisableIconLeft, itemDisableIconRight, topItemIconLeft, topItemIconRight, topItemDisableIconLeft, topItemDisableIconRight, groupExpandIconLeft, groupExpandIconRight, groupCollapseIconLeft, groupCollapseIconRight, groupDisableIconLeft, groupDisableIconRight, topGroupExpandIconLeft, topGroupExpandIconRight, topGroupCollapseIconLeft, topGroupCollapseIconRight, topGroupDisableIconLeft, topGroupDisableIconRight, itemIconLeft

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
    public abstract String getItemDisableClass();

    @Attribute(generate = false)
    public String getItemIconLeft() {
        return (String) getStateHelper().eval(Properties.itemIconLeft, PanelIcons.DEFAULT.toString());
    }

    public void setItemIconLeft(String itemIconLeft) {
        getStateHelper().put(Properties.itemIconLeft, itemIconLeft);
    }

    @Attribute(generate = false)
    public String getItemIconRight() {
        return (String) getStateHelper().eval(Properties.itemIconRight, PanelIcons.DEFAULT.toString());
    }

    public void setItemIconRight(String itemIconRight) {
        getStateHelper().put(Properties.itemIconRight, itemIconRight);
    }

    @Attribute(generate = false)
    public String getItemDisableIconLeft() {
        return (String) getStateHelper().eval(Properties.itemDisableIconLeft, PanelIcons.DEFAULT.toString());
    }

    public void setItemDisableIconLeft(String itemDisableIconLeft) {
        getStateHelper().put(Properties.itemDisableIconLeft, itemDisableIconLeft);
    }

    @Attribute(generate = false)
    public String getItemDisableIconRight() {
        return (String) getStateHelper().eval(Properties.itemDisableIconRight, PanelIcons.DEFAULT.toString());
    }

    public void setItemDisableIconRight(String itemDisableIconRight) {
        getStateHelper().put(Properties.itemDisableIconRight, itemDisableIconRight);
    }

    @Attribute
    public abstract String getTopItemClass();

    @Attribute
    public abstract String getTopItemDisableClass();

    @Attribute(generate = false)
    public String getTopItemIconLeft() {
        return (String) getStateHelper().eval(Properties.topItemIconLeft, PanelIcons.DEFAULT.toString());
    }

    public void setTopItemIconLeft(String topItemIconLeft) {
        getStateHelper().put(Properties.topItemIconLeft, topItemIconLeft);
    }

    @Attribute(generate = false)
    public String getTopItemIconRight() {
        return (String) getStateHelper().eval(Properties.topItemIconRight, PanelIcons.DEFAULT.toString());
    }

    public void setTopItemIconRight(String topItemIconRight) {
        getStateHelper().put(Properties.topItemIconRight, topItemIconRight);
    }

    @Attribute(generate = false)
    public String getTopItemDisableIconLeft() {
        return (String) getStateHelper().eval(Properties.topItemDisableIconLeft, PanelIcons.DEFAULT.toString());
    }

    public void setTopItemDisableIconLeft(String topItemDisableIconLeft) {
        getStateHelper().put(Properties.topItemDisableIconLeft, topItemDisableIconLeft);
    }

    @Attribute(generate = false)
    public String getTopItemDisableIconRight() {
        return (String) getStateHelper().eval(Properties.topItemDisableIconRight, PanelIcons.DEFAULT.toString());
    }

    public void setTopItemDisableIconRight(String topItemDisableIconRight) {
        getStateHelper().put(Properties.topItemDisableIconRight, topItemDisableIconRight);
    }

    @Attribute
    public abstract String getGroupClass();

    @Attribute
    public abstract String getGroupDisableClass();

    @Attribute(generate = false)
    public String getGroupExpandIconLeft() {
        return (String) getStateHelper().eval(Properties.groupExpandIconLeft, PanelIcons.DEFAULT.toString());
    }

    public void setGroupExpandIconLeft(String groupExpandIconLeft) {
        getStateHelper().put(Properties.groupExpandIconLeft, groupExpandIconLeft);
    }

    @Attribute(generate = false)
    public String getGroupExpandIconRight() {
        return (String) getStateHelper().eval(Properties.groupExpandIconRight, PanelIcons.DEFAULT.toString());
    }

    public void setGroupExpandIconRight(String groupExpandIconRight) {
        getStateHelper().put(Properties.groupExpandIconRight, groupExpandIconRight);
    }

    @Attribute(generate = false)
    public String getGroupCollapseIconLeft() {
        return (String) getStateHelper().eval(Properties.groupCollapseIconLeft, PanelIcons.DEFAULT.toString());
    }

    public void setGroupCollapseIconLeft(String groupCollapseIconLeft) {
        getStateHelper().put(Properties.groupCollapseIconLeft, groupCollapseIconLeft);
    }

    @Attribute(generate = false)
    public String getGroupCollapseIconRight() {
        return (String) getStateHelper().eval(Properties.groupCollapseIconRight, PanelIcons.DEFAULT.toString());
    }

    public void setGroupCollapseIconRight(String groupCollapseIconRight) {
        getStateHelper().put(Properties.groupCollapseIconRight, groupCollapseIconRight);
    }

    @Attribute(generate = false)
    public String getGroupDisableIconLeft() {
        return (String) getStateHelper().eval(Properties.groupDisableIconLeft, PanelIcons.DEFAULT.toString());
    }

    public void setGroupDisableIconLeft(String groupDisableIconLeft) {
        getStateHelper().put(Properties.groupDisableIconLeft, groupDisableIconLeft);
    }

    @Attribute(generate = false)
    public String getGroupDisableIconRight() {
        return (String) getStateHelper().eval(Properties.groupDisableIconRight, PanelIcons.DEFAULT.toString());
    }

    public void setGroupDisableIconRight(String groupDisableIconRight) {
        getStateHelper().put(Properties.groupDisableIconRight, groupDisableIconRight);
    }

    @Attribute
    public abstract String getTopGroupClass();

    @Attribute
    public abstract String getTopGroupDisableClass();

    @Attribute(generate = false)
    public String getTopGroupExpandIconLeft() {
        return (String) getStateHelper().eval(Properties.topGroupExpandIconLeft, PanelIcons.DEFAULT.toString());
    }

    public void setTopGroupExpandIconLeft(String topGroupExpandIconLeft) {
        getStateHelper().put(Properties.topGroupExpandIconLeft, topGroupExpandIconLeft);
    }

    @Attribute(generate = false)
    public String getTopGroupExpandIconRight() {
        return (String) getStateHelper().eval(Properties.topGroupExpandIconRight, PanelIcons.DEFAULT.toString());
    }

    public void setTopGroupExpandIconRight(String topGroupExpandIconRight) {
        getStateHelper().put(Properties.topGroupExpandIconRight, topGroupExpandIconRight);
    }

    @Attribute(generate = false)
    public String getTopGroupCollapseIconLeft() {
        return (String) getStateHelper().eval(Properties.topGroupCollapseIconLeft, PanelIcons.DEFAULT.toString());
    }

    public void setTopGroupCollapseIconLeft(String topGroupCollapseIconLeft) {
        getStateHelper().put(Properties.topGroupCollapseIconLeft, topGroupCollapseIconLeft);
    }

    @Attribute(generate = false)
    public String getTopGroupCollapseIconRight() {
        return (String) getStateHelper().eval(Properties.topGroupCollapseIconRight, PanelIcons.DEFAULT.toString());
    }

    public void setTopGroupCollapseIconRight(String topGroupCollapseIconRight) {
        getStateHelper().put(Properties.topGroupCollapseIconRight, topGroupCollapseIconRight);
    }

    @Attribute(generate = false)
    public String getTopGroupDisableIconLeft() {
        return (String) getStateHelper().eval(Properties.topGroupDisableIconLeft, PanelIcons.DEFAULT.toString());
    }

    public void setTopGroupDisableIconLeft(String topGroupDisableIconLeft) {
        getStateHelper().put(Properties.topGroupDisableIconLeft, topGroupDisableIconLeft);
    }

    @Attribute(generate = false)
    public String getTopGroupDisableIconRight() {
        return (String) getStateHelper().eval(Properties.topGroupDisableIconRight, PanelIcons.DEFAULT.toString());
    }

    public void setTopGroupDisableIconRight(String topGroupDisableIconRight) {
        getStateHelper().put(Properties.topGroupDisableIconRight, topGroupDisableIconRight);
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

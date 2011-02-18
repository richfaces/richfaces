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
                AbstractPanelMenuItem prevItm = null;
                AbstractPanelMenuItem actItm = null;
                if (previous != null) {
                    prevItm = getItem(previous);
                }
                if (activeItem != null) {
                    actItm = getItem(activeItem);
                }
                
                ItemChangeEvent event = new ItemChangeEvent(this, previous,prevItm, activeItem, actItm); 
                if (isImmediate() || (actItm != null && actItm.isImmediate())) {
                    event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
                } else if (actItm!= null && actItm.isBypassUpdates()) {
                    event.setPhaseId(PhaseId.PROCESS_VALIDATIONS);
                } else {
                    event.setPhaseId(PhaseId.INVOKE_APPLICATION);
                }
                event.queue();
            }
        } catch (RuntimeException e) {
            context.renderResponse();
            throw e;
        }
    }

    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        super.broadcast(event);

        if (event instanceof ItemChangeEvent) {
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

    // ------------------------------------------------ Html Attributes
    enum Properties {
        itemRightIcon,
        itemDisabledLeftIcon,
        itemDisabledRightIcon,
        topItemLeftIcon,
        topItemRightIcon,
        topItemDisabledLeftIcon,
        topItemDisabledRightIcon,
        groupExpandedLeftIcon,
        groupExpandedRightIcon,
        groupCollapsedLeftIcon,
        groupCollapsedRightIcon,
        groupDisabledLeftIcon,
        groupDisabledRightIcon,
        topGroupExpandedLeftIcon,
        topGroupExpandedRightIcon,
        topGroupCollapsedLeftIcon,
        topGroupCollapsedRightIcon,
        topGroupDisabledLeftIcon,
        topGroupDisabledRightIcon,
        itemLeftIcon
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
    public String getItemDisabledLeftIcon() {
        return (String) getStateHelper().eval(Properties.itemDisabledLeftIcon, PanelIcons.transparent.toString());
    }

    public void setItemDisabledLeftIcon(String itemDisabledLeftIcon) {
        getStateHelper().put(Properties.itemDisabledLeftIcon, itemDisabledLeftIcon);
    }

    @Attribute(generate = false)
    public String getItemDisabledRightIcon() {
        return (String) getStateHelper().eval(Properties.itemDisabledRightIcon, PanelIcons.transparent.toString());
    }

    public void setItemDisabledRightIcon(String itemDisabledRightIcon) {
        getStateHelper().put(Properties.itemDisabledRightIcon, itemDisabledRightIcon);
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
    public String getTopItemDisabledLeftIcon() {
        return (String) getStateHelper().eval(Properties.topItemDisabledLeftIcon, PanelIcons.transparent.toString());
    }

    public void setTopItemDisabledLeftIcon(String topItemDisabledLeftIcon) {
        getStateHelper().put(Properties.topItemDisabledLeftIcon, topItemDisabledLeftIcon);
    }

    @Attribute(generate = false)
    public String getTopItemDisabledRightIcon() {
        return (String) getStateHelper().eval(Properties.topItemDisabledRightIcon, PanelIcons.transparent.toString());
    }

    public void setTopItemDisabledRightIcon(String topItemDisabledRightIcon) {
        getStateHelper().put(Properties.topItemDisabledRightIcon, topItemDisabledRightIcon);
    }

    @Attribute
    public abstract String getGroupClass();

    @Attribute
    public abstract String getGroupDisabledClass();

    @Attribute(generate = false)
    public String getGroupExpandedLeftIcon() {
        return (String) getStateHelper().eval(Properties.groupExpandedLeftIcon, PanelIcons.transparent.toString());
    }

    public void setGroupExpandedLeftIcon(String groupExpandedLeftIcon) {
        getStateHelper().put(Properties.groupExpandedLeftIcon, groupExpandedLeftIcon);
    }

    @Attribute(generate = false)
    public String getGroupExpandedRightIcon() {
        return (String) getStateHelper().eval(Properties.groupExpandedRightIcon, PanelIcons.transparent.toString());
    }

    public void setGroupExpandedRightIcon(String groupExpandedRightIcon) {
        getStateHelper().put(Properties.groupExpandedRightIcon, groupExpandedRightIcon);
    }

    @Attribute(generate = false)
    public String getGroupCollapsedLeftIcon() {
        return (String) getStateHelper().eval(Properties.groupCollapsedLeftIcon, PanelIcons.transparent.toString());
    }

    public void setGroupCollapsedLeftIcon(String groupCollapsedLeftIcon) {
        getStateHelper().put(Properties.groupCollapsedLeftIcon, groupCollapsedLeftIcon);
    }

    @Attribute(generate = false)
    public String getGroupCollapsedRightIcon() {
        return (String) getStateHelper().eval(Properties.groupCollapsedRightIcon, PanelIcons.transparent.toString());
    }

    public void setGroupCollapsedRightIcon(String groupCollapsedRightIcon) {
        getStateHelper().put(Properties.groupCollapsedRightIcon, groupCollapsedRightIcon);
    }

    @Attribute(generate = false)
    public String getGroupDisabledLeftIcon() {
        return (String) getStateHelper().eval(Properties.groupDisabledLeftIcon, PanelIcons.transparent.toString());
    }

    public void setGroupDisabledLeftIcon(String groupDisabledLeftIcon) {
        getStateHelper().put(Properties.groupDisabledLeftIcon, groupDisabledLeftIcon);
    }

    @Attribute(generate = false)
    public String getGroupDisabledRightIcon() {
        return (String) getStateHelper().eval(Properties.groupDisabledRightIcon, PanelIcons.transparent.toString());
    }

    public void setGroupDisabledRightIcon(String groupDisabledRightIcon) {
        getStateHelper().put(Properties.groupDisabledRightIcon, groupDisabledRightIcon);
    }

    @Attribute
    public abstract String getTopGroupClass();

    @Attribute
    public abstract String getTopGroupDisabledClass();

    @Attribute(generate = false)
    public String getTopGroupExpandedLeftIcon() {
        return (String) getStateHelper().eval(Properties.topGroupExpandedLeftIcon, PanelIcons.transparent.toString());
    }

    public void setTopGroupExpandedLeftIcon(String topGroupExpandedLeftIcon) {
        getStateHelper().put(Properties.topGroupExpandedLeftIcon, topGroupExpandedLeftIcon);
    }

    @Attribute(generate = false)
    public String getTopGroupExpandedRightIcon() {
        return (String) getStateHelper().eval(Properties.topGroupExpandedRightIcon, PanelIcons.transparent.toString());
    }

    public void setTopGroupExpandedRightIcon(String topGroupExpandedRightIcon) {
        getStateHelper().put(Properties.topGroupExpandedRightIcon, topGroupExpandedRightIcon);
    }

    @Attribute(generate = false)
    public String getTopGroupCollapsedLeftIcon() {
        return (String) getStateHelper().eval(Properties.topGroupCollapsedLeftIcon, PanelIcons.transparent.toString());
    }

    public void setTopGroupCollapsedLeftIcon(String topGroupCollapsedLeftIcon) {
        getStateHelper().put(Properties.topGroupCollapsedLeftIcon, topGroupCollapsedLeftIcon);
    }

    @Attribute(generate = false)
    public String getTopGroupCollapsedRightIcon() {
        return (String) getStateHelper().eval(Properties.topGroupCollapsedRightIcon, PanelIcons.transparent.toString());
    }

    public void setTopGroupCollapsedRightIcon(String topGroupCollapsedRightIcon) {
        getStateHelper().put(Properties.topGroupCollapsedRightIcon, topGroupCollapsedRightIcon);
    }

    @Attribute(generate = false)
    public String getTopGroupDisabledLeftIcon() {
        return (String) getStateHelper().eval(Properties.topGroupDisabledLeftIcon, PanelIcons.transparent.toString());
    }

    public void setTopGroupDisabledLeftIcon(String topGroupDisabledLeftIcon) {
        getStateHelper().put(Properties.topGroupDisabledLeftIcon, topGroupDisabledLeftIcon);
    }

    @Attribute(generate = false)
    public String getTopGroupDisabledRightIcon() {
        return (String) getStateHelper().eval(Properties.topGroupDisabledRightIcon, PanelIcons.transparent.toString());
    }

    public void setTopGroupDisabledRightIcon(String topGroupDisabledRightIcon) {
        getStateHelper().put(Properties.topGroupDisabledRightIcon, topGroupDisabledRightIcon);
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

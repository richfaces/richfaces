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
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;

import org.richfaces.PanelMenuMode;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.event.ItemChangeEvent;
import org.richfaces.event.PanelToggleEvent;

/**
 * @author akolonitsky
 * @since 2010-10-25
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets))
public abstract class AbstractPanelMenuGroup extends AbstractPanelMenuItem {

    public static final String COMPONENT_TYPE = "org.richfaces.PanelMenuGroup";

    public static final String COMPONENT_FAMILY = "org.richfaces.PanelMenuGroup";
    private Boolean submittedExpanded;

    private enum PropertyKeys {
        selectable, immediate
    }

    protected AbstractPanelMenuGroup() {
        setRendererType("org.richfaces.PanelMenuGroupRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public void processDecodes(FacesContext context) {
        super.processDecodes(context);

        //TODO nick - is component immediate==true always?
        executeValidate(context);
    }

    public void validate(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }

        // Submitted value == null means "the component was not submitted at all".
        Boolean expanded = getSubmittedExpanded();
        if (expanded == null) {
            return;
        }

        Boolean previous = (Boolean) getValue();
        setExpanded(expanded);
        setSubmittedExpanded(null);
        if (previous != null && !previous.equals(expanded)) {
            queueEvent(new PanelToggleEvent(this, previous));
        }
    }

    private void executeValidate(FacesContext context) {
        try {
            validate(context);
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



    public Boolean getSubmittedExpanded() {
        return this.submittedExpanded;
    }

    public void setSubmittedExpanded(Object submittedValue) {
        this.submittedExpanded = Boolean.parseBoolean(String.valueOf(submittedValue));
    }

    public boolean isExpanded() {
        return getValue() == null ? false : (Boolean) getValue();
    }

    public void setExpanded(boolean expanded) {
        setValue(expanded);
    }

    @Override
    public void setValueExpression(String name, ValueExpression binding) {
        if ("expanded".equals(name)) {
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

    // ------------------------------------------------ Component Attributes

    @Attribute(defaultValue = "Boolean.FALSE")
    public abstract Boolean isSelectable();

    @Attribute(defaultValue = "getPanelMenu().getGroupMode()")
    public abstract PanelMenuMode getMode();

    @Attribute(defaultValue = "getPanelMenu().isExpandSingle()")
    public abstract boolean isExpandSingle();

    @Attribute
    public abstract String getCollapseEvent();

    @Attribute
    public abstract String getExpandEvent();

    @Attribute(defaultValue = "getPanelMenu().isBubbleSelection()")
    public abstract boolean isBubbleSelection();

    @Attribute
    public abstract MethodExpression getChangeExpandListener();

    // ------------------------------------------------ Html Attributes

    enum Properties {
        leftIconDisabled, leftIconExpanded, rightIconCollapsed, rightIconDisabled, rightIconExpanded, disabledClass, styleClass, leftIconCollapsed
    }

    @Attribute(generate = false)
    public String getLeftIconCollapsed() {
        return (String) getStateHelper().eval(Properties.leftIconCollapsed,
                isTopItem() ? getPanelMenu().getTopGroupLeftIconCollapsed() : getPanelMenu().getGroupLeftIconCollapsed());
    }

    public void setLeftIconCollapsed(String leftIconCollapsed) {
        getStateHelper().put(Properties.leftIconCollapsed, leftIconCollapsed);
    }


    @Attribute(generate = false)
    public String getLeftIconDisabled() {
        return (String) getStateHelper().eval(Properties.leftIconDisabled,
                isTopItem() ? getPanelMenu().getTopGroupLeftIconDisabled() : getPanelMenu().getGroupLeftIconDisabled());
    }

    public void setLeftIconDisabled(String leftIconDisabled) {
        getStateHelper().put(Properties.leftIconDisabled, leftIconDisabled);
    }

    @Attribute(generate = false)
    public String getLeftIconExpanded() {
        return (String) getStateHelper().eval(Properties.leftIconExpanded,
                isTopItem() ? getPanelMenu().getTopGroupLeftIconExpanded() : getPanelMenu().getGroupLeftIconExpanded());
    }

    public void setLeftIconExpanded(String leftIconExpanded) {
        getStateHelper().put(Properties.leftIconExpanded, leftIconExpanded);
    }

    @Attribute(generate = false)
    public String getRightIconCollapsed() {
        return (String) getStateHelper().eval(Properties.rightIconCollapsed,
                isTopItem() ? getPanelMenu().getTopGroupRightIconCollapsed() : getPanelMenu().getGroupRightIconCollapsed());
    }

    public void setRightIconCollapsed(String rightIconCollapsed) {
        getStateHelper().put(Properties.rightIconCollapsed, rightIconCollapsed);
    }

    @Attribute(generate = false)
    public String getRightIconDisabled() {
        return (String) getStateHelper().eval(Properties.rightIconDisabled,
                isTopItem() ? getPanelMenu().getTopGroupRightIconDisabled() : getPanelMenu().getGroupRightIconDisabled());
    }

    public void setRightIconDisabled(String rightIconDisabled) {
        getStateHelper().put(Properties.rightIconDisabled, rightIconDisabled);
    }

    @Attribute(generate = false)
    public String getRightIconExpanded() {
        return (String) getStateHelper().eval(Properties.rightIconExpanded,
                isTopItem() ? getPanelMenu().getTopGroupRightIconExpanded() : getPanelMenu().getGroupRightIconExpanded());
    }

    public void setRightIconExpanded(String rightIconExpanded) {
        getStateHelper().put(Properties.rightIconExpanded, rightIconExpanded);
    }

    @Attribute(events = @EventName("collapse"))
    public abstract String getOncollapse();

    @Attribute(events = @EventName("expand"))
    public abstract String getOnexpand();

    @Attribute(events = @EventName("switch"))
    public abstract String getOnswitch();

    @Attribute(events = @EventName("beforecollapse"))
    public abstract String getOnbeforecollapse();

    @Attribute(events = @EventName("beforeexpand"))
    public abstract String getOnbeforeexpand();

    @Attribute(events = @EventName("beforeswitch"))
    public abstract String getOnbeforeswitch();

    @Attribute(generate = false)
    public String getDisabledClass() {
        return (String) getStateHelper().eval(Properties.disabledClass,
                isTopItem() ? getPanelMenu().getTopGroupClassDisabled() : getPanelMenu().getGroupDisableClass());
    }

    public void setDisabledClass(String disabledClass) {
        getStateHelper().put(Properties.disabledClass, disabledClass);
    }

    public abstract String getHoverClass();

    public abstract String getLeftIconClass();

    public abstract String getRightIconClass();

    public abstract String getStyle();

    @Attribute(generate = false)
    public String getStyleClass() {
        return (String) getStateHelper().eval(Properties.styleClass,
                isTopItem() ? getPanelMenu().getTopGroupClass() : getPanelMenu().getGroupClass());
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

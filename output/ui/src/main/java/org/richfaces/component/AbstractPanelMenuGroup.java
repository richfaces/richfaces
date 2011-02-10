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
import org.richfaces.event.PanelToggleEvent;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;

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

    @Attribute
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

    @Attribute
    public abstract String getCollapseEvent();

    @Attribute
    public abstract String getExpandEvent();

    @Attribute
    public abstract MethodExpression getChangeExpandListener();

    // ------------------------------------------------ Html Attributes

    enum Properties {
        leftDisabledIcon, leftExpandedIcon, rightCollapsedIcon, rightDisabledIcon, rightExpandedIcon, disabledClass, styleClass, leftCollapsedIcon
    }

    @Attribute(generate = false)
    public String getLeftCollapsedIcon() {
        return (String) getStateHelper().eval(Properties.leftCollapsedIcon,
                isTopItem() ? getPanelMenu().getTopGroupCollapsedLeftIcon() : getPanelMenu().getGroupCollapsedLeftIcon());
    }

    public void setLeftCollapsedIcon(String leftCollapsedIcon) {
        getStateHelper().put(Properties.leftCollapsedIcon, leftCollapsedIcon);
    }


    @Attribute(generate = false)
    public String getLeftDisabledIcon() {
        return (String) getStateHelper().eval(Properties.leftDisabledIcon,
                isTopItem() ? getPanelMenu().getTopGroupDisabledLeftIcon() : getPanelMenu().getGroupDisabledLeftIcon());
    }

    public void setLeftDisabledIcon(String leftDisabledIcon) {
        getStateHelper().put(Properties.leftDisabledIcon, leftDisabledIcon);
    }

    @Attribute(generate = false)
    public String getLeftExpandedIcon() {
        return (String) getStateHelper().eval(Properties.leftExpandedIcon,
                isTopItem() ? getPanelMenu().getTopGroupExpandedLeftIcon() : getPanelMenu().getGroupExpandedLeftIcon());
    }

    public void setLeftExpandedIcon(String leftExpandedIcon) {
        getStateHelper().put(Properties.leftExpandedIcon, leftExpandedIcon);
    }

    @Attribute(generate = false)
    public String getRightCollapsedIcon() {
        return (String) getStateHelper().eval(Properties.rightCollapsedIcon,
                isTopItem() ? getPanelMenu().getTopGroupCollapsedRightIcon() : getPanelMenu().getGroupCollapsedRightIcon());
    }

    public void setRightCollapsedIcon(String rightCollapsedIcon) {
        getStateHelper().put(Properties.rightCollapsedIcon, rightCollapsedIcon);
    }

    @Attribute(generate = false)
    public String getRightDisabledIcon() {
        return (String) getStateHelper().eval(Properties.rightDisabledIcon,
                isTopItem() ? getPanelMenu().getTopGroupDisabledRightIcon() : getPanelMenu().getGroupDisabledRightIcon());
    }

    public void setRightDisabledIcon(String rightDisabledIcon) {
        getStateHelper().put(Properties.rightDisabledIcon, rightDisabledIcon);
    }

    @Attribute(generate = false)
    public String getRightExpandedIcon() {
        return (String) getStateHelper().eval(Properties.rightExpandedIcon,
                isTopItem() ? getPanelMenu().getTopGroupExpandedRightIcon() : getPanelMenu().getGroupExpandedRightIcon());
    }

    public void setRightExpandedIcon(String rightExpandedIcon) {
        getStateHelper().put(Properties.rightExpandedIcon, rightExpandedIcon);
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
                isTopItem() ? getPanelMenu().getTopGroupDisabledClass() : getPanelMenu().getGroupDisabledClass());
    }

    public void setDisabledClass(String disabledClass) {
        getStateHelper().put(Properties.disabledClass, disabledClass);
    }

    public abstract String getHoverClass();

    public abstract String getLeftIconClass();

    public abstract String getRightIconClass();

    @Attribute(hidden = true)
    public abstract String getLeftIcon();

    @Attribute(hidden = true)
    public abstract String getRightIcon();

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

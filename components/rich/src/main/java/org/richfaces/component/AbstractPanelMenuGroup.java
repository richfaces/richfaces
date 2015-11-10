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

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;

import org.richfaces.PanelMenuMode;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.component.attribute.AjaxProps;
import org.richfaces.component.attribute.BypassProps;
import org.richfaces.component.attribute.EventsMouseProps;
import org.richfaces.component.attribute.StyleClassProps;
import org.richfaces.component.attribute.StyleProps;
import org.richfaces.event.ItemChangeEvent;
import org.richfaces.event.PanelToggleEvent;

/**
 * <p>
 * The &lt;rich:panelMenuGroup&gt; component defines a group of &lt;rich:panelMenuItem&gt; components inside a
 * &lt;rich:panelMenu&gt;.
 * </p>
 *
 * @author akolonitsky
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets))
public abstract class AbstractPanelMenuGroup extends AbstractPanelMenuItem implements AjaxProps, BypassProps, EventsMouseProps,
    StyleProps, StyleClassProps {
    public static final String COMPONENT_TYPE = "org.richfaces.PanelMenuGroup";
    public static final String COMPONENT_FAMILY = "org.richfaces.PanelMenuGroup";
    private Boolean submittedExpanded;
    private boolean valueChanged;

    private enum PropertyKeys {
        selectable,
        immediate
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

        // TODO nick - is component immediate==true always?
        executeValidate(context);
    }

    private boolean isValueChanged() {
        return valueChanged;
    }

    private void setValueChanged(boolean valueChanged) {
        this.valueChanged = valueChanged;
    }

    public void validate(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }

        // Submitted value == null means "the component was not submitted at all".
        Boolean expanded = getSubmittedExpanded();
        if (expanded == null) {
            setValueChanged(false);
            return;
        }

        Boolean previous = (Boolean) getValue();
        setExpanded(expanded);
        setSubmittedExpanded(null);
        setValueChanged(!expanded.equals(previous));

        if (previous != null && isValueChanged()) {
            if (expanded && getMode() == PanelMenuMode.server && getPanelMenu().isExpandSingle()) {
                collapseOtherTopGroups();
            }

            queueEvent(new PanelToggleEvent(this, previous));
        }
    }

    private AbstractPanelMenuGroup getTopGroup() {
        AbstractPanelMenuGroup c = this;

        while (c.getParent() instanceof AbstractPanelMenuGroup) {
            c = (AbstractPanelMenuGroup) c.getParent();
        }

        return c;
    }

    private void collapseOtherTopGroups() {
        UIComponent topGroup = getTopGroup();
        for (UIComponent child : getPanelMenu().getChildren()) {
            if (!(child instanceof AbstractPanelMenuGroup)) {
                continue;
            }

            AbstractPanelMenuGroup group = (AbstractPanelMenuGroup) child;

            if (group == topGroup) {
                continue;
            }

            group.setSubmittedExpanded(null);
            group.setExpanded(false);
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

    /**
     * Defines whenever this group is expanded
     */
    @Attribute
    public Boolean isExpanded() {
        return (Boolean) getValue();
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

    /**
     * Defines whenever the group is selectable. Default: false.
     */
    @Attribute(defaultValue = "Boolean.FALSE")
    public abstract Boolean getSelectable();

    /**
     * Mode used for expanding/collapsing of this group: default value is inherited from panelMenu
     */
    @Attribute(defaultValue = "getPanelMenu().getGroupMode()")
    public abstract PanelMenuMode getMode();

    /**
     * The mouse event used for collapsing.
     */
    @Attribute
    public abstract String getCollapseEvent();

    /**
     * The mouse event used for expansion.
     */
    @Attribute
    public abstract String getExpandEvent();

    // ------------------------------------------------ Html Attributes

    enum Properties {
        leftDisabledIcon,
        leftExpandedIcon,
        rightCollapsedIcon,
        rightDisabledIcon,
        rightExpandedIcon,
        disabledClass,
        styleClass,
        leftCollapsedIcon
    }

    /**
     * The icon displayed on the left of the menu group label when the group is collapsed
     */
    @Attribute(generate = false)
    public String getLeftCollapsedIcon() {
        return (String) getStateHelper().eval(Properties.leftCollapsedIcon,
            isTopItem() ? getPanelMenu().getTopGroupCollapsedLeftIcon() : getPanelMenu().getGroupCollapsedLeftIcon());
    }

    public void setLeftCollapsedIcon(String leftCollapsedIcon) {
        getStateHelper().put(Properties.leftCollapsedIcon, leftCollapsedIcon);
    }

    /**
     * The icon displayed on the left of the menu group label when the group is disabled
     */
    @Attribute(generate = false)
    public String getLeftDisabledIcon() {
        return (String) getStateHelper().eval(Properties.leftDisabledIcon,
            isTopItem() ? getPanelMenu().getTopGroupDisabledLeftIcon() : getPanelMenu().getGroupDisabledLeftIcon());
    }

    public void setLeftDisabledIcon(String leftDisabledIcon) {
        getStateHelper().put(Properties.leftDisabledIcon, leftDisabledIcon);
    }

    /**
     * The icon displayed on the left of the menu group label when the group is expanded
     */
    @Attribute(generate = false)
    public String getLeftExpandedIcon() {
        return (String) getStateHelper().eval(Properties.leftExpandedIcon,
            isTopItem() ? getPanelMenu().getTopGroupExpandedLeftIcon() : getPanelMenu().getGroupExpandedLeftIcon());
    }

    public void setLeftExpandedIcon(String leftExpandedIcon) {
        getStateHelper().put(Properties.leftExpandedIcon, leftExpandedIcon);
    }

    /**
     * The icon displayed on the right of the menu group label when the group is collapsed
     */
    @Attribute(generate = false)
    public String getRightCollapsedIcon() {
        return (String) getStateHelper().eval(Properties.rightCollapsedIcon,
            isTopItem() ? getPanelMenu().getTopGroupCollapsedRightIcon() : getPanelMenu().getGroupCollapsedRightIcon());
    }

    public void setRightCollapsedIcon(String rightCollapsedIcon) {
        getStateHelper().put(Properties.rightCollapsedIcon, rightCollapsedIcon);
    }

    /**
     * The icon displayed on the left of the menu group label when the group is disabled
     */
    @Attribute(generate = false)
    public String getRightDisabledIcon() {
        return (String) getStateHelper().eval(Properties.rightDisabledIcon,
            isTopItem() ? getPanelMenu().getTopGroupDisabledRightIcon() : getPanelMenu().getGroupDisabledRightIcon());
    }

    public void setRightDisabledIcon(String rightDisabledIcon) {
        getStateHelper().put(Properties.rightDisabledIcon, rightDisabledIcon);
    }

    /**
     * The icon displayed on the left of the menu group label when the group is expanded
     */
    @Attribute(generate = false)
    public String getRightExpandedIcon() {
        return (String) getStateHelper().eval(Properties.rightExpandedIcon,
            isTopItem() ? getPanelMenu().getTopGroupExpandedRightIcon() : getPanelMenu().getGroupExpandedRightIcon());
    }

    public void setRightExpandedIcon(String rightExpandedIcon) {
        getStateHelper().put(Properties.rightExpandedIcon, rightExpandedIcon);
    }

    /**
     * The client-side script method to be called after the menu group is collapsed
     */
    @Attribute(events = @EventName("collapse"))
    public abstract String getOncollapse();

    /**
     * The client-side script method to be called after the menu group is expanded
     */
    @Attribute(events = @EventName("expand"))
    public abstract String getOnexpand();

    /**
     * The client-side script method to be called after the menu group is switched (toggled)
     */
    @Attribute(events = @EventName("switch"))
    public abstract String getOnswitch();

    /**
     * The client-side script method to be called before the menu group is collapsed
     */
    @Attribute(events = @EventName("beforecollapse"))
    public abstract String getOnbeforecollapse();

    /**
     * The client-side script method to be called before the menu group is expanded
     */
    @Attribute(events = @EventName("beforeexpand"))
    public abstract String getOnbeforeexpand();

    /**
     * The client-side script method to be called before the menu group is switched (toggled)
     */
    @Attribute(events = @EventName("beforeswitch"))
    public abstract String getOnbeforeswitch();

    /**
     * Space-separated list of CSS style class(es) to be applied to the panel menu group when it is disabled.
     */
    @Attribute(generate = false)
    public String getDisabledClass() {
        return (String) getStateHelper().eval(Properties.disabledClass,
            isTopItem() ? getPanelMenu().getTopGroupDisabledClass() : getPanelMenu().getGroupDisabledClass());
    }

    public void setDisabledClass(String disabledClass) {
        getStateHelper().put(Properties.disabledClass, disabledClass);
    }

    /**
     * Space-separated list of CSS style class(es) to be applied to the left icon of the panel menu group.
     */
    @Attribute
    public abstract String getLeftIconClass();

    /**
     * Space-separated list of CSS style class(es) to be applied to the right icon of the panel menu group.
     */
    @Attribute
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

    public boolean hasActiveItem(UIComponent component, String activeItem) {
        if (activeItem == null) {
            return false;
        }
        if (component instanceof AbstractPanelMenuItem) {
            AbstractPanelMenuItem item = (AbstractPanelMenuItem) component;
            if (activeItem.equals(item.getName())) {
                return true;
            }
        }

        if (component instanceof AbstractPanelMenuGroup) {
            AbstractPanelMenuGroup group = (AbstractPanelMenuGroup) component;
            if (!group.getPanelMenu().isBubbleSelection()) {
                return false;
            }
        }

        if (component.getChildCount() > 0) {
            for (UIComponent child : component.getChildren()) {
                if (!child.isRendered()) {
                    continue;
                }

                if (!(child instanceof AbstractPanelMenuItem)) {
                    continue;
                }

                if (hasActiveItem(child, activeItem)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean getState() {
        Boolean flag = this.isExpanded();
        return (flag == null ? this.hasActiveItem(this, this.getPanelMenu().getActiveItem()) : flag);
    }

    @Override
    public void processUpdates(FacesContext context) {
        if (isValueChanged()) {
            ValueExpression ve = getValueExpression("value");
            ELContext elc = FacesContext.getCurrentInstance().getELContext();
            if (ve != null && !ve.isReadOnly(elc)) {
                ve.setValue(elc, isExpanded());
            }
        }
        super.processUpdates(context);
    }
}

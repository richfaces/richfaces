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
import org.richfaces.component.behavior.ToggleControl;

import javax.faces.component.behavior.ClientBehaviorHolder;

/**
 * @author akolonitsky
 * @since 2010-08-13
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets), renderer = @JsfRenderer(type = "org.richfaces.AccordionItemRenderer"))
public abstract class AbstractAccordionItem extends AbstractTogglePanelTitledItem implements ClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "org.richfaces.AccordionItem";

    public static final String COMPONENT_FAMILY = "org.richfaces.AccordionItem";

    enum Properties {
        contentClass, leftIconActive, leftIconInactive, leftIconDisabled, rightIconActive, rightIconDisabled, rightIconInactive, headerClassActive, headerClassDisabled, headerClassInactive, headerClass;
    }

    public AbstractAccordionItem() {
        setRendererType("org.richfaces.AccordionItemRenderer");
    }

    public AbstractAccordion getAccordion() {
        return (AbstractAccordion) ToggleControl.getEnclosedPanel(this);
    }

    @Attribute(defaultValue = "getAccordion().getItemLeftIconActive()", generate = false)
    public String getLeftIconActive() {
        return (String) getStateHelper().eval(Properties.leftIconActive, getAccordion().getItemLeftIconActive());
    }

    public void setLeftIconActive(String leftIconActive) {
        getStateHelper().put(Properties.leftIconActive, leftIconActive);
    }

    @Attribute(defaultValue = "getAccordion().getItemLeftIconDisabled()", generate = false)
    public String getLeftIconDisabled() {
        return (String) getStateHelper().eval(Properties.leftIconDisabled, getAccordion().getItemLeftIconDisabled());
    }

    public void setLeftIconDisabled(String leftIconDisabled) {
        getStateHelper().put(Properties.leftIconDisabled, leftIconDisabled);
    }


    @Attribute(defaultValue = "getAccordion().getItemLeftIconInactive()", generate = false)
    public String getLeftIconInactive() {
        return (String) getStateHelper().eval(Properties.leftIconInactive, getAccordion().getItemLeftIconInactive());
    }

    public void setLeftIconInactive(String leftIconInactive) {
        getStateHelper().put(Properties.leftIconInactive, leftIconInactive);
    }

    @Attribute(defaultValue = "getAccordion().getItemRightIconActive()", generate = false)
    public String getRightIconActive() {
        return (String) getStateHelper().eval(Properties.rightIconActive, getAccordion().getItemRightIconActive());
    }

    public void setRightIconActive(String rightIconActive) {
        getStateHelper().put(Properties.rightIconActive, rightIconActive);
    }

    @Attribute(defaultValue = "getAccordion().getItemRightIconDisabled()", generate = false)
    public String getRightIconDisabled() {
        return (String) getStateHelper().eval(Properties.rightIconDisabled, getAccordion().getItemRightIconDisabled());
    }

    public void setRightIconDisabled(String rightIconDisabled) {
        getStateHelper().put(Properties.rightIconDisabled, rightIconDisabled);
    }

    @Attribute(defaultValue = "getAccordion().getItemRightIconInactive()", generate = false)
    public String getRightIconInactive() {
        return (String) getStateHelper().eval(Properties.rightIconInactive, getAccordion().getItemRightIconInactive());
    }

    public void setRightIconInactive(String rightIconInactive) {
        getStateHelper().put(Properties.rightIconInactive, rightIconInactive);
    }

    @Attribute(defaultValue = "getAccordion().getItemHeaderClassActive()", generate = false)
    public String getHeaderClassActive() {
        return (String) getStateHelper().eval(Properties.headerClassActive, getAccordion().getItemHeaderClassActive());
    }

    public void setHeaderClassActive(String headerClassActive) {
        getStateHelper().put(Properties.headerClassActive, headerClassActive);
    }

    @Attribute(defaultValue = "getAccordion().getItemHeaderClassDisabled()", generate = false)
    public String getHeaderClassDisabled() {
        return (String) getStateHelper().eval(Properties.headerClassDisabled, getAccordion().getItemHeaderClassDisabled());
    }

    public void setHeaderClassDisabled(String headerClassDisabled) {
        getStateHelper().put(Properties.headerClassDisabled, headerClassDisabled);
    }

    @Attribute(defaultValue = "getAccordion().getItemHeaderClassInactive()", generate = false)
    public String getHeaderClassInactive() {
        return (String) getStateHelper().eval(Properties.headerClassInactive, getAccordion().getItemHeaderClassInactive());
    }

    public void setHeaderClassInactive(String headerClassInactive) {
        getStateHelper().put(Properties.headerClassInactive, headerClassInactive);
    }

    @Attribute(defaultValue = "getAccordion().getItemHeaderClass()")
    public String getHeaderClass() {
        return (String) getStateHelper().eval(Properties.headerClass, getAccordion().getItemHeaderClass());
    }

    public void setHeaderClass(String headerClass) {
        getStateHelper().put(Properties.headerClass, headerClass);
    }

    @Attribute
    public abstract String getHeaderStyle();

    @Attribute(defaultValue = "getAccordion().getItemContentClass()")
    public String getContentClass() {
        return (String) getStateHelper().eval(Properties.contentClass, getAccordion().getItemContentClass());
    }

    public void setContentClass(String contentClass) {
        getStateHelper().put(Properties.contentClass, contentClass);
    }

    @Attribute(events = @EventName("headerclick"))
    public abstract String getOnheaderclick();

    @Attribute(events = @EventName("headerdblclick"))
    public abstract String getOnheaderdblclick();

    @Attribute(events = @EventName("headermousedown"))
    public abstract String getOnheadermousedown();

    @Attribute(events = @EventName("headermousemove"))
    public abstract String getOnheadermousemove();

    @Attribute(events = @EventName("headermouseup"))
    public abstract String getOnheadermouseup();

    @Attribute(events = @EventName("enter"))
    public abstract String getOnenter();

    @Attribute(events = @EventName("leave"))
    public abstract String getOnleave();

    @Attribute
    public abstract String getLang();

    @Attribute
    public abstract String getTitle();

    @Attribute
    public abstract String getStyle();

    @Attribute
    public abstract String getStyleClass();

    @Attribute
    public abstract String getDir();

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

}


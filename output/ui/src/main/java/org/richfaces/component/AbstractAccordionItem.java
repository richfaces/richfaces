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
        contentClass,
        leftActiveIcon,
        leftInactiveIcon,
        leftDisabledIcon,
        rightActiveIcon,
        rightDisabledIcon,
        rightInactiveIcon,
        headerActiveClass,
        headerDisabledClass,
        headerInactiveClass,
        headerClass
    }

    public AbstractAccordionItem() {
        setRendererType("org.richfaces.AccordionItemRenderer");
    }

    public AbstractAccordion getAccordion() {
        return (AbstractAccordion) ToggleControl.getEnclosedPanel(this);
    }

    @Attribute(defaultValue = "getAccordion().getItemActiveLeftIcon()", generate = false)
    public String getLeftActiveIcon() {
        return (String) getStateHelper().eval(Properties.leftActiveIcon, getAccordion().getItemActiveLeftIcon());
    }

    public void setLeftActiveIcon(String leftActiveIcon) {
        getStateHelper().put(Properties.leftActiveIcon, leftActiveIcon);
    }

    @Attribute(defaultValue = "getAccordion().getItemDisabledLeftIcon()", generate = false)
    public String getLeftDisabledIcon() {
        return (String) getStateHelper().eval(Properties.leftDisabledIcon, getAccordion().getItemDisabledLeftIcon());
    }

    public void setLeftDisabledIcon(String leftDisabledIcon) {
        getStateHelper().put(Properties.leftDisabledIcon, leftDisabledIcon);
    }


    @Attribute(defaultValue = "getAccordion().getItemInactiveLeftIcon()", generate = false)
    public String getLeftInactiveIcon() {
        return (String) getStateHelper().eval(Properties.leftInactiveIcon, getAccordion().getItemInactiveLeftIcon());
    }

    public void setLeftInactiveIcon(String leftInactiveIcon) {
        getStateHelper().put(Properties.leftInactiveIcon, leftInactiveIcon);
    }

    @Attribute(defaultValue = "getAccordion().getItemActiveRightIcon()", generate = false)
    public String getRightActiveIcon() {
        return (String) getStateHelper().eval(Properties.rightActiveIcon, getAccordion().getItemActiveRightIcon());
    }

    public void setRightActiveIcon(String rightActiveIcon) {
        getStateHelper().put(Properties.rightActiveIcon, rightActiveIcon);
    }

    @Attribute(defaultValue = "getAccordion().getItemDisabledRightIcon()", generate = false)
    public String getRightDisabledIcon() {
        return (String) getStateHelper().eval(Properties.rightDisabledIcon, getAccordion().getItemDisabledRightIcon());
    }

    public void setRightDisabledIcon(String rightDisabledIcon) {
        getStateHelper().put(Properties.rightDisabledIcon, rightDisabledIcon);
    }

    @Attribute(defaultValue = "getAccordion().getItemRightInactiveIcon()", generate = false)
    public String getRightInactiveIcon() {
        return (String) getStateHelper().eval(Properties.rightInactiveIcon, getAccordion().getItemRightInactiveIcon());
    }

    public void setRightInactiveIcon(String rightInactiveIcon) {
        getStateHelper().put(Properties.rightInactiveIcon, rightInactiveIcon);
    }

    @Attribute(defaultValue = "getAccordion().getItemHeaderActiveClass()", generate = false)
    public String getHeaderActiveClass() {
        return (String) getStateHelper().eval(Properties.headerActiveClass, getAccordion().getItemActiveHeaderClass());
    }

    public void setHeaderActiveClass(String headerActiveClass) {
        getStateHelper().put(Properties.headerActiveClass, headerActiveClass);
    }

    @Attribute(defaultValue = "getAccordion().getItemDisabledHeaderClass()", generate = false)
    public String getHeaderDisabledClass() {
        return (String) getStateHelper().eval(Properties.headerDisabledClass, getAccordion().getItemDisabledHeaderClass());
    }

    public void setHeaderDisabledClass(String headerDisabledClass) {
        getStateHelper().put(Properties.headerDisabledClass, headerDisabledClass);
    }

    @Attribute(defaultValue = "getAccordion().getItemInactiveHeaderClass()", generate = false)
    public String getHeaderInactiveClass() {
        return (String) getStateHelper().eval(Properties.headerInactiveClass, getAccordion().getItemInactiveHeaderClass());
    }

    public void setHeaderInactiveClass(String headerInactiveClass) {
        getStateHelper().put(Properties.headerInactiveClass, headerInactiveClass);
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


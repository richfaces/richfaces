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

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;

/**
 * @author akolonitsky
 * @since 2010-08-13
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets), renderer = @JsfRenderer(type = "org.richfaces.AccordionItemRenderer"))
public abstract class AbstractAccordionItem extends AbstractTogglePanelItem implements AbstractTogglePanelTitledItem, ClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "org.richfaces.AccordionItem";

    public static final String COMPONENT_FAMILY = "org.richfaces.AccordionItem";

    enum Properties {
        header,

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

    @Override
    public AbstractAccordion getParentPanel() {
        return ComponentIterators.getParent(this, AbstractAccordion.class);
    }

    public AbstractAccordion getAccordion() {
        return getParentPanel();
    }

    public UIComponent getHeaderFacet(Enum<?> state) {
        return AbstractTab.getHeaderFacet(this, state);
    }

    // ------------------------------------------------ Component Attributes

    @Attribute(generate = false)
    public String getHeader() {
        return (String) getStateHelper().eval(Properties.header, getName());
    }

    public void setHeader(String header) {
        getStateHelper().put(Properties.header, header);
    }

    @Attribute(generate = false)
    public String getLeftActiveIcon() {
        return (String) getStateHelper().eval(Properties.leftActiveIcon, getAccordion().getItemActiveLeftIcon());
    }

    public void setLeftActiveIcon(String leftActiveIcon) {
        getStateHelper().put(Properties.leftActiveIcon, leftActiveIcon);
    }

    @Attribute(generate = false)
    public String getLeftDisabledIcon() {
        return (String) getStateHelper().eval(Properties.leftDisabledIcon, getAccordion().getItemDisabledLeftIcon());
    }

    public void setLeftDisabledIcon(String leftDisabledIcon) {
        getStateHelper().put(Properties.leftDisabledIcon, leftDisabledIcon);
    }


    @Attribute(generate = false)
    public String getLeftInactiveIcon() {
        return (String) getStateHelper().eval(Properties.leftInactiveIcon, getAccordion().getItemInactiveLeftIcon());
    }

    public void setLeftInactiveIcon(String leftInactiveIcon) {
        getStateHelper().put(Properties.leftInactiveIcon, leftInactiveIcon);
    }

    @Attribute(generate = false)
    public String getRightActiveIcon() {
        return (String) getStateHelper().eval(Properties.rightActiveIcon, getAccordion().getItemActiveRightIcon());
    }

    public void setRightActiveIcon(String rightActiveIcon) {
        getStateHelper().put(Properties.rightActiveIcon, rightActiveIcon);
    }

    @Attribute(generate = false)
    public String getRightDisabledIcon() {
        return (String) getStateHelper().eval(Properties.rightDisabledIcon, getAccordion().getItemDisabledRightIcon());
    }

    public void setRightDisabledIcon(String rightDisabledIcon) {
        getStateHelper().put(Properties.rightDisabledIcon, rightDisabledIcon);
    }

    @Attribute(generate = false)
    public String getInactiveRightIcon() {
        return (String) getStateHelper().eval(Properties.rightInactiveIcon, getAccordion().getItemInactiveRightIcon());
    }

    public void setInactiveRightIcon(String inactiveRightIcon) {
        getStateHelper().put(Properties.rightInactiveIcon, inactiveRightIcon);
    }

    @Attribute(generate = false)
    public String getHeaderActiveClass() {
        return (String) getStateHelper().eval(Properties.headerActiveClass, getAccordion().getItemActiveHeaderClass());
    }

    public void setHeaderActiveClass(String headerActiveClass) {
        getStateHelper().put(Properties.headerActiveClass, headerActiveClass);
    }

    @Attribute(generate = false)
    public String getHeaderDisabledClass() {
        return (String) getStateHelper().eval(Properties.headerDisabledClass, getAccordion().getItemDisabledHeaderClass());
    }

    public void setHeaderDisabledClass(String headerDisabledClass) {
        getStateHelper().put(Properties.headerDisabledClass, headerDisabledClass);
    }

    @Attribute(generate = false)
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

    @Attribute(defaultValue = "getAccordion().getItemContentClass()")
    public String getContentClass() {
        return (String) getStateHelper().eval(Properties.contentClass, getAccordion().getItemContentClass());
    }

    public void setContentClass(String contentClass) {
        getStateHelper().put(Properties.contentClass, contentClass);
    }
}


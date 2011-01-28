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

import javax.faces.component.behavior.ClientBehaviorHolder;

/**
 * @author akolonitsky
 * @since 2010-10-19
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets), renderer = @JsfRenderer(type = "org.richfaces.TabRenderer"))
public abstract class AbstractTab extends AbstractTogglePanelTitledItem implements ClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "org.richfaces.Tab";

    public static final String COMPONENT_FAMILY = "org.richfaces.Tab";

    public AbstractTab() {
        setRendererType("org.richfaces.TabRenderer");
    }

    public AbstractTabPanel getTabPanel() {
        return (AbstractTabPanel) this.getParent();
    }

    // ------------------------------------------------ Html Attributes
    enum Properties {
        headerDisabledClass, headerInactiveClass, headerClass, contentClass, headerActiveClass

    }

    @Attribute(generate = false)
    public String getHeaderActiveClass() {
        String value = (String) getStateHelper().eval(Properties.headerActiveClass);
        if (value != null) {
            return value;
        }

        return getTabPanel().getTabActiveHeaderClass();
    }

    public void setHeaderActiveClass(String headerActiveClass) {
        getStateHelper().put(Properties.headerActiveClass, headerActiveClass);
    }


    @Attribute(generate = false)
    public String getHeaderDisabledClass() {
        String value = (String) getStateHelper().eval(Properties.headerDisabledClass);
        if (value != null) {
            return value;
        }

        return getTabPanel().getTabDisabledHeaderClass();
    }

    public void setHeaderDisabledClass(String headerDisabledClass) {
        getStateHelper().put(Properties.headerDisabledClass, headerDisabledClass);
    }

    @Attribute(generate = false)
    public String getHeaderInactiveClass() {
        String value = (String) getStateHelper().eval(Properties.headerInactiveClass);
        if (value != null) {
            return value;
        }

        return getTabPanel().getTabInactiveHeaderClass();
    }

    public void setHeaderInactiveClass(String headerInactiveClass) {
        getStateHelper().put(Properties.headerInactiveClass, headerInactiveClass);
    }

    @Attribute(generate = false)
    public String getHeaderClass() {
        String value = (String) getStateHelper().eval(Properties.headerClass);
        if (value != null) {
            return value;
        }

        return getTabPanel().getTabHeaderClass();
    }

    public void setHeaderClass(String headerClass) {
        getStateHelper().put(Properties.headerClass, headerClass);
    }


    @Attribute
    public abstract String getHeaderStyle();

    @Attribute(generate = false)
    public String getContentClass() {
        String value = (String) getStateHelper().eval(Properties.contentClass);
        if (value != null) {
            return value;
        }

        return getTabPanel().getTabContentClass();
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


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

/**
 * @author akolonitsky
 * @since 2010-08-05
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets, handler = "org.richfaces.view.facelets.html.TogglePanelTagHandler"),
        renderer = @JsfRenderer(type = "org.richfaces.AccordionRenderer"))
public abstract class AbstractAccordion extends AbstractTogglePanel {

    public static final String COMPONENT_TYPE = "org.richfaces.Accordion";

    public static final String COMPONENT_FAMILY = "org.richfaces.Accordion";

    protected AbstractAccordion() {
        setRendererType("org.richfaces.AccordionRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    @Attribute
    public String getActiveItem() {
        String res = super.getActiveItem();
        if (res == null) {
            res = getFirstItem().getName();
        }
        return res;
    }

    // ------------------------------------------------ Html Attributes

    @Attribute
    public abstract String getItemActiveLeftIcon();

    @Attribute
    public abstract String getItemInactiveLeftIcon();

    @Attribute
    public abstract String getItemDisabledLeftIcon();

    @Attribute
    public abstract String getItemActiveRightIcon();

    @Attribute
    public abstract String getItemRightInactiveIcon();

    @Attribute
    public abstract String getItemDisabledRightIcon();

    @Attribute
    public abstract String getWidth();

    @Attribute
    public abstract String getHeight();

    @Attribute
    public abstract String getItemActiveHeaderClass();

    @Attribute
    public abstract String getItemDisabledHeaderClass();

    @Attribute
    public abstract String getItemInactiveHeaderClass();

    @Attribute
    public abstract String getItemContentClass();

    @Attribute
    public abstract String getItemHeaderClass();

    @Attribute(events = @EventName("itemchange"))
    public abstract String getOnitemchange();

    @Attribute(events = @EventName("beforeitemchange"))
    public abstract String getOnbeforeitemchange();

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

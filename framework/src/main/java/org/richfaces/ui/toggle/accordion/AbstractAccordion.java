/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.ui.toggle.accordion;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.ui.attribute.CoreProps;
import org.richfaces.ui.attribute.EventsMouseProps;
import org.richfaces.ui.attribute.I18nProps;
import org.richfaces.ui.toggle.TogglePanelTagHandler;
import org.richfaces.ui.toggle.togglePanel.AbstractTogglePanel;

/**
 * <p>The &lt;r:accordion&gt; is a series of panels stacked on top of each other, each collapsed such that only the
 * header of the panel is showing. When the header of a panel is clicked, it is expanded to show the content of the
 * panel. Clicking on a different header will collapse the previous panel and epand the selected one. Each panel
 * contained in a &lt;r:accordion&gt; component is a &lt;r:accordionItem&gt; component.</p>
 *
 * @author akolonitsky
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets, handlerClass = TogglePanelTagHandler.class),
        renderer = @JsfRenderer(type = "org.richfaces.ui.AccordionRenderer"))
public abstract class AbstractAccordion extends AbstractTogglePanel implements CoreProps, EventsMouseProps, I18nProps {
    public static final String COMPONENT_TYPE = "org.richfaces.ui.Accordion";
    public static final String COMPONENT_FAMILY = "org.richfaces.ui.Accordion";

    protected AbstractAccordion() {
        setRendererType("org.richfaces.AccordionRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    /**
     * Holds the active tab name. This name is a reference to the name identifier of the active child &lt;r:tab&gt;
     * component.
     */
    @Override
    @Attribute
    public String getActiveItem() {
        return super.getActiveItem();
    }

    // ------------------------------------------------ Html Attributes

    /**
     * The icon displayed on the left of the panel header when the panel is active
     */
    @Attribute
    public abstract String getItemActiveLeftIcon();

    /**
     * The icon displayed on the left of the panel header when the panel is not active
     */
    @Attribute
    public abstract String getItemInactiveLeftIcon();

    /**
     * The icon displayed on the left of the panel header when the panel is disabled
     */
    @Attribute
    public abstract String getItemDisabledLeftIcon();

    /**
     * The icon displayed on the right of the panel header when the panel is active
     */
    @Attribute
    public abstract String getItemActiveRightIcon();

    /**
     * The icon displayed on the right of the panel header when the panel is not active
     */
    @Attribute
    public abstract String getItemInactiveRightIcon();

    /**
     * The icon displayed on the right of the panel header when the panel is disabled
     */
    @Attribute
    public abstract String getItemDisabledRightIcon();

    /**
     * The width of the panel
     */
    @Attribute
    public abstract String getWidth();

    /**
     * The height of the panel
     */
    @Attribute
    public abstract String getHeight();

    /**
     * The CSS class applied to the panel header when the panel is active
     */
    @Attribute
    public abstract String getItemActiveHeaderClass();

    /**
     * The CSS class applied to the panel header when the panel is disabled
     */
    @Attribute
    public abstract String getItemDisabledHeaderClass();

    /**
     * The CSS class applied to the panel header when the panel is not active
     */
    @Attribute
    public abstract String getItemInactiveHeaderClass();

    /**
     * A CSS class applied to each of the accordionItem children
     */
    @Attribute
    public abstract String getItemContentClass();

    /**
     * The CSS class applied to the panel header
     */
    @Attribute
    public abstract String getItemHeaderClass();

    /**
     * Points to the function to perform when the switchable item is changed.
     */
    @Attribute(events = @EventName(value = "itemchange", defaultEvent = true))
    public abstract String getOnitemchange();

    /**
     *  Points to the function to perform when before the switchable item is changed
     */
    @Attribute(events = @EventName("beforeitemchange"))
    public abstract String getOnbeforeitemchange();
}

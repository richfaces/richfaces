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
package org.richfaces.ui.output.popupPanel;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.ui.attribute.EventsPopupsBeforeProps;
import org.richfaces.ui.attribute.EventsPopupsProps;
import org.richfaces.ui.attribute.PopupsProps;
import org.richfaces.ui.attribute.StyleClassProps;
import org.richfaces.ui.attribute.StyleProps;

import javax.faces.component.UIComponentBase;

/**
 * <p>The &lt;r:popupPanel&gt; component provides a pop-up panel or window that appears in front of the rest of the
 * application. The &lt;r:popupPanel&gt; component functions either as a modal window which blocks interaction with
 * the rest of the application while active, or as a non-modal window. It can be positioned on the screen, dragged to a
 * new position by the user, and re-sized.</p>
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets),
        renderer = @JsfRenderer(type = "org.richfaces.ui.PopupPanelRenderer"))
public abstract class AbstractPopupPanel extends UIComponentBase implements EventsPopupsProps, EventsPopupsBeforeProps, PopupsProps, StyleProps, StyleClassProps {
    public static final String COMPONENT_TYPE = "org.richfaces.ui.PopupPanel";
    public static final String COMPONENT_FAMILY = "org.richfaces.ui.PopupPanel";

    @Attribute
    public abstract String getStyle();

    @Attribute
    public abstract String getStyleClass();

    /**
     * Defines options that were specified on the client side
     */
    @Attribute
    public abstract String getVisualOptions();

    @Attribute(defaultValue = "100")
    public abstract int getZindex();

    /**
     * Attribute defines height of component. Default value is "300".
     */
    @Attribute(defaultValue = "-1")
    public abstract int getHeight();

    /**
     * Attribute defines width of component. Default value is "200".
     */
    @Attribute(defaultValue = "-1")
    public abstract int getWidth();

    /**
     * Attribute defines min height of component. Default value is "10". If the value is less then 10, a
     * "IllegalArgumentException" exception is thrown.
     */
    @Attribute(defaultValue = "-1")
    public abstract int getMinHeight();

    /**
     * Attribute defines min width of component. Default value is "10". If the value is less then 10, a
     * "IllegalArgumentException" exception is thrown.
     */
    @Attribute(defaultValue = "-1")
    public abstract int getMinWidth();

    /**
     * Attribute defines max height of component.
     */
    @Attribute
    public abstract int getMaxHeight();

    /**
     * Attribute defines max width of component.
     */
    @Attribute
    public abstract int getMaxWidth();

    /**
     * Attribute defines Y position of component left-top corner. Default value is "auto".
     */
    @Attribute
    public abstract String getTop();

    /**
     * Attribute defines X position of component left-top corner. Default value is "auto".
     */
    @Attribute
    public abstract String getLeft();

    /**
     * If "true" value for this attribute makes a modal panel opened as default. Default value is "false"
     */
    @Attribute
    public abstract boolean isShow();

    public abstract void setShow(boolean show);

    /**
     * If "true" there is possibility to move component. Default value is "true".
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isMoveable();

    /**
     * If "true" popupPanel will be auto-sized according to the content. Default value is "false".
     */
    @Attribute
    public abstract boolean isAutosized();

    /**
     * If "true", popupPanel will be modal - it will block the main screen from any operation.
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isModal();

    /**
     * If "true" popupPanel should save state after submission. Default value is "false".
     */
    @Attribute
    public abstract boolean isKeepVisualState();

    /**
     * If "true" popupPanel creates iframe to overlap embed objects like PDF on a page. Default value is "false".
     */
    @Attribute
    public abstract boolean isOverlapEmbedObjects();

    /**
     * If "true" there is possibility to change component size. Default value is "true".
     */
    @Attribute
    public abstract boolean isResizeable();

    /**
     * Defines whether to trim or not elements inside popupPanel. Default value is "true"
     */
    @Attribute
    public abstract boolean isTrimOverlayedElements();

    /**
     * Defines the DOM element, which stacking context will assimilate the popupPanel. Possible values: "body", "form",
     * "parent". Default value is "body".
     */
    @Attribute
    public abstract String getDomElementAttachment();

    /**
     * Assigns one or more space-separated CSS class names to the component controls
     */
    @Attribute
    public abstract String getControlsClass();

    /**
     * Assigns the header for the popupPanel.
     */
    @Attribute
    public abstract String getHeader();

    /**
     * Assigns one or more space-separated CSS class names to the component header
     */
    @Attribute
    public abstract String getHeaderClass();

    /**
     * Pop-up shadow depth for suggestion content
     */
    @Attribute
    public abstract String getShadowDepth();

    /**
     * HTML CSS class attribute of element for pop-up suggestion content
     */
    @Attribute
    public abstract String getShadowOpacity();

    /**
     * Defines whenever the popup will follow the scrolling of the screen. Default value is "true".
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isFollowByScroll();

    /**
     * Javascript code executed when a pointer button is clicked over the popupPanel mask.
     */
    @Attribute(events = @EventName("maskclick"))
    public abstract String getOnmaskclick();

    /**
     * Javascript code executed when a pointer button is double clicked over the popupPanel mask.
     */
    @Attribute(events = @EventName("maskdblclick"))
    public abstract String getOnmaskdblclick();

    /**
     * Javascript code executed when a pointer button is pressed down over the popupPanel mask.
     */
    @Attribute(events = @EventName("maskmousedown"))
    public abstract String getOnmaskmousedown();

    /**
     * Javascript code executed when a pointer button is released over the popupPanel mask.
     */
    @Attribute(events = @EventName("maskmouseup"))
    public abstract String getOnmaskmouseup();

    /**
     * Javascript code executed when a pointer button is moved onto the popupPanel mask.
     */
    @Attribute(events = @EventName("maskmouseover"))
    public abstract String getOnmaskmouseover();

    /**
     * Javascript code executed when a pointer button is moved within the popupPanel mask.
     */
    @Attribute(events = @EventName("maskmousemove"))
    public abstract String getOnmaskmousemove();

    /**
     * Javascript code executed when a pointer button for context menu is clicked over the popupPanel mask.
     */
    @Attribute(events = @EventName("maskcontextmenu"))
    public abstract String getOnmaskcontextmenu();

    /**
     * Javascript code executed when a pointer button is moved away from the popupPanel mask.
     */
    @Attribute(events = @EventName("maskmouseout"))
    public abstract String getOnmaskmouseout();

    /**
     * The client-side script method to be called when the modal panel is resized
     */
    @Attribute(events = @EventName("resize"))
    public abstract String getOnresize();

    @Attribute(events = @EventName("show"))
    public abstract String getOnshow();

    @Attribute(events = @EventName("hide"))
    public abstract String getOnhide();

    /**
     * The client-side script method to be called before the modal panel is moved
     */
    @Attribute(events = @EventName("move"))
    public abstract String getOnmove();

    @Attribute(events = @EventName("beforeshow"))
    public abstract String getOnbeforeshow();

    @Attribute(events = @EventName("beforehide"))
    public abstract String getOnbeforehide();

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }
}

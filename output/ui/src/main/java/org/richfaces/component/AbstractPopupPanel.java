/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package org.richfaces.component;

import javax.faces.component.UIComponentBase;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;

/**
 * JSF component class
 * 
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets), 
	    renderer = @JsfRenderer(type = "org.richfaces.PopupPanelRenderer")
	)
public abstract class AbstractPopupPanel extends UIComponentBase {

    public static final String COMPONENT_TYPE = "org.richfaces.PopupPanel";

    public static final String COMPONENT_FAMILY = "org.richfaces.PopupPanel";
    @Attribute
    public abstract String getVisualOptions();
    @Attribute(defaultValue="100")
    public abstract int getZIndex();
    @Attribute(defaultValue="-1")
    public abstract int getHeight();
    @Attribute(defaultValue="-1")
    public abstract int getWidth();
    @Attribute(defaultValue="-1")
    public abstract int getMinHeight();
    @Attribute(defaultValue="-1")
    public abstract int getMinWidth();
    @Attribute(defaultValue=""+Integer.MAX_VALUE)
    public abstract int getMaxHeight();
    @Attribute(defaultValue=""+Integer.MAX_VALUE)
    public abstract int getMaxWidth();
    @Attribute(defaultValue="auto")
    public abstract String getTop();
    @Attribute(defaultValue="auto")
    public abstract String getLeft();
    @Attribute(defaultValue="false")
    public abstract boolean isShow();
    public abstract void setShow(boolean show);
    @Attribute(defaultValue="true")
    public abstract boolean isMoveable();
    @Attribute(defaultValue="false")
    public abstract boolean isAutosized();
    @Attribute(defaultValue="true")
    public abstract boolean isModal();
    @Attribute(defaultValue="false")
    public abstract boolean isKeepVisualState();
    @Attribute(defaultValue="false")
    public abstract boolean isOverlapEmbedObjects();
    @Attribute(defaultValue="false")
    public abstract boolean isResizeable();
    @Attribute(defaultValue="false")
    public abstract boolean isTrimOverlayedElements();
    @Attribute
    public abstract String getDomElementAttachment();
    @Attribute
    public abstract String getControlsClass();
    @Attribute
    public abstract String getHeader();
    @Attribute
    public abstract String getHeaderClass();
    @Attribute
    public abstract String getShadowDepth();
    @Attribute
    public abstract String getShadowOpacity();
    @Attribute(defaultValue="true")
    public abstract boolean isFollowByScroll();

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }
}

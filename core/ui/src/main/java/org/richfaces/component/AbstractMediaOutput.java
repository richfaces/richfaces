/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
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

import java.util.Date;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.component.UIOutput;

import org.ajax4jsf.resource.ResourceComponent;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.resource.MediaOutputResource;

/**
 * @author shura
 *
 */
@JsfComponent(
        tag = @Tag(generate = false, handler = "org.richfaces.view.facelets.html.MediaOutputHandler", type = TagType.Facelets),
        renderer = @JsfRenderer(type = "org.richfaces.MediaOutputRenderer")
)
public abstract class AbstractMediaOutput extends UIOutput implements ResourceComponent {

    public static final String COMPONENT_TYPE = "org.richfaces.MediaOutput";

    public static final String COMPONENT_FAMILY = "org.richfaces.MediaOutput";

    /**
     * Get URI attribute for resource ( src for images, href for links etc ).
     * @return
     */
    @Attribute
    public abstract String getUriAttribute();

    /**
     * Get Element name for rendering ( imj , a , object, applet ).
     * @return
     */
    @Attribute
    public abstract String getElement();

    public Resource getResource() {
        ResourceHandler resourceHandler = getFacesContext().getApplication().getResourceHandler();
        return resourceHandler.createResource(MediaOutputResource.class.getName());
    }

    @Attribute
    public abstract String getAccesskey();

    @Attribute
    public abstract String getAlign();

    @Attribute
    public abstract String getArchive();

    @Attribute
    public abstract String getBorder();

    @Attribute
    public abstract boolean isCacheable();

    @Attribute
    public abstract String getCharset();

    @Attribute
    public abstract String getClassid();

    @Attribute
    public abstract String getCodebase();

    @Attribute
    public abstract String getCodetype();

    @Attribute
    public abstract String getCoords();

    @Attribute
    public abstract String getDeclare();

    @Attribute
    public abstract String getDir();

    @Attribute
    public abstract Date getExpires();

    @Attribute
    public abstract String getHreflang();

    @Attribute
    public abstract String getHspace();

    @Attribute
    public abstract boolean isIsmap();

    @Attribute
    public abstract String getLang();

    @Attribute
    public abstract Date getLastModified();

    @Attribute
    public abstract String getMimeType();

    @Attribute
    public abstract String getRel();

    @Attribute
    public abstract String getRev();

    @Attribute
    public abstract String getShape();

    @Attribute
    public abstract String getStandby();

    @Attribute
    public abstract String getStyle();

    @Attribute
    public abstract String getStyleClass();

    @Attribute
    public abstract String getTabindex();

    @Attribute
    public abstract String getTarget();

    @Attribute
    public abstract String getTitle();

    @Attribute
    public abstract String getType();

    @Attribute
    public abstract String getUsemap();

    @Attribute
    public abstract String getVspace();

    @Attribute(events = @EventName("blur"))
    public abstract String getOnblur();

    @Attribute(events = @EventName("click"))
    public abstract String getOnclick();

    @Attribute(events = @EventName("dblclick"))
    public abstract String getOndblclick();

    @Attribute(events = @EventName("focus"))
    public abstract String getOnfocus();

    @Attribute(events = @EventName("keydown"))
    public abstract String getOnkeydown();

    @Attribute(events = @EventName("keypress"))
    public abstract String getOnkeypress();

    @Attribute(events = @EventName("keyup"))
    public abstract String getOnkeyup();

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

    @Deprecated
    public boolean isSession() {
        return true;
    }

    @Deprecated
    public void setSession(boolean session) {
        if (!session) {
            // TODO: log
        }
    }
}

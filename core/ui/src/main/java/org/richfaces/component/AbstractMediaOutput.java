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

import java.io.OutputStream;
import java.util.Date;

import javax.el.MethodExpression;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;

import org.ajax4jsf.resource.ResourceComponent2;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Signature;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.resource.MediaOutputResource;
import org.richfaces.resource.UserResourceWrapper;
import org.richfaces.webapp.taglib.MethodBindingMethodExpressionAdaptor;
import org.richfaces.webapp.taglib.MethodExpressionMethodBindingAdaptor;

/**
 * @author shura
 *
 */
@JsfComponent(
        tag = @Tag(generate = false, handler = "org.richfaces.view.facelets.html.MediaOutputHandler", type = TagType.Facelets),
        renderer = @JsfRenderer(type = "org.richfaces.MediaOutputRenderer")
)
public abstract class AbstractMediaOutput extends UIOutput implements ResourceComponent2 {

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

    /**
     * Get EL binding to method in user bean to send resource. Method will
     * called with two parameters - restored data object and servlet output
     * stream.
     *
     * @return MethodBinding to createContent
     */
    @Attribute(signature = @Signature(parameters = {OutputStream.class, Object.class}))
    public MethodBinding getCreateContent() {
        MethodBinding result = null;
        MethodExpression me = getCreateContentExpression();

        if (me != null) {

            // if the MethodExpression is an instance of our private
            // wrapper class.
            if (me instanceof MethodExpressionMethodBindingAdaptor) {
                result = ((MethodExpressionMethodBindingAdaptor) me).getBinding();
            } else {

                // otherwise, this is a real MethodExpression.  Wrap it
                // in a MethodBinding.
                result = new MethodBindingMethodExpressionAdaptor(me);
            }
        }

        return result;
    }

    /**
     * Set EL binding to method in user bean to send resource. Method will
     * called with two parameters - restored data object and servlet output
     * stream.
     *
     * @param newvalue - new value of createContent method binding
     */
    public void setCreateContent(MethodBinding newvalue) {
        MethodExpressionMethodBindingAdaptor adapter;

        if (newvalue != null) {
            adapter = new MethodExpressionMethodBindingAdaptor(newvalue);
            setCreateContentExpression(adapter);
        } else {
            setCreateContentExpression(null);
        }
    }

    public Resource getResource() {
        FacesContext facesContext = getFacesContext();
        ResourceHandler resourceHandler = facesContext.getApplication().getResourceHandler();
        Resource resource = resourceHandler.createResource(MediaOutputResource.class.getName());
        
        MediaOutputResource mediaResource = (MediaOutputResource) ((UserResourceWrapper) resource).getWrapped();
        mediaResource.initialize(this);

        return resource;
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

    public abstract MethodExpression getCreateContentExpression();

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

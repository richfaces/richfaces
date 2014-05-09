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

import javax.el.MethodExpression;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.component.UIOutput;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.component.attribute.AccesskeyProps;
import org.richfaces.resource.MediaOutputResource;
import org.richfaces.component.attribute.EventsKeyProps;
import org.richfaces.component.attribute.EventsMouseProps;
import org.richfaces.component.attribute.FocusProps;
import org.richfaces.component.attribute.I18nProps;
import org.richfaces.view.facelets.html.MediaOutputHandler;

/**
 * <p>
 * The &lt;a4j:mediaOutput&gt; component is used for generating images, video, sounds, and other resources defined on the fly.
 * </p>
 *
 * @author shura
 */
@JsfComponent(tag = @Tag(generate = false, handlerClass = MediaOutputHandler.class, type = TagType.Facelets),
        renderer = @JsfRenderer(type = "org.richfaces.MediaOutputRenderer"))
public abstract class AbstractMediaOutput extends UIOutput implements AccesskeyProps, EventsKeyProps, EventsMouseProps, FocusProps, I18nProps {
    public static final String COMPONENT_TYPE = "org.richfaces.MediaOutput";
    public static final String COMPONENT_FAMILY = "org.richfaces.MediaOutput";

    public Resource getResource() {
        ResourceHandler resourceHandler = getFacesContext().getApplication().getResourceHandler();
        return resourceHandler.createResource(MediaOutputResource.class.getName());
    }

    /**
     * Get URI attribute for resource ( src for images, href for links etc ).
     */
    @Attribute
    public abstract String getUriAttribute();

    /**
     * Get Element name for rendering ( imj , a , object, applet ).
     */
    @Attribute
    public abstract String getElement();

    @Attribute
    public abstract String getAccesskey();

    /**
     * Deprecated. This attribute specifies the position of an IMG, OBJECT, or APPLET with respect to its context.
     * The possible values are "bottom", "middle", "top", "left" and "right". The default value is "middle".
     */
    @Attribute
    public abstract String getAlign();

    /**
     * Specifies a space-separated list of URIs
     */
    @Attribute
    public abstract String getArchive();

    /**
     * Deprecated. This attribute specifies the width of an IMG or OBJECT border, in pixels.
     * The default value for this attribute depends on the user agent
     */
    @Attribute
    public abstract String getBorder();

    /**
     * Attribute is a flag that defines the caching strategy. If 'cacheable' is set to false, the response will not be cached.
     * If it is set to true, it will be cached and the serialized value of 'value' attribute plays the role of a cache key.
     */
    @Attribute
    public abstract boolean isCacheable();

    /**
     * The character encoding of a resource designated by this hyperlink
     */
    @Attribute
    public abstract String getCharset();

    /**
     * Identifies an implementation
     */
    @Attribute
    public abstract String getClassid();

    /**
     * Base URI for classid, data, archive
     */
    @Attribute
    public abstract String getCodebase();

    /**
     * Defines content type for code
     */
    @Attribute
    public abstract String getCodetype();

    /**
     * <p>
     * The attribute specifies shape and it position on the screen. Possible values: "rect: left-x, top-y, right-x,
     * bottom-y", "circle: center-x, center-y, radius", "poly: x1, y1, x2, y2, ..., xN, yN".
     * </p>
     * <p>
     * Notes:
     * <ol>
     *     <li>when giving the radius value in percents, user agents should calculate the final radius value in pixels
     * based on the associated object's width and height;</li>
     *      <li>the radius value should be smaller than center-x and center-y values;</li>
     *      <li>for a polygon, the first and last coordinate pairs should have same x and y to close the shape (x1=xN; y1=yN)
     * (when these coordinates are different, user agents should infer an additional pair to close a polygon).</li>
     * </ol>
     * Coordinates are relative to the top left corner of an object. All values are lengths. All values are comma separated.
     * </p>
     */
    @Attribute
    public abstract String getCoords();

    /**
     * Declare but don't instantiate flag
     */
    @Attribute
    public abstract String getDeclare();

    public abstract String getDir();

    /**
     * The attribute allows to manage caching and defines the period after which a resource is reloaded.
     */
    @Attribute
    public abstract Date getExpires();

    /**
     * Base language of a resource specified with the href attribute; hreflang may only be used with href
     */
    @Attribute
    public abstract String getHreflang();

    /**
     * Deprecated. This attribute specifies the amount of white space to be inserted to the left and right of an
     * IMG, APPLET, or OBJECT. The default value is not specified, but is generally a small, non-zero length
     */
    @Attribute
    public abstract String getHspace();

    /**
     * Use server-side image map
     */
    @Attribute
    public abstract boolean isIsmap();

    @Attribute
    public abstract String getLang();

    /**
     * The attribute allows to manage caching. A browser can send request with the header "If-Modified-Since" for
     * necessity of object reloading.
     * If time of modification is earlier, then the framework doesn't call generation and return code 304.
     */
    @Attribute
    public abstract Date getLastModified();

    /**
     * Geterated content mime-type for append to response header ( 'image/jpeg' etc )
     */
    @Attribute
    public abstract String getMimeType();

    /**
     * Method call expression to send generated resource to OutputStream. It must have two parameter with a type of
     * java.io.OutputStream and java.lang.Object ( deserialized value of data attribute )
     */
    @Attribute
    public abstract MethodExpression getCreateContent();

    public abstract void setCreateContent(MethodExpression createContent);

    /**
     * The relationship from the current document to the anchor specified by this hyperlink.
     * The value of this attribute is a space-separated list of link types
     */
    @Attribute
    public abstract String getRel();

    /**
     * A reverse link from the anchor specified by this hyperlink to the current document.
     * The value of this attribute is a space-separated list of link types
     */
    @Attribute
    public abstract String getRev();

    /**
     * This attribute specifies the shape of a region. The possible values are "default", "rect", "circle" and "poly".
     */
    @Attribute
    public abstract String getShape();

    /**
     * Message to show while loading
     */
    @Attribute
    public abstract String getStandby();

    /**
     * CSS style rules to be applied to the component
     */
    @Attribute
    public abstract String getStyle();

    /**
     * Assigns one or more CSS class names to the component. Corresponds to the HTML "class" attribute.
     */
    @Attribute
    public abstract String getStyleClass();

    /**
     * This attribute specifies the position of the current element in the tabbing order for the current document.
     * This value must be a number between 0 and 32767. User agents should ignore leading zeros
     */
    @Attribute
    public abstract String getTabindex();

    /**
     * This attribute specifies the name of a frame where a document is to be opened. By assigning a name to a frame
     * via the name attribute, authors can refer to it as the "target" of links defined by other elements
     */
    @Attribute
    public abstract String getTarget();

    /**
     * Advisory title information about markup elements generated for this component
     */
    @Attribute
    public abstract String getTitle();

    /**
     * The content type of the resource designated by this hyperlink
     */
    @Attribute
    public abstract String getType();

    /**
     * Specifies an image as a client-side image-map
     */
    @Attribute
    public abstract String getUsemap();

    /**
     * Deprecated. This attribute specifies the amount of white space to be inserted above and below an IMG, APPLET, or OBJECT.
     * The default value is not specified, but is generally a small, non-zero length
     */
    @Attribute
    public abstract String getVspace();

    /**
     * Filename of the resource to be served
     */
    @Attribute
    public abstract String getFileName();

    /**
     * <p>
     * Data value calculated at render time and stored in URI (also as part of cache Key ), at generation time passed
     * to send method. Can be used for update cache at change of generating conditions, and for creating beans as
     * "Lightweight" pattern components (request scope).
     * </p>
     * <p>
     * IMPORTANT: Since serialized data stored in URI, avoid using big objects.
     * </p>
     */
    @Attribute
    public abstract Object getValue();

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
}

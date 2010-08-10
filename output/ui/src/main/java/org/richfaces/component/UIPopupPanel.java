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

import java.util.HashMap;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.component.UIComponentBase;

import org.richfaces.json.JSONException;
import org.richfaces.json.JSONMap;

/**
 * JSF component class
 * 
 */
public class UIPopupPanel extends UIComponentBase {

    public static final String COMPONENT_TYPE = "org.richfaces.PopupPanel";

    public static final String COMPONENT_FAMILY = "org.richfaces.PopupPanel";

    protected enum PropertyKeys {
        width, height, zIndex, trimOverlayedElements, minHeight, minWidth, maxHeight, maxWidth, top, left, moveable, autosized, modal, domElementAttachment, controlsClass, show, headerClass, keepVisualState, overlapEmbedObjects, resizeable, shadowDepth, shadowOpacity, style, styleClass, visualOptions
    }

    public Map<String, Object> getHandledVisualOptions() {
        String options = (String) getStateHelper().eval(PropertyKeys.visualOptions);
        Map<String, Object> result;
        result = prepareVisualOptions(options);

        if (null == result) {
            result = new HashMap<String, Object>();
        }
        return result;
    }

    //TODO nick - CDK should generate all these properties' code
    public String getVisualOptions() {
        return (String) getStateHelper().eval(PropertyKeys.visualOptions);
    }

    public void setVisualOptions(String visualOptions) {
        getStateHelper().put(PropertyKeys.visualOptions, visualOptions);
    }

    public int getZIndex() {
        return (Integer) getStateHelper().eval(PropertyKeys.zIndex, 100);
    }

    public void setZIndex(int zIndex) {
        getStateHelper().put(PropertyKeys.zIndex, zIndex);
    }

    public int getHeight() {
        return (Integer) getStateHelper().eval(PropertyKeys.height, -1);
    }

    public void setHeight(int height) {
        getStateHelper().put(PropertyKeys.height, height);
    }

    public int getWidth() {
        return (Integer) getStateHelper().eval(PropertyKeys.width, -1);
    }

    public void setWidth(int width) {
        getStateHelper().put(PropertyKeys.width, width);
    }

    public int getMinHeight() {
        return (Integer) getStateHelper().eval(PropertyKeys.minHeight, -1);
    }

    public void setMinHeight(int minheight) {
        getStateHelper().put(PropertyKeys.minHeight, minheight);
    }

    public int getMinWidth() {
        return (Integer) getStateHelper().eval(PropertyKeys.minWidth, -1);
    }

    public void setMinWidth(int minWidth) {
        getStateHelper().put(PropertyKeys.minWidth, minWidth);
    }

    public int getMaxHeight() {
        return (Integer) getStateHelper().eval(PropertyKeys.maxHeight, Integer.MAX_VALUE);
    }

    public void setMaxHeight(int maxheight) {
        getStateHelper().put(PropertyKeys.maxHeight, maxheight);
    }

    public int getMaxWidth() {
        return (Integer) getStateHelper().eval(PropertyKeys.maxWidth, Integer.MAX_VALUE);
    }

    public void setMaxWidth(int maxWidth) {
        getStateHelper().put(PropertyKeys.maxWidth, maxWidth);
    }

    public String getTop() {
        return (String) getStateHelper().eval(PropertyKeys.top, "auto");
    }

    public void setTop(String top) {
        getStateHelper().put(PropertyKeys.top, top);
    }

    public String getLeft() {
        return (String) getStateHelper().eval(PropertyKeys.left, "auto");
    }

    public void setLeft(String left) {
        getStateHelper().put(PropertyKeys.left, left);
    }

    public boolean isShow() {
        return (Boolean) getStateHelper().eval(PropertyKeys.show, false);
    }

    public void setShow(boolean show) {
        getStateHelper().put(PropertyKeys.show, show);
    }

    public boolean isMoveable() {
        return (Boolean) getStateHelper().eval(PropertyKeys.moveable, true);
    }

    public void setMoveable(boolean moveable) {
        getStateHelper().put(PropertyKeys.moveable, moveable);
    }

    public boolean isAutosized() {
        return (Boolean) getStateHelper().eval(PropertyKeys.autosized, false);
    }

    public void setAutosized(boolean autosized) {
        getStateHelper().put(PropertyKeys.autosized, autosized);
    }

    public boolean isModal() {
        return (Boolean) getStateHelper().eval(PropertyKeys.modal, true);
    }

    public void setModal(boolean modal) {
        getStateHelper().put(PropertyKeys.modal, modal);
    }

    public boolean isKeepVisualState() {
        return (Boolean) getStateHelper().eval(PropertyKeys.keepVisualState, false);
    }

    public void setKeepVisualState(boolean keepVisualState) {
        getStateHelper().put(PropertyKeys.keepVisualState, keepVisualState);
    }

    public boolean isOverlapEmbedObjects() {
        return (Boolean) getStateHelper().eval(PropertyKeys.overlapEmbedObjects, false);
    }

    public void setOverlapEmbedObjects(boolean overlapEmbedObjects) {
        getStateHelper().put(PropertyKeys.overlapEmbedObjects, overlapEmbedObjects);
    }

    public boolean isResizeable() {
        return (Boolean) getStateHelper().eval(PropertyKeys.resizeable, true);
    }

    public void setResizeable(boolean resizeable) {
        getStateHelper().put(PropertyKeys.resizeable, resizeable);
    }

    public boolean isTrimOverlayedElements() {
        return (Boolean) getStateHelper().eval(
            PropertyKeys.trimOverlayedElements, false);
    }

    public void setTrimOverlayedElements(boolean trimOverlayedElements) {
        getStateHelper().put(PropertyKeys.trimOverlayedElements,
            trimOverlayedElements);
    }

    public String getDomElementAttachment() {
        return (String) getStateHelper()
            .eval(PropertyKeys.domElementAttachment);
    }

    public void setDomElementAttachment(String domElementAttachment) {
        getStateHelper().put(PropertyKeys.domElementAttachment, domElementAttachment);
    }

    public String getControlsClass() {
        return (String) getStateHelper().eval(PropertyKeys.controlsClass);
    }

    public void setControlsClass(String controlsClass) {
        getStateHelper().put(PropertyKeys.controlsClass, controlsClass);
    }

    /*
     * public String getLabel() { return (String) getStateHelper().eval(PropertyKeys.label); }
     * 
     * public void setLabel(String label) { getStateHelper().put(PropertyKeys.label, label); }
     */

    public String getHeaderClass() {
        return (String) getStateHelper().eval(PropertyKeys.headerClass);
    }

    public void setHeaderClass(String headerClass) {
        getStateHelper().put(PropertyKeys.headerClass, headerClass);
    }

    /*
     * public String getScrollerClass() { return (String) getStateHelper().eval(PropertyKeys.scrollerClass); }
     * 
     * public void setScrollerClass(String scrollerClass) { getStateHelper().put(PropertyKeys.scrollerClass,
     * scrollerClass); }
     */

    public String getShadowDepth() {
        return (String) getStateHelper().eval(PropertyKeys.shadowDepth);
    }

    public void setShadowDepth(String shadowDepth) {
        getStateHelper().put(PropertyKeys.shadowDepth, shadowDepth);
    }

    public String getShadowOpacity() {
        return (String) getStateHelper().eval(PropertyKeys.shadowOpacity);
    }

    public void setShadowOpacity(String shadowOpacity) {
        getStateHelper().put(PropertyKeys.shadowOpacity, shadowOpacity);
    }

    //TODO nick - this should part of renderer
    private Map<String, Object> prepareVisualOptions(Object value) {
        if (null == value) {
            return new HashMap<String, Object>();
        } else if (value instanceof Map) {
            return (Map<String, Object>) value;
        } else if (value instanceof String) {
            String s = (String) value;
            if (!s.startsWith("{")) {
                s = "{" + s + "}";
            }
            try {
                return new HashMap<String, Object>(new JSONMap(s));
            } catch (JSONException e) {
                throw new FacesException(e);
            }
        } else {
            throw new FacesException("Attribute visualOptions of component [" + this.getClientId(getFacesContext())
                + "] must be instance of Map or String, but its type is " + value.getClass().getSimpleName());
        }
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }
}

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


package org.richfaces.component.html;

import org.richfaces.component.UITooltip;
import javax.faces.component.behavior.ClientBehaviorHolder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * @author amarkhel
 * @since 2010-10-24
 */
public class HtmlTooltip extends UITooltip implements ClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "org.richfaces.Tooltip";

    public static final String COMPONENT_FAMILY = "org.richfaces.Tooltip";

    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList(
        "click",
        "dblclick",
        "mousedown",
        "mousemove",
        "mouseout",
        "mouseover",
        "mouseup",
        "hide",
        "show",
        "beforehide",
        "beforeshow"
    ));


    public enum PropertyKeys {
        style,
        styleClass,
        zindex,
        onclick,
        ondblclick,
        onmousedown,
        onmousemove,
        onmouseout,
        onmouseover,
        onmouseup,
        onhide,
        onshow,
        onbeforehide,
        onbeforeshow
    }

    public HtmlTooltip() {
        setRendererType("org.richfaces.Tooltip");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getStyle() {
        return (String) getStateHelper().eval(PropertyKeys.style);
    }

    public void setStyle(String style) {
        getStateHelper().put(PropertyKeys.style, style);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass);
    }

    public void setStyleClass(String styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, styleClass);
    }

    public int getZindex() {
        return (Integer) getStateHelper().eval(PropertyKeys.zindex);
    }

    public void setZindex(int zindex) {
        getStateHelper().put(PropertyKeys.zindex, zindex);
    }

    public String getOnclick() {
        return (String) getStateHelper().eval(PropertyKeys.onclick);
    }

    public void setOnclick(String onclick) {
        getStateHelper().put(PropertyKeys.onclick, onclick);
    }

    public String getOndblclick() {
        return (String) getStateHelper().eval(PropertyKeys.ondblclick);
    }

    public void setOndblclick(String ondblclick) {
        getStateHelper().put(PropertyKeys.ondblclick, ondblclick);
    }

    public String getOnmousedown() {
        return (String) getStateHelper().eval(PropertyKeys.onmousedown);
    }

    public void setOnmousedown(String onmousedown) {
        getStateHelper().put(PropertyKeys.onmousedown, onmousedown);
    }

    public String getOnmousemove() {
        return (String) getStateHelper().eval(PropertyKeys.onmousemove);
    }

    public void setOnmousemove(String onmousemove) {
        getStateHelper().put(PropertyKeys.onmousemove, onmousemove);
    }

    public String getOnmouseout() {
        return (String) getStateHelper().eval(PropertyKeys.onmouseout);
    }

    public void setOnmouseout(String onmouseout) {
        getStateHelper().put(PropertyKeys.onmouseout, onmouseout);
    }

    public String getOnmouseover() {
        return (String) getStateHelper().eval(PropertyKeys.onmouseover);
    }

    public void setOnmouseover(String onmouseover) {
        getStateHelper().put(PropertyKeys.onmouseover, onmouseover);
    }

    public String getOnmouseup() {
        return (String) getStateHelper().eval(PropertyKeys.onmouseup);
    }

    public void setOnmouseup(String onmouseup) {
        getStateHelper().put(PropertyKeys.onmouseup, onmouseup);
    }

    public String getOnhide() {
        return (String) getStateHelper().eval(PropertyKeys.onhide);
    }

    public void setOnhide(String onhide) {
        getStateHelper().put(PropertyKeys.onhide, onhide);
    }

    public String getOnshow() {
        return (String) getStateHelper().eval(PropertyKeys.onshow);
    }

    public void setOnshow(String onshow) {
        getStateHelper().put(PropertyKeys.onshow, onshow);
    }

    public String getOnbeforehide() {
        return (String) getStateHelper().eval(PropertyKeys.onbeforehide);
    }

    public void setOnbeforehide(String onbeforehide) {
        getStateHelper().put(PropertyKeys.onbeforehide, onbeforehide);
    }

    public String getOnbeforeshow() {
        return (String) getStateHelper().eval(PropertyKeys.onbeforeshow);
    }

    public void setOnbeforeshow(String onbeforeshow) {
        getStateHelper().put(PropertyKeys.onbeforeshow, onbeforeshow);
    }



    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }
}


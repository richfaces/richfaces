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

import org.richfaces.component.UIPanelMenuItem;
import javax.faces.component.behavior.ClientBehaviorHolder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * @author akolonitsky
 * @since 2010-10-25
 */
public class HtmlPanelMenuItem extends UIPanelMenuItem implements ClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "org.richfaces.PanelMenuItem";

    public static final String COMPONENT_FAMILY = "org.richfaces.PanelMenuItem";

    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList(
        "beforedomupdate",
        "complete",
        "click",
        "dblclick",
        "mousedown",
        "mousemove",
        "mouseout",
        "mouseover",
        "mouseup",
        "unselect",
        "select",
        "beforeselect"
    ));


    public enum PropertyKeys {
        disabledClass,
        disabledStyle,
        hoverClass,
        hoverStyle,
        iconClass,
        iconDisabled,
        iconStyle,
        style,
        styleClass,
        onbeforedomupdate,
        oncomplete,
        onclick,
        ondblclick,
        onmousedown,
        onmousemove,
        onmouseout,
        onmouseover,
        onmouseup,
        onunselect,
        onselect,
        onbeforeselect
    }

    public HtmlPanelMenuItem() {
        setRendererType("org.richfaces.PanelMenuItem");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getDisabledClass() {
        return (String) getStateHelper().eval(PropertyKeys.disabledClass);
    }

    public void setDisabledClass(String disabledClass) {
        getStateHelper().put(PropertyKeys.disabledClass, disabledClass);
    }

    public String getDisabledStyle() {
        return (String) getStateHelper().eval(PropertyKeys.disabledStyle);
    }

    public void setDisabledStyle(String disabledStyle) {
        getStateHelper().put(PropertyKeys.disabledStyle, disabledStyle);
    }

    public String getHoverClass() {
        return (String) getStateHelper().eval(PropertyKeys.hoverClass);
    }

    public void setHoverClass(String hoverClass) {
        getStateHelper().put(PropertyKeys.hoverClass, hoverClass);
    }

    public String getHoverStyle() {
        return (String) getStateHelper().eval(PropertyKeys.hoverStyle);
    }

    public void setHoverStyle(String hoverStyle) {
        getStateHelper().put(PropertyKeys.hoverStyle, hoverStyle);
    }

    public String getIconClass() {
        return (String) getStateHelper().eval(PropertyKeys.iconClass);
    }

    public void setIconClass(String iconClass) {
        getStateHelper().put(PropertyKeys.iconClass, iconClass);
    }

    public String getIconDisabled() {
        return (String) getStateHelper().eval(PropertyKeys.iconDisabled);
    }

    public void setIconDisabled(String iconDisabled) {
        getStateHelper().put(PropertyKeys.iconDisabled, iconDisabled);
    }

    public String getIconStyle() {
        return (String) getStateHelper().eval(PropertyKeys.iconStyle);
    }

    public void setIconStyle(String iconStyle) {
        getStateHelper().put(PropertyKeys.iconStyle, iconStyle);
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

    public String getOnbeforedomupdate() {
        return (String) getStateHelper().eval(PropertyKeys.onbeforedomupdate);
    }

    public void setOnbeforedomupdate(String onbeforedomupdate) {
        getStateHelper().put(PropertyKeys.onbeforedomupdate, onbeforedomupdate);
    }

    public String getOncomplete() {
        return (String) getStateHelper().eval(PropertyKeys.oncomplete);
    }

    public void setOncomplete(String oncomplete) {
        getStateHelper().put(PropertyKeys.oncomplete, oncomplete);
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

    public String getOnunselect() {
        return (String) getStateHelper().eval(PropertyKeys.onunselect);
    }

    public void setOnunselect(String onunselect) {
        getStateHelper().put(PropertyKeys.onunselect, onunselect);
    }

    public String getOnselect() {
        return (String) getStateHelper().eval(PropertyKeys.onselect);
    }

    public void setOnselect(String onselect) {
        getStateHelper().put(PropertyKeys.onselect, onselect);
    }

    public String getOnbeforeselect() {
        return (String) getStateHelper().eval(PropertyKeys.onbeforeselect);
    }

    public void setOnbeforeselect(String onbeforeselect) {
        getStateHelper().put(PropertyKeys.onbeforeselect, onbeforeselect);
    }



    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }
}


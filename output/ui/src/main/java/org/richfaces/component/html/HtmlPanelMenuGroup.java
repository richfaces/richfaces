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

import org.richfaces.component.UIPanelMenuGroup;

import javax.faces.component.behavior.ClientBehaviorHolder;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * @author akolonitsky
 * @since 2010-11-29
 */
public class HtmlPanelMenuGroup extends UIPanelMenuGroup implements ClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "org.richfaces.PanelMenuGroup";

    public static final String COMPONENT_FAMILY = "org.richfaces.PanelMenuGroup";

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
        "beforeselect",

        "collapse",
        "expand",
        "switch",
        "beforecollapse",
        "beforeexpand",
        "beforeswitch"
    ));


    public enum PropertyKeys {
        iconLeftCollapsed,
        iconLeftExpanded,
        iconLeftDisabled,

        iconRightCollapsed,
        iconRightExpanded,
        iconRightDisabled,

        oncollapse,
        onexpand,
        onswitch,
        onbeforecollapse,
        onbeforeexpand,
        onbeforeswitch,

        disabledClass,
        hoverClass,
        iconLeftClass,
        iconRightClass,
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

    public HtmlPanelMenuGroup() {
        setRendererType("org.richfaces.PanelMenuGroup");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public HtmlPanelMenu getPanelMenu() {
        return (HtmlPanelMenu) super.getPanelMenu();
    }

    public String getIconLeftCollapsed() {
        return (String) getStateHelper().eval(PropertyKeys.iconLeftCollapsed,
            isTopItem() ? getPanelMenu().getTopGroupCollapseIconLeft() : getPanelMenu().getGroupCollapseIconLeft());
    }

    public void setIconLeftCollapsed(String iconLeftCollapsed) {
        getStateHelper().put(PropertyKeys.iconLeftCollapsed, iconLeftCollapsed);
    }

    public String getIconLeftExpanded() {
        return (String) getStateHelper().eval(PropertyKeys.iconLeftExpanded,
            isTopItem() ? getPanelMenu().getTopGroupExpandIconLeft() : getPanelMenu().getGroupExpandIconLeft());
    }

    public void setIconLeftExpanded(String iconLeftExpanded) {
        getStateHelper().put(PropertyKeys.iconLeftExpanded, iconLeftExpanded);
    }

    public String getIconLeftDisabled() {
        return (String) getStateHelper().eval(PropertyKeys.iconLeftDisabled,
            isTopItem() ? getPanelMenu().getTopGroupDisableIconLeft() : getPanelMenu().getGroupDisableIconLeft());
    }

    public void setIconLeftDisabled(String iconLeftDisabled) {
        getStateHelper().put(PropertyKeys.iconLeftDisabled, iconLeftDisabled);
    }

    public String getIconRightCollapsed() {
        return (String) getStateHelper().eval(PropertyKeys.iconRightCollapsed,
            isTopItem() ? getPanelMenu().getTopGroupCollapseIconRight() : getPanelMenu().getGroupCollapseIconRight());
    }

    public void setIconRightCollapsed(String iconRightCollapsed) {
        getStateHelper().put(PropertyKeys.iconRightCollapsed, iconRightCollapsed);
    }

    public String getIconRightExpanded() {
        return (String) getStateHelper().eval(PropertyKeys.iconRightExpanded,
            isTopItem() ? getPanelMenu().getTopGroupExpandIconRight() : getPanelMenu().getGroupExpandIconRight());
    }

    public void setIconRightExpanded(String iconRightExpanded) {
        getStateHelper().put(PropertyKeys.iconRightExpanded, iconRightExpanded);
    }

    public String getIconRightDisabled() {
        return (String) getStateHelper().eval(PropertyKeys.iconRightDisabled,
            isTopItem() ? getPanelMenu().getTopGroupDisableIconRight() : getPanelMenu().getGroupDisableIconRight());
    }

    public void setIconRightDisabled(String iconRightDisabled) {
        getStateHelper().put(PropertyKeys.iconRightDisabled, iconRightDisabled);
    }

    public String getOncollapse() {
        return (String) getStateHelper().eval(PropertyKeys.oncollapse);
    }

    public void setOncollapse(String oncollapse) {
        getStateHelper().put(PropertyKeys.oncollapse, oncollapse);
    }

    public String getOnexpand() {
        return (String) getStateHelper().eval(PropertyKeys.onexpand);
    }

    public void setOnexpand(String onexpand) {
        getStateHelper().put(PropertyKeys.onexpand, onexpand);
    }

    public String getOnswitch() {
        return (String) getStateHelper().eval(PropertyKeys.onswitch);
    }

    public void setOnswitch(String onswitch) {
        getStateHelper().put(PropertyKeys.onswitch, onswitch);
    }

    public String getOnbeforecollapse() {
        return (String) getStateHelper().eval(PropertyKeys.onbeforecollapse);
    }

    public void setOnbeforecollapse(String onbeforecollapse) {
        getStateHelper().put(PropertyKeys.onbeforecollapse, onbeforecollapse);
    }

    public String getOnbeforeexpand() {
        return (String) getStateHelper().eval(PropertyKeys.onbeforeexpand);
    }

    public void setOnbeforeexpand(String onbeforeexpand) {
        getStateHelper().put(PropertyKeys.onbeforeexpand, onbeforeexpand);
    }

    public String getOnbeforeswitch() {
        return (String) getStateHelper().eval(PropertyKeys.onbeforeswitch);
    }

    public void setOnbeforeswitch(String onbeforeswitch) {
        getStateHelper().put(PropertyKeys.onbeforeswitch, onbeforeswitch);
    }

    public String getDisabledClass() {
        return (String) getStateHelper().eval(PropertyKeys.disabledClass,
            isTopItem() ? getPanelMenu().getTopGroupDisableClass() : getPanelMenu().getGroupDisableClass());
    }

    public void setDisabledClass(String disabledClass) {
        getStateHelper().put(PropertyKeys.disabledClass, disabledClass);
    }

    public String getHoverClass() {
        return (String) getStateHelper().eval(PropertyKeys.hoverClass);
    }

    public void setHoverClass(String hoverClass) {
        getStateHelper().put(PropertyKeys.hoverClass, hoverClass);
    }

    public String getIconLeftClass() {
        return (String) getStateHelper().eval(PropertyKeys.iconLeftClass);
    }

    public void setIconLeftClass(String iconLeftClass) {
        getStateHelper().put(PropertyKeys.iconLeftClass, iconLeftClass);
    }

    public String getIconRightClass() {
        return (String) getStateHelper().eval(PropertyKeys.iconRightClass);
    }

    public void setIconRightClass(String iconRightClass) {
        getStateHelper().put(PropertyKeys.iconRightClass, iconRightClass);
    }

    public String getStyle() {
        return (String) getStateHelper().eval(PropertyKeys.style);
    }

    public void setStyle(String style) {
        getStateHelper().put(PropertyKeys.style, style);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass,
            isTopItem() ? getPanelMenu().getTopGroupClass() : getPanelMenu().getGroupClass() );
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


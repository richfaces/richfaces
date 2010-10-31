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

import org.richfaces.component.UIPanelMenu;
import javax.faces.component.behavior.ClientBehaviorHolder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * @author akolonitsky
 * @since 2010-10-25
 */
public class HtmlPanelMenu extends UIPanelMenu implements ClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "org.richfaces.PanelMenu";

    public static final String COMPONENT_FAMILY = "org.richfaces.PanelMenu";

    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList(
        "click",
        "dblclick",
        "mousedown",
        "mousemove",
        "mouseout",
        "mouseover",
        "mouseup"
    ));


    public enum PropertyKeys {
        style,
        styleClass,
        width,
        itemClass,
        itemHoverClass,
        itemDisableClass,
        itemIcon,
        itemDisableIcon,
        itemIconPosition,
        topItemClass,
        topItemHoverClass,
        topItemDisableClass,
        topItemIcon,
        topItemDisableIcon,
        topItemIconPosition,
        groupClass,
        groupHoverClass,
        groupDisableClass,
        groupCollapseIcon,
        groupExpandIcon,
        groupDisableIcon,
        groupIconPosition,
        topGroupClass,
        topGroupHoverClass,
        topGroupDisableClass,
        topGroupCollapseIcon,
        topGroupExpandIcon,
        topGroupDisableIcon,
        topGroupIconPosition,
        onclick,
        ondblclick,
        onmousedown,
        onmousemove,
        onmouseout,
        onmouseover,
        onmouseup
    }

    public HtmlPanelMenu() {
        setRendererType("org.richfaces.PanelMenu");
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

    public String getWidth() {
        return (String) getStateHelper().eval(PropertyKeys.width);
    }

    public void setWidth(String width) {
        getStateHelper().put(PropertyKeys.width, width);
    }

    public String getItemClass() {
        return (String) getStateHelper().eval(PropertyKeys.itemClass);
    }

    public void setItemClass(String itemClass) {
        getStateHelper().put(PropertyKeys.itemClass, itemClass);
    }

    public String getItemHoverClass() {
        return (String) getStateHelper().eval(PropertyKeys.itemHoverClass);
    }

    public void setItemHoverClass(String itemHoverClass) {
        getStateHelper().put(PropertyKeys.itemHoverClass, itemHoverClass);
    }

    public String getItemDisableClass() {
        return (String) getStateHelper().eval(PropertyKeys.itemDisableClass);
    }

    public void setItemDisableClass(String itemDisableClass) {
        getStateHelper().put(PropertyKeys.itemDisableClass, itemDisableClass);
    }

    public String getItemIcon() {
        return (String) getStateHelper().eval(PropertyKeys.itemIcon);
    }

    public void setItemIcon(String itemIcon) {
        getStateHelper().put(PropertyKeys.itemIcon, itemIcon);
    }

    public String getItemDisableIcon() {
        return (String) getStateHelper().eval(PropertyKeys.itemDisableIcon);
    }

    public void setItemDisableIcon(String itemDisableIcon) {
        getStateHelper().put(PropertyKeys.itemDisableIcon, itemDisableIcon);
    }

    public String getItemIconPosition() {
        return (String) getStateHelper().eval(PropertyKeys.itemIconPosition);
    }

    public void setItemIconPosition(String itemIconPosition) {
        getStateHelper().put(PropertyKeys.itemIconPosition, itemIconPosition);
    }

    public String getTopItemClass() {
        return (String) getStateHelper().eval(PropertyKeys.topItemClass);
    }

    public void setTopItemClass(String topItemClass) {
        getStateHelper().put(PropertyKeys.topItemClass, topItemClass);
    }

    public String getTopItemHoverClass() {
        return (String) getStateHelper().eval(PropertyKeys.topItemHoverClass);
    }

    public void setTopItemHoverClass(String topItemHoverClass) {
        getStateHelper().put(PropertyKeys.topItemHoverClass, topItemHoverClass);
    }

    public String getTopItemDisableClass() {
        return (String) getStateHelper().eval(PropertyKeys.topItemDisableClass);
    }

    public void setTopItemDisableClass(String topItemDisableClass) {
        getStateHelper().put(PropertyKeys.topItemDisableClass, topItemDisableClass);
    }

    public String getTopItemIcon() {
        return (String) getStateHelper().eval(PropertyKeys.topItemIcon);
    }

    public void setTopItemIcon(String topItemIcon) {
        getStateHelper().put(PropertyKeys.topItemIcon, topItemIcon);
    }

    public String getTopItemDisableIcon() {
        return (String) getStateHelper().eval(PropertyKeys.topItemDisableIcon);
    }

    public void setTopItemDisableIcon(String topItemDisableIcon) {
        getStateHelper().put(PropertyKeys.topItemDisableIcon, topItemDisableIcon);
    }

    public String getTopItemIconPosition() {
        return (String) getStateHelper().eval(PropertyKeys.topItemIconPosition);
    }

    public void setTopItemIconPosition(String topItemIconPosition) {
        getStateHelper().put(PropertyKeys.topItemIconPosition, topItemIconPosition);
    }

    public String getGroupClass() {
        return (String) getStateHelper().eval(PropertyKeys.groupClass);
    }

    public void setGroupClass(String groupClass) {
        getStateHelper().put(PropertyKeys.groupClass, groupClass);
    }

    public String getGroupHoverClass() {
        return (String) getStateHelper().eval(PropertyKeys.groupHoverClass);
    }

    public void setGroupHoverClass(String groupHoverClass) {
        getStateHelper().put(PropertyKeys.groupHoverClass, groupHoverClass);
    }

    public String getGroupDisableClass() {
        return (String) getStateHelper().eval(PropertyKeys.groupDisableClass);
    }

    public void setGroupDisableClass(String groupDisableClass) {
        getStateHelper().put(PropertyKeys.groupDisableClass, groupDisableClass);
    }

    public String getGroupCollapseIcon() {
        return (String) getStateHelper().eval(PropertyKeys.groupCollapseIcon);
    }

    public void setGroupCollapseIcon(String groupCollapseIcon) {
        getStateHelper().put(PropertyKeys.groupCollapseIcon, groupCollapseIcon);
    }

    public String getGroupExpandIcon() {
        return (String) getStateHelper().eval(PropertyKeys.groupExpandIcon);
    }

    public void setGroupExpandIcon(String groupExpandIcon) {
        getStateHelper().put(PropertyKeys.groupExpandIcon, groupExpandIcon);
    }

    public String getGroupDisableIcon() {
        return (String) getStateHelper().eval(PropertyKeys.groupDisableIcon);
    }

    public void setGroupDisableIcon(String groupDisableIcon) {
        getStateHelper().put(PropertyKeys.groupDisableIcon, groupDisableIcon);
    }

    public String getGroupIconPosition() {
        return (String) getStateHelper().eval(PropertyKeys.groupIconPosition);
    }

    public void setGroupIconPosition(String groupIconPosition) {
        getStateHelper().put(PropertyKeys.groupIconPosition, groupIconPosition);
    }

    public String getTopGroupClass() {
        return (String) getStateHelper().eval(PropertyKeys.topGroupClass);
    }

    public void setTopGroupClass(String topGroupClass) {
        getStateHelper().put(PropertyKeys.topGroupClass, topGroupClass);
    }

    public String getTopGroupHoverClass() {
        return (String) getStateHelper().eval(PropertyKeys.topGroupHoverClass);
    }

    public void setTopGroupHoverClass(String topGroupHoverClass) {
        getStateHelper().put(PropertyKeys.topGroupHoverClass, topGroupHoverClass);
    }

    public String getTopGroupDisableClass() {
        return (String) getStateHelper().eval(PropertyKeys.topGroupDisableClass);
    }

    public void setTopGroupDisableClass(String topGroupDisableClass) {
        getStateHelper().put(PropertyKeys.topGroupDisableClass, topGroupDisableClass);
    }

    public String getTopGroupCollapseIcon() {
        return (String) getStateHelper().eval(PropertyKeys.topGroupCollapseIcon);
    }

    public void setTopGroupCollapseIcon(String topGroupCollapseIcon) {
        getStateHelper().put(PropertyKeys.topGroupCollapseIcon, topGroupCollapseIcon);
    }

    public String getTopGroupExpandIcon() {
        return (String) getStateHelper().eval(PropertyKeys.topGroupExpandIcon);
    }

    public void setTopGroupExpandIcon(String topGroupExpandIcon) {
        getStateHelper().put(PropertyKeys.topGroupExpandIcon, topGroupExpandIcon);
    }

    public String getTopGroupDisableIcon() {
        return (String) getStateHelper().eval(PropertyKeys.topGroupDisableIcon);
    }

    public void setTopGroupDisableIcon(String topGroupDisableIcon) {
        getStateHelper().put(PropertyKeys.topGroupDisableIcon, topGroupDisableIcon);
    }

    public String getTopGroupIconPosition() {
        return (String) getStateHelper().eval(PropertyKeys.topGroupIconPosition);
    }

    public void setTopGroupIconPosition(String topGroupIconPosition) {
        getStateHelper().put(PropertyKeys.topGroupIconPosition, topGroupIconPosition);
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



    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }
}


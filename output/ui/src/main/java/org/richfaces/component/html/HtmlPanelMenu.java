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
import org.richfaces.renderkit.util.PanelIcons;

import javax.faces.component.behavior.ClientBehaviorHolder;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * @author akolonitsky
 * @since 2010-11-29
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
        itemDisableClass,
        itemIconLeft,
        itemIconRight,
        itemDisableIconLeft,
        itemDisableIconRight,
        topItemClass,
        topItemDisableClass,
        topItemIconLeft,
        topItemIconRight,
        topItemDisableIconLeft,
        topItemDisableIconRight,
        groupClass,
        groupDisableClass,
        groupExpandIconLeft,
        groupExpandIconRight,
        groupCollapseIconLeft,
        groupCollapseIconRight,
        groupDisableIconLeft,
        groupDisableIconRight,
        topGroupClass,
        topGroupDisableClass,
        topGroupExpandIconLeft,
        topGroupExpandIconRight,
        topGroupCollapseIconLeft,
        topGroupCollapseIconRight,
        topGroupDisableIconLeft,
        topGroupDisableIconRight,
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

    public String getItemDisableClass() {
        return (String) getStateHelper().eval(PropertyKeys.itemDisableClass);
    }

    public void setItemDisableClass(String itemDisableClass) {
        getStateHelper().put(PropertyKeys.itemDisableClass, itemDisableClass);
    }

    public String getItemIconLeft() {
        return (String) getStateHelper().eval(PropertyKeys.itemIconLeft, PanelIcons.DEFAULT.toString());
    }

    public void setItemIconLeft(String itemIconLeft) {
        getStateHelper().put(PropertyKeys.itemIconLeft, itemIconLeft);
    }

    public String getItemIconRight() {
        return (String) getStateHelper().eval(PropertyKeys.itemIconRight, PanelIcons.DEFAULT.toString());
    }

    public void setItemIconRight(String itemIconRight) {
        getStateHelper().put(PropertyKeys.itemIconRight, itemIconRight);
    }

    public String getItemDisableIconLeft() {
        return (String) getStateHelper().eval(PropertyKeys.itemDisableIconLeft, PanelIcons.DEFAULT.toString());
    }

    public void setItemDisableIconLeft(String itemDisableIconLeft) {
        getStateHelper().put(PropertyKeys.itemDisableIconLeft, itemDisableIconLeft);
    }

    public String getItemDisableIconRight() {
        return (String) getStateHelper().eval(PropertyKeys.itemDisableIconRight, PanelIcons.DEFAULT.toString());
    }

    public void setItemDisableIconRight(String itemDisableIconRight) {
        getStateHelper().put(PropertyKeys.itemDisableIconRight, itemDisableIconRight);
    }

    public String getTopItemClass() {
        return (String) getStateHelper().eval(PropertyKeys.topItemClass);
    }

    public void setTopItemClass(String topItemClass) {
        getStateHelper().put(PropertyKeys.topItemClass, topItemClass);
    }

    public String getTopItemDisableClass() {
        return (String) getStateHelper().eval(PropertyKeys.topItemDisableClass);
    }

    public void setTopItemDisableClass(String topItemDisableClass) {
        getStateHelper().put(PropertyKeys.topItemDisableClass, topItemDisableClass);
    }

    public String getTopItemIconLeft() {
        return (String) getStateHelper().eval(PropertyKeys.topItemIconLeft, PanelIcons.DEFAULT.toString());
    }

    public void setTopItemIconLeft(String topItemIconLeft) {
        getStateHelper().put(PropertyKeys.topItemIconLeft, topItemIconLeft);
    }

    public String getTopItemIconRight() {
        return (String) getStateHelper().eval(PropertyKeys.topItemIconRight, PanelIcons.DEFAULT.toString());
    }

    public void setTopItemIconRight(String topItemIconRight) {
        getStateHelper().put(PropertyKeys.topItemIconRight, topItemIconRight);
    }

    public String getTopItemDisableIconLeft() {
        return (String) getStateHelper().eval(PropertyKeys.topItemDisableIconLeft, PanelIcons.DEFAULT.toString());
    }

    public void setTopItemDisableIconLeft(String topItemDisableIconLeft) {
        getStateHelper().put(PropertyKeys.topItemDisableIconLeft, topItemDisableIconLeft);
    }

    public String getTopItemDisableIconRight() {
        return (String) getStateHelper().eval(PropertyKeys.topItemDisableIconRight, PanelIcons.DEFAULT.toString());
    }

    public void setTopItemDisableIconRight(String topItemDisableIconRight) {
        getStateHelper().put(PropertyKeys.topItemDisableIconRight, topItemDisableIconRight);
    }

    public String getGroupClass() {
        return (String) getStateHelper().eval(PropertyKeys.groupClass);
    }

    public void setGroupClass(String groupClass) {
        getStateHelper().put(PropertyKeys.groupClass, groupClass);
    }

    public String getGroupDisableClass() {
        return (String) getStateHelper().eval(PropertyKeys.groupDisableClass);
    }

    public void setGroupDisableClass(String groupDisableClass) {
        getStateHelper().put(PropertyKeys.groupDisableClass, groupDisableClass);
    }

    public String getGroupExpandIconLeft() {
        return (String) getStateHelper().eval(PropertyKeys.groupExpandIconLeft, PanelIcons.DEFAULT.toString());
    }

    public void setGroupExpandIconLeft(String groupExpandIconLeft) {
        getStateHelper().put(PropertyKeys.groupExpandIconLeft, groupExpandIconLeft);
    }

    public String getGroupExpandIconRight() {
        return (String) getStateHelper().eval(PropertyKeys.groupExpandIconRight, PanelIcons.DEFAULT.toString());
    }

    public void setGroupExpandIconRight(String groupExpandIconRight) {
        getStateHelper().put(PropertyKeys.groupExpandIconRight, groupExpandIconRight);
    }

    public String getGroupCollapseIconLeft() {
        return (String) getStateHelper().eval(PropertyKeys.groupCollapseIconLeft, PanelIcons.DEFAULT.toString());
    }

    public void setGroupCollapseIconLeft(String groupCollapseIconLeft) {
        getStateHelper().put(PropertyKeys.groupCollapseIconLeft, groupCollapseIconLeft);
    }

    public String getGroupCollapseIconRight() {
        return (String) getStateHelper().eval(PropertyKeys.groupCollapseIconRight, PanelIcons.DEFAULT.toString());
    }

    public void setGroupCollapseIconRight(String groupCollapseIconRight) {
        getStateHelper().put(PropertyKeys.groupCollapseIconRight, groupCollapseIconRight);
    }

    public String getGroupDisableIconLeft() {
        return (String) getStateHelper().eval(PropertyKeys.groupDisableIconLeft, PanelIcons.DEFAULT.toString());
    }

    public void setGroupDisableIconLeft(String groupDisableIconLeft) {
        getStateHelper().put(PropertyKeys.groupDisableIconLeft, groupDisableIconLeft);
    }

    public String getGroupDisableIconRight() {
        return (String) getStateHelper().eval(PropertyKeys.groupDisableIconRight, PanelIcons.DEFAULT.toString());
    }

    public void setGroupDisableIconRight(String groupDisableIconRight) {
        getStateHelper().put(PropertyKeys.groupDisableIconRight, groupDisableIconRight);
    }

    public String getTopGroupClass() {
        return (String) getStateHelper().eval(PropertyKeys.topGroupClass);
    }

    public void setTopGroupClass(String topGroupClass) {
        getStateHelper().put(PropertyKeys.topGroupClass, topGroupClass);
    }

    public String getTopGroupDisableClass() {
        return (String) getStateHelper().eval(PropertyKeys.topGroupDisableClass);
    }

    public void setTopGroupDisableClass(String topGroupDisableClass) {
        getStateHelper().put(PropertyKeys.topGroupDisableClass, topGroupDisableClass);
    }

    public String getTopGroupExpandIconLeft() {
        return (String) getStateHelper().eval(PropertyKeys.topGroupExpandIconLeft, PanelIcons.DEFAULT.toString());
    }

    public void setTopGroupExpandIconLeft(String topGroupExpandIconLeft) {
        getStateHelper().put(PropertyKeys.topGroupExpandIconLeft, topGroupExpandIconLeft);
    }

    public String getTopGroupExpandIconRight() {
        return (String) getStateHelper().eval(PropertyKeys.topGroupExpandIconRight, PanelIcons.DEFAULT.toString());
    }

    public void setTopGroupExpandIconRight(String topGroupExpandIconRight) {
        getStateHelper().put(PropertyKeys.topGroupExpandIconRight, topGroupExpandIconRight);
    }

    public String getTopGroupCollapseIconLeft() {
        return (String) getStateHelper().eval(PropertyKeys.topGroupCollapseIconLeft, PanelIcons.DEFAULT.toString());
    }

    public void setTopGroupCollapseIconLeft(String topGroupCollapseIconLeft) {
        getStateHelper().put(PropertyKeys.topGroupCollapseIconLeft, topGroupCollapseIconLeft);
    }

    public String getTopGroupCollapseIconRight() {
        return (String) getStateHelper().eval(PropertyKeys.topGroupCollapseIconRight, PanelIcons.DEFAULT.toString());
    }

    public void setTopGroupCollapseIconRight(String topGroupCollapseIconRight) {
        getStateHelper().put(PropertyKeys.topGroupCollapseIconRight, topGroupCollapseIconRight);
    }

    public String getTopGroupDisableIconLeft() {
        return (String) getStateHelper().eval(PropertyKeys.topGroupDisableIconLeft, PanelIcons.DEFAULT.toString());
    }

    public void setTopGroupDisableIconLeft(String topGroupDisableIconLeft) {
        getStateHelper().put(PropertyKeys.topGroupDisableIconLeft, topGroupDisableIconLeft);
    }

    public String getTopGroupDisableIconRight() {
        return (String) getStateHelper().eval(PropertyKeys.topGroupDisableIconRight, PanelIcons.DEFAULT.toString());
    }

    public void setTopGroupDisableIconRight(String topGroupDisableIconRight) {
        getStateHelper().put(PropertyKeys.topGroupDisableIconRight, topGroupDisableIconRight);
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


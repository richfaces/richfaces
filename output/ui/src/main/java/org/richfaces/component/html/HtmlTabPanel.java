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

import org.richfaces.HeaderPosition;
import org.richfaces.HeaderAlignment;
import org.richfaces.component.AbstractTabPanel;
import javax.faces.component.behavior.ClientBehaviorHolder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * @author akolonitsky
 * @since 2010-08-27
 */
public class HtmlTabPanel extends AbstractTabPanel implements ClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "org.richfaces.TabPanel";

    public static final String COMPONENT_FAMILY = "org.richfaces.TabPanel";

    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList(
        "itemchange",
        "beforeitemchange",
        "click",
        "dblclick",
        "mousedown",
        "mousemove",
        "mouseout",
        "mouseover",
        "mouseup"
    ));


    public enum PropertyKeys {
        headerPosition,
        headerAlignment,
        tabHeaderClassActive,
        tabHeaderClassDisabled,
        tabHeaderClassInactive,
        tabContentClass,
        tabHeaderClass,
        onitemchange,
        onbeforeitemchange,
        lang,
        title,
        style,
        styleClass,
        dir,
        onclick,
        ondblclick,
        onmousedown,
        onmousemove,
        onmouseout,
        onmouseover,
        onmouseup
    }

    public HtmlTabPanel() {
        setRendererType("org.richfaces.TabPanel");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public HeaderPosition getHeaderPosition() {
        return (HeaderPosition) getStateHelper().eval(PropertyKeys.headerPosition);
    }

    public void setHeaderPosition(HeaderPosition headerPosition) {
        getStateHelper().put(PropertyKeys.headerPosition, headerPosition);
    }

    public HeaderAlignment getHeaderAlignment() {
        return (HeaderAlignment) getStateHelper().eval(PropertyKeys.headerAlignment);
    }

    public void setHeaderAlignment(HeaderAlignment headerAlignment) {
        getStateHelper().put(PropertyKeys.headerAlignment, headerAlignment);
    }

    public String getTabHeaderClassActive() {
        return (String) getStateHelper().eval(PropertyKeys.tabHeaderClassActive);
    }

    public void setTabHeaderClassActive(String tabHeaderClassActive) {
        getStateHelper().put(PropertyKeys.tabHeaderClassActive, tabHeaderClassActive);
    }

    public String getTabHeaderClassDisabled() {
        return (String) getStateHelper().eval(PropertyKeys.tabHeaderClassDisabled);
    }

    public void setTabHeaderClassDisabled(String tabHeaderClassDisabled) {
        getStateHelper().put(PropertyKeys.tabHeaderClassDisabled, tabHeaderClassDisabled);
    }

    public String getTabHeaderClassInactive() {
        return (String) getStateHelper().eval(PropertyKeys.tabHeaderClassInactive);
    }

    public void setTabHeaderClassInactive(String tabHeaderClassInactive) {
        getStateHelper().put(PropertyKeys.tabHeaderClassInactive, tabHeaderClassInactive);
    }

    public String getTabContentClass() {
        return (String) getStateHelper().eval(PropertyKeys.tabContentClass);
    }

    public void setTabContentClass(String tabContentClass) {
        getStateHelper().put(PropertyKeys.tabContentClass, tabContentClass);
    }

    public String getTabHeaderClass() {
        return (String) getStateHelper().eval(PropertyKeys.tabHeaderClass);
    }

    public void setTabHeaderClass(String tabHeaderClass) {
        getStateHelper().put(PropertyKeys.tabHeaderClass, tabHeaderClass);
    }

    public String getOnitemchange() {
        return (String) getStateHelper().eval(PropertyKeys.onitemchange);
    }

    public void setOnitemchange(String onitemchange) {
        getStateHelper().put(PropertyKeys.onitemchange, onitemchange);
    }

    public String getOnbeforeitemchange() {
        return (String) getStateHelper().eval(PropertyKeys.onbeforeitemchange);
    }

    public void setOnbeforeitemchange(String onbeforeitemchange) {
        getStateHelper().put(PropertyKeys.onbeforeitemchange, onbeforeitemchange);
    }

    public String getLang() {
        return (String) getStateHelper().eval(PropertyKeys.lang);
    }

    public void setLang(String lang) {
        getStateHelper().put(PropertyKeys.lang, lang);
    }

    public String getTitle() {
        return (String) getStateHelper().eval(PropertyKeys.title);
    }

    public void setTitle(String title) {
        getStateHelper().put(PropertyKeys.title, title);
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

    public String getDir() {
        return (String) getStateHelper().eval(PropertyKeys.dir);
    }

    public void setDir(String dir) {
        getStateHelper().put(PropertyKeys.dir, dir);
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


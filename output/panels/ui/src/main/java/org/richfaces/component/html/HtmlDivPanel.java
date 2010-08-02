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


import org.richfaces.component.AbstractDivPanel;

public class HtmlDivPanel extends AbstractDivPanel {

    public static final String COMPONENT_TYPE = "org.richfaces.DivPanel";

    public static final String COMPONENT_FAMILY = "org.richfaces.DivPanel";

    private enum PropertyKeys {
        lang,
        onclick,
        ondblclick,
        onmousedown,
        onmousemove,
        onmouseout,
        onmouseover,
        onmouseup,
        title,
        style,
        styleClass,
        dir
    }

    public HtmlDivPanel() {
        setRendererType("org.richfaces.DivPanel");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getLang() {
        return String.valueOf(getStateHelper().eval(PropertyKeys.lang));
    }

    public void setLang(String lang) {
        getStateHelper().put(PropertyKeys.lang, lang);
    }

    public String getOnclick() {
        return String.valueOf(getStateHelper().eval(PropertyKeys.onclick));
    }

    public void setOnclick(String onclick) {
        getStateHelper().put(PropertyKeys.onclick, onclick);
    }

    public String getOndblclick() {
        return String.valueOf(getStateHelper().eval(PropertyKeys.ondblclick));
    }

    public void setOndblclick(String ondblclick) {
        getStateHelper().put(PropertyKeys.ondblclick, ondblclick);
    }

    public String getOnmousedown() {
        return String.valueOf(getStateHelper().eval(PropertyKeys.onmousedown));
    }

    public void setOnmousedown(String onmousedown) {
        getStateHelper().put(PropertyKeys.onmousedown, onmousedown);
    }

    public String getOnmousemove() {
        return String.valueOf(getStateHelper().eval(PropertyKeys.onmousemove));
    }

    public void setOnmousemove(String onmousemove) {
        getStateHelper().put(PropertyKeys.onmousemove, onmousemove);
    }

    public String getOnmouseout() {
        return String.valueOf(getStateHelper().eval(PropertyKeys.onmouseout));
    }

    public void setOnmouseout(String onmouseout) {
        getStateHelper().put(PropertyKeys.onmouseout, onmouseout);
    }

    public String getOnmouseover() {
        return String.valueOf(getStateHelper().eval(PropertyKeys.onmouseover));
    }

    public void setOnmouseover(String onmouseover) {
        getStateHelper().put(PropertyKeys.onmouseover, onmouseover);
    }

    public String getOnmouseup() {
        return String.valueOf(getStateHelper().eval(PropertyKeys.onmouseup));
    }

    public void setOnmouseup(String onmouseup) {
        getStateHelper().put(PropertyKeys.onmouseup, onmouseup);
    }

    public String getTitle() {
        return String.valueOf(getStateHelper().eval(PropertyKeys.title));
    }

    public void setTitle(String title) {
        getStateHelper().put(PropertyKeys.title, title);
    }

    public String getStyle() {
        return String.valueOf(getStateHelper().eval(PropertyKeys.style));
    }

    public void setStyle(String style) {
        getStateHelper().put(PropertyKeys.style, style);
    }

    public String getStyleClass() {
        return String.valueOf(getStateHelper().eval(PropertyKeys.styleClass));
    }

    public void setStyleClass(String styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, styleClass);
    }

    public String getDir() {
        return String.valueOf(getStateHelper().eval(PropertyKeys.dir));
    }

    public void setDir(String dir) {
        getStateHelper().put(PropertyKeys.dir, dir);
    }


}


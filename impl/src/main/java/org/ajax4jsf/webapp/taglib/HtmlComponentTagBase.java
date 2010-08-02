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

package org.ajax4jsf.webapp.taglib;

import org.ajax4jsf.renderkit.RendererUtils.HTML;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;

/**
 * Base tag for all components with common Html attributes.
 *
 * @author asmirnov@exadel.com (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:59:55 $
 */
public abstract class HtmlComponentTagBase extends UIComponentTagBase {

    // HTML universal attributes
    private ValueExpression dir;
    private ValueExpression lang;

    // HTML event handler attributes
    private ValueExpression onclick;
    private ValueExpression ondblclick;
    private ValueExpression onkeydown;
    private ValueExpression onkeypress;
    private ValueExpression onkeyup;
    private ValueExpression onmousedown;
    private ValueExpression onmousemove;
    private ValueExpression onmouseout;
    private ValueExpression onmouseover;
    private ValueExpression onmouseup;
    private ValueExpression style;
    private ValueExpression styleClass;
    private ValueExpression title;

    @Override
    public void release() {
        super.release();
        dir = null;
        lang = null;
        style = null;
        styleClass = null;
        title = null;
        onclick = null;
        ondblclick = null;
        onkeydown = null;
        onkeypress = null;
        onkeyup = null;
        onmousedown = null;
        onmousemove = null;
        onmouseout = null;
        onmouseover = null;
        onmouseup = null;
    }

    @Override
    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        setProperty(component, HTML.DIR_ATTRIBUTE, dir);
        setProperty(component, HTML.LANG_ATTRIBUTE, lang);
        setProperty(component, HTML.STYLE_ATTRIBUTE, style);
        setProperty(component, HTML.TITLE_ATTRIBUTE, title);
        setProperty(component, HTML.STYLE_CLASS_ATTR, styleClass);
        setProperty(component, HTML.ONCLICK_ATTRIBUTE, onclick);
        setProperty(component, HTML.ONDBLCLICK_ATTRIBUTE, ondblclick);
        setProperty(component, HTML.ONMOUSEDOWN_ATTRIBUTE, onmousedown);
        setProperty(component, HTML.ONMOUSEUP_ATTRIBUTE, onmouseup);
        setProperty(component, HTML.ONMOUSEOVER_ATTRIBUTE, onmouseover);
        setProperty(component, HTML.ONMOUSEMOVE_ATTRIBUTE, onmousemove);
        setProperty(component, HTML.ONMOUSEOUT_ATTRIBUTE, onmouseout);
        setProperty(component, HTML.ONKEYPRESS_ATTRIBUTE, onkeypress);
        setProperty(component, HTML.ONKEYDOWN_ATTRIBUTE, onkeydown);
        setProperty(component, HTML.ONKEYUP_ATTRIBUTE, onkeyup);
    }

    public void setStyleClass(ValueExpression styleClass) {
        this.styleClass = styleClass;
    }

    public void setDir(ValueExpression dir) {
        this.dir = dir;
    }

    public void setLang(ValueExpression lang) {
        this.lang = lang;
    }

    public void setStyle(ValueExpression style) {
        this.style = style;
    }

    public void setTitle(ValueExpression title) {
        this.title = title;
    }

    public void setOnclick(ValueExpression onclick) {
        this.onclick = onclick;
    }

    public void setOndblclick(ValueExpression ondblclick) {
        this.ondblclick = ondblclick;
    }

    public void setOnmousedown(ValueExpression onmousedown) {
        this.onmousedown = onmousedown;
    }

    public void setOnmouseup(ValueExpression onmouseup) {
        this.onmouseup = onmouseup;
    }

    public void setOnmouseover(ValueExpression onmouseover) {
        this.onmouseover = onmouseover;
    }

    public void setOnmousemove(ValueExpression onmousemove) {
        this.onmousemove = onmousemove;
    }

    public void setOnmouseout(ValueExpression onmouseout) {
        this.onmouseout = onmouseout;
    }

    public void setOnkeypress(ValueExpression onkeypress) {
        this.onkeypress = onkeypress;
    }

    public void setOnkeydown(ValueExpression onkeydown) {
        this.onkeydown = onkeydown;
    }

    public void setOnkeyup(ValueExpression onkeyup) {
        this.onkeyup = onkeyup;
    }
}

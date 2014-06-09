/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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

package org.richfaces.demo.input;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;

@ManagedBean(name = "editor")
@ViewScoped
public class EditorBean implements Serializable {

    private String value = "Editor Initial Value";
    private boolean readonly = false;
    private String width = "400px";
    private String height = "200px";
    private boolean rendered = true;
    private String toolbar = "MyToolbar";
    private String skin;
    private String lang;
    private String oninit = "console.log('oninit')";
    private String onfocus = "console.log('onfocus')";
    private String onblur = "console.log('onblur')";
    private String ondirty = "console.log('ondirty')";
    private String onchange = "console.log('onchange')";
    private String styleClass;
    private String textareaClass;
    private String editorClass;
    private String style;
    private String textareaStyle;
    private String editorStyle;
    private String title;
    private String config =
                    "toolbar_MyToolbar:" +
                    "[" +
                    "{ name: 'document', items : [ 'NewPage','Preview' ] }," +
                    "{ name: 'clipboard', items : [ 'Cut','Copy','Paste','PasteText','PasteFromWord','-','Undo','Redo' ] }," +
                    "{ name: 'editing', items : [ 'Find','Replace','-','SelectAll','-','Scayt' ] }," +
                    "{ name: 'insert', items : [ 'Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak'" +
                    ",'Iframe' ] }," +
                    "'/'," +
                    "{ name: 'styles', items : [ 'Styles','Format' ] }," +
                    "{ name: 'basicstyles', items : [ 'Bold','Italic','Strike','-','RemoveFormat' ] }," +
                    "{ name: 'paragraph', items : [ 'NumberedList','BulletedList','-','Outdent','Indent','-','Blockquote' ] }," +
                    "{ name: 'links', items : [ 'Link','Unlink','Anchor' ] }," +
                    "{ name: 'tools', items : [ 'Maximize','-','About' ] }" +
                    "]";

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

    public String getToolbar() {
        return toolbar;
    }

    public void setToolbar(String toolbar) {
        this.toolbar = toolbar;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void valueChangeListener(ValueChangeEvent event) {
        System.out.println("editor value changed: " + event);
    }

    public String getOninit() {
        return oninit;
    }

    public void setOninit(String oninit) {
        this.oninit = oninit;
    }

    public String getOnfocus() {
        return onfocus;
    }

    public void setOnfocus(String onfocus) {
        this.onfocus = onfocus;
    }

    public String getOnblur() {
        return onblur;
    }

    public void setOnblur(String onblur) {
        this.onblur = onblur;
    }

    public String getOnchange() {
        return onchange;
    }

    public void setOnchange(String onchange) {
        this.onchange = onchange;
    }

    public String getOndirty() {
        return ondirty;
    }

    public void setOndirty(String ondirty) {
        this.ondirty = ondirty;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getTextareaClass() {
        return textareaClass;
    }

    public void setTextareaClass(String textareaClass) {
        this.textareaClass = textareaClass;
    }

    public String getEditorClass() {
        return editorClass;
    }

    public void setEditorClass(String editorClass) {
        this.editorClass = editorClass;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getTextareaStyle() {
        return textareaStyle;
    }

    public void setTextareaStyle(String textareaStyle) {
        this.textareaStyle = textareaStyle;
    }

    public String getEditorStyle() {
        return editorStyle;
    }

    public void setEditorStyle(String editorStyle) {
        this.editorStyle = editorStyle;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}

package org.richfaces.demo;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;

@ManagedBean(name = "editor")
@ViewScoped
public class EditorBean implements Serializable {

    private String value = "Editor Initial Value";
    private boolean readonly = false;
    private String width = "500px";
    private String height = "500px";
    private boolean rendered = true;
    private String toolbar;
    private String skin;
    private String lang;
    private String oninit;
    private String onfocus;
    private String onblur;
    private String ondirty;
    private String onchange;
    private String styleClass;
    private String textareaClass;
    private String editorClass;
    private String style;
    private String textareaStyle;
    private String editorStyle;
    private String title;

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
}

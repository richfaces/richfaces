package org.richfaces.demo;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

@ManagedBean
@ViewScoped
public class PlaceholderBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Converter converter = new MyConverter();
    private Object date;
    private String text = "Watermark text";
    private TextObject textObject = new TextObject();
    private Object placeholderInput;

    public Converter getConverter() {
        return converter;
    }

    public Object getDate() {
        return date;
    }

    public void setDate(Object date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public TextObject getTextObject() {
        return textObject;
    }

    public Object getPlaceholderInput() {
        return placeholderInput;
    }

    public void setPlaceholderInput(Object watermarkedInput) {
        this.placeholderInput = watermarkedInput;
    }

    public Object submit() {
        final FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Text:" + getPlaceholderInput()));
        context.addMessage(null, new FacesMessage("Date:" + getDate()));
        return null;
    }

    private class MyConverter implements Converter {

        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            return new TextObject();
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) {
            if (!(value instanceof TextObject)) {
                throw new ConverterException(String.format("%s is not instance of %s", value.getClass().getCanonicalName(),
                        TextObject.class.getCanonicalName()));
            }
            return ((TextObject) value).getText();
        }
    }

    private class TextObject {

        String getText() {
            return text;
        }
    }
}

package org.richfaces.demo;

import java.io.Serializable;
import java.util.Date;

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
    private Date date;
    private String placeholderText = "Watermark text";
    private boolean rendered = true;
    private TextObject textObject = new TextObject();

    private String inputText;
    private String textarea;

    public Converter getConverter() {
        return converter;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public String getTextarea() {
        return textarea;
    }

    public void setTextarea(String textarea) {
        this.textarea = textarea;
    }

    public String getPlaceholderText() {
        return placeholderText;
    }

    public void setPlaceholderText(String text) {
        this.placeholderText = text;
    }

    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

    public TextObject getTextObject() {
        return textObject;
    }

    public Object submit() {
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
            return placeholderText;
        }
    }
}

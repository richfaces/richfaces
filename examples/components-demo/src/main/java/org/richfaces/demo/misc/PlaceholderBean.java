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

package org.richfaces.demo.misc;

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

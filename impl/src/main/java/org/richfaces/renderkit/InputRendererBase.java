/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
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

package org.richfaces.renderkit;

import org.ajax4jsf.renderkit.AjaxComponentRendererBase;
import org.ajax4jsf.util.SelectUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.util.Map;

/**
 * @author Nick Belaevski - nbelaevski@exadel.com
 *         created 23.01.2007
 */
public class InputRendererBase extends AjaxComponentRendererBase {
    protected Class getComponentClass() {
        return UIInput.class;
    }

    protected void doDecode(FacesContext context, UIComponent component) {
        String clientId = component.getClientId(context);
        Map requestParameterMap = context.getExternalContext().getRequestParameterMap();
        String newValue = (String) requestParameterMap.get(clientId);

        if (null != newValue) {
            UIInput input = (UIInput) component;

            input.setSubmittedValue(newValue);
        }
    }

    public Object getConvertedValue(FacesContext context, UIComponent component, Object val) throws ConverterException {
        return SelectUtils.getConvertedUIInputValue(context, (UIInput) component, (String) val);
    }

    public String getInputValue(FacesContext context, UIComponent component) {
        UIInput input = (UIInput) component;
        String value = (String) input.getSubmittedValue();

        if (value == null) {
            Object curVal = input.getValue();
            Converter converter = SelectUtils.getConverterForProperty(context, input, "value");

            if (converter != null) {
                value = converter.getAsString(context, input, curVal);
            } else {
                if (curVal == null) {
                    value = "";
                } else {
                    value = curVal.toString();
                }
            }
        }

        if (value == null) {
            value = "";
        }

        return value;
    }
}

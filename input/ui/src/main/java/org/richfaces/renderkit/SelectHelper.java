/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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

package org.richfaces.renderkit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;

import org.richfaces.component.AbstractSelect;
import org.richfaces.component.util.InputUtils;
import org.richfaces.component.util.SelectUtils;

/**
 * @author abelevich
 *
 */
public final class SelectHelper {
    
    public static final String OPTIONS_SHOWCONTROL = "showControl";
    
    public static final String OPTIONS_SELECT_ITEM_VALUE_INPUT = "selValueInput";

    public static final String OPTIONS_LIST_ITEMS = "items";

    public static final Map<String, ComponentAttribute> SELECT_LIST_HANDLER_ATTRIBUTES = Collections
    .unmodifiableMap(ComponentAttribute.createMap(
            new ComponentAttribute(HtmlConstants.ONCLICK_ATTRIBUTE)
                .setEventNames("listclick")
                .setComponentAttributeName("onlistclick"),
            new ComponentAttribute(HtmlConstants.ONDBLCLICK_ATTRIBUTE)
                .setEventNames("listdblclick")
                .setComponentAttributeName("onlistdblclick"), 
            new ComponentAttribute(HtmlConstants.ONMOUSEDOWN_ATTRIBUTE)
                .setEventNames("listmousedown")
                .setComponentAttributeName("onlistmousedown"),
            new ComponentAttribute(HtmlConstants.ONMOUSEUP_ATTRIBUTE)
                .setEventNames("listmouseup")
                .setComponentAttributeName("onlistmouseup"), 
            new ComponentAttribute(HtmlConstants.ONMOUSEOVER_ATTRIBUTE)
                .setEventNames("listmouseover")
                .setComponentAttributeName("onlistmouseover"), 
            new ComponentAttribute(HtmlConstants.ONMOUSEMOVE_ATTRIBUTE)
                .setEventNames("listmousemove")
                .setComponentAttributeName("onlistmousemove"), 
            new ComponentAttribute(HtmlConstants.ONMOUSEOUT_ATTRIBUTE)
                .setEventNames("listmouseout")
                .setComponentAttributeName("onlistmouseout"), 
            new ComponentAttribute(HtmlConstants.ONKEYPRESS_ATTRIBUTE)
                .setEventNames("listkeypress")
                .setComponentAttributeName("onlistkeypress"),
            new ComponentAttribute(HtmlConstants.ONKEYDOWN_ATTRIBUTE)
                .setEventNames("listkeydown")
                .setComponentAttributeName("onlistkeydown"),
            new ComponentAttribute(HtmlConstants.ONKEYUP_ATTRIBUTE)
                .setEventNames("listkeyup")
                .setComponentAttributeName("onlistkeyup")
    ));

    private SelectHelper() {
    }
    
    public static List<ClientSelectItem> getConvertedSelectItems(FacesContext facesContext, UIComponent component) {
        AbstractSelect select = (AbstractSelect) component;
        List<SelectItem> selectItems = SelectUtils.getSelectItems(facesContext, select);
        List<ClientSelectItem> clientSelectItems = new ArrayList<ClientSelectItem>();
        
        for (SelectItem selectItem : selectItems) {
            String convertedStringValue = InputUtils.getConvertedStringValue(facesContext, select, selectItem.getValue());
            String label = selectItem.getLabel();
            clientSelectItems.add(new ClientSelectItem(convertedStringValue,label));
        }
        return clientSelectItems;
    }
    
    public static void encodeItems(FacesContext facesContext, UIComponent component,
            List<ClientSelectItem> clientSelectItems, String itemHtmlElement) throws IOException {
        AbstractSelect select = (AbstractSelect) component;
        if (clientSelectItems != null && !clientSelectItems.isEmpty()) {
            ResponseWriter writer = facesContext.getResponseWriter();
            String clientId = component.getClientId(facesContext);
            int i = 0;
            for (ClientSelectItem clientSelectItem : clientSelectItems) {
                String itemClientId = clientId + "Item" + (i++);
                clientSelectItem.setClientId(itemClientId);

                writer.startElement(itemHtmlElement, select);
                writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, itemClientId,
                        null);
                writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, select.getItemCss(), null);

                String label = clientSelectItem.getLabel();
                if (label != null && label.trim().length() > 0) {
                    writer.writeText(label, null);
                } else {
                    writer.write("\u00a0");
                }
                writer.endElement(itemHtmlElement);
            }
        }
    }
    
    public static String getSelectInputLabel(FacesContext facesContext, UIComponent component) {
        AbstractSelect select = (AbstractSelect) component;
        Object value = select.getSubmittedValue();
        String label = null;
        if (value == null) {
            value = select.getValue();
            if (value != null) {
                List<SelectItem> items = SelectUtils.getSelectItems(
                        facesContext, component);
                for (SelectItem item : items) {
                    if (value.equals(item.getValue())) {
                        label = item.getLabel();
                    }
                }
            }
        }

        return label;
    }

}

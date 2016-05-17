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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.el.ELException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;

import org.richfaces.component.AbstractSelectComponent;
import org.richfaces.component.util.HtmlUtil;
import org.richfaces.component.util.InputUtils;
import org.richfaces.component.util.SelectUtils;

/**
 * @author abelevich
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 *
 */
public final class SelectHelper {
    public static final String OPTIONS_SHOWCONTROL = "showControl";
    public static final String OPTIONS_LIST_ITEMS = "items";
    public static final String OPTIONS_ENABLE_MANUAL_INPUT = "enableManualInput";
    public static final String OPTIONS_LIST_SELECT_FIRST = "selectFirst";
    public static final String OPTIONS_INPUT_DEFAULT_LABEL = "defaultLabel";
    public static final Map<String, ComponentAttribute> SELECT_LIST_HANDLER_ATTRIBUTES = Collections
        .unmodifiableMap(ComponentAttribute.createMap(
            new ComponentAttribute(HtmlConstants.ONCLICK_ATTRIBUTE).setEventNames("listclick").setComponentAttributeName(
                "onlistclick"),
            new ComponentAttribute(HtmlConstants.ONDBLCLICK_ATTRIBUTE).setEventNames("listdblclick").setComponentAttributeName(
                "onlistdblclick"),
            new ComponentAttribute(HtmlConstants.ONMOUSEDOWN_ATTRIBUTE).setEventNames("listmousedown")
                .setComponentAttributeName("onlistmousedown"),
            new ComponentAttribute(HtmlConstants.ONMOUSEUP_ATTRIBUTE).setEventNames("listmouseup").setComponentAttributeName(
                "onlistmouseup"),
            new ComponentAttribute(HtmlConstants.ONMOUSEOVER_ATTRIBUTE).setEventNames("listmouseover")
                .setComponentAttributeName("onlistmouseover"),
            new ComponentAttribute(HtmlConstants.ONMOUSEMOVE_ATTRIBUTE).setEventNames("listmousemove")
                .setComponentAttributeName("onlistmousemove"),
            new ComponentAttribute(HtmlConstants.ONMOUSEOUT_ATTRIBUTE).setEventNames("listmouseout").setComponentAttributeName(
                "onlistmouseout"),
            new ComponentAttribute(HtmlConstants.ONKEYPRESS_ATTRIBUTE).setEventNames("listkeypress").setComponentAttributeName(
                "onlistkeypress"), new ComponentAttribute(HtmlConstants.ONKEYDOWN_ATTRIBUTE).setEventNames("listkeydown")
                .setComponentAttributeName("onlistkeydown"), new ComponentAttribute(HtmlConstants.ONKEYUP_ATTRIBUTE)
                .setEventNames("listkeyup").setComponentAttributeName("onlistkeyup")));

    private SelectHelper() {
    }

    public static List<ClientSelectItem> getConvertedSelectItems(FacesContext facesContext, UIComponent component) {
        Iterator<SelectItem> selectItems = SelectUtils.getSelectItems(facesContext, component);
        List<ClientSelectItem> clientSelectItems = new ArrayList<ClientSelectItem>();

        while (selectItems.hasNext()) {
            SelectItem selectItem = selectItems.next();
            clientSelectItems.add(generateClientSelectItem(facesContext, component, selectItem, 0, false));
        }
        return clientSelectItems;
    }

    public static ClientSelectItem generateClientSelectItem(FacesContext facesContext, UIComponent component,
        SelectItem selectItem, int sortOrder, boolean selected) {
        String convertedStringValue = InputUtils.getConvertedStringValue(facesContext, component, selectItem.getValue());
        String label = selectItem.getLabel();
        ClientSelectItem clientSelectItem = new ClientSelectItem(selectItem, convertedStringValue, label, sortOrder, selected);
        return clientSelectItem;
    }

    public static void encodeItems(FacesContext facesContext, UIComponent component, List<ClientSelectItem> clientSelectItems,
        String itemHtmlElement, String defaultItemCss) throws IOException {
        AbstractSelectComponent select = (AbstractSelectComponent) component;
        if (clientSelectItems != null && !clientSelectItems.isEmpty()) {
            ResponseWriter writer = facesContext.getResponseWriter();
            String clientId = component.getClientId(facesContext);
            int i = 0;
            for (ClientSelectItem clientSelectItem : clientSelectItems) {
                String itemClientId = clientId + "Item" + (i++);
                clientSelectItem.setClientId(itemClientId);

                writer.startElement(itemHtmlElement, select);
                writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, itemClientId, null);

                writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE,
                    HtmlUtil.concatClasses(defaultItemCss + (clientSelectItem.getSelectItem().isDisabled() ? "-dis" : ""),
                        select.getItemClass()), null);

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
        AbstractSelectComponent select = (AbstractSelectComponent) component;
        Object value = select.getSubmittedValue();
        String label = null;
        if (value == null) {
            value = select.getValue();
            if (value != null) {
                Iterator<SelectItem> items = SelectUtils.getSelectItems(facesContext, component);

                while (items.hasNext()) {
                    SelectItem item = items.next();
                    Object compareValue = null;

                    try {
                        compareValue = facesContext.getApplication().getExpressionFactory()
                            .coerceToType(item.getValue(), value.getClass());
                    } catch (ELException el) {
                        compareValue = item.getValue();
                    }

                    if (value.equals(compareValue)) {
                        label = item.getLabel();
                    }
                }
            }
        }

        return label;
    }

    public static void addSelectCssToOptions(AbstractSelectComponent abstractSelect, Map<String, Object> options,
        String[] defaultCss) {
        String itemCss = abstractSelect.getItemClass();
        if (itemCss != null && itemCss.trim().length() > 0) {
            options.put(PopupConstants.OPTIONS_ITEM_CLASS, HtmlUtil.concatClasses(defaultCss[0], itemCss));
        }

        String selectItemCss = abstractSelect.getSelectItemClass();
        if (selectItemCss != null && selectItemCss.trim().length() > 0) {
            options.put(PopupConstants.OPTIONS_SELECT_ITEM_CLASS, HtmlUtil.concatClasses(defaultCss[1], selectItemCss));
        }

        String listCss = abstractSelect.getListClass();
        if (listCss != null && listCss.trim().length() > 0) {
            options.put(PopupConstants.OPTIONS_LIST_CLASS, HtmlUtil.concatClasses(defaultCss[2], listCss));
        }
    }
}

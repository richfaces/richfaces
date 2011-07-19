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

import org.richfaces.component.AbstractSelectManyComponent;
import org.richfaces.component.util.HtmlUtil;
import org.richfaces.json.JSONArray;
import org.richfaces.json.JSONCollection;
import org.richfaces.json.JSONException;
import org.richfaces.json.JSONStringer;
import org.richfaces.json.JSONTokener;
import org.richfaces.json.JSONWriter;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 *
 */
@ResourceDependencies({ @ResourceDependency(library = "javax.faces", name = "jsf.js"), @ResourceDependency(name = "jquery.js"),
        @ResourceDependency(name = "jquery.position.js"), @ResourceDependency(name = "richfaces.js"),
        @ResourceDependency(name = "richfaces-utils.js"), @ResourceDependency(name = "jquery.position.js"),
        @ResourceDependency(name = "richfaces-event.js"), @ResourceDependency(name = "richfaces-base-component.js"),
        @ResourceDependency(name = "richfaces-selection.js"),
        @ResourceDependency(library = "org.richfaces", name = "inputBase.js"),
        @ResourceDependency(library = "org.richfaces", name = "popup.js"),
        @ResourceDependency(library = "org.richfaces", name = "popupList.js"),
        @ResourceDependency(library = "org.richfaces", name = "select.js"),
        @ResourceDependency(library = "org.richfaces", name = "select.ecss"),
        @ResourceDependency(library = "org.richfaces", name = "pickList.js") })
public class SelectManyRendererBase extends RendererBase {
    public static final String ITEM_CSS = ""; //"rf-sel-opt";
    protected List<ClientSelectItem> convertedSelectItemsAvailable;
    protected List<ClientSelectItem> convertedSelectItemsSelected;

    public void generateSelectItemsLists(FacesContext facesContext, UIComponent component) {
        AbstractSelectManyComponent select = (AbstractSelectManyComponent) component;
        List<SelectItem> selectItemsAll = SelectHelper.getSelectItems(facesContext, component);
        convertedSelectItemsAvailable = new ArrayList<ClientSelectItem>();
        convertedSelectItemsSelected = new ArrayList<ClientSelectItem>();
        Object object = select.getValue();
        Object[] values;
        if (object == null) {
            values = new Object[0];
        }
        else if (object instanceof List) {
            List list = (List) object;
            values = list.toArray();
        } else if (object instanceof Object[]) {
            values = (Object[]) object;
        } else {
            throw new IllegalArgumentException("Value expression must evaluate to either a List or Object[]");
        }
        Set<Object> valuesSet = new HashSet<Object>(Arrays.asList(values));
        for (SelectItem selectItem : selectItemsAll) {
            if (valuesSet.contains(selectItem.getValue())) {
                convertedSelectItemsSelected.add(SelectHelper.generateClientSelectItem(facesContext, component, selectItem));
            } else {
                convertedSelectItemsAvailable.add(SelectHelper.generateClientSelectItem(facesContext, component, selectItem));
            }
        }
    }

    public List<ClientSelectItem> getConvertedSelectItemsAvailable(FacesContext facesContext, UIComponent component) {
        return convertedSelectItemsAvailable;
    }

    public List<ClientSelectItem> getConvertedSelectItemsSelected(FacesContext facesContext, UIComponent component) {
        return convertedSelectItemsSelected;
    }

    public void encodeItems(FacesContext facesContext, UIComponent component, List<ClientSelectItem> clientSelectItems)
            throws IOException {
        AbstractSelectManyComponent select = (AbstractSelectManyComponent) component;
        if (clientSelectItems != null && !clientSelectItems.isEmpty()) {
            ResponseWriter writer = facesContext.getResponseWriter();
            String clientId = component.getClientId(facesContext);
            int i = 0;
            for (ClientSelectItem clientSelectItem : clientSelectItems) {
                String itemClientId = clientId + "Item" + (i++);
                clientSelectItem.setClientId(itemClientId);
                writer.startElement(HtmlConstants.OPTION_ELEM, component);
                writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, itemClientId, null);
                writer.writeAttribute(HtmlConstants.VALUE_ATTRIBUTE, clientSelectItem.getConvertedValue(), null);
                writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE,
                    HtmlUtil.concatClasses(select.getItemClass(), ITEM_CSS), null);
                String label = clientSelectItem.getLabel();
                if (label != null && label.trim().length() > 0) {
                    writer.writeText(label, null);
                } else {
                    writer.write("\u00a0");
                }
                writer.endElement(HtmlConstants.OPTION_ELEM);
                writer.write('\n');
            }
        }
    }

    public String csvEncodeItems(List<ClientSelectItem> clientSelectItems) {
        if (clientSelectItems == null || clientSelectItems.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Iterator<ClientSelectItem> iter = clientSelectItems.iterator();
        while (iter.hasNext()) {
            ClientSelectItem item = iter.next();
            sb.append(item.getConvertedValue());
            if (iter.hasNext()) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    @Override
    public void doDecode(FacesContext context, UIComponent component){
        ExternalContext external = context.getExternalContext();
        Map requestParams = external.getRequestParameterMap();
        AbstractSelectManyComponent select = (AbstractSelectManyComponent)component;
        String clientId = select.getClientId(context);
        String submittedValue = (String)requestParams.get(clientId+"Hidden");
        if (submittedValue != null) {
            String[] values = submittedValue.split(",");
            if (select.getValue() instanceof List) {
                select.setSubmittedValue(Arrays.asList(values));
            } else if (select.getValue() instanceof Object[]) {
                select.setSubmittedValue(values);
            } else {
                throw new IllegalArgumentException("Value expression must evaluate to either a List or Object[]");
            }
        } else {
            if (select.getValue() instanceof List) {
                select.setSubmittedValue(Collections.emptyList());
            } else if (select.getValue() instanceof Object[]) {
                select.setSubmittedValue(new String[0]);
            } else {
                throw new IllegalArgumentException("Value expression must evaluate to either a List or Object[]");
            }
        }
    }
}

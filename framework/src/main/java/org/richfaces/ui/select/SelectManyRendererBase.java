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
package org.richfaces.ui.select;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

import org.richfaces.ui.input.InputRendererBase;
import org.richfaces.util.HtmlDimensions;
import org.richfaces.util.HtmlUtil;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 *
 */
public class SelectManyRendererBase extends InputRendererBase {
    private static final String HIDDEN_SUFFIX = "Hidden";

    public List<ClientSelectItem> getClientSelectItems(FacesContext facesContext, UIComponent component) {
        AbstractSelectManyComponent select = (AbstractSelectManyComponent) component;
        return  SelectManyHelper.getClientSelectItems(facesContext, select, SelectUtils.getSelectItems(facesContext, component));
    }

    public String csvEncodeSelectedItems(List<ClientSelectItem> clientSelectItems) {
        if (clientSelectItems == null || clientSelectItems.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Iterator<ClientSelectItem> iter = clientSelectItems.iterator();
        while (iter.hasNext()) {
            ClientSelectItem item = iter.next();
            if (item.isSelected()) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(item.getConvertedValue());
            }
        }
        return sb.toString();
    }

    @Override
    protected void doDecode(FacesContext facesContext, UIComponent component) {
        if (!(component instanceof AbstractSelectManyComponent)) {
            throw new IllegalArgumentException(String.format("Component %s is not an AbstractSelectManyComponent", component.getClientId(facesContext)));
        }
        AbstractSelectManyComponent picklist = (AbstractSelectManyComponent) component;

        String hiddenClientId = picklist.getClientId(facesContext);
        Map<String, String> paramMap = facesContext.getExternalContext().getRequestParameterMap();

        if (picklist.isDisabled()) {
            return;
        }
        String value = paramMap.get(hiddenClientId);
        if (value != null) {
            if (value.trim().equals("")) {
                ((EditableValueHolder) picklist).setSubmittedValue(new String[] {});
            } else {
                String[] reqValues = value.split(",");
                ((EditableValueHolder) picklist).setSubmittedValue(reqValues);
            }
        } else {
            ((EditableValueHolder) picklist).setSubmittedValue(new String[] {});
        }
    }

    public Object getConvertedValue(FacesContext facesContext, UIComponent component, Object val) throws ConverterException {
        return SelectManyHelper.getConvertedValue(facesContext, component, val);
    }

    public boolean hasColumnChildren(FacesContext facesContext, UIComponent component) {
        AbstractSelectManyComponent select = (AbstractSelectManyComponent) component;
        return select.columns().hasNext();
    }

    public boolean isHeaderExists(FacesContext facesContext, UIComponent component) {
        AbstractSelectManyComponent select = (AbstractSelectManyComponent) component;
        Iterator<UIColumn> columnIterator = select.columns();
        while (columnIterator.hasNext()) {
            UIColumn column = columnIterator.next();
            if (column.getFacet("header") != null) {
                return true;
            }
        }
        return false;
    }
}

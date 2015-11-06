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
import org.richfaces.component.util.SelectUtils;
import org.richfaces.renderkit.util.HtmlDimensions;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 *
 */
@ResourceDependencies({ @ResourceDependency(library = "javax.faces", name = "jsf.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-queue.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-base-component.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.position.js"), @ResourceDependency(library = "org.richfaces", name = "richfaces-event.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-utils.js"), @ResourceDependency(library = "org.richfaces", name = "richfaces-selection.js"),
        @ResourceDependency(library = "org.richfaces", name = "inputBase.js"),
        @ResourceDependency(library = "org.richfaces", name = "popup.js"),
        @ResourceDependency(library = "org.richfaces", name = "list.js"),
        @ResourceDependency(library = "org.richfaces", name = "listMulti.js"),
        @ResourceDependency(library = "org.richfaces", name = "popupList.js"),
        @ResourceDependency(library = "org.richfaces", name = "pickList.js"),
        @ResourceDependency(library = "org.richfaces", name = "pickList.ecss")})
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
                    sb.append("\",'");
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
                String[] reqValues = value.split("\",'");
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

    //TODO: Make the following methods DRY with the corresponding SelectRendererBase methods, CDK support for interfaces required first
    protected String getMinListHeight(AbstractSelectManyComponent select) {
        String height = HtmlDimensions.formatSize(select.getMinListHeight());
        if (height == null || height.length() == 0) {
            height = "20px";
        }
        return height;
    }

    protected String getMaxListHeight(AbstractSelectManyComponent select) {
        String height = HtmlDimensions.formatSize(select.getMaxListHeight());
        if (height == null || height.length() == 0) {
            height = "100px";
        }
        return height;
    }

    protected String getListHeight(AbstractSelectManyComponent select) {
        String height = HtmlDimensions.formatSize(select.getListHeight());
        if (height == null || height.length() == 0) {
            height = "auto";
        }
        return height;
    }

    protected String getListWidth(AbstractSelectManyComponent select) {
        String width = HtmlDimensions.formatSize(select.getListWidth());
        if (width == null || width.length() == 0) {
            width = "200px";
        }
        return width;
    }

    public String encodeHeightAndWidth(UIComponent component) {
        AbstractSelectManyComponent select = (AbstractSelectManyComponent) component;

        String height = getListHeight(select);
        if (!"auto".equals(height)) {
            height = (height != null && height.trim().length() != 0) ? ("height: " + height) : "";
        } else {
            String minHeight = getMinListHeight(select);
            minHeight = (minHeight != null && minHeight.trim().length() != 0) ? ("min-height: " + minHeight) : "";

            String maxHeight = getMaxListHeight(select);
            maxHeight = (maxHeight != null && maxHeight.trim().length() != 0) ? ("max-height: " + maxHeight) : "";
            height = concatStyles(minHeight, maxHeight);
        }

        String width = getListWidth(select);
        width = (width != null && width.trim().length() != 0) ? ("width: " + width) : "";

        return concatStyles(height, width);
    }

    public String getButtonClass(UIComponent component, String cssPrefix, String buttonClass) {
        AbstractSelectManyComponent select = (AbstractSelectManyComponent) component;
        if (!select.isDisabled()) {
            return HtmlUtil.concatClasses(buttonClass, cssPrefix + SelectManyHelper.BUTTON_CSS);
        } else {
            return HtmlUtil.concatClasses(buttonClass, cssPrefix + SelectManyHelper.BUTTON_CSS, cssPrefix + SelectManyHelper.BUTTON_CSS_DIS);

        }
    }
}

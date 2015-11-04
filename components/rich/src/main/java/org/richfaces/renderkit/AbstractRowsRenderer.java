/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
import java.util.Collections;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.model.DataVisitResult;
import org.ajax4jsf.model.DataVisitor;
import org.richfaces.component.Row;
import org.richfaces.component.UIDataTableBase;
import org.richfaces.component.attribute.RowColumnStyleProps;

/**
 * @author Anton Belevich
 *
 */
@ResourceDependencies({ @ResourceDependency(library = "javax.faces", name = "jsf.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-queue.reslib") })
public abstract class AbstractRowsRenderer extends RendererBase implements DataVisitor {
    private static final Map<String, ComponentAttribute> ROW_HANDLER_ATTRIBUTES = Collections
        .unmodifiableMap(ComponentAttribute.createMap(
            new ComponentAttribute(HtmlConstants.ONCLICK_ATTRIBUTE).setEventNames("rowclick").setComponentAttributeName(
                "onrowclick"),
            new ComponentAttribute(HtmlConstants.ONDBLCLICK_ATTRIBUTE).setEventNames("rowdblclick").setComponentAttributeName(
                "onrowdblclick"),
            new ComponentAttribute(HtmlConstants.ONMOUSEDOWN_ATTRIBUTE).setEventNames("rowmousedown")
                .setComponentAttributeName("onrowmousedown"),
            new ComponentAttribute(HtmlConstants.ONMOUSEUP_ATTRIBUTE).setEventNames("rowmouseup").setComponentAttributeName(
                "onrowmouseup"),
            new ComponentAttribute(HtmlConstants.ONMOUSEOVER_ATTRIBUTE).setEventNames("rowmouseover")
                .setComponentAttributeName("onrowmouseover"),
            new ComponentAttribute(HtmlConstants.ONMOUSEMOVE_ATTRIBUTE).setEventNames("rowmousemove")
                .setComponentAttributeName("onrowmousemove"),
            new ComponentAttribute(HtmlConstants.ONMOUSEOUT_ATTRIBUTE).setEventNames("rowmouseout").setComponentAttributeName(
                "onrowmouseout"),
            new ComponentAttribute(HtmlConstants.ONKEYPRESS_ATTRIBUTE).setEventNames("rowkeypress").setComponentAttributeName(
                "onrowkeypress"), new ComponentAttribute(HtmlConstants.ONKEYDOWN_ATTRIBUTE).setEventNames("rowkeydown")
                .setComponentAttributeName("onrowkeydown"), new ComponentAttribute(HtmlConstants.ONKEYUP_ATTRIBUTE)
                .setEventNames("rowkeyup").setComponentAttributeName("onrowkeyup")));

    public abstract void encodeRow(ResponseWriter writer, FacesContext facesContext, RowHolderBase rowHolder)
        throws IOException;

    public abstract RowHolderBase createRowHolder(FacesContext context, UIComponent component, Object[] options);

    public DataVisitResult process(FacesContext facesContext, Object rowKey, Object argument) {
        RowHolderBase holder = (RowHolderBase) argument;
        Row row = holder.getRow();
        row.setRowKey(facesContext, rowKey);

        try {
            ResponseWriter writer = facesContext.getResponseWriter();
            holder.resetProcessCell();
            encodeRow(writer, facesContext, holder);
        } catch (IOException e) {
            throw new FacesException(e);
        }

        holder.nextRow();
        return DataVisitResult.CONTINUE;
    }

    protected void encodeRows(FacesContext facesContext, RowHolderBase rowHolder) {
        rowHolder.getRow().walk(facesContext, this, rowHolder);
    }

    public void encodeFakeRow(FacesContext facesContext, RowHolderBase rowHolder) throws IOException {
    }

    protected void renderRowHandlers(FacesContext context, UIDataTableBase dataTable) throws IOException {
        RenderKitUtils.renderPassThroughAttributesOptimized(context, dataTable, ROW_HANDLER_ATTRIBUTES);
    }

    public void processRows(ResponseWriter writer, FacesContext facesContext, UIComponent component, Object[] options)
        throws IOException {
        RowHolderBase rowHolder = createRowHolder(facesContext, component, options);
        encodeRows(facesContext, rowHolder);
        if (!rowHolder.hasWalkedOverRows()) {
            try {
                encodeFakeRow(facesContext, rowHolder);
            } catch (IOException e) {
                throw new FacesException(e);
            }
        } else {
            doCleanup(facesContext, rowHolder);
        }
    }

    protected void doCleanup(FacesContext context, RowHolderBase rowHolder) throws IOException {
        // Hook method
    }

    protected void doEncodeChildren(ResponseWriter writer, FacesContext facesContext, UIComponent component) throws IOException {
        processRows(writer, facesContext, component, null);
    }

    public boolean getRendersChildren() {
        return true;
    }

    protected String get(FacesContext context, String key) {
        return (String) context.getAttributes().get(key);
    }

    protected void put(FacesContext context, String key, String value) {
        context.getAttributes().put(key, value);
    }

    protected String[] getRowClasses(RowHolderBase rowHolder) {
        String[] rowClasses = new String[0];
        if (rowHolder.getRow() instanceof RowColumnStyleProps) {
            String classes = ((RowColumnStyleProps) rowHolder.getRow()).getRowClasses();
            if (null != classes) {
                rowClasses = classes.split(",");
            }
        }
        return rowClasses;
    }

    protected String[] getColumnClasses(RowHolderBase rowHolder) {
        String[] columnClasses = new String[0];
        if (rowHolder.getRow() instanceof RowColumnStyleProps) {
            String classes = ((RowColumnStyleProps) rowHolder.getRow()).getColumnClasses();
            if (null != classes) {
                columnClasses = classes.split(",");
            }
        }
        return columnClasses;
    }

    protected String getColumnClass(RowHolderBase rowHolder, int columnNumber) {
        String styleClass = "";
        String[] columnClasses = getColumnClasses(rowHolder);
        if (columnClasses.length > 0) {
            styleClass = columnClasses[columnNumber % columnClasses.length];
        }

        return styleClass;
    }

    protected String getRowClassAttribute(RowHolderBase rowHolder) {
        String rowClass = "";
        if (rowHolder.getRow() instanceof UIDataTableBase) {
            rowClass = ((UIDataTableBase) rowHolder.getRow()).getRowClass();
        }
        return rowClass;
    }

    protected String getRowClass(RowHolderBase rowHolder) {
        String styleClass = "";
        String[] rowClasses = getRowClasses(rowHolder);
        if (rowClasses.length > 0) {
            int styleIndex = rowHolder.getCurrentRow() % rowClasses.length;
            styleClass = rowClasses[styleIndex];
        }
        return concatClasses(getRowClassAttribute(rowHolder), styleClass);
    }
}

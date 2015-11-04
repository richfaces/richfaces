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
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.model.DataVisitResult;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractDataGrid;
import org.richfaces.component.Row;
import org.richfaces.component.UIDataTableBase;
import org.richfaces.component.attribute.ColumnProps;
import org.richfaces.component.util.HtmlUtil;

/**
 * @author Anton Belevich
 *
 */
@JsfRenderer(type = "org.richfaces.DataGridRenderer", family = AbstractDataGrid.COMPONENT_FAMILY)
@ResourceDependencies({ @ResourceDependency(library = "javax.faces", name = "jsf.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-queue.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-base-component.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-event.js"),
        @ResourceDependency(library = "org.richfaces", name = "datagrid.ecss") })
public class DataGridRenderer extends AbstractRowsRenderer implements MetaComponentRenderer {
    private static final EncodeStrategy THEAD = new EncodeStrategy() {
        public void begin(ResponseWriter writer, FacesContext context, UIComponent component, Object[] params)
            throws IOException {
            String clientId = component.getClientId(context) + ":h";

            boolean partial = (Boolean) (Boolean) params[0];
            if (partial) {
                context.getPartialViewContext().getPartialResponseWriter().startUpdate(clientId);
            }

            writer.startElement(HtmlConstants.THEAD_ELEMENT, component);
            writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, clientId, null);
            writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-dg-thead", null);
            writer.startElement(HtmlConstants.TR_ELEMENT, component);
            writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, HtmlUtil.concatClasses("rf-dg-h ", ((ColumnProps) component).getHeaderClass()), null);
            writer.startElement(HtmlConstants.TH_ELEM, component);
            writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-dg-h-c", null);

            int columns = (Integer) component.getAttributes().get("columns");
            if (columns > 0) {
                writer.writeAttribute(HtmlConstants.COLSPAN_ATTRIBUTE, columns, null);
            }
        }

        public void end(ResponseWriter writer, FacesContext context, UIComponent component, Object[] params) throws IOException {
            writer.endElement(HtmlConstants.TH_ELEM);
            writer.endElement(HtmlConstants.TR_ELEMENT);
            writer.endElement(HtmlConstants.THEAD_ELEMENT);

            boolean partial = (Boolean) (Boolean) params[0];
            if (partial) {
                context.getPartialViewContext().getPartialResponseWriter().endUpdate();
            }
        }
    };
    private static final EncodeStrategy TFOOT = new EncodeStrategy() {
        public void begin(ResponseWriter writer, FacesContext context, UIComponent component, Object[] params)
            throws IOException {

            String clientId = component.getClientId(context) + ":f";

            boolean partial = (Boolean) (Boolean) params[0];
            if (partial) {
                context.getPartialViewContext().getPartialResponseWriter().startUpdate(clientId);
            }

            writer.startElement(HtmlConstants.TFOOT_ELEMENT, component);
            writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, clientId, null);
            writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-dg-tfoot", null);
            writer.startElement(HtmlConstants.TR_ELEMENT, component);
            writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, HtmlUtil.concatClasses("rf-dg-f ", ((ColumnProps) component).getFooterClass()), null);
            writer.startElement(HtmlConstants.TD_ELEM, component);
            writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-dg-f-c", null);

            int columns = (Integer) component.getAttributes().get("columns");
            if (columns > 0) {
                writer.writeAttribute(HtmlConstants.COLSPAN_ATTRIBUTE, columns, null);
            }
        }

        public void end(ResponseWriter writer, FacesContext context, UIComponent component, Object[] params) throws IOException {
            writer.endElement(HtmlConstants.TD_ELEM);
            writer.endElement(HtmlConstants.TR_ELEMENT);
            writer.endElement(HtmlConstants.TFOOT_ELEMENT);

            boolean partial = (Boolean) (Boolean) params[0];
            if (partial) {
                context.getPartialViewContext().getPartialResponseWriter().endUpdate();
            }
        }
    };
    private static final EncodeStrategy CAPTION = new EncodeStrategy() {
        public void begin(ResponseWriter writer, FacesContext context, UIComponent component, Object[] params)
            throws IOException {
            writer.startElement(HtmlConstants.CAPTION_ELEMENT, component);
            writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-dg-cap", null);
        }

        public void end(ResponseWriter writer, FacesContext context, UIComponent component, Object[] params) throws IOException {
            writer.endElement(HtmlConstants.CAPTION_ELEMENT);
        }
    };
    private static final EncodeStrategy NODATA = new EncodeStrategy() {
        public void begin(ResponseWriter writer, FacesContext context, UIComponent component, Object[] params)
            throws IOException {
            writer.startElement(HtmlConstants.TR_ELEMENT, component);
            writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-dg-nd", null);
            writer.startElement(HtmlConstants.TD_ELEM, component);
            writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-dg-nd-c", null);

            int columns = (Integer) component.getAttributes().get("columns");
            if (columns > 0) {
                writer.writeAttribute(HtmlConstants.COLSPAN_ATTRIBUTE, columns, null);
            }
        }

        public void end(ResponseWriter writer, FacesContext context, UIComponent component, Object[] params) throws IOException {
            writer.endElement(HtmlConstants.TD_ELEM);
            writer.endElement(HtmlConstants.TR_ELEMENT);
        }
    };

    @Override
    public void encodeRow(ResponseWriter writer, FacesContext facesContext, RowHolderBase rowHolder) throws IOException {
        AbstractDataGrid dataGrid = (AbstractDataGrid) rowHolder.getRow();

        int columns = dataGrid.getColumns();
        int processCell = rowHolder.getProcessCell();

        if (columns > 0 && (processCell % columns == 0)) {
            if (processCell != 0) {
                writer.endElement(HtmlConstants.TR_ELEMENT);
                rowHolder.resetProcessCell();
                processCell = rowHolder.getProcessCell();
            }
            writer.startElement(HtmlConstants.TR_ELEMENT, dataGrid);
            writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, HtmlUtil.concatClasses("rf-dg-r ", getRowClass(rowHolder)), null);
            rowHolder.nextRow();
        }

        writer.startElement(HtmlConstants.TD_ELEM, dataGrid);
        writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, HtmlUtil.concatClasses("rf-dg-c ", getColumnClass(rowHolder, processCell)), null);
        renderChildren(facesContext, dataGrid);
        writer.endElement(HtmlConstants.TD_ELEM);
    }

    public void encodeHeader(ResponseWriter writer, FacesContext facesContext, AbstractDataGrid dataGrid, boolean partial)
        throws IOException {
        UIComponent headerFacet = dataGrid.getHeader();
        encodeFacet(writer, facesContext, headerFacet, THEAD, dataGrid, new Object[] { partial });
    }

    public void encodeFooter(ResponseWriter writer, FacesContext facesContext, AbstractDataGrid dataGrid, boolean partial)
        throws IOException {
        UIComponent footerFacet = dataGrid.getFooter();
        encodeFacet(writer, facesContext, footerFacet, TFOOT, dataGrid, new Object[] { partial });
    }

    public void encodeCaption(ResponseWriter writer, FacesContext facesContext, AbstractDataGrid dataGrid) throws IOException {
        UIComponent captionFacet = dataGrid.getCaption();
        encodeFacet(writer, facesContext, captionFacet, CAPTION, dataGrid, null);
    }

    public void encodeNoData(ResponseWriter writer, FacesContext facesContext, AbstractDataGrid dataGrid) throws IOException {
        UIComponent noDataFacet = dataGrid.getNoData();
        encodeFacet(writer, facesContext, noDataFacet, NODATA, dataGrid, null);
    }

    public void encodeFacet(ResponseWriter writer, FacesContext facesContext, UIComponent facet, EncodeStrategy strategy,
        AbstractDataGrid dataGrid, Object[] params) throws IOException {
        if (facet != null && facet.isRendered()) {
            strategy.begin(writer, facesContext, dataGrid, params);
            facet.encodeAll(facesContext);
            strategy.end(writer, facesContext, dataGrid, params);
        }
    }

    public void encodeTBody(ResponseWriter writer, FacesContext facesContext, AbstractDataGrid dataGrid, boolean partial)
        throws IOException {

        String clientId = dataGrid.getClientId(facesContext) + ":dgb";
        if (partial) {
            facesContext.getPartialViewContext().getPartialResponseWriter().startUpdate(clientId);
        }
        writer.startElement(HtmlConstants.TBODY_ELEMENT, dataGrid);
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, clientId, null);
        writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-dg-body", null);
        if (dataGrid.getRowCount() > 0) {
            processRows(writer, facesContext, dataGrid, null);
        } else {
            encodeNoData(writer, facesContext, dataGrid);
        }
        writer.endElement(HtmlConstants.TBODY_ELEMENT);

        if (partial) {
            facesContext.getPartialViewContext().getPartialResponseWriter().endUpdate();
        }
    }

    @Override
    protected void doEncodeChildren(ResponseWriter writer, FacesContext facesContext, UIComponent component) throws IOException {
        AbstractDataGrid dataGrid = (AbstractDataGrid) component;
        writer.startElement(HtmlConstants.TABLE_ELEMENT, dataGrid);
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, dataGrid.getClientId(facesContext), null);
        Map<String, Object> attributes = dataGrid.getAttributes();
        String classes = concatClasses("rf-dg", attributes.get(HtmlConstants.STYLE_CLASS_ATTR));
        writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, classes, null);
        RenderKitUtils.renderAttribute(facesContext, "style", attributes.get(HtmlConstants.STYLE_ATTRIBUTE));
        RenderKitUtils.renderAttribute(facesContext, HtmlConstants.TITLE_ATTRIBUTE,
            attributes.get(HtmlConstants.TITLE_ATTRIBUTE));
        encodeCaption(writer, facesContext, dataGrid);
        encodeHeader(writer, facesContext, dataGrid, false);
        encodeFooter(writer, facesContext, dataGrid, false);
        encodeTBody(writer, facesContext, dataGrid, false);

        writer.endElement(HtmlConstants.TABLE_ELEMENT);
    }

    @Override
    public RowHolderBase createRowHolder(FacesContext context, UIComponent component, Object[] options) {
        return new RowHolder(context, (AbstractDataGrid) component);
    }

    @Override
    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractDataGrid.class;
    }

    public void encodeMetaComponent(FacesContext context, UIComponent component, String metaComponentId) throws IOException {
        AbstractDataGrid table = (AbstractDataGrid) component;

        if (UIDataTableBase.HEADER.equals(metaComponentId)) {
            encodeHeader(context.getResponseWriter(), context, table, true);
        } else if (UIDataTableBase.FOOTER.equals(metaComponentId)) {
            encodeFooter(context.getResponseWriter(), context, table, true);
        } else if (UIDataTableBase.BODY.equals(metaComponentId)) {
            encodeTBody(context.getResponseWriter(), context, table, true);
        } else {
            throw new IllegalArgumentException("Unsupported metaComponentIdentifier: " + metaComponentId);
        }
    }

    public void decodeMetaComponent(FacesContext context, UIComponent component, String metaComponentId) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void doCleanup(FacesContext context, RowHolderBase rowHolder) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        AbstractDataGrid dataGrid = (AbstractDataGrid) rowHolder.getRow();

        int cell = rowHolder.getProcessCell();
        int columns = dataGrid.getColumns();
        int rest = columns - cell;

        for (int i = 0; i < rest; i++) {
            writer.startElement(HtmlConstants.TD_ELEM, dataGrid);
            writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-dg-c", null);
            writer.endElement(HtmlConstants.TD_ELEM);
        }
        writer.endElement(HtmlConstants.TR_ELEMENT);
    }

    public DataVisitResult process(FacesContext facesContext, Object rowKey, Object argument) {
        RowHolderBase holder = (RowHolderBase) argument;
        Row row = holder.getRow();
        row.setRowKey(facesContext, rowKey);

        try {
            ResponseWriter writer = facesContext.getResponseWriter();
            encodeRow(writer, facesContext, holder);
        } catch (IOException e) {
            throw new FacesException(e);
        }

        holder.nextCell();
        return DataVisitResult.CONTINUE;
    }

    @Override
    public void encodeFakeRow(FacesContext facesContext, RowHolderBase rowHolder) throws IOException {
        UIComponent component = (UIComponent) rowHolder.getRow();
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement(HtmlConstants.TR_ELEMENT, component);
        writer.writeAttribute(HtmlConstants.STYLE_ATTRIBUTE, "display:none", null);
        writer.startElement(HtmlConstants.TD_ELEM, component);
        writer.endElement(HtmlConstants.TD_ELEM);
        writer.endElement(HtmlConstants.TR_ELEMENT);
    }
}

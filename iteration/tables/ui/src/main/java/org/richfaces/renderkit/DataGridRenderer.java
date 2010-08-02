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

import javax.faces.FacesException;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.model.DataVisitResult;
import org.ajax4jsf.renderkit.RendererUtils.HTML;
import org.richfaces.component.Row;
import org.richfaces.component.UIDataGrid;
import org.richfaces.component.UIDataTableBase;

/**
 * @author Anton Belevich
 *
 */

@ResourceDependencies({@ResourceDependency(library = "javax.faces", name = "jsf.js"),
    @ResourceDependency(name = "jquery.js"), @ResourceDependency(name = "richfaces.js"),
    @ResourceDependency(name = "richfaces-event.js"), @ResourceDependency(name = "richfaces-base-component.js"),
    @ResourceDependency(name = "datagrid.ecss")})
public class DataGridRenderer extends AbstractRowsRenderer implements MetaComponentRenderer {

    private static final EncodeStrategy THEAD = new  EncodeStrategy () {

        public void begin(ResponseWriter writer, FacesContext context, UIComponent component, Object[] params)
            throws IOException {
            UIDataGrid dataGrid = (UIDataGrid)component;
            String clientId = dataGrid.getClientId(context) + ":h";

            boolean partial = (Boolean)(Boolean)params[0];
            if(partial) {
                context.getPartialViewContext().getPartialResponseWriter().startUpdate(clientId);
            }
            
            int columns = dataGrid.getColumns();
            
            writer.startElement(HTML.THEAD_ELEMENT, component);
            writer.writeAttribute(HTML.ID_ATTRIBUTE, clientId , null);
            writer.writeAttribute(HTML.CLASS_ATTRIBUTE, "rf-dg-thead", null);
            writer.startElement(HTML.TR_ELEMENT, component);
            writer.writeAttribute(HTML.CLASS_ATTRIBUTE, "rf-dg-h", null);
            writer.startElement(HTML.TH_ELEM, component);
            writer.writeAttribute(HTML.CLASS_ATTRIBUTE, "rf-dg-h-c", null);
            writer.writeAttribute(HTML.COLSPAN_ATTRIBUTE, columns, null);
        }

        public void end(ResponseWriter writer, FacesContext context, UIComponent component, Object[] params)
            throws IOException {
            writer.endElement(HTML.TH_ELEM);
            writer.endElement(HTML.TR_ELEMENT);
            writer.endElement(HTML.THEAD_ELEMENT);
            
            boolean partial = (Boolean)(Boolean)params[0];
            if(partial) {
                context.getPartialViewContext().getPartialResponseWriter().endUpdate();
            }
        }
    };
    
    private static final EncodeStrategy TFOOT = new  EncodeStrategy () {

        public void begin(ResponseWriter writer, FacesContext context, UIComponent component, Object[] params)
            throws IOException {
                        
            UIDataGrid dataGrid = (UIDataGrid)component;
            String clientId = dataGrid.getClientId(context) + ":f";
            
            int columns = dataGrid.getColumns();
            boolean partial = (Boolean)(Boolean)params[0];
            if(partial) {
                context.getPartialViewContext().getPartialResponseWriter().startUpdate(clientId);
            }
            
            writer.startElement(HTML.TFOOT_ELEMENT, component);
            writer.writeAttribute(HTML.ID_ATTRIBUTE, clientId , null);
            writer.writeAttribute(HTML.CLASS_ATTRIBUTE, "rf-dg-tfoot", null);
            writer.startElement(HTML.TR_ELEMENT, component);
            writer.writeAttribute(HTML.CLASS_ATTRIBUTE, "rf-dg-f", null);
            writer.startElement(HTML.TD_ELEM, component);
            writer.writeAttribute(HTML.CLASS_ATTRIBUTE, "rf-dg-f-c", null);
            writer.writeAttribute(HTML.COLSPAN_ATTRIBUTE, columns, null);
        }

        public void end(ResponseWriter writer, FacesContext context, UIComponent component, Object[] params)
            throws IOException {
            writer.endElement(HTML.TD_ELEM);
            writer.endElement(HTML.TR_ELEMENT);
            writer.endElement(HTML.TFOOT_ELEMENT);
            
            boolean partial = (Boolean)(Boolean)params[0];
            if(partial) {
                context.getPartialViewContext().getPartialResponseWriter().endUpdate();
            }
        }
    };
    
    private static final EncodeStrategy CAPTION = new EncodeStrategy() {
        public void begin(ResponseWriter writer, FacesContext context, UIComponent component, Object[] params)
            throws IOException {
            writer.startElement(HTML.CAPTION_ELEMENT, component);
            writer.writeAttribute(HTML.CLASS_ATTRIBUTE, "rf-dg-cap", null);
        }

        public void end(ResponseWriter writer, FacesContext context, UIComponent component, Object[] params)
            throws IOException {
            writer.endElement(HTML.CAPTION_ELEMENT);
        }
    };

    private static final EncodeStrategy NODATA = new EncodeStrategy() {
        public void begin(ResponseWriter writer, FacesContext context, UIComponent component, Object[] params)
            throws IOException {
            writer.startElement(HTML.TR_ELEMENT, component);
            writer.writeAttribute(HTML.CLASS_ATTRIBUTE, "rf-dg-nd", null);
            writer.startElement(HTML.TD_ELEM, component);
            writer.writeAttribute(HTML.CLASS_ATTRIBUTE, "rf-dg-nd-c", null);
        }

        public void end(ResponseWriter writer, FacesContext context, UIComponent component, Object[] params)
            throws IOException {
            writer.endElement(HTML.TD_ELEM);
            writer.endElement(HTML.TR_ELEMENT);
        }
    };

    
    @Override
    public void encodeRow(ResponseWriter writer, FacesContext facesContext, RowHolderBase rowHolder) throws IOException {
        UIDataGrid dataGrid = (UIDataGrid) rowHolder.getRow();
        
        int columns = dataGrid.getColumns();
        int processCell = rowHolder.getProcessCell();

        if (columns > 0 && (processCell % columns == 0)) {
            if (processCell != 0) {
                writer.endElement(HTML.TR_ELEMENT);
                rowHolder.resetProcessCell();
                rowHolder.nextRow();
            }
            writer.startElement(HTML.TR_ELEMENT, dataGrid);
            writer.writeAttribute(HTML.CLASS_ATTRIBUTE, "rf-dg-r", null);
        }

        writer.startElement(HTML.TD_ELEM, dataGrid);
        writer.writeAttribute(HTML.CLASS_ATTRIBUTE, "rf-dg-c", null);
        renderChildren(facesContext, dataGrid);
        writer.endElement(HTML.TD_ELEM);
    }
    
    public void encodeHeader(ResponseWriter writer, FacesContext facesContext,  UIDataGrid dataGrid, boolean partial) throws IOException {
        UIComponent headerFacet = dataGrid.getHeaderFacet();
        encodeFacet(writer, facesContext, headerFacet, THEAD, dataGrid, new Object[] {partial});
    }
    
    public void encodeFooter(ResponseWriter writer, FacesContext facesContext, UIDataGrid dataGrid, boolean partial) throws IOException  {
        UIComponent footerFacet = dataGrid.getFooterFacet();
        encodeFacet(writer, facesContext, footerFacet, TFOOT, dataGrid, new Object[] {partial});
    }
    
    public void encodeCaption(ResponseWriter writer, FacesContext facesContext, UIDataGrid dataGrid) throws IOException {
        UIComponent captionFacet = dataGrid.getCaptionFacet();
        encodeFacet(writer, facesContext, captionFacet, CAPTION, dataGrid, null);
    }
    
    public void encodeNoData(ResponseWriter writer, FacesContext facesContext, UIDataGrid dataGrid) throws IOException {
        UIComponent noDataFacet = dataGrid.getNoDataFacet();
        encodeFacet(writer, facesContext, noDataFacet, NODATA, dataGrid, null);
    }
    
    public void encodeFacet(ResponseWriter writer, FacesContext facesContext, UIComponent facet, EncodeStrategy strategy, UIDataGrid dataGrid, Object [] params) throws IOException{
        if(facet != null && facet.isRendered()) {
            strategy.begin(writer, facesContext, dataGrid, params);
            facet.encodeAll(facesContext);
            strategy.end(writer, facesContext, dataGrid, params);
        }
    }
    
    public void encodeTBody(ResponseWriter writer, FacesContext facesContext, UIDataGrid dataGrid,  boolean partial) throws IOException {
        
        String clientId = dataGrid.getClientId(facesContext) + ":dgb";
        if(partial) {
            facesContext.getPartialViewContext().getPartialResponseWriter().startUpdate(clientId);
        }
        writer.startElement(HTML.TBODY_ELEMENT, dataGrid);
        writer.writeAttribute(HTML.ID_ATTRIBUTE, clientId , null);
        writer.writeAttribute(HTML.CLASS_ATTRIBUTE, "rf-dg-body", null);
        if(dataGrid.getRowCount() > 0) {
            processRows(writer, facesContext, dataGrid, null);
        } else {
            encodeNoData(writer, facesContext, dataGrid);
        }
        writer.endElement(HTML.TBODY_ELEMENT);
        
        if(partial) {
            facesContext.getPartialViewContext().getPartialResponseWriter().endUpdate();   
        }
    }
        
    @Override
    protected void doEncodeChildren(ResponseWriter writer, FacesContext facesContext, UIComponent component)
        throws IOException {
        UIDataGrid dataGrid = (UIDataGrid)component;
        writer.startElement(HTML.TABLE_ELEMENT, dataGrid);
        writer.writeAttribute(HTML.ID_ATTRIBUTE, dataGrid.getClientId(facesContext), null);
        writer.writeAttribute(HTML.CLASS_ATTRIBUTE, "rf-dg", null);

        encodeCaption(writer, facesContext, dataGrid);
        encodeHeader(writer, facesContext, dataGrid, false);
        encodeFooter(writer, facesContext, dataGrid, false);
        encodeTBody(writer, facesContext, dataGrid, false);
        
        writer.endElement(HTML.TABLE_ELEMENT);
    }

    @Override
    public RowHolderBase createRowHolder(FacesContext context, UIComponent component, Object[] options) {
        return new RowHolder(context, (UIDataGrid)component);
    }
    
    @Override
    protected Class<? extends UIComponent> getComponentClass() {
        return UIDataGrid.class;
    }

    public void encodeMetaComponent(FacesContext context, UIComponent component, String metaComponentId)
        throws IOException {
        UIDataGrid table = (UIDataGrid) component;
        
        if (UIDataTableBase.HEADER.equals(metaComponentId)) {
            encodeHeader(context.getResponseWriter(), context, table, true);
        } else if (UIDataTableBase.FOOTER.equals(metaComponentId)) {
            encodeFooter(context.getResponseWriter(), context, table, true);
        } else if(UIDataTableBase.BODY.equals(metaComponentId)) {
            encodeTBody(context.getResponseWriter(), context, table, true);
        } else {
            throw new IllegalArgumentException("Unsupported metaComponentIdentifier: " + metaComponentId);
        }
    }
    
    @Override
    protected void doCleanup(FacesContext context, RowHolderBase rowHolder) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        UIDataGrid dataGrid = (UIDataGrid)rowHolder.getRow();
       
        int cell = rowHolder.getProcessCell();
        int columns = dataGrid.getColumns();
        int rest = columns - cell;
        
        if(rest != 0) {
            for (int i = 0; i < rest; i++) {
                writer.startElement(HTML.TD_ELEM, dataGrid);
                writer.writeAttribute(HTML.CLASS_ATTRIBUTE, "rf-dg-c", null);
                writer.endElement(HTML.TD_ELEM);
            }
        }

        writer.endElement(HTML.TR_ELEMENT);
        
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
}

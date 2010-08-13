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
import java.util.Iterator;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.renderkit.RendererUtils.HTML;
import org.richfaces.component.Row;
import org.richfaces.component.UIDataTableBase;
import org.richfaces.component.util.HtmlUtil;

/**
 * @author Anton Belevich
 *
 */

@ResourceDependencies({
    @ResourceDependency(name = "richfaces-event.js")
})    
public abstract class AbstractTableRenderer extends AbstractTableBaseRenderer implements MetaComponentRenderer {
    
    public static final String HIDDEN_CONTAINER_ID =":sc"; 
    
    public abstract EncodeStrategy getHiddenContainerStrategy(UIDataTableBase dataTableBase);
    
    public boolean isColumnAttributeSet(UIDataTableBase table, String attributeName) {
        Iterator<UIComponent> columns = table.columns();
        boolean result = false;
        while (columns.hasNext() && !result) {
            UIComponent component = columns.next();
            result = (component.isRendered() && (null != component.getValueExpression(attributeName)));
        }
        return result;
    }
    
    @Override
    public RowHolderBase createRowHolder(FacesContext context, UIComponent component, Object[] options) {
        RowHolder rowHolder = null;
        if(component instanceof UIDataTableBase) {
            rowHolder = new RowHolder(context, (UIDataTableBase)component);
            rowHolder.setUpdatePartial((Boolean)options[0]);
            rowHolder.setEncodeParentTBody((Boolean)options[1]);
        }    

        return rowHolder;
    }
    
    protected class SimpleHeaderEncodeStrategy implements EncodeStrategy {
        public void begin(ResponseWriter writer, FacesContext context,  UIComponent column, Object [] params)
            throws IOException {
        }

        public void end(ResponseWriter writer, FacesContext context, UIComponent column, Object [] params)
            throws IOException {
        }
    }
    
    protected void doDecode(FacesContext context, UIComponent component) {
        decodeSortingFiltering(context, component);
    }
    
    @Override
    protected void encodeRows(FacesContext facesContext, RowHolderBase rowHolder) {
        UIDataTableBase dataTableBase = (UIDataTableBase)rowHolder.getRow();

        String rowClass = getRowSkinClass();
        String cellClass = getCellSkinClass();
        String firstClass = getFirstRowSkinClass();

        rowClass = mergeStyleClasses(ROW_CLASS_KEY, rowClass, dataTableBase);
        cellClass = mergeStyleClasses(CELL_CLASS_KEY, cellClass, dataTableBase);
        firstClass = mergeStyleClasses(FIRST_ROW_CLASS_KEY, firstClass, dataTableBase);

        saveRowStyles(facesContext, dataTableBase.getClientId(facesContext), firstClass, rowClass, cellClass);

        super.encodeRows(facesContext, rowHolder);
    }

    protected boolean isEncodeHeaders(UIDataTableBase table) {
        return table.isColumnFacetPresent(UIDataTableBase.HEADER) || isColumnAttributeSet(table, "sortBy")
            || isColumnAttributeSet(table, "comparator")
            || isColumnAttributeSet(table, "filterBy");
    }

    protected int getColumnsCount(UIDataTableBase table) {
        // check for exact value in component
        Integer span = (Integer) table.getAttributes().get("columns");
        int count = (null != span && span.intValue() != Integer.MIN_VALUE) ? span.intValue()
            : getColumnsCount(table.columns());
        return count;
    }
    
    public void encodeTableStructure(ResponseWriter writer, FacesContext context, UIDataTableBase dataTable) throws IOException {
        //DataTableRenderer override this method   
    }
    
    public void encodeBeforeRows(ResponseWriter writer, FacesContext facesContext, UIDataTableBase dataTableBase, boolean encodeParentTBody, boolean partialUpdate) throws IOException {
    }
    
    public void encodeAfterRows(ResponseWriter writer, FacesContext facesContext, UIDataTableBase dataTableBase, boolean encodeParentTBody, boolean partialUpdate) throws IOException {
    }
    
    public abstract boolean encodeParentTBody(UIDataTableBase dataTableBase);
    
    public void encodeTableFacets(ResponseWriter writer, FacesContext context, UIDataTableBase dataTable) throws IOException {

        Object key = dataTable.getRowKey();
        dataTable.captureOrigValue(context);
        dataTable.setRowKey(context, null);
        
        encodeTableStructure(writer, context, dataTable);
        
        setupTableStartElement(context, dataTable);
        encodeHeaderFacet(writer, context, dataTable, false);
        setupTableStartElement(context, dataTable, HTML.TD_ELEM);
        encodeFooterFacet(writer, context, dataTable, false);
        dataTable.setRowKey(context, key);
        dataTable.restoreOrigValue(context);
    }
    
    public void encodeTableRows(ResponseWriter writer, FacesContext facesContext, UIDataTableBase dataTableBase,
        boolean encodePartialUpdate) throws IOException {
        
        int rowCount = dataTableBase.getRowCount();
       
        put(facesContext, dataTableBase.getClientId(facesContext) + CELL_ELEMENT_KEY, HTML.TD_ELEM);
        
        Object key = dataTableBase.getRowKey();
        dataTableBase.captureOrigValue(facesContext);
        dataTableBase.setRowKey(facesContext, null);
        
        boolean encodeParentTBody = encodeParentTBody(dataTableBase);
        encodeBeforeRows(writer, facesContext, dataTableBase, encodeParentTBody, encodePartialUpdate);

        if (rowCount > 0) {
            processRows(writer, facesContext, dataTableBase, new Object[] {encodePartialUpdate, encodeParentTBody});
        } else {
            int columns = getColumnsCount(dataTableBase.columns());

            writer.startElement(HTML.TR_ELEMENT, dataTableBase);
            writer.startElement(HTML.TD_ELEM, dataTableBase);
            writer.writeAttribute(HTML.COLSPAN_ATTRIBUTE, columns, null);

            String styleClass = (String) dataTableBase.getAttributes().get("noDataStyleClass");
            styleClass = styleClass != null ? getNoDataClass() + " " + styleClass : getNoDataClass();

            writer.writeAttribute(HTML.CLASS_ATTRIBUTE, styleClass, null);

            UIComponent noDataFacet = dataTableBase.getNoData();
            if (noDataFacet != null && noDataFacet.isRendered()) {
                noDataFacet.encodeAll(facesContext);
            } else {
                String noDataLabel = dataTableBase.getNoDataLabel();
                if (noDataLabel != null) {
                    writer.writeText(noDataLabel, "noDataLabel");
                }
            }

            writer.endElement(HTML.TD_ELEM);
            writer.endElement(HTML.TR_ELEMENT);
        }      
       
        
        encodeAfterRows(writer, facesContext, dataTableBase, encodeParentTBody, encodePartialUpdate);

        if(encodePartialUpdate) {
            String id = dataTableBase.getClientId(facesContext) + HIDDEN_CONTAINER_ID;
            partialStart(facesContext, id);
        }
        
        encodeHiddens(writer, facesContext, dataTableBase, new Object[]{encodeParentTBody});

        if(encodePartialUpdate) {
            partialEnd(facesContext);
        }

        dataTableBase.setRowKey(facesContext, key);
        dataTableBase.restoreOrigValue(facesContext);
    }
           
    protected void doEncodeChildren(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        if(component instanceof UIDataTableBase) {
            encodeTableRows(writer, context, (UIDataTableBase)component, false);
        }    
    }
    
    public void encodeTableStart(ResponseWriter writer, FacesContext context, UIDataTableBase component) throws IOException {
        writer.startElement(HTML.TABLE_ELEMENT, component);
        writer.writeAttribute(HTML.ID_ATTRIBUTE, component.getClientId(), null);
        String styleClass = getTableSkinClass();
        encodeStyleClass(writer, context, component, HTML.STYLE_CLASS_ATTR, styleClass);
    }
    
    protected void encodeHiddens(ResponseWriter writer, FacesContext facesContext, UIDataTableBase dataTableBase, Object [] params) throws IOException {
        EncodeStrategy encodeStrategy = getHiddenContainerStrategy(dataTableBase);
        if(encodeStrategy != null) {
            encodeStrategy.begin(writer, facesContext, dataTableBase, params);
            
            encodeClientScript(writer, facesContext, dataTableBase);
            encodeHiddenInput(writer, facesContext, dataTableBase);
            
            encodeStrategy.end(writer, facesContext, dataTableBase, params);
        }    
    }
    
    public void encodeTableEnd(ResponseWriter writer) throws IOException {
        writer.endElement(HTML.TABLE_ELEMENT);
    }
    
    public abstract void encodeClientScript(ResponseWriter writer, FacesContext context, UIDataTableBase component) throws IOException;
    
    public abstract void encodeHiddenInput(ResponseWriter writer, FacesContext context, UIDataTableBase component) throws IOException;
           
    public void encodeTableBodyStart(ResponseWriter writer, FacesContext facesContext, UIDataTableBase dataTableBase)
        throws IOException {
           
        writer.startElement(HTML.TBODY_ELEMENT, dataTableBase);
        String clientId = (dataTableBase.getRelativeRowIndex() < 0) ? dataTableBase.getClientId(facesContext) : dataTableBase.getRelativeClientId(facesContext);
       
        writer.writeAttribute(HTML.ID_ATTRIBUTE,  clientId + ":tb", null);
        writer.writeAttribute(HTML.CLASS_ATTRIBUTE, getTableSkinClass(), null);
        encodeStyle(writer, facesContext, dataTableBase, null);
    }
    
    public void encodeTableBodyEnd(ResponseWriter writer) throws IOException {
        writer.endElement(HTML.TBODY_ELEMENT);
    }

    public void encodeFooterFacet(ResponseWriter writer, FacesContext facesContext, UIDataTableBase dataTable, 
        boolean encodePartialUpdate) throws IOException {

        UIComponent footer = dataTable.getFooter();
        boolean columnFacetPresent = dataTable.isColumnFacetPresent("footer");

        if ((footer != null && footer.isRendered()) || columnFacetPresent) {
            boolean partialUpdateEncoded = false;

            boolean encodeTfoot = containsThead();
            if (encodeTfoot) {
                String footerClientId = dataTable.getClientId(facesContext) + ":tf";

                if (encodePartialUpdate) {
                    partialUpdateEncoded = true;
                    partialStart(facesContext, footerClientId);
                }

                writer.startElement(HTML.TFOOT_ELEMENT, dataTable);
                writer.writeAttribute(HTML.ID_ATTRIBUTE, footerClientId, null);
                writer.writeAttribute(HTML.CLASS_ATTRIBUTE, "rd-dt-tfoot", null);
            }
     
            int columns = getColumnsCount(dataTable);
            String id = dataTable.getClientId(facesContext);

            boolean encodePartialUpdateForChildren = (encodePartialUpdate && !partialUpdateEncoded);
            
            if (columnFacetPresent) {
                
                
                String rowClass =  getColumnFooterSkinClass();
                String cellClass = getColumnFooterCellSkinClass();
                String firstClass = getColumnFooterFirstSkinClass();
                
                rowClass = mergeStyleClasses("columnFooterClass", rowClass, dataTable);
                cellClass = mergeStyleClasses("columnFooterCellClass", cellClass, dataTable);
                firstClass = mergeStyleClasses("firstColumnFooterClass", firstClass, dataTable);
                                
                saveRowStyles(facesContext,id, firstClass, rowClass, cellClass);
                
                String targetId = id + ":cf";
                
                if (encodePartialUpdateForChildren) {
                    partialStart(facesContext, targetId);
                }
                
                writer.startElement(HTML.TR_ELEMENT, dataTable);
                writer.writeAttribute(HTML.ID_ATTRIBUTE, targetId, null);
                
                encodeStyleClass(writer, facesContext, dataTable, null, rowClass);
                encodeColumnFacet(facesContext, writer, dataTable, UIDataTableBase.FOOTER,columns, cellClass);
                writer.endElement(HTML.TR_ELEMENT);

                if (encodePartialUpdateForChildren) {
                    partialEnd(facesContext);
                }
            }

            if (footer != null && footer.isRendered()) {
                         
                String rowClass =  getFooterSkinClass();
                String cellClass = getFooterCellSkinClass();
                String firstClass = getFooterFirstSkinClass();
                
                rowClass = mergeStyleClasses("footerClass", rowClass, dataTable);
                cellClass = mergeStyleClasses("footerCellClass", cellClass, dataTable);
                firstClass = mergeStyleClasses("footerFirstClass", firstClass, dataTable);
                // TODO nick - rename method "encodeTableHeaderFacet"
                saveRowStyles(facesContext, id, firstClass, rowClass, cellClass);
                encodeTableFacet(facesContext, writer, id, columns, footer, UIDataTableBase.FOOTER, rowClass, cellClass, 
                    encodePartialUpdateForChildren);
            }

            if (encodeTfoot) {
                writer.endElement(HTML.TFOOT_ELEMENT);
                
                if (partialUpdateEncoded) {
                    partialEnd(facesContext);
                }
            }
        }

    }
    
    protected String mergeStyleClasses(String classAttribibute, String skinClass, UIComponent component) {
        String resultClass = skinClass; 
        
        String styleClass = null;
        if(classAttribibute != null && component != null ) {
            styleClass = (String)component.getAttributes().get(classAttribibute);
        }
             
        return HtmlUtil.concatClasses(resultClass, styleClass);
    }
    
    public void encodeHeaderFacet(ResponseWriter writer, FacesContext facesContext, UIDataTableBase dataTable, 
        boolean encodePartialUpdate) throws IOException {

        UIComponent header = dataTable.getHeader();
        boolean isEncodeHeaders = isEncodeHeaders(dataTable);

        boolean encodeThead = containsThead();

        if ((header != null && header.isRendered()) || isEncodeHeaders) {
            boolean partialUpdateEncoded = false;

            if (encodeThead) {
                String headerClientId = dataTable.getClientId(facesContext) + ":th";

                if (encodePartialUpdate) {
                    partialUpdateEncoded = true;
                    partialStart(facesContext, headerClientId);
                }
                
                writer.startElement(HTML.THEAD_ELEMENT, dataTable);
                writer.writeAttribute(HTML.ID_ATTRIBUTE, headerClientId, null);
                writer.writeAttribute(HTML.CLASS_ATTRIBUTE, "rf-dt-thead", null);
            }
            
            int columns = getColumnsCount(dataTable);
            String id = dataTable.getClientId(facesContext); 
            
            boolean encodePartialUpdateForChildren = (encodePartialUpdate && !partialUpdateEncoded);
            
            if (header != null && header.isRendered()) {
                                
                String rowClass =  getHeaderSkinClass();
                String cellClass = getHeaderCellSkinClass();
                String firstClass = getHeaderFirstSkinClass();
                
                rowClass = mergeStyleClasses("headerClass", rowClass, dataTable);
                cellClass = mergeStyleClasses("headerCellClass", cellClass, dataTable);
                firstClass = mergeStyleClasses("headerFirstClass", firstClass, dataTable);
                saveRowStyles(facesContext, id, firstClass, rowClass, cellClass);

                encodeTableFacet(facesContext, writer, id, columns, header, UIDataTableBase.HEADER, rowClass, cellClass, 
                    encodePartialUpdateForChildren);
            }

            if (isEncodeHeaders) {
                
                String rowClass =  getColumnHeaderSkinClass();
                String cellClass = getColumnHeaderCellSkinClass();
                String firstClass = getColumnHeaderFirstSkinClass();
                
                rowClass = mergeStyleClasses("columnHeaderClass", rowClass, dataTable);
                cellClass = mergeStyleClasses("columnHeaderCellClass", cellClass, dataTable);
                firstClass = mergeStyleClasses("columnHeaderFirstClass", firstClass, dataTable);
                saveRowStyles(facesContext, id, firstClass, rowClass, cellClass);
                 
                String targetId = id + ":ch";
                
                if (encodePartialUpdateForChildren) {
                    partialStart(facesContext, targetId);
                }
                
                writer.startElement(HTML.TR_ELEMENT, dataTable);
                writer.writeAttribute(HTML.ID_ATTRIBUTE, targetId, null);
                
                encodeStyleClass(writer, facesContext, dataTable, null, rowClass);
                
                encodeColumnFacet(facesContext, writer, dataTable, UIDataTableBase.HEADER, columns, cellClass);
                writer.endElement(HTML.TR_ELEMENT);
                
                if (encodePartialUpdateForChildren) {
                    partialEnd(facesContext);
                }
            }

            if (encodeThead) {
                writer.endElement(HTML.THEAD_ELEMENT);
                
                if (partialUpdateEncoded) {
                    partialEnd(facesContext);
                }
            }
        }

    }
    
    protected void encodeColumnFacet(FacesContext context, ResponseWriter writer, UIDataTableBase dataTableBase,  String facetName, int colCount, String cellClass) throws IOException {
        int tColCount = 0;
        String id  = dataTableBase.getClientId(context);
        String element = getCellElement(context, id);
        
        Iterator<UIComponent> headers = dataTableBase.columns();
        
        while (headers.hasNext()) {
            UIComponent column = headers.next();
            
            
            if (!column.isRendered() || (column instanceof Row)) {
                continue;
            }

            Integer colspan = (Integer) column.getAttributes().get(HTML.COLSPAN_ATTRIBUTE);
            if (colspan != null && colspan.intValue() > 0) {
                tColCount += colspan.intValue();
            } else {
                tColCount++;
            }

            if (tColCount > colCount) {
                break;
            }

            writer.startElement(element, column);

            encodeStyleClass(writer, context, column, null, cellClass);

            writer.writeAttribute(HTML.SCOPE_ATTRIBUTE, HTML.COL_ELEMENT, null);
            getUtils().encodeAttribute(context, column, HTML.COLSPAN_ATTRIBUTE);
            
            EncodeStrategy strategy = getHeaderEncodeStrategy(column, facetName);
            if(strategy != null) {
                strategy.begin(writer, context, column, new String[] {facetName});
    
                UIComponent facet = column.getFacet(facetName);
                if (facet != null && facet.isRendered()) {
                    facet.encodeAll(context);
                }
                strategy.end(writer, context, column, new String[] {facetName});
            } 
            writer.endElement(element);
        }
    }
    
    protected void encodeTableFacet(FacesContext facesContext, ResponseWriter writer, String id, int columns, 
        UIComponent footer, String facetName, String rowClass, String cellClass, boolean encodePartialUpdate) throws IOException {
        
        boolean isColumnGroup = (footer instanceof Row);
        String element = getCellElement(facesContext, id);
        
        boolean partialUpdateEncoded = false;
        
        if (!isColumnGroup) {
            String targetId = id + ":" + facetName.charAt(0);

            if (encodePartialUpdate) {
                partialUpdateEncoded = true;
                partialStart(facesContext, targetId);
            }
            
            writer.startElement(HTML.TR_ELEMENT, footer);
            writer.writeAttribute(HTML.ID_ATTRIBUTE, targetId, null);

            encodeStyleClass(writer, facesContext, footer, null, rowClass);
            
            writer.startElement(element, footer);

            encodeStyleClass(writer, facesContext, footer, null, cellClass);

            if (columns > 0) {
                writer.writeAttribute(HTML.COLSPAN_ATTRIBUTE, String.valueOf(columns), null);
            }

            writer.writeAttribute(HTML.SCOPE_ATTRIBUTE, HTML.COLGROUP_ELEMENT, null);
        }    
        
        if (encodePartialUpdate && !partialUpdateEncoded) {
            partialStart(facesContext, footer.getClientId(facesContext));
        }
        
        footer.encodeAll(facesContext);
       
        if (encodePartialUpdate && !partialUpdateEncoded) {
            partialEnd(facesContext);
        }

        if (!isColumnGroup){
            writer.endElement(element);
            writer.endElement(HTML.TR_ELEMENT);
            
            if (partialUpdateEncoded) {
                partialEnd(facesContext);
            }
        }    
    }   
    
    public  abstract EncodeStrategy getHeaderEncodeStrategy(UIComponent column, String tableFacetName);
      
    public abstract boolean containsThead();
    
    public abstract String getTableSkinClass();
    
    public abstract String getFirstRowSkinClass();

    public abstract String getRowSkinClass();
        
    public abstract String getHeaderCellSkinClass();

    public abstract String getHeaderSkinClass();

    public abstract String getHeaderFirstSkinClass();
    
    public abstract String getColumnHeaderCellSkinClass();

    public abstract String getColumnHeaderSkinClass();
    
    public abstract String getColumnHeaderFirstSkinClass();

    public abstract String getFooterCellSkinClass();

    public abstract String getFooterSkinClass();
    
    public abstract String getFooterFirstSkinClass();

    public abstract String getColumnFooterCellSkinClass();

    public abstract String getColumnFooterSkinClass();
    
    public abstract String getColumnFooterFirstSkinClass();
    
    public abstract String getCellSkinClass();
    
    public abstract String getNoDataClass();

    protected abstract void setupTableStartElement(FacesContext context, UIComponent component);
    
    protected void setupTableStartElement(FacesContext context, UIComponent component, String elementName) {
        put(context, component.getClientId(context) + CELL_ELEMENT_KEY, elementName);
    }

    public void encodeMetaComponent(FacesContext context, UIComponent component, String metaComponentId)
        throws IOException {

        UIDataTableBase table = (UIDataTableBase) component;
        
        
        if (UIDataTableBase.HEADER.equals(metaComponentId)) {
            setupTableStartElement(context, component);
            encodeHeaderFacet(context.getResponseWriter(), context, table, true);
        } else if (UIDataTableBase.FOOTER.equals(metaComponentId)) {
            setupTableStartElement(context, component, HTML.TD_ELEM);
            encodeFooterFacet(context.getResponseWriter(), context, table, true);
        } else if(UIDataTableBase.BODY.equals(metaComponentId)) {
            setupTableStartElement(context, component, HTML.TD_ELEM);
            encodeTableRows(context.getResponseWriter(), context, table, true);
        } else {
            throw new IllegalArgumentException("Unsupported metaComponentIdentifier: " + metaComponentId);
        }
    }
    
    protected void partialStart(FacesContext facesContext, String id) throws IOException {
        facesContext.getPartialViewContext().getPartialResponseWriter().startUpdate(id);
    }
    
    protected void partialEnd(FacesContext facesContext) throws IOException {
        facesContext.getPartialViewContext().getPartialResponseWriter().endUpdate();   
    }
}

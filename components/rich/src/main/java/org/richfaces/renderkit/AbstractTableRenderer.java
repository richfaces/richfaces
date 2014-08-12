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

import org.richfaces.component.Row;
import org.richfaces.component.UIDataTableBase;
import org.richfaces.component.util.HtmlUtil;

/**
 * @author Anton Belevich
 *
 */
@ResourceDependencies({ @ResourceDependency(library = "org.richfaces", name = "richfaces-event.js") })
public abstract class AbstractTableRenderer extends AbstractTableBaseRenderer implements MetaComponentRenderer {
    public static final String HIDDEN_CONTAINER_ID = ":sc";

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
        if (component instanceof UIDataTableBase) {
            rowHolder = new RowHolder(context, (UIDataTableBase) component);
            rowHolder.setUpdatePartial((Boolean) options[0]);
            rowHolder.setEncodeParentTBody((Boolean) options[1]);
        }

        return rowHolder;
    }

    protected class SimpleHeaderEncodeStrategy implements EncodeStrategy {
        public void begin(ResponseWriter writer, FacesContext context, UIComponent column, Object[] params) throws IOException {
        }

        public void end(ResponseWriter writer, FacesContext context, UIComponent column, Object[] params) throws IOException {
        }
    }

    protected void doDecode(FacesContext context, UIComponent component) {
        decodeSortingFiltering(context, component);
    }

    protected void putRowStylesIntoContext(FacesContext facesContext, RowHolderBase rowHolder) {
        UIDataTableBase dataTableBase = (UIDataTableBase) rowHolder.getRow();

        String rowClass = getRowSkinClass();
        String cellClass = getCellSkinClass();
        String firstClass = getFirstRowSkinClass();

        rowClass = mergeStyleClasses(ROW_CLASS_KEY, rowClass, dataTableBase);
        cellClass = mergeStyleClasses(CELL_CLASS_KEY, cellClass, dataTableBase);
        firstClass = mergeStyleClasses(FIRST_ROW_CLASS_KEY, firstClass, dataTableBase);

        saveRowStyles(facesContext, dataTableBase.getClientId(facesContext), firstClass, rowClass, cellClass);
    }

    protected boolean isEncodeHeaders(UIDataTableBase table) {
        return table.isColumnFacetPresent(UIDataTableBase.HEADER) || isColumnAttributeSet(table, "sortBy")
            || isColumnAttributeSet(table, "comparator") || isColumnAttributeSet(table, "filterBy");
    }

    public void encodeTableStructure(ResponseWriter writer, FacesContext context, UIDataTableBase dataTable) throws IOException {
        // DataTableRenderer override this method
    }

    public void encodeBeforeRows(ResponseWriter writer, FacesContext facesContext, UIDataTableBase dataTableBase,
        boolean encodeParentTBody, boolean partialUpdate) throws IOException {
    }

    public void encodeAfterRows(ResponseWriter writer, FacesContext facesContext, UIDataTableBase dataTableBase,
        boolean encodeParentTBody, boolean partialUpdate) throws IOException {
    }

    public abstract boolean encodeParentTBody(UIDataTableBase dataTableBase);

    public void encodeTableFacets(ResponseWriter writer, FacesContext context, UIDataTableBase dataTable) throws IOException {

        Object key = dataTable.getRowKey();
        dataTable.captureOrigValue(context);
        dataTable.setRowKey(context, null);

        encodeTableStructure(writer, context, dataTable);

        encodeHeaderFacet(writer, context, dataTable, false);
        encodeFooterFacet(writer, context, dataTable, false);
        dataTable.setRowKey(context, key);
        dataTable.restoreOrigValue(context);
    }

    public void encodeTableRows(ResponseWriter writer, FacesContext facesContext, UIDataTableBase dataTableBase,
        boolean encodePartialUpdate) throws IOException {

        int rowCount = dataTableBase.getRowCount();

        Object key = dataTableBase.getRowKey();
        dataTableBase.captureOrigValue(facesContext);
        dataTableBase.setRowKey(facesContext, null);

        boolean encodeParentTBody = encodeParentTBody(dataTableBase);
        encodeBeforeRows(writer, facesContext, dataTableBase, encodeParentTBody, encodePartialUpdate);

        if (rowCount > 0) {
            processRows(writer, facesContext, dataTableBase, new Object[] { encodePartialUpdate, encodeParentTBody });
        } else {
            encodeNoDataFacetOrLabel(writer, facesContext, dataTableBase);
        }

        encodeAfterRows(writer, facesContext, dataTableBase, encodeParentTBody, encodePartialUpdate);

        if (encodePartialUpdate) {
            String id = dataTableBase.getClientId(facesContext) + HIDDEN_CONTAINER_ID;
            partialStart(facesContext, id);
        }

        encodeHiddens(writer, facesContext, dataTableBase, new Object[] { encodeParentTBody });

        if (encodePartialUpdate) {
            partialEnd(facesContext);
        }

        dataTableBase.setRowKey(facesContext, key);
        dataTableBase.restoreOrigValue(facesContext);
    }

    public void encodeNoDataFacetOrLabel(ResponseWriter writer, FacesContext facesContext, UIDataTableBase dataTableBase)
        throws IOException {

        int columns = getColumnsCount(dataTableBase.columns());
        UIComponent noDataFacet = dataTableBase.getNoData();
        String noDataLabel = dataTableBase.getNoDataLabel();

        writer.startElement(HtmlConstants.TR_ELEMENT, dataTableBase);
        String styleClass = (String) dataTableBase.getAttributes().get("noDataStyleClass");
        styleClass = concatClasses(getNoDataClass(), styleClass);
        writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, styleClass, null);

        writer.startElement(HtmlConstants.TD_ELEM, dataTableBase);
        writer.writeAttribute(HtmlConstants.COLSPAN_ATTRIBUTE, columns, null);

        String cellStyleClass = (String) dataTableBase.getAttributes().get("noDataCellStyleClass");
        cellStyleClass = concatClasses(getNoDataCellClass(), cellStyleClass);
        writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, cellStyleClass, null);

        if (noDataFacet != null && noDataFacet.isRendered()) {
            noDataFacet.encodeAll(facesContext);
        } else if (noDataLabel != null && noDataLabel.length() > 0) {
            writer.writeText(noDataLabel, "noDataLabel");
        } else {
            writer.writeAttribute(HtmlConstants.STYLE_ATTRIBUTE, "display: none;", null);
        }
        writer.endElement(HtmlConstants.TD_ELEM);
        writer.endElement(HtmlConstants.TR_ELEMENT);
    }

    /**
     * Clear the extendedDataModel before the component encode begins.  This is to force the extendedDataModel to be
     * re-initialized taking into account any model changes that were applied since the model was created in the
     * RESTORE_VIEW phase.
     *
     * @param context
     * @param component
     * @throws IOException
     */
    @Override
    protected void preEncodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.preEncodeBegin(context, component);
        if (component instanceof UIDataTableBase) {
            ((UIDataTableBase) component).clearExtendedDataModel();
        }
    }

    protected void doEncodeChildren(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        if (component instanceof UIDataTableBase) {
            encodeTableRows(writer, context, (UIDataTableBase) component, false);
        }
    }

    public void encodeTableStart(ResponseWriter writer, FacesContext context, UIDataTableBase component) throws IOException {
        writer.startElement(HtmlConstants.TABLE_ELEMENT, component);
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, component.getClientId(context), null);
        String styleClass = getTableSkinClass();
        encodeStyleClass(writer, context, component, HtmlConstants.STYLE_CLASS_ATTR, styleClass);
    }

    protected void encodeHiddens(ResponseWriter writer, FacesContext facesContext, UIDataTableBase dataTableBase,
        Object[] params) throws IOException {
        EncodeStrategy encodeStrategy = getHiddenContainerStrategy(dataTableBase);
        if (encodeStrategy != null) {
            encodeStrategy.begin(writer, facesContext, dataTableBase, params);

            encodeClientScript(writer, facesContext, dataTableBase);
            encodeHiddenInput(writer, facesContext, dataTableBase);

            encodeStrategy.end(writer, facesContext, dataTableBase, params);
        }
    }

    public void encodeTableEnd(ResponseWriter writer) throws IOException {
        writer.endElement(HtmlConstants.TABLE_ELEMENT);
    }

    public abstract void encodeClientScript(ResponseWriter writer, FacesContext context, UIDataTableBase component)
        throws IOException;

    public abstract void encodeHiddenInput(ResponseWriter writer, FacesContext context, UIDataTableBase component)
        throws IOException;

    public void encodeTableBodyStart(ResponseWriter writer, FacesContext facesContext, UIDataTableBase dataTableBase)
        throws IOException {

        writer.startElement(HtmlConstants.TBODY_ELEMENT, dataTableBase);
        String clientId = (dataTableBase.getRowKey() == null) ? dataTableBase.getContainerClientId(facesContext)
            : dataTableBase.getRelativeClientId(facesContext);

        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, clientId + ":tb", null);
        writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, getTableBodySkinClass(), null);
    }

    public void encodeTableBodyEnd(ResponseWriter writer) throws IOException {
        writer.endElement(HtmlConstants.TBODY_ELEMENT);
    }

    public void encodeFooterFacet(ResponseWriter writer, FacesContext facesContext, UIDataTableBase dataTable,
        boolean encodePartialUpdate) throws IOException {

        UIComponent footer = dataTable.getFooter();
        boolean columnFacetPresent = dataTable.isColumnFacetPresent("footer");

        if ((footer != null && footer.isRendered()) || columnFacetPresent) {
            boolean partialUpdateEncoded = false;

            String clientId = dataTable.getClientId(facesContext);
            boolean encodeTfoot = containsThead();
            if (encodeTfoot) {
                String footerClientId = clientId + ":tf";

                if (encodePartialUpdate) {
                    partialUpdateEncoded = true;
                    partialStart(facesContext, footerClientId);
                }

                writer.startElement(HtmlConstants.TFOOT_ELEMENT, dataTable);
                writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, footerClientId, null);
                writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-dt-tft", null);
            }

            int columns = getColumnsCount(dataTable.columns());

            boolean encodePartialUpdateForChildren = (encodePartialUpdate && !partialUpdateEncoded);

            if (columnFacetPresent) {

                String rowClass = getColumnFooterSkinClass();
                String cellClass = getColumnFooterCellSkinClass();
                String firstClass = getColumnFooterFirstSkinClass();

                rowClass = mergeStyleClasses("columnFooterClass", rowClass, dataTable);
                cellClass = mergeStyleClasses("columnFooterCellClass", cellClass, dataTable);
                firstClass = mergeStyleClasses("firstColumnFooterClass", firstClass, dataTable);

                saveRowStyles(facesContext, clientId, firstClass, rowClass, cellClass);

                String targetId = clientId + ":cf";

                if (encodePartialUpdateForChildren) {
                    partialStart(facesContext, targetId);
                }

                writer.startElement(HtmlConstants.TR_ELEMENT, dataTable);
                writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, targetId, null);

                encodeStyleClass(writer, facesContext, dataTable, null, rowClass);
                encodeColumnFacet(facesContext, writer, dataTable, UIDataTableBase.FOOTER, columns, cellClass);
                writer.endElement(HtmlConstants.TR_ELEMENT);

                if (encodePartialUpdateForChildren) {
                    partialEnd(facesContext);
                }
            }

            if (footer != null && footer.isRendered()) {

                String rowClass = getFooterSkinClass();
                String cellClass = getFooterCellSkinClass();
                String firstClass = getFooterFirstSkinClass();

                rowClass = mergeStyleClasses("footerClass", rowClass, dataTable);
                cellClass = mergeStyleClasses("footerCellClass", cellClass, dataTable);
                firstClass = mergeStyleClasses("footerFirstClass", firstClass, dataTable);
                // TODO nick - rename method "encodeTableHeaderFacet"
                saveRowStyles(facesContext, clientId, firstClass, rowClass, cellClass);
                encodeTableFacet(facesContext, writer, clientId, columns, footer, UIDataTableBase.FOOTER, rowClass, cellClass,
                    encodePartialUpdateForChildren);
            }

            if (encodeTfoot) {
                writer.endElement(HtmlConstants.TFOOT_ELEMENT);

                if (partialUpdateEncoded) {
                    partialEnd(facesContext);
                }
            }
        }
    }

    protected String mergeStyleClasses(String classAttribibute, String skinClass, UIComponent component) {
        String resultClass = skinClass;

        String styleClass = null;
        if (classAttribibute != null && component != null) {
            styleClass = (String) component.getAttributes().get(classAttribibute);
        }

        return HtmlUtil.concatClasses(resultClass, styleClass);
    }

    public void encodeHeaderFacet(ResponseWriter writer, FacesContext facesContext, UIDataTableBase dataTable,
        boolean encodePartialUpdate) throws IOException {

        UIComponent header = dataTable.getHeader();
        boolean isEncodeHeaders = isEncodeHeaders(dataTable);

        boolean encodeThead = containsThead();

        if ((header != null && header.isRendered()) || isEncodeHeaders) {

            String id = dataTable.getClientId(facesContext);

            boolean partialUpdateEncoded = false;

            String clientId = dataTable.getClientId(facesContext);
            if (encodeThead) {
                String headerClientId = clientId + ":th";

                if (encodePartialUpdate) {
                    partialUpdateEncoded = true;
                    partialStart(facesContext, headerClientId);
                }

                writer.startElement(HtmlConstants.THEAD_ELEMENT, dataTable);
                setCellElement(facesContext, id, HtmlConstants.TH_ELEM);

                writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, headerClientId, null);
                writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-dt-thd", null);
            }

            int columns = getColumnsCount(dataTable.columns());

            boolean encodePartialUpdateForChildren = (encodePartialUpdate && !partialUpdateEncoded);

            if (header != null && header.isRendered()) {

                String rowClass = getHeaderSkinClass();
                String cellClass = getHeaderCellSkinClass();
                String firstClass = getHeaderFirstSkinClass();

                rowClass = mergeStyleClasses("headerClass", rowClass, dataTable);
                cellClass = mergeStyleClasses("headerCellClass", cellClass, dataTable);
                firstClass = mergeStyleClasses("headerFirstClass", firstClass, dataTable);
                saveRowStyles(facesContext, clientId, firstClass, rowClass, cellClass);

                encodeTableFacet(facesContext, writer, clientId, columns, header, UIDataTableBase.HEADER, rowClass, cellClass,
                    encodePartialUpdateForChildren);
            }

            if (isEncodeHeaders) {

                String rowClass = getColumnHeaderSkinClass();
                String cellClass = getColumnHeaderCellSkinClass();
                String firstClass = getColumnHeaderFirstSkinClass();

                rowClass = mergeStyleClasses("columnHeaderClass", rowClass, dataTable);
                cellClass = mergeStyleClasses("columnHeaderCellClass", cellClass, dataTable);
                firstClass = mergeStyleClasses("columnHeaderFirstClass", firstClass, dataTable);
                saveRowStyles(facesContext, clientId, firstClass, rowClass, cellClass);

                String targetId = clientId + ":ch";

                if (encodePartialUpdateForChildren) {
                    partialStart(facesContext, targetId);
                }

                writer.startElement(HtmlConstants.TR_ELEMENT, dataTable);
                writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, targetId, null);

                encodeStyleClass(writer, facesContext, dataTable, null, rowClass);

                encodeColumnFacet(facesContext, writer, dataTable, UIDataTableBase.HEADER, columns, cellClass);
                writer.endElement(HtmlConstants.TR_ELEMENT);

                if (Boolean.TRUE.equals(dataTable.getAttributes().get("rf-filterRowRequired"))) {  // filter row
                    Iterator<UIComponent> headers = dataTable.columns();  // reset the columns iterator
                    this.renderFilterRow(facesContext, dataTable, headers, "rf-dt");
                }

                if (encodePartialUpdateForChildren) {
                    partialEnd(facesContext);
                }
            }

            if (encodeThead) {
                setCellElement(facesContext, id, null);
                writer.endElement(HtmlConstants.THEAD_ELEMENT);

                if (partialUpdateEncoded) {
                    partialEnd(facesContext);
                }
            }
        }
    }

    protected void encodeColumnFacet(FacesContext context, ResponseWriter writer, UIDataTableBase dataTableBase,
        String facetName, int colCount, String cellClass) throws IOException {
        int tColCount = 0;
        String id = dataTableBase.getClientId(context);
        String element = getCellElement(context, id);

        Iterator<UIComponent> headers = dataTableBase.columns();

        boolean filterRowRequired = false;
        while (headers.hasNext()) {
            UIComponent column = headers.next();

            if (!column.isRendered() || (column instanceof Row)) {
                continue;
            }

            if (!filterRowRequired && this.isFilterRowRequiredForColumn(facetName, column)) {
                filterRowRequired = true;
            }

            Integer colspan = (Integer) column.getAttributes().get(HtmlConstants.COLSPAN_ATTRIBUTE);
            if (colspan != null && colspan.intValue() > 0) {
                tColCount += colspan.intValue();
            } else {
                tColCount++;
            }

            if (tColCount > colCount) {
                break;
            }

            writer.startElement(element, column);

            String columnClass = cellClass;
            boolean useBuiltInSort = this.isBuiltInSortRequiredFocColumn(facetName, column);
            if (useBuiltInSort) {
                columnClass = HtmlUtil.concatClasses( columnClass, "rf-dt-c-srt");
            }

            encodeStyleClass(writer, context, column, facetName + "Class", columnClass);

            if (HtmlConstants.TH_ELEM.equals(element)) { // HTML5 allows scope attr only on th elements
                writer.writeAttribute(HtmlConstants.SCOPE_ATTRIBUTE, HtmlConstants.COL_ELEMENT, null);
            }
            getUtils().encodeAttribute(context, column, HtmlConstants.COLSPAN_ATTRIBUTE);

            EncodeStrategy strategy = getHeaderEncodeStrategy(column, facetName);
            if (strategy != null) {
                strategy.begin(writer, context, column, new String[] { facetName });

                UIComponent facet = column.getFacet(facetName);
                if (facet != null && facet.isRendered()) {
                    facet.encodeAll(context);
                }
                strategy.end(writer, context, column, new String[] { facetName });
            }
            if (useBuiltInSort) {
                this.renderSortButton(context, column, "rf-dt");
            }
            writer.endElement(element);
        }
        dataTableBase.getAttributes().put("rf-filterRowRequired", filterRowRequired);
    }

    protected void encodeTableFacet(FacesContext facesContext, ResponseWriter writer, String id, int columns,
        UIComponent footer, String facetName, String rowClass, String cellClass, boolean encodePartialUpdate)
        throws IOException {

        boolean isColumnGroup = (footer instanceof Row);
        String element = getCellElement(facesContext, id);

        boolean partialUpdateEncoded = false;

        if (!isColumnGroup) {
            String targetId = id + ":" + facetName.charAt(0);

            if (encodePartialUpdate) {
                partialUpdateEncoded = true;
                partialStart(facesContext, targetId);
            }

            writer.startElement(HtmlConstants.TR_ELEMENT, footer);
            writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, targetId, null);

            encodeStyleClass(writer, facesContext, footer, null, rowClass);

            writer.startElement(element, footer);

            encodeStyleClass(writer, facesContext, footer, null, cellClass);

            if (columns > 0) {
                writer.writeAttribute(HtmlConstants.COLSPAN_ATTRIBUTE, String.valueOf(columns), null);
            }

            if (HtmlConstants.TH_ELEM.equals(element)) { // HTML5 allows scope attr only on th elements
                writer.writeAttribute(HtmlConstants.SCOPE_ATTRIBUTE, HtmlConstants.COLGROUP_ELEMENT, null);
            }
        }

        if (encodePartialUpdate && !partialUpdateEncoded) {
            partialStart(facesContext, footer.getClientId(facesContext));
        }

        footer.encodeAll(facesContext);

        if (encodePartialUpdate && !partialUpdateEncoded) {
            partialEnd(facesContext);
        }

        if (isColumnGroup) {
            writer.endElement(HtmlConstants.TR_ELEMENT);
        } else {
            writer.endElement(element);
            writer.endElement(HtmlConstants.TR_ELEMENT);

            if (partialUpdateEncoded) {
                partialEnd(facesContext);
            }
        }
    }

    public abstract EncodeStrategy getHeaderEncodeStrategy(UIComponent column, String tableFacetName);

    public abstract boolean containsThead();

    public abstract String getTableSkinClass();

    public abstract String getTableBodySkinClass();

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

    public abstract String getNoDataCellClass();

    public void encodeMetaComponent(FacesContext context, UIComponent component, String metaComponentId) throws IOException {

        UIDataTableBase table = (UIDataTableBase) component;

        if (UIDataTableBase.HEADER.equals(metaComponentId)) {
            encodeHeaderFacet(context.getResponseWriter(), context, table, true);
        } else if (UIDataTableBase.FOOTER.equals(metaComponentId)) {
            encodeFooterFacet(context.getResponseWriter(), context, table, true);
        } else if (UIDataTableBase.BODY.equals(metaComponentId)) {
            encodeTableRows(context.getResponseWriter(), context, table, true);
        } else {
            throw new IllegalArgumentException("Unsupported metaComponentIdentifier: " + metaComponentId);
        }
    }

    public void decodeMetaComponent(FacesContext context, UIComponent component, String metaComponentId) {
        throw new UnsupportedOperationException();
    }

    protected void partialStart(FacesContext facesContext, String id) throws IOException {
        facesContext.getPartialViewContext().getPartialResponseWriter().startUpdate(id);
    }

    protected void partialEnd(FacesContext facesContext) throws IOException {
        facesContext.getPartialViewContext().getPartialResponseWriter().endUpdate();
    }
}

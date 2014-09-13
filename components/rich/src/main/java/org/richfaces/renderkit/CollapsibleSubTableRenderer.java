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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;

import org.ajax4jsf.javascript.JSFunction;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractCollapsibleSubTable;
import org.richfaces.component.AbstractDataTable;
import org.richfaces.component.Row;
import org.richfaces.component.UIDataTableBase;
import org.richfaces.event.CollapsibleSubTableToggleEvent;
import org.richfaces.renderkit.util.AjaxRendererUtils;

/**
 * @author Anton Belevich
 *
 */
@JsfRenderer(type = "org.richfaces.CollapsibleSubTableRenderer", family = AbstractCollapsibleSubTable.COMPONENT_FAMILY)
@ResourceDependencies({ @ResourceDependency(library = "javax.faces", name = "jsf.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-base-component.js"),
        @ResourceDependency(library = "org.richfaces", name = "collapsible-subtable.js"),
        @ResourceDependency(library = "org.richfaces", name = "collapsible-subtable.ecss") })
public class CollapsibleSubTableRenderer extends AbstractTableRenderer {
    public static final String TB_ROW = ":c";
    private static final String STATE = ":state";
    private static final String OPTIONS = ":options";
    private static final String DISPLAY_NONE = "display: none;";

    private class CollapsibleSubTableHiddenEncodeStrategy implements EncodeStrategy {
        public void begin(ResponseWriter writer, FacesContext context, UIComponent component, Object[] params)
            throws IOException {
            AbstractCollapsibleSubTable subTable = (AbstractCollapsibleSubTable) component;
            writer.startElement(HtmlConstants.TR_ELEMENT, subTable);
            writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, subTable.getContainerClientId(context) + HIDDEN_CONTAINER_ID,
                null);
            writer.writeAttribute(HtmlConstants.STYLE_ATTRIBUTE, DISPLAY_NONE, null);
            writer.startElement(HtmlConstants.TD_ELEM, subTable);
        }

        public void end(ResponseWriter writer, FacesContext context, UIComponent component, Object[] params) throws IOException {
            writer.endElement(HtmlConstants.TD_ELEM);
            writer.endElement(HtmlConstants.TR_ELEMENT);

            if (params != null && params.length == 1) {
                boolean endTbody = (Boolean) params[0];
                if (endTbody) {
                    encodeTableBodyEnd(writer);
                }
            }
        }
    }

    ;

    protected void doDecode(FacesContext facesContext, UIComponent component) {
        super.doDecode(facesContext, component);
        AbstractCollapsibleSubTable subTable = (AbstractCollapsibleSubTable) component;

        String clientId = subTable.getClientId(facesContext);
        Map<String, String> requestMap = facesContext.getExternalContext().getRequestParameterMap();

        String optionsId = clientId + OPTIONS;
        String togglerId = requestMap.get(optionsId);

        String stateId = clientId + STATE;
        String state = (String) requestMap.get(stateId);

        boolean isExpanded = true;
        if (state != null) {
            int newValue = Integer.parseInt(state);

            if (newValue < 1) {
                isExpanded = false;
            }

            if (subTable.isExpanded() != isExpanded) {
                new CollapsibleSubTableToggleEvent(subTable, isExpanded, togglerId).queue();
            }
        }
    }

    @Override
    protected void decodeFiltering(FacesContext context, UIDataTableBase dataTableBase, String value) {
        super.decodeFiltering(context, dataTableBase, value);
        dataTableBase.clearExtendedDataModel();
    }

    @Override
    protected void decodeSorting(FacesContext context, UIDataTableBase dataTableBase, String value) {
        super.decodeSorting(context, dataTableBase, value);
        dataTableBase.clearExtendedDataModel();
    }

    @Override
    public void encodeFirstRowStart(ResponseWriter writer, FacesContext context, String parentId, int currentRow,
        UIComponent component) throws IOException {
        writer.startElement(HtmlConstants.TR_ELEMENT, component);
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, parentId + ":" + currentRow + ":b", null);
        String styleClass = concatClasses(getRowClass(context, parentId), getFirstRowClass(context, parentId), component
            .getAttributes().get(ROW_CLASS));
        encodeStyleClass(writer, context, component, HtmlConstants.STYLE_CLASS_ATTR, styleClass);
        UIComponent parent = component.getParent();
        if (parent instanceof AbstractCollapsibleSubTable && Boolean.TRUE.equals(parent.getAttributes().get("isNested"))) {
            AbstractCollapsibleSubTable subTable = (AbstractCollapsibleSubTable) parent;
            if (!subTable.isExpanded()) {
                writer.writeAttribute(HtmlConstants.STYLE_ATTRIBUTE, DISPLAY_NONE, null);
            }
        }
    }

    @Override
    public void encodeRowStart(ResponseWriter writer, FacesContext context, String parentId, int currentRow,
        UIComponent component) throws IOException {
        writer.startElement(HtmlConstants.TR_ELEMENT, component);
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, parentId + ":" + currentRow + ":b", null);
        String styleClass = concatClasses(getRowClass(context, parentId), component.getAttributes().get(ROW_CLASS));
        encodeStyleClass(writer, context, component, HtmlConstants.STYLE_CLASS_ATTR, styleClass);
        UIComponent parent = component.getParent();
        if (parent instanceof AbstractCollapsibleSubTable && Boolean.TRUE.equals(parent.getAttributes().get("isNested"))) {
            AbstractCollapsibleSubTable subTable = (AbstractCollapsibleSubTable) parent;
            if (!subTable.isExpanded()) {
                writer.writeAttribute(HtmlConstants.STYLE_ATTRIBUTE, DISPLAY_NONE, null);
            }
        }
    }

    public void encodeTableFacets(ResponseWriter writer, FacesContext context, UIDataTableBase dataTable) throws IOException {
        AbstractCollapsibleSubTable subTable = (AbstractCollapsibleSubTable) dataTable;

        encodeStyle(writer, context, subTable, null);

        encodeHeaderFacet(writer, context, subTable, false);

        String rowClass = getRowSkinClass();
        String cellClass = getCellSkinClass();
        String firstClass = getFirstRowSkinClass();

        rowClass = mergeStyleClasses("rowClass", rowClass, subTable);
        cellClass = mergeStyleClasses("cellClass", cellClass, subTable);
        firstClass = mergeStyleClasses("firstRowClass", firstClass, subTable);

        saveRowStyles(context, subTable.getContainerClientId(context), firstClass, rowClass, cellClass);
    }

    @Override
    public void encodeTableBodyStart(ResponseWriter writer, FacesContext facesContext, UIDataTableBase dataTableBase)
        throws IOException {
        AbstractCollapsibleSubTable subTable = (AbstractCollapsibleSubTable) dataTableBase;

        UIDataTableBase parent = findParent(subTable);
        if (parent instanceof AbstractDataTable) {
            writer.startElement(HtmlConstants.TBODY_ELEMENT, null);
            writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE,
                parent.getRelativeClientId(facesContext) + ":" + subTable.getId() + TB_ROW, null);
            writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, getTableSkinClass(), null);

            String predefinedStyles = !subTable.isExpanded() ? DISPLAY_NONE : null;
            encodeStyle(writer, facesContext, subTable, predefinedStyles);
        }
        // stash whether or not this subTable is nested in the attribute map for later retrieval
        subTable.getAttributes().put("isNested", (parent instanceof AbstractCollapsibleSubTable));
    }

    @Override
    public void encodeBeforeRows(ResponseWriter writer, FacesContext facesContext, UIDataTableBase dataTableBase,
        boolean encodeParentTBody, boolean partialUpdate) throws IOException {
        AbstractCollapsibleSubTable subTable = (AbstractCollapsibleSubTable) dataTableBase;
        encodeTableBodyStart(writer, facesContext, subTable);
        encodeSubTableDomElement(writer, facesContext, subTable);
        encodeHeaderFacet(writer, facesContext, subTable, false);
    }

    private void encodeSubTableDomElement(ResponseWriter writer, FacesContext facesContext, AbstractCollapsibleSubTable subTable)
        throws IOException {
        writer.startElement(HtmlConstants.TR_ELEMENT, subTable);
        writer.writeAttribute(HtmlConstants.STYLE_ATTRIBUTE, DISPLAY_NONE, null);
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, subTable.getContainerClientId(facesContext), null);
        writer.startElement(HtmlConstants.TD_ELEM, subTable);
        writer.endElement(HtmlConstants.TD_ELEM);
        writer.endElement(HtmlConstants.TR_ELEMENT);
    }

    public void encodeRow(ResponseWriter writer, FacesContext facesContext, RowHolderBase holder) throws IOException {
        RowHolder rowHolder = (RowHolder) holder;
        Row row = rowHolder.getRow();
        putRowStylesIntoContext(facesContext, rowHolder);
        rowHolder.setRowStart(true);
        Iterator<UIComponent> components = row.columns();
        if (rowHolder.isUpdatePartial()) {
            partialStart(facesContext, ((AbstractCollapsibleSubTable) row).getRelativeClientId(facesContext) + ":b");
        }

        int columnNumber = 0;
        boolean rowEnded = false;
        while (components.hasNext()) {
            UIComponent component = components.next();
            if (component.isRendered()) {
                if (component instanceof UIColumn) {
                    component.getAttributes().put(COLUMN_CLASS, getColumnClass(rowHolder, columnNumber));
                    encodeColumn(facesContext, writer, (UIColumn) component, rowHolder);
                    columnNumber++;
                } else if (component instanceof AbstractCollapsibleSubTable) {
                    if (component.isRendered()) {
                        rowEnded = true;
                        encodeRowEnd(writer);
                    }

                    component.encodeAll(facesContext);
                    rowHolder.setRowStart(true);
                }
            }
        }

        if (!rowEnded) {
            encodeRowEnd(writer);
        }

        if (rowHolder.isUpdatePartial()) {
            partialEnd(facesContext);
        }
    }

    @Override
    public void encodeAfterRows(ResponseWriter writer, FacesContext facesContext, UIDataTableBase dataTableBase,
        boolean encodeParentTBody, boolean partialUpdate) throws IOException {
        AbstractCollapsibleSubTable subTable = (AbstractCollapsibleSubTable) dataTableBase;
        encodeFooterFacet(writer, facesContext, subTable, false);
    }

    @Override
    public boolean encodeParentTBody(UIDataTableBase dataTableBase) {
        UIDataTableBase parent = findParent((AbstractCollapsibleSubTable) dataTableBase);
        return (parent instanceof AbstractDataTable);
    }

    public void encodeHiddenInput(ResponseWriter writer, FacesContext facesContext, UIDataTableBase dataTableBase)
        throws IOException {
        AbstractCollapsibleSubTable subTable = (AbstractCollapsibleSubTable) dataTableBase;

        String stateId = subTable.getClientId(facesContext) + STATE;

        writer.startElement(HtmlConstants.INPUT_ELEM, subTable);
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, stateId, null);
        writer.writeAttribute(HtmlConstants.NAME_ATTRIBUTE, stateId, null);
        writer.writeAttribute(HtmlConstants.TYPE_ATTR, HtmlConstants.INPUT_TYPE_HIDDEN, null);

        int state = subTable.isExpanded() ? AbstractCollapsibleSubTable.EXPANDED_STATE
            : AbstractCollapsibleSubTable.COLLAPSED_STATE;

        writer.writeAttribute(HtmlConstants.VALUE_ATTRIBUTE, state, null);
        writer.endElement(HtmlConstants.INPUT_ELEM);

        String optionsId = subTable.getClientId(facesContext) + OPTIONS;
        writer.startElement(HtmlConstants.INPUT_ELEM, subTable);
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, optionsId, null);
        writer.writeAttribute(HtmlConstants.NAME_ATTRIBUTE, optionsId, null);
        writer.writeAttribute(HtmlConstants.TYPE_ATTR, HtmlConstants.INPUT_TYPE_HIDDEN, null);
        writer.endElement(HtmlConstants.INPUT_ELEM);
    }

    public boolean containsThead() {
        return false;
    }

    public EncodeStrategy getHeaderEncodeStrategy(UIComponent column, String tableFacetName) {
        // TODO: anton -> use RichHeaderEncodeStrategy for our columns ???
        return new SimpleHeaderEncodeStrategy();
    }

    public void encodeClientScript(ResponseWriter writer, FacesContext facesContext, UIDataTableBase component)
        throws IOException {
        AbstractCollapsibleSubTable subTable = (AbstractCollapsibleSubTable) component;

        String id = subTable.getClientId(facesContext);

        UIComponent nestingForm = getUtils().getNestingForm(subTable);
        String formId = nestingForm != null ? nestingForm.getClientId(facesContext) : "";
        AjaxOptions ajaxOptions = AjaxRendererUtils.buildEventOptions(facesContext, subTable);

        Map<String, Object> options = new HashMap<String, Object>();
        options.put("ajaxEventOptions", ajaxOptions.getParameters());
        options.put("stateInput", subTable.getClientId(facesContext) + STATE);
        options.put("optionsInput", subTable.getClientId(facesContext) + OPTIONS);
        boolean isNested = Boolean.TRUE.equals(subTable.getAttributes().get("isNested"));
        String expandMode = isNested ? "client" : subTable.getExpandMode();
        options.put("expandMode", expandMode);
        options.put("isNested", isNested);
        options.put("eventOptions", AjaxRendererUtils.buildEventOptions(facesContext, subTable));

        JSFunction jsFunction = new JSFunction("new RichFaces.ui.CollapsibleSubTable");
        jsFunction.addParameter(id);
        jsFunction.addParameter(formId);
        jsFunction.addParameter(options);

        writer.startElement(HtmlConstants.SCRIPT_ELEM, subTable);
        writer.writeAttribute(HtmlConstants.TYPE_ATTR, HtmlConstants.JAVASCRIPT_TYPE, null);
        writer.writeText(jsFunction.toScript(), null);
        writer.endElement(HtmlConstants.SCRIPT_ELEM);
    }

    public String getTableSkinClass() {
        return "rf-cst";
    }

    public String getRowSkinClass() {
        return "rf-cst-r";
    }

    public String getFirstRowSkinClass() {
        return "rf-cst-fst-r";
    }

    public String getHeaderRowSkinClass() {
        return "rf-cst-hdr-r";
    }

    public String getHeaderFirstRowSkinClass() {
        return "rf-cst-hdr-fst-r";
    }

    public String getCellSkinClass() {
        return "rf-cst-c";
    }

    public String getHeaderCellSkinClass() {
        return "rf-cst-hdr-c";
    }

    public String getColumnHeaderCellSkinClass() {
        return "rf-cst-shdr-c";
    }

    public String getColumnHeaderSkinClass() {
        return "rf-cst-shdr";
    }

    public String getFooterSkinClass() {
        return "rf-cst-ftr";
    }

    public String getFooterCellSkinClass() {
        return "rf-cst-ftr-c";
    }

    public String getFooterFirstRowSkinClass() {
        return "rf-cst-ftr-fst";
    }

    public String getColumnFooterCellSkinClass() {
        return "rf-cst-sftr-c";
    }

    public String getColumnFooterSkinClass() {
        return "rf-cst-sftr";
    }

    public String getColumnFooterFirstSkinClass() {
        return "rf-cst-sftr-fst";
    }

    public String getColumnHeaderFirstSkinClass() {
        return "rf-cst-shdr-fst";
    }

    public String getFooterFirstSkinClass() {
        return "rf-cst-ftr-fst";
    }

    public String getHeaderFirstSkinClass() {
        return "rf-cst-hdr-fst";
    }

    public String getHeaderSkinClass() {
        return "rf-cst-hdr";
    }

    public String getNoDataClass() {
        return "rf-cst-nd";
    }

    @Override
    public String getNoDataCellClass() {
        return "rf-cst-nd-c";
    }

    @Override
    public String getTableBodySkinClass() {
        // AbstractSubTable doesn't have tbody
        return null;
    }

    @Override
    public void encodeMetaComponent(FacesContext facesContext, UIComponent component, String metaComponentId)
        throws IOException {
        AbstractCollapsibleSubTable subTable = (AbstractCollapsibleSubTable) component;

        if (AbstractCollapsibleSubTable.BODY.equals(metaComponentId)) {
            ResponseWriter writer = facesContext.getResponseWriter();
            UIDataTableBase dataTableBase = findParent(subTable);

            String updateId = dataTableBase.getRelativeClientId(facesContext) + ":" + subTable.getId() + TB_ROW;

            partialStart(facesContext, updateId);
            encodeTableRows(writer, facesContext, subTable, false);
            partialEnd(facesContext);
        }
    }

    @Override
    public EncodeStrategy getHiddenContainerStrategy(UIDataTableBase dataTableBase) {
        return new CollapsibleSubTableHiddenEncodeStrategy();
    }

    protected UIDataTableBase findParent(AbstractCollapsibleSubTable subTable) {
        UIComponent parent = subTable.getParent();

        while (parent != null && !(parent instanceof UIDataTableBase)) {
            parent = parent.getParent();
        }

        if (parent == null) {
            // TODO: anton -> do we need this?
            throw new AbortProcessingException("UISubTable should be a child of UIDataTable or UISubTable");
        }
        return (UIDataTableBase) parent;
    }
}
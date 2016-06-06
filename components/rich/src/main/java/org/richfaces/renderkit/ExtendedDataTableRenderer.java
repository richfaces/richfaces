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

import static org.richfaces.renderkit.RenderKitUtils.addToScriptHash;
import static org.richfaces.renderkit.RenderKitUtils.renderAttribute;
import static org.richfaces.renderkit.util.AjaxRendererUtils.buildAjaxFunction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.PartialViewContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.javascript.JSFunction;
import org.ajax4jsf.javascript.JSReference;
import org.ajax4jsf.javascript.ScriptUtils;
import org.ajax4jsf.model.DataVisitResult;
import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.SequenceRange;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractColumn;
import org.richfaces.component.AbstractExtendedDataTable;
import org.richfaces.component.ExtendedDataTableState;
import org.richfaces.component.SortOrder;
import org.richfaces.component.UIDataAdaptor;
import org.richfaces.component.UIDataTableBase;
import org.richfaces.component.util.HtmlUtil;
import org.richfaces.context.OnOffResponseWriter;
import org.richfaces.model.SelectionMode;
import org.richfaces.renderkit.RenderKitUtils.ScriptHashVariableWrapper;

/**
 * @author Konstantin Mishin
 *
 */
@JsfRenderer(type = "org.richfaces.ExtendedDataTableRenderer", family = AbstractExtendedDataTable.COMPONENT_FAMILY)
@ResourceDependencies({ @ResourceDependency(library = "javax.faces", name = "jsf.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-queue.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-base-component.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.position.js"),
        @ResourceDependency(library = "org.richfaces", name = "richfaces-event.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.mousewheel.js"),
        @ResourceDependency(library = "org.richfaces", name = "popup.js"),
        @ResourceDependency(library = "org.richfaces", name = "extendedDataTable.js"),
        @ResourceDependency(library = "org.richfaces", name = "extendedDataTable.ecss") })
public class ExtendedDataTableRenderer extends SelectionRenderer implements MetaComponentRenderer {
    private static final JSReference CLIENT_PARAMS = new JSReference("clientParams");
    private static final String DATA_ATTRIBUTE = "data-rf-column-name";
    private static final String BEHAVIOR_EVENT_NAME = "javax.faces.behavior.event";
    private static final String ROW = "row";

    protected static enum PartName {

        frozen,
        normal;
        private String id;

        private PartName() {
            id = String.valueOf(this.toString().charAt(0));
        }

        public String getId() {
            return id;
        }
    }

    protected final class Part {
        private PartName name;
        private List<UIComponent> columns;

        public Part(PartName name, List<UIComponent> columns) {
            this.name = name;
            this.columns = columns;
        }

        public PartName getName() {
            return name;
        }

        public List<UIComponent> getColumns() {
            return columns;
        }
    }

    protected class RendererState extends RowHolderBase {
        private UIDataTableBase table;
        private List<Part> parts;
        private Part current;
        private Iterator<Part> partIterator;
        private EncoderVariance encoderVariance = EncoderVariance.full;
        private int frozenColumnsSize;

        public RendererState(FacesContext context, UIDataTableBase table) {
            super(context);
            this.table = table;

            List<UIComponent> columns = getOrderedColumns(context);

            frozenColumnsSize = (Integer) table.getAttributes().get("frozenColumns");
            if (frozenColumnsSize < 0 || frozenColumnsSize >= columns.size()) {
                frozenColumnsSize = 0;
            }

            int count = Math.min(frozenColumnsSize, columns.size());
            List<UIComponent> frozenColumns = columns.subList(0, count);
            columns = columns.subList(count, columns.size());
            parts = new ArrayList<Part>(PartName.values().length);
            if (frozenColumns.size() > 0) {
                parts.add(new Part(PartName.frozen, frozenColumns));
            }
            if (columns.size() > 0) {
                parts.add(new Part(PartName.normal, columns));
            }
        }

        protected List<UIComponent> getOrderedColumns(FacesContext context) {
            Map<String, UIComponent> columnsMap = new LinkedHashMap<String, UIComponent>();
            Iterator<UIComponent> iterator = table.columns();
            while (iterator.hasNext()) { // initialize a map of all the columns
                UIComponent component = iterator.next();
                if (component.isRendered()) {
                    columnsMap.put(component.getId(), component);
                }
            }

            List<UIComponent> columns = new ArrayList<UIComponent>();

            String[] columnsOrder = RenderKitUtils.evaluateAttribute("columnsOrder", table, context);
            if (columnsOrder != null && columnsOrder.length > 0) { // add columns in the order specified by columnsOrder
                for (int i = 0; i < columnsOrder.length && !columnsMap.isEmpty(); i++) {
                    columns.add(columnsMap.remove(columnsOrder[i]));
                }
            }
            for (UIComponent column : columnsMap.values()) { // add the remaining columns
                columns.add(column);
            }

            return columns;
        }

        public UIDataTableBase getRow() {
            return table;
        }

        public void startIterate() {
            partIterator = parts.iterator();
        }

        public Part nextPart() {
            current = partIterator.next();
            return current;
        }

        public Part getPart() {
            return current;
        }

        public boolean hasNextPart() {
            return partIterator.hasNext();
        }

        public EncoderVariance getEncoderVariance() {
            return encoderVariance;
        }

        public void setEncoderVariance(EncoderVariance encoderVariance) {
            this.encoderVariance = encoderVariance;
        }
    }

    protected enum EncoderVariance {
        full {
            public void encodeStartUpdate(FacesContext context, String targetId) throws IOException {
                // do nothing
            }

            public void encodeEndUpdate(FacesContext context) throws IOException {
                // do nothing
            }
        },
        partial {
            private void switchResponseWriter(FacesContext context, boolean writerState) {
                ResponseWriter writer = context.getResponseWriter();
                ((OnOffResponseWriter) writer).setSwitchedOn(writerState);
            }

            public void encodeStartUpdate(FacesContext context, String targetId) throws IOException {
                switchResponseWriter(context, true);

                context.getPartialViewContext().getPartialResponseWriter().startUpdate(targetId);
            }

            public void encodeEndUpdate(FacesContext context) throws IOException {
                context.getPartialViewContext().getPartialResponseWriter().endUpdate();

                switchResponseWriter(context, false);
            }
        };

        public abstract void encodeStartUpdate(FacesContext context, String targetId) throws IOException;

        public abstract void encodeEndUpdate(FacesContext context) throws IOException;

    }

    private static final Map<java.lang.String, ComponentAttribute> EVENT_ATTRIBUTES = Collections
        .unmodifiableMap(ComponentAttribute.createMap(
            new ComponentAttribute("onselectionchange").setEventNames(new String[] { "selectionchange" }),
            new ComponentAttribute("onbeforeselectionchange").setEventNames(new String[] { "beforeselectionchange" }),
            new ComponentAttribute("onready").setEventNames(new String[] { "ready" })));

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

    private void encodeEmptyFooterCell(FacesContext context, ResponseWriter writer, UIComponent column, boolean isLastColumn) throws IOException {
        if (column.isRendered()) {
            writer.startElement(HtmlConstants.TD_ELEM, column);
            if (!isLastColumn) {
                writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-edt-td-" + column.getId(), null);
            }
            writer.startElement(HtmlConstants.DIV_ELEM, column);
            writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-edt-ftr-c-emp rf-edt-c-" + column.getId(), null);
            writer.endElement(HtmlConstants.DIV_ELEM);
            writer.endElement(HtmlConstants.TD_ELEM);
        }
    }

    private void encodeHeaderOrFooterCell(FacesContext context, ResponseWriter writer, UIComponent column, String facetName, boolean isLastColumn)
        throws IOException {
        if (column.isRendered()) {

            String classAttribute = facetName + "Class";
            boolean useBuiltInSort = this.isBuiltInSortRequiredFocColumn(facetName, column);
            writer.startElement(HtmlConstants.TD_ELEM, column);
            if (!isLastColumn) {
                writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-edt-td-" + column.getId(), null);
            }
            if ("header".equals(facetName)) {
                writer.startElement(HtmlConstants.DIV_ELEM, column);
                writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-edt-rsz-cntr rf-edt-c-" + column.getId(), null);
                writer.startElement(HtmlConstants.DIV_ELEM, column);
                writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-edt-rsz", null);
                writer.endElement(HtmlConstants.DIV_ELEM);
                writer.endElement(HtmlConstants.DIV_ELEM);
            }

            writer.startElement(HtmlConstants.DIV_ELEM, column);
            writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, HtmlUtil.concatClasses("rf-edt-"
                + getFacetClassName(facetName) + "-c", "rf-edt-c-" + column.getId()), null);
            writer.startElement(HtmlConstants.DIV_ELEM, column);
            String columnHeaderClass = "rf-edt-" + getFacetClassName(facetName) + "-c-cnt";
            if (useBuiltInSort) {
                columnHeaderClass = HtmlUtil.concatClasses( columnHeaderClass, "rf-edt-c-srt");
            }
            columnHeaderClass = HtmlUtil.concatClasses( columnHeaderClass, column.getAttributes().get(classAttribute));
            writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, columnHeaderClass, null);
            UIComponent facet = column.getFacet(facetName);
            if (facet != null && facet.isRendered()) {
                facet.encodeAll(context);
            }

            if (useBuiltInSort) {
                this.renderSortButton(context, column, "rf-edt");
            }

            writer.endElement(HtmlConstants.DIV_ELEM);
            writer.endElement(HtmlConstants.DIV_ELEM);
            writer.endElement(HtmlConstants.TD_ELEM);
        }
    }

    private String getFacetClassName(String name) {
        if ("header".equals(name)) {
            return "hdr";
        } else if ("footer".equals(name)) {
            return "ftr";
        }

        throw new IllegalArgumentException(name);
    }

    private void encodeHeaderOrFooter(RendererState state, String facetName) throws IOException {
        FacesContext context = state.getContext();
        ResponseWriter writer = context.getResponseWriter();
        UIDataTableBase table = state.getRow();
        boolean columnFacetPresent = table.isColumnFacetPresent(facetName);
        if (columnFacetPresent || "footer".equals(facetName)) {
            writer.startElement(HtmlConstants.DIV_ELEM, table);
            writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-edt-" + getFacetClassName(facetName), null);
            writer.startElement(HtmlConstants.TABLE_ELEMENT, table);
            writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-edt-tbl", null);
            writer.startElement(HtmlConstants.TBODY_ELEMENT, table);
            writer.startElement(HtmlConstants.TR_ELEMENT, table);
            String clientId = table.getClientId(context);
            for (state.startIterate(); state.hasNextPart();) {
                Part part = state.nextPart();
                PartName partName = part.getName();
                Iterator<UIComponent> columns = part.getColumns().iterator();
                if (columns.hasNext()) {
                    writer.startElement(HtmlConstants.TD_ELEM, table);
                    if (PartName.frozen.equals(partName) && "footer".equals(facetName)) {
                        writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-edt-ftr-fzn", null);
                    }
                    writer.startElement(HtmlConstants.DIV_ELEM, table);
                    if (PartName.frozen.equals(partName)) {
                        if ("header".equals(facetName)) {
                            writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, clientId + ":frozenHeader", null);
                        }
                    } else {
                        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, clientId + ":" + facetName, null);
                        writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-edt-cnt"
                            + ("footer".equals(facetName) ? " rf-edt-ftr-cnt" : ""), null);
                    }

                    String tableId = clientId + ":cf" + facetName.charAt(0) + partName.getId();
                    EncoderVariance encoderVariance = state.getEncoderVariance();
                    encoderVariance.encodeStartUpdate(context, tableId);

                    writer.startElement(HtmlConstants.TABLE_ELEMENT, table);
                    writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, tableId, null);
                    writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-edt-tbl", null);
                    writer.startElement(HtmlConstants.TBODY_ELEMENT, table);
                    writer.startElement(HtmlConstants.TR_ELEMENT, table);
                    int columnNumber = 0;
                    boolean filterRowRequired = false;
                    int lastColumnNumber = part.getColumns().size() - 1;
                    while (columns.hasNext()) {
                        UIComponent column = columns.next();
                        if (!filterRowRequired && this.isFilterRowRequiredForColumn(facetName, column)) {
                            filterRowRequired = true;
                        }
                        if (columnFacetPresent) {
                            encodeHeaderOrFooterCell(context, writer, column, facetName, columnNumber == lastColumnNumber);
                        } else {
                            encodeEmptyFooterCell(context, writer, column, columnNumber == lastColumnNumber);
                        }
                        columnNumber++;
                    }
                    writer.endElement(HtmlConstants.TR_ELEMENT);
                    if (filterRowRequired) {  // filter row
                        columns = part.getColumns().iterator();  // reset the columns iterator
                        this.renderFilterRow(context, table, columns, "rf-edt");
                    }
                    writer.endElement(HtmlConstants.TBODY_ELEMENT);
                    writer.endElement(HtmlConstants.TABLE_ELEMENT);

                    encoderVariance.encodeEndUpdate(context);

                    writer.endElement(HtmlConstants.DIV_ELEM);

                    writer.endElement(HtmlConstants.TD_ELEM);
                }
            }
            writer.endElement(HtmlConstants.TR_ELEMENT);
            // the start of the scroller
            if ("footer".equals(facetName)) {
                int frozenColumns = 0;
                int scrollingColumns = 0;
                for (state.startIterate(); state.hasNextPart();) {
                    Part part = state.nextPart();
                    PartName partName = part.getName();
                    Iterator<UIComponent> columns = part.getColumns().iterator();
                    if (columns.hasNext()) {
                        if (PartName.frozen.equals(partName)) {
                            frozenColumns += 1;
                        } else {
                            scrollingColumns += 1;
                        }
                    }
                }
                writer.startElement(HtmlConstants.TR_ELEMENT, table);
                if (frozenColumns > 0) {
                    writer.startElement(HtmlConstants.TD_ELEM, table);
                    writer.writeAttribute(HtmlConstants.COLSPAN_ATTRIBUTE, frozenColumns, null);
                    writer.endElement(HtmlConstants.TD_ELEM);
                }
                if (scrollingColumns > 0) {
                    writer.startElement(HtmlConstants.TD_ELEM, table);
                    writer.writeAttribute(HtmlConstants.COLSPAN_ATTRIBUTE, scrollingColumns, null);
                    writer.startElement(HtmlConstants.DIV_ELEM, table);
                    writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, clientId + ":scrl", null);
                    writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-edt-scrl", null);
                    writer.startElement(HtmlConstants.DIV_ELEM, table);
                    writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, clientId + ":scrl-cnt", null);
                    writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-edt-scrl-cnt", null);
                    writer.endElement(HtmlConstants.DIV_ELEM);
                    writer.endElement(HtmlConstants.DIV_ELEM);
                    writer.endElement(HtmlConstants.TD_ELEM);
                }
                writer.endElement(HtmlConstants.TR_ELEMENT);
            }
            // the end of the scroller
            writer.endElement(HtmlConstants.TBODY_ELEMENT);
            writer.endElement(HtmlConstants.TABLE_ELEMENT);
            writer.endElement(HtmlConstants.DIV_ELEM);
        }
    }

    public void encodeHeader(RendererState state) throws IOException {
        FacesContext context = state.getContext();
        ResponseWriter writer = context.getResponseWriter();
        UIDataTableBase table = state.getRow();

        UIComponent header = table.getFacet("header");
        if (header != null && header.isRendered()) {
            String elementId = table.getClientId(context) + ":tfh";

            EncoderVariance encoderVariance = state.getEncoderVariance();
            encoderVariance.encodeStartUpdate(context, elementId);

            writer.startElement(HtmlConstants.DIV_ELEM, table);
            writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, elementId, null);
            writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE,
                HtmlUtil.concatClasses("rf-edt-tbl-hdr", table.getHeaderClass()), null);
            header.encodeAll(context);
            writer.endElement(HtmlConstants.DIV_ELEM);

            encoderVariance.encodeEndUpdate(context);
        }

        encodeHeaderOrFooter(state, "header");
    }

    public void encodeBody(RendererState state) throws IOException {
        FacesContext context = state.getContext();
        ResponseWriter writer = context.getResponseWriter();
        UIDataTableBase table = state.getRow();
        String clientId = table.getClientId(context);
        String tableBodyId = clientId + ":b";
        EncoderVariance encoderVariance = state.getEncoderVariance();
        encoderVariance.encodeStartUpdate(context, tableBodyId);
        writer.startElement(HtmlConstants.DIV_ELEM, table);
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, tableBodyId, null);
        writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-edt-b", null);
        if (table.getRowCount() == 0) {
            UIComponent facet = table.getFacet("noData");
            writer.startElement(HtmlConstants.DIV_ELEM, table);
            writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-edt-ndt", null);
            if (facet != null && facet.isRendered()) {
                facet.encodeAll(context);
            } else {
                Object noDataLabel = table.getAttributes().get("noDataLabel");
                if (noDataLabel != null) {
                    writer.writeText(noDataLabel, "noDataLabel");
                }
            }
            writer.endElement(HtmlConstants.DIV_ELEM);
        } else {
            table.getAttributes().put("clientFirst", 0);
            writer.startElement(HtmlConstants.DIV_ELEM, table);
            writer.startElement(HtmlConstants.DIV_ELEM, table);
            writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-edt-spcr", null);
            writer.endElement(HtmlConstants.DIV_ELEM);
            writer.startElement(HtmlConstants.TABLE_ELEMENT, table);
            writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-edt-tbl", null);
            writer.startElement(HtmlConstants.TBODY_ELEMENT, table);
            writer.startElement(HtmlConstants.TR_ELEMENT, table);
            for (state.startIterate(); state.hasNextPart();) {
                writer.startElement(HtmlConstants.TD_ELEM, table);
                writer.startElement(HtmlConstants.DIV_ELEM, table);
                PartName partName = state.nextPart().getName();
                if (PartName.normal.equals(partName)) {
                    writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, clientId + ":body", null);
                    writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-edt-cnt", null);
                }
                String targetId = clientId + ":tbt" + partName.getId();
                writer.startElement(HtmlConstants.TABLE_ELEMENT, table);
                writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, targetId, null);
                writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-edt-tbl", null);
                writer.startElement(HtmlConstants.TBODY_ELEMENT, table);
                writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, clientId + ":tb" + partName.getId(), null);
                state.resetCurrentRow();
                encodeRows(context, state);
                writer.endElement(HtmlConstants.TBODY_ELEMENT);
                writer.endElement(HtmlConstants.TABLE_ELEMENT);

                writer.endElement(HtmlConstants.DIV_ELEM);
                writer.endElement(HtmlConstants.TD_ELEM);
            }
            writer.endElement(HtmlConstants.TR_ELEMENT);
            writer.endElement(HtmlConstants.TBODY_ELEMENT);
            writer.endElement(HtmlConstants.TABLE_ELEMENT);
            writer.endElement(HtmlConstants.DIV_ELEM);
        }
        writer.endElement(HtmlConstants.DIV_ELEM);
        encoderVariance.encodeEndUpdate(context);
    }

    public void encodeFooter(RendererState state) throws IOException {
        FacesContext context = state.getContext();
        ResponseWriter writer = context.getResponseWriter();
        UIDataTableBase table = state.getRow();

        encodeHeaderOrFooter(state, "footer");

        UIComponent footer = table.getFacet("footer");
        if (footer != null && footer.isRendered()) {
            String elementId = table.getClientId(context) + ":tff";

            EncoderVariance encoderVariance = state.getEncoderVariance();
            encoderVariance.encodeStartUpdate(context, elementId);

            writer.startElement(HtmlConstants.DIV_ELEM, table);
            writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, elementId, null);
            writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE,
                HtmlUtil.concatClasses("rf-edt-tbl-ftr", table.getFooterClass()), null);
            footer.encodeAll(context);
            writer.endElement(HtmlConstants.DIV_ELEM);

            encoderVariance.encodeEndUpdate(context);
        }
    }

    @Override
    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractExtendedDataTable.class;
    }

    public void encodeMetaComponent(FacesContext context, UIComponent component, String metaComponentId) throws IOException {
        AbstractExtendedDataTable table = (AbstractExtendedDataTable) component;
        if (AbstractExtendedDataTable.SCROLL.equals(metaComponentId)) {
            final PartialResponseWriter writer = context.getPartialViewContext().getPartialResponseWriter();
            int clientFirst = table.getClientFirst();
            Integer oldClientFirst = (Integer) table.getAttributes().remove(AbstractExtendedDataTable.OLD_CLIENT_FIRST);
            if (oldClientFirst == null) {
                oldClientFirst = clientFirst;
            }
            int clientRows = ((SequenceRange) table.getComponentState().getRange()).getRows();
            int difference = clientFirst - oldClientFirst;
            SequenceRange addRange = null;
            SequenceRange removeRange = null;
            if (Math.abs(difference) >= clientRows) {
                difference = 0;
                addRange = new SequenceRange(clientFirst, clientRows);
                removeRange = new SequenceRange(oldClientFirst, clientRows);
            } else if (difference < 0) {
                clientFirst += table.getFirst();
                addRange = new SequenceRange(clientFirst, -difference);
                removeRange = new SequenceRange(clientFirst + clientRows, -difference);
            } else if (difference > 0) {
                oldClientFirst += table.getFirst();
                removeRange = new SequenceRange(oldClientFirst, difference);
                int last = oldClientFirst + clientRows;
                addRange = new SequenceRange(last, difference);
            }
            if (addRange != null) {
                Object key = table.getRowKey();
                table.captureOrigValue(context);
                table.setRowKey(context, null);
                final RendererState state = createRowHolder(context, table, null);
                state.setCurrentRow(addRange.getFirstRow());
                String clientId = table.getClientId(context);
                // TODO 1. Encode fixed children
                for (state.startIterate(); state.hasNextPart();) {
                    String partId = state.nextPart().getName().getId();
                    final List<String> ids = new LinkedList<String>();
                    table.walk(context, new DataVisitor() {
                        public DataVisitResult process(FacesContext context, Object rowKey, Object argument) {
                            UIDataTableBase dataTable = state.getRow();
                            dataTable.setRowKey(context, rowKey);
                            ids.add(dataTable.getContainerClientId(context) + ":" + state.getPart().getName().getId());
                            return DataVisitResult.CONTINUE;
                        }
                    }, removeRange, null);
                    table.walk(context, new DataVisitor() {
                        public DataVisitResult process(FacesContext context, Object rowKey, Object argument) {
                            UIDataTableBase dataTable = state.getRow();
                            dataTable.setRowKey(context, rowKey);
                            HashMap<String, String> attributes = new HashMap<String, String>(1);
                            String id = dataTable.getContainerClientId(context) + ":" + state.getPart().getName().getId();
                            attributes.put("id", id);
                            try {
                                writer.updateAttributes(ids.remove(0), attributes);
                                writer.startUpdate(id);
                                encodeRow(writer, context, state);
                                writer.endUpdate();
                            } catch (IOException e) {
                                throw new FacesException(e);
                            }
                            RowHolderBase holder = (RowHolderBase) argument;
                            holder.nextRow();
                            return DataVisitResult.CONTINUE;
                        }
                    }, addRange, state);
                    writer.startEval();
                    if (difference < 0) {
                        difference += clientRows;
                    }

                    // TODO nick - move this to external JavaScript file
                    writer.write("var richTBody = document.getElementById('" + clientId + ":tb" + partId + "');");
                    writer.write("var richRows = richTBody.rows;");
                    writer.write("for (var i = 0; i < " + difference
                        + "; i++ ) richTBody.appendChild(richTBody.removeChild(richRows[0]));");
                    writer.endEval();
                }
                writer.startUpdate(clientId + ":si");
                encodeSelectionInput(writer, context, component);
                writer.endUpdate();
                writer.startEval();
                writer.write("RichFaces.jQuery(" + ScriptUtils.toScript('#' + ScriptUtils.escapeCSSMetachars(clientId))
                    + ").triggerHandler('rich:onajaxcomplete', {first: " + table.getClientFirst() + "});");
                writer.endEval();
                table.setRowKey(context, key);
                table.restoreOrigValue(context);
            }
        } else {

            ResponseWriter initialWriter = context.getResponseWriter();
            assert !(initialWriter instanceof OnOffResponseWriter);

            try {
                context.setResponseWriter(new OnOffResponseWriter(initialWriter));

                RendererState state = createRowHolder(context, component, null);
                state.setEncoderVariance(EncoderVariance.partial);

                PartialResponseWriter writer = context.getPartialViewContext().getPartialResponseWriter();

                if (UIDataTableBase.HEADER.equals(metaComponentId)) {
                    encodeHeader(state);
                    writer.startEval();
                    writer.write("RichFaces.jQuery("
                        + ScriptUtils.toScript('#' + ScriptUtils.escapeCSSMetachars(table.getClientId(context)))
                        + ").triggerHandler('rich:onajaxcomplete', {reinitializeHeader: true});");
                    writer.endEval();
                } else if (UIDataTableBase.FOOTER.equals(metaComponentId)) {
                    encodeFooter(state);
                } else if (UIDataTableBase.BODY.equals(metaComponentId)) {
                    encodeBody(state);
                    String clientId = table.getClientId(context);
                    writer.startUpdate(clientId + ":si");
                    encodeSelectionInput(writer, context, component);
                    writer.endUpdate();
                    writer.startEval();
                    writer.write("RichFaces.jQuery(" + ScriptUtils.toScript('#' + ScriptUtils.escapeCSSMetachars(clientId))
                        + ").triggerHandler('rich:onajaxcomplete', {first: " + table.getClientFirst() + ", rowCount: "
                        + getRowCount(component) + ", reinitializeBody: true});");
                    writer.endEval();
                } else {
                    throw new IllegalArgumentException("Unsupported metaComponentIdentifier: " + metaComponentId);
                }
            } finally {
                context.setResponseWriter(initialWriter);
            }
        }
    }

    public void decodeMetaComponent(FacesContext context, UIComponent component, String metaComponentId) {
        throw new UnsupportedOperationException();
    }

    protected void doEncodeBegin(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        String savedTableState = (String) component.getAttributes().get("tableState");
        if (savedTableState != null && ! savedTableState.isEmpty()) { // retrieve table state
            ExtendedDataTableState tableState = new ExtendedDataTableState(savedTableState);
            consumeTableState(context, (UIDataTableBase) component, tableState);
        }

        Map<String, Object> attributes = component.getAttributes();
        writer.startElement(HtmlConstants.DIV_ELEM, component);
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, component.getClientId(context), null);
        writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE,
            HtmlUtil.concatClasses("rf-edt", (String) attributes.get("styleClass")), null);
        renderAttribute(context, HtmlConstants.STYLE_ATTRIBUTE, attributes.get("style"));
    }

    public RendererState createRowHolder(FacesContext context, UIComponent component, Object[] options) {
        return new RendererState(context, (UIDataTableBase) component);
    }

    protected void doEncodeChildren(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {

        UIDataTableBase table = (UIDataTableBase) component;
        Object key = table.getRowKey();
        table.captureOrigValue(context);
        table.setRowKey(context, null);
        RendererState state = createRowHolder(context, table, null);
        encodeStyle(state);
        encodeHeader(state);
        encodeBody(state);
        encodeFooter(state);
        table.setRowKey(context, key);
        table.restoreOrigValue(context);
    }

    protected void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        writer.startElement(HtmlConstants.TABLE_ELEMENT, component);
        String clientId = component.getClientId(context);
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, clientId + ":r", null);
        writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-edt-rord rf-edt-tbl", null);
        writer.startElement(HtmlConstants.TR_ELEMENT, component);
        writer.startElement(HtmlConstants.TH_ELEM, component);
        writer.write("&#160;");
        writer.endElement(HtmlConstants.TH_ELEM);
        writer.endElement(HtmlConstants.TR_ELEMENT);
        for (int i = 0; i < 6; i++) {
            writer.startElement(HtmlConstants.TR_ELEMENT, component);
            writer.startElement(HtmlConstants.TD_ELEM, component);
            writer.write("&#160;");
            writer.endElement(HtmlConstants.TD_ELEM);
            writer.endElement(HtmlConstants.TR_ELEMENT);
        }
        writer.endElement(HtmlConstants.TABLE_ELEMENT);
        writer.startElement(HtmlConstants.DIV_ELEM, component);
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, clientId + ":d", null);
        writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-edt-rsz-mkr", null);
        writer.endElement(HtmlConstants.DIV_ELEM);
        writer.startElement(HtmlConstants.DIV_ELEM, component);
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, clientId + ":rm", null);
        writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-edt-rord-mkr", null);
        writer.endElement(HtmlConstants.DIV_ELEM);
        writer.startElement(HtmlConstants.INPUT_ELEM, component);
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, clientId + ":wi", null);
        writer.writeAttribute(HtmlConstants.NAME_ATTRIBUTE, clientId + ":wi", null);
        writer.writeAttribute(HtmlConstants.TYPE_ATTR, HtmlConstants.INPUT_TYPE_HIDDEN, null);
        writer.endElement(HtmlConstants.INPUT_ELEM);
        writer.startElement(HtmlConstants.INPUT_ELEM, component);
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, clientId + ":cols", null);
        writer.writeAttribute(HtmlConstants.NAME_ATTRIBUTE, clientId + ":cols", null);
        writer.writeAttribute(HtmlConstants.VALUE_ATTRIBUTE, context.getExternalContext().getRequestParameterMap().get(clientId + ":cols"), null);
        writer.writeAttribute(HtmlConstants.TYPE_ATTR, HtmlConstants.INPUT_TYPE_HIDDEN, null);
        writer.endElement(HtmlConstants.INPUT_ELEM);
        writer.startElement(HtmlConstants.INPUT_ELEM, component);
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, clientId + ":scroll", null);
        writer.writeAttribute(HtmlConstants.NAME_ATTRIBUTE, clientId + ":scroll", null);
        writer.writeAttribute(HtmlConstants.VALUE_ATTRIBUTE, context.getExternalContext().getRequestParameterMap().get(clientId + ":scroll"), null);
        writer.writeAttribute(HtmlConstants.TYPE_ATTR, HtmlConstants.INPUT_TYPE_HIDDEN, null);
        writer.endElement(HtmlConstants.INPUT_ELEM);
        encodeSelectionInput(writer, context, component);
        AjaxFunction ajaxFunction = buildAjaxFunction(context, component);
        ajaxFunction.getOptions().setClientParameters(CLIENT_PARAMS);

        Map<String, Object> attributes = component.getAttributes();
        Map<String, Object> options = new HashMap<String, Object>();
        addToScriptHash(options, "selectionMode", attributes.get("selectionMode"), SelectionMode.multiple);
        addToScriptHash(options, "showColumnControl", attributes.get("showColumnControl"), false);
        addToScriptHash(options, "onbeforeselectionchange",
            RenderKitUtils.getAttributeAndBehaviorsValue(context, component, EVENT_ATTRIBUTES.get("onbeforeselectionchange")),
            null, ScriptHashVariableWrapper.eventHandler);
        addToScriptHash(options, "onselectionchange",
            RenderKitUtils.getAttributeAndBehaviorsValue(context, component, EVENT_ATTRIBUTES.get("onselectionchange")),
            null, ScriptHashVariableWrapper.eventHandler);
        addToScriptHash(options, "onready",
            RenderKitUtils.getAttributeAndBehaviorsValue(context, component, EVENT_ATTRIBUTES.get("onready")),
            null, ScriptHashVariableWrapper.eventHandler);
        StringBuilder builder = new StringBuilder("new RichFaces.ui.ExtendedDataTable('");
        builder.append(clientId).append("', ").append(getRowCount(component)).append(", function(event, clientParams) {")
            .append(ajaxFunction.toScript()).append(";}");
        if (!options.isEmpty()) {
            builder.append(",").append(ScriptUtils.toScript(options));
        }
        builder.append(");");
        getUtils().writeScript(context, component, builder.toString());
        writer.endElement(HtmlConstants.DIV_ELEM);
    }

    private int getRowCount(UIComponent component) {
        UIDataTableBase table = (UIDataTableBase) component;
        int rows = table.getRows();
        int rowCount = table.getRowCount() - table.getFirst();
        if (rows > 0) {
            rows = Math.min(rows, rowCount);
        } else {
            rows = rowCount;
        }

        return rows;
    }

    private void encodeStyle(RendererState state) throws IOException {
        FacesContext context = state.getContext();
        ResponseWriter writer = context.getResponseWriter();
        UIDataTableBase table = state.getRow();

        PartialViewContext pvc = context.getPartialViewContext();
        if (!pvc.isAjaxRequest()) {
            writer.startElement("style", table);
            writer.writeAttribute(HtmlConstants.TYPE_ATTR, "text/css", null);
            writer.writeText(getCSSText(context, table), null);
            writer.endElement("style");
        } else {
            writer.startElement(HtmlConstants.SCRIPT_ELEM, table);
            writer.writeAttribute(HtmlConstants.TYPE_ATTR, HtmlConstants.TEXT_JAVASCRIPT_TYPE, null);

            String cssText = getCSSText(context, table);
            JSFunction function = new JSFunction("RichFaces.utils.addCSSText", cssText, table.getClientId(context) + ":st");

            writer.writeText(function.toScript(), null);

            writer.endElement(HtmlConstants.SCRIPT_ELEM);
        }
    }

    protected String getCSSText(FacesContext context, UIDataTableBase table) throws IOException {
        StringBuilder sb = new StringBuilder();
        String tableLocator = "div.rf-edt[id=\"" + table.getClientId() + "\"]";

        sb.append("div.rf-edt-cnt { width: 100%; }"); // TODO getNormalizedId(context, state.getGrid())

        Iterator<UIComponent> columns = table.columns();
        while (columns.hasNext()) {
            UIComponent column = columns.next();
            String id = column.getId();
            if (id == null) {
                column.getClientId(context); // hack initialize id
                id = column.getId();
            }
            String width = getColumnWidth(column);
            sb.append(tableLocator);
            sb.append(" .rf-edt-c-" + id + " {"); // TODO getNormalizedId(context,
            sb.append("width: " + width + ";");
            sb.append("}");

            sb.append(tableLocator);
            sb.append(" .rf-edt-td-" + id + " {"); // TODO getNormalizedId(context,
            sb.append("width: " + width + ";");
            sb.append("}");
        }

        return sb.toString();
    }

    public void encodeRow(ResponseWriter writer, FacesContext facesContext, RowHolderBase rowHolder) throws IOException {
        RendererState state = (RendererState) rowHolder;
        AbstractExtendedDataTable table = (AbstractExtendedDataTable) state.getRow();
        writer.startElement(HtmlConstants.TR_ELEMENT, table);

        if (rowHolder.getRow() instanceof UIDataTableBase) {
            renderRowHandlers(facesContext, (UIDataTableBase) rowHolder.getRow());
        }

        String rowClass = getRowClass(rowHolder);

        StringBuilder builder = new StringBuilder();
        Collection<Object> selection = table.getSelection();
        if (selection != null && selection.contains(table.getRowKey())) {
            builder.append("rf-edt-r-sel");
        }
        if (table.getRowKey().equals(table.getAttributes().get("activeRowKey"))) {
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append("rf-edt-r-act");
        }
        if (table.getRowKey().equals(table.getAttributes().get("shiftRowKey"))) {
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append("rf-edt-r-sht");
        }
        rowClass = concatClasses(builder.toString(), rowClass);
        if (rowClass.length() > 0) {
            writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, rowClass, null);
        }
        Iterator<UIComponent> columns = null;
        Part part = state.getPart();
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, table.getContainerClientId(facesContext) + ":"
            + part.getName().getId(), null);
        columns = part.getColumns().iterator();
        int frozenColumns = (state.getPart().name == PartName.normal ? state.frozenColumnsSize : 0);
        int columnNumber = 0 + frozenColumns;
        int lastColumnNumber = part.getColumns().size() + frozenColumns - 1;
        while (columns.hasNext()) {
            UIComponent column = (UIComponent) columns.next();
            if (column.isRendered()) {
                writer.startElement(HtmlConstants.TD_ELEM, table);
                writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, column.getContainerClientId(facesContext), null);
                if (column instanceof AbstractColumn) {
                    writer.writeAttribute(DATA_ATTRIBUTE, ((AbstractColumn) column).getName(), null);
                }
                String columnClass = "";
                if (columnNumber != lastColumnNumber) {
                    columnClass = "rf-edt-td-" + column.getId();
                }
                columnClass = concatClasses(
                        columnClass,
                        getColumnClass(rowHolder, columnNumber),
                        column.getAttributes().get(HtmlConstants.STYLE_CLASS_ATTR));
                if (!"".equals(columnClass)) {
                    writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, columnClass, null);
                }

                String columnStyle = (String) column.getAttributes().get(HtmlConstants.STYLE_ATTRIBUTE);
                if (!"".equals(columnStyle)) {
                    writer.writeAttribute(HtmlConstants.STYLE_ATTRIBUTE, columnStyle, null);
                }

                columnNumber++;

                writer.startElement(HtmlConstants.DIV_ELEM, table);
                writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-edt-c rf-edt-c-" + column.getId(), null);
                writer.startElement(HtmlConstants.DIV_ELEM, column);
                writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-edt-c-cnt", null);
                renderChildren(facesContext, column);
                writer.endElement(HtmlConstants.DIV_ELEM);
                writer.endElement(HtmlConstants.DIV_ELEM);
                writer.endElement(HtmlConstants.TD_ELEM);
            }
        }
        writer.endElement(HtmlConstants.TR_ELEMENT);
    }

    @Override
    protected void doDecode(FacesContext context, final UIComponent component) {
        super.doDecode(context, component);
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String clientId = component.getClientId(context);
        updateWidthOfColumns(context, component, map.get(clientId + ":wi"));
        if (map.get(clientId) != null) {
            updateColumnsOrder(context, component, map.get("rich:columnsOrder"));
        }
        if (map.get(clientId) != null) {
            updateClientFirst(context, component, map.get("rich:clientFirst"));
        }
        decodeSortingFiltering(context, component);

        if (component.getAttributes().get("tableState") != null) {
            ExtendedDataTableState tableState = new ExtendedDataTableState((UIDataTableBase) component);
            updateAttribute(context, component, "tableState", tableState.toString());
        }

        if (component instanceof ClientBehaviorHolder) {
            final Map<String, List<ClientBehavior>> behaviors = ((ClientBehaviorHolder) component).getClientBehaviors();

            if (behaviors == null || behaviors.isEmpty()) {
                return;
            }

            Map<String, String> parametersMap = context.getExternalContext().getRequestParameterMap();
            final String behaviorEvent = parametersMap.get(BEHAVIOR_EVENT_NAME);

            if (behaviorEvent == null || !behaviorEvent.startsWith(ROW)) {
                return;
            }

            String behaviorSourceId = RenderKitUtils.getBehaviorSourceId(context);
            if (behaviorSourceId.endsWith(":n")) {
                behaviorSourceId = behaviorSourceId.substring(0, behaviorSourceId.length() - 2);
            } else {
                return;
            }

            ((UIDataAdaptor) component).invokeOnRow(context, behaviorSourceId, new ContextCallback() {
                public void invokeContextCallback(FacesContext context, UIComponent target) {
                    if (target.equals(component)) {
                        List<ClientBehavior> behaviorsForEvent = behaviors.get(behaviorEvent);
                        if (behaviorsForEvent != null && !behaviorsForEvent.isEmpty()) {
                            for (ClientBehavior behavior : behaviorsForEvent) {
                                behavior.decode(context, component);
                            }
                        }
                    }
                }
            });
        }
    }

    private void updateWidthOfColumns(FacesContext context, UIComponent component, String widthString) {
        if (widthString != null && widthString.length() > 0) {
            String[] widthArray = widthString.split(",");
            for (int i = 0; i < widthArray.length; i++) {
                String[] widthEntry = widthArray[i].split(":");
                UIComponent column = component.findComponent(widthEntry[0]);
                updateAttribute(context, column, "width", widthEntry[1]);
            }
        }
    }

    private void updateColumnsOrder(FacesContext context, UIComponent component, String columnsOrderString) {
        if (columnsOrderString != null && columnsOrderString.length() > 0) {
            String[] columnsOrder = columnsOrderString.split(",");
            updateAttribute(context, component, "columnsOrder", columnsOrder);
            context.getPartialViewContext().getRenderIds().add(component.getClientId(context)); // TODO Use partial re-rendering here.
        }
    }

    private void updateClientFirst(FacesContext context, UIComponent component, String clientFirst) {
        if (clientFirst != null && clientFirst.length() > 0) {
            Integer value = Integer.valueOf(clientFirst);
            Map<String, Object> attributes = component.getAttributes();
            if (!value.equals(attributes.get("clientFirst"))) {
                attributes.put(AbstractExtendedDataTable.SUBMITTED_CLIENT_FIRST, value);
                context.getPartialViewContext().getRenderIds()
                    .add(component.getClientId(context) + "@" + AbstractExtendedDataTable.SCROLL);
            }
        }
    }

    public void consumeTableState(FacesContext facesContext, UIDataTableBase table, ExtendedDataTableState tableState) {
        Iterator<UIComponent> columns = table.columns();

        // width, filter, sort
        while (columns.hasNext()) {
            UIComponent component = columns.next();
            if (component instanceof AbstractColumn) {
                AbstractColumn column = (AbstractColumn) component;

                String width = tableState.getColumnWidth(column);
                if (width != null && ! width.equals(column.getWidth())) {
                    updateAttribute(facesContext, column, "width", width);
                }

                String stateFilterValue = tableState.getColumnFilter(column);
                if ( stateFilterValue != null &&  (column.getFilterValue() == null || ! column.getFilterValue().toString().equals(stateFilterValue))) {
                        updateAttribute(facesContext, column, "filterValue", stateFilterValue);
                }

                String sort = tableState.getColumnSort(column);
                if (sort != null) {
                    SortOrder sortOrder = SortOrder.valueOf(sort);
                    if (! sortOrder.equals(column.getSortOrder())) {
                        updateAttribute(facesContext, column, "sortOrder", sortOrder);
                    }
                }
            }
        }

        //order
        String[] columnsOrder = tableState.getColumnsOrder();
        if (columnsOrder != null) {
            updateAttribute(facesContext, table, "columnsOrder", columnsOrder);
        }

    }


    /**
     * @deprecated TODO Remove this method when width in relative units in columns will be implemented.
     * @param column
     * @return width
     */
    private String getColumnWidth(UIComponent column) {
        String width = (String) column.getAttributes().get("width");
        if (width == null || width.length() == 0 || width.indexOf("%") != -1) {
            width = "100px";
        }
        return width;
    }


}

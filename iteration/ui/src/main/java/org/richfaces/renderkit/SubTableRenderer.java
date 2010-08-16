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
import org.ajax4jsf.renderkit.AjaxRendererUtils;
import org.ajax4jsf.renderkit.RendererUtils.HTML;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractDataTable;
import org.richfaces.component.AbstractSubTable;
import org.richfaces.component.Row;
import org.richfaces.component.UIDataTableBase;
import org.richfaces.event.ToggleEvent;

/**
 * @author Anton Belevich
 *
 */

@JsfRenderer(type = "org.richfaces.SubTableRenderer", family = AbstractSubTable.COMPONENT_FAMILY)
@ResourceDependencies({
    @ResourceDependency(name = "jquery.js"),
    @ResourceDependency(name = "richfaces.js"),
    @ResourceDependency(library="org.richfaces", name = "subtable.js")
})
public class SubTableRenderer extends AbstractTableRenderer {

    public static final String TB_ROW = ":c";
    
    private static final String STATE = ":state"; 
    
    private static final String OPTIONS = ":options";
    
    private static final String DISPLAY_NONE = "display: none;";
    
    private class SubTableHiddenEncodeStrategy implements EncodeStrategy {
       
        public void begin(ResponseWriter writer, FacesContext context, UIComponent component, Object[] params) throws IOException {
            AbstractSubTable subTable = (AbstractSubTable)component;
            writer.startElement(HTML.TR_ELEMENT, subTable);
            writer.writeAttribute(HTML.ID_ATTRIBUTE, subTable.getClientId(context) + HIDDEN_CONTAINER_ID, null);
            writer.writeAttribute(HTML.STYLE_ATTRIBUTE, DISPLAY_NONE, null);
            writer.startElement(HTML.TD_ELEM, subTable);
        }

        public void end(ResponseWriter writer, FacesContext context, UIComponent component, Object[] params) throws IOException {
            writer.endElement(HTML.TD_ELEM);
            writer.endElement(HTML.TR_ELEMENT);
            
            if(params != null && params.length ==1) {
                boolean endTbody = (Boolean)params[0];
                if(endTbody) {
                    encodeTableBodyEnd(writer);
                }
            }
        }
        
    };
        
    protected void doDecode(FacesContext facesContext, UIComponent component) {
        AbstractSubTable subTable = (AbstractSubTable)component;
       
        String clientId = subTable.getClientId(facesContext);
        Map<String, String> requestMap = facesContext.getExternalContext().getRequestParameterMap();

        String optionsId = clientId + OPTIONS;
        String togglerId = requestMap.get(optionsId);
                
        String stateId = clientId + STATE;
        String state = (String)requestMap.get(stateId);
                
        boolean isExpand = true; 
        if(state != null) {
            int newValue = Integer.parseInt(state);
            
            if(newValue < 1) {
                isExpand = false;
            } 
            
            if(subTable.isExpanded() != isExpand) {
                new ToggleEvent(subTable, isExpand, togglerId).queue();
            }
        }
    }
    
    @Override
    public void encodeFirstRowStart(ResponseWriter writer, FacesContext context, String parentId, int currentRow, UIComponent component) throws IOException {
        writer.startElement(HTML.TR_ELEMENT, component);
        writer.writeAttribute(HTML.ID_ATTRIBUTE, parentId + ":" + currentRow + ":b", null);
        String styleClass = getFirstRowClass(context, parentId);
        encodeStyleClass(writer, context, component, HTML.STYLE_CLASS_ATTR, styleClass);
    }
    
    @Override
    public void encodeRowStart(ResponseWriter writer, FacesContext context, String parentId, int currentRow, UIComponent component) throws IOException {
        writer.startElement(HTML.TR_ELEMENT, component);
        writer.writeAttribute(HTML.ID_ATTRIBUTE, parentId + ":" + currentRow + ":b", null);
        String styleClass = getRowClass(context, parentId);
        encodeStyleClass(writer, context, component, HTML.STYLE_CLASS_ATTR, styleClass);
    }
    
    public void encodeTableFacets(ResponseWriter writer, FacesContext context, UIDataTableBase dataTable) throws IOException {
        AbstractSubTable subTable = (AbstractSubTable)dataTable;

        encodeHeaderFacet(writer, context, subTable, false);
        
        String rowClass =  getRowSkinClass();
        String cellClass = getCellSkinClass();
        String firstClass = getFirstRowSkinClass();
        
        rowClass = mergeStyleClasses("rowClass", rowClass, subTable);
        cellClass = mergeStyleClasses("cellClass", cellClass, subTable);
        firstClass = mergeStyleClasses("firstRowClass", firstClass, subTable);
        
        saveRowStyles(context, subTable.getClientId(context), firstClass, rowClass, cellClass);
    }
    
    @Override
    public void encodeTableBodyStart(ResponseWriter writer, FacesContext facesContext, UIDataTableBase dataTableBase) throws IOException {
        AbstractSubTable subTable = (AbstractSubTable)dataTableBase;
        
        UIDataTableBase component = findParent(subTable);
        if(component instanceof AbstractDataTable) {
            writer.startElement(HTML.TBODY_ELEMENT, null);
            writer.writeAttribute(HTML.ID_ATTRIBUTE, component.getRelativeClientId(facesContext) + ":" + subTable.getId() + TB_ROW, null);
            writer.writeAttribute(HTML.CLASS_ATTRIBUTE, getTableSkinClass(), null);
            
            String predefinedStyles = !subTable.isExpanded() ?  DISPLAY_NONE : null;
            encodeStyle(writer, facesContext, subTable, predefinedStyles);
        }
    }
    
    @Override
    public void encodeBeforeRows(ResponseWriter writer, FacesContext facesContext, UIDataTableBase dataTableBase, boolean encodeParentTBody, boolean partialUpdate) throws IOException {
        AbstractSubTable subTable = (AbstractSubTable)dataTableBase;
        encodeTableBodyStart(writer, facesContext, subTable);
        encodeSubTableDomElement(writer, facesContext, subTable);
        setupTableStartElement(facesContext, subTable);
        encodeHeaderFacet(writer, facesContext, subTable, false);
    }

    private void encodeSubTableDomElement(ResponseWriter writer, FacesContext facesContext, AbstractSubTable subTable) throws IOException{
        writer.startElement(HTML.TR_ELEMENT, subTable);
        writer.writeAttribute(HTML.STYLE_ATTRIBUTE, DISPLAY_NONE, null);
        writer.writeAttribute(HTML.ID_ATTRIBUTE, subTable.getClientId(facesContext), null);
        writer.startElement(HTML.TD_ELEM, subTable);
        writer.endElement(HTML.TD_ELEM);
        writer.endElement(HTML.TR_ELEMENT);
    }
    
    public void encodeRow(ResponseWriter writer, FacesContext facesContext, RowHolderBase holder) throws IOException {
        RowHolder rowHolder = (RowHolder)holder;
        Row row = rowHolder.getRow();
            
        rowHolder.setRowStart(true);
        Iterator<UIComponent> components = row.columns();
        if (rowHolder.isUpdatePartial()) {
            partialStart(facesContext,((AbstractSubTable) row).getRelativeClientId(facesContext) + ":b");
        }
        
        while (components.hasNext()) {
            UIComponent component = components.next();
            
            if(component instanceof UIColumn) {
                encodeColumn(facesContext, writer, (UIColumn)component , rowHolder);
            
            } else if (component instanceof AbstractSubTable) {
                if(component.isRendered()) {
                    encodeRowEnd(writer);
                }
                
                component.encodeAll(facesContext);
                rowHolder.setRowStart(true);
            }
        }

        encodeRowEnd(writer);

        if (rowHolder.isUpdatePartial()) {
            partialEnd(facesContext);
        }
    }
    
    @Override
    public void encodeAfterRows(ResponseWriter writer, FacesContext facesContext, UIDataTableBase dataTableBase,
        boolean encodeParentTBody, boolean partialUpdate) throws IOException {
        AbstractSubTable subTable = (AbstractSubTable)dataTableBase;
        encodeFooterFacet(writer, facesContext, subTable, false);
    }

    @Override
    public boolean encodeParentTBody(UIDataTableBase dataTableBase) {
        UIDataTableBase parent = findParent((AbstractSubTable)dataTableBase);
        return (parent instanceof AbstractDataTable);
    }
    
    public void encodeHiddenInput(ResponseWriter writer, FacesContext facesContext, UIDataTableBase dataTableBase)
        throws IOException {
        AbstractSubTable subTable = (AbstractSubTable) dataTableBase;

        String stateId = subTable.getClientId(facesContext) + STATE;

        writer.startElement(HTML.INPUT_ELEM, subTable);
        writer.writeAttribute(HTML.ID_ATTRIBUTE, stateId, null);
        writer.writeAttribute(HTML.NAME_ATTRIBUTE, stateId, null);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_HIDDEN, null);
        
        int state = subTable.isExpanded() ? AbstractSubTable.EXPAND_STATE : AbstractSubTable.COLLAPSE_STATE;
        
        writer.writeAttribute(HTML.VALUE_ATTRIBUTE, state, null);
        writer.endElement(HTML.INPUT_ELEM);

        String optionsId = subTable.getClientId(facesContext) + OPTIONS;
        writer.startElement(HTML.INPUT_ELEM, subTable);
        writer.writeAttribute(HTML.ID_ATTRIBUTE, optionsId, null);
        writer.writeAttribute(HTML.NAME_ATTRIBUTE, optionsId, null);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_HIDDEN, null);
        writer.endElement(HTML.INPUT_ELEM);

    }
    
    public boolean containsThead() {
        return false;
    }

    public EncodeStrategy getHeaderEncodeStrategy(UIComponent column, String tableFacetName) {
        //TODO: anton -> use RichHeaderEncodeStrategy for our columns ???   
        return new SimpleHeaderEncodeStrategy();
        
    }

    public void encodeClientScript(ResponseWriter writer, FacesContext facesContext, UIDataTableBase component)
        throws IOException {
        AbstractSubTable subTable = (AbstractSubTable) component;

        String id = subTable.getClientId(facesContext);

        UIComponent nestingForm = getUtils().getNestingForm(facesContext, subTable);
        String formId = nestingForm != null ? nestingForm.getClientId(facesContext) : "";

        Map<String, Object> options = new HashMap<String, Object>();
        options.put("stateInput", subTable.getClientId(facesContext) + STATE);
        options.put("optionsInput", subTable.getClientId(facesContext) + OPTIONS);
        options.put("expandMode", subTable.getExpandMode());
        options.put("eventOptions", AjaxRendererUtils.buildEventOptions(facesContext, subTable));

        JSFunction jsFunction = new JSFunction("new RichFaces.ui.SubTable");
        jsFunction.addParameter(id);
        jsFunction.addParameter(formId);
        jsFunction.addParameter(options);

        writer.startElement(HTML.SCRIPT_ELEM, subTable);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.JAVASCRIPT_TYPE, null);
        writer.writeText(jsFunction.toScript(), null);
        writer.endElement(HTML.SCRIPT_ELEM);
    }
    
    public String getTableSkinClass() {
        return "rf-st";
    }
        
    public String getRowSkinClass() {
        return "rf-st-r";
    }

    public String getFirstRowSkinClass() {
        return "rf-st-f-r";
    }

    public String getHeaderRowSkinClass() {
        return "rf-st-h-r";
    }

    public String getHeaderFirstRowSkinClass() {
        return "rf-st-h-f-r";
    }

    public String getCellSkinClass() {
        return "rf-st-c";
    }
   
    public String getHeaderCellSkinClass() {
        return "rf-st-h-c";
    }

    public String getColumnHeaderCellSkinClass() {
        return "rf-st-sh-c";
    }

    public String getColumnHeaderSkinClass() {
        return "rf-st-sh";
    }

    public String getFooterSkinClass() {
        return "rf-st-f";
    }
    
    public String getFooterCellSkinClass() {
        return "rf-st-f-c";
    }

    public String getFooterFirstRowSkinClass() {
        return "rf-st-f-f";
    }

    public String getColumnFooterCellSkinClass() {
        return "rf-st-sf-c";
    }

    public String getColumnFooterSkinClass() {
        return "rf-st-sf";
    }
    
    public String getColumnFooterFirstSkinClass() {
        return "rf-st-sf-f";    
    }

    public String getColumnHeaderFirstSkinClass() {
        return "rf-st-sh-f";
    }

    public String getFooterFirstSkinClass() {
        return "rf-st-f-f";
    }

    public String getHeaderFirstSkinClass() {
        return "rf-st-h-f";
    }

    public String getHeaderSkinClass() {
        return "rf-st-h";
    }
    
    public String getNoDataClass() {
        return "rf-st-nd-c";
    }
    
    @Override
    public void encodeMetaComponent(FacesContext facesContext, UIComponent component, String metaComponentId)
        throws IOException {
        AbstractSubTable subTable = (AbstractSubTable)component;
        setupTableStartElement(facesContext, subTable);
        
        if(AbstractSubTable.BODY.equals(metaComponentId)) {
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
        return new SubTableHiddenEncodeStrategy();
    }

    protected void setupTableStartElement(FacesContext context, UIComponent component) {
        setupTableStartElement(context, component, HTML.TD_ELEM);
    }

    protected UIDataTableBase findParent(AbstractSubTable subTable) {
        UIComponent parent = subTable.getParent();

        while(parent != null && !(parent instanceof UIDataTableBase)) {
            parent = parent.getParent();
        }
        
        if(parent == null){
            //TODO: anton -> do we need this?
            throw new AbortProcessingException("UISubTable should be a child of UIDataTable or UISubTable");
        }
        return (UIDataTableBase)parent;
    }
}

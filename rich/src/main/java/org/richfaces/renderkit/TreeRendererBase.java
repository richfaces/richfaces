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

import static org.richfaces.component.AbstractTree.SELECTION_META_COMPONENT_ID;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.PartialViewContext;
import javax.faces.context.ResponseWriter;

import org.richfaces.component.AbstractTree;
import org.richfaces.component.AbstractTreeNode;
import org.richfaces.component.SwitchType;
import org.richfaces.context.ExtendedPartialViewContext;
import org.richfaces.event.TreeSelectionChangeEvent;
import org.richfaces.javascript.JSFunction;
import org.richfaces.javascript.JSReference;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.renderkit.util.AjaxRendererUtils;
import org.richfaces.ui.common.HtmlConstants;
import org.richfaces.ui.common.meta.MetaComponentRenderer;
import org.richfaces.ui.common.meta.MetaComponentResolver;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;

/**
 * @author Nick Belaevski
 *
 */
public abstract class TreeRendererBase extends RendererBase implements MetaComponentRenderer {
    static final Logger LOGGER = RichfacesLogger.RENDERKIT.getLogger();
    private static final JSReference PARAMS_JS_REF = new JSReference("params");
    private static final JSReference SOURCE_JS_REF = new JSReference("source");
    private static final JSReference COMPLETE_JS_REF = new JSReference("complete");
    private static final String SELECTION_STATE = "__SELECTION_STATE";

    /**
     * @author Nick Belaevski
     *
     */
    private final class RowKeyContextCallback implements ContextCallback {
        private Object rowKey;

        public void invokeContextCallback(FacesContext context, UIComponent target) {
            AbstractTreeNode treeNode = (AbstractTreeNode) target;
            rowKey = treeNode.findTreeComponent().getRowKey();
        }

        public Object getRowKey() {
            return rowKey;
        }
    }

    public void encodeTree(FacesContext context, UIComponent component) throws IOException {
        AbstractTree tree = (AbstractTree) component;

        new TreeEncoderFull(context, tree).encode();
    }

    protected String getAjaxSubmitFunction(FacesContext context, UIComponent component) {
        AbstractTree tree = (AbstractTree) component;

        if (getToggleTypeOrDefault(tree) != SwitchType.ajax && getSelectionTypeOrDefault(tree) != SwitchType.ajax) {
            return null;
        }

        AjaxFunction ajaxFunction = AjaxRendererUtils.buildAjaxFunction(context, component);
        ajaxFunction.setSource(SOURCE_JS_REF);

        AjaxOptions options = ajaxFunction.getOptions();

        options.set("complete", COMPLETE_JS_REF);
        options.setClientParameters(PARAMS_JS_REF);

        return ajaxFunction.toScript();
    }

    protected void encodeSelectionStateInput(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement(HtmlConstants.INPUT_ELEM, component);
        writer.writeAttribute(HtmlConstants.TYPE_ATTR, "hidden", null);
        String selectionStateInputId = getSelectionStateInputId(context, component);
        writer.writeAttribute(HtmlConstants.NAME_ATTRIBUTE, selectionStateInputId, null);
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, selectionStateInputId, null);
        writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-tr-sel-inp", null);

        String selectedNodeId = "";
        AbstractTree tree = (AbstractTree) component;

        Iterator<Object> selectedKeys = tree.getSelection().iterator();

        if (selectedKeys.hasNext()) {
            Object selectionKey = selectedKeys.next();
            Object initialKey = tree.getRowKey();
            try {
                tree.setRowKey(context, selectionKey);
                if (tree.isRowAvailable()) {
                    selectedNodeId = tree.findTreeNodeComponent().getClientId(context);
                }
            } finally {
                try {
                    tree.setRowKey(context, initialKey);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }

        if (selectedKeys.hasNext()) {
            // TODO - better message
            throw new IllegalArgumentException("Selection object should not contain more than one keys!");
        }

        writer.writeAttribute(HtmlConstants.VALUE_ATTRIBUTE, selectedNodeId, null);

        writer.endElement(HtmlConstants.INPUT_ELEM);
    }

    protected String getSelectionStateInputId(FacesContext context, UIComponent component) {
        return component.getClientId(context) + SELECTION_STATE;
    }

    protected SwitchType getSelectionType(FacesContext context, UIComponent component) {
        AbstractTree tree = (AbstractTree) component;

        SwitchType selectionType = getSelectionTypeOrDefault(tree);
        if (selectionType != SwitchType.ajax && selectionType != SwitchType.client) {
            // TODO - better message
            throw new IllegalArgumentException(String.valueOf(selectionType));
        }

        return selectionType;
    }

    public void encodeMetaComponent(FacesContext context, UIComponent component, String metaComponentId) throws IOException {

        if (SELECTION_META_COMPONENT_ID.equals(metaComponentId)) {
            PartialResponseWriter writer = context.getPartialViewContext().getPartialResponseWriter();

            writer.startUpdate(getSelectionStateInputId(context, component));
            encodeSelectionStateInput(context, component);
            writer.endUpdate();

            JSFunction function = new JSFunction("RichFaces.component", component.getClientId(context));

            ExtendedPartialViewContext partialContext = ExtendedPartialViewContext.getInstance(context);
            partialContext.appendOncomplete(function.toScript() + ".__updateSelectionFromInput();");
        } else {
            throw new IllegalArgumentException(metaComponentId);
        }

        // TODO Auto-generated method stub

    }

    public void decodeMetaComponent(FacesContext context, UIComponent component, String metaComponentId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void doDecode(FacesContext context, UIComponent component) {
        super.decode(context, component);

        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String selectedNode = map.get(getSelectionStateInputId(context, component));
        AbstractTree tree = (AbstractTree) component;

        Object selectionRowKey = null;

        if (!Strings.isNullOrEmpty(selectedNode)) {
            RowKeyContextCallback rowKeyContextCallback = new RowKeyContextCallback();
            tree.invokeOnComponent(context, selectedNode, rowKeyContextCallback);
            selectionRowKey = rowKeyContextCallback.getRowKey();
        }

        Collection<Object> selection = tree.getSelection();

        Collection<Object> newSelection = null;

        if (selectionRowKey == null) {
            if (!selection.isEmpty()) {
                newSelection = Collections.emptySet();
            }
        } else {
            if (!selection.contains(selectionRowKey)) {
                newSelection = Collections.singleton(selectionRowKey);
            }
        }

        if (newSelection != null) {
            new TreeSelectionChangeEvent(component, Sets.newHashSet(selection), newSelection).queue();
        }

        PartialViewContext pvc = context.getPartialViewContext();
        if (pvc.isAjaxRequest()) {
            pvc.getRenderIds().add(
                tree.getClientId(context) + MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR
                    + AbstractTree.SELECTION_META_COMPONENT_ID);
        }
    }

    protected void createTreeRenderingContext(FacesContext context, UIComponent component) {
        TreeRenderingContext.create(context, (AbstractTree) component);
    }

    protected Object getClientEventHandlers(FacesContext facesContext) {
        TreeRenderingContext treeRenderingContext = TreeRenderingContext.get(facesContext);
        return treeRenderingContext.getHandlers();
    }

    protected void deleteTreeRenderingContext(FacesContext context) {
        TreeRenderingContext.delete(context);
    }

    static SwitchType getSelectionTypeOrDefault(AbstractTree tree) {
        SwitchType selectionType = tree.getSelectionType();
        if (selectionType == null) {
            selectionType = SwitchType.client;
        }
        return selectionType;
    }

    static SwitchType getToggleTypeOrDefault(AbstractTree tree) {
        SwitchType toggleType = tree.getToggleType();
        if (toggleType == null) {
            toggleType = SwitchType.DEFAULT;
        }
        return toggleType;
    }
}

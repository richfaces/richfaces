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

import static org.richfaces.renderkit.RenderKitUtils.getFirstNonEmptyAttribute;
import static org.richfaces.renderkit.TreeRendererBase.getToggleTypeOrDefault;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.context.ResponseWriter;

import org.richfaces.component.AbstractTree;
import org.richfaces.component.AbstractTreeNode;
import org.richfaces.ui.common.HtmlConstants;
import org.richfaces.ui.common.meta.MetaComponentRenderer;
import org.richfaces.ui.common.meta.MetaComponentResolver;
import org.richfaces.component.SwitchType;
import org.richfaces.event.TreeToggleEvent;

import com.google.common.base.Strings;

/**
 * @author Nick Belaevski
 *
 */
public class TreeNodeRendererBase extends RendererBase implements MetaComponentRenderer {
    static final String AJAX_TOGGLED_NODE_ATTRIBUTE = TreeNodeRendererBase.class.getName() + ":AJAX_TOGGLED_NODE_ATTRIBUTE";
    static final String AJAX_TOGGLED_NODE_STATE_ATTRIBUTE = TreeNodeRendererBase.class.getName()
        + ":AJAX_TOGGLED_NODE_STATE_ATTRIBUTE";
    private static final String NEW_NODE_TOGGLE_STATE = "__NEW_NODE_TOGGLE_STATE";
    private static final String TRIGGER_NODE_AJAX_UPDATE = "__TRIGGER_NODE_AJAX_UPDATE";
    private static final String HANDLE_LOADING_FACET_NAME = "handleLoading";

    @Override
    public void decode(FacesContext context, UIComponent component) {
        super.decode(context, component);

        final Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String newToggleState = map.get(component.getClientId(context) + NEW_NODE_TOGGLE_STATE);
        if (newToggleState != null) {

            AbstractTreeNode treeNode = (AbstractTreeNode) component;

            boolean initialState = treeNode.isExpanded();
            boolean newState = Boolean.valueOf(newToggleState);
            if (initialState ^ newState) {
                new TreeToggleEvent(treeNode, newState).queue();
            }

            PartialViewContext pvc = context.getPartialViewContext();
            if (pvc.isAjaxRequest() && map.get(component.getClientId(context) + TRIGGER_NODE_AJAX_UPDATE) != null) {
                pvc.getRenderIds().add(
                    component.getClientId(context) + MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR
                        + AbstractTreeNode.SUBTREE_META_COMPONENT_ID);

                context.getAttributes().put(AJAX_TOGGLED_NODE_ATTRIBUTE, component.getClientId(context));
                context.getAttributes().put(AJAX_TOGGLED_NODE_STATE_ATTRIBUTE,
                    initialState ? TreeNodeState.expanded : TreeNodeState.collapsed);
            }
        }
    }

    public void decodeMetaComponent(FacesContext context, UIComponent component, String metaComponentId) {
        throw new UnsupportedOperationException();
    }

    public void encodeMetaComponent(FacesContext context, UIComponent component, String metaComponentId) throws IOException {

        if (AbstractTreeNode.SUBTREE_META_COMPONENT_ID.equals(metaComponentId)) {
            AbstractTreeNode treeNode = (AbstractTreeNode) component;
            new TreeEncoderPartial(context, treeNode).encode();
        } else {
            throw new IllegalArgumentException(metaComponentId);
        }
    }

    protected TreeNodeState getNodeState(FacesContext context) {
        return (TreeNodeState) context.getAttributes().get(TreeEncoderBase.TREE_NODE_STATE_ATTRIBUTE);
    }

    protected UIComponent getTreeComponent(UIComponent treeNodeComponent) {
        return ((AbstractTreeNode) treeNodeComponent).findTreeComponent();
    }

    protected void encodeDefaultIcon(FacesContext context, UIComponent component, String styleClass) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement(HtmlConstants.SPAN_ELEM, component);
        writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, styleClass, null);
        writer.endElement(HtmlConstants.SPAN_ELEM);
    }

    protected void encodeCustomIcon(FacesContext context, UIComponent component, String styleClass, String iconSource)
        throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement(HtmlConstants.IMG_ELEMENT, component);
        writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, styleClass, null);
        writer.writeAttribute(HtmlConstants.ALT_ATTRIBUTE, "", null);
        writer.writeURIAttribute(HtmlConstants.SRC_ATTRIBUTE, RenderKitUtils.getResourceURL(iconSource, context), null);
        writer.endElement(HtmlConstants.IMG_ELEMENT);
    }

    protected void encodeIcon(FacesContext context, UIComponent component) throws IOException {
        TreeNodeState nodeState = getNodeState(context);

        AbstractTreeNode treeNode = (AbstractTreeNode) component;

        AbstractTree tree = treeNode.findTreeComponent();

        if (nodeState.isLeaf()) {
            String iconLeaf = (String) getFirstNonEmptyAttribute("iconLeaf", treeNode, tree);
            encodeIconForNodeState(context, tree, treeNode, nodeState, iconLeaf);
        } else {
            String iconExpanded = (String) getFirstNonEmptyAttribute("iconExpanded", treeNode, tree);
            String iconCollapsed = (String) getFirstNonEmptyAttribute("iconCollapsed", treeNode, tree);

            if (Strings.isNullOrEmpty(iconCollapsed) && Strings.isNullOrEmpty(iconExpanded)) {
                encodeIconForNodeState(context, tree, treeNode, nodeState, null);
            } else {
                SwitchType toggleType = getToggleTypeOrDefault(treeNode.findTreeComponent());

                if (toggleType == SwitchType.client || nodeState == TreeNodeState.collapsed) {
                    encodeIconForNodeState(context, tree, treeNode, TreeNodeState.collapsed, iconCollapsed);
                }

                if (toggleType == SwitchType.client || nodeState == TreeNodeState.expanded
                    || nodeState == TreeNodeState.expandedNoChildren) {
                    encodeIconForNodeState(context, tree, treeNode, TreeNodeState.expanded, iconExpanded);
                }
            }
        }
    }

    protected void encodeIconForNodeState(FacesContext context, AbstractTree tree, AbstractTreeNode treeNode,
        TreeNodeState nodeState, String customIcon) throws IOException {
        if (Strings.isNullOrEmpty(customIcon)) {
            encodeDefaultIcon(
                context,
                treeNode,
                concatClasses(nodeState.getIconClass(), treeNode.getAttributes().get("iconClass"),
                    tree.getAttributes().get("iconClass")));
        } else {
            encodeCustomIcon(
                context,
                treeNode,
                concatClasses(nodeState.getCustomIconClass(), treeNode.getAttributes().get("iconClass"), tree.getAttributes()
                    .get("iconClass")), customIcon);
        }
    }

    protected void addClientEventHandlers(FacesContext facesContext, UIComponent component) {
        AbstractTreeNode treeNode = (AbstractTreeNode) component;

        // TODO check node state
        // TODO check toggle/selection types
        TreeRenderingContext renderingContext = TreeRenderingContext.get(facesContext);
        renderingContext.addHandlers(treeNode);
    }

    protected UIComponent getHandleLoadingFacetIfApplicable(UIComponent component) {
        AbstractTreeNode treeNode = (AbstractTreeNode) component;

        AbstractTree tree = treeNode.findTreeComponent();

        if (getToggleTypeOrDefault(tree) != SwitchType.ajax) {
            return null;
        }

        UIComponent facet = treeNode.getFacet(HANDLE_LOADING_FACET_NAME);
        if (facet == null) {
            facet = tree.getFacet(HANDLE_LOADING_FACET_NAME);
        }

        if (facet != null && facet.isRendered()) {
            return facet;
        }

        return null;
    }
}

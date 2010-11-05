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

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;

import org.richfaces.component.AbstractTreeNode;
import org.richfaces.component.MetaComponentResolver;
import org.richfaces.event.TreeToggleEvent;

/**
 * @author Nick Belaevski
 * 
 */
public class TreeNodeRendererBase extends RendererBase implements MetaComponentRenderer {

    private static final String NEW_NODE_TOGGLE_STATE = "__NEW_NODE_TOGGLE_STATE";
    
    private static final String TRIGGER_NODE_AJAX_UPDATE = "__TRIGGER_NODE_AJAX_UPDATE";
    
    @Override
    public void decode(FacesContext context, UIComponent component) {
        super.decode(context, component);

        final Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String newToggleState = map.get(component.getClientId(context) + NEW_NODE_TOGGLE_STATE);
        if (newToggleState != null) {

            AbstractTreeNode treeNode = (AbstractTreeNode) component;
            
            boolean expanded = Boolean.valueOf(newToggleState);
            if (treeNode.isExpanded() ^ expanded) {
                new TreeToggleEvent(treeNode, expanded).queue();
            }

            PartialViewContext pvc = context.getPartialViewContext();
            if (pvc.isAjaxRequest() && map.get(component.getClientId(context) + TRIGGER_NODE_AJAX_UPDATE) != null) {
                pvc.getRenderIds().add(treeNode.getClientId(context) + MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR + AbstractTreeNode.SUBTREE_META_COMPONENT_ID);
            }
        }
    }
    
    public void decodeMetaComponent(FacesContext context, UIComponent component, String metaComponentId) {
        throw new UnsupportedOperationException();
    }
    
    public void encodeMetaComponent(FacesContext context, UIComponent component, String metaComponentId)
        throws IOException {

        if (AbstractTreeNode.SUBTREE_META_COMPONENT_ID.equals(metaComponentId)) {
            AbstractTreeNode treeNode = (AbstractTreeNode) component;
            new TreeEncoderPartial(context, treeNode).encode();
        } else {
            throw new IllegalArgumentException(metaComponentId);
        }

    }
}

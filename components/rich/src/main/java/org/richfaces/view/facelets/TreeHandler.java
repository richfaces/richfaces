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
package org.richfaces.view.facelets;

import static org.richfaces.component.AbstractTree.DEFAULT_TREE_NODE_FACET_NAME;
import static org.richfaces.component.AbstractTree.DEFAULT_TREE_NODE_ID;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.MetaRuleset;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.TagAttribute;

import org.richfaces.component.AbstractTree;
import org.richfaces.component.AbstractTreeNode;

import com.google.common.base.Strings;

/**
 * @author Nick Belaevski
 *
 */
public class TreeHandler extends ComponentHandler {
    private static final MetaRule RULE = new MetaRule() {
        @Override
        public Metadata applyRule(String name, TagAttribute attribute, MetadataTarget meta) {
            if (meta.isTargetInstanceOf(AbstractTree.class)) {
                if ("selectionChangeListener".equals(name)) {
                    return new TreeSelectionChangeListenerExpressionMetadata(attribute);
                } else if ("toggleListener".equals(name)) {
                    return new TreeToggleListenerExpressionMetadata(attribute);
                }
            }
            return null;
        }
    };

    public TreeHandler(ComponentConfig config) {
        super(config);
    }

    @Override
    protected MetaRuleset createMetaRuleset(Class type) {
        MetaRuleset metaRuleset = super.createMetaRuleset(type);
        metaRuleset.addRule(RULE);
        metaRuleset.addRule(RowKeyConverterRule.INSTANCE);
        return metaRuleset;
    }

    @Override
    public void onComponentPopulated(FaceletContext ctx, UIComponent c, UIComponent parent) {
        super.onComponentPopulated(ctx, c, parent);

        UIComponent defaultTreeNode = c.getFacet(DEFAULT_TREE_NODE_FACET_NAME);
        if (defaultTreeNode == null) {
            defaultTreeNode = c.getFacet(DEFAULT_TREE_NODE_ID); // backwards compatibility
        }
        if (defaultTreeNode == null) {
            String var = ((AbstractTree) c).getVar();

            if (Strings.isNullOrEmpty(var)) {
                return;
            }

            FacesContext facesContext = ctx.getFacesContext();
            Application application = facesContext.getApplication();

            AbstractTreeNode treeNode = (AbstractTreeNode) application.createComponent(AbstractTreeNode.COMPONENT_TYPE);
            treeNode.setId(DEFAULT_TREE_NODE_ID);

            c.getFacets().put(DEFAULT_TREE_NODE_FACET_NAME, treeNode);

            UIComponent text = application.createComponent(HtmlOutputText.COMPONENT_TYPE);

            text.setValueExpression(
                "value",
                application.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{" + var + "}",
                    String.class));
            treeNode.getChildren().add(text);
        }
    }
}

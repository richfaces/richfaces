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
package org.richfaces.component;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.view.facelets.TreeModelAdaptorHandler;

/**
 * The <rich:treeModelAdaptor> component takes an object which implements the Map or Iterable interfaces. It adds all the object entries to the parent node as child nodes.
 *
 * @author Nick Belaevski
 */
@JsfComponent(type = UITreeModelAdaptor.COMPONENT_TYPE, family = UITreeModelAdaptor.COMPONENT_FAMILY, tag = @Tag(name = "treeModelAdaptor", handlerClass = TreeModelAdaptorHandler.class), attributes = "tree-model-props.xml")
public class UITreeModelAdaptor extends AbstractTreeModelAdaptor implements TreeModelAdaptor {
    public static final String COMPONENT_TYPE = "org.richfaces.TreeModelAdaptor";
    public static final String COMPONENT_FAMILY = "org.richfaces.TreeModelAdaptor";

    private enum PropertyKeys {
        nodes,
        leaf
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Attribute
    public Object getNodes() {
        Object nodes = getStateHelper().eval(PropertyKeys.nodes);

        memoizeDefaultRowKeyConverter(nodes);

        return nodes;
    }

    public void setNodes(Object nodes) {
        getStateHelper().put(PropertyKeys.nodes, nodes);
    }

    @Attribute
    public boolean isLeaf() {
        return (Boolean) getStateHelper().eval(PropertyKeys.leaf, false);
    }

    public void setLeaf(boolean leaf) {
        getStateHelper().put(PropertyKeys.leaf, leaf);
    }
}
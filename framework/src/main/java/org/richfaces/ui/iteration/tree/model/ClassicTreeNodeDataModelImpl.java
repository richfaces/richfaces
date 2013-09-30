/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.ui.iteration.tree.model;

import java.util.Iterator;

import javax.faces.convert.Converter;

import org.richfaces.model.TreeDataModelTuple;
import org.richfaces.model.TreeNode;
import org.richfaces.ui.iteration.tree.convert.StringSequenceRowKeyConverter;

/**
 * @author Nick Belaevski
 *
 */
public class ClassicTreeNodeDataModelImpl extends NodesTreeSequenceKeyModel<TreeNode> {
    private static final Converter DEFAULT_CONVERTER = new StringSequenceRowKeyConverter();

    public boolean isLeaf() {
        return getData().isLeaf();
    }

    public Iterator<TreeDataModelTuple> children() {
        return new ClassicTreeNodeTuplesIterator(getData(), getRowKey());
    }

    @Override
    protected TreeNode setupChildContext(Object segment) {
        return getData().getChild(segment);
    }

    @Override
    public Object getWrappedData() {
        return getRootNode();
    }

    @Override
    public void setWrappedData(Object data) {
        setRootNode((TreeNode) data);
    }

    public Converter getRowKeyConverter() {
        return DEFAULT_CONVERTER;
    }
}

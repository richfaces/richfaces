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
package org.richfaces.model;

/**
 * @author Nick Belaevski
 *
 */
public abstract class NodesTreeSequenceKeyModel<V> extends TreeSequenceKeyModel<V> {
    private V rootNode;

    protected V getRootNode() {
        return rootNode;
    }

    protected void setRootNode(V rootNode) {
        this.rootNode = rootNode;
    }

    protected void setupKey(SequenceRowKey key) {
        setRowKeyAndData(null, rootNode);

        if (key != null) {
            V data = getRootNode();

            for (Object simpleKey : key.getSimpleKeys()) {
                data = setupChildContext(simpleKey);

                if (data == null) {
                    break;
                }

                setData(data);
            }

            setRowKeyAndData(key, data);
        }
    }

    protected abstract V setupChildContext(Object segment);

    public TreeDataModelTuple createSnapshot() {
        return new TreeDataModelTuple(getRowKey(), getData());
    }

    public void restoreFromSnapshot(TreeDataModelTuple tuple) {
        setRowKeyAndData((SequenceRowKey) tuple.getRowKey(), (V) tuple.getData());
    }
}

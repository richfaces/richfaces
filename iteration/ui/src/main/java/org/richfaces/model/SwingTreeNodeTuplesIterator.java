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

import java.util.Iterator;

import javax.swing.tree.TreeNode;

final class SwingTreeNodeTuplesIterator implements Iterator<TreeDataModelTuple> {

    private SequenceRowKey<Integer> baseKey;
    
    private Iterator<TreeNode> children;
    
    private int counter = 0;

    SwingTreeNodeTuplesIterator(SequenceRowKey<Integer> baseKey, Iterator<TreeNode> children) {
        this.baseKey = baseKey;
        this.children = children;
    }

    private int getNextCounterValue() {
        return counter++;
    }
    
    public boolean hasNext() {
        return children.hasNext();
    }
    
    public TreeDataModelTuple next() {
        TreeNode node = children.next();
        
        SequenceRowKey<Integer> key;
        
        if (baseKey != null) {
            key = baseKey.append(getNextCounterValue());
        } else {
            key = new SequenceRowKey<Integer>(getNextCounterValue());
        }
        
        return new TreeDataModelTuple(key, node);
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
    
}
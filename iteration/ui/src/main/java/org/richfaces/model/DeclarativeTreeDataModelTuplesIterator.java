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

import javax.faces.component.UIComponent;

final class DeclarativeTreeDataModelTuplesIterator implements Iterator<TreeDataModelTuple> {
    
    private UIComponent component;

    private SequenceRowKey<DeclarativeModelKey> baseKey;
    
    private int counter = 0;
    
    private Iterator<?> dataIterator;
    
    public DeclarativeTreeDataModelTuplesIterator(UIComponent component, SequenceRowKey<DeclarativeModelKey> baseKey, Iterator<?> dataIterator) {
        super();
        this.component = component;
        this.baseKey = baseKey;
        this.dataIterator = dataIterator;
    }

    public TreeDataModelTuple next() {
        Object nextNode = dataIterator.next();
        DeclarativeModelKey key = new DeclarativeModelKey(component.getId(), counter++);
        
        SequenceRowKey<DeclarativeModelKey> newKey = baseKey.append(key);
        
        return new DeclarativeTreeDataModelTuple(newKey, nextNode, component);
    }
    
    public boolean hasNext() {
        return dataIterator.hasNext();
    }
    
    public void remove() {
        throw new UnsupportedOperationException();
    }
    
}
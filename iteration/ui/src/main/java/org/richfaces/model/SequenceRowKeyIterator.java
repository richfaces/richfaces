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

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

/**
 * @author Nick Belaevski
 * 
 */
public abstract class SequenceRowKeyIterator<K, T> implements Iterator<Object> {

    private SequenceRowKey<K> baseKey;
    
    private Iterator<T> itr;

    private T element;

    private SequenceRowKey<K> elementKey;

    private T baseElement;
    
    public SequenceRowKeyIterator(SequenceRowKey<K> baseKey, T baseElement, Iterator<T> itr) {
        super();
        this.baseKey = baseKey;
        this.baseElement = baseElement;
        this.itr = itr;
    }

    public boolean hasNext() {
        return itr.hasNext();
    }

    protected abstract K nextKey();
    
    public Object next() {
        element = itr.next();
        
        if (baseKey != null) {
            elementKey = baseKey.append(nextKey());
        } else {
            elementKey = new SequenceRowKey<K>(nextKey());
        }
        
        return elementKey;
    }
    
    public T getElement() {
        return element;
    }

    public SequenceRowKey<K> getBaseKey() {
        return baseKey;
    }
    
    public T getBaseElement() {
        return baseElement;
    }
    
    public SequenceRowKey<K> getElementKey() {
        return elementKey;
    }
    
    public void remove() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String toString() {
        ToStringHelper helper = Objects.toStringHelper(this);
        
        helper.add("element", element).add("elementKey", elementKey);
        
        return helper.toString();
    }
}

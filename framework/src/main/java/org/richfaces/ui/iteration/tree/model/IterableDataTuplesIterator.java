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

import javax.faces.component.UIComponent;

import org.richfaces.model.SequenceRowKey;

/**
 * @author Nick Belaevski
 *
 */
public class IterableDataTuplesIterator extends BaseTupleIterator {
    private Iterator<?> iterator;
    private int counter = 0;

    public IterableDataTuplesIterator(SequenceRowKey baseKey, Iterator<?> children) {
        this(baseKey, children, null);
    }

    public IterableDataTuplesIterator(SequenceRowKey baseKey, Iterator<?> children, UIComponent component) {
        super(baseKey, component);
        this.iterator = children;
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    protected void proceedToNext() {
        setKeyAndData(getNextCounterValue(), iterator.next());
    }

    private int getNextCounterValue() {
        return counter++;
    }
}

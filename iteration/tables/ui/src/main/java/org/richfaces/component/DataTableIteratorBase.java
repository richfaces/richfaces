/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Anton Belevich
 * @author Nick Belaevski
 */
public abstract class DataTableIteratorBase<E> implements Iterator<E> {

    private boolean isCompleted = false;

    private E next = null;

    private void setupNext() {
        if (!isCompleted) {
            if (next == null) {
                next = nextItem();

                if (next == null) {
                    isCompleted = true;
                }
            }
        }
    }

    public boolean hasNext() {
        setupNext();

        return next != null;
    }

    public E next() {
        setupNext();

        if (next == null) {
            throw new NoSuchElementException();
        }

        E result = next;
        next = null;
        return result;
    }

    public void remove() {
        throw new UnsupportedOperationException("Iterator is read-only");
    }

    protected abstract E nextItem();
}

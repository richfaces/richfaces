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
package org.richfaces.demo.iteration;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.richfaces.log.LogFactory;
import org.richfaces.log.Logger;

import com.google.common.collect.ForwardingIterator;
import com.google.common.collect.ForwardingSet;

/**
 * @author Nick Belaevski
 *
 */
public class TracingSet<E> extends ForwardingSet<E> implements Serializable {
    private static final long serialVersionUID = 267329344963751893L;
    private static final Logger LOGGER = LogFactory.getLogger(TracingSet.class);

    private class TracingIterator extends ForwardingIterator<E> {
        private final Iterator<E> itr = backingCollection.iterator();
        private E lastObject;

        @Override
        protected Iterator<E> delegate() {
            return itr;
        }

        @Override
        public E next() {
            try {
                lastObject = super.next();
                return lastObject;
            } catch (NoSuchElementException e) {
                lastObject = null;
                throw e;
            }
        }

        @Override
        public void remove() {
            LOGGER.info("TracingSet.TracingIterator.remove() " + lastObject);
            super.remove();
        }
    }

    private Set<E> backingCollection = new HashSet<E>();

    @Override
    protected Set<E> delegate() {
        return backingCollection;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        LOGGER.info("TracingSet.removeAll() " + collection);
        return super.removeAll(collection);
    }

    @Override
    public boolean add(E element) {
        LOGGER.info("TracingSet.add() " + element);
        return super.add(element);
    }

    @Override
    public boolean remove(Object object) {
        LOGGER.info("TracingSet.remove() " + object);
        return super.remove(object);
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        LOGGER.info("TracingSet.addAll() " + collection);
        return super.addAll(collection);
    }

    @Override
    public void clear() {
        LOGGER.info("TracingSet.clear()");
        super.clear();
    }

    @Override
    public Iterator<E> iterator() {
        return new TracingIterator();
    }
}

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.richfaces.renderkit;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.servlet.jsp.jstl.core.LoopTagStatus;

/**
 * <p>
 * Provides implementation of {@link Iterable} object wrapping {@link Collection}, {@link Iterable}, array or {@link Iterator}.
 * </p>
 *
 * <p>
 * Provides the status of iteration compatible with {@link LoopTagStatus}.
 * </p>
 *
 * @author Lukas Fryc
 */
public class ForEachLoop<T> implements Iterable<T> {

    private StateAwareIterator stateAwareIterator;
    private Status status;

    private boolean iterationStarted = false;
    /**
     * the current stateAwareIterator
     */
    private T currentElement;
    private int count = 1;
    private Integer index;
    private Integer step;
    private Integer begin;
    private Integer end;

    /**
     * the current element of last call to iterator.next() of original provided iterator
     */
    private T element;

    /**
     * the next item which will be returned when calling stateAwareIterator.next()
     *
     * used as cache for determining if the original provided iterator has next element available
     */
    private T nextElement;
    /**
     * determines if we looked for next time - in that case, nextElement contains next element to return
     */
    private boolean foundNext = false;

    private ForEachLoop(Iterator<T> iterator) {
        this.stateAwareIterator = new StateAwareIterator(iterator);
        this.status = new Status();
    }

    public static <T> ForEachLoop<T> getInstance(Iterator<T> iterator) {
        return new ForEachLoop<T>(iterator);
    }

    public static <T> ForEachLoop<T> getInstance(Iterable<T> iterable) {
        ForEachLoop<T> loop = new ForEachLoop<T>(iterable.iterator());
        return loop;
    }

    public static <T> ForEachLoop<T> getInstance(T[] array) {
        List<T> arrayList = Arrays.asList(array);
        return ForEachLoop.getInstance(arrayList);
    }

    @Override
    public Iterator<T> iterator() {
        return stateAwareIterator;
    }

    public Status getStatus() {
        return status;
    }

    /**
     * Wraps {@link Iterator} instance in order to allow setup properties begin, end and step of iteration.
     */
    public class StateAwareIterator implements Iterator<T> {

        private Iterator<T> iterator;

        public StateAwareIterator(Iterator<T> iterator) {
            this.iterator = iterator;
        }

        /**
         * Asks underlying iterator if there is next element available.
         *
         * This method can call method {@link Iterator#next()} for underlying iterator in order to determine if there is next
         * element (especially when there are begin or step properties setup).
         */
        @Override
        public boolean hasNext() {
            try {
                nextElement = next();
                if (end != null && index > end) {
                    foundNext = false;
                    return false;
                }
                foundNext = true;
                return true;
            } catch (NoSuchElementException e) {
                return false;
            }
        }

        /**
         * Provides next element of iteration given by begin, end and step properties
         */
        @Override
        public T next() {
            if (foundNext) {
                foundNext = false;
                currentElement = nextElement;
                return nextElement;
            }

            if (!iterationStarted) {
                iterateToBegin();
                iterationStarted = true;
            } else {
                iterateByStep();
            }
            currentElement = element;
            return currentElement;
        }

        /**
         * Iterates to first element provided by underlying iterator based on given begin property.
         */
        private void iterateToBegin() {
            int numberOfAdditionalIterations = (begin == null) ? 0 : begin;
            element = iterator.next();
            index = 0;
            for (int i = 0; i < numberOfAdditionalIterations; i++) {
                element = iterator.next();
                index += 1;
            }
        }

        /**
         * Iterates by the step of elements to next element
         */
        private void iterateByStep() {
            int realStep = (step == null) ? 1 : step;
            for (int i = 0; i < realStep; i++) {
                element = iterator.next();
                index += 1;
            }
            count += 1;
        }

        @Override
        public void remove() {
            iterator.remove();
        }
    }

    /**
     * Provides the functionality of {@link LoopTagStatus} for state of iteration.
     */
    public class Status implements LoopTagStatus {

        @Override
        public Object getCurrent() {
            return currentElement;
        }

        @Override
        public int getIndex() {
            return index;
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public boolean isFirst() {
            return count == 1;
        }

        @Override
        public boolean isLast() {
            return !stateAwareIterator.hasNext();
        }

        @Override
        public Integer getBegin() {
            return begin;
        }

        @Override
        public Integer getEnd() {
            return end;
        }

        @Override
        public Integer getStep() {
            return step;
        }
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public void setStep(int step) {
        this.step = step;
    }
}

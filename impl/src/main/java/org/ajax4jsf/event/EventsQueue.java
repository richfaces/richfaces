/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package org.ajax4jsf.event;

import javax.faces.event.FacesEvent;
import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Very simple implementation of FIFO buffer, to organize JSF events queue.
 *
 * @author asmirnov
 */
public class EventsQueue extends AbstractQueue<FacesEvent> {
    private int size = 0;
    private QueueElement first;
    private QueueElement last;

    /**
     * Remove and return first queued event.
     *
     * @return faces event form top of queue
     * @throws NoSuchElementException , if queue is empty.
     */
    public FacesEvent poll() {
        FacesEvent element = null;

        if (!isEmpty()) {
            element = first.getElement();
            first = first.getPrevious();

            if (null == first) {
                last = null;
                size = 0;
            } else {
                size--;
            }
        }

        return element;
    }

    public FacesEvent peek() {
        FacesEvent element = null;

        if (!isEmpty()) {
            element = first.getElement();
        }

        return element;
    }

    /**
     * Add event to queue.
     *
     * @param element
     * @return TODO
     */
    public boolean offer(FacesEvent element) {
        QueueElement queueElement = new QueueElement(element);

        if (isEmpty()) {
            first = queueElement;
            last = queueElement;
        } else {
            last.setPrevious(queueElement);
            last = queueElement;
        }

        size++;

        return true;
    }

    @Override
    public void clear() {
        size = 0;
        first = null;
        last = null;
    }

    @Override
    public boolean isEmpty() {
        return null == first;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<FacesEvent> iterator() {
        return new Iterator<FacesEvent>() {
            private QueueElement current = first;

            public boolean hasNext() {
                return null != current;
            }

            public FacesEvent next() {
                if (null == current) {
                    throw new NoSuchElementException();
                }

                FacesEvent event = current.getElement();

                current = current.getPrevious();

                return event;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    private static class QueueElement {
        private FacesEvent element;
        private QueueElement previous;

        public QueueElement(FacesEvent element) {
            this.element = element;
        }

        /**
         * @param previous the previous to set
         */
        public void setPrevious(QueueElement previsious) {
            this.previous = previsious;
        }

        /**
         * @return the previous
         */
        public QueueElement getPrevious() {
            return previous;
        }

        /**
         * @return the element
         */
        public FacesEvent getElement() {
            return element;
        }
    }
}

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

import java.util.NoSuchElementException;

import javax.faces.component.UICommand;
import javax.faces.event.ActionEvent;

import junit.framework.TestCase;

/**
 * @author asmirnov
 *
 */
public class EventsQueueTest extends TestCase {

    /**
     * @param name
     */
    public EventsQueueTest(String name) {
        super(name);
    }

    /*
     *  (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /*
     *  (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for {@link org.ajax4jsf.event.EventsQueue#remove()}.
     */
    public void testRemove() {
        ActionEvent event = new ActionEvent(new UICommand());
        EventsQueue queue = new EventsQueue();

        queue.offer(event);
        assertFalse(queue.isEmpty());

        ActionEvent event2 = new ActionEvent(new UICommand());

        queue.offer(event2);
        assertSame(event, queue.remove());
        assertSame(event2, queue.remove());
        assertTrue(queue.isEmpty());

        try {
            queue.remove();
        } catch (NoSuchElementException e) {
            return;
        }

        assertTrue("exception not thrown", false);
    }

    /**
     * Test method for {@link org.ajax4jsf.event.EventsQueue#offer(javax.faces.event.FacesEvent)}.
     */
    public void testAdd() {
        ActionEvent event = new ActionEvent(new UICommand());
        EventsQueue queue = new EventsQueue();

        queue.offer(event);
        assertFalse(queue.isEmpty());

        ActionEvent event2 = new ActionEvent(new UICommand());

        queue.offer(event2);
    }

    /**
     * Test method for {@link org.ajax4jsf.event.EventsQueue#clear()}.
     */
    public void testClear() {
        ActionEvent event = new ActionEvent(new UICommand());
        EventsQueue queue = new EventsQueue();

        queue.offer(event);
        assertFalse(queue.isEmpty());

        ActionEvent event2 = new ActionEvent(new UICommand());

        queue.offer(event2);
        queue.clear();
        assertTrue(queue.isEmpty());
    }
}

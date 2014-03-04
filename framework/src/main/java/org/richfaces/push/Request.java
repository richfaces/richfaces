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
package org.richfaces.push;

/**
 * Request encapsulates active browser request to the server.
 *
 * @author Nick Belaevski
 */
public interface Request {

    /**
     * suspends an underlying response object
     */
    void suspend();

    /**
     * Resume the underlying response object
     */
    void resume();

    /**
     * Returns a user push session associated with this request
     */
    Session getSession();

    /**
     * Returns true if this request represents long-polling request
     */
    boolean isPolling();

    /**
     * Tries to push messages, when there are some in the session's queue.
     */
    void postMessages();
}

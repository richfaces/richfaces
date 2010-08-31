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

package org.richfaces.event;

/**
 * @author akolonitsky
 * @since 2010-08-27
 */
public interface ChangeExpandSource {

    /**
     * <p>Add a new {@link org.richfaces.event.ChangeExpandListener} to the set of listeners
     * interested in being notified when {@link org.richfaces.event.ChangeExpandEvent}s occur.</p>
     *
     * @param listener The {@link org.richfaces.event.ChangeExpandListener} to be added
     * @throws NullPointerException if <code>listener</code>
     *                              is <code>null</code>
     */
    void addChangeExpandListener(ChangeExpandListener listener);

    /**
     * <p>Return the set of registered {@link org.richfaces.event.ChangeExpandListener}s for this instance.
     * If there are no registered listeners, a zero-length array is returned.</p>
     */
    ChangeExpandListener[] getChangeExpandListeners();

    /**
     * <p>Remove an existing {@link org.richfaces.event.ChangeExpandListener} (if any) from the
     * set of listeners interested in being notified when
     * {@link org.richfaces.event.ChangeExpandEvent}s occur.</p>
     *
     * @param listener The {@link org.richfaces.event.ChangeExpandListener} to be removed
     * @throws NullPointerException if <code>listener</code>
     *                              is <code>null</code>
     */
    void removeChangeExpandListener(ChangeExpandListener listener);

}

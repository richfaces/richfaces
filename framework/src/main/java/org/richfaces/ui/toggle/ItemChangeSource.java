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

package org.richfaces.ui.toggle;

/**
 * @author akolonitsky
 * @since Jul 20, 2010
 */
public interface ItemChangeSource {
    /**
     * <p>
     * Add a new {@link org.richfaces.ui.toggle.ItemChangeListener} to the set of listeners interested in being notified when
     * {@link org.richfaces.ui.toggle.ItemChangeEvent}s occur.
     * </p>
     *
     * @param listener The {@link org.richfaces.ui.toggle.ItemChangeListener} to be added
     * @throws NullPointerException if <code>listener</code> is <code>null</code>
     */
    void addItemChangeListener(ItemChangeListener listener);

    /**
     * <p>
     * Return the set of registered {@link org.richfaces.ui.toggle.ItemChangeListener}s for this instance. If there are no
     * registered listeners, a zero-length array is returned.
     * </p>
     */
    ItemChangeListener[] getItemChangeListeners();

    /**
     * <p>
     * Remove an existing {@link org.richfaces.ui.toggle.ItemChangeListener} (if any) from the set of listeners interested in being
     * notified when {@link org.richfaces.ui.toggle.ItemChangeEvent}s occur.
     * </p>
     *
     * @param listener The {@link org.richfaces.ui.toggle.ItemChangeListener} to be removed
     * @throws NullPointerException if <code>listener</code> is <code>null</code>
     */
    void removeItemChangeListener(ItemChangeListener listener);
}

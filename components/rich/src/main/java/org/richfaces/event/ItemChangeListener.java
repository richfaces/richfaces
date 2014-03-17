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

import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesListener;

/**
 * <p>
 * A listener interface for receiving {@link ItemChangeEvent}s. A class that is interested in receiving such events implements
 * this interface, and then registers itself with the source {@link javax.faces.component.UIComponent} of interest, by calling
 * <code>addItemChangeListener()</code>.
 * </p>
 *
 * @author akolonitsky
 * @version 1.0
 * @since -4712-01-01
 *
 */
public interface ItemChangeListener extends FacesListener {
    /**
     * <p>
     * Invoked when {@link ItemChangeEvent} occurs.
     * </p>
     *
     * @param event The {@link ItemChangeEvent} that has occurred
     *
     * @throws AbortProcessingException Signal the JavaServer Faces implementation that no further processing on the current
     *         event should be performed
     */
    void processItemChange(ItemChangeEvent event) throws AbortProcessingException;
}

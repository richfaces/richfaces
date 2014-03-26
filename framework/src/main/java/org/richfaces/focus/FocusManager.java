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

package org.richfaces.focus;

import javax.faces.event.PhaseId;

/**
 * Service for managing Focus of components on the page.
 */
public interface FocusManager {

    String FOCUS_CONTEXT_ATTRIBUTE = FocusManager.class.getName() + ".FOCUS";

    /**
     * <p>
     * Enforces to focus given component.
     * </p>
     *
     * <p>
     * In order to ensure the focus will be given to component, this method must be used before {@link PhaseId#RENDER_RESPONSE}
     * phase takes place.
     * </p>
     *
     * @param componentId ID of the component to be focused; or null if focus should not be enforced
     */
    void focus(String componentId);
}

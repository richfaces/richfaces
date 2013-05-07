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

package org.richfaces.ui.misc.focus;

import javax.faces.context.FacesContext;

/**
 * The strategy for rendering Focus component
 */
public interface FocusRenderStrategy {

    /**
     * This method will be called once the component is placed into view
     */
    void postAddToView(FacesContext context, AbstractFocus component);

    /**
     * Get space-separated list of clientIds as component candidates to be focused on client.
     */
    String getFocusCandidatesAsString(FacesContext context, AbstractFocus component);

    /**
     * Determines whether the currently rendered Focus should be rendered or not based on if request is postback and if Focus
     * belongs to form which has been submitted.
     */
    boolean shouldApply(FacesContext context, AbstractFocus component);
}
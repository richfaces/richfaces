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
package org.richfaces.javascript;

import javax.faces.context.FacesContext;

/**
 * <p class="changed_added_4_0">
 * This service stores JavaScript objects for deffered rendering, as described on
 * http://community.jboss.org/wiki/RichFacesJavaScripthandling
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public interface JavaScriptService {
    /**
     * <p class="changed_added_4_0">
     * Adds new script to render at the end of page. Is the same script already set to render ( lookup by equals() method ), no
     * new object added.
     * </p>
     *
     * @param facesContext TODO
     * @param script
     * @return actual object that will be rendered at the end of page.
     */
    <S> S addScript(FacesContext facesContext, S script);

    /**
     * <p class="changed_added_4_0">
     * This method adds script that has to be executed in page.onready event listener, as required by jQuery components.
     * </p>
     *
     * @param facesContext TODO
     * @param script
     * @param <S>
     * @return
     */
    <S> S addPageReadyScript(FacesContext facesContext, S script);

    /**
     * <p class="changed_added_4_0">
     * Get object with collection of scripts designeted for deffered rendering.
     * </p>
     *
     * @param context
     * @return ViewResource component that holds contect of all scripts, or special case object with empty collections.
     */
    ScriptsHolder getScriptsHolder(FacesContext context);
}

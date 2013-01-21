/*
 * $Id$
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
 * This interface describes service that determines JavaScript module and function for Java corresponded version ( both
 * Converter and Validator )
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public interface ClientScriptService {
    /**
     * <p class="changed_added_4_0">
     * Get description for client-side version of Java implementation
     * </p>
     *
     * @param facesContext TODO
     * @param javaClass either Converter or Validator class.
     * @return description of client-side script.
     * @throws ScriptNotFoundException if no JavaScript code associated with Java class.
     */
    LibraryFunction getScript(FacesContext facesContext, Class<?> javaClass) throws ScriptNotFoundException;
}

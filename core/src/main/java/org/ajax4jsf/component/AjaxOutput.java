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
package org.ajax4jsf.component;

/**
 * Marker interface for all JSF components, encoded ( or not ) on rendering Ajax request , depend on it self properties ( such
 * as messages, help or like components )
 *
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:57:39 $
 *
 */
public interface AjaxOutput {
    /**
     * getter for ajax-rendered flag property.
     *
     * @return ajax-rendered flag
     */
    boolean isAjaxRendered();

    /**
     * Setter for ajax rendered property.
     *
     * @param ajaxRendered
     */
    void setAjaxRendered(boolean ajaxRendered);

    /**
     * getter for keep transient property.
     *
     * @return keep transient property
     */
    boolean isKeepTransient();

    /**
     * Setter for keep transient property.
     *
     * @param ajaxRendered
     */
    void setKeepTransient(boolean ajaxRendered);
}

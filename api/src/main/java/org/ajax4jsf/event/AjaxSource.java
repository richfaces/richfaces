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

/**
 * Interface for all components capable to process {@link org.ajax4jsf.event.AjaxEvent}'s
 * @author shura
 *
 */
public interface AjaxSource {

    /**
     * same as for ActionSource, but for AjaxListener
     *
     * @see javax.faces.component.ActionSource#addActionListener(javax.faces.event.ActionListener)
     */
    public void addAjaxListener(AjaxListener listener);

    /**
     * same as for ActionSource, but for AjaxListener
     *
     * @see javax.faces.component.ActionSource#getActionListeners()
     */
    public AjaxListener[] getAjaxListeners();

    /**
     *  same as for ActionSource, but for AjaxListener
     * @see javax.faces.component.ActionSource#removeActionListener(javax.faces.event.ActionListener)
     */
    public void removeAjaxListener(AjaxListener listener);
}

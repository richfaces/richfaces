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



package org.ajax4jsf.resource;

import javax.el.MethodExpression;

/**
 * Interface for the ResourceComponent introduced after refactoring
 * to support MethodExpression. Old interface is left for the
 * compatibility.
 *
 * @author Vladislav Baranov
 */
public interface ResourceComponent2 extends ResourceComponent {

    /**
     * Get MethodExpression to method in user bean to send resource. Method will
     * called with two parameters - restored data object and servlet output
     * stream.
     *
     * @return MethodExpression
     */
    public abstract MethodExpression getCreateContentExpression();

    /**
     * Set MethodExpression to method in user bean to send resource. Method will
     * called with two parameters - restored data object and servlet output
     * stream.
     *
     * @param newvalue - new MethodExpression value
     */
    public abstract void setCreateContentExpression(MethodExpression newvalue);
}

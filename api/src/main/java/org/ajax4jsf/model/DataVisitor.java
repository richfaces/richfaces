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
package org.ajax4jsf.model;

import javax.faces.context.FacesContext;

/**
 * Interface for classes, able to receive visitor processing calls from ExtendedDataModel
 *
 * @author shura
 *
 */
public interface DataVisitor {
    /**
     * This method called back ( as visitor ) from {@link ExtendedDataModel#walk(FacesContext, DataVisitor, Range)} for each
     * row.
     *
     * @param context current JSF context.
     * @param rowKey current row key of DataModel
     * @param argument Implementation-specific argument
     */
    DataVisitResult process(FacesContext context, Object rowKey, Object argument);
}

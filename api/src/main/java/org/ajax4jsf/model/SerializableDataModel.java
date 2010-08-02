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

import java.io.Serializable;

/**
 * Serializable version of {@link ExtendedDataModel}, for save lightweight version of data
 *
 * @author shura
 *
 */
@SuppressWarnings("serial")
public abstract class SerializableDataModel extends ExtendedDataModel implements Serializable {

    /**
     * Method called after update all model values. For example, developer can update
     * database with new values of modified rows.
     */
    public abstract void update();
}

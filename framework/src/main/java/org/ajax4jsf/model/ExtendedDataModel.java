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
import javax.faces.model.DataModel;

/**
 * Extesion for {@link DataModel} , for support complex data structure, like tree, spreadsheet etc in iterable components.
 *
 * @author shura
 *
 */
public abstract class ExtendedDataModel<E> extends DataModel<E> {
    /**
     * <p>
     * Instead of simple <code>int</code> for current state selection, this model can use any object for select current data.
     * Implementation depend on model, with any restrictions :
     * </p>
     * <ol>
     * <li>key must be {@link java.io.Serializable}.</li>
     * <li>{@link Object#toString()} method must return representation compatible with
     * {@link javax.faces.component.UIComponent#getClientId(javax.faces.context.FacesContext)}, as far as this string will be
     * appended to clientId of iterator component.</li>
     * </ol>
     *
     * @param key key for select current data, or null for clear selection.
     */
    public abstract void setRowKey(Object key);

    /**
     * @return key for selected data or <code>null</code>
     */
    public abstract Object getRowKey();

    /**
     * Iterate over model by "visitor" pattern, for given range
     *
     * @param context current JSF context.
     * @param visitor instance of {@link DataVisitor}, for process each row.
     * @param range Implementation-specific range of data keys.
     * @param argument Implementation-specific argument
     * @throws java.io.IOException
     */
    public abstract void walk(FacesContext context, DataVisitor visitor, Range range, Object argument);

    /**
     * Create Iterator on all row keys for given range.
     *
     * @param range - Implementation-specific range of data keys.
     * @return
     */

    // public abstract Iterator dataIterator(Range range);
}

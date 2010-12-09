/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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

package org.richfaces.component;

import java.util.Iterator;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;


class DataTableDataIterator extends DataTableIteratorBase<UIComponent> {

    private Iterator<UIComponent> facetsIterator;

    private Iterator<UIComponent> childrenIterator;

    public DataTableDataIterator(UIDataTableBase dataTable) {
        this.childrenIterator = dataTable.getChildren().iterator();
        this.facetsIterator = dataTable.getFacets().values().iterator();
    }

    protected UIComponent nextItem() {

        UIComponent nextColumn = null;
        while (nextColumn == null && childrenIterator.hasNext()) {
            UIComponent child = childrenIterator.next();
            if ((child instanceof UIColumn) || (child instanceof Column)) {
                nextColumn = child;
            } 
        }

        // TODO nick - free childrenIterator

        while (nextColumn == null && facetsIterator.hasNext()) {
            nextColumn = facetsIterator.next();
        }

        // TODO nick - free facetsIterator

        return nextColumn;
    }

    protected Iterator<UIComponent> getFacetsIterator() {
        return this.facetsIterator;
    }

    protected Iterator<UIComponent> getChildrenIterator() {
        return this.childrenIterator;
    }

}

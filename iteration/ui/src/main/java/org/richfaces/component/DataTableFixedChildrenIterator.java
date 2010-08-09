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

import javax.faces.component.UIComponent;


class DataTableFixedChildrenIterator extends DataTableDataIterator {

    private Iterator<UIComponent> currentColumnIterator;

    public DataTableFixedChildrenIterator(UIDataTableBase dataTable) {
        super(dataTable);
    }

    protected UIComponent nextItem() {
        UIComponent next = null;

        if (currentColumnIterator != null && currentColumnIterator.hasNext()) {
            next = currentColumnIterator.next();
            checkColumnIterator();
        }

        if (next == null) {
            Iterator<UIComponent> childrenIterator = getChildrenIterator();
            while (next == null && childrenIterator.hasNext()) {
                UIComponent child = childrenIterator.next();
                if ((child instanceof UIColumn) && child.isRendered()) {
                    currentColumnIterator = getChildFacetIterator(child);
                    next = nextItem();
                } 
            }
        }

        if (next == null) {
            next = getNextFacet();
        }
        return next;
    }

    protected UIComponent getNextFacet() {
        Iterator<UIComponent> facetsIterator = getFacetsIterator();
        if(facetsIterator.hasNext()) {
            return facetsIterator.next();
        }
        return null;
    }

    protected void checkColumnIterator() {
        if (!currentColumnIterator.hasNext()) {
            currentColumnIterator = null;
        }
    }

    protected Iterator<UIComponent> getChildFacetIterator(UIComponent component) {
        return component.getFacets().values().iterator();
    }

}

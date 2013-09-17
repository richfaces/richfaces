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

import com.google.common.collect.AbstractIterator;

/**
 * Iterator for all children table columns.
 *
 * @author asmirnov
 *
 */
class DataTableColumnsIterator extends AbstractIterator<UIComponent> {
    private Iterator<UIComponent> childrenIterator;

    public DataTableColumnsIterator(UIComponent component) {
        super();
        this.childrenIterator = component.getChildren().iterator();
    }

    @Override
    protected UIComponent computeNext() {
        while (childrenIterator.hasNext()) {
            UIComponent child = childrenIterator.next();

            if (child instanceof UIColumn || child instanceof Column) {
                return child;
            }
        }

        return endOfData();
    }
}

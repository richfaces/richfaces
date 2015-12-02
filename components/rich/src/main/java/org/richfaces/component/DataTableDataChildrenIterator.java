/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
import com.google.common.collect.ImmutableSet;

/**
 * @author Nick Belaevski
 *
 */
class DataTableDataChildrenIterator extends AbstractIterator<UIComponent> {
    private Iterator<UIComponent> dataTableChildren;
    private Iterator<UIComponent> columnChildren = ImmutableSet.<UIComponent>of().iterator();

    public DataTableDataChildrenIterator(UIComponent dataTable) {
        super();
        this.dataTableChildren = dataTable.getChildren().iterator();
    }

    @Override
    protected UIComponent computeNext() {
        while (columnChildren.hasNext() || dataTableChildren.hasNext()) {
            if (columnChildren.hasNext()) {
                return columnChildren.next();
            }

            UIComponent child = dataTableChildren.next();
            if (child instanceof UIColumn || child instanceof AbstractColumn) {
                columnChildren = child.getChildren().iterator();
            } else {
                columnChildren = ImmutableSet.<UIComponent>of().iterator();
                return child;
            }
        }

        dataTableChildren = ImmutableSet.<UIComponent>of().iterator();
        columnChildren = ImmutableSet.<UIComponent>of().iterator();

        return endOfData();
    }
}

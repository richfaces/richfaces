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
package org.richfaces.ui.iteration.column;

import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.ui.iteration.AbstractTableBaseRenderer;
import org.richfaces.ui.iteration.RowHolder;
import org.richfaces.ui.iteration.RowHolderBase;
import org.richfaces.ui.iteration.UIDataTableBase;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;

import java.io.IOException;
import java.util.Iterator;

/**
 * @author Anton Belevich
 *
 */
@JsfRenderer(type = "org.richfaces.ui.ColumnGroupRenderer", family = AbstractColumnGroup.COMPONENT_FAMILY)
public class ColumnGroupRenderer extends AbstractTableBaseRenderer {
    public void encodeRow(ResponseWriter writer, FacesContext facesContext, RowHolderBase holder) throws IOException {
        RowHolder rowHolder = (RowHolder) holder;

        AbstractColumnGroup row = (AbstractColumnGroup) rowHolder.getRow();
        rowHolder.setRowStart(true);

        Iterator<UIComponent> components = row.columns();
        int columnNumber = 0;
        while (components.hasNext()) {
            UIColumn column = (UIColumn) components.next();
            if (column.isRendered()) {
                column.getAttributes().put(COLUMN_CLASS, getColumnClass(rowHolder, columnNumber));
                encodeColumn(facesContext, writer, column, rowHolder);
                columnNumber++;
            }
        }
    }

    public RowHolderBase createRowHolder(FacesContext context, UIComponent component, Object[] options) {
        UIComponent parent = component.getParent();
        while (parent != null && !(parent instanceof UIDataTableBase)) {
            parent = parent.getParent();
        }

        if (parent == null) {
            throw new AbortProcessingException("UIColumnGroup should be a child of UIDataTable or UISubTable");
        }

        RowHolder rowHolder = new RowHolder(context, (AbstractColumnGroup) component);
        rowHolder.setParentClientId(parent.getClientId(context));
        return rowHolder;
    }
}

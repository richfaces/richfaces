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

package org.richfaces.renderkit;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;

import org.richfaces.component.UIColumnGroup;
import org.richfaces.component.UIDataTableBase;

/**
 * @author Anton Belevich
 *
 */
public class ColumnGroupRenderer extends AbstractTableBaseRenderer {

    public void encodeRow(ResponseWriter writer, FacesContext facesContext, RowHolderBase holder) throws IOException {
        RowHolder rowHolder = (RowHolder) holder;

        UIColumnGroup row = (UIColumnGroup)rowHolder.getRow();
        rowHolder.setRowStart(true);
        
        Iterator<UIComponent> components = row.columns();

        while(components.hasNext()){
            encodeColumn(facesContext,  writer, (UIColumn)components.next(),rowHolder);
        }
        encodeRowEnd(writer);
    }

    public RowHolderBase createRowHolder(FacesContext context, UIComponent component, Object[] options) {
        UIComponent parent = component.getParent(); 
        while(parent != null && !(parent instanceof UIDataTableBase)) {
            parent = parent.getParent();
        }
        
        if(parent == null) {
            throw new AbortProcessingException("UIColumnGroup should be a child of UIDataTable or UISubTable");
        }
        
        RowHolder rowHolder = new RowHolder(context, (UIColumnGroup)component); 
        rowHolder.setParentClientId(parent.getClientId());
        return rowHolder;
    }
}

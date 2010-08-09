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
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;

import org.ajax4jsf.model.DataVisitor;
import org.richfaces.renderkit.RowHolderBase;

public abstract class UIColumnGroup extends UIPanel implements Row, Column {

    public Iterator<UIComponent> columns() {
        return new DataTableColumnsIterator(this);
    }

    public void setBreakBefore(boolean newBreakBefore) {
        throw new IllegalStateException("Property 'breakBefore' for subtable is read-only");
    }

    public void setRowKey(FacesContext context, Object rowKey) {
        // columnGroup doesn't have data model
    }

    public void walk(FacesContext context, DataVisitor visitor, Object argument) {
        if (!(argument instanceof RowHolderBase)) {
            return;
        }

        //TODO nick - implement in the proper way
        visitor.process(context, null, argument);
    }
}

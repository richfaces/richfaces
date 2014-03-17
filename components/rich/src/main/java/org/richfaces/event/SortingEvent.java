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
package org.richfaces.event;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

import org.richfaces.model.SortOrder;

public class SortingEvent extends FacesEvent {
    private static final long serialVersionUID = 2125258204996535522L;
    private SortOrder sortOrder;
    private String columnId;

    public SortingEvent(UIComponent source, String columnId, SortOrder sortOrder) {
        super(source);
        this.sortOrder = sortOrder;
        this.setColumnId(columnId);
    }

    public boolean isAppropriateListener(FacesListener listener) {
        return (listener instanceof SortingListener);
    }

    public void processListener(FacesListener listener) {
        ((SortingListener) listener).processSorting(this);
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public String getColumnId() {
        return columnId;
    }
}

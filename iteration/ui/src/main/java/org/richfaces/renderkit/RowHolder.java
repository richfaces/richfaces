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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.richfaces.component.Row;

/**
 * @author Anton Belevich
 *
 */
public class RowHolder extends RowHolderBase {
    private Row row;
    private String parentClientId;
    private boolean isRowStart;
    private boolean updatePartial;
    private boolean encodeParentTBody;

    public RowHolder(FacesContext context, Row row) {
        this(context, row, 0, true);
    }

    public RowHolder(FacesContext context, Row row, int processCell, boolean isRowStart) {
        super(context);
        this.row = row;
        this.parentClientId = ((UIComponent) row).getClientId(context);
    }

    public boolean isEncodeParentTBody() {
        return encodeParentTBody;
    }

    public void setEncodeParentTBody(boolean encodeParentTBody) {
        this.encodeParentTBody = encodeParentTBody;
    }

    public boolean isUpdatePartial() {
        return updatePartial;
    }

    public void setUpdatePartial(boolean updatePartial) {
        this.updatePartial = updatePartial;
    }

    public String getParentClientId() {
        return parentClientId;
    }

    public void setParentClientId(String parentClientId) {
        this.parentClientId = parentClientId;
    }

    public Row getRow() {
        return this.row;
    }

    public boolean isRowStart() {
        return isRowStart;
    }

    public void setRowStart(boolean isRowStart) {
        this.isRowStart = isRowStart;
    }
}

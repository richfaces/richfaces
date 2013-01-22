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



package org.ajax4jsf.tests;

import java.io.IOException;

import javax.faces.context.FacesContext;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SerializableDataModel;

/**
 * @author shura
 *
 */
public class MockSerializableDataModel extends MockDataModel {
    public SerializableDataModel getSerializableModel(Range range) {
        MockRange mockRange = (MockRange) range;
        SerializableDataModel model = new SerializableDataModel() {
            public void update() {

                // TODO Auto-generated method stub
            }
            public Object getRowKey() {

                // TODO Auto-generated method stub
                return null;
            }
            public void setRowKey(Object key) {

                // TODO Auto-generated method stub
            }
            public void walk(FacesContext context, DataVisitor visitor, Range range, Object argument)
                    throws IOException {

                // TODO Auto-generated method stub
            }
            public int getRowCount() {

                // TODO Auto-generated method stub
                return 0;
            }
            public Object getRowData() {

                // TODO Auto-generated method stub
                return null;
            }
            public int getRowIndex() {

                // TODO Auto-generated method stub
                return 0;
            }
            public Object getWrappedData() {

                // TODO Auto-generated method stub
                return null;
            }
            public boolean isRowAvailable() {

                // TODO Auto-generated method stub
                return false;
            }
            public void setRowIndex(int arg0) {

                // TODO Auto-generated method stub
            }
            public void setWrappedData(Object arg0) {

                // TODO Auto-generated method stub
            }
        };

        return model;
    }
}

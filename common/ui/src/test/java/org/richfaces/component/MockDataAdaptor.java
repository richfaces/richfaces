/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat, Inc. and individual contributors
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

import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.ajax4jsf.model.DataComponentState;
import org.ajax4jsf.model.ExtendedDataModel;

/**
 * @author Nick Belaevski
 *
 */
public class MockDataAdaptor extends UIDataAdaptor {
    private ExtendedDataModel<?> dataModel;

    @Override
    protected DataComponentState createComponentState() {
        return new MockDataAdaptorComponentState();
    }

    @Override
    protected ExtendedDataModel<?> createExtendedDataModel() {
        return dataModel;
    }

    public ExtendedDataModel<?> getDataModel() {
        return dataModel;
    }

    public void setDataModel(ExtendedDataModel<?> dataModel) {
        this.dataModel = dataModel;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.component.UIDataAdaptor#getRowKeyConverter()
     */
    @Override
    public Converter getRowKeyConverter() {
        // TODO Auto-generated method stub
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().createConverter(Integer.class);
    }
}

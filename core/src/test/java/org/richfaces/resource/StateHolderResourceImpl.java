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
package org.richfaces.resource;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
@DynamicResource
// TODO add tests for StateHolder
public class StateHolderResourceImpl extends AbstractCacheableResource implements StateHolderResource {
    private Object state = "";

    public StateHolderResourceImpl() {
        setResourceName(StateHolderResourceImpl.class.getName());
    }

    @Override
    public InputStream getInputStream() throws IOException {
        try {
            return new ByteArrayInputStream(state.toString().getBytes("US-ASCII"));
        } catch (UnsupportedEncodingException e) {
            throw new FacesException(e.getMessage(), e);
        }
    }

    public boolean isTransient() {
        return false;
    }

    public void restoreState(FacesContext context, Object state) {
        this.state = state;
    }

    public Object saveState(FacesContext context) {
        return state;
    }

    public void setTransient(boolean newTransientValue) {
        throw new UnsupportedOperationException();
    }

    public void readState(FacesContext context, DataInput dataInput) throws IOException {
        state = dataInput.readLine();
    }

    public void writeState(FacesContext context, DataOutput dataOutput) throws IOException {
        dataOutput.writeBytes(state.toString());
    }
}

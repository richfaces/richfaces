/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.context;

import javax.faces.component.UIOutput;

import org.ajax4jsf.component.AjaxOutput;

/**
 * @author Nick Belaevski
 *
 */
public class AjaxOutputComponentImpl extends UIOutput implements AjaxOutput {
    private enum PropertyKeys {
        ajaxRendered
    }

    public AjaxOutputComponentImpl() {
        setRendererType("javax.faces.Text");
    }

    public boolean isAjaxRendered() {
        return Boolean.valueOf(String.valueOf(getStateHelper().eval(PropertyKeys.ajaxRendered)));
    }

    public boolean isKeepTransient() {
        return false;
    }

    public void setAjaxRendered(boolean ajaxRendered) {
        getStateHelper().put(PropertyKeys.ajaxRendered, ajaxRendered);
    }

    public void setKeepTransient(boolean ajaxRendered) {
        throw new UnsupportedOperationException();
    }
}

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
package org.richfaces.context;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContextWrapper;

import com.google.common.collect.Maps;

/**
 * @author Nick Belaevski
 *
 */
public abstract class ExtendedPartialViewContext extends PartialViewContextWrapper {
    private static final String ATTRIBUTE_NAME = ExtendedPartialViewContext.class.getName();
    private FacesContext facesContext;
    private boolean limitRender = false;
    private Object responseData = null;
    private Map<String, Object> responseComponentDataMap = Maps.newHashMap();
    private StringBuilder beforedomupdateHandler = new StringBuilder();
    private StringBuilder completeHandler = new StringBuilder();

    public ExtendedPartialViewContext(FacesContext facesContext) {
        this.facesContext = facesContext;

        setInstance(facesContext, this);
    }

    protected FacesContext getFacesContext() {
        return facesContext;
    }

    private static void setInstance(FacesContext facesContext, ExtendedPartialViewContext instance) {
        facesContext.getAttributes().put(ATTRIBUTE_NAME, instance);
    }

    public static ExtendedPartialViewContext getInstance(FacesContext facesContext) {
        return (ExtendedPartialViewContext) facesContext.getAttributes().get(ATTRIBUTE_NAME);
    }

    @Override
    public void release() {
        setInstance(facesContext, null);
        facesContext = null;
    }

    public Object getResponseData() {
        return responseData;
    }

    public void setResponseData(Object responseData) {
        this.responseData = responseData;
    }

    public void appendOncomplete(Object handler) {
        if (handler != null) {
            completeHandler.append(handler.toString());
            completeHandler.append(';');
        }
    }

    public void prependOncomplete(Object handler) {
        if (handler != null) {
            completeHandler.insert(0, ';');
            completeHandler.insert(0, handler.toString());
        }
    }

    public Object getOncomplete() {
        return completeHandler.toString();
    }

    public void appendOnbeforedomupdate(Object handler) {
        if (handler != null) {
            beforedomupdateHandler.append(handler.toString());
            beforedomupdateHandler.append(';');
        }
    }

    public void prependOnbeforedomupdate(Object handler) {
        if (handler != null) {
            beforedomupdateHandler.insert(0, handler.toString());
            beforedomupdateHandler.insert(0, ';');
        }
    }

    public Object getOnbeforedomupdate() {
        return beforedomupdateHandler.toString();
    }

    public Map<String, Object> getResponseComponentDataMap() {
        return responseComponentDataMap;
    }

    public boolean isLimitRender() {
        return limitRender;
    }

    public void setLimitRender(boolean limitRender) {
        this.limitRender = limitRender;
    }
}

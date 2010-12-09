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



package org.ajax4jsf.context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.ajax4jsf.resource.util.URLToStreamHelper;

public abstract class AjaxContext {

    /**
     * Key for keep request state information in request-scope attributes.
     */
    public static final String AJAX_CONTEXT_KEY = "ajaxContext";
    static final String SERVICE_RESOURCE = "META-INF/services/" + AjaxContext.class.getName();
    private static final String DEFAULT_CONTEXT_CLASS = "org.ajax4jsf.context.AjaxContextImpl";
    private static Map<ClassLoader, Class<? extends AjaxContext>> ajaxContextClasses =
        new HashMap<ClassLoader, Class<? extends AjaxContext>>();
    private boolean disableImplicitRender;

    protected AjaxContext() { }

    public abstract Map<String, Object> getCommonAjaxParameters();

    public abstract String getAjaxActionURL(FacesContext context);

    public abstract void setResponseData(Object responseData);

    public abstract Object getResponseData();

    public abstract void setOncomplete(Object oncompleteFunction);

    public abstract void appendOncomplete(Object oncompleteFunction);

    public abstract Object getOncomplete();

    public abstract void setOnbeforedomupdate(Object onbeforedomupdateFunction);

    public abstract Object getOnbeforedomupdate();
    
    public abstract void setViewIdHolder(ViewIdHolder viewIdHolder);

    public abstract ViewIdHolder getViewIdHolder();

    public abstract boolean removeRenderedArea(String id);

    public abstract void addRenderedArea(String id);

    public abstract Set<String> getAjaxRenderedAreas();

    public abstract void addRegionsFromComponent(UIComponent component);

    /**
     * @param component
     * @since 3.3.0
     */
    public abstract void addAreasToProcessFromComponent(FacesContext context, UIComponent component);

    public abstract Set<String> getAjaxAreasToRender();

    public abstract Set<String> getAjaxAreasToProcess();

    public boolean isAjaxRequest(FacesContext facesContext) {
        return isAjaxRequest();
    }

    public abstract boolean isAjaxRequest();

    public abstract void encodeAjaxEnd(FacesContext context) throws IOException;

    public abstract void encodeAjaxBegin(FacesContext context) throws IOException;

    public abstract void renderAjax(FacesContext context);

    public abstract void release();

    public abstract Map<String, Object> getResponseComponentDataMap();

    public abstract void setAjaxRequest(boolean b);

    public abstract boolean isSelfRender();

    public abstract void setSelfRender(boolean b);

    public abstract String getSubmittedRegionClientId();

    public abstract void saveViewState(FacesContext context) throws IOException;

    public abstract void setAjaxSingleClientId(String ajaxSingleClientId);

    public abstract String getAjaxSingleClientId();

    public abstract void setAjaxAreasToProcess(Set<String> ajaxAreasToProcess);

    public abstract void setSubmittedRegionClientId(String submittedClientId);

    public boolean isDisableImplicitRender() {
        return disableImplicitRender;
    }

    public void setDisableImplicitRender(boolean disableImplicitRender) {
        this.disableImplicitRender = disableImplicitRender;
    }

    /**
     * Get instance of current AJAX Context. Instance get by
     * variable {@link AjaxContext#AJAX_CONTEXT_KEY}
     *
     * @return memento instance for current request
     */
    public static AjaxContext getCurrentInstance() {
        FacesContext context = FacesContext.getCurrentInstance();

        return getCurrentInstance(context);
    }

    /**
     * Get instance of current AJAX Context. Instance get by
     * variable {@link AjaxContext#AJAX_CONTEXT_KEY}
     *
     * @param context
     *            current FacesContext
     * @return instance of AjaxContext.
     */
    public static AjaxContext getCurrentInstance(FacesContext context) {
        if (null == context) {
            throw new NullPointerException("FacesContext is null");
        }

        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        AjaxContext ajaxContext = (AjaxContext) requestMap.get(AJAX_CONTEXT_KEY);

        if (null == ajaxContext) {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

            if (null == contextClassLoader) {
                contextClassLoader = AjaxContext.class.getClassLoader();
            }

            Class<? extends AjaxContext> clazz;

            synchronized (ajaxContextClasses) {
                clazz = ajaxContextClasses.get(contextClassLoader);

                if (null == clazz) {
                    String factoryClassName = DEFAULT_CONTEXT_CLASS;

                    // Pluggable factories.
                    InputStream input = null; // loader.getResourceAsStream(SERVICE_RESOURCE);

                    input = URLToStreamHelper.urlToStreamSafe(contextClassLoader.getResource(SERVICE_RESOURCE));

                    // have services file.
                    if (input != null) {
                        try {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                            factoryClassName = reader.readLine();
                        } catch (Exception e) {
                            throw new FacesException("Error to create AjaxContext Instance", e);
                        } finally {
                            try {
                                input.close();
                            } catch (IOException e) {

                                // Ignore
                            }
                        }
                    }

                    try {
                        clazz = Class.forName(factoryClassName, false,
                                              contextClassLoader).asSubclass(AjaxContext.class);
                    } catch (ClassNotFoundException e) {
                        throw new FacesException("AjaxContext implementation class " + factoryClassName
                                                 + " not found ", e);
                    }

                    ajaxContextClasses.put(contextClassLoader, clazz);
                }
            }

            try {
                ajaxContext = clazz.newInstance();
            } catch (InstantiationException e) {
                throw new FacesException("Error to create AjaxContext Instance", e);
            } catch (IllegalAccessException e) {
                throw new FacesException("No access to AjaxContext constructor", e);
            }

            requestMap.put(AJAX_CONTEXT_KEY, ajaxContext);
        }

        return ajaxContext;
    }
}

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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.ajax4jsf.Messages;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.renderkit.HtmlConstants;
import org.richfaces.renderkit.util.CoreAjaxRendererUtils;
import org.richfaces.renderkit.util.CoreRendererUtils;

/**
 * This class incapsulated
 *
 * @author asmirnov@exadel.com (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.7 $ $Date: 2007/02/08 19:07:16 $
 */
public class AjaxContextImpl extends AjaxContext {
    public static final String SERVLET_ERROR_EXCEPTION_ATTRIBUTE = "javax.servlet.error.exception";
    
    //TODO remove this
    private static final String SERIALIZED_STATE_KEY = "org.ajax4jsf.view.serializedstate";

    private static final Logger LOG = RichfacesLogger.CONTEXT.getLogger();
    Set<String> ajaxAreasToProcess = null;
    Set<String> ajaxAreasToRender = new LinkedHashSet<String>();
    Set<String> ajaxRenderedAreas = new LinkedHashSet<String>();
    boolean ajaxRequest = false;
    String ajaxSingleClientId = null;
    Object oncomplete = null;
    Object onbeforedomupdate = null;
    boolean selfRender = false;
    String submittedRegionClientId = null;
    ViewIdHolder viewIdHolder = null;
    Integer viewSequence = new Integer(1);
    Map<String, Object> responseComponentDataMap = new HashMap<String, Object>();
    Map<String, Object> commonAjaxParameters = new HashMap<String, Object>();
    Object responseData = null;
    
    public void release() {
        ajaxAreasToRender = new LinkedHashSet<String>();
        ajaxAreasToProcess = null;
        ajaxRenderedAreas = new LinkedHashSet<String>();
        ajaxRequest = false;
        selfRender = false;
        viewSequence = new Integer(1);
        submittedRegionClientId = null;
        viewIdHolder = null;
        responseComponentDataMap = new HashMap<String, Object>();
        commonAjaxParameters = new HashMap<String, Object>();
        responseData = null;
    }

    /**
     * @param context
     * @throws javax.faces.event.AbortProcessingException
     *
     */
    public void renderAjax(FacesContext context) throws FacesException {
        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getMessage(Messages.RENDER_AJAX_REQUEST, getSubmittedRegionClientId()));
        }

        try {

            // Just in case...
            setSelfRender(true);
            setAjaxRequest(true);

            // create response writer.
            ExternalContext extContext = context.getExternalContext();
            RenderKit renderKit = context.getRenderKit();
            String encoding;

            // Depends if we talk about servlets, portlets, ...
            if (extContext.getRequest() instanceof ServletRequest) {
                ServletRequest request = (ServletRequest) extContext.getRequest();
                ServletResponse response = (ServletResponse) extContext.getResponse();

                // Setup encoding and content type
                String contentType = "text/xml";

                // get the encoding - must be setup by faces context or filter.
                encoding = request.getCharacterEncoding();

                if (encoding == null) {
                    encoding = "UTF-8";
                }

                response.setContentType(contentType + ";charset=" + encoding);
            } else {
                encoding = "UTF-8";
            }

            PrintWriter servletWriter;

            servletWriter = getWriter(extContext);

            ResponseWriter writer = renderKit.createResponseWriter(servletWriter, null, encoding);

            context.setResponseWriter(writer);

            // make response
            writer.startDocument();
            encodeAjaxBegin(context);
            context.getViewRoot().encodeAll(context);
            saveViewState(context);
            encodeAjaxEnd(context);
            writer.endDocument();
            writer.flush();
            writer.close();
            servletWriter.close();

            // Save tree state.
        } catch (IOException e) {
            throw new FacesException(Messages.getMessage(Messages.RENDERING_AJAX_REGION_ERROR,
                getSubmittedRegionClientId()), e);
        } finally {
            context.responseComplete();

            // component.setRendererType(defaultRenderer);
        }
    }

    /**
     * Encode declaration for AJAX response. Render &lt;html&gt;&lt;body&gt;
     *
     * @param context
     * @throws IOException
     */
    public void encodeAjaxBegin(FacesContext context) throws IOException {
        UIViewRoot viewRoot = context.getViewRoot();

        // AjaxContainer ajax = (AjaxContainer) component;
        ResponseWriter out = context.getResponseWriter();

        // DebugUtils.traceView("ViewRoot in AJAX Page encode begin");
        out.startElement(HtmlConstants.HTML_ELEMENT, viewRoot);

        Locale locale = viewRoot.getLocale();

        out.writeAttribute(HtmlConstants.LANG_ATTRIBUTE, locale.toString(), "lang");
        out.startElement(HtmlConstants.BODY_ELEMENT, viewRoot);
    }

    /**
     * End encoding of AJAX response. Render tag with included areas and close
     * &lt;/body&gt;&lt;/html&gt;
     *
     * @param context
     * @throws IOException
     */
    public void encodeAjaxEnd(FacesContext context) throws IOException {

        // AjaxContainer ajax = (AjaxContainer) component;
        ResponseWriter out = context.getResponseWriter();

        // DebugUtils.traceView("ViewRoot in AJAX Page encode begin");
        out.endElement(HtmlConstants.BODY_ELEMENT);
        out.endElement(HtmlConstants.HTML_ELEMENT);
    }

    public void saveViewState(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        StateManager stateManager = context.getApplication().getStateManager();
        Object serializedView = stateManager.saveView(context);

        if ((null != serializedView) && (null != writer)) {
            StringWriter bufWriter = new StringWriter();
            ResponseWriter tempWriter;

            tempWriter = writer.cloneWithWriter(bufWriter);
            context.setResponseWriter(tempWriter);
            stateManager.writeState(context, serializedView);
            tempWriter.flush();

            if (bufWriter.getBuffer().length() > 0) {
                context.getExternalContext().getRequestMap().put(SERIALIZED_STATE_KEY,
                    bufWriter.toString());
            }

            context.setResponseWriter(writer);
        }
    }

    protected RenderKit getRenderKit(FacesContext context) {
        RenderKit renderKit = context.getRenderKit();

        if (null == renderKit) {
            String renderKitId = context.getApplication().getViewHandler().calculateRenderKitId(context);
            RenderKitFactory factory = (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);

            renderKit = factory.getRenderKit(context, renderKitId);
        }

        return renderKit;
    }

    /**
     * @return Returns the ajaxRequest.
     */
    public boolean isAjaxRequest() {
        return ajaxRequest;
    }

    /**
     * @param ajaxRequest The ajaxRequest to set.
     */
    public void setAjaxRequest(boolean ajaxRequest) {
        this.ajaxRequest = ajaxRequest;
    }

    /**
     * @return Returns the ajaxAreasToRender.
     */
    public Set<String> getAjaxAreasToRender() {
        return this.ajaxAreasToRender;
    }

    /**
     * @return the ajaxAreasToProcess
     */
    @Override
    public Set<String> getAjaxAreasToProcess() {
        return ajaxAreasToProcess;
    }

    /**
     * @param ajaxAreasToProcess the ajaxAreasToProcess to set
     */
    @Override
    public void setAjaxAreasToProcess(Set<String> ajaxAreasToProcess) {
        this.ajaxAreasToProcess = ajaxAreasToProcess;
    }

    /**
     * Add affected regions's ID to ajaxView component.
     *
     * @param component
     */
    public void addRegionsFromComponent(UIComponent component) {

        // First step - find parent ajax view
        Set<String> ajaxRegions = CoreAjaxRendererUtils.getAjaxAreas(component);

        // if (ajaxRegions == null){
        // FacesContext context = FacesContext.getCurrentInstance();
        // ajaxRegions = AjaxRendererUtils.getAbsoluteId(context,component);
        // }
        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getMessage(Messages.INVOKE_AJAX_REGION_LISTENER, component.getId()));
        }

        
        if (ajaxRegions != null) {
            ajaxAreasToRender.addAll(CoreRendererUtils.INSTANCE.findComponentsFor(FacesContext.getCurrentInstance(), component, ajaxRegions));
        }

        // Is that component limit to list ?
        if (Boolean.TRUE.equals(component.getAttributes().get("limitRender"))) {
            setLimitRender(true);
        }
    }

    /**
     * Add IDs of regions to process
     *
     * @see org.ajax4jsf.context.AjaxContext#addAreasToProcessFromComponent(javax.faces.component.UIComponent)
     */
    @Override
    public void addAreasToProcessFromComponent(FacesContext context, UIComponent component) {
        Set<String> areasToProcess = CoreAjaxRendererUtils.getAjaxAreasToProcess(component);

        if (areasToProcess != null) {
            Collection<String> convertedAreaIds = CoreRendererUtils.INSTANCE.findComponentsFor(context, component, areasToProcess);

            if (this.ajaxAreasToProcess == null) {
                this.ajaxAreasToProcess = new HashSet<String>(convertedAreaIds);
            } else {
                this.ajaxAreasToProcess.addAll(convertedAreaIds);
            }
        }
    }

    /**
     * @return Returns the ajaxRenderedAreas.
     */
    public Set<String> getAjaxRenderedAreas() {
        return ajaxRenderedAreas;
    }

    public void addRenderedArea(String id) {
        ajaxRenderedAreas.add(id);
    }

    public boolean removeRenderedArea(String id) {
        return ajaxRenderedAreas.remove(id);
    }

    /**
     * @return Returns the submittedClientId.
     */
    public String getSubmittedRegionClientId() {
        return this.submittedRegionClientId;
    }

    /**
     * @param submittedClientId The submittedClientId to set.
     */
    @Override
    public void setSubmittedRegionClientId(String submittedClientId) {
        this.submittedRegionClientId = submittedClientId;
    }

    /**
     * @return the ajaxSingleClientId
     */
    @Override
    public String getAjaxSingleClientId() {
        return ajaxSingleClientId;
    }

    /**
     * @param ajaxSingleClientId the ajaxSingleClientId to set
     */
    @Override
    public void setAjaxSingleClientId(String ajaxSingleClientId) {
        this.ajaxSingleClientId = ajaxSingleClientId;
    }

    /**
     * @return Returns the selfRender.
     */
    public boolean isSelfRender() {
        return selfRender;
    }

    /**
     * @param selfRender The selfRender to set.
     */
    public void setSelfRender(boolean selfRender) {
        this.selfRender = selfRender;
    }

    /**
     * @return the vievIdHolder
     */
    public ViewIdHolder getViewIdHolder() {
        return viewIdHolder;
    }

    /**
     * @param viewIdHolder the vievIdHolder to set
     */
    public void setViewIdHolder(ViewIdHolder viewIdHolder) {
        this.viewIdHolder = viewIdHolder;
    }

    /**
     * @return the responseData
     */
    public Object getResponseData() {
        return responseData;
    }

    /**
     * @param responseData the responseData to set
     */
    public void setResponseData(Object responseData) {
        this.responseData = responseData;
    }

    /**
     * @return the responseDataMap
     */
    public Map<String, Object> getResponseComponentDataMap() {
        return responseComponentDataMap;
    }

    /**
     * Gives back the writer of a Response object.
     *
     * @param extContext The external context.
     * @return The writer of the response.
     * @throws FacesException If the response object has no getWriter() method.
     */
    protected PrintWriter getWriter(ExternalContext extContext) throws FacesException {
        PrintWriter writer = null;
        Object response = extContext.getResponse();

        try {
            Method gW = response.getClass().getMethod("getWriter", new Class[0]);

            writer = (PrintWriter) gW.invoke(response, new Object[0]);
        } catch (Exception e) {
            throw new FacesException(e);
        }

        return writer;
    }

    public String getAjaxActionURL(FacesContext context) {

        // Check arguments
        if (null == context) {
            throw new NullPointerException("Faces context for build AJAX Action URL is null");
        }

        UIViewRoot viewRoot = context.getViewRoot();

        if (null == viewRoot) {
            throw new NullPointerException("Faces view tree for build AJAX Action URL is null");
        }

        String viewId = viewRoot.getViewId();

        if (null == viewId) {
            throw new NullPointerException("View id for build AJAX Action URL is null");
        }

        if (!viewId.startsWith("/")) {
            throw new IllegalArgumentException("Illegal view Id for build AJAX Action URL: " + viewId);
        }

        ViewHandler viewHandler = context.getApplication().getViewHandler();
        String actionURL = viewHandler.getActionURL(context, viewId);

        // HACK - check for a Jboss PortletBridge implementation. If present, append DirectLink attribute to url.
        // TODO - how to detect portlet application ?
        if (null != context.getExternalContext().getApplicationMap().get(
            "org.jboss.portletbridge.application.PortletStateHolder")) {

            // Mark Ajax action url as transparent with jsf-portlet bridge.
            actionURL = actionURL + ((actionURL.lastIndexOf('?') > 0) ? "&" : "?")
                + "javax.portlet.faces.DirectLink=true";
        }

        return context.getExternalContext().encodeActionURL(actionURL);
    }

    /**
     * @return the commonAjaxParameters
     */
    public Map<String, Object> getCommonAjaxParameters() {
        return commonAjaxParameters;
    }

    /**
     * @return the oncomplete
     */
    public Object getOncomplete() {
        return oncomplete;
    }

    /**
     * @param oncomplete the oncomplete to set
     */
    public void setOncomplete(Object oncomplete) {
        this.oncomplete = oncomplete;
    }

    public void appendOncomplete(Object onComplete) {
        StringBuilder res = new StringBuilder();
        if (this.oncomplete != null) {
            res.append(this.oncomplete);
            res.append(';');
        }

        if (onComplete != null) {
            res.append(onComplete).append(';');
        }
        
        this.oncomplete = res.toString();
    }

    /* (non-Javadoc)
    * @see org.ajax4jsf.context.AjaxContext#getOnbeforedomupdate()
    */
    @Override
    public Object getOnbeforedomupdate() {
        return onbeforedomupdate;
    }

    /* (non-Javadoc)
    * @see org.ajax4jsf.context.AjaxContext#setOnbeforedomupdate(java.lang.Object)
    */
    @Override
    public void setOnbeforedomupdate(Object onbeforedomupdateFunction) {
        this.onbeforedomupdate = onbeforedomupdateFunction;
    }
}

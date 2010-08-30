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
package org.richfaces.context;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.PartialViewContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseId;

import org.ajax4jsf.context.AjaxContext;
import org.ajax4jsf.renderkit.AjaxRendererUtils;
import org.richfaces.component.MetaComponentEncoder;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
public class PartialViewContextImpl extends PartialViewContext {

    private static final Logger LOG = RichfacesLogger.CONTEXT.getLogger();

    private static final String ORIGINAL_WRITER = "org.richfaces.PartialViewContextImpl.ORIGINAL_WRITER";

    private enum ContextMode {
        WRAPPED, DIRECT
    }

    private ContextMode contextMode = null;

    private FacesContext facesContext;

    private Collection<String> executeIds = null;
    private Collection<String> renderIds = null;

    private Boolean renderAll = null;

    private String activatorComponentId = null;
    private String behaviorEvent = null;

    private boolean released = false;

    private boolean limitRender = false;

    private PartialViewContext wrappedViewContext;

    public PartialViewContextImpl(PartialViewContext wrappedViewContext, FacesContext facesContext) {
        super();

        this.wrappedViewContext = wrappedViewContext;
        this.facesContext = facesContext;
    }

    @Override
    public Collection<String> getExecuteIds() {
        assertNotReleased();

        if (detectContextMode() == ContextMode.DIRECT) {
            if (executeIds == null) {
                executeIds = new LinkedHashSet<String>();
                setupExecuteIds(executeIds);
            }

            return executeIds;
        } else {
            return wrappedViewContext.getExecuteIds();
        }
    }

    @Override
    public Collection<String> getRenderIds() {
        assertNotReleased();

        if (detectContextMode() == ContextMode.DIRECT) {
            if (renderIds == null) {
                renderIds = new LinkedHashSet<String>();
            }

            return renderIds;
        } else {
            return wrappedViewContext.getRenderIds();
        }
    }

    @Override
    public boolean isAjaxRequest() {
        assertNotReleased();

        return wrappedViewContext.isAjaxRequest();
    }

    @Override
    public boolean isPartialRequest() {
        assertNotReleased();

        return wrappedViewContext.isPartialRequest();
    }

    @Override
    public void setPartialRequest(boolean isPartialRequest) {
        assertNotReleased();

        wrappedViewContext.setPartialRequest(isPartialRequest);
    }

    @Override
    public boolean isExecuteAll() {
        assertNotReleased();

        if (detectContextMode() == ContextMode.DIRECT) {
            return getExecuteIds().contains(AjaxRendererUtils.ALL);
        } else {
            return wrappedViewContext.isExecuteAll();
        }
    }

    @Override
    public boolean isRenderAll() {
        assertNotReleased();

        if (detectContextMode() == ContextMode.DIRECT) {
            if (renderAll != null) {
                return renderAll.booleanValue();
            }

            return getRenderIds().contains(AjaxRendererUtils.ALL);
        } else {
            return wrappedViewContext.isRenderAll();
        }
    }

    @Override
    public void setRenderAll(boolean isRenderAll) {
        assertNotReleased();

        if (detectContextMode() == ContextMode.DIRECT) {
            renderAll = isRenderAll;
        } else {
            wrappedViewContext.setRenderAll(isRenderAll);
        }
    }

    @Override
    public PartialResponseWriter getPartialResponseWriter() {
        return wrappedViewContext.getPartialResponseWriter();
    }

    @Override
    public void processPartial(PhaseId phaseId) {
        if (detectContextMode() == ContextMode.DIRECT && phaseId == PhaseId.RENDER_RESPONSE) {
            processPartialRenderPhase();
        } else {
            wrappedViewContext.processPartial(phaseId);
        }
    }

    protected void processPartialRenderPhase() {
        PartialViewContext pvc = facesContext.getPartialViewContext();
        UIViewRoot viewRoot = facesContext.getViewRoot();
        Collection<String> phaseIds = pvc.getRenderIds();
        setupRenderIds(phaseIds);

        try {
            PartialResponseWriter writer = pvc.getPartialResponseWriter();
            ResponseWriter orig = facesContext.getResponseWriter();
            facesContext.getAttributes().put(ORIGINAL_WRITER, orig);
            facesContext.setResponseWriter(writer);

            ExternalContext exContext = facesContext.getExternalContext();
            exContext.setResponseContentType("text/xml");
            exContext.addResponseHeader("Cache-Control", "no-cache");
            writer.startDocument();
            if (isRenderAll()) {
                renderAll(facesContext, viewRoot);
                renderState(facesContext);
                writer.endDocument();
                return;
            }

            // Skip this processing if "none" is specified in the render list,
            // or there were no render phase client ids.
            if ((phaseIds != null && !phaseIds.isEmpty()) ||
                (!limitRender && PartialViewContextAjaxOutputTracker.hasNestedAjaxOutputs(viewRoot))) {

                EnumSet<VisitHint> hints = EnumSet.of(VisitHint.SKIP_UNRENDERED);
                VisitContext visitContext = new ExtendedPartialVisitContext(facesContext, phaseIds, hints, limitRender);
                VisitCallback visitCallback = new RenderVisitCallback(facesContext);
                viewRoot.visitTree(visitContext, visitCallback);
            }

            renderState(facesContext);

            //TODO - render extensions for renderAll?
            renderExtensions(facesContext, viewRoot);

            writer.endDocument();
        } catch (IOException ex) {
            this.cleanupAfterView();
            //TODO - review?
            ex.printStackTrace();
        } catch (RuntimeException ex) {
            //TODO - review?
            this.cleanupAfterView();
            // Throw the exception
            throw ex;
        }
    }

    private void setupExecuteIds(Collection<String> ids) {
        ExecuteComponentCallback callback = new ExecuteComponentCallback(behaviorEvent);

        if (visitActivatorComponent(activatorComponentId, callback)) {
            ids.addAll(callback.getComponentIds());

            if (!ids.contains(AjaxRendererUtils.ALL)) {
                addImplicitExecuteIds(ids);
            }
        } else {
            //TODO - log or exception?
            //TODO - process default execute value
        }
    }

    private void setupRenderIds(Collection<String> ids) {
        if (!isRenderAll()) {
            RenderComponentCallback callback = new RenderComponentCallback(behaviorEvent);

            if (visitActivatorComponent(activatorComponentId, callback)) {
                ids.addAll(callback.getComponentIds());
                limitRender = callback.isLimitRender();

                if (!Boolean.TRUE.equals(renderAll) && !ids.contains(AjaxRendererUtils.ALL)) {
                    addImplicitRenderIds(ids, limitRender);

                    //TODO - review
                    AjaxContext ajaxContext = AjaxContext.getCurrentInstance();
                    ajaxContext.setOnbeforedomupdate(callback.getOnbeforedomupdate());
                    ajaxContext.appendOncomplete(callback.getOncomplete());
                    ajaxContext.setResponseData(callback.getData());
                }
            } else {
                //TODO - the same as for "execute"
            }
        }
    }

    private void renderAll(FacesContext context, UIViewRoot viewRoot) throws IOException {
        // If this is a "render all via ajax" request,
        // make sure to wrap the entire page in a <render> elemnt
        // with the special id of VIEW_ROOT_ID.  This is how the client
        // JavaScript knows how to replace the entire document with
        // this response.
        PartialViewContext pvc = context.getPartialViewContext();
        PartialResponseWriter writer = pvc.getPartialResponseWriter();
        writer.startUpdate(PartialResponseWriter.RENDER_ALL_MARKER);

        if (viewRoot.getChildCount() > 0) {
            for (UIComponent child : viewRoot.getChildren()) {
                child.encodeAll(context);
            }
        }

        writer.endUpdate();
    }

    private void renderState(FacesContext context) throws IOException {
        if (!context.getViewRoot().isTransient()) {
            // Get the view state and write it to the response..
            PartialViewContext pvc = context.getPartialViewContext();
            PartialResponseWriter writer = pvc.getPartialResponseWriter();
            writer.startUpdate(PartialResponseWriter.VIEW_STATE_MARKER);
            String state = context.getApplication().getStateManager().getViewState(context);
            writer.write(state);
            writer.endUpdate();
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see javax.faces.context.PartialViewContext#release()
	 */
    @Override
    public void release() {
        assertNotReleased();

        released = true;

        wrappedViewContext.release();
        wrappedViewContext = null;

        facesContext = null;

        renderAll = null;
        executeIds = null;
        renderIds = null;

        limitRender = false;

        activatorComponentId = null;
        behaviorEvent = null;
        contextMode = null;
    }

    protected void addImplicitExecuteIds(Collection<String> ids) {
        if (!ids.isEmpty()) {
            UIViewRoot root = facesContext.getViewRoot();
            if (root.getFacetCount() > 0) {
                if (root.getFacet(UIViewRoot.METADATA_FACET_NAME) != null) {
                    //TODO nick - does ordering matter?
                    ids.add(UIViewRoot.METADATA_FACET_NAME);
                    //ids.add(0, UIViewRoot.METADATA_FACET_NAME);
                }
            }
        }
    }

    protected void addImplicitRenderIds(Collection<String> ids, boolean limitRender) {
    }

    protected void renderExtensions(FacesContext context, UIComponent component) throws IOException {
        AjaxRendererUtils.renderAjaxExtensions(context, component);
    }

    private void assertNotReleased() {
        if (released) {
            throw new IllegalStateException("PartialViewContext already released!");
        }
    }

    private boolean visitActivatorComponent(String componentActivatorId, VisitCallback visitCallback) {
        Set<String> idsToVisit = Collections.singleton(componentActivatorId);
        Set<VisitHint> visitHints = EnumSet.of(VisitHint.SKIP_UNRENDERED);
        VisitContext visitContext = VisitContext.createVisitContext(facesContext, idsToVisit, visitHints);

        boolean visitResult = facesContext.getViewRoot().visitTree(visitContext, visitCallback);
        return visitResult;
    }

    private void cleanupAfterView() {
        ResponseWriter orig = (ResponseWriter) facesContext.getAttributes().get(ORIGINAL_WRITER);
        assert null != orig;
        // move aside the PartialResponseWriter
        facesContext.setResponseWriter(orig);
    }

    protected ContextMode detectContextMode() {
        if (contextMode == null) {
            Map<String, String> requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
            activatorComponentId = requestParameterMap.get(AjaxRendererUtils.AJAX_COMPONENT_ID_PARAMETER);

            if (activatorComponentId != null) {
                contextMode = ContextMode.DIRECT;
                behaviorEvent = requestParameterMap.get(AjaxRendererUtils.BEHAVIOR_EVENT_PARAMETER);
            } else {
                contextMode = ContextMode.WRAPPED;
            }
        }

        return contextMode;
    }

    private static final class RenderVisitCallback implements VisitCallback {

        private FacesContext ctx;

        private RenderVisitCallback(FacesContext ctx) {
            this.ctx = ctx;
        }

        private void logException(Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error(e.getMessage());
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug(e.getMessage(), e);
            }
        }

        /* (non-Javadoc)
         * @see javax.faces.component.visit.VisitCallback#visit(javax.faces.component.visit.VisitContext, javax.faces.component.UIComponent)
         */
        public VisitResult visit(VisitContext context, UIComponent target) {
            String metaComponentId = (String) ctx.getAttributes().get(ExtendedVisitContext.META_COMPONENT_ID);
            if (metaComponentId != null) {
                MetaComponentEncoder encoder = (MetaComponentEncoder) target;
                try {
                    encoder.encodeMetaComponent(ctx, metaComponentId);
                } catch (Exception e) {
                    logException(e);
                }
            } else {
                PartialResponseWriter writer = ctx.getPartialViewContext().getPartialResponseWriter();

                try {
                    writer.startUpdate(target.getClientId(ctx));
                    try {
                        // do the default behavior...
                        target.encodeAll(ctx);
                    } catch (Exception ce) {
                        logException(ce);
                    }

                    writer.endUpdate();
                } catch (IOException e) {
                    logException(e);
                }
            }
            
            // Once we visit a component, there is no need to visit
            // its children, since processDecodes/Validators/Updates and
            // encodeAll() already traverse the subtree.  We return
            // VisitResult.REJECT to supress the subtree visit.
            return VisitResult.REJECT;
        }
    }
}

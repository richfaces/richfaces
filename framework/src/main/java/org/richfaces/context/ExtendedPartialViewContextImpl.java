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

import static org.richfaces.renderkit.AjaxConstants.AJAX_COMPONENT_ID_PARAMETER;
import static org.richfaces.renderkit.AjaxConstants.ALL;
import static org.richfaces.renderkit.AjaxConstants.BEHAVIOR_EVENT_PARAMETER;

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

import org.ajax4jsf.javascript.ScriptUtils;
import org.richfaces.application.ServiceTracker;
import org.richfaces.component.MetaComponentEncoder;
import org.richfaces.javascript.JavaScriptService;
import org.richfaces.javascript.ScriptsHolder;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.renderkit.util.CoreAjaxRendererUtils;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
public class ExtendedPartialViewContextImpl extends ExtendedPartialViewContext {
    private static final Logger LOG = RichfacesLogger.CONTEXT.getLogger();
    private static final String ORIGINAL_WRITER = "org.richfaces.PartialViewContextImpl.ORIGINAL_WRITER";

    private enum ContextMode {
        WRAPPED,
        DIRECT
    }

    private ContextMode contextMode = null;
    private Collection<String> executeIds = null;
    private Collection<String> renderIds = null;
    private Collection<String> componentRenderIds = null;
    private Boolean renderAll = null;
    private String activatorComponentId = null;
    private String behaviorEvent = null;
    private boolean released = false;
    private boolean limitRender = false;
    private PartialViewContext wrappedViewContext;
    private String onbeforedomupdate;
    private String oncomplete;
    private Object responseData;

    public ExtendedPartialViewContextImpl(PartialViewContext wrappedViewContext, FacesContext facesContext) {
        super(facesContext);

        this.wrappedViewContext = wrappedViewContext;
    }

    @Override
    public Collection<String> getExecuteIds() {
        assertNotReleased();

        if (detectContextMode() == ContextMode.DIRECT) {
            if (executeIds == null) {
                executeIds = new LinkedHashSet<String>();
                visitActivatorAtExecute();
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
            return getExecuteIds().contains(ALL);
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

            return getRenderIds().contains(ALL);
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

    private boolean isProcessedExecutePhase(PhaseId phaseId) {
        return phaseId == PhaseId.APPLY_REQUEST_VALUES || phaseId == PhaseId.PROCESS_VALIDATIONS
            || phaseId == PhaseId.UPDATE_MODEL_VALUES;
    }

    @Override
    public void processPartial(PhaseId phaseId) {
        if (detectContextMode() == ContextMode.DIRECT) {
            if (phaseId == PhaseId.RENDER_RESPONSE) {
                processPartialRenderPhase();
            } else if (isProcessedExecutePhase(phaseId)) {
                processPartialExecutePhase(phaseId);
            }
        } else {
            wrappedViewContext.processPartial(phaseId);
        }
    }

    protected void processPartialExecutePhase(PhaseId phaseId) {
        FacesContext facesContext = getFacesContext();
        PartialViewContext pvc = facesContext.getPartialViewContext();
        Collection<String> executeIds = pvc.getExecuteIds();

        if (executeIds == null || executeIds.isEmpty()) {
            // TODO - review
            // if (phaseId == PhaseId.APPLY_REQUEST_VALUES) {
            // LOG.warn("Partial execute won't happen - executeIds were not specified");
            // }
            return;
        }

        try {
            executeComponents(phaseId, executeIds);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        if (phaseId == PhaseId.APPLY_REQUEST_VALUES) {
            // fix for MyFaces
            ExternalContext externalContext = facesContext.getExternalContext();
            externalContext.setResponseCharacterEncoding(externalContext.getRequestCharacterEncoding());

            PartialResponseWriter writer = pvc.getPartialResponseWriter();
            facesContext.setResponseWriter(writer);
        }
    }

    protected void executeComponents(PhaseId phaseId, Collection<String> executeIds) {
        FacesContext facesContext = getFacesContext();
        EnumSet<VisitHint> hints = EnumSet.of(VisitHint.SKIP_UNRENDERED);
        VisitContext visitContext = new ExecuteExtendedVisitContext(facesContext, executeIds, hints);
        PartialViewExecuteVisitCallback callback = new PartialViewExecuteVisitCallback(facesContext, phaseId);
        facesContext.getViewRoot().visitTree(visitContext, callback);
    }

    protected void processPartialRenderPhase() {
        FacesContext facesContext = getFacesContext();
        PartialViewContext pvc = facesContext.getPartialViewContext();
        UIViewRoot viewRoot = facesContext.getViewRoot();
        Collection<String> phaseIds = pvc.getRenderIds();
        visitActivatorAtRender(phaseIds);

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
            } else {
                // Skip this processing if "none" is specified in the render list,
                // or there were no render phase client ids.
                if ((phaseIds != null && !phaseIds.isEmpty())
                    || (!limitRender && PartialViewContextAjaxOutputTracker.hasNestedAjaxOutputs(viewRoot))) {

                    EnumSet<VisitHint> hints = EnumSet.of(VisitHint.SKIP_UNRENDERED);
                    VisitContext visitContext = new RenderExtendedVisitContext(facesContext, phaseIds, hints, limitRender);
                    VisitCallback visitCallback = new RenderVisitCallback(facesContext);
                    viewRoot.visitTree(visitContext, visitCallback);
                }

                renderState(facesContext);
            }

            addJavaScriptServicePageScripts(facesContext, viewRoot);
            // TODO - render extensions for renderAll?
            renderExtensions(facesContext, viewRoot);

            writer.endDocument();
        } catch (IOException ex) {
            this.cleanupAfterView();
            // TODO - review?
            ex.printStackTrace();
        } catch (RuntimeException ex) {
            // TODO - review?
            this.cleanupAfterView();
            // Throw the exception
            throw ex;
        }
    }

    private void setupExecuteCallbackData(ExecuteComponentCallback callback) {
        executeIds.addAll(callback.getExecuteIds());

        setupRenderCallbackData(callback);
    }

    private void setupRenderCallbackData(RenderComponentCallback callback) {
        componentRenderIds = callback.getRenderIds();
        onbeforedomupdate = callback.getOnbeforedomupdate();
        oncomplete = callback.getOncomplete();
        responseData = callback.getData();
        limitRender = callback.isLimitRender();
    }

    private void visitActivatorAtExecute() {
        ExecuteComponentCallback callback = new ExecuteComponentCallback(getFacesContext(), behaviorEvent);

        if (visitActivatorComponent(activatorComponentId, callback, EnumSet.noneOf(VisitHint.class))) {
            setupExecuteCallbackData(callback);

            if (!executeIds.contains(ALL)) {
                addImplicitExecuteIds(executeIds);
            }
        } else {
            // TODO - log or exception?
            // TODO - process default execute value
        }
    }

    private void visitActivatorAtRender(Collection<String> ids) {
        if (!isRenderAll()) {
            RenderComponentCallback callback = new RenderComponentCallback(getFacesContext(), behaviorEvent);

            if (visitActivatorComponent(activatorComponentId, callback, EnumSet.noneOf(VisitHint.class))) {
                setupRenderCallbackData(callback);
            } else {
                // TODO - the same as for "execute"
            }

            // take collection value stored during execute
            if (componentRenderIds != null) {
                ids.addAll(componentRenderIds);
            }

            if (!Boolean.TRUE.equals(renderAll) && !ids.contains(ALL)) {
                addImplicitRenderIds(ids, limitRender);

                appendOnbeforedomupdate(onbeforedomupdate);
                appendOncomplete(oncomplete);
                setResponseData(responseData);
            }
        }
    }

    private void renderAll(FacesContext context, UIViewRoot viewRoot) throws IOException {
        // If this is a "render all via ajax" request,
        // make sure to wrap the entire page in a <render> elemnt
        // with the special id of VIEW_ROOT_ID. This is how the client
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

    @Override
    public void release() {
        super.release();

        assertNotReleased();

        released = true;

        wrappedViewContext.release();
        wrappedViewContext = null;

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
            UIViewRoot root = getFacesContext().getViewRoot();
            if (root.getFacetCount() > 0) {
                if (root.getFacet(UIViewRoot.METADATA_FACET_NAME) != null) {
                    // TODO nick - does ordering matter?
                    ids.add(UIViewRoot.METADATA_FACET_NAME);
                    // ids.add(0, UIViewRoot.METADATA_FACET_NAME);
                }
            }
        }
    }

    protected void addImplicitRenderIds(Collection<String> ids, boolean limitRender) {
    }

    protected void addJavaScriptServicePageScripts(FacesContext context, UIComponent component) {
        ScriptsHolder scriptsHolder = ServiceTracker.getService(JavaScriptService.class).getScriptsHolder(context);
        StringBuilder scripts = new StringBuilder();
        for (Object script : scriptsHolder.getScripts()) {
            scripts.append(ScriptUtils.toScript(script));
            scripts.append(";");
        }
        for (Object script : scriptsHolder.getPageReadyScripts()) {
            scripts.append(ScriptUtils.toScript(script));
            scripts.append(";");
        }
        if (scripts.length() > 0) {
            scripts.append("RichFaces.javascriptServiceComplete();");
            prependOncomplete(scripts.toString());
        }
    }

    protected void renderExtensions(FacesContext context, UIComponent component) throws IOException {
        CoreAjaxRendererUtils.renderAjaxExtensions(context, component);
    }

    private void assertNotReleased() {
        if (released) {
            throw new IllegalStateException("PartialViewContext already released!");
        }
    }

    private boolean visitActivatorComponent(String componentActivatorId, VisitCallback visitCallback, Set<VisitHint> visitHints) {
        FacesContext facesContext = getFacesContext();

        Set<String> idsToVisit = Collections.singleton(componentActivatorId);
        VisitContext visitContext = new ExecuteExtendedVisitContext(facesContext, idsToVisit, visitHints);

        boolean visitResult = facesContext.getViewRoot().visitTree(visitContext, visitCallback);
        return visitResult;
    }

    private void cleanupAfterView() {
        FacesContext facesContext = getFacesContext();

        ResponseWriter orig = (ResponseWriter) facesContext.getAttributes().get(ORIGINAL_WRITER);
        assert null != orig;
        // move aside the PartialResponseWriter
        facesContext.setResponseWriter(orig);
    }

    protected ContextMode detectContextMode() {
        if (contextMode == null) {
            Map<String, String> requestParameterMap = getFacesContext().getExternalContext().getRequestParameterMap();
            activatorComponentId = requestParameterMap.get(AJAX_COMPONENT_ID_PARAMETER);

            if (activatorComponentId != null) {
                contextMode = ContextMode.DIRECT;
                behaviorEvent = requestParameterMap.get(BEHAVIOR_EVENT_PARAMETER);
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
                LOG.error(e.getMessage(), e);
            }
        }

        /*
         * (non-Javadoc)
         *
         * @see javax.faces.component.visit.VisitCallback#visit(javax.faces.component.visit.VisitContext,
         * javax.faces.component.UIComponent)
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
            // encodeAll() already traverse the subtree. We return
            // VisitResult.REJECT to supress the subtree visit.
            return VisitResult.REJECT;
        }
    }
}

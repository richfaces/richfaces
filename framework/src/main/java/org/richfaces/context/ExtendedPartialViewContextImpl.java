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

import static org.richfaces.ui.common.AjaxConstants.AJAX_COMPONENT_ID_PARAMETER;
import static org.richfaces.ui.common.AjaxConstants.ALL;
import static org.richfaces.ui.common.AjaxConstants.BEHAVIOR_EVENT_PARAMETER;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitContextFactory;
import javax.faces.component.visit.VisitHint;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.PartialViewContext;
import javax.faces.event.PhaseId;

import org.richfaces.javascript.JavaScriptService;
import org.richfaces.javascript.ScriptUtils;
import org.richfaces.javascript.ScriptsHolder;
import org.richfaces.services.ServiceTracker;
import org.richfaces.ui.common.HtmlConstants;
import org.richfaces.util.FastJoiner;

/**
 * <p>
 * The RichFaces custom version of PartialViewContext
 * </p>
 * <p>
 * Unfortunately, it is not possible to get the parameters value from inside PartialViewContextFactory (due to encoding issues),
 * so a RichFaces implementation wrapping the original context is created. It either delegates calls to the wrapped context (if
 * the request does not include ‘org.richfaces.ajax.component’ parameter), or it executes the necessary logic (for
 * RichFaces-initiated requests). The detectContextMode() method is responsible for this. Due to this issue we have to
 * re-implement big portion of PartialViewContext functionality in our class.
 * </p>
 * <p>
 * Important differences of RichFaces implementation:
 * </p>
 * <ul>
 * <li>Values are resolved in runtime by visiting the request activator component and evaluating attributes</li>
 * <li>Usage of extended visit contexts in order to support meta-components processing</li>
 * <li>Support for auto-updateable Ajax components</li>
 * <li>Support for Ajax extensions like passing data to the client</li>
 * </ul>
 *
 * @author Nick Belaevski
 */
public class ExtendedPartialViewContextImpl extends ExtendedPartialViewContext {

    private static final String EXTENSION_ID = "org.richfaces.extension";
    private static final String BEFOREDOMUPDATE_ELEMENT_NAME = "beforedomupdate";
    private static final String COMPLETE_ELEMENT_NAME = "complete";
    private static final String RENDER_ELEMENT_NAME = "render";
    private static final String DATA_ELEMENT_NAME = "data";
    private static final String COMPONENT_DATA_ELEMENT_NAME = "componentData";
    private static final FastJoiner SPACE_JOINER = FastJoiner.on(' ');

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

    private PartialViewContext wrappedViewContext;
    private PartialResponseWriter partialResponseWriter;

    private String onbeforedomupdate;
    private String oncomplete;
    private Object responseData;

    public ExtendedPartialViewContextImpl(PartialViewContext wrappedViewContext, FacesContext facesContext) {
        super(facesContext);

        this.wrappedViewContext = wrappedViewContext;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.context.PartialViewContextWrapper#getWrapped()
     */
    @Override
    public PartialViewContext getWrapped() {
        return wrappedViewContext;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.context.PartialViewContextWrapper#processPartial(javax.faces.event.PhaseId)
     */
    @Override
    public void processPartial(PhaseId phaseId) {
        detectContextMode();
        try {
            if (isProcessedExecutePhase(phaseId)) {
                super.setVisitMode(ExtendedVisitContextMode.EXECUTE);
            } else {
                super.setVisitMode(ExtendedVisitContextMode.RENDER);
                visitActivatorAtRender();
            }

            wrappedViewContext.processPartial(phaseId);

        } finally {
            super.resetVisitMode();
        }
    }

    private boolean isProcessedExecutePhase(PhaseId phaseId) {
        return phaseId == PhaseId.APPLY_REQUEST_VALUES || phaseId == PhaseId.PROCESS_VALIDATIONS
                || phaseId == PhaseId.UPDATE_MODEL_VALUES;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.context.PartialViewContextWrapper#getExecuteIds()
     */
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

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.context.PartialViewContextWrapper#getRenderIds()
     */
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

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.context.PartialViewContextWrapper#isExecuteAll()
     */
    @Override
    public boolean isExecuteAll() {
        assertNotReleased();

        if (detectContextMode() == ContextMode.DIRECT) {
            return getExecuteIds().contains(ALL);
        } else {
            return wrappedViewContext.isExecuteAll();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.context.PartialViewContextWrapper#isRenderAll()
     */
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

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.context.PartialViewContextWrapper#setRenderAll(boolean)
     */
    @Override
    public void setRenderAll(boolean isRenderAll) {
        assertNotReleased();
        renderAll = isRenderAll;
        super.setRenderAll(renderAll);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.context.PartialViewContextWrapper#getPartialResponseWriter()
     */
    @Override
    public PartialResponseWriter getPartialResponseWriter() {
        assertNotReleased();
        if (partialResponseWriter == null) {
            partialResponseWriter = new ExtensionWritingPartialResponseWriter(wrappedViewContext.getPartialResponseWriter());
        }
        return partialResponseWriter;
    }

    /**
     * Writes RichFaces extensions once document is ended
     */
    private class ExtensionWritingPartialResponseWriter extends PartialResponseWriterWrapper {

        public ExtensionWritingPartialResponseWriter(PartialResponseWriter wrapped) {
            super(wrapped);
        }

        @Override
        public void endDocument() throws IOException {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot = facesContext.getViewRoot();

            addJavaScriptServicePageScripts(facesContext);
            renderExtensions(facesContext, viewRoot);

            super.endDocument();
        }
    }

    /**
     * Visits activator component to collect attributes needed for execute phase
     */
    private void visitActivatorAtExecute() {
        final PartialViewContext pvc = getTopPartialViewContext();

        ExecuteComponentCallback callback = new ExecuteComponentCallback(getFacesContext(), behaviorEvent);

        if (visitActivatorComponent(activatorComponentId, callback, EnumSet.noneOf(VisitHint.class))) {
            setupExecuteCallbackData(callback);

            if (!pvc.isRenderAll()) {
                addImplicitExecuteIds(executeIds);
            }
        }
    }

    /**
     * Returns the top {@link PartialViewContext}, i.e. the first {@link PartialViewContext} in a chain of wrappers as accessible by {@link FacesContext#getPartialViewContext()}.
     *
     * Note: the top {@link PartialViewContext} doesn't have to be this instance
     */
    private PartialViewContext getTopPartialViewContext() {
        final FacesContext facesContext = getFacesContext();
        return facesContext.getPartialViewContext();
    }

    private void setupRenderCallbackData(RenderComponentCallback callback) {
        componentRenderIds = callback.getRenderIds();
        onbeforedomupdate = callback.getOnbeforedomupdate();
        oncomplete = callback.getOncomplete();
        responseData = callback.getData();
        super.setLimitRender(callback.isLimitRender());
    }

    private void setupExecuteCallbackData(ExecuteComponentCallback callback) {
        executeIds.addAll(callback.getExecuteIds());

        setupRenderCallbackData(callback);
    }

    /**
     * Visits activator component to collect attributes needed for render phase
     */
    private void visitActivatorAtRender() {
        if (!isRenderAll() && contextMode == ContextMode.DIRECT) {
            RenderComponentCallback callback = new RenderComponentCallback(getFacesContext(), behaviorEvent);

            if (visitActivatorComponent(activatorComponentId, callback, EnumSet.noneOf(VisitHint.class))) {
                setupRenderCallbackData(callback);
            } else {
                // TODO - the same as for "execute"
            }

            // take collection value stored during execute
            if (componentRenderIds != null) {
                renderIds.addAll(componentRenderIds);
            }

            if (!Boolean.TRUE.equals(renderAll) && !renderIds.contains(ALL)) {
                addImplicitRenderIds(renderIds, super.isLimitRender());

                appendOnbeforedomupdate(onbeforedomupdate);
                appendOncomplete(oncomplete);
                setResponseData(responseData);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.context.ExtendedPartialViewContext#release()
     */
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

        super.setLimitRender(false);

        activatorComponentId = null;
        behaviorEvent = null;
        contextMode = null;
    }

    /**
     * Adding implicitly executed areas to the list of component that should be executed
     */
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

    /**
     * Adding implicitly renderer areas to the list of component that should be rendered
     */
    protected void addImplicitRenderIds(Collection<String> renderIds, boolean limitRender) {
        if (!limitRender) {
            final FacesContext facesContext = getFacesContext();
            Collection<String> ajaxOutputs = PartialViewContextAjaxOutputTracker.getAjaxOutputs(facesContext, facesContext.getViewRoot());
            renderIds.addAll(ajaxOutputs);
        }
    }

    /**
     * Add scripts collected by {@link JavaScriptService} as a partial response extension.
     */
    protected void addJavaScriptServicePageScripts(FacesContext context) {
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

    /**
     * Render RichFaces specific extensions for partial-update such as:
     *
     * <ul>
     * <li>beforedumpdate</li>
     * <li>complete</li>
     * <li>render</li>
     * <li>data</li>
     * <li>componentData</li>
     * </ul>
     */
    protected void renderExtensions(FacesContext context, UIComponent component) throws IOException {
        ExtendedPartialViewContext partialContext = ExtendedPartialViewContext.getInstance(context);

        Map<String, String> attributes = Collections.singletonMap(HtmlConstants.ID_ATTRIBUTE, context.getExternalContext()
                .encodeNamespace(EXTENSION_ID));
        PartialResponseWriter writer = context.getPartialViewContext().getPartialResponseWriter();
        boolean[] writingState = new boolean[] { false };

        Object onbeforedomupdate = partialContext.getOnbeforedomupdate();
        if (onbeforedomupdate != null) {
            String string = onbeforedomupdate.toString();
            if (string.length() != 0) {
                startExtensionElementIfNecessary(writer, attributes, writingState);
                writer.startElement(BEFOREDOMUPDATE_ELEMENT_NAME, component);
                writer.writeText(onbeforedomupdate, null);
                writer.endElement(BEFOREDOMUPDATE_ELEMENT_NAME);
            }
        }

        Object oncomplete = partialContext.getOncomplete();
        if (oncomplete != null) {
            String string = oncomplete.toString();
            if (string.length() != 0) {
                startExtensionElementIfNecessary(writer, attributes, writingState);
                writer.startElement(COMPLETE_ELEMENT_NAME, component);
                writer.writeText(oncomplete, null);
                writer.endElement(COMPLETE_ELEMENT_NAME);
            }
        }

        if (!partialContext.getRenderIds().isEmpty()) {
            String renderIds = SPACE_JOINER.join(partialContext.getRenderIds());
            startExtensionElementIfNecessary(writer, attributes, writingState);
            writer.startElement(RENDER_ELEMENT_NAME, component);
            writer.writeText(renderIds, null);
            writer.endElement(RENDER_ELEMENT_NAME);
        }

        Object responseData = partialContext.getResponseData();
        if (responseData != null) {
            startExtensionElementIfNecessary(writer, attributes, writingState);
            writer.startElement(DATA_ELEMENT_NAME, component);

            AjaxDataSerializer serializer = ServiceTracker.getService(context, AjaxDataSerializer.class);
            writer.writeText(serializer.asString(responseData), null);

            writer.endElement(DATA_ELEMENT_NAME);
        }

        Map<String, Object> responseComponentDataMap = partialContext.getResponseComponentDataMap();
        if (responseComponentDataMap != null && !responseComponentDataMap.isEmpty()) {
            startExtensionElementIfNecessary(writer, attributes, writingState);
            writer.startElement(COMPONENT_DATA_ELEMENT_NAME, component);

            AjaxDataSerializer serializer = ServiceTracker.getService(context, AjaxDataSerializer.class);
            writer.writeText(serializer.asString(responseComponentDataMap), null);

            writer.endElement(COMPONENT_DATA_ELEMENT_NAME);
        }

        endExtensionElementIfNecessary(writer, writingState);
    }

    private void assertNotReleased() {
        if (released) {
            throw new IllegalStateException("PartialViewContext already released!");
        }
    }

    private boolean visitActivatorComponent(String componentActivatorId, VisitCallback visitCallback, Set<VisitHint> visitHints) {
        FacesContext facesContext = getFacesContext();

        Set<String> idsToVisit = Collections.singleton(componentActivatorId);


        try {
            setVisitMode(ExtendedVisitContextMode.EXECUTE);

            VisitContextFactory visitContextFactory = (VisitContextFactory) FactoryFinder
                    .getFactory(javax.faces.FactoryFinder.VISIT_CONTEXT_FACTORY);
            VisitContext visitContext = visitContextFactory.getVisitContext(facesContext, idsToVisit, visitHints);

            return facesContext.getViewRoot().visitTree(visitContext, visitCallback);
        } finally {
            super.resetVisitMode();
        }
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

    private static void startExtensionElementIfNecessary(PartialResponseWriter partialResponseWriter,
            Map<String, String> attributes, boolean[] writingState) throws IOException {

        if (!writingState[0]) {
            writingState[0] = true;

            partialResponseWriter.startExtension(attributes);
        }
    }

    private static void endExtensionElementIfNecessary(PartialResponseWriter partialResponseWriter, boolean[] writingState)
            throws IOException {

        if (writingState[0]) {
            writingState[0] = false;

            partialResponseWriter.endExtension();
        }
    }
}

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

package org.ajax4jsf.component;

import org.ajax4jsf.Messages;
import org.ajax4jsf.context.AjaxContext;
import org.ajax4jsf.context.InvokerCallback;
import org.ajax4jsf.context.ViewIdHolder;
import org.ajax4jsf.event.AjaxListener;
import org.ajax4jsf.event.EventsQueue;
import org.ajax4jsf.renderkit.AjaxContainerRenderer;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.log.Logger;

import javax.el.MethodExpression;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.render.Renderer;
import javax.faces.webapp.FacesServlet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Custom ViewRoot for support render parts of tree for Ajax requests. Main
 * difference from default ViewRoot - store events queue and unique id counter
 * in request-scope variables. In common realisation, store request-scope
 * variables in component produce errors in case of concurrent requests to same
 * view.
 *
 * @author asmirnov@exadel.com (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.4 $ $Date: 2007/02/28 17:01:01 $
 */
public class AjaxViewRoot extends UIViewRoot implements AjaxContainer {
    public static final String ROOT_ID = "_viewRoot";
    private static final Logger LOG = RichfacesLogger.COMPONENTS.getLogger();

    /**
     * Use own {@link PhaseListener}'s list, to use with AJAX processing.
     * phases.
     */
    private List<PhaseListener> phaseListeners = null;
    private EventsQueue ajaxEvents = new EventsQueue();
    private InvokerCallback validatorsInvoker = new InvokerCallback() {
        public void invokeContextCallback(FacesContext context, UIComponent component) {
            component.processValidators(context);
        }

        public void invokeRoot(FacesContext context) {
        }
    };
    private InvokerCallback updatesInvoker = new InvokerCallback() {
        public void invokeContextCallback(FacesContext context, UIComponent component) {
            component.processUpdates(context);
        }

        public void invokeRoot(FacesContext context) {
        }
    };
    private InvokerCallback decodeInvoker = new InvokerCallback() {
        public void invokeContextCallback(FacesContext context, UIComponent component) {
            component.processDecodes(context);
        }

        public void invokeRoot(FacesContext context) {
            decode(context);
        }
    };

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.UIViewRoot#encodeBegin(javax.faces.context.FacesContext)
     */

    // public void encodeBegin(FacesContext context) throws IOException {
    // UIComponent submittedComponent = getSubmittedRegion(context);
    // if(null == submittedComponent){
    // super.encodeBegin(context);
    // } else {
    // submittedComponent.encodeBegin(context);
    // }
    // }
    private ContextCallback ajaxInvoker = new ContextCallback() {
        public void invokeContextCallback(FacesContext context, UIComponent component) {
            try {
                if (component instanceof AjaxContainer) {
                    AjaxContainer ajax = (AjaxContainer) component;

                    ajax.encodeAjax(context);
                } else {

                    // Container not found, use Root for encode.
                    encodeAjax(context);
                }
            } catch (IOException e) {
                throw new FacesException(e);
            }
        }
    };
    private AjaxRegionBrige brige;
    private EventsQueue[] events;
    private Lifecycle lifecycle;

    /**
     *
     */
    public AjaxViewRoot() {
        super();
        super.setId(ROOT_ID);
        brige = new AjaxRegionBrige(this);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.UIComponentBase#getId()
     */

    // public String getId() {
    // return ROOT_ID;
    // }

    public String getRendererType() {
        return COMPONENT_FAMILY;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.framework.ajax.AjaxViewBrige#broadcast(javax.faces.event.FacesEvent)
     */
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        super.broadcast(event);
        getBrige().broadcast(event);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.framework.ajax.AjaxViewBrige#getAjaxListener()
     */
    public MethodExpression getAjaxListener() {
        return getBrige().getAjaxListener();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.framework.ajax.AjaxViewBrige#isImmediate()
     */
    public boolean isImmediate() {
        return getBrige().isImmediate();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.framework.ajax.AjaxViewBrige#isSubmitted()
     */
    public boolean isSubmitted() {
        return getBrige().isSubmitted();
    }

    public void removePhaseListener(PhaseListener toRemove) {
        if (null != phaseListeners) {
            phaseListeners.remove(toRemove);
        }

        super.removePhaseListener(toRemove);
    }

    public void addPhaseListener(PhaseListener newPhaseListener) {
        if (null == phaseListeners) {
            phaseListeners = new ArrayList<PhaseListener>();
        }

        phaseListeners.add(newPhaseListener);
        super.addPhaseListener(newPhaseListener);
    }

    /**
     * Send notification to a view-scope phase listeners.
     *
     * @param context
     * @param phase
     * @param before
     */
    protected void processPhaseListeners(FacesContext context, PhaseId phase, boolean before) {
        MethodExpression listenerExpression = before ? getBeforePhaseListener() : getAfterPhaseListener();
        PhaseEvent event = null;

        if (null != listenerExpression) {
            event = createPhaseEvent(context, phase);
            listenerExpression.invoke(context.getELContext(), new Object[]{event});
        }

        if (null != this.phaseListeners) {
            for (PhaseListener listener : phaseListeners) {
                PhaseId phaseId = listener.getPhaseId();

                if ((phaseId == phase) || (phaseId == PhaseId.ANY_PHASE)) {
                    if (null == event) {
                        event = createPhaseEvent(context, phase);
                    }

                    if (before) {
                        listener.beforePhase(event);
                    } else {
                        listener.afterPhase(event);
                    }
                }
            }
        }
    }

    protected PhaseEvent createPhaseEvent(FacesContext context, PhaseId phaseId) throws FacesException {
        if (lifecycle == null) {
            LifecycleFactory lifecycleFactory =
                (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
            String lifecycleId = context.getExternalContext().getInitParameter(FacesServlet.LIFECYCLE_ID_ATTR);

            if (lifecycleId == null) {
                lifecycleId = LifecycleFactory.DEFAULT_LIFECYCLE;
            }

            lifecycle = lifecycleFactory.getLifecycle(lifecycleId);
        }

        return new PhaseEvent(context, phaseId, lifecycle);
    }

    protected void processPhase(FacesContext context, PhaseId phase, InvokerCallback callback) {

        // Process phase listeners before phase.
        processPhaseListeners(context, phase, true);

        // Process phase. Run callback method by invokeOnComponent for a
        // submitted region.
        if (!((null == callback) || context.getRenderResponse() || context.getResponseComplete())) {
            AjaxContext ajaxContext = AjaxContext.getCurrentInstance(context);
            String submittedRegionClientId = ajaxContext.getSubmittedRegionClientId();

            if (ajaxContext.isAjaxRequest() && (submittedRegionClientId != null)
                && !submittedRegionClientId.equals(ROOT_ID)
                && !submittedRegionClientId.equals(getClientId(context))) {
                invokeOnComponent(context, submittedRegionClientId, new InvokerCallbackWrapper(callback));
            } else {

                // For a root region, call invokeRoot method, then process all
                // facets and children by invoke method.
                try {
                    callback.invokeRoot(context);
                } catch (RuntimeException e) {
                    context.renderResponse();

                    throw e;
                }

                String ajaxSingleClientId = ajaxContext.getAjaxSingleClientId();

                if (null == ajaxSingleClientId) {
                    for (Iterator<UIComponent> iter = getFacetsAndChildren(); iter.hasNext();) {
                        callback.invokeContextCallback(context, iter.next());
                    }
                } else {
                    InvokerCallback invokerCallback = new InvokerCallbackWrapper(callback);

                    invokeOnComponent(context, ajaxSingleClientId, invokerCallback);

                    Set<String> areasToProcess = ajaxContext.getAjaxAreasToProcess();

                    if (null != areasToProcess) {
                        for (String areaId : areasToProcess) {
                            invokeOnComponent(context, areaId, invokerCallback);
                        }
                    }
                }
            }
        }

        // Broadcast phase events.
        broadcastEvents(context, phase);

        // Process afterPhase listeners.
        processPhaseListeners(context, phase, false);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.UIComponent#queueEvent(javax.faces.event.FacesEvent)
     */
    public void queueEvent(FacesEvent event) {
        if (event == null) {
            throw new NullPointerException(Messages.getMessage(Messages.NULL_EVENT_SUBMITTED_ERROR));
        }

        if (event.getPhaseId().compareTo(PhaseId.RENDER_RESPONSE) == 0) {

            // HACK - Special case - Ajax Events to RenderResponse phase
            // queue.
            getAjaxEventsQueue().offer(event);
        } else {
            getEventsQueue(event.getPhaseId()).offer(event);
        }
    }

    /**
     * Broadcast events for specified Phase
     *
     * @param context
     * @param phaseId -
     *                phase, for which events must be processed.
     */
    public void broadcastEvents(FacesContext context, PhaseId phaseId) {
        EventsQueue[] eWebXmlvents = getEvents();
        EventsQueue anyPhaseEvents = events[PhaseId.ANY_PHASE.getOrdinal()];
        EventsQueue phaseEvents = events[phaseId.getOrdinal()];

        if (phaseEvents.isEmpty() && anyPhaseEvents.isEmpty()) {
            return;
        }

        // FacesEvent event = null;
        boolean haveAnyPhaseEvents = !anyPhaseEvents.isEmpty();
        boolean havePhaseEvents = !phaseEvents.isEmpty();

        do {

            // ANY_PHASE first
            processEvents(context, anyPhaseEvents, haveAnyPhaseEvents);
            processEvents(context, phaseEvents, havePhaseEvents);

            // Events can queued in other events processing
            haveAnyPhaseEvents = !anyPhaseEvents.isEmpty();
            havePhaseEvents = !phaseEvents.isEmpty();
        } while (haveAnyPhaseEvents || havePhaseEvents);

        if (context.getRenderResponse() || context.getResponseComplete()) {
            clearEvents();
        }
    }

    /**
     * @param context          TODO
     * @param phaseEventsQueue
     * @param havePhaseEvents
     */
    public void processEvents(FacesContext context, EventsQueue phaseEventsQueue, boolean havePhaseEvents) {
        FacesEvent event;

        while (havePhaseEvents) {
            try {
                event = (FacesEvent) phaseEventsQueue.remove();

                UIComponent source = event.getComponent();

                try {
                    source.broadcast(event);
                } catch (AbortProcessingException e) {
                    if (LOG.isErrorEnabled()) {
                        UIComponent component = event.getComponent();
                        String id = (null != component) ? component.getClientId(context) : "";

                        LOG.error("Error processing faces event for the component " + id, e);
                    }
                }
            } catch (NoSuchElementException e) {
                havePhaseEvents = false;
            }
        }
    }

    public void broadcastAjaxEvents(FacesContext context) {
        EventsQueue queue = getAjaxEventsQueue();

        processEvents(context, queue, !queue.isEmpty());
    }

    /**
     * Use FIFO buffers for hold Faces Events. Hold this buffers in Request
     * scope parameters for support any concurrent requests for same component
     * tree ( since RI hold component tree in session ).
     *
     * @param phase
     * @return
     */
    protected EventsQueue getEventsQueue(PhaseId phase) {
        return getEvents()[phase.getOrdinal()];
    }

    /**
     * @return
     */
    protected EventsQueue[] getEvents() {
        if (events == null) {
            clearEvents();
        }

        return events;
    }

    /**
     * Special Fifo Buffer for ajax events to Render Responce phase.
     *
     * @return
     */
    protected EventsQueue getAjaxEventsQueue() {
        return ajaxEvents;
    }

    public void clearEvents() {
        int len = PhaseId.VALUES.size();

        events = new EventsQueue[len];

        for (int i = 0; i < len; i++) {
            events[i] = new EventsQueue();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.framework.ajax.AjaxViewBrige#processDecodes(javax.faces.context.FacesContext)
     */
    public void processDecodes(FacesContext context) {
        if (context == null) {
            throw new NullPointerException("context");
        }

        processPhase(context, PhaseId.APPLY_REQUEST_VALUES, decodeInvoker);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.framework.ajax.AjaxViewBrige#processUpdates(javax.faces.context.FacesContext)
     */
    public void processUpdates(FacesContext context) {
        if (context == null) {
            throw new NullPointerException("context");
        }

        processPhase(context, PhaseId.UPDATE_MODEL_VALUES, updatesInvoker);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.framework.ajax.AjaxViewBrige#processValidators(javax.faces.context.FacesContext)
     */
    public void processValidators(FacesContext context) {
        if (context == null) {
            throw new NullPointerException("context");
        }

        processPhase(context, PhaseId.PROCESS_VALIDATIONS, validatorsInvoker);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.UIViewRoot#processApplication(javax.faces.context.FacesContext)
     */
    public void processApplication(FacesContext context) {
        if (context == null) {
            throw new NullPointerException("context");
        }

        processPhase(context, PhaseId.INVOKE_APPLICATION, null);
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        processPhaseListeners(context, PhaseId.RENDER_RESPONSE, true);

        // Copy/paste from UIComponentBase, so in no way for java to call super.super method.
        String rendererType = getRendererType();

        if (rendererType != null) {
            Renderer renderer = this.getRenderer(context);

            if (renderer != null) {
                renderer.encodeBegin(context, this);
            } else {

                // TODO: log
            }
        }
    }

    @Override
    public void encodeEnd(FacesContext context) throws IOException {

        // Copy/paste from UIComponentBase, so in no way for java to call super.super method.
        String rendererType = getRendererType();

        if (rendererType != null) {
            Renderer renderer = this.getRenderer(context);

            if (renderer != null) {
                renderer.encodeEnd(context, this);
            } else {

                // TODO: log
            }
        }

        processPhaseListeners(context, PhaseId.RENDER_RESPONSE, false);

//      super.encodeEnd(context);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.UIComponentBase#encodeChildren(javax.faces.context.FacesContext)
     */
    public void encodeChildren(FacesContext context) throws IOException {
        AjaxContext ajaxContext = AjaxContext.getCurrentInstance(context);

        if (ajaxContext.isAjaxRequest()) {
            String submittedRegionClientId = ajaxContext.getSubmittedRegionClientId();
            boolean invoked = false;

            if ((submittedRegionClientId != null) && !submittedRegionClientId.equals(ROOT_ID)
                && !submittedRegionClientId.equals(getClientId(context))) {
                invoked = invokeOnComponent(context, submittedRegionClientId, ajaxInvoker);
            }

            // if container not found, use Root for encode.
            // https://jira.jboss.org/jira/browse/RF-3975
            if (!invoked) {
                encodeAjax(context);
            }
        } else {
            super.encodeChildren(context);
        }
    }

    @SuppressWarnings("unchecked")
    public void restoreState(FacesContext context, Object state) {
        Object[] mystate = (Object[]) state;

        super.restoreState(context, mystate[0]);
        getBrige().restoreState(context, mystate[1]);

        Object listeners = restoreAttachedState(context, mystate[2]);

        if (null != listeners) {
            phaseListeners = (List<PhaseListener>) listeners;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.framework.ajax.AjaxViewBrige#saveState(javax.faces.context.FacesContext)
     */
    public Object saveState(FacesContext context) {
        Object[] state = new Object[3];

        state[0] = super.saveState(context);
        state[1] = getBrige().saveState(context);
        state[2] = saveAttachedState(context, phaseListeners);

        return state;
    }

    public String getViewId() {
        ViewIdHolder viewIdHolder = AjaxContext.getCurrentInstance().getViewIdHolder();
        String viewId;

        if (null != viewIdHolder) {
            viewId = viewIdHolder.getViewId();
        } else {
            viewId = super.getViewId();
        }

        return viewId;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.framework.ajax.AjaxViewBrige#setAjaxListener(javax.faces.el.MethodBinding)
     */
    public void setAjaxListener(MethodExpression ajaxListener) {
        getBrige().setAjaxListener(ajaxListener);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.framework.ajax.AjaxViewBrige#setImmediate(boolean)
     */
    public void setImmediate(boolean immediate) {
        getBrige().setImmediate(immediate);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.framework.ajax.AjaxViewBrige#setSubmitted(boolean)
     */
    public void setSubmitted(boolean submitted) {
        getBrige().setSubmitted(submitted);
    }

    public void addAjaxListener(AjaxListener listener) {
        addFacesListener(listener);
    }

    public AjaxListener[] getAjaxListeners() {
        return (AjaxListener[]) getFacesListeners(AjaxListener.class);
    }

    public void removeAjaxListener(AjaxListener listener) {
        removeFacesListener(listener);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.framework.ajax.AjaxViewBrige#isSelfRendered()
     */
    public boolean isSelfRendered() {
        return false; // _brige.isSelfRendered();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.framework.ajax.AjaxViewBrige#setSelfRendered(boolean)
     */
    public void setSelfRendered(boolean selfRendered) {

        // _brige.setSelfRendered(selfRendered);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.UIComponentBase#getRendersChildren()
     */
    public boolean getRendersChildren() {
        FacesContext context = FacesContext.getCurrentInstance();

        // For non Ajax request, view root not render children
        if (!AjaxContext.getCurrentInstance(context).isAjaxRequest()) {
            return false;
        }

        // Ajax Request. Control all output.
        return true;
    }

    public boolean isRenderRegionOnly() {

        // for viewroot it not applicable.
        return false;
    }

    public void setRenderRegionOnly(boolean reRenderPage) {

        // Ignore for a ViewRoot.
    }

    public void encodeAjax(FacesContext context) throws IOException {
        String rendererType = getRendererType();

        if (rendererType != null) {
            ((AjaxContainerRenderer) getRenderer(context)).encodeAjax(context, this);
        }
    }

    /**
     * @return the brige
     */
    protected AjaxRegionBrige getBrige() {
        return brige;
    }
}

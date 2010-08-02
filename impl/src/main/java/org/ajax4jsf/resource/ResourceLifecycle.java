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

package org.ajax4jsf.resource;

import org.richfaces.log.RichfacesLogger;
import org.slf4j.Logger;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.render.RenderKitFactory;
import java.io.IOException;
import java.util.Locale;

/**
 * Lifecycle for simulate faces request processing for resource.
 *
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:56:56 $
 */
public class ResourceLifecycle extends Lifecycle {
    private static final Logger LOG = RichfacesLogger.RESOURCE.getLogger();
    private Lifecycle lifecycle;

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.lifecycle.Lifecycle#addPhaseListener(javax.faces.event.PhaseListener)
     */
    @Override
    public void addPhaseListener(PhaseListener arg0) {

        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.lifecycle.Lifecycle#execute(javax.faces.context.FacesContext)
     */
    @Override
    public void execute(FacesContext arg0) throws FacesException {

        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.lifecycle.Lifecycle#getPhaseListeners()
     */
    @Override
    public PhaseListener[] getPhaseListeners() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.lifecycle.Lifecycle#removePhaseListener(javax.faces.event.PhaseListener)
     */
    @Override
    public void removePhaseListener(PhaseListener arg0) {

        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.lifecycle.Lifecycle#render(javax.faces.context.FacesContext)
     */
    @Override
    public void render(FacesContext arg0) throws FacesException {

        // TODO Auto-generated method stub
    }

    /**
     * @param context
     * @param resource
     * @throws IOException
     */
    public void send(ResourceContext resourceContext, InternetResource resource) throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        if (null != facesContext) {
            Lifecycle facesLifecycle = getFacesLifecycle();
            PhaseListener[] phaseListeners = facesLifecycle.getPhaseListeners();
            PhaseEvent restoreViewEvent = new PhaseEvent(facesContext, PhaseId.RESTORE_VIEW, this);

            processPhaseListeners(phaseListeners, restoreViewEvent, true);

            // Fix for a http://jira.jboss.org/jira/browse/RF-1056
            if (facesContext.getResponseComplete()) {
                return;
            }

            // fix for a http://jira.jboss.com/jira/browse/RF-1064 .
            // viewRoot can be created outside.
            UIViewRoot savedViewRoot = facesContext.getViewRoot();

            try {

                // create "dummy" viewRoot, to avoid problems in phase listeners.
                UIViewRoot root = new UIViewRoot();
                String key = resource.getKey();

                if ((null != key) && !key.startsWith("/")) {
                    key = "/" + key;
                }

                root.setViewId(key);
                root.setLocale(Locale.getDefault());
                root.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);
                facesContext.setViewRoot(root);

                // Invoke after restore view phase listeners
                processPhaseListeners(phaseListeners, restoreViewEvent, false);

                // Fix for a http://jira.jboss.org/jira/browse/RF-1056
                if (!facesContext.getResponseComplete()) {

                    // Invoke before render view phase listeners
                    PhaseEvent renderViewEvent = new PhaseEvent(facesContext, PhaseId.RENDER_RESPONSE, this);

                    processPhaseListeners(phaseListeners, renderViewEvent, true);
                    sendResource(resourceContext, resource);
                    processPhaseListeners(phaseListeners, renderViewEvent, false);
                }
            } finally {
                if (null != savedViewRoot) {
                    facesContext.setViewRoot(savedViewRoot);
                }
            }
        } else {
            sendResource(resourceContext, resource);
        }
    }

    /**
     * Send phase event to all apropriate PhaseListener's
     *
     * @param phaseListeners
     * @param phaseEvent
     * @param beforePhase    TODO
     */
    private void processPhaseListeners(PhaseListener[] phaseListeners, PhaseEvent phaseEvent, boolean beforePhase) {
        if (beforePhase) {

            // Invoke before phase listeners
            for (int i = 0; i < phaseListeners.length; i++) {
                PhaseListener phaseListener = phaseListeners[i];

                invokePhaseListener(phaseListener, phaseEvent, beforePhase);
            }
        } else {

            // Invoke after phase listeners, in reverse order.
            for (int i = phaseListeners.length - 1; i >= 0; i--) {
                PhaseListener phaseListener = phaseListeners[i];

                invokePhaseListener(phaseListener, phaseEvent, beforePhase);
            }
        }
    }

    /**
     * @param phaseListener
     * @param phaseEvent
     * @param beforePhase
     */
    private void invokePhaseListener(PhaseListener phaseListener, PhaseEvent phaseEvent, boolean beforePhase) {
        if (phaseEvent.getPhaseId().equals(phaseListener.getPhaseId())
            || PhaseId.ANY_PHASE.equals(phaseListener.getPhaseId())) {
            try {
                if (beforePhase) {
                    phaseListener.beforePhase(phaseEvent);
                } else {
                    phaseListener.afterPhase(phaseEvent);
                }
            } catch (Exception e) {
                LOG.debug("Exception in PhaseListener, phase :" + phaseEvent.getPhaseId().toString()
                    + (beforePhase ? " : beforePhase" : " : afterPhase"), e);
            }
        }
    }

    /**
     * @param resourceContext
     * @param resource
     * @throws IOException
     */
    private void sendResource(ResourceContext resourceContext, InternetResource resource) throws IOException {
        resource.sendHeaders(resourceContext);
        resource.send(resourceContext);
    }

    protected synchronized Lifecycle getFacesLifecycle() {
        if (lifecycle == null) {

            // Acquire our Lifecycle instance
            LifecycleFactory lifecycleFactory =
                (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);

            lifecycle = lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
        }

        return lifecycle;
    }
}

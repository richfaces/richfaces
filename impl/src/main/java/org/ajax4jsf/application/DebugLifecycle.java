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

package org.ajax4jsf.application;

import org.richfaces.log.RichfacesLogger;
import org.richfaces.log.Logger;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;

/**
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:57:12 $
 */
public class DebugLifecycle extends Lifecycle implements PhaseListener {
    public static final String DEBUG_LYFECYCLE_ID = "DEBUG";
    public static final String LIFECYCLE_ID_ATTR = "javax.faces.LIFECYCLE_ID";
    public static final String PHASE_ID_PARAM = "org.ajax4jsf.CURRENT_PHASE";
    private static final long serialVersionUID = -3247965217553145312L;
    private static final Logger LOG = RichfacesLogger.APPLICATION.getLogger();
    private static final DebugOutputMaker DEBUG_OUTPUT = new DebugOutputMaker();
    private Lifecycle defaultLifecycle;

    /**
     * @param default1
     */
    public DebugLifecycle(Lifecycle default1) {
        super();

        // TODO Auto-generated constructor stub
        this.defaultLifecycle = default1;
    }

    /**
     *
     */
    public DebugLifecycle(LifecycleFactory defaultFactory) {
        super();
        defaultLifecycle = defaultFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
        defaultLifecycle.addPhaseListener(this);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.lifecycle.Lifecycle#addPhaseListener(javax.faces.event.PhaseListener)
     */
    public void addPhaseListener(PhaseListener arg0) {
        defaultLifecycle.addPhaseListener(arg0);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.lifecycle.Lifecycle#execute(javax.faces.context.FacesContext)
     */
    public void execute(FacesContext context) throws FacesException {
        try {
            defaultLifecycle.execute(context);

//          TODO Refactoring:       } catch (Throwable e) {
        } catch (FacesException e) {

//          DebugOutputMaker debugOutput = new DebugOutputMaker();
            DEBUG_OUTPUT.writeErrorMessage(context, e, "execute");
            context.responseComplete();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.lifecycle.Lifecycle#getPhaseListeners()
     */
    public PhaseListener[] getPhaseListeners() {

        // TODO Auto-generated method stub
        return defaultLifecycle.getPhaseListeners();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.lifecycle.Lifecycle#removePhaseListener(javax.faces.event.PhaseListener)
     */
    public void removePhaseListener(PhaseListener arg0) {
        defaultLifecycle.removePhaseListener(arg0);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.lifecycle.Lifecycle#render(javax.faces.context.FacesContext)
     */
    public void render(FacesContext context) throws FacesException {
        try {
            defaultLifecycle.render(context);

//          TODO Refactoring:        } catch (Throwable e) {
        } catch (FacesException e) {

//          DebugOutputMaker debugOutput = new DebugOutputMaker();
            DEBUG_OUTPUT.writeErrorMessage(context, e, "render");
            context.responseComplete();
        }
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.event.PhaseListener#afterPhase(javax.faces.event.PhaseEvent)
     */
    public void afterPhase(PhaseEvent event) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("End phase " + event.getPhaseId().toString());
        }
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.event.PhaseListener#beforePhase(javax.faces.event.PhaseEvent)
     */
    public void beforePhase(PhaseEvent event) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start phase " + event.getPhaseId().toString());
        }

        event.getFacesContext().getExternalContext().getRequestMap().put(PHASE_ID_PARAM, event.getPhaseId());
    }

    /*
     *  (non-Javadoc)
     * @see javax.faces.event.PhaseListener#getPhaseId()
     */
    public PhaseId getPhaseId() {

        // TODO Auto-generated method stub
        return PhaseId.ANY_PHASE;
    }
}

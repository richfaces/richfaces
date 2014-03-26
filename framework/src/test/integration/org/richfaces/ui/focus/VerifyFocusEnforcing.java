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

package org.richfaces.ui.focus;

import static org.junit.Assert.assertTrue;

import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.PreRenderViewEvent;

import org.jboss.arquillian.warp.jsf.AfterPhase;
import org.jboss.arquillian.warp.jsf.BeforePhase;
import org.jboss.arquillian.warp.jsf.Phase;
import org.richfaces.services.ServiceTracker;
import org.richfaces.focus.FocusManager;
import org.richfaces.ui.misc.focus.FocusRendererUtils;

public class VerifyFocusEnforcing extends AbstractComponentAssertion implements ComponentSystemEventListener {

    private static final long serialVersionUID = 1L;

    private String enforceFocusId;

    /**
     * @param enforceFocusId clientId of input component to be enforced to gain focus
     */
    public VerifyFocusEnforcing(String enforceFocusId) {
        this.enforceFocusId = enforceFocusId;
    }

    @BeforePhase(Phase.RENDER_RESPONSE)
    public void subscribe_to_preRenderViewEvent() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getViewRoot().subscribeToEvent(PreRenderViewEvent.class, this);
    }

    @AfterPhase(Phase.RENDER_RESPONSE)
    public void verify_focus_was_enforced() {
        FacesContext context = FacesContext.getCurrentInstance();
        assertTrue(FocusRendererUtils.isFocusEnforced(context));
    }

    @Override
    public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {
        if (event instanceof PreRenderViewEvent) {
            FacesContext context = FacesContext.getCurrentInstance();
            FocusManager focusManager = ServiceTracker.getService(context, FocusManager.class);
            focusManager.focus(enforceFocusId);
        }
    }
}

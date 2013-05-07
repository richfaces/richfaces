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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.jboss.arquillian.warp.jsf.AfterPhase;
import org.jboss.arquillian.warp.jsf.BeforePhase;
import org.jboss.arquillian.warp.jsf.Phase;
import org.richfaces.ui.misc.focus.AbstractFocus;
import org.richfaces.ui.misc.focus.FocusRendererBase;

import com.google.common.base.Splitter;

public class VerifyFocusCandidates extends AbstractComponentAssertion {

    private static final long serialVersionUID = 1L;

    private String invalidatedComponents;
    private String expectedFocusCandidates;
    private String message;

    /**
     *
     * @param message message to be thrown during focus candidates verification
     * @param invalidatedComponents space separated list of components to invalidate
     * @param focusCandidates candidates for gaining focus
     */
    public VerifyFocusCandidates(String message, String invalidatedComponents, String focusCandidates) {
        this.invalidatedComponents = invalidatedComponents;
        this.expectedFocusCandidates = focusCandidates;
        this.message = message;
    }

    @BeforePhase(Phase.RENDER_RESPONSE)
    public void invalidate_first_input() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        if (invalidatedComponents != null) {
            for (String invalidate : Splitter.on(" ").split(invalidatedComponents)) {
                facesContext.addMessage(invalidate, new FacesMessage("invalidated " + invalidate));
            }

            assertTrue(facesContext.getClientIdsWithMessages().hasNext());
        }
    }

    @AfterPhase(Phase.RENDER_RESPONSE)
    public void verify_focus_candidates() {

        FacesContext context = FacesContext.getCurrentInstance();

        AbstractFocus component = bean.getComponent();
        FocusRendererBase renderer = bean.getRenderer();

        String actualFocusCandidates = renderer.getFocusCandidatesAsString(context, component);

        assertEquals(message, expectedFocusCandidates, actualFocusCandidates);
    }
}

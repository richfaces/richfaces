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



package org.ajax4jsf.framework.renderer;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.ajax4jsf.context.AjaxContext;
import org.ajax4jsf.event.AjaxPhaseListener;
import org.ajax4jsf.tests.AbstractAjax4JsfTestCase;
import org.ajax4jsf.webapp.BaseFilter;
import org.ajax4jsf.webapp.FilterServletResponseWrapper;

/**
 * @author asmirnov@exadel.com (latest modification by $Author: ishabalov $)
 * @version $Revision: 1.1.2.4 $ $Date: 2007/02/20 20:58:09 $
 *
 */
public class BeforeRendererListenerTestCase extends AbstractAjax4JsfTestCase {

    /**
     * @param name
     */
    public BeforeRendererListenerTestCase(String name) {
        super(name);
    }

    /*
     * @see VcpJsfTestCase#setUp()
     */
    public void setUp() throws Exception {
        super.setUp();

        FilterServletResponseWrapper responseWrapper = new FilterServletResponseWrapper(response);

        request.setAttribute(BaseFilter.RESPONSE_WRAPPER_ATTRIBUTE, responseWrapper);
    }

    /*
     * @see VcpJsfTestCase#tearDown()
     */
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /*
     * Test method for 'org.ajax4jsf.renderkit.BeforeRenderListener.afterPhase(PhaseEvent)'
     */
    public void testAfterPhase() throws Exception {
        PhaseListener listener = new AjaxPhaseListener();
        PhaseEvent event = new PhaseEvent(facesContext, PhaseId.RENDER_RESPONSE, lifecycle);

//      UIComponent ajaxButton = createComponent("org.ajax4jsf.ajax.AjaxButton", "org.ajax4jsf.ajax.html.HtmlAjaxCommandButton", "org.ajax4jsf.ajax.AjaxButton", CommandButtonRenderer.class, null);
//      facesContext.getViewRoot().getChildren().add(ajaxButton);
        AjaxContext.getCurrentInstance(facesContext).setAjaxRequest(true);

        // TODO Must be used different StateManager !
//      listener.afterPhase(event);
//      assertNotNull(externalContext.getRequestMap().get(AjaxPhaseListener.VIEW_STATE_SAVED_PARAM));
//      assertNull(externalContext.getRequestMap().get(AjaxContext.STYLES_PARAMETER));
    }

    /*
     * Test method for 'org.ajax4jsf.renderkit.BeforeRenderListener.beforePhase(PhaseEvent)'
     */
    public void testBeforePhase() {}

    /*
     * Test method for 'org.ajax4jsf.renderkit.BeforeRenderListener.isValueReference(String)'
     */
    public void testIsValueReference() {}
}

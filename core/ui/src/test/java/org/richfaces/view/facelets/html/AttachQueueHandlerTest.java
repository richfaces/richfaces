/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.view.facelets.html;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewDeclarationLanguage;

import org.ajax4jsf.component.behavior.AjaxBehavior;
import org.jboss.test.faces.FacesEnvironment;
import org.jboss.test.faces.FacesEnvironment.FacesRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.richfaces.component.AbstractAttachQueue;
import org.richfaces.renderkit.util.AjaxRendererUtils;

/**
 * @author Nick Belaevski
 *
 */
public class AttachQueueHandlerTest {
    private FacesEnvironment environment;
    private FacesRequest facesRequest;
    private FacesContext facesContext;
    private UIViewRoot viewRoot;

    @Before
    public void setUp() throws Exception {
        environment = FacesEnvironment.createEnvironment();

        environment.withWebRoot(getClass().getResource("/org/richfaces/view/facelets/html/attachQueueWithNestedAjax.xhtml"));

        environment.start();

        facesRequest = environment.createFacesRequest();
        facesRequest.start();

        facesContext = FacesContext.getCurrentInstance();
        viewRoot = facesContext.getViewRoot();
    }

    @After
    public void tearDown() throws Exception {
        viewRoot = null;
        facesContext = null;

        facesRequest.release();
        facesRequest = null;

        environment.release();
        environment = null;
    }

    private void buildView(String viewId) throws IOException {
        viewRoot.setViewId(viewId);
        ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
        ViewDeclarationLanguage vdl = viewHandler.getViewDeclarationLanguage(facesContext, viewRoot.getViewId());
        vdl.buildView(facesContext, viewRoot);
    }

    private AjaxBehavior getAjaxBehavior(String componentId, String eventName) {
        UIComponent component = viewRoot.findComponent(componentId);
        assertNotNull(component);

        ClientBehaviorHolder behaviorHolder = (ClientBehaviorHolder) component;

        String localEventName = eventName;
        if (localEventName == null) {
            localEventName = behaviorHolder.getDefaultEventName();
        }

        List<ClientBehavior> behaviors = behaviorHolder.getClientBehaviors().get(localEventName);
        if (behaviors != null) {
            for (ClientBehavior behavior : behaviors) {
                if (behavior instanceof AjaxBehavior) {
                    return (AjaxBehavior) behavior;
                }
            }
        }

        return null;
    }

    private void checkNotAssignedToParent(String attachQueueId) {
        UIComponent attachQueue = viewRoot.findComponent(attachQueueId);

        assertTrue(attachQueue instanceof AbstractAttachQueue);
        assertNull(attachQueue.getParent().getAttributes().get(AjaxRendererUtils.QUEUE_ID_ATTRIBUTE));
    }

    @Test
    public void testAttachQueueWithNestedAjax() throws Exception {
        buildView("/attachQueueWithNestedAjax.xhtml");

        AjaxBehavior defaultEvent = getAjaxBehavior(":form:defaultEventText", null);
        assertNotNull(defaultEvent);
        assertEquals("form:defaultEventQueue", defaultEvent.getQueueId());

        AjaxBehavior keyup = getAjaxBehavior(":form:keyupText", "keyup");
        assertNotNull(keyup);
        assertEquals("form:keyupQueue", keyup.getQueueId());

        checkNotAssignedToParent(":form:defaultEventQueue");
        checkNotAssignedToParent(":form:keyupQueue");
    }

    @Test
    public void testAttachQueueWithParentComponent() throws Exception {
        buildView("/attachQueueWithParentComponent.xhtml");

        UIComponent link = viewRoot.findComponent(":form:link");
        assertEquals("form:attachQueue", link.getAttributes().get(AjaxRendererUtils.QUEUE_ID_ATTRIBUTE));
    }

    @Test
    public void testAttachQueueWithWrappingAjax() throws Exception {
        buildView("/attachQueueWithWrappingAjax.xhtml");

        AjaxBehavior defaultEventInput = getAjaxBehavior(":form:defaultEventInput", null);
        assertNotNull(defaultEventInput);
        assertEquals("form:defaultEventQueue", defaultEventInput.getQueueId());

        AjaxBehavior defaultEventLinkBehavior = getAjaxBehavior(":form:defaultEventLink", null);
        assertNotNull(defaultEventLinkBehavior);
        assertEquals("form:defaultEventQueue", defaultEventLinkBehavior.getQueueId());

        AjaxBehavior valueChangeInput = getAjaxBehavior(":form:valueChangeInput", null);
        assertNotNull(valueChangeInput);
        assertEquals("form:valueChangeQueue", valueChangeInput.getQueueId());

        AjaxBehavior valueChangeLink = getAjaxBehavior(":form:valueChangeLink", null);
        assertNull(valueChangeLink);

        checkNotAssignedToParent(":form:defaultEventQueue");
        checkNotAssignedToParent(":form:valueChangeQueue");
    }

    @Test
    public void testAttachQueueWithWrappingBehaviors() throws Exception {
        buildView("/attachQueueWithWrappingBehaviors.xhtml");

        AjaxBehavior input = getAjaxBehavior(":form:input", "click");
        assertNotNull(input);
        assertEquals("form:clickQueue", input.getQueueId());

        AjaxBehavior button = getAjaxBehavior(":form:button", "click");
        assertNotNull(button);
        assertEquals("form:clickQueue", button.getQueueId());

        AjaxBehavior nestedInputClick = getAjaxBehavior(":form:nestedInput", "click");
        assertNotNull(nestedInputClick);
        assertEquals("form:clickQueue", nestedInputClick.getQueueId());

        AjaxBehavior nestedInputChange = getAjaxBehavior(":form:nestedInput", "valueChange");
        assertNotNull(nestedInputChange);
        assertEquals("form:valueChangeQueue", nestedInputChange.getQueueId());

        AjaxBehavior nestedButtonClick = getAjaxBehavior(":form:nestedButton", "click");
        assertNotNull(nestedButtonClick);
        assertEquals("form:clickQueue", nestedButtonClick.getQueueId());

        checkNotAssignedToParent(":form:valueChangeQueue");
        checkNotAssignedToParent(":form:clickQueue");
    }
}

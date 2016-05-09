/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
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

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;

import org.ajax4jsf.renderkit.html.scripts.QueueScriptResourceRenderer;
import org.ajax4jsf.tests.AbstractAjax4JsfTestCase;
import org.jaxen.JaxenException;

import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author Nick Belaevski
 * @since 3.3.0
 */
public class QueueRendererTest extends AbstractAjax4JsfTestCase {
    private UIForm form;

    /**
     * @param name
     */
    public QueueRendererTest(String name) {
        super(name);
    }

    private String dry(String s) {
        return s.replace(" ", "");
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.tests.AbstractAjax4JsfTestCase#setUp()
     */
    public void setUp() throws Exception {
        super.setUp();

        UIViewRoot root = facesContext.getViewRoot();
        List<UIComponent> children = root.getChildren();

        form = (UIForm) application.createComponent(UIForm.COMPONENT_TYPE);
        form.setId("theform");
        form.getChildren().add(application.createComponent(UIInput.COMPONENT_TYPE));
        children.add(form);
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.tests.AbstractAjax4JsfTestCase#tearDown()
     */
    public void tearDown() throws Exception {
        super.tearDown();
        form = null;
    }

    private String getQueueScript(HtmlPage page) throws JaxenException {
        List<?> list = page.getByXPath("//head/script[@id='" + QueueScriptResourceRenderer.QUEUE_SCRIPT_ID
                + "']/text()");
        DomText text = (DomText) list.get(0);
        String scriptData = text.getData();

        return scriptData.replaceAll(
                "^\\Qif (typeof A4J != 'undefined') { if (A4J.AJAX) { with (A4J.AJAX) {\\E|\\Q}}};\\E$", "");
    }

    private String[] splitScript(String s) {
        String[] split = s.split(";");

        for (int i = 0; i < split.length; i++) {
            split[i] = split[i] + ";";
        }

        return split;
    }

    private String createQueueInitString(String queueName, String queueParams, String requestParams) {
        StringBuilder builder = new StringBuilder("if (!EventQueue.getQueue('").append(queueName).append(
                "')) { EventQueue.addQueue(new EventQueue('").append(queueName).append("'");

        builder.append(",");
        builder.append(String.valueOf(queueParams));
        builder.append(",");
        builder.append(String.valueOf(requestParams));
        builder.append(")) };");

        return builder.toString();
    }

    public void testViewQueueName() throws Exception {
        UIQueue queue = (UIQueue) application.createComponent(UIQueue.COMPONENT_TYPE);

        queue.setName("test_view_queue");
        facesContext.getViewRoot().getChildren().add(queue);

        HtmlPage page = renderView();
        String queueScript = getQueueScript(page);

        assertEquals(createQueueInitString("test_view_queue", null, null), queueScript);
    }

    public void testViewQueueDefaultName() throws Exception {
        UIQueue queue = (UIQueue) application.createComponent(UIQueue.COMPONENT_TYPE);

        facesContext.getViewRoot().getChildren().add(queue);

        HtmlPage page = renderView();
        String queueScript = getQueueScript(page);

        assertEquals(createQueueInitString(UIQueue.GLOBAL_QUEUE_NAME, null, null), queueScript);
    }

    public void testFormQueueName() throws Exception {
        UIQueue queue = (UIQueue) application.createComponent(UIQueue.COMPONENT_TYPE);

        queue.setName("test_view_queue");
        form.getChildren().add(queue);

        HtmlPage page = renderView();
        String queueScript = getQueueScript(page);

        assertEquals(createQueueInitString("theform:test_view_queue", null, null), queueScript);
    }

    public void testFormQueueDefaultName() throws Exception {
        UIQueue queue = (UIQueue) application.createComponent(UIQueue.COMPONENT_TYPE);

        form.getChildren().add(queue);

        HtmlPage page = renderView();
        String queueScript = getQueueScript(page);

        assertEquals(createQueueInitString("theform", null, null), queueScript);
    }

    public void testRenderQueueAttributes() throws Exception {
        UIQueue queue1 = (UIQueue) application.createComponent(UIQueue.COMPONENT_TYPE);

        queue1.setSize(10);
        queue1.setOnsizeexceeded("sizeexceeded_handler()");
        queue1.setSizeExceededBehavior("dropNext");
        queue1.setOnrequestqueue("request_queue_handler()");
        queue1.setOnrequestdequeue("request_de_queue_handler()");
        form.getChildren().add(queue1);

        UIQueue queue2 = (UIQueue) application.createComponent(UIQueue.COMPONENT_TYPE);

        queue2.setSize(-1);
        queue2.setName("unsizedQueue");
        form.getChildren().add(queue2);

        UIQueue queue3 = (UIQueue) application.createComponent(UIQueue.COMPONENT_TYPE);

        queue3.setName("defaultSizeQueue");
        form.getChildren().add(queue3);

        HtmlPage page = renderView();
        String queueScript = dry(getQueueScript(page));
        String[] scripts = splitScript(queueScript);

        assertEquals(3, scripts.length);
        assertEquals(dry(createQueueInitString("theform",
                "{'size':10,'sizeExceededBehavior':'dropNext','onsizeexceeded':function(query,options,event){sizeexceeded_handler()},'onrequestqueue':function(query,options,event){request_queue_handler()},'onrequestdequeue':function(query,options,event){request_de_queue_handler()}}", null)), scripts[0]);
        assertEquals(dry(createQueueInitString("theform:unsizedQueue", "{'size':-1}", null)), scripts[1]);
        assertEquals(dry(createQueueInitString("theform:defaultSizeQueue", null, null)), scripts[2]);
    }

    public void testRenderRequestAttributes() throws Exception {
        UIQueue queue1 = (UIQueue) application.createComponent(UIQueue.COMPONENT_TYPE);

        queue1.setName("queue1");
        queue1.setTimeout(50021);
        queue1.setOnerror("error_queue_handler()");
        queue1.setOnsubmit("submit_queue_handler()");
        queue1.setIgnoreDupResponses(false);
        form.getChildren().add(queue1);

        UIQueue queue2 = (UIQueue) application.createComponent(UIQueue.COMPONENT_TYPE);

        queue2.setName("queue2");
        queue2.setRequestDelay(600);
        queue2.setIgnoreDupResponses(true);
        queue2.setOnbeforedomupdate("beforedomupdate_handler()");
        queue2.setOncomplete("complete_handler()");
        queue2.setStatus("mystatus");
        form.getChildren().add(queue2);

        HtmlPage page = renderView();
        String queueScript = dry(getQueueScript(page));
        String[] scripts = splitScript(queueScript);

        assertEquals(2, scripts.length);
        assertEquals(dry(createQueueInitString("theform:queue1", null,
                "{'timeout':50021,'queueonsubmit':function(request){submit_queue_handler()},'queueonerror':function(request,status,message){error_queue_handler()}}")), scripts[0]);
        assertEquals(dry(createQueueInitString("theform:queue2", null,
                "{'ignoreDupResponses':true,'requestDelay':600,'status':'mystatus','queueonbeforedomupdate':function(request,event,data){beforedomupdate_handler()},'queueoncomplete':function(request,event,data){complete_handler()}}")), scripts[1]);
    }

    public void testInvalidSizeExceededBehavior() throws Exception {
        UIQueue queue = (UIQueue) application.createComponent(UIQueue.COMPONENT_TYPE);

        queue.setSizeExceededBehavior("unknownBehavior");
        form.getChildren().add(queue);

        try {
            renderView();
            fail();
        } catch (IllegalArgumentException e) {

            // ok
        }
    }

    public void testDuplicateQueues() throws Exception {
        UIQueue queue1 = (UIQueue) application.createComponent(UIQueue.COMPONENT_TYPE);

        queue1.setName("testQueue");
        queue1.setSize(2);
        form.getChildren().add(queue1);

        UIQueue queue2 = (UIQueue) application.createComponent(UIQueue.COMPONENT_TYPE);

        queue2.setName("testQueue");
        queue2.setSize(5);
        form.getChildren().add(queue2);

        HtmlPage page = renderView();
        String queueScript = dry(getQueueScript(page));
        String[] scripts = splitScript(queueScript);

        assertEquals(1, scripts.length);
        assertEquals(dry(createQueueInitString("theform:testQueue", "{'size':2}", null)), scripts[0]);
    }
}

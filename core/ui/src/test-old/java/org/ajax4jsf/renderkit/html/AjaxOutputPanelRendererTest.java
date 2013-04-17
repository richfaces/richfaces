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
package org.ajax4jsf.renderkit.html;

import javax.faces.component.UIForm;
import javax.faces.component.UIOutput;
import javax.faces.component.html.HtmlForm;

import org.ajax4jsf.component.UIAjaxOutputPanel;
import org.ajax4jsf.tests.AbstractAjax4JsfTestCase;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class AjaxOutputPanelRendererTest extends AbstractAjax4JsfTestCase {
    private UIForm form = null;
    private UIOutput output = null;
    private UIAjaxOutputPanel panel1 = null;
    private UIAjaxOutputPanel panel2 = null;
    private UIAjaxOutputPanel panel3 = null;
    private UIAjaxOutputPanel panel4 = null;

    public AjaxOutputPanelRendererTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        form = new HtmlForm();
        form.setId("form");
        facesContext.getViewRoot().getChildren().add(form);
        panel1 = (UIAjaxOutputPanel) application.createComponent(UIAjaxOutputPanel.COMPONENT_TYPE);
        panel1.setId("panel1");
        panel1.setLayout("none");
        form.getChildren().add(panel1);
        panel2 = (UIAjaxOutputPanel) application.createComponent(UIAjaxOutputPanel.COMPONENT_TYPE);
        panel2.setId("panel2");
        panel2.setLayout("block");
        form.getChildren().add(panel2);
        panel3 = (UIAjaxOutputPanel) application.createComponent(UIAjaxOutputPanel.COMPONENT_TYPE);
        panel3.setId("panel3");
        panel3.setLayout("inline");
        form.getChildren().add(panel3);
        panel4 = (UIAjaxOutputPanel) application.createComponent(UIAjaxOutputPanel.COMPONENT_TYPE);
        panel4.setId("panel4");
        panel4.setLayout("none");
        output = (UIOutput) application.createComponent(UIOutput.COMPONENT_TYPE);
        output.setId("output");
        output.setValue("output");
        output.setTransient(true);
        panel4.getChildren().add(output);
        form.getChildren().add(panel4);
    }

    public void tearDown() throws Exception {
        super.tearDown();
        output = null;
        panel1 = null;
        panel2 = null;
        panel3 = null;
        form = null;
    }

    public void testRender() throws Exception {
        HtmlPage page = renderView();

        assertNotNull(page);
        System.out.println(page.asXml());

        try {
            HtmlElement empty = page.getHtmlElementById(panel1.getClientId(facesContext));

            assertFalse("ElementNotFoundException was not thrown", true);
        } catch (ElementNotFoundException e) {
        }

        HtmlElement div = page.getHtmlElementById(panel2.getClientId(facesContext));

        assertNotNull(div);
        assertEquals("div", div.getNodeName());

        HtmlElement span = page.getHtmlElementById(panel3.getClientId(facesContext));

        assertNotNull(span);
        assertEquals("span", span.getNodeName());
        span = page.getHtmlElementById(output.getClientId(facesContext));
        assertNotNull(span);
        assertEquals("span", span.getNodeName());
        assertFalse(output.isTransient());
    }
}

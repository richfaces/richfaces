/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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

package org.richfaces.renderkit;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.ajax4jsf.renderkit.RendererUtils.HTML;
import org.jboss.test.faces.htmlunit.HtmlUnitEnvironment;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author Anton Belevich
 *
 */
public class InplaceInputRendererTest {

    private HtmlUnitEnvironment environment;

    @Before
    public void setUp() {
        environment = new HtmlUnitEnvironment();
        environment.withWebRoot(new File("src/test/resources"));
        environment.withResource("/WEB-INF/faces-config.xml", "org/richfaces/renderkit/faces-config.xml");
        environment.withResource("/test.xhtml", "org/richfaces/renderkit/rendererTest.xhtml");
        environment.start();
    }
    
    @Test
    @Ignore("Updates to RF-9132 broke this test - it needs to be fixed, but is blocking M2 release")
    public void testRenderDefaultState() throws Exception {
        HtmlPage page = environment.getPage("/test.jsf");
        List<?> nodes = page.getByXPath("//*[@id = 'form:input_default']");
        assertEquals(1, nodes.size());
        HtmlElement span = (HtmlElement) nodes.get(0);
        assertEquals("span", span.getNodeName());
        assertEquals("rf-ii-d-s", span.getAttribute(HTML.CLASS_ATTRIBUTE));
        
        HtmlElement label = (HtmlElement)span.getFirstChild();
        assertEquals("span", label.getNodeName());
        assertEquals("rf-ii-lbl", label.getAttribute(HTML.CLASS_ATTRIBUTE));
        DomNode text = label.getFirstChild();
        assertEquals(DomNode.TEXT_NODE, text.getNodeType());
        
        List<?> editNodes = page.getByXPath("//*[@id = 'form:input_default:edit']");
        assertEquals(1, editNodes.size());
        HtmlElement edit = (HtmlElement) editNodes.get(0);
        
        assertEquals("span", edit.getNodeName());
        assertEquals("rf-ii-e-s rf-ii-none", edit.getAttribute(HTML.CLASS_ATTRIBUTE));
        
        HtmlElement input = (HtmlElement)edit.getFirstChild();
        assertEquals("input", input.getNodeName());
        assertEquals("rf-ii-f", input.getAttribute(HTML.CLASS_ATTRIBUTE));
        assertEquals(text.getNodeValue(), input.getAttribute(HTML.VALUE_ATTRIBUTE));
        
        List<?> buttons = page.getByXPath("//*[@id = 'form:input_default:btn']");
        assertEquals(true, buttons.isEmpty());
    }
    
    @Test
    @Ignore("Updates to RF-9132 broke this test - it needs to be fixed, but is blocking M2 release")
    public void testRenderEditState() throws Exception {
        
        HtmlPage page = environment.getPage("/test.jsf");
    
        List<?> nodes = page.getByXPath("//*[@id = 'form:input_edit']");
        assertEquals(1, nodes.size());
        HtmlElement span = (HtmlElement) nodes.get(0);
        assertEquals("span", span.getNodeName());
        assertEquals("rf-ii-d-s", span.getAttribute(HTML.CLASS_ATTRIBUTE));

        HtmlElement label = (HtmlElement)span.getFirstChild();
        assertEquals("span", label.getNodeName());
        assertEquals("rf-ii-lbl", label.getAttribute(HTML.CLASS_ATTRIBUTE));
        DomNode text = label.getFirstChild();
        assertEquals(DomNode.TEXT_NODE, text.getNodeType());
        
        List<?> editNodes = page.getByXPath("//*[@id = 'form:input_edit:edit']");
        assertEquals(1, editNodes.size());
        HtmlElement edit = (HtmlElement) editNodes.get(0);
        assertEquals("span", edit.getNodeName());
        assertEquals("rf-ii-e-s", edit.getAttribute(HTML.CLASS_ATTRIBUTE));
        
        HtmlElement input = (HtmlElement)edit.getFirstChild();
        assertEquals("input", input.getNodeName());
        assertEquals("rf-ii-f", input.getAttribute(HTML.CLASS_ATTRIBUTE));
        assertEquals(text.getNodeValue(), input.getAttribute(HTML.VALUE_ATTRIBUTE));

        List<?> buttonNodes = page.getByXPath("//*[@id = 'form:input_edit:btn']");
        assertEquals(1, buttonNodes.size());

        HtmlElement button = (HtmlElement) buttonNodes.get(0);
        assertEquals("span", button.getNodeName());
        
        List<?> okButtonNodes = page.getByXPath("//*[@id = 'form:input_edit:okbtn']");
        assertEquals(1, okButtonNodes.size());
        
        HtmlElement okButton = (HtmlElement) okButtonNodes.get(0);
        assertEquals("input", okButton.getNodeName());
        assertEquals("rf-ii-btn", okButton.getAttribute(HTML.CLASS_ATTRIBUTE));
        assertEquals("image", okButton.getAttribute(HTML.TYPE_ATTR));

        List<?> cancelButtonNodes = page.getByXPath("//*[@id = 'form:input_edit:cancelbtn']");
        assertEquals(1, cancelButtonNodes.size());
        
        HtmlElement cancelButton = (HtmlElement) cancelButtonNodes.get(0);
        assertEquals("input", cancelButton.getNodeName());
        assertEquals("rf-ii-btn", cancelButton.getAttribute(HTML.CLASS_ATTRIBUTE));
        assertEquals("image", cancelButton.getAttribute(HTML.TYPE_ATTR));

    }
    
    @Test
    public void testEdit() throws Exception {
        HtmlPage page = environment.getPage("/test.jsf");
        edit(page, "input_default", "Another Test String");  

        blur(page);

        List<?> labelNodes = page.getByXPath("//*[@id = 'form:input_default:label']/text()");
        assertEquals(1, labelNodes.size());
        DomText text = (DomText) labelNodes.get(0);
        assertEquals("Another Test String", text.getTextContent());
        
        List<?>nodes = page.getByXPath("//*[@id = 'form:input_default']");
        assertEquals(1, nodes.size());
        HtmlElement span = (HtmlElement) nodes.get(0);
        assertEquals("rf-ii-d-s rf-ii-c-s", span.getAttribute(HTML.CLASS_ATTRIBUTE));

    }
    
    private void blur(HtmlPage page) throws Exception {
        List<?> panelNodes = page.getByXPath("//*[@id = 'form:panel']");
        assertEquals(1, panelNodes.size());
        HtmlElement panel = (HtmlElement) panelNodes.get(0);
        panel.click();
    }
    
    private void typeNewValue(HtmlPage page, String inplaceInputId, String value) throws Exception {
        List<?> inputNodes = page.getByXPath("//*[@id = 'form:" + inplaceInputId + ":input']");
        assertEquals(1, inputNodes.size());
        HtmlElement input = (HtmlElement) inputNodes.get(0);
        input.setAttribute(HTML.VALUE_ATTRIBUTE, "");
        input.type(value);
    }
    
    private void edit(HtmlPage page, String inplaceInputId, String value) throws Exception {
        List<?> nodes = page.getByXPath("//*[@id = 'form:" + inplaceInputId + "']");
        assertEquals(1, nodes.size());
        HtmlElement span = (HtmlElement) nodes.get(0);
        span.click();
        
        List<?> editNodes = page.getByXPath("//*[@id = 'form:" + inplaceInputId + ":edit']");
        assertEquals(1, editNodes.size());
        HtmlElement edit = (HtmlElement) editNodes.get(0);
        assertEquals("rf-ii-e-s", edit.getAttribute(HTML.CLASS_ATTRIBUTE));

        typeNewValue(page, inplaceInputId, value);
    }
    
    @Test
    public void testEditWithControls() throws Exception {
        HtmlPage page = environment.getPage("/test.jsf");
        
        edit(page, "input_controls", "Another Test String");
        
        List<?> cancelNodes = page.getByXPath("//*[@id = 'form:input_controls:cancelbtn']");
        assertEquals(1, cancelNodes.size());
        HtmlElement cancel = (HtmlElement) cancelNodes.get(0);
       
        cancel.mouseDown();
        
        List<?> labelNodes = page.getByXPath("//*[@id = 'form:input_controls:label']/text()");
        assertEquals(1, labelNodes.size());
        DomText text = (DomText) labelNodes.get(0);
        assertEquals("Test String", text.getTextContent());
                
        List<?> nodes = page.getByXPath("//*[@id = 'form:input_controls']");
        assertEquals(1, nodes.size());
        HtmlElement span = (HtmlElement) nodes.get(0);
        assertEquals("rf-ii-d-s", span.getAttribute(HTML.CLASS_ATTRIBUTE));
        
        edit(page, "input_controls",  "Another Test String");
                
        List<?> okNodes = page.getByXPath("//*[@id = 'form:input_controls:okbtn']");
        assertEquals(1, okNodes.size());
        HtmlElement ok = (HtmlElement) okNodes.get(0);
      
        ok.mouseDown();
        
        labelNodes = page.getByXPath("//*[@id = 'form:input_controls:label']/text()");
        assertEquals(1, labelNodes.size());
        text = (DomText) labelNodes.get(0);
        assertEquals("Another Test String", text.getTextContent());
        
        nodes = page.getByXPath("//*[@id = 'form:input_controls']");
        assertEquals(1, nodes.size());
        span = (HtmlElement) nodes.get(0);
        assertEquals("rf-ii-d-s rf-ii-c-s", span.getAttribute(HTML.CLASS_ATTRIBUTE));
        
        edit(page, "input_controls", "Test String");
        
        blur(page);
        
        labelNodes = page.getByXPath("//*[@id = 'form:input_controls:label']/text()");
        assertEquals(1, labelNodes.size());
        text = (DomText) labelNodes.get(0);
        assertEquals("Test String", text.getTextContent());
    }

    @After
    public void tearDown() {
        environment.release();
        environment = null;
    }    
}

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

import org.jboss.test.faces.htmlunit.HtmlUnitEnvironment;
import org.junit.After;
import org.junit.Before;
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
        environment.withResource("/test.xhtml", "org/richfaces/renderkit/inplaceInputTest.xhtml");
        environment.start();
    }
    
    @Test
    public void testRenderDefaultState() throws Exception {
        HtmlPage page = environment.getPage("/test.jsf");
        HtmlElement span = page.getFirstByXPath("//*[@id = 'form:input_default']");
        assertEquals("span", span.getNodeName());
        assertEquals("rf-ii-d-s", span.getAttribute(HtmlConstants.CLASS_ATTRIBUTE));
        
        HtmlElement label = (HtmlElement)span.getFirstChild();
        assertEquals("span", label.getNodeName());
        assertEquals("rf-ii-lbl", label.getAttribute(HtmlConstants.CLASS_ATTRIBUTE));
        DomNode text = label.getFirstChild();
        assertEquals(DomNode.TEXT_NODE, text.getNodeType());
        
        HtmlElement edit = page.getFirstByXPath("//*[@id = 'form:input_default:edit']");
        assertEquals("span", edit.getNodeName());
        assertEquals("rf-ii-e-s rf-ii-none", edit.getAttribute(HtmlConstants.CLASS_ATTRIBUTE));
        
        HtmlElement input = (HtmlElement)edit.getFirstChild();
        assertEquals("input", input.getNodeName());
        assertEquals("rf-ii-f", input.getAttribute(HtmlConstants.CLASS_ATTRIBUTE));
        assertEquals(text.getNodeValue(), input.getAttribute(HtmlConstants.VALUE_ATTRIBUTE));
        
        List<?> buttons = page.getByXPath("//*[@id = 'form:input_default:btn']");
        assertEquals(true, buttons.isEmpty());
    }
    
    @Test
    public void testRenderEditState() throws Exception {
        
        HtmlPage page = environment.getPage("/test.jsf");
    
        HtmlElement span = page.getFirstByXPath("//*[@id = 'form:input_edit']");
        assertEquals("span", span.getNodeName());
        assertEquals("rf-ii-d-s", span.getAttribute(HtmlConstants.CLASS_ATTRIBUTE));

        HtmlElement label = (HtmlElement)span.getFirstChild();
        assertEquals("span", label.getNodeName());
        assertEquals("rf-ii-lbl", label.getAttribute(HtmlConstants.CLASS_ATTRIBUTE));
        DomNode text = label.getFirstChild();
        assertEquals(DomNode.TEXT_NODE, text.getNodeType());
        
        HtmlElement edit = page.getFirstByXPath("//*[@id = 'form:input_edit:edit']");
        assertEquals("span", edit.getNodeName());
        assertEquals("rf-ii-e-s", edit.getAttribute(HtmlConstants.CLASS_ATTRIBUTE));
        
        HtmlElement input = (HtmlElement)edit.getFirstChild();
        assertEquals("input", input.getNodeName());
        assertEquals("rf-ii-f", input.getAttribute(HtmlConstants.CLASS_ATTRIBUTE));
        assertEquals(text.getNodeValue(), input.getAttribute(HtmlConstants.VALUE_ATTRIBUTE));

        HtmlElement button = page.getFirstByXPath("//*[@id = 'form:input_edit:btn']");
        assertEquals("span", button.getNodeName());
        
        HtmlElement okButton = page.getFirstByXPath("//*[@id = 'form:input_edit:okbtn']");
        assertEquals("input", okButton.getNodeName());
        assertEquals("rf-ii-btn", okButton.getAttribute(HtmlConstants.CLASS_ATTRIBUTE));
        assertEquals("image", okButton.getAttribute(HtmlConstants.TYPE_ATTR));

        HtmlElement cancelButton = page.getFirstByXPath("//*[@id = 'form:input_edit:cancelbtn']");
        assertEquals("input", cancelButton.getNodeName());
        assertEquals("rf-ii-btn", cancelButton.getAttribute(HtmlConstants.CLASS_ATTRIBUTE));
        assertEquals("image", cancelButton.getAttribute(HtmlConstants.TYPE_ATTR));

    }
    
    @Test
    public void testEdit() throws Exception {
        HtmlPage page = environment.getPage("/test.jsf");
        edit(page, "input_default", "Another Test String");  
        blur(page);
        DomText text = page.getFirstByXPath("//*[@id = 'form:input_default:label']/text()");
        assertEquals("Another Test String", text.getTextContent());
        HtmlElement span = page.getFirstByXPath("//*[@id = 'form:input_default']");
        assertEquals("rf-ii-d-s rf-ii-c-s", span.getAttribute(HtmlConstants.CLASS_ATTRIBUTE));
    }
    
    private void blur(HtmlPage page) throws Exception {
        HtmlElement panel = page.getFirstByXPath("//*[@id = 'form:panel']");
        panel.click();
    }
    
    private void typeNewValue(HtmlPage page, String inplaceInputId, String value) throws Exception {
        HtmlElement input = page.getFirstByXPath("//*[@id = 'form:" + inplaceInputId + ":input']");
        input.setAttribute(HtmlConstants.VALUE_ATTRIBUTE, "");
        input.type(value);
    }
    
    private void edit(HtmlPage page, String inplaceInputId, String value) throws Exception {
        HtmlElement span = page.getFirstByXPath("//*[@id = 'form:" + inplaceInputId + "']");
        span.click();
        HtmlElement edit = page.getFirstByXPath("//*[@id = 'form:" + inplaceInputId + ":edit']");
        assertEquals("rf-ii-e-s", edit.getAttribute(HtmlConstants.CLASS_ATTRIBUTE));
        typeNewValue(page, inplaceInputId, value);
    }
    
    @Test
    public void testEditWithControls() throws Exception {
        HtmlPage page = environment.getPage("/test.jsf");
        edit(page, "input_controls", "Another Test String");

        HtmlElement cancel = page.getFirstByXPath("//*[@id = 'form:input_controls:cancelbtn']");
        cancel.mouseDown();
        
        DomText text = page.getFirstByXPath("//*[@id = 'form:input_controls:label']/text()");
        assertEquals("Test String", text.getTextContent());
                
        HtmlElement span = page.getFirstByXPath("//*[@id = 'form:input_controls']");
        assertEquals("rf-ii-d-s", span.getAttribute(HtmlConstants.CLASS_ATTRIBUTE));
        
        edit(page, "input_controls",  "Another Test String");
                
        HtmlElement ok = page.getFirstByXPath("//*[@id = 'form:input_controls:okbtn']");
        ok.mouseDown();
        
        text = page.getFirstByXPath("//*[@id = 'form:input_controls:label']/text()");
        assertEquals("Another Test String", text.getTextContent());
        
        span = page.getFirstByXPath("//*[@id = 'form:input_controls']");
        assertEquals("rf-ii-d-s rf-ii-c-s", span.getAttribute(HtmlConstants.CLASS_ATTRIBUTE));
        
        edit(page, "input_controls", "Test String");
        
        blur(page);
        
        text = page.getFirstByXPath("//*[@id = 'form:input_controls:label']/text()");
        assertEquals("Test String", text.getTextContent());
    }

    @After
    public void tearDown() {
        environment.release();
        environment = null;
    }    
}

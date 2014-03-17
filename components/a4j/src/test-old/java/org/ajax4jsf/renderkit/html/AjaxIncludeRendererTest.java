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

import org.ajax4jsf.component.UIInclude;
import org.ajax4jsf.tests.AbstractAjax4JsfTestCase;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class AjaxIncludeRendererTest extends AbstractAjax4JsfTestCase {
    private UIInclude include1 = null;
    private UIInclude include2 = null;
    private UIInclude include3 = null;
    private UIForm form;

    public AjaxIncludeRendererTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        form = new HtmlForm();
        form.setId("form");
        facesContext.getViewRoot().getChildren().add(form);
        include1 = (UIInclude) application.createComponent(UIInclude.COMPONENT_TYPE);
        include1.setId("include1");
        include1.setLayout(UIInclude.LAYOUT_NONE);

        UIOutput output = (UIOutput) application.createComponent(UIOutput.COMPONENT_TYPE);

        output.setRendered(true);
        output.setId("output");
        output.setValue("output");
        include1.getChildren().add(output);
        include2 = (UIInclude) application.createComponent(UIInclude.COMPONENT_TYPE);
        include2.setId("include2");
        include2.setLayout(UIInclude.LAYOUT_BLOCK);
        include3 = (UIInclude) application.createComponent(UIInclude.COMPONENT_TYPE);
        include3.setId("include3");
        include3.setLayout(UIInclude.LAYOUT_INLINE);
        form.getChildren().add(include1);
        form.getChildren().add(include2);
        form.getChildren().add(include3);
    }

    public void tearDown() throws Exception {
        super.tearDown();
        include1 = null;
        include2 = null;
        include3 = null;
    }

    public void testRender() throws Exception {
        HtmlPage page = renderView();

        assertNotNull(page);
        System.out.println(page.asXml());

        try {
            HtmlElement empty = page.getHtmlElementById(include1.getClientId(facesContext));

            assertFalse("ElementNotFoundException was not thrown", true);
        } catch (ElementNotFoundException e) {
        }

        HtmlElement div = page.getHtmlElementById(include2.getClientId(facesContext));

        assertNotNull(div);
        assertEquals("div", div.getNodeName());

        HtmlElement span = page.getHtmlElementById(include3.getClientId(facesContext));

        assertNotNull(span);
        assertEquals("span", span.getNodeName());
    }
}

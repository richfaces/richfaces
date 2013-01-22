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
package org.ajax4jsf.renderkit.html;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.ajax4jsf.component.UIAjaxForm;
import org.ajax4jsf.tests.AbstractAjax4JsfTestCase;
import org.apache.commons.lang.StringUtils;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlScript;

public class AjaxFormRendererTest extends AbstractAjax4JsfTestCase {
    private static final boolean IS_PAGE_AVAILABILITY_CHECK = true;
    private static Set javaScripts = new HashSet();

    static {
        javaScripts.add("org.ajax4jsf.javascript.AjaxScript");
        javaScripts.add("org/ajax4jsf/javascript/scripts/form.js");
    }

    private UIAjaxForm form1;
    private UIAjaxForm form2;

    public AjaxFormRendererTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        form1 = (UIAjaxForm) application.createComponent(UIAjaxForm.COMPONENT_TYPE);
        form1.setId("form1");
        form1.setAjaxSubmit(true);
        form2 = (UIAjaxForm) application.createComponent(UIAjaxForm.COMPONENT_TYPE);
        form2.setId("form2");
        form2.setAjaxSubmit(false);
        facesContext.getViewRoot().getChildren().add(form1);
        facesContext.getViewRoot().getChildren().add(form2);
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test script rendering
     *
     * @throws Exception
     */
    public void testRenderScript() throws Exception {
        HtmlPage page = renderView();

        assertNotNull(page);
        System.out.println(page.asXml());
        assertEquals(getCountValidScripts(page, javaScripts, IS_PAGE_AVAILABILITY_CHECK).intValue(),
                javaScripts.size());
    }

    /**
     * Test rendering
     *
     * @throws Exception
     */
    public void testRender() throws Exception {
        HtmlPage page = renderView();

        assertNotNull(page);

        HtmlElement htmlForm1 = page.getHtmlElementById(form1.getClientId(facesContext));

        assertNotNull(htmlForm1);
        assertEquals("form", htmlForm1.getTagName());

        String action = htmlForm1.getAttributeValue("action");

        assertNotNull(action);
        assertTrue(action.startsWith("javascript:A4J.AJAX.SubmitForm"));

        HtmlElement htmlForm2 = page.getHtmlElementById(form2.getClientId(facesContext));

        assertNotNull(htmlForm2);
        assertEquals("form", htmlForm2.getTagName());
        action = htmlForm2.getAttributeValue("action");
        assertNotNull(action);
        assertTrue(action.startsWith("/"));
    }

    /**
     * Test rendering hidden inputs
     *
     * @throws Exception
     */
    public void testRenderHiddenInputs() throws Exception {
        HtmlPage page = renderView();

        assertNotNull(page);

        HtmlElement htmlForm1 = page.getHtmlElementById(form1.getClientId(facesContext));
        List inputs = htmlForm1.getHtmlElementsByTagName("input");

        assertNotNull(inputs);

        boolean foundId = false;
        boolean foundAutoscroll = false;

        for (Iterator it = inputs.iterator(); it.hasNext(); ) {
            HtmlElement input = (HtmlElement) it.next();
            String name = input.getAttributeValue("name");

            assertNotNull(name);

            if (!foundId && name.equals(form1.getClientId(facesContext))) {
                foundId = true;
            }

            if (!foundAutoscroll && name.equals("autoScroll")) {
                foundAutoscroll = true;
            }
        }

        assertTrue(foundId);
        assertTrue(foundAutoscroll);

        HtmlElement htmlForm2 = page.getHtmlElementById(form2.getClientId(facesContext));

        assertNotNull(htmlForm2);
        assertEquals("form", htmlForm2.getTagName());
    }
}

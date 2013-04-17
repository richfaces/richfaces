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

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.component.UIForm;
import javax.faces.component.html.HtmlForm;

import org.ajax4jsf.component.UIPush;
import org.ajax4jsf.tests.AbstractAjax4JsfTestCase;
import org.apache.commons.lang.StringUtils;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlScript;

public class AjaxPushRendererTest extends AbstractAjax4JsfTestCase {
    private static Set<String> javaScripts = new HashSet<String>();
    private static final boolean IS_PAGE_AVAILABILITY_CHECK = true;

    static {
        javaScripts.add("org.ajax4jsf.javascript.AjaxScript");
    }

    private UIForm form = null;
    private UIPush push1 = null;
    private UIPush push2 = null;

    public AjaxPushRendererTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        form = new HtmlForm();
        form.setId("form");
        facesContext.getViewRoot().getChildren().add(form);
        push1 = (UIPush) application.createComponent(UIPush.COMPONENT_TYPE);
        push1.setId("push1");
        push1.setEnabled(true);
        form.getChildren().add(push1);
        push2 = (UIPush) application.createComponent(UIPush.COMPONENT_TYPE);
        push2.setId("push2");
        push2.setEnabled(false);
        form.getChildren().add(push2);
    }

    public void tearDown() throws Exception {
        super.tearDown();
        push1 = null;
        push2 = null;
        form = null;
    }

    public void testRender() throws Exception {
        HtmlPage page = renderView();

        assertNotNull(page);
        System.out.println(page.asXml());

        HtmlElement span1 = page.getHtmlElementById(push1.getClientId(facesContext));

        assertNotNull(span1);
        assertEquals("span", span1.getTagName());

        String style = span1.getAttributeValue("style");

        assertNotNull(style);
        assertTrue(style.matches("display\\s*\\:\\s*none\\s*\\;\\s*"));

        HtmlElement span2 = page.getHtmlElementById(push2.getClientId(facesContext));

        assertNotNull(span2);
        assertEquals("span", span2.getTagName());
        style = span2.getAttributeValue("style");
        assertNotNull(style);
        assertTrue(style.matches("display\\s*\\:\\s*none\\s*\\;\\s*"));
    }

    public void testRenderScript() throws Exception {
        HtmlPage page = renderView();

        assertNotNull(page);

        List scripts = page.getDocumentElement().getHtmlElementsByTagName("script");

        assertEquals(getCountValidScripts(page, javaScripts, IS_PAGE_AVAILABILITY_CHECK).intValue(),
                javaScripts.size());

        HtmlElement span1 = page.getHtmlElementById(push1.getClientId(facesContext));

        assertNotNull(span1);
        scripts = span1.getHtmlElementsByTagName("script");

        int i = 0;

        for (Iterator it = scripts.iterator(); it.hasNext(); ) {
            HtmlScript item = (HtmlScript) it.next();
            DomText text = (DomText) item.getFirstChild();

            assertNotNull(text);
            assertTrue(text.asText().contains("A4J.AJAX.Push"));
            i++;
        }

        assertEquals(1, i);

        HtmlElement span2 = page.getHtmlElementById(push2.getClientId(facesContext));

        assertNotNull(span2);
        scripts = span2.getHtmlElementsByTagName("script");
        i = 0;

        for (Iterator it = scripts.iterator(); it.hasNext(); ) {
            HtmlScript item = (HtmlScript) it.next();
            DomText text = (DomText) item.getFirstChild();

            assertNotNull(text);
            assertTrue(text.asText().contains("A4J.AJAX.StopPush"));
            i++;
        }

        assertEquals(1, i);
    }
}

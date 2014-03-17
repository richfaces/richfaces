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

import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.tests.AbstractAjax4JsfTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class AjaxPageRendererTest extends AbstractAjax4JsfTestCase {
    private static Set<String> javaScripts = new HashSet<String>();
    private static final boolean IS_PAGE_AVAILABILITY_CHECK = true;

    static {
        javaScripts.add("org.ajax4jsf.javascript.AjaxScript");
    }

    private org.ajax4jsf.component.html.HtmlPage ajaxPage = null;

    public AjaxPageRendererTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        ajaxPage = (org.ajax4jsf.component.html.HtmlPage) application.createComponent(
                org.ajax4jsf.component.html.HtmlPage.COMPONENT_TYPE);
        ajaxPage.setId("page");

        UIOutput head = new UIOutput();

        head.setValue("HEAD");
        ajaxPage.getFacets().put("head", head);

        UIOutput content = new UIOutput();

        content.setValue("content");
        ajaxPage.getChildren().add(content);
        ajaxPage.setFormat("xhtml");
        ajaxPage.setPageTitle("title");
        facesContext.getViewRoot().setLocale(new Locale("be", "BY"));
        facesContext.getViewRoot().getChildren().add(ajaxPage);
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testRender() throws Exception {
        HtmlPage page = renderView();

        assertNotNull(page);
        System.out.println(page.asXml());

        HtmlElement html = (HtmlElement) page.getFirstChild();

        assertNotNull(html);
        assertEquals("html", html.getTagName());

        String lang = html.getAttributeValue("lang");

        assertNotNull(lang);
        assertEquals(lang, "be_BY");

        HtmlElement title = (HtmlElement) html.getHtmlElementsByTagName("title").get(0);

        assertNotNull(title);
        assertEquals("title", title.getFirstChild().asText());

        HtmlElement meta = (HtmlElement) html.getHtmlElementsByTagName("meta").get(0);

        assertNotNull(meta);

        String httpEquiv = meta.getAttributeValue("http-equiv");

        assertEquals(httpEquiv, "Content-Type");

        String content = meta.getAttributeValue("content");

        assertEquals(content, "application/xhtml+xml");
    }

    public void testRenderScript() throws Exception {
        HtmlPage page = renderView();

        assertNotNull(page);
        assertEquals(getCountValidScripts(page, javaScripts, IS_PAGE_AVAILABILITY_CHECK).intValue(),
                javaScripts.size());
    }

    @Override
    protected void encodeDocumentProlog(FacesContext context, UIViewRoot viewRoot, ResponseWriter writer)
            throws IOException {

        // do nothing as a4j:page encodes full page structure
    }

    @Override
    protected void encodeDocumentEpilog(FacesContext context, UIViewRoot viewRoot, ResponseWriter writer)
            throws IOException {

        // do nothing as a4j:page encodes full page structure
    }
}

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
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.component.html.HtmlForm;

import org.ajax4jsf.tests.AbstractAjax4JsfTestCase;
import org.apache.commons.lang.StringUtils;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlScript;

public class HtmlCommandLinkRendererTest extends AbstractAjax4JsfTestCase {
    /** Set with required javascripts for Editor */
    private static Set<String> javaScripts = new HashSet<String>();

    static {
        javaScripts.add("org/ajax4jsf/javascript/scripts/form.js");
    }

    private UIForm form = null;
    private HtmlCommandLink link1 = null;
    private HtmlCommandLink link2 = null;

    public HtmlCommandLinkRendererTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        facesContext.getRenderKit().addRenderer(HtmlCommandLink.COMPONENT_FAMILY, "org.ajax4jsf.Link",
                new HtmlCommandLinkRenderer());
        form = new HtmlForm();
        form.setId("form");
        facesContext.getViewRoot().getChildren().add(form);
        link1 = (HtmlCommandLink) application.createComponent(HtmlCommandLink.COMPONENT_TYPE);
        link1.setId("link1");
        link1.setValue("link1");
        link1.getAttributes().put("disabled", Boolean.FALSE);
        link1.setRendererType("org.ajax4jsf.Link");
        form.getChildren().add(link1);
        link2 = (HtmlCommandLink) application.createComponent(HtmlCommandLink.COMPONENT_TYPE);
        link2.setId("link2");
        link2.setValue("link2");
        link2.getAttributes().put("disabled", Boolean.TRUE);
        link2.setRendererType("org.ajax4jsf.Link");
        form.getChildren().add(link2);
    }

    public void tearDown() throws Exception {
        super.tearDown();
        link1 = null;
        link2 = null;
        form = null;
    }

    public void testRendered() throws Exception {
        HtmlPage page = renderView();

        assertNotNull(page);
        System.out.println(page.asXml());

        HtmlElement href = page.getHtmlElementById(link1.getClientId(facesContext));

        assertNotNull(href);
        assertEquals("a", href.getTagName());

        String onclick = href.getAttributeValue("onclick");

        assertNotNull(onclick);
        assertTrue(onclick.contains(AjaxFormRenderer.FORM_SUBMIT_FUNCTION_NAME));

        HtmlElement span = page.getHtmlElementById(link2.getClientId(facesContext));

        assertNotNull(span);
        assertEquals("span", span.getTagName());

        String disabled = span.getAttributeValue("disabled");

        assertNotNull(disabled);
        assertEquals("disabled", disabled);
    }

    /**
     * Method to test if required scripts is present on page
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void testLinkScripts() throws Exception {
        HtmlPage page = renderView();

        assertNotNull(page);

        List scripts = page.getDocumentHtmlElement().getHtmlElementsByTagName("script");

        for (Iterator it = scripts.iterator(); it.hasNext(); ) {
            HtmlScript item = (HtmlScript) it.next();
            String srcAttr = item.getSrcAttribute();

            if (StringUtils.isNotBlank(srcAttr)) {
                boolean found = false;

                for (Iterator srcIt = javaScripts.iterator(); srcIt.hasNext(); ) {
                    String src = (String) srcIt.next();

                    found = srcAttr.contains(src);

                    if (found) {
                        break;
                    }
                }

                assertTrue(found);
            }
        }
    }
}

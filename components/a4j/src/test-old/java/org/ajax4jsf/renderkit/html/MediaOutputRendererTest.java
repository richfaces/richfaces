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

import javax.faces.FacesException;
import javax.faces.component.UIForm;
import javax.faces.component.UIParameter;
import javax.faces.component.html.HtmlForm;

import org.ajax4jsf.component.UIMediaOutput;
import org.ajax4jsf.tests.AbstractAjax4JsfTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class MediaOutputRendererTest extends AbstractAjax4JsfTestCase {
    UIForm form = null;
    UIMediaOutput media1 = null;
    UIMediaOutput media2 = null;
    UIMediaOutput media3 = null;
    UIMediaOutput media4 = null;
    UIMediaOutput media5 = null;

    public MediaOutputRendererTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        application.addComponent("org.ajax4jsf.MMedia", "org.ajax4jsf.component.html.MediaOutput");
        form = new HtmlForm();
        form.setId("form");
        facesContext.getViewRoot().getChildren().add(form);
        media1 = (UIMediaOutput) application.createComponent(UIMediaOutput.COMPONENT_TYPE);
        media1.setId("media1");
        media1.setElement("a");
        media1.setUriAttribute("href");
        form.getChildren().add(media1);
        media2 = (UIMediaOutput) application.createComponent(UIMediaOutput.COMPONENT_TYPE);
        media2.setId("media2");
        media2.setElement("img");
        media2.setUriAttribute("src");
        media2.getAttributes().put("alt", "Generated value");
        form.getChildren().add(media2);
        media3 = (UIMediaOutput) application.createComponent(UIMediaOutput.COMPONENT_TYPE);
        media3.setId("media3");
        media3.setElement("object");
        media3.setUriAttribute("data");
        form.getChildren().add(media3);
        media4 = (UIMediaOutput) application.createComponent(UIMediaOutput.COMPONENT_TYPE);
        media4.setId("media4");
        media4.setElement("a");

        UIParameter param = new UIParameter();

        param.setName("name");
        param.setValue("value");
        media4.getChildren().add(param);
        form.getChildren().add(media4);
        media5 = (UIMediaOutput) application.createComponent(UIMediaOutput.COMPONENT_TYPE);
        media5.setId("media5");
    }

    public void tearDown() throws Exception {
        super.tearDown();
        media1 = null;
        media2 = null;
        media3 = null;
        media4 = null;
        media5 = null;
        form = null;
    }

    public void testRender() throws Exception {
        HtmlPage page = renderView();

        assertNotNull(page);
        System.out.println(page.asXml());

        HtmlElement a = page.getHtmlElementById(media1.getClientId(facesContext));

        assertNotNull(a);
        assertEquals("a", a.getTagName());

        String href = a.getAttributeValue("href");

        assertNotNull(href);

        HtmlElement img = page.getHtmlElementById(media2.getClientId(facesContext));

        assertNotNull(img);
        assertEquals("img", img.getTagName());

        String src = img.getAttributeValue("src");

        assertNotNull(src);

        HtmlElement object = page.getHtmlElementById(media3.getClientId(facesContext));

        assertNotNull(object);
        assertEquals("object", object.getTagName());

        String data = object.getAttributeValue("data");

        assertNotNull(data);

        // Rendering without uriAttribute
        HtmlElement a2 = page.getHtmlElementById(media4.getClientId(facesContext));

        assertNotNull(a2);
        assertEquals("a", a2.getTagName());

        String href2 = a2.getAttributeValue("href");

        assertNotNull(href2);
        assertTrue(href2.endsWith("name=value"));
    }

    public void testRenderWithoutElement() throws Exception {
        form.getChildren().add(media5);

        try {
            renderView();
            assertTrue("'element' is undefined but exception was not thrown", false);
        } catch (FacesException e) {
        }
    }
}

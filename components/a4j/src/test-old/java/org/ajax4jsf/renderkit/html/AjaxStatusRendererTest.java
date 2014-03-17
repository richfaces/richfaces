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

import java.util.Iterator;

import javax.faces.component.UIForm;
import javax.faces.component.UIGraphic;
import javax.faces.component.html.HtmlForm;

import org.ajax4jsf.component.UIAjaxStatus;
import org.ajax4jsf.tests.AbstractAjax4JsfTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class AjaxStatusRendererTest extends AbstractAjax4JsfTestCase {
    private UIForm form = null;
    private UIAjaxStatus status1 = null;
    private UIAjaxStatus status2 = null;

    public AjaxStatusRendererTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        application.addComponent("org.ajax4jsf.AjaxStatus", "org.ajax4jsf.component.html.HtmlAjaxStatus");
        form = new HtmlForm();
        form.setId("form");
        facesContext.getViewRoot().getChildren().add(form);
        status1 = (UIAjaxStatus) application.createComponent(UIAjaxStatus.COMPONENT_TYPE);
        status1.setId("status1");
        status1.setStartStyle("color: red;");
        status1.setStartStyleClass("A B C D");
        status1.setStopStyle("color: green;");
        status1.setStopStyleClass("X Y Z");
        status1.setStartText("startText");
        status1.setStopText("stopText");
        status1.getAttributes().put("layout", "block");
        status2 = (UIAjaxStatus) application.createComponent(UIAjaxStatus.COMPONENT_TYPE);
        status2.setId("status2");
        status2.setStartStyle("color: red;");
        status2.setStartStyleClass("A B C D");
        status2.setStopStyle("color: green;");
        status2.setStopStyleClass("X Y Z");
        status2.getAttributes().put("layout", "inline");

        UIGraphic startImage = new UIGraphic();

        startImage.setValue("start.png");
        startImage.getAttributes().put("alt", "alt");
        status2.getFacets().put("start", startImage);

        UIGraphic stopGraphic = new UIGraphic();

        stopGraphic.setValue("stop.png");
        stopGraphic.getAttributes().put("alt", "alt");
        status2.getFacets().put("stop", stopGraphic);
        form.getChildren().add(status1);
        form.getChildren().add(status2);
    }

    public void tearDown() throws Exception {
        super.tearDown();
        status1 = null;
        status2 = null;
        form = null;
    }

    /**
     * Test rendering
     *
     * @throws Exception
     */
    public void testRender() throws Exception {
        HtmlPage page = renderView();

        assertNotNull(page);

        HtmlElement div = page.getHtmlElementById(status1.getClientId(facesContext));

        assertNotNull(div);
        assertEquals("div", div.getNodeName());

        Iterator childIterator = div.getChildIterator();
        int i = 0;

        while (childIterator.hasNext()) {
            i++;

            HtmlElement element = (HtmlElement) childIterator.next();

            assertEquals("div", element.getNodeName());
        }

        assertEquals(2, i);

        HtmlElement div1 = page.getHtmlElementById(status1.getClientId(facesContext) + ".start");

        assertNotNull(div1);

        String style1 = div1.getAttributeValue("style");

        assertNotNull(style1);
        assertTrue(style1.contains("color: red;"));

        String class1 = div1.getAttributeValue("class");

        assertNotNull(class1);
        assertEquals(class1, "A B C D");

        HtmlElement div2 = page.getHtmlElementById(status1.getClientId(facesContext) + ".stop");

        assertNotNull(div2);

        String style2 = div2.getAttributeValue("style");

        assertNotNull(style2);
        assertTrue(style2.contains("color: green;"));

        String class2 = div2.getAttributeValue("class");

        assertNotNull(class2);
        assertEquals(class2, "X Y Z");
        form.getChildren().remove(0);
        page = renderView();
        System.out.println(page.asXml());

        HtmlElement span = (HtmlElement) div.getNextSibling();

        assertNotNull(span);
        assertEquals("span", span.getNodeName());
        childIterator = span.getChildIterator();
        i = 0;

        while (childIterator.hasNext()) {
            i++;

            HtmlElement element = (HtmlElement) childIterator.next();

            assertEquals("span", element.getNodeName());
            assertEquals("img", element.getFirstChild().getNodeName());
        }

        assertEquals(2, i);
    }
}

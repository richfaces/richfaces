/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.ui.iteration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.jboss.test.faces.htmlunit.HtmlUnitEnvironment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.richfaces.CustomizedHtmlUnitEnvironment;
import org.richfaces.ui.common.HtmlConstants;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author nick
 *
 */
public class ListRendererTest {
    private HtmlUnitEnvironment environment;
    private DataBean testBean;

    @Before
    public void setUp() {
        environment = new CustomizedHtmlUnitEnvironment();

        environment.withResource("/WEB-INF/faces-config.xml", "org/richfaces/ui/iteration/faces-config.xml");
        environment.withResource("/test.xhtml", "org/richfaces/ui/iteration/rendererTest.xhtml");

        environment.start();

        testBean = new DataBean();
    }

    @After
    public void tearDown() {
        environment.release();
        environment = null;

        testBean = null;
    }

    @Test
    public void testOrderedList() throws Exception {
        HtmlPage page = environment.getPage("/test.jsf");
        List<?> nodes = page.getByXPath("//*[@id = 'form:ol']");

        assertEquals(1, nodes.size());
        HtmlElement list = (HtmlElement) nodes.get(0);
        assertEquals("ol", list.getNodeName());
        assertEquals("rf-olst", list.getAttribute(HtmlConstants.CLASS_ATTRIBUTE));
        verifySimpleListItems(list, "rf-olst-itm");
    }

    @Test
    public void testUnorderedList() throws Exception {
        HtmlPage page = environment.getPage("/test.jsf");
        List<?> nodes = page.getByXPath("//*[@id = 'form:ul']");

        assertEquals(1, nodes.size());
        HtmlElement list = (HtmlElement) nodes.get(0);

        assertEquals("ul", list.getNodeName());
        assertEquals("rf-ulst", list.getAttribute(HtmlConstants.CLASS_ATTRIBUTE));
        verifySimpleListItems((HtmlElement) list, "rf-ulst-itm");
    }

    @Test
    public void testDefinitionsList() throws Exception {
        HtmlPage page = environment.getPage("/test.jsf");
        List<?> nodes = page.getByXPath("//*[@id = 'form:dl']");

        assertEquals(1, nodes.size());
        HtmlElement list = (HtmlElement) nodes.get(0);
        assertEquals("dl", list.getNodeName());
        verifyDefinitionsListItems(list);
    }

    @Test
    public void testFakeItem() throws Exception {
        HtmlPage page = environment.getPage("/test.jsf");
        List<?> nodes = page.getByXPath("//*[@id = 'form:emptyList']");

        assertEquals(1, nodes.size());
        HtmlElement list = (HtmlElement) nodes.get(0);
        assertEquals("ol", list.getNodeName());

        HtmlElement fakeItem = (HtmlElement) list.getFirstByXPath("li");
        assertNotNull(fakeItem);
        assertEquals("display:none", fakeItem.getAttribute("style"));
    }

    private void verifySimpleListItems(HtmlElement listElement, String styleClass) {
        List<?> listItems = listElement.getByXPath("li");
        assertEquals(testBean.getList().size(), listItems.size());
        for (int i = 0; i < listItems.size(); i++) {
            Data data = testBean.getList().get(i);

            HtmlElement item = (HtmlElement) listItems.get(i);

            assertEquals("li", item.getNodeName());
            assertEquals(styleClass, item.getAttribute("class"));
            assertEquals(data.getTerm(), item.asText());
        }
    }

    private void verifyDefinitionsListItems(HtmlElement listElement) {
        List<?> termItems = listElement.getByXPath("dt");
        List<?> definitionItems = listElement.getByXPath("dd");

        assertEquals(testBean.getList().size(), termItems.size());
        assertEquals(testBean.getList().size(), definitionItems.size());

        for (int i = 0; i < termItems.size(); i++) {
            Data data = testBean.getList().get(i);

            HtmlElement item = (HtmlElement) termItems.get(i);
            assertEquals("dt", item.getNodeName());
            assertEquals("rf-dlst-trm", item.getAttribute("class"));
            assertEquals(data.getTerm(), item.asText());
        }

        for (int i = 0; i < definitionItems.size(); i++) {
            Data data = testBean.getList().get(i);

            HtmlElement item = (HtmlElement) definitionItems.get(i);
            assertEquals("dd", item.getNodeName());
            assertEquals("rf-dlst-dfn", item.getAttribute("class"));
            assertEquals(data.getDefinition(), item.asText());
        }
    }
}

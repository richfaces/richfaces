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
package org.richfaces.component.util;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import junit.framework.TestCase;

import com.google.common.collect.Maps;

/**
 * @author Nick Belaevski - nbelaevski@exadel.com created 11.04.2007
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
public class HtmlUtilTest extends TestCase {
    public HtmlUtilTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testAddToSize() throws Exception {
        assertEquals("120px", HtmlUtil.addToSize("100", "20px"));
        assertEquals("120px", HtmlUtil.addToSize("100px", "20"));
        assertEquals("120px", HtmlUtil.addToSize("100", "20"));
        assertEquals("120px", HtmlUtil.addToSize("100px", "20px"));
    }

    public void testExpandIdSelectorWhenNoHashBeforeComponentIdThenIdNotExpanded() {
        Map<String, String> map = Maps.newHashMap();
        map.put("componentId", "form:componentId");

        String selector = "componentId";
        String expected = "componentId";

        String actual = expandIdSelector(selector, map);
        assertEquals(expected, actual);
    }

    public void testExpandIdSelectorWhenHavingExpandableSelectorThenExpandIt() {
        Map<String, String> map = Maps.newHashMap();
        map.put("componentId", "form:componentId");

        String selector = "#componentId";
        String expected = "#form\\:componentId";

        String actual = expandIdSelector(selector, map);
        assertEquals(expected, actual);
    }

    public void testExpandIdSelectorWhenHavingComplexSelectorWithExpandableIdentifiersThenExpandThem() {
        Map<String, String> map = Maps.newHashMap();
        map.put("-Test", "component$1");
        map.put("_aaaa", "component2");

        String selector = ".class_form+#-Test #form\\:element .class2 #_aaaa";
        String expected = ".class_form+#component\\$1 #form\\:element .class2 #component2";

        String actual = expandIdSelector(selector, map);
        assertEquals(expected, actual);
    }

    public void testEscapeHtml() {
        assertEquals("&amp; &gt; &amp;gt; &lt; &quot; &#39; ", HtmlUtil.escapeHtml("& > &gt; < \" ' "));
    }

    public void testExpandIdSelectorWhenHavingComplexSelectorWithNoExpandablePartThenNothingChanges() {
        Map<String, String> map = Maps.newHashMap();
        map.put("-Test", "component$1");
        map.put("_aaaa", "component2");

        String selector = ".class_form+.component1 .class2 #1component2";
        String expected = selector;

        String actual = expandIdSelector(selector, map);
        assertEquals(expected, actual);
    }

    private String expandIdSelector(String selector, final Map<String, String> clientIdMap) {
        UIComponent component = new UIComponentBase() {
            public String getFamily() {
                return null;
            }

            public UIComponent findComponent(String expr) {
                final String clientId = clientIdMap.get(expr);
                if (clientId == null) {
                    return null;
                }

                return new UIComponentBase() {
                    public String getClientId(FacesContext context) {
                        return clientId;
                    }

                    public String getFamily() {
                        return null;
                    }
                };
            }
        };

        return HtmlUtil.expandIdSelector(selector, component, null);
    }
}

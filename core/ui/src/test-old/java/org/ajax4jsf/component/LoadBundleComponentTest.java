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
package org.ajax4jsf.component;

import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIForm;
import javax.faces.component.html.HtmlForm;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class LoadBundleComponentTest extends org.ajax4jsf.tests.AbstractAjax4JsfTestCase {
    private static final String BUNDLE_NAME = "testBundle";
    private UILoadBundle bundle = null;
    private UIForm form;

    public LoadBundleComponentTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        form = new HtmlForm();
        form.setId("form");
        facesContext.getViewRoot().getChildren().add(form);
        bundle = (UILoadBundle) application.createComponent(UILoadBundle.COMPONENT_TYPE);

        if (null != bundle) {
            bundle.setId("loadBundle");
            bundle.setVar(BUNDLE_NAME);
            bundle.setBasename("org.ajax4jsf.component.test_skin");
            form.getChildren().add(bundle);
        }
    }

    public void tearDown() throws Exception {
        super.tearDown();
        form = null;
        bundle = null;
    }

    public void testLoadBundle() throws Exception {
        HtmlPage page = renderView();

        assertNotNull(page);

        Map loadedMap = (Map) facesContext.getExternalContext().getRequestMap().get(BUNDLE_NAME);

        assertNotNull(loadedMap);
        assertEquals("non-existent key", loadedMap.get("non-existent key"));
        assertEquals("#000000", loadedMap.get("shadowBackgroundColor"));
    }

    public void testSize() throws Exception {
        HtmlPage page = renderView();

        assertNotNull(page);

        Map loadedMap = (Map) facesContext.getExternalContext().getRequestMap().get(BUNDLE_NAME);

        assertNotNull(loadedMap);
        assertEquals(0, loadedMap.size());
    }

    public void testContainsKey() throws Exception {
        HtmlPage page = renderView();

        assertNotNull(page);

        Map loadedMap = (Map) facesContext.getExternalContext().getRequestMap().get(BUNDLE_NAME);

        assertNotNull(loadedMap);
        assertTrue(loadedMap.containsKey("shadowBackgroundColor"));
        assertFalse(loadedMap.containsKey("non-existent key"));
    }

    public void testFakeFunctions() throws Exception {
        HtmlPage page = renderView();

        assertNotNull(page);

        Map loadedMap = (Map) facesContext.getExternalContext().getRequestMap().get(BUNDLE_NAME);

        assertNotNull(loadedMap);
        assertFalse(loadedMap.isEmpty());
        assertFalse(loadedMap.containsValue(null));
        assertFalse(loadedMap.containsValue("any-string"));

        try {
            loadedMap.put("key1", "value1");
            assertFalse("UnsupportedOperationException was not thrown", true);
        } catch (UnsupportedOperationException e) {
        }

        try {
            loadedMap.putAll(new HashMap());
            assertFalse("UnsupportedOperationException was not thrown", true);
        } catch (UnsupportedOperationException e) {
        }

        try {
            loadedMap.remove("key1");
            assertFalse("UnsupportedOperationException was not thrown", true);
        } catch (UnsupportedOperationException e) {
        }

        try {
            loadedMap.clear();
            assertFalse("UnsupportedOperationException was not thrown", true);
        } catch (UnsupportedOperationException e) {
        }

        assertNull(loadedMap.keySet());
        assertNull(loadedMap.values());
        assertNull(loadedMap.entrySet());
    }
}

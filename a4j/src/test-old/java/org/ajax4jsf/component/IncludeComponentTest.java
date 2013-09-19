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

import javax.faces.component.UIForm;
import javax.faces.component.html.HtmlForm;

public class IncludeComponentTest extends org.ajax4jsf.tests.AbstractAjax4JsfTestCase {
    private UIForm form = null;
    private UIInclude include = null;

    public IncludeComponentTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        form = new HtmlForm();
        form.setId("form");
        facesContext.getViewRoot().getChildren().add(form);
        include = (UIInclude) application.createComponent(UIInclude.COMPONENT_TYPE);
        include.setId("include");
        include.setLayout(UIInclude.LAYOUT_NONE);
        form.getChildren().add(include);
    }

    public void tearDown() throws Exception {
        super.tearDown();
        form = null;
        include = null;
    }

    public void testState() throws Exception {
    }

    public void testViewId() throws Exception {
        include.setViewId(null);
        assertNull(include.getViewId());

        String viewId = "viewId";

        include.setViewId(viewId);

        String newViewId = include.getViewId();

        assertNotNull(newViewId);
        assertEquals(viewId, newViewId);
    }

    public void testLayout() throws Exception {
        include.setLayout(null);
        assertNull(include.getLayout());
        include.setLayout(UIInclude.LAYOUT_BLOCK);

        String newLayout = include.getLayout();

        assertNotNull(newLayout);
        assertEquals(UIInclude.LAYOUT_BLOCK, newLayout);
    }

    public void testAjaxRendered() throws Exception {
        assertFalse(include.isAjaxRendered());
    }
}

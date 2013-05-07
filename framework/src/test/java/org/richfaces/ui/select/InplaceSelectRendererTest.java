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

package org.richfaces.ui.select;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.Ignore;
import org.junit.Test;
import org.richfaces.ui.common.HtmlConstants;
import org.richfaces.ui.input.InplaceRendererTestBase;
import org.xml.sax.SAXException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author abelevich
 *
 */
public class InplaceSelectRendererTest extends InplaceRendererTestBase {
    public static final String PAGE_NAME = "inplaceSelectTest";
    public static final String BASE_ID = "form:inplaceSelect";

    @Test
    public void testDefaultEncode() throws IOException, SAXException {
        doTestDefaultEncode(PAGE_NAME, BASE_ID);
    }

    @Test
    public void testDefaultWithControlsEncode() throws IOException, SAXException {
        doTestDefaultWithControlsEncode(PAGE_NAME, BASE_ID);
    }

    @Test
    @Ignore
    public void testEditEncode() throws IOException, SAXException {
        doTestEditEncode(PAGE_NAME, BASE_ID);
    }

    private void edit(HtmlPage page, String inplaceSelectId, int selectIndex) throws Exception {
        HtmlElement span = page.getFirstByXPath("//*[@id = '" + inplaceSelectId + "']");
        assertNotNull(span);
        span.click();

        HtmlElement edit = page.getFirstByXPath("//*[@id = '" + inplaceSelectId + "Edit']");
        assertNotNull(edit);
        assertEquals("rf-is-fld-cntr", edit.getAttribute(HtmlConstants.CLASS_ATTRIBUTE));

        HtmlElement list = page.getFirstByXPath("//*[@id = '" + inplaceSelectId + "List']");
        assertNotNull(list);
        assertTrue(list.isDisplayed());

        HtmlElement item = page.getFirstByXPath("//*[@id = '" + inplaceSelectId + "Item" + selectIndex + "']");
        assertNotNull(item);
        item.click();

        HtmlElement panel = page.getFirstByXPath("//*[@id = 'form:out']");
        panel.click();

        list = page.getFirstByXPath("//*[@id = '" + inplaceSelectId + "List']");
        assertNotNull(list);
        assertFalse(list.isDisplayed());
    }

    public void TestEditWithControls() {
    }
}

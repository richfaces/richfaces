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

package org.richfaces.ui.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Arrays;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.richfaces.ui.common.HtmlConstants;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class InplaceInputRendererTest extends InplaceRendererTestBase {
    public static final String PAGE_NAME = "inplaceInputTest";
    public static final String BASE_ID = "form:inplaceInput";

    @Test
    public void testDefaultEncode() throws IOException, SAXException {
        doTestDefaultEncode(PAGE_NAME, BASE_ID);
    }

    @Test
    public void testDefaultWithControlsEncode() throws IOException, SAXException {
        doTestDefaultWithControlsEncode(PAGE_NAME, BASE_ID);
    }

    @Test
    public void testEdit() throws Exception {
        HtmlPage page = environment.getPage("/inplaceInputTest.jsf");
        edit(page, BASE_ID + DEFAULT, "Another Test String");
        blur(page);
        DomText text = page.getFirstByXPath("//*[@id = '" + BASE_ID + "DefaultLabel']/text()");
        assertEquals("Another Test String", text.getTextContent());
        HtmlElement span = page.getFirstByXPath("//*[@id = '" + BASE_ID + DEFAULT + "']");
        assertEquals("rf-ii rf-ii-chng", span.getAttribute(HtmlConstants.CLASS_ATTRIBUTE));
    }

    @Test
    public void testEditWithControls() throws Exception {
        HtmlPage page = environment.getPage("/inplaceInputTest.jsf");
        String withControlsComponentId = BASE_ID + WITH_CONTROLS;
        edit(page, withControlsComponentId, "Another Test String");

        HtmlElement cancel = page.getFirstByXPath("//*[@id = '" + withControlsComponentId + "Cancelbtn']");
        assertNotNull(cancel);
        cancel.mouseDown();

        DomText text = page.getFirstByXPath("//*[@id = '" + withControlsComponentId + "Label']/text()");
        assertNotNull(text);
        assertEquals("Edit Text", text.getTextContent());

        HtmlElement span = page.getFirstByXPath("//*[@id = '" + withControlsComponentId + "']");
        assertNotNull(span);
        assertEquals("rf-ii", span.getAttribute(HtmlConstants.CLASS_ATTRIBUTE));

        edit(page, withControlsComponentId, "Another Test String");

        HtmlElement ok = page.getFirstByXPath("//*[@id = '" + withControlsComponentId + "Okbtn']");
        assertNotNull(ok);
        ok.mouseDown();

        text = page.getFirstByXPath("//*[@id = '" + withControlsComponentId + "Label']/text()");
        assertNotNull(text);
        assertEquals("Another Test String", text.getTextContent());

        span = page.getFirstByXPath("//*[@id = '" + withControlsComponentId + "']");
        assertNotNull(span);
        String[] classAttribute = span.getAttribute(HtmlConstants.CLASS_ATTRIBUTE).split(" ");
        assertThat(Arrays.asList(classAttribute), CoreMatchers.hasItems("rf-ii", "rf-ii-chng"));

        edit(page, withControlsComponentId, "Test String");

        blur(page);

        text = page.getFirstByXPath("//*[@id = '" + withControlsComponentId + "Label']/text()");
        assertNotNull(text);
        assertEquals("Test String", text.getTextContent());
    }

    private void edit(HtmlPage page, String inplaceInputId, String value) throws Exception {
        HtmlElement span = page.getFirstByXPath("//*[@id = '" + inplaceInputId + "']");
        assertNotNull(span);
        span.click();
        HtmlElement edit = page.getFirstByXPath("//*[@id = '" + inplaceInputId + "Edit']");
        assertNotNull(edit);
        assertEquals("rf-ii-fld-cntr", edit.getAttribute(HtmlConstants.CLASS_ATTRIBUTE));
        typeNewValue(page, inplaceInputId, value);
    }

    private void typeNewValue(HtmlPage page, String inplaceInputId, String value) throws Exception {
        HtmlElement input = page.getFirstByXPath("//*[@id = '" + inplaceInputId + "Input']");
        assertNotNull(input);
        input.setAttribute(HtmlConstants.VALUE_ATTRIBUTE, "");
        input.type(value);
    }

    private void blur(HtmlPage page) throws Exception {
        HtmlElement panel = page.getFirstByXPath("//*[@id = 'form:panel']");
        panel.click();
    }
}

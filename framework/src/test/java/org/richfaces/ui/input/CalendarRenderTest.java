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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;
import org.richfaces.CustomizedHtmlUnitEnvironment;
import org.richfaces.ui.common.RendererTestBase;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;

public class CalendarRenderTest extends RendererTestBase {
    @Override
    public void setUp() throws URISyntaxException {
        environment = new CustomizedHtmlUnitEnvironment();
        environment.withWebRoot(new File(this.getClass().getResource(".").toURI()));
        environment.withResource("/WEB-INF/faces-config.xml", "org/richfaces/ui/input/faces-config.xml");
        environment.start();
        environment.getWebClient().setJavaScriptEnabled(true);
        environment.getWebClient().getOptions().setPrintContentOnFailingStatusCode(true);
    }

    @Test
    public void testExistenceCalendarPopup() throws Exception {
        HtmlPage page = environment.getPage("/calendarTest.jsf");
        HtmlElement calendarPopupElement = (HtmlElement) page.getElementById("form:calendarPopup");
        Assert.assertNotNull("form:calendarPopup element missed.", calendarPopupElement);
    }

    @Test
    public void testExistenceCalendarContent() throws Exception {
        HtmlPage page = environment.getPage("/calendarTest.jsf");
        HtmlElement calendarContentElement = (HtmlElement) page.getElementById("form:calendarContent");
        Assert.assertNotNull("form:calendarContent element missed.", calendarContentElement);
    }

    @Test
    public void testRenderCalendarScript() throws Exception {
        doTest("calendarTest", "calendarScript", "form:calendarScript");
    }

    @Test
    public void testRenderCalendarContent() throws Exception {
        doTest("calendarTest", "calendarContent", "form:calendarContent");
    }

    @Test
    public void testCalendarScrolling() throws Exception {
        HtmlPage page = environment.getPage("/calendarTest.jsf");

        HtmlImage calendarPopupButton = (HtmlImage) page.getElementById("form:calendarPopupButton");
        assertNotNull(calendarPopupButton);
        page = (HtmlPage) calendarPopupButton.click();
        HtmlElement calendarHeaderElement = page.getHtmlElementById("form:calendarHeader");
        assertNotNull("form:calendarHeader element missed.", calendarHeaderElement);

        HtmlTableDataCell nextTD = null;
        List<?> tds = calendarHeaderElement.getByXPath("table/tbody/tr/td");
        for (Object td : tds) {
            HtmlTableDataCell htdc = (HtmlTableDataCell) td;
            if (">".equals(htdc.asText())) {
                nextTD = htdc;
            }
        }
        assertNotNull(nextTD);
        HtmlElement div = (HtmlElement) nextTD.getChildElements().iterator().next();

        // Before click
        Calendar calendar = Calendar.getInstance();
        calendar.set(CalendarBean.CURRENT_YEAR, CalendarBean.CURRENT_MONTH, CalendarBean.CURRENT_DAY);
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
        assertTrue(calendarHeaderElement.asText().indexOf(month) > -1);

        page = div.click();

        // After click
        calendar.add(Calendar.MONTH, 1);
        month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
        assertTrue(calendarHeaderElement.asText().indexOf(month) > -1);
    }
}

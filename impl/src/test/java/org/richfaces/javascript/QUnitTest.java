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



package org.richfaces.javascript;

import static org.junit.Assert.fail;

import java.net.URL;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 *
 * @version $Revision$
 * @author Konstantin Mishin
 */
public class QUnitTest {
    private WebClient webClient_;

    @Before
    public void setUp() {
        webClient_ = new WebClient();
    }

    @After
    public void tearDown() {
        webClient_.closeAllWindows();
        webClient_ = null;
    }
    
    protected void runTest(URL url, String query) throws Exception{
        String urlStr = url.toExternalForm() + query;
        URL newURL = new URL(urlStr);
        runTest(newURL);
    }

    protected void runTest(URL url) throws Exception {

        HtmlPage page = loadPage(url);
        HtmlElement doc = page.getDocumentElement();
        HtmlElement tests = (HtmlElement) doc.getElementById("qunit-tests");
        Iterator<HtmlElement> iter = tests.getChildElements().iterator();

        if (!iter.hasNext()) {
            fail("No result found");
        }

        StringBuilder sb = new StringBuilder();
        int i = 1;

        while (iter.hasNext()) {
            final HtmlListItem testNode = (HtmlListItem) iter.next();

            if (testNode.getAttribute("class").contains("fail")) {
                sb.append(i).append(".  ").append(
                    testNode.<HtmlElement>getFirstByXPath("./strong").getFirstChild().getTextContent()).append("\n");

                int j = 1;

                for (HtmlElement li : testNode.<HtmlElement>getFirstByXPath("./ol").getChildElements()) {
                    if (li.getAttribute("class").contains("fail")) {
                        sb.append("     ").append(j).append(". ").append(li.getTextContent()).append("\n");
                    }

                    j++;
                }
            }

            i++;
        }

        if (sb.length() > 0) {
            fail("Failures:\n" + sb + "User Agent: " + doc.getElementById("qunit-userAgent").getTextContent());
        }
    }

    protected HtmlPage loadPage(URL url) throws Exception {
        HtmlPage page = webClient_.getPage(url);

        webClient_.waitForBackgroundJavaScriptStartingBefore(4 * 60 * 1000);

        return page;
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void test() throws Exception {
        runTest(getClass().getClassLoader().getResource("javascript/4_0_0.html"));
        runTest(getClass().getClassLoader().getResource("javascript/richfaces-client-api.html"), "?richfaces-event");
        runTest(getClass().getClassLoader().getResource("javascript/richfaces-client-api.html"), "?jquery-position");
        runTest(getClass().getClassLoader().getResource("javascript/richfaces-client-api.html"), "?richfaces-queue-request");
        runTest(getClass().getClassLoader().getResource("javascript/richfaces-client-api.html"), "?richfaces-queue-getSize");
        runTest(getClass().getClassLoader().getResource("javascript/richfaces-client-api.html"), "?richfaces-queue-isEmpty");
        runTest(getClass().getClassLoader().getResource("javascript/richfaces-client-api.html"), "?richfaces-queue-clear");
        runTest(getClass().getClassLoader().getResource("javascript/richfaces-client-api.html"), "?richfaces-queue-submitFirst");
        runTest(getClass().getClassLoader().getResource("javascript/richfaces-client-api.html"), "?richfaces-base-component module");
    }
}

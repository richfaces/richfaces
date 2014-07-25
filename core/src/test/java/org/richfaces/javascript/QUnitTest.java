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
package org.richfaces.javascript;

import static org.junit.Assert.fail;

import java.net.URL;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
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
        webClient_ = new WebClient(BrowserVersion.INTERNET_EXPLORER_11);
    }

    @After
    public void tearDown() {
        webClient_.closeAllWindows();
        webClient_ = null;
    }

    protected void runTest(URL url, String query) throws Exception {
        String urlStr = url.toExternalForm() + query;
        URL newURL = new URL(urlStr);
        runTest(newURL);
    }

    protected void runTest(URL url) throws Exception {

        HtmlPage page = loadPage(url);
        DomElement tests = page.getElementById("qunit-tests");
        Iterator<DomElement> iter = tests.getChildElements().iterator();

        if (!iter.hasNext()) {
            fail("No result found");
        }

        StringBuilder sb = new StringBuilder();
        int i = 1;

        while (iter.hasNext()) {
            final HtmlListItem testNode = (HtmlListItem) iter.next();

            if (testNode.getAttribute("class").contains("fail")) {
                sb.append(i).append(".  ")
                    .append(testNode.<HtmlElement>getFirstByXPath("./strong").getFirstChild().getTextContent()).append("\n");

                int j = 1;

                for (DomElement li : testNode.<DomElement>getFirstByXPath("./ol").getChildElements()) {
                    if (li.getAttribute("class").contains("fail")) {
                        sb.append("     ").append(j).append(". ").append(li.getTextContent()).append("\n");
                    }

                    j++;
                }
            }

            i++;
        }

        if (sb.length() > 0) {
            fail("Failures:\n" + sb + "User Agent: " + page.getElementById("qunit-userAgent").getTextContent());
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
    }

    @Test
    public void testEvent() throws Exception {
        runTest(getClass().getClassLoader().getResource("javascript/richfaces-client-api.html"), "?module=richfaces-event");
    }

    @Test
    public void testPosition() throws Exception {
        runTest(getClass().getClassLoader().getResource("javascript/richfaces-client-api.html"), "?module=jquery-position");
    }

    @Test
    public void testQueueRequest() throws Exception {
        runTest(getClass().getClassLoader().getResource("javascript/richfaces-client-api.html"), "?module=richfaces-queue-request");
    }

    @Test
    public void testQueueGetSize() throws Exception {
        runTest(getClass().getClassLoader().getResource("javascript/richfaces-client-api.html"), "?module=richfaces-queue-getSize");
    }

    @Test
    public void testQueueEmpty() throws Exception {
        runTest(getClass().getClassLoader().getResource("javascript/richfaces-client-api.html"), "?module=richfaces-queue-isEmpty");
    }

    @Test
    public void testQueueClear() throws Exception {
        runTest(getClass().getClassLoader().getResource("javascript/richfaces-client-api.html"), "?module=richfaces-queue-clear");
    }

    @Test
    public void testQueueSubmitFirst() throws Exception {
        runTest(getClass().getClassLoader().getResource("javascript/richfaces-client-api.html"), "?module=richfaces-queue-submitFirst");
    }

    @Test
    public void testBaseComponent() throws Exception {
        runTest(getClass().getClassLoader().getResource("javascript/richfaces-client-api.html"),
            "?module=richfaces-base-component");
    }

    @Test
    public void testRemoveStaleEntries() throws Exception {
        runTest(getClass().getClassLoader().getResource("javascript/richfaces-client-api.html"),
                "?module=richfaces-queue-remove-stale-entries");
    }
}

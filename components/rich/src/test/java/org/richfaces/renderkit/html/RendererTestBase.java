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
package org.richfaces.renderkit.html;

/*
 *  Remove after test moved to the test-jsf project
 *
 */

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.DifferenceListener;
import org.custommonkey.xmlunit.XMLUnit;
import org.jboss.test.faces.htmlunit.HtmlUnitEnvironment;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.richfaces.CustomizedHtmlUnitEnvironment;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author akolonitsky
 * @since Oct 22, 2010
 */
public abstract class RendererTestBase {
    static {
        XMLUnit.setNormalizeWhitespace(true);
        XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreComments(true);
    }

    protected HtmlUnitEnvironment environment;

    @Before
    public void setUp() throws URISyntaxException {
        environment = new CustomizedHtmlUnitEnvironment();
        environment.withWebRoot(new File(this.getClass().getResource(".").toURI()));
        environment.start();

        environment.getWebClient().getOptions().setJavaScriptEnabled(false);
    }

    @After
    public void tearDown() {
        environment.release();
        environment = null;
    }

    protected void doTest(String pageName, String pageElementToTest) throws IOException, SAXException {
        doTest(pageName, null, pageElementToTest);
    }

    protected void doTest(String pageName, String xmlunitPageName, String pageElementToTest) throws IOException, SAXException {
        HtmlPage page = environment.getPage('/' + pageName + ".jsf");
        HtmlElement htmlElement = (HtmlElement) page.getElementById(pageElementToTest);
        assertNotNull(htmlElement);

        String pageCode = htmlElement.asXml();

        checkXmlStructure(pageName, xmlunitPageName, pageCode);
    }

    protected void checkXmlStructure(String pageName, String xmlunitPageName, String pageCode) throws SAXException, IOException {
        if (xmlunitPageName == null) {
            xmlunitPageName = pageName + ".xmlunit.xml";
        }
        InputStream expectedPageCode = getExpectedPageCode(xmlunitPageName);
        if (expectedPageCode == null) {
            return;
        }

        Diff xmlDiff = new Diff(new InputStreamReader(expectedPageCode), new StringReader(pageCode));
        xmlDiff.overrideDifferenceListener(getDifferenceListener());

        if (!xmlDiff.similar()) {
            System.out.println("=== EXPECTED PAGE CODE ===");
            System.out.println(IOUtils.toString(getExpectedPageCode(xmlunitPageName)));
            System.out.println("==== ACTUAL PAGE CODE ====");
            System.out.println(pageCode);
            System.out.println("========= ERROR ==========");
            System.out.println(xmlDiff.toString());
            System.out.println("==========================");
            Assert.fail("XML was not similar:" + xmlDiff.toString());
        }
    }

    private InputStream getExpectedPageCode(String xmlunitPageName) {
        return this.getClass().getResourceAsStream(xmlunitPageName + ".xmlunit.xml");
    }

    protected DifferenceListener getDifferenceListener() {
        return new IgnoreScriptsContent();
    }
}

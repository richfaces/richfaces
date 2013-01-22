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



package org.ajax4jsf.framework.ajax.xmlfilter;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import junit.framework.TestCase;

import org.ajax4jsf.webapp.nekko.NekkoParser;

public class NekkoParserTest extends TestCase {
    private NekkoParser parser;

    protected void setUp() throws Exception {
        parser = new NekkoParser();
        parser.setInputEncoding("UTF-8");
        parser.setOutputEncoding("UTF-8");
        parser.init();
        super.setUp();
    }

    protected void tearDown() throws Exception {
        parser = null;
        super.tearDown();
    }

    /*
     * Test method for 'org.ajax4jsf.webapp.NekkoParser.parseHtml(Reader, Writer)'
     */
    public void testParseHtmlReaderWriter() {
        String html = "<html><body><table><tr><td>xxx</td></tr></table></body></html>";

        ParseString(html);
    }

    public void testParseHtmlReaderWriter1() {
        String html = "<html><body><table><tr><td>xxx</td></tr><tr><td>xxx</td></tr></table></body></html>";

        ParseString(html);
    }

    public void testParseHtmlReaderWriter2() {
        String html =
            "<html><body><table><tbody><tr><td>xxx</td></tr><tr><td>xxx</td></tr></tbody></table></body></html>";

        ParseString(html);
    }

    public void testParseHtmlReaderWriter3() {
        String html =
            "<html><body><table><thead><tr><td>xxx</td></tr></thead><tr><td>xxx</td></tr></table></body></html>";

        ParseString(html);
    }

    public void testParseHtmlReaderWriter4() {
        String html =
            "<html><body><table><span><tr><td>xxx</td></tr><tr><td>xxx</td></tr></span></table></body></html>";

        ParseString(html);
    }

    /**
     * @param html
     */
    private String ParseString(String html) {
        Reader in = new StringReader(html);
        StringWriter out = new StringWriter();

        try {
            parser.parseHtml(in, out);
        } catch (IOException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
            assertTrue("Io error in parser ", false);
        }

        String toString = out.toString();

        System.out.println(toString);

        return toString;
    }
}

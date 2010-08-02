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



package org.ajax4jsf.javascript;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import junit.framework.TestCase;

import org.ajax4jsf.javascript.JSMin.UnterminatedCommentException;
import org.ajax4jsf.javascript.JSMin.UnterminatedRegExpLiteralException;
import org.ajax4jsf.javascript.JSMin.UnterminatedStringLiteralException;

/**
 * @author asmirnov
 *
 */
public class JSMinTest extends TestCase {

    /**
     * @param name
     */
    public JSMinTest(String name) {
        super(name);
    }

    /*
     *  (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /*
     *  (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for {@link org.ajax4jsf.javascript.JSMin#jsmin()}.
     * @throws UnterminatedStringLiteralException
     * @throws UnterminatedCommentException
     * @throws UnterminatedRegExpLiteralException
     * @throws IOException
     */
    public void testJsmin()
            throws IOException, UnterminatedRegExpLiteralException, UnterminatedCommentException,
                   UnterminatedStringLiteralException {
        String script =
            "  patterns: {\n" + "    // combinators must be listed first\n"
            + "    // (and descendant needs to be last combinator)\n" + "    laterSibling: /^\\s*~\\s*/,\n"
            + "    child:        /^\\s*>\\s*/,\n" + "    adjacent:     /^\\s*\\+\\s*/,\n"
            + "    descendant:   /^\\s/,\n" + "\n" + "    // selectors follow\n"
            + "    tagName:      /^\\s*(\\*|[\\w\\-]+)(\\b|$)?/,\n" + "    id:           /^#([\\w\\-\\*]+)(\\b|$)/,\n"
            + "    className:    /^\\.([\\w\\-\\*]+)(\\b|$)/,\n"
            + "    pseudo:       /^:((first|last|nth|nth-last|only)(-child|-of-type)|empty|checked|(en|dis)abled|not)(\\((.*?)\\))?(\\b|$|\\s|(?=:))/,\n"
            + "    attrPresence: /^\\[([\\w]+)\\]/,\n"
            + "    attr:         /\\[((?:[\\w-]*:)?[\\w-]+)\\s*(?:([!^$*~|]?=)\\s*(([\'\"])([^\\]]*?)\\4|([^\'\"][^\\]]*?)))?\\]/\n"
            + "  }";
        ByteArrayInputStream in = new ByteArrayInputStream(script.getBytes());
        JSMin jsmin = new JSMin(in, System.out);

        jsmin.jsmin();
    }
}

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
package org.ajax4jsf.codec;

import org.ajax4jsf.util.base64.Codec;
import org.jboss.test.faces.AbstractThreadedTest;

public class CodecTest extends AbstractThreadedTest {
    Codec c;

    public void setUp() throws Exception {
        super.setUp();

        String message = "";

        try {
            c = new Codec("anbshsquycwuudyft");
        } catch (Exception e) {
            message = "Cannot create Codec instance " + e.getMessage();
        }

        assertNotNull(message, c);
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCodec() {
        CodecTestRunnable[] runnables = new CodecTestRunnable[100];

        for (int i = 0; i < runnables.length; i++) {
            runnables[i] = new CodecTestRunnable(c, generateRandomString(), i);
        }

        runTestCaseRunnables(runnables);
    }

    private String generateRandomString() {
        StringBuffer ss = new StringBuffer();

        for (int i = 0; i < 50000; i++) {
            char c = (char) (96 + Math.random() * 26);

            ss.append(c);
        }

        return ss.toString();
    }

    class CodecTestRunnable extends TestCaseRunnable {
        Codec c;
        int id;
        String s;

        public CodecTestRunnable(Codec c, String s, int id) {
            this.c = c;
            this.s = s;
            this.id = id;
        }

        public void runTestCase() throws Throwable {
            String s1 = c.encode(s);
            String s2 = c.decode(s1);

            assertEquals("Failure in thread " + id, s2, s);
        }
    }
}

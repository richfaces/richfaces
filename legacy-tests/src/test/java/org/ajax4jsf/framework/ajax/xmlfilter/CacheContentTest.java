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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.ajax4jsf.tests.AbstractAjax4JsfTestCase;
import org.ajax4jsf.webapp.CacheContent;

import org.apache.shale.test.mock.MockPrintWriter;
import org.apache.shale.test.mock.MockServletOutputStream;

/**
 * @author shura
 *
 */
public class CacheContentTest extends AbstractAjax4JsfTestCase {
    public CacheContentTest(String name) {
        super(name);

        // TODO Auto-generated constructor stub
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.tests.AbstractAjax4JsfTestCase#setUp()
     */
    public void setUp() throws Exception {

        // TODO Auto-generated method stub
        super.setUp();
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.tests.AbstractAjax4JsfTestCase#tearDown()
     */
    public void tearDown() throws Exception {

        // TODO Auto-generated method stub
        super.tearDown();
    }

    /**
     * Test method for {@link org.ajax4jsf.webapp.CacheContent#getOutputStream(java.io.OutputStream)}.
     * @throws IOException
     */
    public void testGetOutputStream() throws IOException {
        CacheContent content = new CacheContent();
        OutputStream outputStream = content.getOutputStream();
        byte[] bytes = "Test".getBytes();

        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();

        ByteArrayOutputStream ser = new ByteArrayOutputStream(1024);
        ObjectOutputStream objStream = new ObjectOutputStream(ser);

        objStream.writeObject(content);
        objStream.flush();

        ByteArrayInputStream in = new ByteArrayInputStream(ser.toByteArray());
        ObjectInputStream objInput = new ObjectInputStream(in);

        try {
            content = (CacheContent) objInput.readObject();
        } catch (ClassNotFoundException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        content.send(this.response);

        MockServletOutputStream mockStream = (MockServletOutputStream) response.getOutputStream();
        byte[] bs = mockStream.content();

        assertEquals("Test", new String(bs));
    }

    /**
     * Test method for {@link org.ajax4jsf.webapp.CacheContent#getWriter(java.io.Writer)}.
     * @throws IOException
     */
    public void testGetWriter() throws IOException {
        CacheContent content = new CacheContent();
        PrintWriter outputStream = content.getWriter();

        outputStream.write("Test");
        outputStream.flush();
        outputStream.close();

        ByteArrayOutputStream ser = new ByteArrayOutputStream(1024);
        ObjectOutputStream objStream = new ObjectOutputStream(ser);

        objStream.writeObject(content);
        objStream.flush();

        ByteArrayInputStream in = new ByteArrayInputStream(ser.toByteArray());
        ObjectInputStream objInput = new ObjectInputStream(in);

        try {
            content = (CacheContent) objInput.readObject();
        } catch (ClassNotFoundException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        content.send(this.response);

        MockPrintWriter writer = (MockPrintWriter) response.getWriter();
        char[] cs = writer.content();

        assertEquals("Test", new String(cs));
    }
}

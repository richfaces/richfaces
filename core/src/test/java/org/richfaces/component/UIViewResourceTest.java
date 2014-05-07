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

package org.richfaces.component;

import org.jboss.test.faces.FacesEnvironment;
import org.jboss.test.faces.FacesEnvironment.FacesRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for dynamic add/remove {@link org.richfaces.component.UIScripts} as view resource.
 *
 * @author asmirnov
 *
 */
public class UIViewResourceTest {
    private FacesEnvironment environment;

    @Before
    public void setUp() {
        environment = FacesEnvironment
            .createEnvironment()
            .withContent(
                "/test.xhtml",
                "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" + "      xmlns:ui=\"http://java.sun.com/jsf/facelets\"\n"
                    + "      xmlns:h=\"http://java.sun.com/jsf/html\">" + "<h:body>\n" + "<h:form id=\"helloForm\" >"
                    + "    <h:inputText id=\"input\" value=\"#{test.value}\" />\n"
                    + "    <h:commandButton id=\"command\" value=\"Ok\" action=\"#{test.action}\"/>\n" + "</h:form>\n"
                    + "</h:body>\n" + "</html>")
            .withResource(FacesEnvironment.FACES_CONFIG_XML, "org/richfaces/ui/core/faces-config.xml").start();
    }

    @After
    public void thearDown() throws Exception {
        environment.release();
        environment = null;
    }

    @Test
    public void testRequest() throws Exception {
        FacesRequest request = environment.createFacesRequest("http://localhost/test.jsf?foo=bar");
        assertNotNull(request.execute());
        String contentAsString = request.getConnection().getContentAsString();
        assertFalse(contentAsString.contains(Bean.TEST_SCRIPT));
        FacesRequest request2 = submit(request).submit();
        request2.execute();
        String content2 = request2.getConnection().getContentAsString();
        assertFalse(content2.contains(Bean.TEST_SCRIPT));
    }

    private FacesRequest submit(FacesRequest request) throws MalformedURLException {
        FacesRequest request2 = request.submit().withParameter("helloForm:input", "BAZ")
            .withParameter("helloForm:command", "Ok");
        request2.execute();
        String content2 = request2.getConnection().getContentAsString();
        assertTrue(content2.contains(Bean.TEST_SCRIPT));
        return request2;
    }
}

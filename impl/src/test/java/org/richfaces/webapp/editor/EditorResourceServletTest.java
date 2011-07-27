package org.richfaces.webapp.editor;

import static org.junit.Assert.assertNotNull;

import java.net.MalformedURLException;

import javax.faces.FacesException;

import org.jboss.test.faces.FacesEnvironment;
import org.jboss.test.faces.FacesEnvironment.FacesRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EditorResourceServletTest {

    private FacesEnvironment environment;

    @Before
    public void setUp() {
        environment = FacesEnvironment.createEnvironment().start();
    }

    @Test
    public void test() throws Exception {
        environment.withContent(
                "/test.xhtml",
                "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" + "      xmlns:ui=\"http://java.sun.com/jsf/facelets\"\n"
                        + "      xmlns:h=\"http://java.sun.com/jsf/html\">" + "<h:form id=\"helloForm\" >"
                        + "    <h:outputText value=\"foo_bar\" />\n" + "</h:form>\n" + "</html>").start();
        
        environment.withContent("/test.js", "test-javascript");

        FacesRequest request = environment.cre
        assertNotNull(request.execute());
        String contentAsString = request.getConnection().getContentAsString();
        System.out.println(contentAsString);

    }

    @After
    public void thearDown() throws Exception {
        environment.release();
    }
}

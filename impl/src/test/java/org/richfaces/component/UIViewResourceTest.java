package org.richfaces.component;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;

import org.jboss.test.faces.FacesEnvironment;
import org.jboss.test.faces.FacesEnvironment.FacesRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for dynamic add/remove {@link UIScripts} as view resource.
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
            .withResource(FacesEnvironment.FACES_CONFIG_XML, "org/richfaces/component/faces-config.xml").start();
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

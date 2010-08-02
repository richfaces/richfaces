
/**
 *
 */
package org.ajax4jsf.context;

import javax.faces.FacesException;

import org.jboss.test.faces.AbstractFacesTest;

/**
 * @author asmirnov
 *
 */
public class InitParametersTest extends AbstractFacesTest {
    private static final int INT_PARAM = 367;
    private static final String NO = "no";
    private static final String ORG_AJAX4JSF_FALSE = "org.ajax4jsf.FALSE";
    private static final String ORG_AJAX4JSF_INT = "org.ajax4jsf.INT";
    private static final String ORG_AJAX4JSF_STRING = "org.ajax4jsf.STRING";
    private static final String ORG_AJAX4JSF_TRUE = "org.ajax4jsf.TRUE";
    private static final String STRING_PARAM = "init";
    private static final String TRUE = "true";

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.tests.AbstractAjax4JsfTestCase#setUp()
     */
    public void setUp() throws Exception {
        super.setUp();
        setupFacesRequest();
    }

    @Override
    protected void setupJsfInitParameters() {
        super.setupJsfInitParameters();
        facesServer.addInitParameter(ORG_AJAX4JSF_TRUE, TRUE);
        facesServer.addInitParameter(ORG_AJAX4JSF_FALSE, NO);
        facesServer.addInitParameter(ORG_AJAX4JSF_INT, String.valueOf(INT_PARAM));
        facesServer.addInitParameter(ORG_AJAX4JSF_STRING, STRING_PARAM);
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.tests.AbstractAjax4JsfTestCase#tearDown()
     */
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for {@link org.ajax4jsf.context.ContextInitParameters#getInteger(javax.faces.context.FacesContext, java.lang.String[], int)}.
     */
    public void testGetInteger() {
        String[] params = {"foo.bar", ORG_AJAX4JSF_INT};
        int value = ContextInitParameters.getInteger(facesContext, params, 12);

        assertEquals(INT_PARAM, value);

        String[] params2 = {"foo.bar"};

        value = ContextInitParameters.getInteger(facesContext, params2, 12);
        assertEquals(12, value);

        String[] params3 = {"foo.bar", ORG_AJAX4JSF_STRING};

        try {
            value = ContextInitParameters.getInteger(facesContext, params3, 12);
        } catch (FacesException e) {
            return;
        }

        assertFalse("No exception on invalid parameter", true);
    }

    /**
     * Test method for {@link org.ajax4jsf.context.ContextInitParameters#getString(javax.faces.context.FacesContext, java.lang.String[], java.lang.String)}.
     */
    public void testGetString() {
        String[] params = {"foo.bar", ORG_AJAX4JSF_STRING};
        String value = ContextInitParameters.getString(facesContext, params, "foo");

        assertEquals(STRING_PARAM, value);

        String[] params2 = {"foo.bar"};

        value = ContextInitParameters.getString(facesContext, params2, "foo");
        assertEquals("foo", value);
    }

    /**
     * Test method for {@link org.ajax4jsf.context.ContextInitParameters#getBoolean(javax.faces.context.FacesContext, java.lang.String[], boolean)}.
     */
    public void testGetBoolean() {
        String[] params = {"foo.bar", ORG_AJAX4JSF_TRUE};
        boolean value = ContextInitParameters.getBoolean(facesContext, params, false);

        assertTrue(value);

        String[] params2 = {"foo.bar"};

        value = ContextInitParameters.getBoolean(facesContext, params2, true);
        assertTrue(value);

        String[] params3 = {"foo.bar", ORG_AJAX4JSF_FALSE};

        value = ContextInitParameters.getBoolean(facesContext, params3, true);
        assertFalse(value);

        String[] params4 = {"foo.bar", ORG_AJAX4JSF_STRING};

        try {
            value = ContextInitParameters.getBoolean(facesContext, params4, false);
        } catch (FacesException e) {
            return;
        }

        assertFalse("No exception on invalid parameter", true);
    }

    /**
     * Test method for {@link org.ajax4jsf.context.ContextInitParameters#getInitParameter(javax.faces.context.FacesContext, java.lang.String[])}.
     */
    public void testGetInitParameter() {
        String[] params = {"foo.bar", ORG_AJAX4JSF_STRING};
        String value = ContextInitParameters.getInitParameter(facesContext, params);

        assertEquals(STRING_PARAM, value);

        String[] params2 = {"foo.bar"};

        value = ContextInitParameters.getInitParameter(facesContext, params2);
        assertNull(value);
    }
}

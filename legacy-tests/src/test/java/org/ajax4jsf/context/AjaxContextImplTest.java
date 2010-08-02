
/**
 *
 */
package org.ajax4jsf.context;

import org.ajax4jsf.component.AjaxContainer;
import org.ajax4jsf.renderkit.AjaxContainerRenderer;
import org.ajax4jsf.renderkit.AjaxRendererUtils;
import org.ajax4jsf.tests.AbstractAjax4JsfTestCase;

/**
 * @author asmirnov
 *
 */
public class AjaxContextImplTest extends AbstractAjax4JsfTestCase {
    private static final String FOO_BAR = "foo:bar";

    /**
     * @param name
     */
    public AjaxContextImplTest(String name) {
        super(name);
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.tests.AbstractAjax4JsfTestCase#setUp()
     */
    public void setUp() throws Exception {
        super.setUp();
    }

    /*
     *  (non-Javadoc)
     * @see org.ajax4jsf.tests.AbstractAjax4JsfTestCase#tearDown()
     */
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for {@link org.ajax4jsf.context.AjaxContextImpl#decode(javax.faces.context.FacesContext)}.
     */
    public void testDecode0() {
        ajaxContext.decode(facesContext);
        assertFalse(ajaxContext.isAjaxRequest());
        assertNull(ajaxContext.getSubmittedRegionClientId());
        assertNull(ajaxContext.getAjaxSingleClientId());
    }

    /**
     * Test method for {@link org.ajax4jsf.context.AjaxContextImpl#decode(javax.faces.context.FacesContext)}.
     */
    public void testDecode1() {
        request.addParameter(AjaxContainerRenderer.AJAX_PARAMETER_NAME, FOO_BAR);
        externalContext.addRequestParameterMap(AjaxContainerRenderer.AJAX_PARAMETER_NAME, FOO_BAR);
        ajaxContext.decode(facesContext);
        assertTrue(ajaxContext.isAjaxRequest());
        assertEquals(FOO_BAR, ajaxContext.getSubmittedRegionClientId());
        assertNull(ajaxContext.getAjaxSingleClientId());
    }

    /**
     * Test method for {@link org.ajax4jsf.context.AjaxContextImpl#decode(javax.faces.context.FacesContext)}.
     */
    public void testDecode2() {
        externalContext.addRequestParameterMap(AjaxContainerRenderer.AJAX_PARAMETER_NAME, FOO_BAR);
        externalContext.addRequestParameterMap(AjaxRendererUtils.AJAX_SINGLE_PARAMETER_NAME, FOO_BAR);
        ajaxContext.decode(facesContext);
        assertTrue(ajaxContext.isAjaxRequest());
        assertEquals(FOO_BAR, ajaxContext.getSubmittedRegionClientId());
        assertEquals(FOO_BAR, ajaxContext.getAjaxSingleClientId());
    }

    /**
     * Test method for {@link org.ajax4jsf.context.AjaxContextImpl#release()}.
     */
    public void testRelease() {}

    public void testGetCurrentInstance() throws Exception {
        this.ajaxContext = null;
        request.removeAttribute(AjaxContext.AJAX_CONTEXT_KEY);
        externalContext.addRequestParameterMap(AjaxContainerRenderer.AJAX_PARAMETER_NAME, FOO_BAR);
        ajaxContext = AjaxContext.getCurrentInstance(facesContext);
        assertTrue(ajaxContext.isAjaxRequest());
        assertEquals(FOO_BAR, ajaxContext.getSubmittedRegionClientId());
    }
}

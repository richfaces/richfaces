
/**
 *
 */
package org.ajax4jsf.application;

import org.ajax4jsf.tests.AbstractAjax4JsfTestCase;

/**
 * @author asmirnov
 *
 */
public class AjaxStateHolderTest extends AbstractAjax4JsfTestCase {

    /**
     * @param name
     */
    public AjaxStateHolderTest(String name) {
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
     * Test method for {@link org.ajax4jsf.application.AjaxStateHolder#getInstance(javax.faces.context.FacesContext)}.
     */
    public void testGetInstance() {
        StateHolder ajaxStateHolder = AjaxStateHolder.getInstance(facesContext);

        assertNotNull(ajaxStateHolder);

        StateHolder ajaxStateHolder2 = AjaxStateHolder.getInstance(facesContext);

        assertSame(ajaxStateHolder, ajaxStateHolder2);
    }

    /**
     * Test method for {@link org.ajax4jsf.application.AjaxStateHolder#getState(java.lang.String, String)}.
     */
    public void testGetState() {
        Object state = new Object();
        Object state2 = new Object();
        StateHolder ajaxStateHolder = AjaxStateHolder.getInstance(facesContext);

        assertNull(ajaxStateHolder.getState("foo", "_id1"));
        ajaxStateHolder.saveState("foo", "_id1", new Object[] {state});
        ajaxStateHolder.saveState("foo", "_id2", new Object[] {state2});
        assertNull(ajaxStateHolder.getState("bar", "_id1"));
        assertSame(state2, ajaxStateHolder.getState("foo", null));
        assertSame(state, ajaxStateHolder.getState("foo", "_id1"));
        assertSame(state, ajaxStateHolder.getState("foo", "_id3"));

        Object state3 = new Object();
        Object state4 = new Object();

        ajaxStateHolder.saveState("bar", "_id1", new Object[] {state3});
        ajaxStateHolder.saveState("bar", "_id2", new Object[] {state4});
        assertSame(state3, ajaxStateHolder.getState("bar", "_id1"));
        assertSame(state, ajaxStateHolder.getState("foo", "_id3"));
    }
}

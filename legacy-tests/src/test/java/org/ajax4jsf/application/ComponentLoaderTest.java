
/**
 *
 */
package org.ajax4jsf.application;

import java.net.URL;
import java.net.URLClassLoader;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;

import junit.framework.TestCase;

/**
 * @author asmirnov
 *
 */
public class ComponentLoaderTest extends TestCase {
    private URLClassLoader classLoader;
    private ClassLoader contextClassLoader;
    private ComponentsLoaderImpl loader;

    /*
     *  (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        contextClassLoader = Thread.currentThread().getContextClassLoader();
        classLoader = new URLClassLoader(new URL[0], this.getClass().getClassLoader());
        Thread.currentThread().setContextClassLoader(classLoader);
        loader = new ComponentsLoaderImpl();
    }

    /*
     *  (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        loader = null;
        Thread.currentThread().setContextClassLoader(contextClassLoader);
        contextClassLoader = null;
    }

    /**
     * Test method for {@link org.ajax4jsf.application.ComponentsLoaderImpl#createComponent(java.lang.String)}.
     */
    public void testCreateComponent() {
        UIComponent input = loader.createComponent(UIInput.class.getName());

        assertNotNull(input);
        assertEquals(UIInput.class, input.getClass());
    }

    /**
     * Test method for {@link org.ajax4jsf.application.ComponentsLoaderImpl#transform(java.lang.Object)}.
     */
    public void testTransform() {
        Object componentClass = loader.transform(UIInput.class.getName());

        assertSame(UIInput.class, componentClass);
    }

    public void testGetLoader() throws Exception {
        assertSame(classLoader, loader.getClassLoader());
    }
}

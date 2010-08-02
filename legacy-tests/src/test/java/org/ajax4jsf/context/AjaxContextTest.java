package org.ajax4jsf.context;

import java.net.URL;

import org.ajax4jsf.tests.AbstractAjax4JsfTestCase;

public class AjaxContextTest extends AbstractAjax4JsfTestCase {
    public AjaxContextTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetCurrentInstance() {
        AjaxContext ajaxContext2 = AjaxContext.getCurrentInstance();

        assertSame(ajaxContext, ajaxContext2);
    }

    public void testGetCurrentInstanceFacesContext() {
        this.ajaxContext = null;
        request.removeAttribute(AjaxContext.AJAX_CONTEXT_KEY);

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader loader = new ClassLoader(contextClassLoader) {
            @Override
            public URL getResource(String name) {
                if (AjaxContext.SERVICE_RESOURCE.equals(name)) {
                    return super.getResource("META-INF/ajaxContext.txt");
                } else {
                    return super.getResource(name);
                }
            }
        };

        Thread.currentThread().setContextClassLoader(loader);

        AjaxContext ajaxContext2 = AjaxContext.getCurrentInstance(facesContext);

        assertSame(MockAjaxContext.class, ajaxContext2.getClass());
    }
}

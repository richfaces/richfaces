/**
 *
 */
package org.richfaces.component;

import java.util.Collections;

import javax.faces.context.FacesContext;

import org.ajax4jsf.javascript.JSLiteral;
import org.richfaces.application.ServiceTracker;
import org.richfaces.javascript.JavaScriptService;
import org.richfaces.resource.ResourceKey;
import org.richfaces.resource.ResourceLibrary;

/**
 * @author asmirnov
 *
 */
public class Bean {
    private static final class TestScript extends JSLiteral implements ResourceLibrary {
        public TestScript() {
            super(TEST_SCRIPT);
        }

        public Iterable<ResourceKey> getResources() {
            return Collections.singleton(TEST_RESOURCE);
        }
    }

    public static final String TEST_SCRIPT_NAME = "test_script";
    public static final String FOO_BAR = "foo.bar";
    public static final String FOO = "foo";
    public static final String FOO_VALUE = "fooValue";
    public static final String TEST_SCRIPT = "function " + FOO + "(id){alert(id);}";
    private static final ResourceKey TEST_RESOURCE = ResourceKey.create(TEST_SCRIPT_NAME + ".js", FOO_BAR);
    private static final ResourceLibrary SCRIPT = new TestScript();
    private String value = FOO_VALUE;

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    public String action() {
        JavaScriptService javaScriptService = ServiceTracker.getService(JavaScriptService.class);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        javaScriptService.addPageReadyScript(facesContext, SCRIPT);
        return null;
    }
}

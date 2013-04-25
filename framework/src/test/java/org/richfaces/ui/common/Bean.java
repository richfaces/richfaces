package org.richfaces.ui.common;

import java.util.Collections;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.richfaces.javascript.JSLiteral;
import org.richfaces.javascript.JavaScriptService;
import org.richfaces.resource.ResourceKey;
import org.richfaces.resource.ResourceLibrary;
import org.richfaces.services.ServiceTracker;

@RequestScoped
@ManagedBean(name = "test")
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

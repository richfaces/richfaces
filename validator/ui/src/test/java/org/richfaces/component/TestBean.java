/**
 * 
 */
package org.richfaces.component;

import java.util.Collection;
import java.util.Collections;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.richfaces.renderkit.html.ComponentValidatorScript;
import org.richfaces.validator.LibraryResource;

/**
 * @author asmirnov
 *
 */
public class TestBean {
    
    public static final String TEST_SCRIPT_NAME = "test_script";

    public static final String FOO_BAR = "foo.bar";

    public static final String FOO = "foo";

    public static final String FOO_VALUE = "fooValue";
    
    public static final String TEST_SCRIPT = "function " + FOO + "(id){alert(id);}";
    
    private static final LibraryResource TEST_RESOURCE = new LibraryResource(FOO_BAR, TEST_SCRIPT_NAME + ".js");

    private static final ComponentValidatorScript SCRIPT = new ComponentValidatorScript() {
        
        public String toScript() {
            return TEST_SCRIPT;
        }
        
        public void appendScript(StringBuffer functionString) {
            functionString.append(TEST_SCRIPT);
        }
        
        public Collection<LibraryResource> getResources() {
            return Collections.singleton(TEST_RESOURCE);
        }
        
        public String getName() {
            return FOO;
        }
        
        public String createCallScript(String clientId,String sourceId) {
            return FOO+"("+clientId+")";
        }
    };

    private String value=FOO_VALUE;

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
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIViewRoot viewRoot = facesContext.getViewRoot();
        UIValidatorScript resource = (UIValidatorScript) facesContext.getApplication().createComponent(UIValidatorScript.COMPONENT_TYPE);
        resource.addOrFindScript(SCRIPT );
        viewRoot.addComponentResource(facesContext, resource);
        return null;
    }
    

}

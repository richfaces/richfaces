
/**
 *
 */
package org.ajax4jsf.tests;

import java.io.IOException;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.el.ELContext;

import javax.faces.component.UIOutput;
import javax.faces.webapp.UIComponentELTag;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.PageContext;

/**
 * @author Administrator
 *
 */
public abstract class AbstractJspTestCase extends AbstractAjax4JsfTestCase {
    protected PageContext pageContext = null;
    protected UIComponentELTag rootTag;

    /**
     * @param name
     */
    public AbstractJspTestCase(String name) {
        super(name);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        pageContext = new MockPageContext() {
            private final int[] SCOPE_ORDER = {PAGE_SCOPE, REQUEST_SCOPE, SESSION_SCOPE, APPLICATION_SCOPE};
            private Map<String, Object> attributes = new HashMap<String, Object>();
            @Override
            public ELContext getELContext() {
                return elContext;
            }
            @Override
            public Object findAttribute(String name) {
                Object value = null;

                for (int i = 0; i < SCOPE_ORDER.length && value == null; i++) {
                    value = getScope(SCOPE_ORDER[i]).get(name);
                }

                return value;
            }
            @Override
            public int getAttributesScope(String name) {
                Object value = null;
                int i = 0;

                for (; i < SCOPE_ORDER.length && value == null; i++) {
                    value = getScope(SCOPE_ORDER[i]).get(name);
                }

                return value == null ? 0 : SCOPE_ORDER[i];
            }
            @Override
            public Enumeration<String> getAttributeNamesInScope(int scope) {
                Map<String, Object> scopeMap = getScope(scope);
                final Iterator<String> iterator = scopeMap.keySet().iterator();

                return new Enumeration<String>() {
                    public boolean hasMoreElements() {
                        return iterator.hasNext();
                    }
                    public String nextElement() {

                        // TODO Auto-generated method stub
                        return iterator.next();
                    }
                };
            }
            @Override
            public ServletRequest getRequest() {
                return request;
            }
            @Override
            public Object getAttribute(String name) {
                return getScope(PAGE_SCOPE).get(name);
            }
            @Override
            public Object getAttribute(String name, int scope) {
                return getScope(scope).get(name);
            }
            @SuppressWarnings("unchecked")
            private Map<String, Object> getScope(int scopeName) {
                switch (scopeName) {
                    case APPLICATION_SCOPE :
                        return externalContext.getApplicationMap();

                    case SESSION_SCOPE :
                        return externalContext.getSessionMap();

                    case REQUEST_SCOPE :
                        return externalContext.getRequestMap();

                    default :
                        return attributes;
                }
            }
        };
        rootTag = new UIComponentELTag() {
            @Override
            public String getComponentType() {

                // TODO Auto-generated method stub
                return UIOutput.COMPONENT_TYPE;
            }
            @Override
            public String getRendererType() {
                return null;
            }
        };
        rootTag.setPageContext(pageContext);
        rootTag.doStartTag();
    }

    @Override
    public void tearDown() throws Exception {
        pageContext = null;
        rootTag = null;
        super.tearDown();
    }
}

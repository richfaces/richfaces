package org.richfaces.javascript;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.junit.Assert.assertSame;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.jboss.test.faces.mock.Mock;
import org.jboss.test.faces.mock.MockTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.ValidatorTestBase;
import org.richfaces.component.UIScripts;

import com.google.common.collect.Lists;

@RunWith(MockTestRunner.class)
public class ServiceGetOrCreateResourceTest extends ValidatorTestBase {
    @Mock
    private UIViewRoot viewRoot;
    private UIScripts scriptResource;
    private JavaScriptServiceImpl serviceImpl;

    @Before
    public void setUpResource() {
        serviceImpl = new JavaScriptServiceImpl();
        scriptResource = new UIScripts();
    }

    @After
    public void cleanUpResource() {
        scriptResource = null;
    }

    /**
     * <p class="changed_added_4_0">
     * No resource exist in view, create a new one and store in "form" target.
     * </p>
     */
    @Test
    public void testCreateValidatorScriptResource() {
        FacesContext facesContext = recordResources(null, null);
        expect(environment.getApplication().createComponent(UIScripts.COMPONENT_TYPE)).andReturn(scriptResource);
        viewRoot.addComponentResource(facesContext, scriptResource);
        expectLastCall();
        verifyResult(facesContext);
    }

    private FacesContext recordResources(UIComponent formResource, UIComponent bodyResource) {
        FacesContext facesContext = recordViewRoot();
        recordViewResources("form", formResource);
        if (null == formResource || null != bodyResource) {
            recordViewResources("body", bodyResource);
        }
        return facesContext;
    }

    private void verifyResult(FacesContext facesContext) {
        controller.replay();
        assertSame(scriptResource, serviceImpl.getOrCreateScriptResource(facesContext));
        controller.verify();
    }

    /**
     * <p class="changed_added_4_0">
     * Resource already exists in "form" target
     * </p>
     */
    @Test
    public void testGetValidatorScriptResourceForm() {
        FacesContext facesContext = recordResources(scriptResource, null);
        verifyResult(facesContext);
    }

    /**
     * <p class="changed_added_4_0">
     * Resource already exists in "body" target
     * </p>
     */
    @Test
    public void testGetValidatorScriptResourceBody() {
        FacesContext facesContext = recordResources(null, scriptResource);
        verifyResult(facesContext);
    }

    private void recordViewResources(String target, UIComponent resource) {
        FacesContext facesContext = environment.getFacesContext();
        List<UIComponent> resources = Lists.newArrayList();
        if (null != resource) {
            resources.add(resource);
        }
        expect(viewRoot.getComponentResources(facesContext, target)).andReturn(resources);
    }

    private FacesContext recordViewRoot() {
        FacesContext facesContext = environment.getFacesContext();
        expect(facesContext.getViewRoot()).andStubReturn(viewRoot);
        return facesContext;
    }
}

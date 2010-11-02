package org.richfaces.renderkit.html;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;

import org.jboss.test.faces.mock.Mock;
import org.jboss.test.faces.mock.MockTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.component.UIValidatorScript;
import org.richfaces.component.behavior.ClientValidatorBehavior;

import com.google.common.collect.Lists;

@RunWith(MockTestRunner.class)
public class RendererGetOrCreateResourceTest extends RendererTestBase {

    private static final String FUNCTION_NAME = "inputValidate";
    
    private static final String SOURCE_ID ="clientValidator";

    @Mock
    private UIViewRoot viewRoot;

    @Mock
    private ComponentValidatorScript validatorScript;

    private UIValidatorScript scriptResource;

    @Before
    public void setUpResource(){
        scriptResource = new UIValidatorScript();
    }
    
    @After
    public void cleanUpResource(){
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
        expect(environment.getApplication().createComponent(UIValidatorScript.COMPONENT_TYPE))
            .andReturn(scriptResource);
        viewRoot.addComponentResource(facesContext, scriptResource);
        expectLastCall();
        verifyResult(facesContext);
    }

    private FacesContext recordResources(UIComponent formResource, UIComponent bodyResource) {
        FacesContext facesContext = recordViewRoot();
        recordViewResources("form", formResource);
        if(null == formResource || null != bodyResource){
            recordViewResources("body", bodyResource);
        }
        return facesContext;
    }

    private void verifyResult(FacesContext facesContext) {
        controller.replay();
        assertSame(scriptResource, renderer.getOrCreateValidatorScriptResource(facesContext));
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

    @Test
    public void buildAndStoreScript() throws Exception {
        FacesContext facesContext = recordResources(scriptResource, null);
        ClientValidatorRenderer renderer = new ClientValidatorRenderer() {
            ComponentValidatorScript createValidatorScript(ClientBehaviorContext behaviorContext,
                ClientValidatorBehavior behavior) {
                return validatorScript;
            };
        };
        setupBehaviorContext(input);
        expect(behaviorContext.getSourceId()).andStubReturn(SOURCE_ID);
        expect(validatorScript.createCallScript(FUNCTION_NAME,SOURCE_ID)).andReturn(FUNCTION_NAME);
        expect(input.getClientId(facesContext)).andReturn(FUNCTION_NAME);
        controller.replay();
        assertEquals(FUNCTION_NAME, renderer.buildAndStoreValidatorScript(behaviorContext, mockBehavior));
        assertEquals(1, scriptResource.getScripts().size());
        controller.verify();
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

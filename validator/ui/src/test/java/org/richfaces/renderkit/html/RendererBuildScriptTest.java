package org.richfaces.renderkit.html;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;

import javax.faces.component.UIViewRoot;
import javax.faces.component.behavior.ClientBehaviorContext;

import org.jboss.test.faces.mock.Mock;
import org.jboss.test.faces.mock.MockTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.services.ServiceTracker;
import org.richfaces.component.behavior.ClientValidatorBehavior;
import org.richfaces.javascript.JavaScriptService;

@RunWith(MockTestRunner.class)
public class RendererBuildScriptTest extends RendererTestBase {
    private static final String FUNCTION_NAME = "inputValidate";
    private static final String SOURCE_ID = "clientValidator";
    @Mock
    private UIViewRoot viewRoot;
    @Mock
    private ComponentValidatorScript validatorScript;
    @Mock
    private JavaScriptService scriptService;

    @Before
    public void setUpResource() {
        expect(factory.getInstance(JavaScriptService.class)).andReturn(scriptService);
        ServiceTracker.setFactory(factory);
    }

    @After
    public void cleanUpResource() {
        ServiceTracker.release();
    }

    @Test
    public void buildAndStoreScript() throws Exception {
        ClientValidatorRenderer renderer = new ClientValidatorRenderer() {
            ComponentValidatorScript createValidatorScript(ClientBehaviorContext behaviorContext,
                ClientValidatorBehavior behavior) {
                return validatorScript;
            }

            ;
        };
        setupBehaviorContext(input);
        expect(behaviorContext.getSourceId()).andStubReturn(SOURCE_ID);
        expect(validatorScript.createCallScript(FUNCTION_NAME, SOURCE_ID)).andReturn(FUNCTION_NAME);
        expect(input.getClientId(environment.getFacesContext())).andReturn(FUNCTION_NAME);
        expect(scriptService.addScript(environment.getFacesContext(), validatorScript)).andReturn(validatorScript);
        controller.replay();
        assertEquals(FUNCTION_NAME, renderer.buildAndStoreValidatorScript(behaviorContext, mockBehavior));
        controller.verify();
    }
}

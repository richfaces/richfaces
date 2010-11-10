package org.richfaces.component.behavior;

import static org.easymock.EasyMock.expect;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.render.ClientBehaviorRenderer;

import org.jboss.test.faces.mock.Environment;
import org.jboss.test.faces.mock.Environment.Feature;
import org.jboss.test.faces.mock.Mock;
import org.jboss.test.faces.mock.MockController;
import org.jboss.test.faces.mock.MockFacesEnvironment;
import org.jboss.test.faces.mock.Stub;
import org.junit.After;
import org.junit.Before;
import org.richfaces.application.ServicesFactory;

public class BehaviorTestBase {

    @Mock()
    @Environment({ Feature.APPLICATION, Feature.RENDER_KIT,Feature.EL_CONTEXT })
    protected MockFacesEnvironment environment;
    @Mock
    protected UIInput input;
    @Mock
    protected ClientBehaviorContext behaviorContext;
    @Mock
    protected ClientBehaviorRenderer behaviorRenderer;
    protected MockController controller;
    protected ClientValidatorBehavior behavior;
    @Mock
    protected ValueExpression expression;
    @Stub
    protected ServicesFactory factory;

    public BehaviorTestBase() {
        super();
    }

    @Before
    public void setUp() {
        behavior = createBehavior();
    }

    
    @After
    public void tearDown() throws Exception {
        controller.release();
    }
    
    protected ClientBehaviorContext setupBehaviorContext(UIComponent component) {
        expect(behaviorContext.getComponent()).andStubReturn(component);
        expect(behaviorContext.getFacesContext()).andStubReturn(environment.getFacesContext());
        return behaviorContext;
    }

    protected ClientValidatorBehavior createBehavior() {
        return new ClientValidatorImpl();
    }

}
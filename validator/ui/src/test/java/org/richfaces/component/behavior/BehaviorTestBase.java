package org.richfaces.component.behavior;

import static org.easymock.EasyMock.expect;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.render.ClientBehaviorRenderer;

import org.jboss.test.faces.mock.Mock;
import org.junit.Before;
import org.richfaces.ValidatorTestBase;

public class BehaviorTestBase extends ValidatorTestBase {
    @Mock
    protected UIInput input;
    @Mock
    protected ClientBehaviorContext behaviorContext;
    @Mock
    protected ClientBehaviorRenderer behaviorRenderer;
    protected ClientValidatorBehavior behavior;

    public BehaviorTestBase() {
        super();
    }

    @Before
    public void setUp() {
        behavior = createBehavior();
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
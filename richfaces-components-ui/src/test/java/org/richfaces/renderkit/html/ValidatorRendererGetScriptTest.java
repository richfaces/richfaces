package org.richfaces.renderkit.html;

import javax.faces.FacesException;
import javax.faces.component.behavior.ClientBehavior;

import org.jboss.test.faces.mock.MockTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * <p class="changed_added_4_0">
 * Test getScript method for wrong parameters.
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
@RunWith(MockTestRunner.class)
public class ValidatorRendererGetScriptTest extends ValidatorRendererTestBase {
    @Test(expected = NullPointerException.class)
    public void testGetScriptNullContext() {
        renderer.getScript(null, mockBehavior);
    }

    @Test(expected = NullPointerException.class)
    public void testGetScriptNullBehavior() {
        renderer.getScript(setupBehaviorContext(input), null);
    }

    @Test(expected = FacesException.class)
    public void testGetScriptWrongBehavior() {
        renderer.getScript(behaviorContext, controller.createNiceMock(ClientBehavior.class));
    }
}

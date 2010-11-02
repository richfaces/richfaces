package org.richfaces.component.behavior;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.jboss.test.faces.mock.Environment;
import org.jboss.test.faces.mock.Mock;
import org.jboss.test.faces.mock.MockController;
import org.jboss.test.faces.mock.MockFacesEnvironment;
import org.jboss.test.faces.mock.MockTestRunner;
import org.jboss.test.faces.mock.Stub;
import org.jboss.test.faces.mock.Environment.Feature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.component.behavior.ClientValidatorBehavior;
import org.richfaces.component.mock.MockUIForm;
import org.richfaces.component.mock.MockUIMessage;


/**
 * <p class="changed_added_4_0">This class tests client validator behavior.
 * as it described at https://community.jboss.org/wiki/ClientSideValidation # Server-side rendering algorithm</p>
 * @author asmirnov@exadel.com
 *
 */
@RunWith(MockTestRunner.class)
public class ClientValidatorBehaviorTest {
    
    private static final String CLIENT_VALIDATION_FUNCTION = "rf.csv.v";

    private final class DummyConverter implements Converter {
        public String getAsString(FacesContext context, UIComponent component, Object value) {
            return value.toString();
        }

        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            return value;
        }
    }

    private static final String FORM_ID = "formId";

    private static final String INPUT_ID = "inputId";

    private static final String MESSAGE_ID = "messageId";

    @Mock()
    @Environment({Feature.APPLICATION})
    protected MockFacesEnvironment environment;

    @Stub
    private UIOutput output;
    
    @Mock
    protected UIInput input;
    
    @Mock
    protected UIViewRoot root;
    
    @Mock
    protected MockUIForm form;
    
    @Mock
    protected MockUIMessage message;

    @Mock
    private ClientBehaviorContext behaviorContext;

    protected MockController controller;

    private ClientValidatorBehavior behavior;
    
    @Before
    public void setUp() {
        behavior = createBehavior();
    }
    
    /**
     * <p class="changed_added_4_0">Server-side rendering algorithm .1</p>
     */
    @Test(expected=FacesException.class)
    public void testGetScriptForIllegalComponent() {
        getScript(output);
    }

    /**
     * <p class="changed_added_4_0">Server-side rendering algorithm .2</p>
     * @throws Exception
     */
    @Test
    public void testGetScriptWithoutMessageComponent() throws Exception {
        buildForm(false);
        assertEquals(0, getScript(input).length());
    }
    
    /**
     * <p class="changed_added_4_0">Server-side rendering algorithm .3 - determine client-side converter</p>
     * @throws Exception
     */
    @Test
    public void testGetClientConverter() throws Exception {
        
    }
    /**
     * <p class="changed_added_4_0">Server-side rendering algorithm .3</p>
     * @throws Exception
     */
    @Test
    public void testGetScriptWithoutClientConverter() throws Exception {
        buildForm(true);
        expect(input.getConverter()).andStubReturn(new DummyConverter());
        String script = getScript(input);// ajax-only validator.
        assertFalse(script.contains(CLIENT_VALIDATION_FUNCTION));
    }
    
    /**
     * <p class="changed_added_4_0">Server-side rendering algorithm .3</p>
     * @throws Exception
     */
    @Test
    public void testGetScriptWithClientConverter() throws Exception {
        
    }

    private String getScript(UIComponent component) {
        ClientBehaviorContext clientBehaviorContext = setupBehaviorContext(component);
        controller.replay();
        String script = behavior.getScript(clientBehaviorContext);
        controller.verify();
        return script;
    }

    private ClientBehaviorContext setupBehaviorContext(UIComponent component) {
        expect(behaviorContext.getComponent()).andStubReturn(component);
        expect(behaviorContext.getFacesContext()).andStubReturn(environment.getFacesContext());
        return behaviorContext;
    }
    
    private void buildForm(boolean withMessage) {
        expect(environment.getFacesContext().getViewRoot()).andStubReturn(root);
        expect(form.getId()).andStubReturn(FORM_ID);
        List<UIComponent> formChildren = new ArrayList<UIComponent>();
        expect(form.getChildren()).andStubReturn(formChildren);
        expect(input.getId()).andStubReturn(INPUT_ID);
        expect(input.getParent()).andStubReturn(form);
        formChildren.add(input);
        expect(output.getParent()).andStubReturn(form);
        formChildren.add(output);
        if(withMessage){
            expect(message.getId()).andStubReturn(MESSAGE_ID);
            expect(message.getFor()).andStubReturn(INPUT_ID);
            expect(message.getParent()).andStubReturn(form);
            formChildren.add(message);
        }
//        root.getChildren().add(form);
    }

    private ClientValidatorBehavior createBehavior() {
        return new ClientValidatorBehavior();
    }

    @Test
    public void testDecode() {
        fail("Not yet implemented");
    }

}

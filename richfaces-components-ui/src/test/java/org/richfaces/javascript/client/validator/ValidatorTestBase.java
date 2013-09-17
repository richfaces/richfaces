package org.richfaces.javascript.client.validator;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.validation.ValidationException;

import net.sourceforge.htmlunit.corejs.javascript.JavaScriptException;
import net.sourceforge.htmlunit.corejs.javascript.NativeObject;

import org.richfaces.javascript.JSFunction;
import org.junit.Test;
import org.richfaces.javascript.Message;
import org.richfaces.javascript.client.MockTestBase;
import org.richfaces.javascript.client.RunParameters;
import org.richfaces.validator.FacesValidatorServiceImpl;

import com.gargoylesoftware.htmlunit.ScriptException;

public abstract class ValidatorTestBase extends MockTestBase {
    /**
     * <p class="changed_added_4_0">
     * TODO remove to check all messages.
     * </p>
     *
     * @deprecated Remove this option then all messages will be passed properly.
     */
    public static final String IGNORE_MESSAGE = "ignoreMessage";
    private static final Converter NUMBER_CONVERTER = new Converter() {
        public String getAsString(FacesContext context, UIComponent component, Object value) {

            return String.valueOf(value);
        }

        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            return Double.valueOf(value);
        }
    };

    public ValidatorTestBase(RunParameters criteria) {
        super(criteria);
    }

    @Test
    public void testValidator() throws Exception {
        Validator validator = createValidator();
        try {
            validator.validate(facesEnvironment.getFacesContext(), input, criteria.getValue());
            validateOnClient(validator);
        } catch (ValidatorException e) {
            // client-side script has to throw exception too.
            try {
                validateOnClient(validator);
                assertFalse("JSF validator throws exception for value: " + criteria.getValue() + ", validator options: "
                    + getOptions(), true);
            } catch (ScriptException e2) {
                // both methods throws exceptions - it's ok.
                Throwable cause = e2.getCause();
                assertTrue(cause instanceof JavaScriptException);
                if (!getOptions().containsKey(IGNORE_MESSAGE)) {
                    NativeObject value = (NativeObject) ((JavaScriptException) cause).getValue();
                    assertEquals(e.getFacesMessage().getDetail(), value.get("detail"));
                    assertEquals(e.getFacesMessage().getSummary(), value.get("summary"));
                }
            }
        }
    }

    protected Object validateOnClient(Validator validator) throws ValidationException {
        JSFunction clientSideFunction = new JSFunction("RichFaces.rf4.csv." + getJavaScriptFunctionName(), criteria.getValue(),
            TEST_COMPONENT_ID, getJavaScriptOptions(), getErrorMessage(validator));
        return qunit.runScript(clientSideFunction.toScript());
    }

    private Object getErrorMessage(Validator validator) {
        FacesValidatorServiceImpl validatorService = new FacesValidatorServiceImpl();
        FacesMessage message = validatorService.getMessage(facesEnvironment.getFacesContext(), validator, input, null);
        return new Message(message);
    }

    protected abstract Validator createValidator();

    @Override
    protected void recordMocks() {
        super.recordMocks();
        expect(facesEnvironment.getApplication().createConverter("javax.faces.Number")).andStubReturn(NUMBER_CONVERTER);
    }
}
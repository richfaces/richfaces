package org.richfaces.javascript.client.validator;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Collections;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.validation.ValidationException;

import net.sourceforge.htmlunit.corejs.javascript.JavaScriptException;
import net.sourceforge.htmlunit.corejs.javascript.NativeObject;

import org.ajax4jsf.javascript.JSFunction;
import org.junit.Test;
import org.richfaces.javascript.client.MockTestBase;
import org.richfaces.javascript.client.RunParameters;

import com.gargoylesoftware.htmlunit.ScriptException;

public abstract class ValidatorTestBase extends MockTestBase {

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
            validator.validate(facesEnvironment.getFacesContext(), component, criteria.getValue());
            validateOnClient();
        } catch (ValidatorException e) {
            // client-side script has to throw exception too.
            try {
                validateOnClient();
                assertFalse("JSF validator throws exception for value: " + criteria.getValue(), true);
            } catch (ScriptException e2) {
                // both methods throws exceptions - it's ok.
                e2.printStackTrace();
                Throwable cause = e2.getCause();
                assertTrue(cause instanceof JavaScriptException);
                NativeObject value = (NativeObject) ((JavaScriptException) cause).getValue();
                assertEquals(getErrorMessage().getDetail(), value.get("detail"));
            }
        }
    }

    protected Object validateOnClient() throws ValidationException {
        JSFunction clientSideFunction =
            new JSFunction("RichFaces.csv." + getJavaScriptFunctionName(), criteria.getValue(), getErrorMessage(),
                getJavaScriptOptions());
        return qunit.runScript(clientSideFunction.toScript());

    }

    protected abstract Validator createValidator();

    @Override
    protected void recordMocks() {
        super.recordMocks();
        expect(component.getAttributes()).andStubReturn(Collections.EMPTY_MAP);
        expect(component.getClientId(facesEnvironment.getFacesContext())).andStubReturn("testComponent");
        expect(facesEnvironment.getApplication().createConverter("javax.faces.Number")).andStubReturn(NUMBER_CONVERTER);
    }
}

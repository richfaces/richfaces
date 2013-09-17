package org.richfaces.javascript.client.validator;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;

import net.sourceforge.htmlunit.corejs.javascript.JavaScriptException;

import org.richfaces.javascript.JSFunction;
import org.junit.Test;
import org.richfaces.javascript.client.MockTestBase;
import org.richfaces.javascript.client.RunParameters;

import com.gargoylesoftware.htmlunit.ScriptException;

public abstract class BeanValidatorTestBase extends MockTestBase {
    protected static final String PROP = "property";

    public BeanValidatorTestBase(RunParameters criteria) {
        super(criteria);
    }

    @Test
    public void testValidator() throws Exception {
        Validator validator = createValidator();
        Set<?> constrains = validator.validateValue(getBeanType(), (String) getOptions().get(PROP), criteria.getValue());
        try {
            validateOnClient(validator);
            assertTrue("Bean validator found error for value: " + criteria.getValue() + ", validator options: " + getOptions(),
                constrains.isEmpty());
        } catch (ScriptException e2) {
            // both methods throws exceptions - it's ok.
            Throwable cause = e2.getCause();
            assertTrue(cause instanceof JavaScriptException);
        }
    }

    protected abstract Class<?> getBeanType();

    protected Object validateOnClient(Validator validator) throws ValidationException {
        JSFunction clientSideFunction = new JSFunction("RichFaces.rf4.csv." + getJavaScriptFunctionName(), criteria.getValue(),
            TEST_COMPONENT_ID, getJavaScriptOptions(), getErrorMessage());
        return qunit.runScript(clientSideFunction.toScript());
    }

    protected Validator createValidator() {
        return Validation.buildDefaultValidatorFactory().usingContext().getValidator();
    }
}

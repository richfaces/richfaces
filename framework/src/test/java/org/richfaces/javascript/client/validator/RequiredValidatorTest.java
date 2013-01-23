/**
 *
 */
package org.richfaces.javascript.client.validator;

import org.junit.runners.Parameterized.Parameters;
import org.richfaces.javascript.client.RunParameters;

import javax.faces.validator.RequiredValidator;
import javax.faces.validator.Validator;
import java.util.List;
import java.util.Map;

/**
 * @author asmirnov
 *
 */
public class RequiredValidatorTest extends ValidatorTestBase {
    /**
     * @param criteria
     */
    public RequiredValidatorTest(RunParameters criteria) {
        super(criteria);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.javascript.client.validator.ValidatorTestBase#createValidator()
     */
    @Override
    protected Validator createValidator() {
        RequiredValidator validator = new RequiredValidator();
        Map<String, Object> options = getOptions();
        return validator;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.javascript.client.MockTestBase#getJavaScriptFunctionName()
     */
    @Override
    protected String getJavaScriptFunctionName() {
        return "validateRequired";
    }

    @Parameters
    public static List<RunParameters[]> parameters() {
        return options(pass(""), pass("aaa"), pass("123"), pass(null));
    }
}

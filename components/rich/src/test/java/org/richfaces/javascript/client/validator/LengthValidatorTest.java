/**
 *
 */
package org.richfaces.javascript.client.validator;

import java.util.List;
import java.util.Map;

import javax.faces.validator.LengthValidator;
import javax.faces.validator.Validator;

import org.junit.runners.Parameterized.Parameters;
import org.richfaces.javascript.client.RunParameters;

/**
 * @author asmirnov
 *
 */
public class LengthValidatorTest extends ValidatorTestBase {
    private static final String MINIMUM = "min";
    private static final String MAXIMUM = "max";

    /**
     * @param criteria
     */
    public LengthValidatorTest(RunParameters criteria) {
        super(criteria);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.javascript.client.validator.ValidatorTestBase#createValidator()
     */
    @Override
    protected Validator createValidator() {
        LengthValidator validator = new LengthValidator();
        Map<String, Object> options = getOptions();
        if (options.containsKey(MINIMUM)) {
            validator.setMinimum((Integer) options.get(MINIMUM));
        }
        if (options.containsKey(MAXIMUM)) {
            validator.setMaximum((Integer) options.get(MAXIMUM));
        }
        return validator;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.javascript.client.MockTestBase#getJavaScriptFunctionName()
     */
    @Override
    protected String getJavaScriptFunctionName() {
        return "validateLength";
    }

    @SuppressWarnings("deprecation")
    @Parameters
    public static List<RunParameters[]> parameters() {
        return options(pass(""), pass("aaa"), pass("123"), pass("", MINIMUM, 2), pass("vv", MINIMUM, 2),
            pass("vvv", MINIMUM, 2), pass("", MAXIMUM, 2), pass("vv", MAXIMUM, 2), pass("123", MAXIMUM, 2),
            pass("", MINIMUM, 3, MAXIMUM, 5, IGNORE_MESSAGE, true), pass("ddd", MINIMUM, 3, MAXIMUM, 5),
            pass("dddd", MINIMUM, 3, MAXIMUM, 5), pass("abcdefg", MINIMUM, 3, MAXIMUM, 5, IGNORE_MESSAGE, true));
    }
}

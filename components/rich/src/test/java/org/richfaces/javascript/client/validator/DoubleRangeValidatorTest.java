/**
 *
 */
package org.richfaces.javascript.client.validator;

import java.util.List;
import java.util.Map;

import javax.faces.validator.DoubleRangeValidator;
import javax.faces.validator.Validator;

import org.junit.runners.Parameterized.Parameters;
import org.richfaces.javascript.client.RunParameters;

/**
 * @author asmirnov
 *
 */
public class DoubleRangeValidatorTest extends ValidatorTestBase {
    private static final String MINIMUM = "min";
    private static final String MAXIMUM = "max";

    /**
     * @param criteria
     */
    public DoubleRangeValidatorTest(RunParameters criteria) {
        super(criteria);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.javascript.client.validator.ValidatorTestBase#createValidator()
     */
    @Override
    protected Validator createValidator() {
        DoubleRangeValidator validator = new DoubleRangeValidator();
        Map<String, Object> options = getOptions();
        if (options.containsKey(MINIMUM)) {
            validator.setMinimum((Double) options.get(MINIMUM));
        }
        if (options.containsKey(MAXIMUM)) {
            validator.setMaximum((Double) options.get(MAXIMUM));
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
        return "validateDoubleRange";
    }

    @SuppressWarnings("deprecation")
    @Parameters
    public static List<RunParameters[]> parameters() {
        return options(pass(0L), pass(3L), pass(Double.MAX_VALUE), pass(0.0D, MINIMUM, 2.0D, IGNORE_MESSAGE, true),
            pass(2.0D, MINIMUM, 2.0D), pass(3.0D, MINIMUM, 2.0D), pass(-3.0D, MINIMUM, 2.0D, IGNORE_MESSAGE, true),
            pass(0.0D, MAXIMUM, 2.0D), pass(2.0D, MAXIMUM, 2.0D), pass(3.0D, MAXIMUM, 2.0D, IGNORE_MESSAGE, true),
            pass(-3.0D, MAXIMUM, 2.0D), pass(0.0D, MINIMUM, 3.0D, MAXIMUM, 5.0D, IGNORE_MESSAGE, true),
            pass(3.0D, MINIMUM, 3.0D, MAXIMUM, 5.0D), pass(4.0D, MINIMUM, 3.0D, MAXIMUM, 5.0D),
            pass(7.0D, MINIMUM, 3.0D, MAXIMUM, 5.0D, IGNORE_MESSAGE, true));
    }
}

/**
 *
 */
package org.richfaces.javascript.client.validator;

import java.util.List;
import java.util.Map;

import javax.faces.validator.LongRangeValidator;
import javax.faces.validator.Validator;

import org.junit.runners.Parameterized.Parameters;
import org.richfaces.javascript.client.RunParameters;

/**
 * @author asmirnov
 *
 */
public class LongRangeValidatorTest extends ValidatorTestBase {
    private static final String MINIMUM = "min";
    private static final String MAXIMUM = "max";

    /**
     * @param criteria
     */
    public LongRangeValidatorTest(RunParameters criteria) {
        super(criteria);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.javascript.client.validator.ValidatorTestBase#createValidator()
     */
    @Override
    protected Validator createValidator() {
        LongRangeValidator validator = new LongRangeValidator();
        Map<String, Object> options = getOptions();
        if (options.containsKey(MINIMUM)) {
            validator.setMinimum((Long) options.get(MINIMUM));
        }
        if (options.containsKey(MAXIMUM)) {
            validator.setMaximum((Long) options.get(MAXIMUM));
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
        return "validateLongRange";
    }

    @Parameters
    public static List<RunParameters[]> parameters() {
        return options(pass(0L), pass(3L), pass(Long.MAX_VALUE), pass(0L, MINIMUM, 2L), pass(2L, MINIMUM, 2L),
            pass(3L, MINIMUM, 2L), pass(-3L, MINIMUM, 2L), pass(0L, MAXIMUM, 2L), pass(2L, MAXIMUM, 2L), pass(3L, MAXIMUM, 2L),
            pass(-3L, MAXIMUM, 2L), pass(0L, MINIMUM, 3L, MAXIMUM, 5L), pass(3L, MINIMUM, 3L, MAXIMUM, 5L),
            pass(4L, MINIMUM, 3L, MAXIMUM, 5L), pass(7L, MINIMUM, 3L, MAXIMUM, 5L));
    }
}

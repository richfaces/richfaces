/**
 *
 */
package org.richfaces.javascript.client.validator;

import java.util.List;

import javax.validation.constraints.Max;

import org.junit.runners.Parameterized.Parameters;
import org.richfaces.javascript.client.RunParameters;

/**
 * @author asmirnov
 *
 */
public class MaxValidatorTest extends BeanValidatorTestBase {
    private static final String MAXIMUM = "value";

    /**
     * @param criteria
     */
    public MaxValidatorTest(RunParameters criteria) {
        super(criteria);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.javascript.client.MockTestBase#getJavaScriptFunctionName()
     */
    @Override
    protected String getJavaScriptFunctionName() {
        return "validateMax";
    }

    @Parameters
    public static List<RunParameters[]> parameters() {
        return options(pass(0, PROP, "number", MAXIMUM, 2), pass(2, PROP, "number", MAXIMUM, 2),
            pass(123, PROP, "number", MAXIMUM, 2));
    }

    public static final class Bean {
        @Max(2)
        public int getNumber() {
            return 0;
        }
    }

    @Override
    protected Class<?> getBeanType() {
        return Bean.class;
    }
}

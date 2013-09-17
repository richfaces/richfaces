/**
 *
 */
package org.richfaces.javascript.client.validator;

import java.util.List;
import java.util.Map;

import javax.faces.validator.RegexValidator;
import javax.faces.validator.Validator;

import org.junit.runners.Parameterized.Parameters;
import org.richfaces.javascript.client.RunParameters;

/**
 * @author asmirnov
 *
 */
public class RegexValidatorTest extends ValidatorTestBase {
    private static final String PATTERN = "pattern";

    /**
     * @param criteria
     */
    public RegexValidatorTest(RunParameters criteria) {
        super(criteria);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.javascript.client.validator.ValidatorTestBase#createValidator()
     */
    @Override
    protected Validator createValidator() {
        RegexValidator validator = new RegexValidator();
        Map<String, Object> options = getOptions();
        if (options.containsKey(PATTERN)) {
            validator.setPattern((String) options.get(PATTERN));
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
        return "validateRegex";
    }

    @Parameters
    public static List<RunParameters[]> parameters() {
        return options(/* pass(""),pass("aaa"),pass("123"), */
        pass("", PATTERN, ".*"), pass("vv", PATTERN, "\\S*"), pass("123", PATTERN, "\\d+"));
    }
}

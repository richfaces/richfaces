package org.richfaces.javascript.client.converter;

import java.util.List;
import java.util.Map;

import javax.faces.convert.Converter;
import javax.faces.convert.NumberConverter;

import org.junit.runners.Parameterized.Parameters;
import org.richfaces.javascript.client.RunParameters;

public class NumberConverterTest extends ConverterTestBase {
    private static final String TYPE = "type";

    public NumberConverterTest(RunParameters criteria) {
        super(criteria);
    }

    @Override
    protected Converter createConverter() {
        NumberConverter converter = new NumberConverter();
        Map<String, Object> options = getOptions();
        if (options.containsKey(TYPE)) {
            converter.setType((String) options.get(TYPE));
        }
        return converter;
    }

    @Override
    protected String getJavaScriptFunctionName() {
        return "convertNumber";
    }

    @Override
    protected void compareResult(Object convertedValue, Object jsConvertedValue) {
        compareNumbers(convertedValue, jsConvertedValue);
    }

    @Parameters
    public static List<RunParameters[]> getRunParameterss() {
        return options(
                pass("true"),
                pass("123"),
                pass("0"),
                pass("1"),
                pass("255"),
                pass("-128"),
                pass("-129"),
                pass("256"),
                pass("-0"),
                pass("0.05"));
    }
}

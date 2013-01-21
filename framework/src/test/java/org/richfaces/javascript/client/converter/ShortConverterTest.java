package org.richfaces.javascript.client.converter;

import java.util.List;

import javax.faces.convert.Converter;
import javax.faces.convert.ShortConverter;

import org.junit.runners.Parameterized.Parameters;
import org.richfaces.javascript.client.RunParameters;

public class ShortConverterTest extends ConverterTestBase {
    public ShortConverterTest(RunParameters criteria) {
        super(criteria);
    }

    @Override
    protected Converter createConverter() {
        ShortConverter byteConverter = new ShortConverter();
        return byteConverter;
    }

    @Override
    protected String getJavaScriptFunctionName() {
        return "convertShort";
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
                pass(Long.toString(Long.MAX_VALUE)),
                pass(Short.toString(Short.MIN_VALUE)),
                pass(Short.toString(Short.MAX_VALUE)),
                pass("-129"),
                pass("256"),
                pass("-0"),
                pass("0.05"));
    }
}

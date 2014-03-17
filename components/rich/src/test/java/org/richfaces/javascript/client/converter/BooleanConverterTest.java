package org.richfaces.javascript.client.converter;

import java.util.List;

import javax.faces.convert.BooleanConverter;
import javax.faces.convert.Converter;

import org.junit.runners.Parameterized.Parameters;
import org.richfaces.javascript.client.RunParameters;

public class BooleanConverterTest extends ConverterTestBase {
    public BooleanConverterTest(RunParameters criteria) {
        super(criteria);
    }

    @Override
    protected Converter createConverter() {
        return new BooleanConverter();
    }

    @Override
    protected String getJavaScriptFunctionName() {
        return "convertBoolean";
    }

    @Parameters
    public static List<RunParameters[]> getRunParameterss() {
        return options(pass("true"), pass("ok"), pass("123"), pass("0"), pass("1"), pass("no"));
    }
}

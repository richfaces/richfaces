package org.richfaces.javascript.client.converter;

import static org.junit.Assert.*;


import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.ajax4jsf.javascript.JSFunction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.richfaces.javascript.client.MockTestBase;
import org.richfaces.javascript.client.RunParameters;


public abstract class ConverterTestBase extends MockTestBase {

    public ConverterTestBase(RunParameters criteria) {
        super(criteria);
    }

    @Test
    public void testConvert() throws Exception {
        Converter converter = createConverter();
        try {
            Object convertedValue =
                converter.getAsObject(facesEnvironment.getFacesContext(), component, criteria.getValue().toString());
            Object jsConvertedValue =
                convertOnClient();
            assertEquals(convertedValue, jsConvertedValue);
        } catch (ConverterException e) {
            // JSF conversion error - JavaScript should throw exception too.
            try {
                convertOnClient();
                assertFalse("Client-side converted didn't throw exception for value:"+criteria.getValue(), true);
            } catch (Exception jsException){
                // Test passed
            }
        }
    }

    protected Object convertOnClient() throws ConverterException {
        JSFunction clientSideFunction = new JSFunction("RichFaces.csv." + getJavaScriptFunctionName(),criteria.getValue(),getErrorMessage(),getJavaScriptOptions());
        return qunit.runScript(clientSideFunction.toScript());
    }

    protected abstract Converter createConverter();

}
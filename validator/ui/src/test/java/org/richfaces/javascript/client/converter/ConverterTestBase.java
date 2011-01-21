package org.richfaces.javascript.client.converter;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.*;

import javax.faces.application.FacesMessage;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.validator.Validator;

import net.sourceforge.htmlunit.corejs.javascript.JavaScriptException;
import net.sourceforge.htmlunit.corejs.javascript.NativeObject;

import org.ajax4jsf.javascript.JSFunction;
import org.junit.Test;
import org.richfaces.javascript.Message;
import org.richfaces.javascript.client.MockTestBase;
import org.richfaces.javascript.client.RunParameters;
import org.richfaces.validator.ConverterServiceImpl;

import com.gargoylesoftware.htmlunit.ScriptException;


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
                convertOnClient(converter);
            if(null != convertedValue || null != jsConvertedValue){
                compareResult(convertedValue, jsConvertedValue);
            }
        } catch (ConverterException e) {
            // JSF conversion error - JavaScript should throw exception too.
            try {
                convertOnClient(converter);
                assertFalse("Client-side converted didn't throw exception for value:"+criteria.getValue(), true);
            } catch (ScriptException jsException){
                // Test passed
                Throwable cause = jsException.getCause();
                assertTrue(cause instanceof JavaScriptException);
                NativeObject value = (NativeObject) ((JavaScriptException) cause).getValue();
                assertEquals(e.getFacesMessage().getDetail(), value.get("detail"));
                assertEquals(e.getFacesMessage().getSummary(), value.get("summary"));
            }
        }
    }

    protected void compareResult(Object convertedValue, Object jsConvertedValue) {
        assertEquals(convertedValue, jsConvertedValue);
    }

    protected Object convertOnClient(Converter converter) throws ConverterException {
        JSFunction clientSideFunction = new JSFunction("RichFaces.csv." + getJavaScriptFunctionName(),criteria.getValue(),TEST_COMPONENT_ID,getJavaScriptOptions(), getErrorMessage(converter));
        return qunit.runScript(clientSideFunction.toScript());
    }

    private Object getErrorMessage(Converter converter) {
        ConverterServiceImpl converterService = new ConverterServiceImpl();
        FacesMessage message = converterService.getMessage(facesEnvironment.getFacesContext(), converter);
        return new Message(message);
    }

    protected abstract Converter createConverter();

    protected void compareNumbers(Object convertedValue, Object jsConvertedValue) {
        assertTrue(jsConvertedValue instanceof Double);
        assertTrue(convertedValue instanceof Number);
        Double jsDouble = (Double) jsConvertedValue;
        Double jsfDouble = Double.valueOf(((Number)convertedValue).doubleValue());
        assertEquals(jsfDouble, jsDouble,0.0000001);
    }

}
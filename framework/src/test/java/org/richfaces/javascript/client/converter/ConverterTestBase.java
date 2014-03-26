/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.richfaces.javascript.client.converter;

import com.gargoylesoftware.htmlunit.ScriptException;

import net.sourceforge.htmlunit.corejs.javascript.JavaScriptException;
import net.sourceforge.htmlunit.corejs.javascript.NativeObject;

import org.ajax4jsf.javascript.JSFunction;
import org.junit.Test;
import org.richfaces.javascript.client.MockTestBase;
import org.richfaces.javascript.client.RunParameters;
import org.richfaces.validator.ConverterServiceImpl;
import org.richfaces.validator.Message;

import javax.faces.application.FacesMessage;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public abstract class ConverterTestBase extends MockTestBase {
    public ConverterTestBase(RunParameters criteria) {
        super(criteria);
    }

    @Test
    public void testConvert() throws Exception {
        Converter converter = createConverter();
        try {
            Object convertedValue = converter.getAsObject(facesEnvironment.getFacesContext(), input, criteria.getValue()
                .toString());
            Object jsConvertedValue = convertOnClient(converter);
            if (null != convertedValue || null != jsConvertedValue) {
                compareResult(convertedValue, jsConvertedValue);
            }
        } catch (ConverterException e) {
            // JSF conversion error - JavaScript should throw exception too.
            try {
                convertOnClient(converter);
                fail("Client-side converted didn't throw exception for value:" + criteria.getValue());
            } catch (ScriptException jsException) {
                // Test passed
                Throwable cause = jsException.getCause();
                assertTrue(cause instanceof JavaScriptException);
                NativeObject value = (NativeObject) ((JavaScriptException) cause).getValue();

                String facesMessageDetail = unifyMessage(e.getFacesMessage().getDetail());
                String facesMessageSummary = unifyMessage(e.getFacesMessage().getSummary());

                String javaScriptMessageDetail = unifyMessage((String) value.get("detail"));
                String javaScriptMessageSummary = unifyMessage((String) value.get("summary"));

                assertEquals(facesMessageDetail, javaScriptMessageDetail);
                assertEquals(facesMessageSummary, javaScriptMessageSummary);
            }
        }
    }

    private String unifyMessage(String message) {
        return removeExampleValue(message);
    }

    private String removeExampleValue(String message) {
        return message.replaceAll("Example: .*$", "");
    }

    protected void compareResult(Object convertedValue, Object jsConvertedValue) {
        assertEquals(convertedValue, jsConvertedValue);
    }

    protected Object convertOnClient(Converter converter) throws ConverterException {
        JSFunction clientSideFunction = new JSFunction("RichFaces.csv." + getJavaScriptFunctionName(), criteria.getValue(),
            TEST_COMPONENT_ID, getJavaScriptOptions(), getErrorMessage(converter));
        return qunit.runScript(clientSideFunction.toScript());
    }

    private Object getErrorMessage(Converter converter) {
        ConverterServiceImpl converterService = new ConverterServiceImpl();
        FacesMessage message = converterService.getMessage(facesEnvironment.getFacesContext(), converter, input, null);
        return new Message(message);
    }

    protected abstract Converter createConverter();

    protected void compareNumbers(Object convertedValue, Object jsConvertedValue) {
        assertTrue(jsConvertedValue instanceof Double);
        assertTrue(convertedValue instanceof Number);
        Double jsDouble = (Double) jsConvertedValue;
        Double jsfDouble = Double.valueOf(((Number) convertedValue).doubleValue());
        assertEquals(jsfDouble, jsDouble, 0.0000001);
    }
}
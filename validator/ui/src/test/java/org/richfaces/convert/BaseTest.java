/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.convert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.Map.Entry;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.el.VariableMapper;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

import org.ajax4jsf.javascript.JSFunction;
import org.ajax4jsf.javascript.ScriptUtils;
import org.jboss.test.faces.htmlunit.HtmlUnitEnvironment;
import org.jboss.test.faces.jetty.JettyServer;
import org.junit.After;
import org.junit.Before;
import org.richfaces.application.MessageFactory;
import org.richfaces.application.ServiceTracker;

import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.common.collect.Maps;

/**
 * @author Nick Belaevski
 * 
 */
public abstract class BaseTest {

    public static final String TEST_COMPONENT_LABEL = "input";

    public class TestBean {

        private static final String CONVERTER_VAR = "__converter";

        private static final String VALIDATOR_VAR = "__validator";

        private String objectId;

        private String clientFunction;

        private String valueAsScript;

        private String errorMessage;

        private Enum<?>[] errorMessageEnums;

        private Exception resultException;

        private Map<String, Object> attributes = new HashMap<String, Object>();

        private String parametersString;

        private String componentLabel;

        public String getComponentLabel() {
            return this.componentLabel;
        }

        public void setComponentLabel(String componentLabel) {
            this.componentLabel = componentLabel;
        }

        public void setClientFunction(String clientFunction) {
            this.clientFunction = clientFunction;
        }

        public String getClientFunction() {
            return clientFunction;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public String getSubmittedValue() {
            return submittedValue;
        }

        public String getSubmittedValueAsScript() {
            return ScriptUtils.toScript(submittedValue);
        }

        public String getErrorMessagesAsScript() {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            final Map<Object, Object> result = Maps.newHashMap();

            if (errorMessageEnums != null) {
                for (Enum<?> errorMessageEnum : errorMessageEnums) {
                    MessageFactory messageFactory = ServiceTracker.getService(MessageFactory.class);
                    String errorMessage = messageFactory.getMessageFormat(facesContext, errorMessageEnum);

                    result.put(errorMessageEnum.name(), MessageFormat.format(errorMessage, "{0}"));
                }
            }
            return ScriptUtils.toScript(result);
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public void setAttribute(String name, Object value) {
            attributes.put(name, value);
        }

        public String getParametersString() {
            return parametersString;
        }

        public Exception getResultException() {
            return resultException;
        }

        public void setErrorMessageEnums(Enum<?>[] errorMessageEnums) {
            this.errorMessageEnums = errorMessageEnums;
        }

        private void configure(FacesContext facesContext, Object object, String bindingVarName) {
            ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();

            VariableMapper varMapper = facesContext.getELContext().getVariableMapper();
            ValueExpression originalExpr = varMapper.resolveVariable(bindingVarName);
            try {
                varMapper.setVariable(bindingVarName, expressionFactory.createValueExpression(object, Object.class));

                for (Entry<String, Object> mapEntry : attributes.entrySet()) {

                    String exprString = MessageFormat.format("#'{'{0}.{1}'}'", bindingVarName, mapEntry.getKey());
                    ValueExpression ve =
                        expressionFactory.createValueExpression(facesContext.getELContext(), exprString, Object.class);

                    ve.setValue(facesContext.getELContext(), mapEntry.getValue());
                }
            } finally {
                varMapper.setVariable(bindingVarName, originalExpr);
            }
        }

        private Converter createConverter(FacesContext facesContext) {
            Converter converter = facesContext.getApplication().createConverter(objectId);
            configure(facesContext, converter, CONVERTER_VAR);
            return converter;
        }

        private Validator createValidator(FacesContext facesContext) {
            Validator validator = facesContext.getApplication().createValidator(objectId);
            configure(facesContext, validator, VALIDATOR_VAR);
            return validator;
        }

        private UIComponent createTestComponent() {
            HtmlInputText testComponent = new HtmlInputText();
            testComponent.setLabel(TEST_COMPONENT_LABEL);
            testBean.componentLabel = TEST_COMPONENT_LABEL;
            if (errorMessage!=null) {
            	testComponent.setRequiredMessage(errorMessage);
            }
            return testComponent;
        }

        public String getConvertedValueAsScript() {
            return valueAsScript != null ? valueAsScript : "null";
        }

        public void initConverter() {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            Converter converter = createConverter(facesContext);
            try {
                Object convertedObject = converter.getAsObject(facesContext, createTestComponent(), submittedValue);
                valueAsScript = serializeObjectToScript(convertedObject);
            } catch (ConverterException e) {
                this.resultException = e;
            }

            init();
        }

        public void initValidator() {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            Validator validator = createValidator(facesContext);
            try {
                validator.validate(facesContext, createTestComponent(), submittedValue);
            } catch (ValidatorException e) {
                this.resultException = e;
            }

            init();
        }

        protected void init() {
            if (objectId == null || clientFunction == null) {
                throw new NullPointerException();
            }

            Map<String, Object> map = new HashMap<String, Object>(attributes);
            map.put("componentId", TEST_COMPONENT_LABEL);
            if (errorMessage != null) {
                map.put("customMessage", MessageFormat.format(errorMessage, "{0}"));
            }

            parametersString = ScriptUtils.toScript(map);
        }
    }

    private TestBean testBean;

    private HtmlUnitEnvironment environment;

    private String converterScriptName;

    private String submittedValue;

    private String failureMessage;

    public BaseTest(String converterScriptName) {
        this.converterScriptName = converterScriptName;
    }

    @Before
    public void setUp() throws Exception {
    	Locale.setDefault(Locale.US);
        testBean = new TestBean();

        environment = new HtmlUnitEnvironment(new JettyServer());
        environment.getServer().addResource("/testConverter.xhtml", BaseTest.class.getResource("testConverter.xhtml"));
        environment.getServer().addResource("/testValidator.xhtml", BaseTest.class.getResource("testValidator.xhtml"));
        environment.getServer().addResource("/resources/resource.js", converterScriptName);
        environment.getServer().addWebListener(new ServletRequestListener() {

            public void requestInitialized(ServletRequestEvent sre) {
                sre.getServletRequest().setAttribute("testBean", testBean);
            }

            public void requestDestroyed(ServletRequestEvent sre) {
            }
        });

        environment.start();
    }

    @After
    public void tearDown() throws Exception {
        submittedValue = null;
        failureMessage = null;

        testBean = null;
        if (environment != null) {
            environment.release();
            environment = null;
        }
    }

    protected String serializeObjectToScript(Object object) {
        if (object instanceof Date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date) object);
            calendar.setTimeZone(TimeZone.getTimeZone("UTC"));

            return new JSFunction("new Date", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND), calendar.get(Calendar.MILLISECOND)).toScript();
        }

        return ScriptUtils.toScript(object);
    }

    public void setErrorMessage(String errorMessage) {
        testBean.setErrorMessage(errorMessage);
    }

    public void setObjectId(String objectId) {
        testBean.setObjectId(objectId);
    }

    public void setClientFunction(String clientFunction) {
        testBean.setClientFunction(clientFunction);
    }

    public void setAttribute(String name, Object value) {
        testBean.setAttribute(name, value);
    }

    public void setSubmittedValue(String submittedValue) {
        this.submittedValue = submittedValue;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }

    public void setErrorMessageEnums(Enum<?>... errorMessageEnums) {
        testBean.setErrorMessageEnums(errorMessageEnums);
    }

    public void assertConversionOk() throws Exception {
        HtmlPage page = environment.getPage("/testConverter.jsf");
        System.out.println(page.asXml());
        assertTrue(page.getWebClient().isJavaScriptEnabled());
        ScriptResult conversionMessageResult = page.executeJavaScript("verifyConversion()");
        if (!ScriptResult.isUndefined(conversionMessageResult)) {
            fail(conversionMessageResult.getJavaScriptResult().toString());
        }
    }

    public void assertValidationOk() throws Exception {
        HtmlPage page = environment.getPage("/testValidator.jsf");
        System.out.println(page.asXml());
        assertTrue(page.getWebClient().isJavaScriptEnabled());
        ScriptResult messageResult = page.executeJavaScript("verifyValidation()");
        if (!ScriptResult.isUndefined(messageResult)) {
            fail(messageResult.getJavaScriptResult().toString());
        }
    }

    public void assertConversionFailure() throws Exception {
        HtmlPage page = environment.getPage("/testConverter.jsf");

        assertNotNull(testBean.getResultException());

        assertTrue(page.getWebClient().isJavaScriptEnabled());
        ScriptResult conversionErrorResult = page.executeJavaScript("window.conversionError");

        if (ScriptResult.isUndefined(conversionErrorResult)) {
            fail("converter has not thrown an error");
            return;
        }

        Object conversionErrorMessage;

        Object scriptResult = conversionErrorResult.getJavaScriptResult();
        if (scriptResult instanceof Scriptable) {
            Scriptable scriptable = (Scriptable) scriptResult;
            conversionErrorMessage = scriptable.get("message", scriptable);
        } else {
            conversionErrorMessage = scriptResult;
        }

        if (failureMessage != null) {
            assertEquals(failureMessage, conversionErrorMessage);
        } else {
            assertEquals(testBean.getResultException().getMessage(), conversionErrorMessage);
        }
    }

    public void assertValidationFailure() throws Exception {
        HtmlPage page = environment.getPage("/testValidator.jsf");

        assertNotNull(testBean.getResultException());

        assertTrue(page.getWebClient().isJavaScriptEnabled());
        ScriptResult validationErrorResult = page.executeJavaScript("window.validationError");

        if (ScriptResult.isUndefined(validationErrorResult)) {
            fail("validator has not thrown an error");
            return;
        }

        Object validationErrorMessage;

        Object scriptResult = validationErrorResult.getJavaScriptResult();
        if (scriptResult instanceof Scriptable) {
            Scriptable scriptable = (Scriptable) scriptResult;
            validationErrorMessage = scriptable.get("message", scriptable);
        } else {
            validationErrorMessage = scriptResult;
        }

        if (failureMessage != null) {
            assertEquals(failureMessage, validationErrorMessage);
        } else {
            assertEquals(testBean.getResultException().getMessage(), validationErrorMessage);
        }
    }

    public void setup(TestData testData) {
        setSubmittedValue(testData.submittedValue());
        setFailureMessage(testData.failureMessage());
    }

}

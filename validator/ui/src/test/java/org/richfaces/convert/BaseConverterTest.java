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
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.el.VariableMapper;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

import org.ajax4jsf.javascript.JSFunction;
import org.ajax4jsf.javascript.ScriptUtils;
import org.jboss.test.faces.htmlunit.HtmlUnitEnvironment;
import org.jboss.test.faces.jetty.JettyServer;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.richfaces.application.MessageFactory;
import org.richfaces.application.ServiceTracker;

import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.common.base.Strings;

/**
 * @author Nick Belaevski
 * 
 */
@RunWith(BaseConverterTestRunner.class)
public abstract class BaseConverterTest {

	public static final String TEST_COMPONENT_LABEL = "input";

	public class TestBean {

		private static final String CONVERTER_VAR = "__converter";

		private String converterId; 

		private String clientConverterClassName;

		private String convertedValueAsScript;

		private String conversionErrorMessage;

		private Enum<?> conversionErrorMessageEnum;

		private ConverterException converterException;

		private Map<String, Object> attributes = new HashMap<String, Object>();

		private String converterParametersString;

		public void setClientConverterClassName(String clientConverterClassName) {
			this.clientConverterClassName = clientConverterClassName;
		}

		public String getClientConverterClassName() {
			return clientConverterClassName;
		}

		public void setConverterId(String converterId) {
			this.converterId = converterId;
		}

		public String getSubmittedValue() {
			return submittedValue;
		}

		public String getSubmittedValueAsScript() {
			return ScriptUtils.toScript(submittedValue);
		}

		public String getConversionErrorMessage() {
			return conversionErrorMessage;
		}

		public void setConversionErrorMessage(String conversionErrorMessage) {
			this.conversionErrorMessage = conversionErrorMessage;
		}

		public void setAttribute(String name, Object value) {
			attributes.put(name, value);
		}

		public String getConverterParametersString() {
			return converterParametersString;
		}

		public ConverterException getConverterException() {
			return converterException;
		}

		public void setConversionErrorMessageEnum(
				Enum<?> conversionErrorMessageEnum) {
			this.conversionErrorMessageEnum = conversionErrorMessageEnum;
		}

		private void configure(FacesContext facesContext, Converter converter) {
			ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();

			VariableMapper varMapper = facesContext.getELContext().getVariableMapper();
			ValueExpression originalExpr = varMapper.resolveVariable(CONVERTER_VAR);
			try {
				varMapper.setVariable(CONVERTER_VAR, expressionFactory.createValueExpression(converter, Converter.class));

				for (Entry<String, Object> mapEntry: attributes.entrySet()) {

					String exprString = MessageFormat.format("#'{'{0}.{1}'}'", CONVERTER_VAR, mapEntry.getKey());
					ValueExpression ve = expressionFactory.createValueExpression(facesContext.getELContext(), exprString, Object.class);

					ve.setValue(facesContext.getELContext(), mapEntry.getValue());
				}
			} finally {
				varMapper.setVariable(CONVERTER_VAR, originalExpr);
			}
		}

		private Converter createConverter(FacesContext facesContext) {
			Converter converter = facesContext.getApplication().createConverter(converterId);
			configure(facesContext, converter);
			return converter;
		}

		private UIComponent createTestComponent() {
			HtmlInputText testComponent = new HtmlInputText();
			testComponent.setLabel(TEST_COMPONENT_LABEL);
			return testComponent;
		}

		public String getConvertedValueAsScript() {
			return convertedValueAsScript != null ? convertedValueAsScript : "null";
		}

		public void init() {
			if (converterId == null || clientConverterClassName == null) {
				throw new NullPointerException();
			}

			FacesContext facesContext = FacesContext.getCurrentInstance();
			Converter converter = createConverter(facesContext);
			try {
				Object convertedObject = converter.getAsObject(facesContext, createTestComponent(), submittedValue);
				convertedValueAsScript = serializeObjectToScript(convertedObject);
			} catch (ConverterException e) {
				this.converterException = e;
			}

			if (Strings.isNullOrEmpty(conversionErrorMessage) && conversionErrorMessageEnum != null) {
				MessageFactory messageFactory = ServiceTracker.getService(MessageFactory.class);
				conversionErrorMessage = messageFactory.getMessageFormat(facesContext, conversionErrorMessageEnum);
			}
			
			Map<String,Object> map = new HashMap<String, Object>(attributes);
			map.put("componentId", TEST_COMPONENT_LABEL);
			map.put("customMessage", MessageFormat.format(conversionErrorMessage , "{0}"));

			converterParametersString = ScriptUtils.toScript(map);
		}
	}

	private TestBean testBean;

	private HtmlUnitEnvironment environment;

	private String converterScriptName;

	private String submittedValue;

	private String failureMessage;

	public BaseConverterTest(String converterScriptName) {
		this.converterScriptName = converterScriptName;
	}

	@Before
	public void setUp() throws Exception {
		testBean = new TestBean();

		environment = new HtmlUnitEnvironment(new JettyServer());
		environment.getServer().addResource("/test.xhtml", BaseConverterTest.class.getResource("test.xhtml"));
		environment.getServer().addResource("/resources/converter.js", converterScriptName);
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

		environment.release();
		environment = null;
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

	public void setConversionErrorMessage(String conversionErrorMessage) {
		testBean.setConversionErrorMessage(conversionErrorMessage);
	}

	public void setConverterId(String converterId) {
		testBean.setConverterId(converterId);
	}

	public void setClientConverterClassName(String clientConverterClassName) {
		testBean.setClientConverterClassName(clientConverterClassName);
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

	public void setConversionErrorMessageEnum(
			Enum<?> conversionErrorMessageEnum) {
		testBean.setConversionErrorMessageEnum(conversionErrorMessageEnum);
	}

	public void assertConversionOk() throws Exception {
		HtmlPage page = environment.getPage("/test.jsf");
		System.out.println(page.asXml());
		assertTrue(page.getWebClient().isJavaScriptEnabled());
		ScriptResult conversionMessageResult = page.executeJavaScript("verifyConversion()");
		if (!ScriptResult.isUndefined(conversionMessageResult)) {
			fail(conversionMessageResult.getJavaScriptResult().toString());
		}
	}

	public void assertConversionFailure() throws Exception {
		HtmlPage page = environment.getPage("/test.jsf");

		assertNotNull(testBean.getConverterException());

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
			conversionErrorMessage =  scriptable.get("message", scriptable);
		} else {
			conversionErrorMessage = scriptResult;
		}

		if (failureMessage != null) {
			assertEquals(failureMessage, conversionErrorMessage);
		} else {
			assertEquals(testBean.getConverterException().getMessage(), conversionErrorMessage);
		}
	}

	public void setup(TestData testData) {
		setSubmittedValue(testData.submittedValue());
		setFailureMessage(testData.failureMessage());
	}

}

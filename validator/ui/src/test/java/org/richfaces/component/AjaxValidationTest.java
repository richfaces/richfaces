package org.richfaces.component;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;

import java.io.IOException;
import java.net.MalformedURLException;

import org.hamcrest.Matcher;
import org.jboss.test.faces.htmlunit.HtmlUnitEnvironment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;


/**
 * Test for dynamic add/remove {@link UIValidatorScript} as view resource. 
 * @author asmirnov
 *
 */
public class AjaxValidationTest {
    
    private HtmlUnitEnvironment environment;
    
    
    @Before
    public void setUp() {
        this.environment = new HtmlUnitEnvironment();
        this.environment.withWebRoot("org/richfaces/component/test.xhtml").start();
    }
    
    @After
    public void thearDown() throws Exception{
        environment.release();
        environment = null;
    }

    @Test
    public void testRequest() throws Exception {
        HtmlPage page = requestPage();
        HtmlInput input = hetInput(page);
        assertNotNull(input);

    }

    @Test
    public void testSubmitTooShortValue() throws Exception {
        submitValueAndCheckMesage("",containsString("Error"));
    }

    @Test
    public void testSubmitTooLongValue() throws Exception {
        submitValueAndCheckMesage("123456",containsString("Error"));
    }

    @Test
    public void testSubmitProperValue() throws Exception {
        submitValueAndCheckMesage("ab",equalTo(""));
    }

    private void submitValueAndCheckMesage(String value, Matcher<String> matcher) throws Exception {
        HtmlPage page = requestPage();
        HtmlInput input = hetInput(page);
        input.setValueAttribute(value);
        input.fireEvent("keyup");
        HtmlElement message = page.getElementById("form:message");
        assertThat(message.getTextContent(), matcher);
//        System.out.println(page.asXml());
    }
    private HtmlInput hetInput(HtmlPage page) {
        HtmlForm htmlForm = page.getFormByName("form");
        assertNotNull(htmlForm);
        HtmlInput input = htmlForm.getInputByName("form:text");
        return input;
    }

    private HtmlPage requestPage() throws MalformedURLException, IOException {
        HtmlPage page = environment.getPage("/test.jsf");
//        System.out.println(page.asXml());
        return page;
    }


}

package org.richfaces.ui.validation;

import static org.junit.Assert.assertThat;

import java.net.URL;

import org.hamcrest.Matcher;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class ValidationTestBase {

    @Drone
    protected WebDriver browser;

    @ArquillianResource
    protected URL contextPath;

    @FindBy(name = "form:text")
    protected WebElement inputText;

    @FindBy(css = "body")
    protected WebElement body;

    protected void submitValueAndCheckMessage(String value, Matcher<String> matcher) throws Exception {
        browser.get(contextPath.toString());
        inputText.clear();
        inputText.sendKeys(value);
        submitValue();
        checkMessage("uiMessage", matcher);
    }

    protected void submitValue() {
        body.click(); // blur
    }

    protected void checkMessage(String messageId, Matcher<String> matcher) {
        WebElement message = browser.findElement(By.id(messageId));
        assertThat(message.getText().trim(), matcher);
    }
}

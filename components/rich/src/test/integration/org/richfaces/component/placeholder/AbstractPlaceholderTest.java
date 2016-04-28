/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.component.placeholder;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.arquillian.graphene.Graphene.waitModel;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.net.URL;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.arquillian.page.source.SourceChecker;
import org.richfaces.component.ColorUtils;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import category.Smoke;
import com.google.common.base.Predicate;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@RunAsClient
@RunWith(Arquillian.class)
public abstract class AbstractPlaceholderTest {

    protected static final Color DEFAULT_PLACEHOLDER_COLOR = new Color(119, 119, 119);
    protected static final String EMPTY_STRING = "";
    protected static final String INPUT_ID = "input";
    protected static final String INPUT_SELECTOR = "[id=input]";
    protected static final String PLACEHOLDER_CLASS = "rf-plhdr";
    protected static final String PLACEHOLDER_ID = "placeholderID";
    protected static final String PLACEHOLDER_TEXT = "Placeholder Text";
    protected static final String SECOND_INPUT_ID = "second-input";
    protected static final String SECOND_INPUT_SELECTOR = "[id=second-input]";
    protected static final String TESTED_VALUE = "some value";

    @FindBy(css = "[id$=ajaxSubmit]")
    private WebElement a4jSubmitBtn;
    @FindBy(css = "input[id$=blurButton]")
    private WebElement blurButton;
    @FindBy(tagName = "body")
    private WebElement body;

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;
    @FindBy(css = "[id$=httpSubmit]")
    private WebElement httpSubmitBtn;
    @FindBy(css = "[id$=output]")
    private WebElement output;
    @FindBy(id = PLACEHOLDER_ID)
    private GrapheneElement placeholderElement;
    @ArquillianResource
    private SourceChecker sourceChecker;

    protected static FaceletAsset placeholderFacelet(String name, RichDeployment deployment) {
        FaceletAsset p;
        p = deployment.baseFacelet(name);
        p.head("<style> input, textarea { color: #000; } </style>");
        p.body("<input type='button' value='blur' id='blurButton'/>");
        p.body("<br />");
        return p;
    }

    protected void blur() {
        blurButton.click();
    }

    public WebElement getA4jSubmitBtn() {
        return a4jSubmitBtn;
    }

    public WebElement getBlurButton() {
        return blurButton;
    }

    public WebElement getBody() {
        return body;
    }

    public WebDriver getBrowser() {
        return browser;
    }

    public URL getContextPath() {
        return contextPath;
    }

    protected Color getDefaultInputColor() {
        return Color.BLACK;
    }

    public WebElement getHttpSubmitBtn() {
        return httpSubmitBtn;
    }

    public WebElement getOutput() {
        return output;
    }

    public GrapheneElement getPlaceholderElement() {
        return placeholderElement;
    }

    public SourceChecker getSourceChecker() {
        return sourceChecker;
    }

    protected String getTestedValue() {
        return TESTED_VALUE;
    }

    protected String getTestedValueResponse() {
        return TESTED_VALUE;
    }

    abstract Input input();

    @Test
    public void testAjaxSendsEmptyValue() {
        // given
        browser.get(contextPath.toExternalForm() + "submit.jsf");
        input().setTestedValue(getTestedValue());
        blur();

        guardAjax(a4jSubmitBtn).click();

        waitAjax().until().element(output).is().visible();
        waitAjax().until().element(output).text().equalTo(getTestedValueResponse());

        // when
        input().clear();
        guardAjax(a4jSubmitBtn).click();

        // then
        waitAjax().until().element(output).is().not().visible();
    }

    @Test
    public void testAjaxSendsTextValue() {
        // given
        browser.get(contextPath.toExternalForm() + "submit.jsf");
        // when
        input().setTestedValue(getTestedValue());
        guardAjax(a4jSubmitBtn).click();

        // then
        waitAjax().until().element(output).is().visible();
        waitAjax().until().element(output).text().equalTo(getTestedValueResponse());
    }

    @Test
    public void testConverter() {
        // having
        browser.get(contextPath.toExternalForm() + "converter.jsf");

        // then
        assertEquals(PlaceHolderValue.DEFAULT_VALUE, input().getDefaultText());
    }

    @Test
    @Category(Smoke.class)
    public void testDefaultAttributes() {
        // having
        browser.get(contextPath.toExternalForm() + "index.jsf");

        // then
        assertEquals(PLACEHOLDER_TEXT, input().getDefaultText());
        assertEquals(DEFAULT_PLACEHOLDER_COLOR, input().getTextColor());
        assertTrue("placeholder does not contain default class",
            input().getStyleClass().contains(PLACEHOLDER_CLASS));
    }

    @Test
    public void testRendered() {
        // having
        browser.get(contextPath.toExternalForm() + "rendered.jsf");
        // then
        assertFalse("Placeholder should not be present.", placeholderElement.isPresent());
    }

    @Test
    public void testSelector() {
        // having
        browser.get(contextPath.toExternalForm() + "selector.jsf");
        // then
        assertEquals(PLACEHOLDER_TEXT, input().getDefaultText());
    }

    @Test
    public void testStyleClass() {
        // having
        String className = "some-class";
        browser.get(contextPath.toExternalForm() + "index.jsf?styleClass=" + className);
        // then
        assertTrue("input should contain placeholder's default class", input().getStyleClass().contains(PLACEHOLDER_CLASS));
        assertTrue("input should contain specified class", input().getStyleClass().contains(className));
    }

    @Test
    public void testSubmitEmptyValue() {
        // given
        browser.get(contextPath.toExternalForm() + "submit.jsf");

        // when
        guardHttp(httpSubmitBtn).click();

        // then
        waitModel().until().element(output).is().not().visible();
    }

    @Test
    public void testSubmitTextValue() {
        // given
        browser.get(contextPath.toExternalForm() + "submit.jsf");
        // when
        input().setTestedValue(getTestedValue());
        guardHttp(httpSubmitBtn).click();
        // then
        waitModel().until().element(output).is().visible();
        assertEquals(getTestedValueResponse(), output.getText());
    }

    @Test
    public void testWhenInputWithPlaceholderGainsFocus_placeholderIsRemoved() {
        // having
        browser.get(contextPath.toExternalForm() + "index.jsf");

        // when
        input().focus();
        waitGui().until(new Predicate<WebDriver>() {

            @Override
            public boolean apply(WebDriver input) {
                return input().getEditedText().isEmpty();
            }
        });
    }

    @Test
    public void testWhenTextIsChangedAndInputIsBlurred_typedTextIsPreserved() {
        // having
        browser.get(contextPath.toExternalForm() + "index.jsf");

        // when
        input().setTestedValue(getTestedValue());
        blur();
        waitGui().until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver input) {
                return input().getEditedText().equals(getTestedValue());
            }
        });
        // then
        assertEquals(getTestedValue(), input().getEditedText());
        assertEquals(getDefaultInputColor(), input().getTextColor());
    }

    @Test
    public void testWhenTextIsChanged_textChangesColorToDefaultAndRemovesPlaceholderStyleClasses() {
        // having
        browser.get(contextPath.toExternalForm() + "index.jsf");
        input().setTestedValue(getTestedValue());
        waitGui().until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver input) {
                return !input().getStyleClass().contains(PLACEHOLDER_CLASS);
            }
        });
        // then
        assertFalse("input should not contain placeholder class", input().getStyleClass().contains(PLACEHOLDER_CLASS));
        assertEquals(getDefaultInputColor(), input().getTextColor());
        assertEquals(getTestedValue(), input().getEditedText());
    }

    @Test
    public void testWhenTextIsCleared_inputGetsPlaceholderTextAndStyleAgain() {
        // having
        browser.get(contextPath.toExternalForm() + "index.jsf");

        // when
        input().setTestedValue(getTestedValue());
        input().clear();
        blur();
        waitGui().until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver input) {
                return input().getDefaultText().equals(PLACEHOLDER_TEXT);
            }
        });
        // then
        assertEquals(PLACEHOLDER_TEXT, input().getDefaultText());
        assertEquals(DEFAULT_PLACEHOLDER_COLOR, input().getTextColor());
        assertTrue("input should contain placeholder's default class", input().getStyleClass().contains(PLACEHOLDER_CLASS));
    }

    public static class InplaceInput extends Input {

        @FindBy(css = "input[id$=Input]")
        private WebElement inplaceInput;
        @FindBy(css = "span[id$=Label]")
        private WebElement inplaceLabel;

        @Override
        public void clear() {
            focus();
            inplaceInput.clear();
        }

        @Override
        public void focus() {
            inplaceLabel.click();
        }

        @Override
        public String getDefaultText() {
            return inplaceLabel.getText();
        }

        @Override
        public String getEditedText() {
            return inplaceInput.getAttribute("value");
        }

        public WebElement getInplaceInput() {
            return inplaceInput;
        }

        @Override
        public String getStyleClass() {
            return inplaceLabel.getAttribute("class");
        }

        @Override
        public Color getTextColor() {
            return ColorUtils.convertToAWTColor(inplaceLabel.getCssValue("color"));
        }

        @Override
        public void setTestedValue(String value) {
            focus();
            inplaceInput.sendKeys(value);
        }

    }

    public static class InplaceSelectInput extends InplaceInput {

        @Override
        public void setTestedValue(String value) {
            focus();
            getInplaceInput().sendKeys(Keys.DOWN);
            getInplaceInput().sendKeys("\n");//Enter does not work
        }

    }

    public static class Input {

        @Root
        private WebElement input;

        public Input() {
        }

        public Input(WebElement input) {
            this.input = input;
        }

        public void clear() {
            input.clear();
        }

        public void focus() {
            input.click();
        }

        public String getDefaultText() {
            return getEditedText();
        }

        public String getEditedText() {
            return input.getAttribute("value");
        }

        public WebElement getInputElement() {
            return input;
        }

        public String getStyleClass() {
            return input.getAttribute("class");
        }

        public Color getTextColor() {
            return ColorUtils.convertToAWTColor(input.getCssValue("color"));
        }

        public void setTestedValue(String value) {
            focus();
            waitGui().until().element(input).text().equalTo("");
            input.sendKeys(value);
        }
    }

    public static class SelectInput extends Input {

        @Override
        public void clear() {
            focus();
            getInputElement().clear();
        }

        @Override
        public void setTestedValue(String value) {
            focus();
            getInputElement().sendKeys(value);
            getInputElement().sendKeys("\n");//Enter does not work
        }
    }
}

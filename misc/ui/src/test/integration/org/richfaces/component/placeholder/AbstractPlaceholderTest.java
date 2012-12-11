/**
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.arquillian.page.source.SourceChecker;
import org.richfaces.integration.MiscDeployment;
import org.richfaces.integration.utils.AssetBuilder;
import org.richfaces.integration.utils.Component;
import org.richfaces.integration.utils.ELAttribute;
import org.richfaces.utils.ColorUtils;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@RunAsClient
@RunWith(Arquillian.class)
public abstract class AbstractPlaceholderTest {

    protected static final Color DEFAULT_PLACEHOLDER_COLOR = new Color(119, 119, 119);
    protected static final String PLACEHOLDER_TEXT = "Placeholder Text";
    protected static final String PLACEHOLDER_CLASS = "rf-plhdr";
    protected static final String PLACEHOLDER_ID = "placeholderID";
    protected static final String INPUT_ID = "input";
    protected static final String INPUT_SELECTOR = "[id=input]";
    protected static final String SECOND_INPUT_ID = "second-input";
    protected static final String SECOND_INPUT_SELECTOR = "[id=second-input]";
    protected static final String EMPTY_STRING = "";
    protected static final String TESTED_VALUE = "some value";

    @Drone
    WebDriver browser;

    @ArquillianResource
    URL contextPath;
    @ArquillianResource
    SourceChecker sourceChecker;

    @FindBy(css = "[id$=ajaxSubmit]")
    WebElement a4jSubmitBtn;
    @FindBy(css = "[id$=httpSubmit]")
    WebElement httpSubmitBtn;
    @FindBy(css = "[id$=output]")
    WebElement output;
    @FindBy(id = PLACEHOLDER_ID)
    WebElement placeholderElement;

    private static final List<Component> TESTED_INPUT_COMPONENTS = new ArrayList<Component>();

    static {
        TESTED_INPUT_COMPONENTS.add(Component.hInputText());
        TESTED_INPUT_COMPONENTS.add(Component.richAutocomplete());
        TESTED_INPUT_COMPONENTS.add(Component.richCalendar()
                .withAttribute("enableManualInput", "true")
                .withAttribute("datePattern", "MMM d, yyyy")
                .withAttribute("timezone", "UTC"));
        TESTED_INPUT_COMPONENTS.add(Component.richInplaceInput());
        TESTED_INPUT_COMPONENTS.add(Component.richInplaceSelect()
                .addToBody(
                Component.fSelectItems().withValue("#{placeHolderValue.items}")));
        TESTED_INPUT_COMPONENTS.add(Component.richSelect()
                .withAttribute("enableManualInput", "true")
                .addToBody(
                Component.fSelectItems().withValue("#{placeHolderValue.items}")));
        TESTED_INPUT_COMPONENTS.add(Component.hInputTextarea());
    }

    @Deployment
    public static WebArchive createDeployment() {
        MiscDeployment deployment = new MiscDeployment(AbstractPlaceholderTest.class);
        addIndexPages(deployment);
        addSelectorPages(deployment);
        addEmptySelectorPages(deployment);
        addRenderedPages(deployment);
        addConverterPages(deployment);
        addSubmitPages(deployment);
        deployment.archive().addClass(PlaceHolderValueConverter.class)
                .addClass(PlaceHolderValue.class);
        return deployment.getFinalArchive();
    }

    abstract Input getFirstInput();

    abstract Input getSecondInput();

    abstract String testedComponent();

    protected Color getDefaultInputColor() {
        return Color.BLACK;
    }

    protected String getTestedValue() {
        return TESTED_VALUE;
    }

    protected String getTestedValueResponse() {
        return TESTED_VALUE;
    }

    @Test
    public void testConverter() {
        // having
        browser.get(contextPath.toExternalForm() + "converter-" + testedComponent() + ".jsf");

        // then
        assertEquals(PlaceHolderValue.DEFAULT_VALUE, getFirstInput().getDefaultText());
    }

    @Test
    public void testDefaultAttributes() {
        // having
        browser.get(contextPath.toExternalForm() + "index-" + testedComponent() + ".jsf");

        // then
        assertEquals(PLACEHOLDER_TEXT, getFirstInput().getDefaultText());
        assertEquals(DEFAULT_PLACEHOLDER_COLOR, getFirstInput().getTextColor());
        assertTrue("placeholder does not contain default class",
                getFirstInput().getStyleClass().contains(PLACEHOLDER_CLASS));
    }

    @Test
    public void testRendered() {
        // having
        browser.navigate().to(contextPath.toExternalForm() + "rendered-" + testedComponent() + ".jsf");
        // then
        assertFalse("Placeholder should not be present.", Graphene.element(placeholderElement).isPresent().apply(browser));
    }

    @Test
    public void testSelector() {
        // having
        browser.navigate().to(contextPath.toExternalForm() + "selector-" + testedComponent() + ".jsf?selector=[id=input]");
        // then
        assertEquals(PLACEHOLDER_TEXT, getFirstInput().getDefaultText());
    }

    @Test
    public void testSelectorEmpty() {
        // having
        browser.navigate().to(contextPath.toExternalForm() + "emptySelector-" + testedComponent() + ".jsf");

        // then
        assertEquals(PLACEHOLDER_TEXT, getFirstInput().getDefaultText());
    }

    @Test
    public void testStyleClass() {
        // having
        String className = "some-class";
        browser.navigate().to(contextPath.toExternalForm()
                + "index-" + testedComponent() + ".jsf" + "?styleClass=" + className);
        // then
        assertTrue("input should contain placeholder's default class", getFirstInput().getStyleClass().contains(PLACEHOLDER_CLASS));
        assertTrue("input should contain specified class", getFirstInput().getStyleClass().contains(className));
    }

    @Test
    public void when_input_with_placeholder_gains_focus_then_placeholder_is_removed() {
        // having
        browser.navigate().to(contextPath.toExternalForm() + "index-" + testedComponent() + ".jsf");

        // when
        getFirstInput().clickOnInput();
        // then
        assertEquals("", getFirstInput().getEditedText());
    }

    @Test
    public void when_text_is_changed_then_text_changes_color_to_default_and_removes_placeholder_style_classes() {
        // having
        browser.navigate().to(contextPath.toExternalForm() + "index-" + testedComponent() + ".jsf");
        // when
        getFirstInput().setTestedValue(getTestedValue());

        // then
        assertFalse("input should not contain placeholder class", getFirstInput().getStyleClass().contains(PLACEHOLDER_CLASS));
        assertEquals(getDefaultInputColor(), getFirstInput().getTextColor());
        assertEquals(getTestedValue(), getFirstInput().getEditedText());
    }

    @Test
    public void when_text_is_cleared_then_input_gets_placeholder_text_and_style_again() {
        // having
        browser.navigate().to(contextPath.toExternalForm() + "index-" + testedComponent() + ".jsf");

        // when
        getFirstInput().setTestedValue(getTestedValue());
        getFirstInput().clear();
        getSecondInput().clickOnInput();

        // then
        assertEquals(PLACEHOLDER_TEXT, getFirstInput().getDefaultText());
        assertEquals(DEFAULT_PLACEHOLDER_COLOR, getFirstInput().getTextColor());
        assertTrue("input should contain placeholder's default class", getFirstInput().getStyleClass().contains(PLACEHOLDER_CLASS));
    }

    @Test
    public void when_text_is_changed_and_input_is_blurred_then_typed_text_is_preserved() {
        // having
        browser.navigate().to(contextPath.toExternalForm() + "index-" + testedComponent() + ".jsf");

        // when
        getFirstInput().setTestedValue(getTestedValue());
        getSecondInput().clickOnInput();

        // then
        assertEquals(getTestedValue(), getFirstInput().getEditedText());
        assertEquals(getDefaultInputColor(), getFirstInput().getTextColor());
    }

    @Test
    public void testAjaxSendsEmptyValue() {
        // given
        browser.get(contextPath.toExternalForm() + "submit-" + testedComponent() + ".jsf");
        getFirstInput().clickOnInput();
        getFirstInput().setTestedValue(getTestedValue());

        Graphene.guardXhr(a4jSubmitBtn).click();

        Graphene.waitAjax().until(Graphene.element(output).isVisible());
        Graphene.waitAjax().until().element(output).text().equalTo(getTestedValueResponse());

        // when
        getFirstInput().clear();
        Graphene.guardXhr(a4jSubmitBtn).click();

        // then
        Graphene.waitAjax().until(Graphene.element(output).not().isVisible());
    }

    @Test
    public void testAjaxSendsTextValue() {
        // given
        browser.get(contextPath.toExternalForm() + "submit-" + testedComponent() + ".jsf");
        // when
        getFirstInput().clickOnInput();
        getFirstInput().setTestedValue(getTestedValue());
        Graphene.guardXhr(a4jSubmitBtn).click();

        // then
        Graphene.waitAjax().until(Graphene.element(output).isVisible());
        Graphene.waitAjax().until().element(output).text().equalTo(getTestedValueResponse());
    }

    @Test
    public void testSubmitEmptyValue() {
        // given
        browser.get(contextPath.toExternalForm() + "submit-" + testedComponent() + ".jsf");

        // when
        Graphene.guardHttp(httpSubmitBtn).click();

        // then
        Graphene.waitModel().until(Graphene.element(output).not().isVisible());
    }

    @Test
    public void testSubmitTextValue() {
        // given
        browser.get(contextPath.toExternalForm() + "submit-" + testedComponent() + ".jsf");
        // when
        getFirstInput().clickOnInput();
        getFirstInput().setTestedValue(getTestedValue());
        Graphene.guardHttp(httpSubmitBtn).click();
        // then
        Graphene.waitModel().until(Graphene.element(output).isVisible());
        assertEquals(getTestedValueResponse(), output.getText());
    }

    private static void addIndexPages(MiscDeployment deployment) {
        for (Component c : TESTED_INPUT_COMPONENTS) {
            Component inputWithPlaceholder = c.withId(INPUT_ID)
                    .addToBody(
                    Component.richPlaceholder()
                    .withId(PLACEHOLDER_ID)
                    .withAttribute(new ELAttribute("styleClass"))
                    .withValue(PLACEHOLDER_TEXT));

            Component secondInput = c.withId(SECOND_INPUT_ID);

            Component wrapperSpan = new Component("span")
                    .withId("wrapper")
                    .addToBody(inputWithPlaceholder, secondInput);

            deployment.archive().addAsWebResource(new AssetBuilder()
                    .addComponents(wrapperSpan).build(),
                    "index-" + c.getName() + ".xhtml");
        }
    }

    private static void addSelectorPages(MiscDeployment deployment) {
        Component placeholder = Component.richPlaceholder()
                .withId(PLACEHOLDER_ID)
                .withValue(PLACEHOLDER_TEXT)
                .withAttribute(new ELAttribute("selector"));

        for (Component c : TESTED_INPUT_COMPONENTS) {
            Component input = c.withId(INPUT_ID);

            Component wrapperSpan = new Component("span")
                    .withId("wrapper")
                    .addToBody(
                    input,
                    placeholder);

            deployment.archive().addAsWebResource(new AssetBuilder()
                    .addComponents(wrapperSpan).build(),
                    "selector-" + c.getName() + ".xhtml");
        }
    }

    private static void addEmptySelectorPages(MiscDeployment deployment) {
        for (Component c : TESTED_INPUT_COMPONENTS) {
            Component inputWithPlaceholder = c.withId(INPUT_ID)
                    .addToBody(
                    Component.richPlaceholder()
                    .withId(PLACEHOLDER_ID)
                    .withAttribute("selector", "")
                    .withValue(PLACEHOLDER_TEXT));
            Component secondInput = c.withId(SECOND_INPUT_ID);

            deployment.archive().addAsWebResource(new AssetBuilder()
                    .addComponents(
                    inputWithPlaceholder,
                    secondInput).build(),
                    "emptySelector-" + c.getName() + ".xhtml");
        }
    }

    private static void addRenderedPages(MiscDeployment deployment) {
        Component placeholder = Component.richPlaceholder()
                .withAttribute("rendered", "false")
                .withValue(PLACEHOLDER_TEXT);

        for (Component c : TESTED_INPUT_COMPONENTS) {
            Component inputWithPlaceholder = c.withId(INPUT_ID).addToBody(placeholder);

            deployment.archive().addAsWebResource(new AssetBuilder()
                    .addComponents(inputWithPlaceholder)
                    .build(),
                    "rendered-" + c.getName() + ".xhtml");
        }
    }

    private static void addConverterPages(MiscDeployment deployment) {
        for (Component c : TESTED_INPUT_COMPONENTS) {
            Component inputWithPlaceholder = c.withId(INPUT_ID)
                    .addToBody(
                    Component.richPlaceholder()
                    .withId(PLACEHOLDER_ID)
                    .withValue("#{placeHolderValue}")
                    .withAttribute("converter", "placeHolderValueConverter"));

            Component secondInput = c.withId(SECOND_INPUT_ID);

            deployment.archive().addAsWebResource(new AssetBuilder()
                    .addComponents(
                    inputWithPlaceholder,
                    secondInput).build(),
                    "converter-" + c.getName() + ".xhtml");
        }
    }

    private static void addSubmitPages(MiscDeployment deployment) {
        Component ajaxSubmitBtn = Component.a4jCommandButton()
                .withId("ajaxSubmit").withValue("ajax submit")
                .withAttribute("execute", "@form")
                .withAttribute("render", "output");

        Component httpSubmitBtn = Component.hCommandButton()
                .withId("httpSubmit")
                .withValue("http submit");

        Component output =
                Component.hOutputText()
                .withId("output")
                .withValue("#{placeHolderValue.value2}");

        for (Component c : TESTED_INPUT_COMPONENTS) {
            Component inputWithPlaceholder = c.withId(INPUT_ID)
                    .withValue("#{placeHolderValue.value2}")
                    .addToBody(
                    Component.richPlaceholder()
                    .withId(PLACEHOLDER_ID)
                    .withValue(PLACEHOLDER_TEXT));

            deployment.archive().addAsWebResource(new AssetBuilder()
                    .addComponentsToForm(
                    inputWithPlaceholder,
                    Component.br(),
                    ajaxSubmitBtn, httpSubmitBtn,
                    Component.br(),
                    output,
                    Component.uiDebug())
                    .build(),
                    "submit-" + c.getName() + ".xhtml");
        }
    }

    public static class Input {

        @Root
        protected WebElement input;

        public String getEditedText() {
            return input.getAttribute("value");
        }

        public String getDefaultText() {
            return getEditedText();
        }

        public void clickOnInput() {
            input.click();
        }

        public void clear() {
            input.clear();
        }

        public void setTestedValue(String value) {
            input.sendKeys(value);
        }

        public Color getTextColor() {
            return ColorUtils.convertToAWTColor(input.getCssValue("color"));
        }

        public String getStyleClass() {
            return input.getAttribute("class");
        }
    }

    public static class InplaceInput extends Input {

        @FindBy(css = "input[id$=Input]")
        WebElement inplaceInput;
        @FindBy(css = "span[id$=Label]")
        WebElement inplaceLabel;

        @Override
        public String getEditedText() {
            return inplaceInput.getAttribute("value");
        }

        @Override
        public String getDefaultText() {
            return inplaceLabel.getText();
        }

        @Override
        public void clickOnInput() {
            inplaceInput.click();
        }

        @Override
        public void setTestedValue(String value) {
            inplaceInput.click();
            inplaceInput.sendKeys(value);
        }

        @Override
        public void clear() {
            inplaceInput.click();
            inplaceInput.clear();
        }

        @Override
        public Color getTextColor() {
            return ColorUtils.convertToAWTColor(inplaceLabel.getCssValue("color"));
        }

        @Override
        public String getStyleClass() {
            return inplaceLabel.getAttribute("class");
        }
    }

    public static class InplaceSelectInput extends InplaceInput {

        @Override
        public void setTestedValue(String value) {
            inplaceInput.sendKeys(Keys.DOWN);
            inplaceInput.sendKeys("\n");//Enter does not work
        }
    }

    public static class SelectInput extends Input {

        @Override
        public void setTestedValue(String value) {
            input.sendKeys(value);
            input.sendKeys("\n");//Enter does not work
        }

        @Override
        public void clear() {
            input.click();
            input.clear();
        }
    }
}

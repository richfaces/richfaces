package org.richfaces.component.placeholder;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.impl.utils.URLUtils;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.arquillian.page.source.SourceChecker;
import org.richfaces.integration.MiscDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;
import org.richfaces.utils.ColorUtils;

@RunAsClient
@RunWith(Arquillian.class)
public class TestPlaceholder {

    private static final String PLACEHOLDER_TEXT = "Placeholder Text";
    private static final Color DEFAULT_PLACEHOLDER_COLOR = new Color(119, 119, 119);
    private static final String PLACEHOLDER_CLASS = "rf-plhdr";

    @Drone
    WebDriver browser;

    @ArquillianResource
    URL contextPath;

    @ArquillianResource
    SourceChecker sourceChecker;

    @FindBy(id = "input")
    InputWithPlaceholder placeholder;

    @FindBy(id = "second-input")
    WebElement secondInput;

    @Deployment
    public static WebArchive createDeployment() {
        MiscDeployment deployment = new MiscDeployment(TestPlaceholder.class);

        addIndexPage(deployment);
        addSelectorPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void testComponentSourceWithoutSelector() throws Exception {
        sourceChecker.checkComponentSource(contextPath, "placeholder-without-selector.xmlunit.xml", By.tagName("body"));
    }

    @Test
    public void testComponentSourceWithSelector() throws Exception {
        URL selectorUrl = URLUtils.buildUrl(contextPath, "selector.jsf?selector=input");
        sourceChecker.checkComponentSource(selectorUrl, "placeholder-with-selector.xmlunit.xml", By.tagName("body"));
    }

    @Test
    public void testDefaultAttributes() {
        // having
        browser.get(contextPath.toExternalForm());

        // then
        assertEquals(PLACEHOLDER_TEXT, placeholder.getText());
        assertEquals(DEFAULT_PLACEHOLDER_COLOR, placeholder.getTextColor());
        assertEquals(PLACEHOLDER_CLASS, placeholder.getStyleClass());
    }

    @Test
    public void testStyleClass() {
        // having
        String className = "some-class";
        browser.navigate().to(contextPath.toExternalForm() + "?styleClass=some-class");

        // then
        assertEquals(PLACEHOLDER_CLASS + " " + className, placeholder.getStyleClass());
    }

    @Test
    public void testSelector() {
        // having
        browser.navigate().to(contextPath.toExternalForm() + "selector.jsf?selector=input");

        // then
        assertEquals(PLACEHOLDER_TEXT, placeholder.getText());
    }

    @Test
    public void when_input_with_placeholder_gains_focus_then_placeholder_is_removed() {
        // having
        browser.navigate().to(contextPath.toExternalForm());

        // when
        placeholder.input.click();

        // then
        assertEquals("", placeholder.getText());
    }

    @Test
    public void when_text_is_changed_then_text_changes_color_to_default_and_removes_placeholder_style_classes() {
        // having
        browser.navigate().to(contextPath.toExternalForm());

        // when
        placeholder.input.sendKeys("a");

        // then
        assertEquals(Color.BLACK, placeholder.getTextColor());
        assertEquals("", placeholder.getStyleClass());
    }

    @Test
    public void when_text_is_cleared_then_input_gets_placeholder_text_and_style_again() {
        // having
        browser.navigate().to(contextPath.toExternalForm());

        // when
        placeholder.input.sendKeys("a");
        placeholder.input.clear();
        secondInput.click();

        // then
        assertEquals(PLACEHOLDER_TEXT, placeholder.getText());
        assertEquals(DEFAULT_PLACEHOLDER_COLOR, placeholder.getTextColor());
        assertEquals(PLACEHOLDER_CLASS, placeholder.getStyleClass());
    }

    @Test
    public void when_text_is_changed_and_input_is_blurred_then_typed_text_is_preserved() {
        // having
        String text = "some-text";
        browser.navigate().to(contextPath.toExternalForm());

        // when
        placeholder.input.sendKeys(text);
        secondInput.click();

        // then
        assertEquals(text, placeholder.getText());
        assertEquals(Color.BLACK, placeholder.getTextColor());
    }

    private static void addIndexPage(MiscDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.xmlns("rich", "http://richfaces.org/misc");

        p.body("<h:inputText id='input' value=''>");
        p.body("    <rich:placeholder value='" + PLACEHOLDER_TEXT + "' styleClass='#{param.styleClass}' />");
        p.body("</h:inputText>");

        p.body("<h:inputText id='second-input' value='' />");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

    private static void addSelectorPage(MiscDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.xmlns("rich", "http://richfaces.org/misc");

        p.body("<input id='input' type='text' value='' />");
        p.body("<rich:placeholder value='" + PLACEHOLDER_TEXT + "' selector='#{param.selector}' />");

        deployment.archive().addAsWebResource(p, "selector.xhtml");
    }

    public static class InputWithPlaceholder {

        @Root
        private WebElement input;

        public String getText() {
            return input.getAttribute("value");
        }

        public Color getTextColor() {
            return ColorUtils.convertToAWTColor(input.getCssValue("color"));
        }

        public String getStyleClass() {
            return input.getAttribute("class");
        }
    }
}

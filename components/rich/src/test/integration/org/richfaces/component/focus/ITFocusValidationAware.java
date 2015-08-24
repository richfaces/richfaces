package org.richfaces.component.focus;

import static java.text.MessageFormat.format;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.message.Message.MessageType;
import org.richfaces.fragment.messages.RichFacesMessages;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;
import org.richfaces.utils.focus.ElementIsFocused;

@RunAsClient
@RunWith(Arquillian.class)
public class ITFocusValidationAware {

    @Drone
    private WebDriver browser;
    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "form:ajaxInvalidate")
    private WebElement ajaxSubmitAndInvalidateFirstTwoInputsButton;
    @FindBy(id = "form:createGlobalMessageAndCheckFocusResultElement")
    private WebElement createGlobalMessageAndCheckFocusResultElement;
    @FindBy(id = "form:input2")
    private WebElement input2;
    @FindBy(id = "form:input3")
    private WebElement input3;
    @FindBy(id = "form:invalidateBothInputsAndCheckFocusResultElement")
    private WebElement invalidateBothInputsAndCheckFocusResultElement;
    @FindBy(id = "form:submitGlobal")
    private WebElement submitAndCreateGlobalMessage;
    @FindBy(id = "form:submitInvalidate")
    private WebElement submitAndInvalidateFirstTwoInputsButton;
    @FindBy(id = "form:messages")
    private RichFacesMessages messages;

    private void checkInvalidatedMessages() {
        assertEquals(2, messages.getItems().size());
        for (int i = 0; i < 2; i++) {
            assertEquals(format("invalidated form:input{0}", i + 1), messages.getItem(i).getText());
            assertEquals(MessageType.INFORMATION, messages.getItem(i).getType());
        }
    }

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITFocusValidationAware.class);

        deployment.archive().addClasses(ComponentBean.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Before
    public void openInitialPage() {
        browser.get(contextPath.toExternalForm());
    }

    @Test
    public void testGlobalMessageIsIgnored() {
        waitGui().withMessage("third input should be focused, since it has lowest tabindex in the form.").until(new ElementIsFocused(input3));
        guardHttp(submitAndCreateGlobalMessage).click();
        assertEquals("global message", messages.getItem(0).getText());
        assertEquals(MessageType.INFORMATION, messages.getItem(0).getType());
        assertEquals(ComponentBean.PASSED, createGlobalMessageAndCheckFocusResultElement.getText());
        waitGui().withMessage("third input should still be focused, since it has lowest tabindex in the form and global message is ignored.").until(new ElementIsFocused(input3));
    }

    @Test
    public void testValidateMultipleInputsDuringAjax() {
        waitGui().withMessage("third input should be focused, since it has lowest tabindex in the form.").until(new ElementIsFocused(input3));
        guardAjax(ajaxSubmitAndInvalidateFirstTwoInputsButton).click();
        checkInvalidatedMessages();
        assertEquals(ComponentBean.PASSED, invalidateBothInputsAndCheckFocusResultElement.getText());
        waitGui().withMessage("second input should be focused, since it has lower tabindex than the first input.").until(new ElementIsFocused(input2));
    }

    /**
     *
     */
    @Test
    public void testValidateMultipleInputsDuringFormSubmission() {
        waitGui().withMessage("third input should be focused, since it has lowest tabindex in the form.").until(new ElementIsFocused(input3));
        guardHttp(submitAndInvalidateFirstTwoInputsButton).click();
        checkInvalidatedMessages();
        assertEquals(ComponentBean.PASSED, invalidateBothInputsAndCheckFocusResultElement.getText());
        waitGui().withMessage("second input should be focused, since it has lower tabindex than the first input.").until(new ElementIsFocused(input2));
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<h:form id='form'>");
        p.body("    <rich:focus id='focus' binding='#{componentBean.component}' />");
        p.body("    <h:inputText id='input1' />");
        p.body("    <h:inputText id='input2' tabindex='2' />");
        p.body("    <h:inputText id='input3' tabindex='1' />");
        p.body("    <br/>");
        p.body("    <h:commandButton id='submitInvalidate' value='Submit invalidating first two inputs' action='#{componentBean.invalidateBothInputsAndCheckFocus}' />");
        p.body("    <a4j:commandButton id='ajaxInvalidate' render='@form' value='Ajax invalidating first two inputs' action='#{componentBean.invalidateBothInputsAndCheckFocus}' />");
        p.body("    <h:commandButton id='submitGlobal' value='Submit and create global message' action='#{componentBean.createGlobalMessageAndCheckFocus}' />");
        p.body("    <br/>");
        p.body("    global message result: <h:outputText id='createGlobalMessageAndCheckFocusResultElement' value='#{componentBean.createGlobalMessageAndCheckFocusResult}' />");
        p.body("    <br/>");
        p.body("    multiple input invalidation result: <h:outputText id='invalidateBothInputsAndCheckFocusResultElement' value='#{componentBean.invalidateBothInputsAndCheckFocusResult}' />");
        p.body("    <br/>");
        p.body("    <rich:messages id='messages' />");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

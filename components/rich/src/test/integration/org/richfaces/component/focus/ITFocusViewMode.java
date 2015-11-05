package org.richfaces.component.focus;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;
import org.richfaces.utils.focus.ElementIsFocused;

@RunAsClient
@RunWith(Arquillian.class)
public class ITFocusViewMode {

    @Drone
    private WebDriver browser;
    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "form1")
    private Form form1;
    @FindBy(id = "form2")
    private Form form2;
    @FindBy(id = "form3")
    private Form form3;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITFocusViewMode.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void when_form_focus_is_defined_then_it_overrides_view_focus_settings() {
        // having
        browser.get(contextPath.toExternalForm());
        Graphene.waitGui().until(new ElementIsFocused(form1.getInput2()));

        // then
        form3.getInput1().click();
        form3.submitAjax();
        Graphene.waitGui().until(new ElementIsFocused(form3.getInput1()));

        form3.submit();
        Graphene.waitGui().until(new ElementIsFocused(form3.getInput1()));
    }

    @Test
    public void when_forms_without_focus_are_sent_using_ajax_then_view_focus_settings_is_applied_to_them_and_tabindex_priority_is_used() {
        // having
        browser.get(contextPath.toExternalForm());

        // then
        form2.submitAjax();
        Graphene.waitGui().until(new ElementIsFocused(form2.getInput2()));
    }

    @Test
    public void when_forms_without_focus_are_sent_using_ajax_then_view_focus_settings_is_applied_to_them_and_validation_awareness_is_used() {
        // having
        browser.get(contextPath.toExternalForm());

        // then
        form1.submit();
        Graphene.waitGui().until(new ElementIsFocused(form1.getInput1()));
    }

    @Test
    public void when_forms_without_focus_are_submitted_then_view_focus_settings_is_applied_to_them_and_tabindex_priority_is_used() {
        // having
        browser.get(contextPath.toExternalForm());

        // then
        form2.submit();
        Graphene.waitGui().until(new ElementIsFocused(form2.getInput2()));
    }

    @Test
    public void when_forms_without_focus_are_submitted_then_view_focus_settings_is_applied_to_them_and_validation_awareness_is_used() {
        // having
        browser.get(contextPath.toExternalForm());

        // then
        form1.submit();
        Graphene.waitGui().until(new ElementIsFocused(form1.getInput1()));
    }

    @Test
    public void when_view_focus_is_renderer_on_initial_request_then_first_tabbable_input_from_first_form_on_the_page_is_focused() {
        // having
        browser.get(contextPath.toExternalForm());

        // then
        Graphene.waitGui().until(new ElementIsFocused(form1.getInput2()));
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<rich:focus id='focus' />");

        p.body("<h:form id='form1'>");

        p.body("    <h:inputText id='input1' required='true' />");
        p.body("    <h:inputText id='input2' tabindex='1' />");

        p.body("    <h:commandButton id='submit' value='Submit' />");

        p.body("    <h:commandButton id='ajax' value='Ajax'>");
        p.body("        <a4j:ajax execute='@form' render='@form' />");
        p.body("    </h:commandButton>");

        p.body("</h:form>");

        p.body("<h:form id='form2'>");

        p.body("    <h:inputText id='input1' />");
        p.body("    <h:inputText id='input2' tabindex='1' />");

        p.body("    <h:commandButton id='submit' value='Submit' />");

        p.body("    <h:commandButton id='ajax' value='Ajax'>");
        p.body("        <a4j:ajax render='@form' />");
        p.body("    </h:commandButton>");

        p.body("</h:form>");

        p.body("<h:form id='form3'>");
        p.body("    <rich:focus id='focus' preserve='true' />");

        p.body("    <h:inputText id='input1' />");
        p.body("    <h:inputText id='input2' tabindex='1' />");

        p.body("    <h:commandButton id='submit' value='Submit' />");

        p.body("    <h:commandButton id='ajax' value='Ajax'>");
        p.body("        <a4j:ajax render='@form' />");
        p.body("    </h:commandButton>");

        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

    public static class Form {

        @FindBy(css = "[id$=ajax]")
        private WebElement ajax;
        @FindBy(css = "[id$=input1]")
        private WebElement input1;
        @FindBy(css = "[id$=input2]")
        private WebElement input2;
        @FindBy(css = "[id$=submit]")
        private WebElement submit;

        public WebElement getAjax() {
            return ajax;
        }

        public WebElement getInput1() {
            return input1;
        }

        public WebElement getInput2() {
            return input2;
        }

        public WebElement getSubmit() {
            return submit;
        }

        public void submit() {
            Graphene.guardHttp(submit).click();
        }

        public void submitAjax() {
            Graphene.guardAjax(ajax).click();
        }
    }
}

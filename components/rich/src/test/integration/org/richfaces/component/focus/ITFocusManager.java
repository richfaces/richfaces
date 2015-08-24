package org.richfaces.component.focus;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;
import org.richfaces.utils.focus.ElementIsFocused;
import org.richfaces.utils.focus.FocusRetriever;

import category.Smoke;

@RunAsClient
@RunWith(Arquillian.class)
public class ITFocusManager {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "form:submit")
    private WebElement submitButton;

    @FindBy(id = "form:ajax")
    private WebElement ajaxButton;

    @FindBy(id = "form:input2")
    private WebElement input2;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITFocusManager.class);

        deployment.archive().addClasses(ComponentBean.class);

        addIndexPage(deployment);
        addViewFocusPage(deployment);
        addFormFocusIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    @Category(Smoke.class)
    public void test_FocusManager_on_initial_request() {
        Graphene.guardHttp(browser).get(contextPath.toString());
        Graphene.waitGui().until(new ElementIsFocused(input2));
    }

    @Test
    public void test_FocusManager_on_form_submit_postback() {
        // given
        Graphene.guardHttp(browser).get(contextPath.toExternalForm());
        // when
        guardHttp(submitButton).click();
        // then
        Graphene.waitGui().until(new ElementIsFocused(input2));
    }

    @Test
    public void test_FocusManager_on_ajax_postback() {
        // given
        Graphene.guardHttp(browser).get(contextPath.toExternalForm());
        // when
        guardAjax(ajaxButton).click();
        // then
        Graphene.waitGui().until(new ElementIsFocused(input2));
    }

    @Test
    public void when_there_is_form_based_focus_but_focus_was_enforced_using_FocusManager_then_it_is_not_aplied() {

        Graphene.guardHttp(browser).get(contextPath.toExternalForm() + "form.jsf");
        Graphene.waitGui().until(new ElementIsFocused(input2));

        guardHttp(submitButton).click();
        Graphene.waitGui().until(new ElementIsFocused(input2));

        guardAjax(ajaxButton).click();
        Graphene.waitGui().until(new ElementIsFocused(input2));
    }

    @Test
    public void when_there_is_view_based_focus_but_focus_was_enforced_using_FocusManager_then_it_is_not_aplied() {

        Graphene.guardHttp(browser).get(contextPath.toExternalForm() + "view.jsf");
        Graphene.waitGui().until(new ElementIsFocused(input2));

        guardHttp(submitButton).click();
        Graphene.waitGui().until(new ElementIsFocused(input2));

        guardAjax(ajaxButton).click();
        Graphene.waitGui().until(new ElementIsFocused(input2));
    }

    private WebElement getFocusedElement() {
        return FocusRetriever.retrieveActiveElement();
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<f:event listener='#{componentBean.setFocusToSecondInput}' type='preRenderView' />");

        p.body("<h:form id='form'>");

        p.body("    <h:inputText id='input1' />");
        p.body("    <h:inputText id='input2' />");

        p.body("    <h:commandButton id='submit' value='Submit' />");

        p.body("    <a4j:commandButton id='ajax' render='@form' value='Ajax' />");

        p.body("</h:form> <ui:debug />");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

    private static void addFormFocusIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<f:event listener='#{componentBean.setFocusToSecondInput}' type='preRenderView' />");

        p.body("<h:form id='form'>");
        p.body("    <rich:focus id='focus' binding='#{componentBean.component}' />");

        p.body("    <h:inputText id='input1' />");
        p.body("    <h:inputText id='input2' />");

        p.body("    <h:commandButton id='submit' value='Submit' />");

        p.body("    <a4j:commandButton id='ajax' render='@form' value='Ajax' />");

        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "form.xhtml");
    }

    private static void addViewFocusPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<f:event listener='#{componentBean.setFocusToSecondInput}' type='preRenderView' />");

        p.body("<rich:focus id='focus' binding='#{componentBean.component}' />");

        p.body("<h:form id='form'>");

        p.body("    <h:inputText id='input1' />");
        p.body("    <h:inputText id='input2' />");

        p.body("    <h:commandButton id='submit' value='Submit' />");

        p.body("    <a4j:commandButton id='ajax' render='@form' value='Ajax' />");

        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "view.xhtml");
    }
}

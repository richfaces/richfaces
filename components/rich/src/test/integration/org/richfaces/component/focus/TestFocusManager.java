package org.richfaces.component.focus;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.impl.utils.URLUtils;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.MiscDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@WarpTest
@RunWith(Arquillian.class)
public class TestFocusManager {

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

    private Activity openPage = new Activity() {
        public void perform() {
            browser.get(contextPath.toExternalForm());
        }
    };

    private Activity submit = new Activity() {
        public void perform() {
            guardHttp(submitButton).click();
        }
    };

    private Activity ajax = new Activity() {
        public void perform() {
            guardAjax(ajaxButton).click();
        }
    };

    @Deployment
    public static WebArchive createDeployment() {
        MiscDeployment deployment = new MiscDeployment(TestFocusManager.class);

        deployment.archive().addClasses(ComponentBean.class, VerifyFocusCandidates.class, VerifyFocusEnforcing.class,
                VerifyFocusEnforcingOverridesFocusSettings.class, AbstractComponentAssertion.class);
        deployment.archive().addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        addIndexPage(deployment);
        addViewFocusPage(deployment);
        addFormFocusIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void test_FocusManager_on_initial_request() {
        Warp.initiate(openPage).inspect(new VerifyFocusEnforcing("input2"));
        assertEquals(input2, getFocusedElement());
    }

    @Test
    public void test_FocusManager_on_form_submit_postback() {
        // given
        browser.get(contextPath.toExternalForm());
        // when
        Warp.initiate(submit)
        // then
                .inspect(new VerifyFocusEnforcing("input2"));
        assertEquals(input2, getFocusedElement());
    }

    @Test
    public void test_FocusManager_on_ajax_postback() {
        // given
        browser.get(contextPath.toExternalForm());
        // when
        Warp.initiate(ajax)
        // then
                .inspect(new VerifyFocusEnforcing("input2"));
        assertEquals(input2, getFocusedElement());
    }

    @Test
    public void when_there_is_form_based_focus_but_focus_was_enforced_using_FocusManager_then_it_is_not_aplied() {

        contextPath = URLUtils.buildUrl(contextPath, "form.jsf");

        Warp.initiate(openPage).inspect(new VerifyFocusEnforcingOverridesFocusSettings("form:input2"));
        assertEquals(input2, getFocusedElement());

        Warp.initiate(submit).inspect(new VerifyFocusEnforcingOverridesFocusSettings("form:input2"));
        assertEquals(input2, getFocusedElement());

        Warp.initiate(ajax).inspect(new VerifyFocusEnforcingOverridesFocusSettings("form:input2"));
        assertEquals(input2, getFocusedElement());
    }

    @Test
    public void when_there_is_view_based_focus_but_focus_was_enforced_using_FocusManager_then_it_is_not_aplied() {

        contextPath = URLUtils.buildUrl(contextPath, "form.jsf");

        Warp.initiate(openPage).inspect(new VerifyFocusEnforcingOverridesFocusSettings("form:input2"));
        assertEquals(input2, getFocusedElement());

        Warp.initiate(submit).inspect(new VerifyFocusEnforcingOverridesFocusSettings("form:input2"));
        assertEquals(input2, getFocusedElement());

        Warp.initiate(ajax).inspect(new VerifyFocusEnforcingOverridesFocusSettings("form:input2"));
        assertEquals(input2, getFocusedElement());
    }

    private WebElement getFocusedElement() {
        return FocusRetriever.retrieveActiveElement();
    }

    private static void addIndexPage(MiscDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.xmlns("rich", "http://richfaces.org/rich");
        p.xmlns("a4j", "http://richfaces.org/a4j");

        p.body("<h:form id='form'>");

        p.body("    <h:inputText id='input1' />");
        p.body("    <h:inputText id='input2' />");

        p.body("    <h:commandButton id='submit' value='Submit' />");

        p.body("    <a4j:commandButton id='ajax' render='@form' value='Ajax' />");

        p.body("</h:form> <ui:debug />");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

    private static void addFormFocusIndexPage(MiscDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.xmlns("rich", "http://richfaces.org/rich");
        p.xmlns("a4j", "http://richfaces.org/a4j");

        p.body("<h:form id='form'>");
        p.body("    <rich:focus id='focus' binding='#{componentBean.component}' />");

        p.body("    <h:inputText id='input1' />");
        p.body("    <h:inputText id='input2' />");

        p.body("    <h:commandButton id='submit' value='Submit' />");

        p.body("    <a4j:commandButton id='ajax' render='@form' value='Ajax' />");

        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "form.xhtml");
    }

    private static void addViewFocusPage(MiscDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.xmlns("rich", "http://richfaces.org/rich");
        p.xmlns("a4j", "http://richfaces.org/a4j");

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

package org.richfaces.ui.autocomplete;

import static org.jboss.arquillian.graphene.Graphene.element;
import static org.jboss.arquillian.graphene.Graphene.guardXhr;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.jsf.AfterPhase;
import org.jboss.arquillian.warp.jsf.Phase;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.InputDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import category.Smoke;

@RunAsClient
@WarpTest
@RunWith(Arquillian.class)
public class ITAutocompleteBehaviors {

    @Drone
    WebDriver browser;

    @ArquillianResource
    URL contextPath;

    @FindBy(id = "form:render")
    WebElement renderButton;

    @FindBy(css = "input.rf-au-inp")
    WebElement autocompleteInput;

    @FindBy(css = ".rf-au-itm")
    WebElement autocompleteItem;

    @FindBy(css = "body")
    WebElement body;

    By suggestionList = By.cssSelector(".rf-au-lst-cord");

    @Deployment
    public static WebArchive createDeployment() {
        InputDeployment deployment = new InputDeployment(ITAutocompleteBehaviors.class);

        deployment.archive().addClasses(AutocompleteBean.class).addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    /**
     * onblur should have input value available via 'this.value' expression (RF-12114)
     */
    @Test
    @Category(Smoke.class)
    public void testAjaxOnBlur() {

        // given
        browser.get(contextPath.toExternalForm());

        autocompleteInput.sendKeys("t");
        waitGui().withMessage("suggestion list is visible").until(element(suggestionList).isVisible());
        guardXhr(autocompleteItem).click();

        // when / then
        Warp.initiate(new Activity() {

            @Override
            public void perform() {
                guardXhr(body).click();
            }
        }).inspect(new Inspection() {
            private static final long serialVersionUID = 1L;

            @Inject
            AutocompleteBean bean;

            @AfterPhase(Phase.INVOKE_APPLICATION)
            public void verify_bean_executed() {
                assertTrue(bean.isListenerInvoked());
            }
        });
    }

    private static void addIndexPage(InputDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<h:form id='form'>");
        p.body("    <r:autocomplete id='autocomplete' autocompleteList='#{autocompleteBean.suggestions}'>");
        p.body("        <r:ajax event='blur' listener='#{autocompleteBean.actionListener}' />");
        p.body("    </r:autocomplete>");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

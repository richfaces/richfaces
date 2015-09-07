package org.richfaces.component.validation;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.junit.Assert.assertThat;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
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

@RunWith(Arquillian.class)
@RunAsClient
public class ITGraphValidatorGroups {

    @Drone
    private WebDriver browser;
    @ArquillianResource
    private URL contextPath;

    @FindBy
    private WebElement applyChanges;
    @FindBy
    private WebElement inputTextMsg;
    @FindBy(id = "removeValidationGroup")
    private WebElement removeValidationGroupButton;
    @FindBy(id = "setAlwaysPassingValidationGroup")
    private WebElement setAlwaysPassingValidationGroupButton;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITGraphValidatorGroups.class);

        deployment.archive().addClass(GraphValidatorBean.class);
        addIndexPage(deployment);

        deployment.addHibernateValidatorWhenUsingServletContainer();

        return deployment.getFinalArchive();
    }

    @Test
    public void when_validation_group_is_empty_then_bean_validator_should_be_used() {
        browser.get(contextPath.toExternalForm());
        guardHttp(removeValidationGroupButton).click();
        guardAjax(applyChanges).click();
        assertThat(inputTextMsg.getText(), containsString("may not be empty"));
    }

    @Test
    public void when_validation_group_is_set_then_bean_validator_should_not_be_used() {
        browser.get(contextPath.toExternalForm());
        guardHttp(setAlwaysPassingValidationGroupButton).click();
        guardAjax(applyChanges).click();
        assertThat(inputTextMsg.getText(), equalTo(""));
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.form("<h:commandButton id='removeValidationGroup' value='remove validation group' action='#{graphValidatorBean.setEmptyValidationGroup}' />");
        p.form("<h:commandButton id='setAlwaysPassingValidationGroup' value='set always passing validation group' action='#{graphValidatorBean.setAlwaysPassingValidationGroup}' />");
        p.form("<br/>");
        p.form("<rich:graphValidator id='graphValidator'");
        p.form("        value='#{graphValidatorBean}'");
        p.form("        groups='#{graphValidatorBean.validationGroups}'>");
        p.form("    <h:panelGrid columns='3'>");

        p.form("        <h:outputText value='Input Text' />");
        p.form("        <h:inputText id='inputText' value='#{graphValidatorBean.inputText}' />");
        p.form("        <rich:message id='inputTextMsg' for='inputText' />");

        p.form("    </h:panelGrid>");

        p.form("    <a4j:commandButton id='applyChanges' value='Apply changes' />");

        p.form("</rich:graphValidator>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

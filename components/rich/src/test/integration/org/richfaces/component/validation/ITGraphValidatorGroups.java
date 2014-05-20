package org.richfaces.component.validation;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.junit.Assert.assertThat;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.servlet.BeforeServlet;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.component.validation.GraphValidatorBean.MethodValidationGroup;
import org.richfaces.integration.UIDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunWith(Arquillian.class)
@RunAsClient
@WarpTest
public class ITGraphValidatorGroups {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @Deployment
    public static WebArchive createDeployment() {
        UIDeployment deployment = new UIDeployment(ITGraphValidatorGroups.class);

        deployment.archive().addClass(GraphValidatorBean.class);
        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @FindBy
    private WebElement applyChanges;

    @FindBy
    private WebElement inputText;

    @FindBy
    private WebElement inputTextMsg;

    @Test
    public void when_validation_group_is_empty_then_bean_validator_should_be_used() throws InterruptedException {
        Warp.initiate(new Activity() {
            public void perform() {
                browser.get(contextPath.toExternalForm());
            }
        }).inspect(new Inspection() {
            private static final long serialVersionUID = 1L;

            @BeforeServlet
            public void setupGroup(GraphValidatorBean bean) {
                bean.setValidationGroups(new Class<?>[] {});
            }
        });

        guardAjax(applyChanges).click();
        assertThat(inputTextMsg.getText(), containsString("may not be empty"));
    }

    @Test
    public void when_validation_group_is_set_then_bean_validator_should_not_be_used() throws InterruptedException {
        Warp.initiate(new Activity() {
            public void perform() {
                browser.get(contextPath.toExternalForm());
            }
        }).inspect(new Inspection() {
            private static final long serialVersionUID = 1L;

            @BeforeServlet
            public void setupGroup(GraphValidatorBean bean) {
                bean.setValidationGroups(new Class[] { MethodValidationGroup.class });
            }
        });

        guardAjax(applyChanges).click();
        assertThat(inputTextMsg.getText(), equalTo(""));
    }

    private static void addIndexPage(UIDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.form("<rich:graphValidator id='graphValidator'");
        p.form("        value='#{graphValidatorBean}'");
        p.form("        groups='#{graphValidatorBean.validationGroups}'>");
        p.form("    <h:panelGrid columns='3'>");

        p.form("        <h:outputText value='Select Boolean Checkbox' />");
        p.form("        <h:selectBooleanCheckbox id='selectBooleanCheckbox' value='#{graphValidatorBean.selectBooleanCheckbox}' />");
        p.form("        <rich:message id='selectBooleanCheckboxMsg' for='selectBooleanCheckbox' />");

        p.form("        <h:outputText value='Input Text' />");
        p.form("        <h:inputText id='inputText' value='#{graphValidatorBean.inputText}' />");
        p.form("        <rich:message id='inputTextMsg' for='inputText' />");

        p.form("    </h:panelGrid>");

        p.form("    <a4j:commandButton id='applyChanges'");
        p.form("        value='Apply changes' ");
        p.form("        action='#{graphValidatorBean.action}' />");

        p.form("</rich:graphValidator>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}
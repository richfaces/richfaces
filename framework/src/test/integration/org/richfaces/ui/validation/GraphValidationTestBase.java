package org.richfaces.ui.validation;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import category.Smoke;

public class GraphValidationTestBase extends ValidationTestBase {

    @FindBy(id = "form:submit")
    private WebElement element;

    @Override
    protected void submitValue() {
        element.click();
    }

    @Test
    @Category(Smoke.class)
    public void testSubmitTooShortValue() throws Exception {
        submitValueAndCheckMessage("", containsString(GraphBean.SHORT_MSG));
        checkMessage("textMessage", containsString(GraphBean.SHORT_MSG));
        checkMessage("graphMessage", equalTo(""));
    }

    @Test
    public void testBeanLevelConstrain() throws Exception {
        submitValueAndCheckMessage("bar", equalTo(GraphBean.FOO_MSG));
        checkMessage("graphMessage", containsString(GraphBean.FOO_MSG));
        checkMessage("textMessage", equalTo(""));
    }

    @Test
    public void testCorrectValue() throws Exception {
        submitValueAndCheckMessage("foobar", equalTo(""));
    }

    protected static void addIndexPage(org.richfaces.deployment.Deployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<h:form id='form'>");
        p.body("    <r:graphValidator id='validator' value='#{graphBean}' >");
        p.body("    <h:inputText id='text' value='#{graphBean.value}'>");
        p.body("    </h:inputText>");
        p.body("    </r:graphValidator>");
        p.body("    <h:outputText id='out' value='#{graphBean.value}'></h:outputText>");
        p.body("    <h:commandButton id='submit' value='Submit'/>");
        p.body("</h:form>");
        p.body("<r:message id='textMessage' for='text' />");
        p.body("<r:message id='graphMessage' for='validator' />");
        p.body("<r:messages id='uiMessage' />");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

package org.richfaces.integration.ui.validation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.jboss.arquillian.graphene.Graphene.guardXhr;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.integration.FrameworkDeployment;
import org.richfaces.integration.shrinkwrap.descriptor.FaceletAsset;
import org.richfaces.ui.common.Bean;
import org.richfaces.ui.validation.CustomValidator;

@RunWith(Arquillian.class)
@RunAsClient
public class AjaxValidationTest extends ValidationTestBase {

    @Deployment
    public static WebArchive deployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(AjaxValidationTest.class);

        deployment.archive().addClasses(Bean.class, CustomValidator.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void testSubmitTooShortValue() throws Exception {
        submitValueAndCheckMessage("", not(equalTo("")));
    }

    @Test
    public void testSubmitTooLongValue() throws Exception {
        submitValueAndCheckMessage("123456", not(equalTo("")));
    }

    @Test
    public void testSubmitProperValue() throws Exception {
        submitValueAndCheckMessage("ab", equalTo(""));
    }

    @Override
    protected void submitValue() {
        guardXhr(body).click();
    }

    private static void addIndexPage(org.richfaces.integration.Deployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<h:form id='form'>");
        p.body("<h:inputText id='text' value='#{test.value}'>");
        p.body("    <f:validator validatorId='custom' />");
        p.body("    <r:validator event='blur' />");
        p.body("</h:inputText>");
        p.body("<h:outputText id='out' value='#{test.value}'></h:outputText>");
        p.body("</h:form>");
        p.body("<r:message id='uiMessage' for='text' />");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

}

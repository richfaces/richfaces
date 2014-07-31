package org.richfaces.integration.javascript;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.deployment.CoreDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunWith(Arquillian.class)
@RunAsClient
public class ITAjaxSubmissionCallbacks {

    @Drone
    private WebDriver driver;

    @ArquillianResource
    private URL contextPath;

    @JavaScript
    private Callbacks callbacks;

    @FindByJQuery("input:visible")
    private WebElement input;

    @FindBy(id = "out")
    private WebElement output;

    @FindBy(tagName = "body")
    private WebElement body;

    private static final String FORM_ID = "form1";
    private static final List<String> EVENT_CALLBACKS = Arrays.asList(new String[]{ "ajaxsubmit", "ajaxbegin", "ajaxbeforedomupdate", "ajaxcomplete" });

    @Deployment(testable = false)
    public static WebArchive deployment() {
        CoreDeployment deployment = new CoreDeployment(ITAjaxSubmissionCallbacks.class);
        deployment.withA4jComponents();
        
        deployment.archive().addAsWebResource(buildPage(true, true), "documentAndFormScoped.xhtml");
        deployment.archive().addAsWebResource(buildPage(true, false), "formScoped.xhtml");
        deployment.archive().addAsWebResource(buildPage(false, true), "documentScoped.xhtml");

        return deployment.getFinalArchive();
    }

    @Test
    public void should_register_ajax_callbacks_both_on_form_and_document_object() {
        // when
        loadThePageAndWriteTextToInput("documentAndFormScoped.jsf");

        // then
        assertCorrectCallbacks(true, callbacks.getDocumentScopedCallbacksCalled());
        assertCorrectCallbacks(true, callbacks.getFormScopedCallbacksCalled());
    }

    @Test
    public void should_register_ajax_callbacks_on_form() {
        // when
        loadThePageAndWriteTextToInput("formScoped.jsf");

        // then
        assertCorrectCallbacks(false, callbacks.getDocumentScopedCallbacksCalled());
        assertCorrectCallbacks(true, callbacks.getFormScopedCallbacksCalled());
    }

    @Test
    public void should_register_ajax_callbacks_on_document_object() {
        // when
        loadThePageAndWriteTextToInput("documentScoped.jsf");

        // then
        assertCorrectCallbacks(true, callbacks.getDocumentScopedCallbacksCalled());
        assertCorrectCallbacks(false, callbacks.getFormScopedCallbacksCalled());
    }

    private void loadThePageAndWriteTextToInput(String pageName) {
        driver.get(contextPath.toExternalForm() + pageName);

        String toWrite = "text";
        for (char ch : toWrite.toCharArray()) {
            resetCallbacks();
            Graphene.guardAjax(input).sendKeys(Character.toString(ch));
        }
    }

    private void assertCorrectCallbacks(boolean registered, String callbacksToControll) {
        if (registered) {
            assertEquals(Arrays.asList(callbacksToControll.trim().split(" ")), EVENT_CALLBACKS);
        } else {
            assertEquals("Event callback was called even when it was not registered: " + callbacksToControll, " ", callbacksToControll);
        }
    }

    private static FaceletAsset buildPage(boolean formScopedRegistration, boolean pageScopedRegistration) {
        FaceletAsset p = new FaceletAsset();

        p.head("<h:outputScript library='javax.faces' name='jsf.js' />");
        p.head("<h:outputScript library='org.richfaces' name='jquery.js' />");
        p.head("<h:outputScript library='org.richfaces' name='richfaces.js' />");

        p.body("<h:outputScript>");
        p.body("  window.RichFaces.jQuery(document).ready(function() {");

        if (formScopedRegistration) {
            for (String eventCallback : EVENT_CALLBACKS) {
                registerCallBackToForm(p, eventCallback);
            }
        }

        if (pageScopedRegistration) {
            for (String eventCallback : EVENT_CALLBACKS) {
                registerCallBackToDocumentObject(p, eventCallback);
            }
        }

        p.body("    document.formScopedCallbacksCalled = ' ';");
        p.body("    document.documentScopedCallbacksCalled = ' ';");

        p.body("});");
        p.body("</h:outputScript>");

        p.body("<h:form prependId=\"false\" id=\"" + FORM_ID + "\">");
        p.body("  <h:inputText value=\"#{requestScope.property}\">");
        p.body("     <a4j:ajax event=\"keyup\" render=\"out\" />");
        p.body("  </h:inputText>");
        p.body("  <h:outputText value=\"#{requestScope.property}\" id=\"out\" />");
        p.body("</h:form>");

        return p;
    }

    private static void registerCallBackToForm(FaceletAsset p, String eventCallback) {
        p.body("    window.RichFaces.jQuery(document.getElementById('" + FORM_ID + "')).on(\"" + eventCallback + "\", function() {");
        p.body("         document.formScopedCallbacksCalled += '" + eventCallback + " ';");
        p.body("    });");
    }

    private static void registerCallBackToDocumentObject(FaceletAsset p, String eventCallback) {
        p.body("    window.RichFaces.jQuery(document).on(\"" + eventCallback + "\", function() {");
        p.body("         document.documentScopedCallbacksCalled += '" + eventCallback + " ';");
        p.body("    });");
    }

    private void resetCallbacks() {
        callbacks.setDocumentScopedCallbacksCalled(" ");
        callbacks.setFormScopedCallbacksCalled(" ");
    }

    @JavaScript("document")
    public interface Callbacks {

        String getFormScopedCallbacksCalled();

        String getDocumentScopedCallbacksCalled();

        void setFormScopedCallbacksCalled(String s);

        void setDocumentScopedCallbacksCalled(String s);
    }
}
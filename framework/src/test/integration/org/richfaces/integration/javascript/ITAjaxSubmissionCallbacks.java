package org.richfaces.integration.javascript;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
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
    private static final String[] EVENT_CALLBACKS = { "ajaxsubmit", "ajaxbegin", "ajaxbeforedomupdate", "ajaxcomplete" };
    private static final int NUMBER_OF_AJAX_REQUESTS = 4;

    @Deployment(name = "formAndDocumentScope")
    public static WebArchive createDeploymentBothFormAndDocumentScopeRegistration() {
        return addIndexPage(getCommonDeployment(ITAjaxSubmissionCallbacks1.class), true, true);
    }

    @Deployment(name = "formScope")
    public static WebArchive createDeploymentFormScopeRegistration() {
        return addIndexPage(getCommonDeployment(ITAjaxSubmissionCallbacks2.class), true, false);
    }

    @Deployment(name = "documentScope")
    public static WebArchive createDeploymentDocumentScopeRegistration() {
        return addIndexPage(getCommonDeployment(ITAjaxSubmissionCallbacks3.class), false, true);
    }
    
    private static WebArchive getCommonDeployment(Class<?> clazz) {
        CoreDeployment deployment = new CoreDeployment(clazz);
        deployment.withWholeCore();
        return deployment.getFinalArchive().addClass(AjaxSubmissionsCallbacksBean.class);
    }

    @Test
    @OperateOnDeployment("formAndDocumentScope")
    public void should_register_ajax_callbacks_both_on_form_and_document_object() {
        // when
        loadThePageAndWriteTextToInput();

        // then
        assertCorrectCallbacks(true, callbacks.getDocumentScopedCalledCallbacks());
        assertCorrectCallbacks(true, callbacks.getFormScopedCalledCallbacks());
    }

    @Test
    @OperateOnDeployment("formScope")
    public void should_register_ajax_callbacks_on_form() {
        // when
        loadThePageAndWriteTextToInput();

        // then
        assertCorrectCallbacks(false, callbacks.getDocumentScopedCalledCallbacks());
        assertCorrectCallbacks(true, callbacks.getFormScopedCalledCallbacks());
    }

    @Test
    @OperateOnDeployment("documentScope")
    public void should_register_ajax_callbacks_on_document_object() {
        // when
        loadThePageAndWriteTextToInput();

        // then
        assertCorrectCallbacks(true, callbacks.getDocumentScopedCalledCallbacks());
        assertCorrectCallbacks(false, callbacks.getFormScopedCalledCallbacks());
    }

    private void assertCorrectCallbacks(boolean registered, String callbacksToControll) {
        for (String callback : EVENT_CALLBACKS) {
            if (registered) {
                assertEquals("Event callback: " + callback + ", was not called enough times!", NUMBER_OF_AJAX_REQUESTS,
                    getCountOfSubString(callbacksToControll, callback));
            } else {
                assertEquals("Event callback: " + callback + ", was called even when it was not registered!", 0,
                    getCountOfSubString(callbacksToControll, callback));
            }
        }
    }

    private int getCountOfSubString(String str, String findStr) {
        int lastIndex = 0;
        int count = 0;

        while (lastIndex != -1) {

            lastIndex = str.indexOf(findStr, lastIndex);

            if (lastIndex != -1) {
                count++;
                lastIndex += findStr.length();
            }
        }
        return count;
    }

    private void loadThePageAndWriteTextToInput() {
        driver.get(contextPath.toExternalForm());

        String toWrite = "text";
        for (char ch : toWrite.toCharArray()) {
            Graphene.guardAjax(input).sendKeys(Character.toString(ch));
        }

        Graphene.waitAjax().until().element(output).text().equalTo(toWrite);
    }

    private static WebArchive addIndexPage(WebArchive deployment, boolean formScopedRegistration, boolean pageScopedRegistration) {
        FaceletAsset p = new FaceletAsset();

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

        p.body("    document.AjaxCallbacks = {};");
        p.body("    document.AjaxCallbacks.getFormScopedCalledCallbacks = function() {");
        p.body("       return document.formScopedCallbacksCalled;");
        p.body("    };");
        p.body("    document.AjaxCallbacks.getDocumentScopedCalledCallbacks = function() {");
        p.body("       return document.documentScopedCallbacksCalled;");
        p.body("    };");
        p.body("    document.formScopedCallbacksCalled = '';");
        p.body("    document.documentScopedCallbacksCalled = '';");

        p.body("});");
        p.body("</h:outputScript>");

        p.body("<h:form prependId=\"false\" id=\"" + FORM_ID + "\">");
        p.body("  <h:inputText value=\"#{ajaxSubmissionsCallbacksBean.property}\">");
        p.body("     <r:ajax event=\"keyup\" render=\"out\" />");
        p.body("  </h:inputText>");
        p.body("  <h:outputText value=\"#{ajaxSubmissionsCallbacksBean.property}\" id=\"out\" />");
        p.body("</h:form>");

        return deployment.addAsWebResource(p, "index.xhtml");
    }

    private static void registerCallBackToForm(FaceletAsset p, String eventCallback) {
        p.body("    window.RichFaces.jQuery(#{r:element('" + FORM_ID + "')}).on(\"" + eventCallback + "\", function() {");
        p.body("         document.formScopedCallbacksCalled += '" + eventCallback + " ';");
        p.body("    });");
    }

    private static void registerCallBackToDocumentObject(FaceletAsset p, String eventCallback) {
        p.body("    window.RichFaces.jQuery(document).on(\"" + eventCallback + "\", function() {");
        p.body("         document.documentScopedCallbacksCalled += '" + eventCallback + " ';");
        p.body("    });");
    }

    @JavaScript("document.AjaxCallbacks")
    public interface Callbacks {

        String getFormScopedCalledCallbacks();

        String getDocumentScopedCalledCallbacks();
    }

    private class ITAjaxSubmissionCallbacks1 {
    }

    private class ITAjaxSubmissionCallbacks2 {
    }

    private class ITAjaxSubmissionCallbacks3 {
    }
}
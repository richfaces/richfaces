package org.richfaces.component.fileUpload;

import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.jboss.arquillian.warp.client.filter.http.HttpFilters.request;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.servlet.AfterServlet;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.UIDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import category.FailingOnPhantomJS;
import category.Smoke;

@RunWith(Arquillian.class)
@RunAsClient
@WarpTest
public class ITFileUpload {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @Deployment
    public static WebArchive createDeployment() {
        UIDeployment deployment = new UIDeployment(ITFileUpload.class);

        deployment.archive().addClass(FileUploadBean.class);
        deployment.archive().addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @FindByJQuery("input[type=file].rf-fu-inp:last")
    private WebElement fileInputField;
    @FindBy(css = "span.rf-fu-btn-upl")
    private WebElement uploadButton;
    @FindBy(css = "a.rf-fu-itm-lnk")
    private WebElement firstAfterUploadClearLink;

    @ArquillianResource
    private JavascriptExecutor executor;

    @Test
    @Category({Smoke.class, FailingOnPhantomJS.class})
    public void test_file_upload() throws InterruptedException, URISyntaxException {
        browser.get(contextPath.toExternalForm());

        File file = new File(ITFileUpload.class.getResource("ITFileUpload.class").toURI());

        executor.executeScript("$(arguments[0]).css({ position: 'absolute', top: '100px', left: '100px', display: 'block', visibility: 'visible', width: '100px', height: '100px' })", fileInputField);
        fileInputField.sendKeys(file.getAbsolutePath());

        Warp
            .initiate(new Activity() {
                public void perform() {
                    uploadButton.click();
                }})
            .group()
                .observe(request().uri().contains("index.xhtml"))
                .inspect(new Inspection() {
                    private static final long serialVersionUID = 1L;

                    @AfterServlet
                    public void verifyUploadedFile(FileUploadBean bean) {
                        assertEquals("ITFileUpload.class", bean.getUploadedFile().getName());
                    }
                })
            .execute();

        waitAjax().until().element(firstAfterUploadClearLink).is().visible();
    }

    private static void addIndexPage(UIDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<script type='text/javascript'>");
        p.body("    jsf.ajax.addOnError(function(e) {");
        p.body("        console.log(e);");
        p.body("    });");
        p.body("</script>");
        p.body("<h:form>");
        p.body("    <rich:fileUpload fileUploadListener='#{fileUploadBean.listener}' />");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

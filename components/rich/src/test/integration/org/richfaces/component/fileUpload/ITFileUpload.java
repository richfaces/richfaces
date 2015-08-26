package org.richfaces.component.fileUpload;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.fileUpload.FileUploadItem;
import org.richfaces.fragment.fileUpload.RichFacesFileUpload;
import org.richfaces.fragment.list.ListComponent;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import category.Smoke;

@RunWith(Arquillian.class)
@RunAsClient
public class ITFileUpload {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @ArquillianResource
    private JavascriptExecutor executor;

    @FindBy(id = "fileUpload")
    private RichFacesFileUpload fileUpload;

    @FindBy(id = "output")
    private WebElement output;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITFileUpload.class);

        deployment.archive().addClass(FileUploadBean.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    @Category({ Smoke.class })
    public void test_file_upload() throws InterruptedException, URISyntaxException {
        browser.get(contextPath.toExternalForm());

        File file = new File(ITFileUpload.class.getResource("ITFileUpload.class").toURI());

        ListComponent<? extends FileUploadItem> items = fileUpload.advanced().getItems();
        Assert.assertTrue("List of uploaded file should be empty.", items.isEmpty());

        fileUpload.addFile(file);
        Assert.assertFalse("List of uploaded file should not be empty.", items.isEmpty());
        Assert.assertEquals("There should be Delete link in first item", "Delete", items.getItem(0)
            .getClearOrDeleteElement().getText());

        Graphene.guardAjax(fileUpload).upload();

        Assert.assertFalse("List of uploaded file should not be empty.", items.isEmpty());
        Assert.assertEquals("There should be Clear link in first item", "Clear", items.getItem(0)
            .getClearOrDeleteElement().getText());

        Assert.assertEquals("Uploaded file", "ITFileUpload.class", output.getText());
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<script type='text/javascript'>");
        p.body("    jsf.ajax.addOnError(function(e) {");
        p.body("        console.log(e);");
        p.body("    });");
        p.body("</script>");

        p.form("<rich:fileUpload id='fileUpload' fileUploadListener='#{fileUploadBean.listener}' render='output' />");
        p.form("<h:outputText id='output' value='#{fileUploadBean.uploadedFile.name}'/>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

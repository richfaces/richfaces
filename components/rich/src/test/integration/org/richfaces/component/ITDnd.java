package org.richfaces.component;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.RichDeployment;

import category.Smoke;

import java.io.File;
import java.net.URL;

@RunAsClient
@RunWith(Arquillian.class)
public class ITDnd {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "form:ind")
    private WebElement indicator;
    
    @FindBy(id = "form:php")
    private WebElement phpTarget;
    
    @FindBy(id = "form:dnet")
    private WebElement dnetTarget;
    
    @FindBy(id = "form:cf")
    private WebElement cfTarget;
    
    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITDnd.class);
        deployment.archive().addAsWebResource(new File("src/test/resources/org/richfaces/component/dnd.xhtml"));
        deployment.archive().addAsWebResource(new File("src/test/resources/images/dnd/accept.gif"), "/resources/images/dnd/accept.gif");
        deployment.archive().addAsWebResource(new File("src/test/resources/images/dnd/default.gif"), "/resources/images/dnd/default.gif");
        deployment.archive().addAsWebResource(new File("src/test/resources/images/dnd/reject.gif"), "/resources/images/dnd/reject.gif");
        deployment.archive().addClass(DragDropBean.class);
        deployment.archive().addClass(DragDropEventBean.class);
        deployment.archive().addClass(Framework.class);

        return deployment.getFinalArchive();
    }

    @Test
    @Category(Smoke.class)
    public void test_dnd() throws InterruptedException {
        // given
        browser.get(contextPath.toExternalForm() + "dnd.jsf");
        
        WebElement framework = browser.findElements(By.cssSelector(".ui-draggable")).get(0);

        Actions builder = new Actions(browser);
        final Action dragAndDrop = builder.dragAndDrop(framework, phpTarget).build();
        guardAjax(dragAndDrop).perform();

        Assert.assertEquals(1, phpTarget.findElements(By.cssSelector("td")).size());
    }

}

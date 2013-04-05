package org.richfaces.ui.menu;

import static org.jboss.arquillian.graphene.Graphene.guardNoRequest;
import static org.jboss.arquillian.warp.jsf.Phase.RENDER_RESPONSE;
import static org.junit.Assert.assertEquals;

import java.net.URL;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.jsf.AfterPhase;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.deployment.FrameworkDeployment;

@RunWith(Arquillian.class)
@RunAsClient
@WarpTest
public class ITDropDownMenuItem {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @Deployment
    public static WebArchive createDeployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(ITDropDownMenuItem.class);
        deployment.archive()
            .addClasses(DropDownMenuBean.class)
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsWebResource(ITDropDownMenuItem.class.getResource("menuItem_serverMode.xhtml"), "menuItem_serverMode.xhtml")
            .addAsWebResource(ITDropDownMenuItem.class.getResource("menuItem_ajaxMode.xhtml"), "menuItem_ajaxMode.xhtml")
            .addAsWebResource(ITDropDownMenuItem.class.getResource("menuItem_clientMode.xhtml"), "menuItem_clientMode.xhtml");

        return deployment.getFinalArchive();
    }

    @FindBy(className ="rf-ddm-itm-lbl")
    private WebElement menuItem;

    @Test
    public void testServer() {
        browser.get(contextPath.toString() + "menuItem_serverMode.jsf");
        verifyOnServer();
    }

    @Test
    public void testAjax() {
        browser.get(contextPath.toString() + "menuItem_ajaxMode.jsf");
        verifyOnServer();
    }

    @Test
    public void testClick() {
        browser.get(contextPath.toString() + "menuItem_clientMode.jsf");
        guardNoRequest(menuItem).click();
    }

    private void verifyOnServer() {
        Warp
        .initiate(new Activity() {
            public void perform() {
                menuItem.click();
            }
        })
        .inspect(new Inspection() {
            private static final long serialVersionUID = 1L;

            @Inject
            private DropDownMenuBean ddmBean;

            @AfterPhase(RENDER_RESPONSE)
            public void verifyAction() {
                assertEquals("action", ddmBean.getCurrent());
            }
        });
    }
}
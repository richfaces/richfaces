/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.richfaces.integration.javascript;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.warp.jsf.Phase.INVOKE_APPLICATION;
import static org.jboss.arquillian.warp.jsf.Phase.RENDER_RESPONSE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import javax.faces.context.FacesContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.jsf.AfterPhase;
import org.jboss.arquillian.warp.jsf.BeforePhase;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.deployment.CoreDeployment;
import org.richfaces.javascript.JSLiteral;
import org.richfaces.javascript.JavaScriptService;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import category.Smoke;

@RunWith(Arquillian.class)
@WarpTest
@RunAsClient
@Category(Smoke.class)
public class ITJavaScriptServiceAjax {

    @Drone
    private WebDriver driver;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "richfacesAjax")
    private WebElement richfacesAjax;

    @FindBy(id = "jsfAjax")
    private WebElement jsfAjax;

    @FindBy(id = "buttonWithOnComplete")
    private WebElement buttonWithOncomplete;

    @ArquillianResource
    private JavascriptExecutor executor;

    @Deployment
    public static WebArchive createDeployment() {
        CoreDeployment deployment = new CoreDeployment(ITJavaScriptServiceAjax.class);

        deployment.withWholeCore();

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void richfaces_ajax_should_trigger_script_added_by_JavaScriptService() {
        driver.navigate().to(contextPath);

        Warp.initiate(new Activity() {
            public void perform() {
                guardAjax(richfacesAjax).click();
            }
        }).inspect(new AddScriptAfterInvokeApplication());

        assertEquals("executed", driver.getTitle());
    }

    @Test
    public void jsf_ajax_should_trigger_script_added_by_JavaScriptService() {
        driver.navigate().to(contextPath);

        Warp.initiate(new Activity() {
            public void perform() {
                guardAjax(jsfAjax).click();
            }
        }).inspect(new AddScriptAfterInvokeApplication());

        assertEquals("executed", driver.getTitle());
    }

    @Test
    public void javascript_service_complete_event_callback_should_be_called_after_oncomplete() {
        driver.navigate().to(contextPath);

        Warp.initiate(new Activity() {
            public void perform() {
                guardAjax(buttonWithOncomplete).click();
            }
        }).inspect(new AddScriptInBeforeRender());

        String title = (String) executor.executeScript("return document.title;");
        assertTrue("oncomplete callback should be called before javascriptservice callback!",
            title.indexOf("oncomplete") < title.indexOf("javascriptservice"));
    }

    private static void addIndexPage(CoreDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.head("<title>initial value</title>");

        p.head("<h:outputScript name='jsf.js' library='javax.faces' />");
        p.head("<h:outputScript name='jquery.js' />");
        p.head("<h:outputScript name='richfaces.js' />");

        p.form("<r:commandButton id='buttonWithOnComplete' onclick='RichFaces.ajax(this, event, {}); return false;' oncomplete=\"document.title += ' oncomplete';\" />");
        p.form("<h:commandButton id='richfacesAjax' onclick='RichFaces.ajax(this, event, {}); return false;' />");
        p.form("<h:commandButton id='jsfAjax' onclick='jsf.ajax.request(this, event, {}); return false;' />");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

    public static class AddScriptInBeforeRender extends Inspection {
        private static final long serialVersionUID = 1L;

        @ArquillianResource
        private FacesContext facesContext;

        @ArquillianResource
        private JavaScriptService jsService;

        @BeforePhase(RENDER_RESPONSE)
        public void add_script_using_JavaScriptService() {
            jsService.addScript(facesContext, new JSLiteral("document.title += ' javascriptservice';"));
        }
    }

    public static class AddScriptAfterInvokeApplication extends Inspection {
        private static final long serialVersionUID = 1L;

        @ArquillianResource
        private FacesContext facesContext;

        @ArquillianResource
        private JavaScriptService jsService;

        @AfterPhase(INVOKE_APPLICATION)
        public void add_script_using_JavaScriptService() {
            jsService.addScript(facesContext, new JSLiteral("document.title = 'executed'"));
        }
    }
}
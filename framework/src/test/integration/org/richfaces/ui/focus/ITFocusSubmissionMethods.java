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
package org.richfaces.ui.focus;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.waitAjax;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.context.ExtendedPartialViewContext;
import org.richfaces.deployment.FrameworkDeployment;
import org.richfaces.javascript.JavaScriptService;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;
import org.richfaces.ui.misc.focus.FocusRendererBase;

import category.Failing;

@RunAsClient
@WarpTest
@RunWith(Arquillian.class)
public class ITFocusSubmissionMethods {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "form:submit")
    private WebElement submitButton;

    @FindBy(id = "form:ajaxJSF")
    private WebElement ajaxJSF;

    @FindBy(id = "form:ajaxRF")
    private WebElement ajaxRF;

    @FindBy(id = "form:ajaxCommandButton")
    private WebElement ajaxCommandButton;

    @FindBy(id = "form:input2")
    private WebElement input2;

    @Deployment
    public static WebArchive createDeployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(ITFocusSubmissionMethods.class);

        deployment.archive().addClasses(ComponentBean.class);
        deployment.archive().addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Before
    public void openInitialPage() {
        browser.get(contextPath.toExternalForm());
    }

    @Test
    public void testFocusAfterFormSubmission() {
        // when
        input2.click();
        guardHttp(submitButton).click();

        // then
        Graphene.waitGui().until(new ElementIsFocused(input2));
    }

    /**
     * Won't work with f:ajax since it does not support {@link ExtendedPartialViewContext} with oncomplete, which is used by
     * {@link JavaScriptService} in method
     * {@link FocusRendererBase#renderOncompleteScript(javax.faces.context.FacesContext, String)}.
     */
    @Test
    @Category(Failing.class)
    public void testFocusAfterAjaxJSF() {
        // when
        input2.click();
        guardAjax(ajaxJSF).click();

        // then
        waitAjax().until(new ElementIsFocused(input2));
    }

    @Test
    public void testFocusAfterAjaxRF() {
        // when
        input2.click();
        guardAjax(ajaxRF).click();

        // then
        waitAjax().until(new ElementIsFocused(input2));
    }

    @Test
    public void testFocusAfterAjaxCommandButton() {
        // when
        input2.click();
        guardAjax(ajaxCommandButton).click();

        // then
        waitAjax().until(new ElementIsFocused(input2));
    }

    private static void addIndexPage(FrameworkDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<h:form id='form'>");
        p.body("    <r:focus id='focus' preserve='true' />");

        p.body("    <h:inputText id='input1' />");
        p.body("    <h:inputText id='input2' />");

        p.body("    <h:commandButton id='submit' value='Submit' />");

        p.body("    <h:commandButton id='ajaxJSF' value='Ajax JSF'>");
        p.body("        <f:ajax render='@form' />");
        p.body("    </h:commandButton>");

        p.body("    <h:commandButton id='ajaxRF' value='Ajax RF'>");
        p.body("        <r:ajax render='@form' />");
        p.body("    </h:commandButton>");

        p.body("    <r:commandButton id='ajaxCommandButton' render='@form' value='Ajax Command Button' />");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

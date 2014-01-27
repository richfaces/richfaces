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

import static org.jboss.arquillian.graphene.Graphene.waitAjax;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.deployment.FrameworkDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@WarpTest
@RunWith(Arquillian.class)
public class ITFocusMultipleForms {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "form1")
    private Form form1;

    @FindBy(id = "form2")
    private Form form2;

    @FindBy(id = "form3")
    private Form form3;

    @Deployment
    public static WebArchive createDeployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(ITFocusMultipleForms.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void when_page_is_loaded_initially_then_first_input_from_first_form_should_gain_focus() {
        browser.get(contextPath.toExternalForm());

        Graphene.waitGui().until(new ElementIsFocused(form1.getInput1()));
    }

    @Test
    public void when_form_is_submitted_then_first_input_from_this_form_should_gain_focus() {
        browser.get(contextPath.toExternalForm());

        form2.submit();
        Graphene.waitGui().until(new ElementIsFocused(form2.getInput2()));

        form1.submit();
        Graphene.waitGui().until(new ElementIsFocused(form1.getInput2()));

        form3.submit();
        Graphene.waitGui().until(new ElementIsFocused(form3.getInput1()));
    }

    @Test
    public void when_ajax_is_sent_then_first_input_from_submitted_form_should_gain_focus() {
        browser.get(contextPath.toExternalForm());

        form2.submitAjax();
        waitAjax().until(new ElementIsFocused(form2.getInput2()));

        form1.submitAjax();
        waitAjax().until(new ElementIsFocused(form1.getInput2()));

        form3.submitAjax();
        waitAjax().until(new ElementIsFocused(form3.getInput1()));
    }

    private static void addIndexPage(FrameworkDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<h:form id='form1'>");
        p.body("    <r:focus id='focus' />");

        p.body("    <h:inputText id='input1' />");
        p.body("    <h:inputText id='input2' required='true' />");

        p.body("    <h:commandButton id='submit' value='Submit' />");

        p.body("    <r:commandButton id='ajax' render='@form' value='Ajax' />");
        p.body("</h:form>");

        p.body("<h:form id='form2'>");
        p.body("    <r:focus id='focus' />");

        p.body("    <h:inputText id='input1' />");
        p.body("    <h:inputText id='input2' required='true' />");

        p.body("    <h:commandButton id='submit' value='Submit' />");

        p.body("    <r:commandButton id='ajax' render='@form' value='Ajax' />");
        p.body("</h:form>");

        p.body("<h:form id='form3'>");
        p.body("    <r:focus id='focus' />");

        p.body("    <h:inputText id='input1' />");
        p.body("    <h:inputText id='input2' />");

        p.body("    <h:commandButton id='submit' value='Submit' />");

        p.body("    <r:commandButton id='ajax' render='@form' value='Ajax' />");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

    public static class Form {

        @FindBy(css = "[id$=input1]")
        private WebElement input1;

        @FindBy(css = "[id$=input2]")
        private WebElement input2;

        @FindBy(css = "[id$=submit]")
        private WebElement submit;

        @FindBy(css = "[id$=ajax]")
        private WebElement ajax;

        public WebElement getInput1() {
            return input1;
        }

        public WebElement getInput2() {
            return input2;
        }

        public void submit() {
            Graphene.guardHttp(submit).click();
        }

        public void submitAjax() {
            Graphene.guardAjax(ajax).click();
        }
    }
}

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

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.impl.utils.URLUtils;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.deployment.FrameworkDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import category.Smoke;

@RunAsClient
@WarpTest
@RunWith(Arquillian.class)
public class ITFocusManager {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "form:submit")
    private WebElement submitButton;

    @FindBy(id = "form:ajax")
    private WebElement ajaxButton;

    @FindBy(id = "form:input2")
    private WebElement input2;

    private Activity openPage = new Activity() {
        public void perform() {
            browser.get(contextPath.toExternalForm());
        }
    };

    private Activity submit = new Activity() {
        public void perform() {
            guardHttp(submitButton).click();
        }
    };

    private Activity ajax = new Activity() {
        public void perform() {
            guardAjax(ajaxButton).click();
        }
    };

    @Deployment
    public static WebArchive createDeployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(ITFocusManager.class);

        deployment.archive().addClasses(ComponentBean.class, AbstractComponentAssertion.class)
                .addClasses(VerifyFocusEnforcing.class, VerifyFocusEnforcingOverridesFocusSettings.class);
        deployment.archive().addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        addIndexPage(deployment);
        addViewFocusPage(deployment);
        addFormFocusIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    @Category(Smoke.class)
    public void test_FocusManager_on_initial_request() {
        Warp.initiate(openPage).inspect(new VerifyFocusEnforcing("input2"));
        
        Graphene.waitGui().until(new ElementIsFocused(input2));
    }

    @Test
    public void test_FocusManager_on_form_submit_postback() {
        // given
        browser.get(contextPath.toExternalForm());
        // when
        Warp.initiate(submit)
        // then
                .inspect(new VerifyFocusEnforcing("input2"));
        Graphene.waitGui().until(new ElementIsFocused(input2));
    }

    @Test
    public void test_FocusManager_on_ajax_postback() {
        // given
        browser.get(contextPath.toExternalForm());
        // when
        Warp.initiate(ajax)
        // then
                .inspect(new VerifyFocusEnforcing("input2"));
        Graphene.waitGui().until(new ElementIsFocused(input2));
    }

    @Test
    public void when_there_is_form_based_focus_but_focus_was_enforced_using_FocusManager_then_it_is_not_aplied() {

        contextPath = URLUtils.buildUrl(contextPath, "form.jsf");

        Warp.initiate(openPage).inspect(new VerifyFocusEnforcingOverridesFocusSettings("form:input2"));
        Graphene.waitGui().until(new ElementIsFocused(input2));

        Warp.initiate(submit).inspect(new VerifyFocusEnforcingOverridesFocusSettings("form:input2"));
        Graphene.waitGui().until(new ElementIsFocused(input2));

        Warp.initiate(ajax).inspect(new VerifyFocusEnforcingOverridesFocusSettings("form:input2"));
        Graphene.waitGui().until(new ElementIsFocused(input2));
    }

    @Test
    public void when_there_is_view_based_focus_but_focus_was_enforced_using_FocusManager_then_it_is_not_aplied() {

        contextPath = URLUtils.buildUrl(contextPath, "form.jsf");

        Warp.initiate(openPage).inspect(new VerifyFocusEnforcingOverridesFocusSettings("form:input2"));
        Graphene.waitGui().until(new ElementIsFocused(input2));

        Warp.initiate(submit).inspect(new VerifyFocusEnforcingOverridesFocusSettings("form:input2"));
        Graphene.waitGui().until(new ElementIsFocused(input2));

        Warp.initiate(ajax).inspect(new VerifyFocusEnforcingOverridesFocusSettings("form:input2"));
        Graphene.waitGui().until(new ElementIsFocused(input2));
    }

    private WebElement getFocusedElement() {
        return FocusRetriever.retrieveActiveElement();
    }

    private static void addIndexPage(FrameworkDeployment deployment) {
        FaceletAsset p = new FaceletAsset();



        p.body("<h:form id='form'>");

        p.body("    <h:inputText id='input1' />");
        p.body("    <h:inputText id='input2' />");

        p.body("    <h:commandButton id='submit' value='Submit' />");

        p.body("    <r:commandButton id='ajax' render='@form' value='Ajax' />");

        p.body("</h:form> <ui:debug />");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

    private static void addFormFocusIndexPage(FrameworkDeployment deployment) {
        FaceletAsset p = new FaceletAsset();



        p.body("<h:form id='form'>");
        p.body("    <r:focus id='focus' binding='#{componentBean.component}' />");

        p.body("    <h:inputText id='input1' />");
        p.body("    <h:inputText id='input2' />");

        p.body("    <h:commandButton id='submit' value='Submit' />");

        p.body("    <r:commandButton id='ajax' render='@form' value='Ajax' />");

        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "form.xhtml");
    }

    private static void addViewFocusPage(FrameworkDeployment deployment) {
        FaceletAsset p = new FaceletAsset();



        p.body("<r:focus id='focus' binding='#{componentBean.component}' />");

        p.body("<h:form id='form'>");

        p.body("    <h:inputText id='input1' />");
        p.body("    <h:inputText id='input2' />");

        p.body("    <h:commandButton id='submit' value='Submit' />");

        p.body("    <r:commandButton id='ajax' render='@form' value='Ajax' />");

        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "view.xhtml");
    }
}

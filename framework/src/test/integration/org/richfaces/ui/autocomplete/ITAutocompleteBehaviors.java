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

package org.richfaces.ui.autocomplete;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.jsf.AfterPhase;
import org.jboss.arquillian.warp.jsf.Phase;
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
public class ITAutocompleteBehaviors {

    @Drone
    WebDriver browser;

    @ArquillianResource
    URL contextPath;

    @FindBy(id = "form:render")
    WebElement renderButton;

    @FindBy(css = "body")
    WebElement body;

    @FindBy(id = "autocomplete")
    RichAutocomplete autocomplete;

    @Deployment
    public static WebArchive createDeployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(ITAutocompleteBehaviors.class);

        deployment.archive().addClasses(AutocompleteBean.class).addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    /**
     * onblur should have input value available via 'this.value' expression (RF-12114)
     */
    @Test
    @Category(Smoke.class)
    public void testAjaxOnBlur() {

        // given
        browser.get(contextPath.toExternalForm());

        autocomplete.type("t");
        autocomplete.waitForSuggestionsToShow();
        autocomplete.selectFirst();

        // when / then
        Warp.initiate(new Activity() {

            @Override
            public void perform() {
                guardAjax(body).click();
            }
        }).inspect(new Inspection() {
            private static final long serialVersionUID = 1L;

            @Inject
            AutocompleteBean bean;

            @AfterPhase(Phase.INVOKE_APPLICATION)
            public void verify_bean_executed() {
                assertTrue("action listener is invoked", bean.isListenerInvoked());
            }
        });
    }

    private static void addIndexPage(FrameworkDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.form("<r:autocomplete id='autocomplete' mode='client' autocompleteList='#{autocompleteBean.suggestions}'>");
        p.form("    <r:ajax event='blur' listener='#{autocompleteBean.actionListener}' />");
        p.form("</r:autocomplete>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

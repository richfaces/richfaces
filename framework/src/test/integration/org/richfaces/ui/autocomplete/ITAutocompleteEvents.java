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
import static org.jboss.arquillian.graphene.Graphene.waitGui;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.deployment.FrameworkDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@RunWith(Arquillian.class)
public class ITAutocompleteEvents {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "render")
    private WebElement renderButton;

    @FindBy(id = "blur-handler")
    private RichAutocomplete autocompleteBlurHandler;

    @FindBy(id = "change-ajax")
    private RichAutocomplete autocompleteChangeAjax;

    @FindBy(id = "input")
    private WebElement input;

    @Deployment
    public static WebArchive createDeployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(ITAutocompleteEvents.class);

        deployment.archive().addClasses(AutocompleteBean.class).addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    /**
     * onblur should have input value available via 'this.value' expression
     *
     * regressions test for RF-12605
     */
    @Test
    public void testOnblurEventPayload() {
        // given
        browser.get(contextPath.toExternalForm());

        autocompleteBlurHandler.type("t").selectFirst();

        // when
        input.click();

        // then
        waitGui().until().element(autocompleteBlurHandler.getInput()).attribute("value").equalTo("TORONTO");
    }

    @Test
    public void test_ajax_on_change() {
        browser.get(contextPath.toExternalForm());

        autocompleteChangeAjax.type("t").selectFirst();

        // when
        guardAjax(input).click();
    }

    private static void addIndexPage(FrameworkDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.form("<input id='input' placeholder='an element to switch focus' />");

        p.form("<r:autocomplete id='blur-handler' autocompleteList='#{autocompleteBean.suggestions}'");
        p.form("    onblur='this.value = this.value.toUpperCase()'  />");

        p.form("<r:autocomplete id='change-ajax' autocompleteList='#{autocompleteBean.suggestions}'>");
        p.form("    <r:ajax event='change' />");
        p.form("</r:autocomplete>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

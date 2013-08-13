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

    @FindBy(css = ".r-autocomplete")
    private RichAutocomplete autocomplete;

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
     */
    @Test
    // RF-12605
    public void testOnblurEventPayload() {
        // given
        browser.get(contextPath.toExternalForm());

        autocomplete.type("t").selectFirst();

        // when
        input.click();

        // then
        waitGui().until().element(autocomplete.getInput()).attribute("value").equalTo("TORONTO");
    }

    private static void addIndexPage(FrameworkDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.form("<input id='input' placeholder='an element to switch focus' />");

        p.form("<r:autocomplete autocompleteList='#{autocompleteBean.suggestions}'");
        p.form("    onblur='this.value = this.value.toUpperCase()'  />");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

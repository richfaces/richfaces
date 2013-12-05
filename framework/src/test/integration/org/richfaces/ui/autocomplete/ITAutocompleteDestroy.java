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

import static org.hamcrest.Matchers.empty;
import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

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
public class ITAutocompleteDestroy {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "render")
    private WebElement renderButton;

    @FindBy(css = ".r-autocomplete")
    private RichAutocomplete autocomplete;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(ITAutocompleteDestroy.class);

        deployment.archive().addClasses(AutocompleteBean.class).addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    // RF-12481
    public void when_suggestion_list_opened_and_autocomplete_is_rerendered_then_it_should_not_be_visible() {
        // given
        browser.get(contextPath.toExternalForm());
        autocomplete.type("t");

        // when
        guardAjax(renderButton).click();

        // then
        assertEquals("suggestion list is not displayed", 0, autocomplete.getSuggestions().size());
        assertThat(autocomplete.getSuggestions(), empty());
    }

    private static void addIndexPage(FrameworkDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.form("<h:commandButton id='render' value='Render'>");
        p.form("    <f:ajax execute='@form' render='@form'/>");
        p.form("</h:commandButton>");

        p.form("<r:autocomplete autocompleteList='#{autocompleteBean.suggestions}' />");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

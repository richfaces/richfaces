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

import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.richfaces.deployment.FrameworkDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import category.JSF22Only;
import category.Smoke;

@RunWith(Arquillian.class)
public class ITAutocompletePassThroughAttributes {

    @Drone
    WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "firstAutocomplete")
    private RichAutocomplete firstAutocomplete;

    @FindBy(id = "secondAutocomplete")
    private RichAutocomplete secondAutocomplete;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(ITAutocompletePassThroughAttributes.class);

        deployment.archive().addClasses(AutocompleteBean.class).addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    private static void addIndexPage(FrameworkDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        // add a namespace with placeholder
        p.xmlns("p", "http://xmlns.jcp.org/jsf/passthrough");
        // first option to use pass through attributes
        p.form("<r:autocomplete id='firstAutocomplete' autocompleteList='#{autocompleteBean.suggestions}' >");
        p.form("<f:passThroughAttribute name='placeholder' value='Enter text' />");
        p.form("</r:autocomplete>");

        p.form("<br/>");

        // second option
        p.form("<r:autocomplete id='secondAutocomplete' p:placeholder='Enter text' autocompleteList='#{autocompleteBean.suggestions}' />");
        p.form("<br/>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

    /**
     * This test is aimed to ensure the basic functionality of autocomplete if not affected. This test consists of code snippets
     * simple option select and suggestion assertion.
     */
    @Test
    @Category({ JSF22Only.class, Smoke.class })
    public void testFunctionality() {
        // load page
        browser.get(contextPath.toExternalForm());

        // simple selection for both autocompletes
        firstAutocomplete.type("T");
        assertEquals(2, firstAutocomplete.getSuggestions().size());
        firstAutocomplete.selectFirst();
        assertEquals("Toronto", firstAutocomplete.getInput().getAttribute("value"));

        secondAutocomplete.type("N");
        assertEquals(1, secondAutocomplete.getSuggestions().size());
        secondAutocomplete.selectFirst();
        assertEquals("New York", secondAutocomplete.getInput().getAttribute("value"));

    }

    /**
     * This test is aimed to ensure the mark-up is correct and pass-through attribute is present.
     */
    @Test
    @Category({ JSF22Only.class, Smoke.class })
    public void testMarkUp() {
        // load page
        browser.get(contextPath.toExternalForm());

        // assert for first autocomlete with <f:passThroughAttribute>
        String firstPlaceholder = browser.findElement(By.id("firstAutocomplete")).getAttribute("placeholder");
        assertEquals("Enter text", firstPlaceholder);

        // assert for second autocomplete with p:placeholder='Enter text'
        String secondPlaceholder = browser.findElement(By.id("secondAutocomplete")).getAttribute("placeholder");
        assertEquals("Enter text", secondPlaceholder);
    }
}

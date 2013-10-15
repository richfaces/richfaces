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
import static org.junit.Assert.assertEquals;

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
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.deployment.FrameworkDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@RunWith(Arquillian.class)
public class ITAutocompleteTokenizing {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(css = ".r-autocomplete")
    private RichAutocomplete autocomplete;

    @ArquillianResource
    private Actions actions;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(ITAutocompleteTokenizing.class);

        deployment.archive().addClasses(AutocompleteBean.class).addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void testAutofillDisabledSelectionByMouse() {
        browser.get(contextPath.toExternalForm() + "?autofill=false");

        autocomplete.type("t,");
        assertEquals(4, autocomplete.getSuggestions().size());
        assertEquals("t,", autocomplete.getInput().getAttribute("value"));

        WebElement secondItem = autocomplete.getSuggestions().get(1);

        actions.moveToElement(secondItem).perform();
        waitUntilItemFocused(secondItem);
        autocomplete.waitForSuggestionsToShow();
        assertEquals(4, autocomplete.getSuggestions().size());
        assertEquals("t,", autocomplete.getInput().getAttribute("value"));

        secondItem.click();
        autocomplete.waitForSuggestionsToHide();
        assertEquals("t, New York", autocomplete.getInput().getAttribute("value"));

        autocomplete.type(", ");
        autocomplete.waitForSuggestionsToShow();
        assertEquals(4, autocomplete.getSuggestions().size());
        assertEquals("t, New York, ", autocomplete.getInput().getAttribute("value"));

        WebElement thirdItem = autocomplete.getSuggestions().get(2);

        actions.moveToElement(thirdItem).perform();
        waitUntilItemFocused(thirdItem);
        autocomplete.waitForSuggestionsToShow();
        assertEquals(4, autocomplete.getSuggestions().size());
        assertEquals("t, New York, ", autocomplete.getInput().getAttribute("value"));

        thirdItem.click();
        autocomplete.waitForSuggestionsToHide();
        assertEquals("t, New York, San Francisco", autocomplete.getInput().getAttribute("value"));
    }

    @Test
    public void testAutofillEnabledSelectionByMouse() {
        browser.get(contextPath.toExternalForm() + "?autofill=true");

        autocomplete.type("t, t");
        autocomplete.waitForSuggestionsToShow();
        assertEquals(2, autocomplete.getSuggestions().size());
        assertEquals("t, toronto", autocomplete.getInput().getAttribute("value"));

        WebElement secondItem = autocomplete.getSuggestions().get(1);

        actions.moveToElement(secondItem).perform();
        waitUntilItemFocused(secondItem);
        waitUntilInputValueChangesTo("t, tampa Bay");
        autocomplete.waitForSuggestionsToShow();
        assertEquals(2, autocomplete.getSuggestions().size());

        secondItem.click();
        autocomplete.waitForSuggestionsToHide();
        assertEquals("t, Tampa Bay", autocomplete.getInput().getAttribute("value"));

        autocomplete.type(", ");
        autocomplete.waitForSuggestionsToShow();
        assertEquals(4, autocomplete.getSuggestions().size());
        assertEquals("t, Tampa Bay, ", autocomplete.getInput().getAttribute("value"));

        WebElement thirdItem = autocomplete.getSuggestions().get(2);

        actions.moveToElement(thirdItem).perform();
        waitUntilInputValueChangesTo("t, Tampa Bay, ");
        autocomplete.waitForSuggestionsToShow();
        assertEquals(4, autocomplete.getSuggestions().size());

        thirdItem.click();
        autocomplete.waitForSuggestionsToHide();
        assertEquals("t, Tampa Bay, San Francisco", autocomplete.getInput().getAttribute("value"));
    }


    @Test
    public void when_space_is_not_token_then_it_should_not_be_used_to_separate_input() {
        browser.get(contextPath.toExternalForm() + "?autofill=false");

        autocomplete.type("t");
        autocomplete.waitForSuggestionsToShow();

        autocomplete.getInput().sendKeys(" ");
        autocomplete.waitForSuggestionsToHide();
    }

    private Void waitUntilItemFocused(WebElement item) {
        return waitGui().until().element(item.findElement(By.className("ui-state-focus"))).is().present();
    }

    private Void waitUntilInputValueChangesTo(String to) {
        return waitGui().until().element(autocomplete.getInput()).attribute("value").equalTo(to);
    }

    private static void addIndexPage(FrameworkDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<h:form id='form'>");
        p.body("    <r:autocomplete id='autocomplete' mode='client' autocompleteList='#{autocompleteBean.suggestions}' tokens=',' autofill='#{param.autofill}' minChars='0' />");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}
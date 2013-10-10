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
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.deployment.FrameworkDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import category.Failing;

@RunAsClient
@RunWith(Arquillian.class)
@Category(Failing.class) // RFPL-3043
public class ITAutocompleteTokenizing {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(css = "input.rf-au-inp")
    private WebElement autocompleteInput;

    @FindBy(css = ".rf-au-itm")
    private List<WebElement> autocompleteItems;

    @ArquillianResource
    private Actions actions;

    @FindBy(css = ".rf-au-lst-cord")
    WebElement suggestionList;

    @Deployment
    public static WebArchive createDeployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(ITAutocompleteTokenizing.class);

        deployment.archive().addClasses(AutocompleteBean.class).addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void testAutofillDisabledSelectionByMouse() {
        browser.get(contextPath.toExternalForm() + "?autofill=false");

        autocompleteInput.sendKeys("t,");
        waitGui().until().element(suggestionList).is().visible();
        assertEquals(4, autocompleteItems.size());
        assertEquals("t,", autocompleteInput.getAttribute("value"));

        WebElement secondItem = autocompleteItems.get(1);

        actions.moveToElement(secondItem).perform();
        waitGui().until().element(secondItem).attribute("class").contains("rf-au-itm-sel");
        assertTrue(suggestionList.isDisplayed());
        assertEquals(4, autocompleteItems.size());
        assertEquals("t,", autocompleteInput.getAttribute("value"));

        secondItem.click();
        waitGui().until().element(suggestionList).is().not().visible();
        assertEquals("t,New York", autocompleteInput.getAttribute("value"));

        autocompleteInput.sendKeys(", ");
        waitGui().until().element(suggestionList).is().visible();
        assertEquals(4, autocompleteItems.size());
        assertEquals("t,New York, ", autocompleteInput.getAttribute("value"));

        WebElement thirdItem = autocompleteItems.get(2);

        actions.moveToElement(thirdItem).perform();
        waitGui().until().element(thirdItem).attribute("class").contains("rf-au-itm-sel");
        assertTrue(suggestionList.isDisplayed());
        assertEquals(4, autocompleteItems.size());
        assertEquals("t,New York, ", autocompleteInput.getAttribute("value"));

        thirdItem.click();
        waitGui().until().element(suggestionList).is().not().visible();
        assertEquals("t,New York, San Francisco", autocompleteInput.getAttribute("value"));
    }

    @Test
    public void testAutofillEnabledSelectionByMouse() {
        browser.get(contextPath.toExternalForm() + "?autofill=true");

        autocompleteInput.sendKeys("t,");
        waitGui().until().element(suggestionList).is().visible();
        assertEquals(4, autocompleteItems.size());
        assertEquals("t,Toronto", autocompleteInput.getAttribute("value"));

        WebElement secondItem = autocompleteItems.get(1);

        actions.moveToElement(secondItem).perform();
        waitGui().until().element(autocompleteInput).attribute("value").equalTo("t,New York");
        assertTrue(suggestionList.isDisplayed());
        assertEquals(4, autocompleteItems.size());

        secondItem.click();
        waitGui().until().element(suggestionList).is().not().visible();
        assertEquals("t,New York", autocompleteInput.getAttribute("value"));

        autocompleteInput.sendKeys(", ");
        waitGui().until().element(suggestionList).is().visible();
        assertEquals(4, autocompleteItems.size());
        assertEquals("t,New York, Toronto", autocompleteInput.getAttribute("value"));

        WebElement thirdItem = autocompleteItems.get(2);

        actions.moveToElement(thirdItem).perform();
        waitGui().until().element(autocompleteInput).attribute("value").equalTo("t,New York, San Francisco");
        assertTrue(suggestionList.isDisplayed());
        assertEquals(4, autocompleteItems.size());

        thirdItem.click();
        waitGui().until().element(suggestionList).is().not().visible();
        assertEquals("t,New York, San Francisco", autocompleteInput.getAttribute("value"));
    }

    @Test
    public void when_space_is_not_token_then_it_should_not_be_used_to_separate_input() {
        browser.get(contextPath.toExternalForm() + "?autofill=false");

        autocompleteInput.sendKeys("t");
        waitGui().until().element(suggestionList).is().visible();

        autocompleteInput.sendKeys(" ");
        waitGui().until().element(suggestionList).is().not().visible();
    }

    private static void addIndexPage(FrameworkDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<h:form id='form'>");
        p.body("    <r:autocomplete id='autocomplete' mode='client' autocompleteList='#{autocompleteBean.suggestions}' tokens=',' autofill='#{param.autofill}' />");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}
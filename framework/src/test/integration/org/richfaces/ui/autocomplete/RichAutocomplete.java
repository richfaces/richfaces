/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
 *******************************************************************************/
package org.richfaces.ui.autocomplete;

import static org.jboss.arquillian.graphene.Graphene.waitAjax;

import java.util.Arrays;
import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichAutocomplete {

    @Drone
    private WebDriver driver;

    @ArquillianResource
    private Actions actions;

    @Root
    private WebElement root;

    @FindBy(css = ".ui-autocomplete-input")
    private WebElement input;

    @FindBy(xpath = "//body")
    private WebElement body;

    @FindByJQuery("body ul.ui-autocomplete.ui-menu:visible")
    private Menu menu;


    public static class Menu {

        @Root
        private WebElement root;

        @FindBy(css = ".ui-menu-item")
        private List<WebElement> suggestions;

        public WebElement root() {
            return root;
        }

        public List<WebElement> getSuggestions() {
            try {
                root.isDisplayed();
                return suggestions;
            } catch (NoSuchElementException e) {
                return Arrays.asList();
            }

        }
    }

    public List<WebElement> getSuggestions() {
        return menu.getSuggestions();
    }

    public WebElement getInput() {
        return input;
    }

    public RichAutocomplete type(String str) {
        input.sendKeys(str);
        waitForSuggestionsToShow();
        return this;
    }

    public RichAutocomplete selectFirst() {
        menu.getSuggestions().get(0).click();
        waitForSuggestionsToHide();
        return this;
    }

    public void waitForSuggestionsToShow() {
        waitAjax().until().element(menu.root()).is().present();
    }

    public void waitForSuggestionsToHide() {
        waitAjax().until().element(menu.root()).is().not().present();
    }
}
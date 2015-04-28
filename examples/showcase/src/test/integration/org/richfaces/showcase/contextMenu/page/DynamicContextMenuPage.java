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
package org.richfaces.showcase.contextMenu.page;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.fragment.contextMenu.RichFacesContextMenu;

/**
 * Showcase` dynamic context menu tab.
 * @author pan
 *
 */
public class DynamicContextMenuPage {

    @FindBy(css = ".rf-edt-b > div > table > tbody > tr > td:nth-of-type(2) tr")
    private List<WebElement> prices;

    @FindBy(css = "form[name='form']")
    private RichFacesContextMenu contextMenu;

    @FindBy(css = "#popupContent tr:nth-of-type(3) input")
    private WebElement priceFromPopup;
    
    @FindBy(css = "#popupContent tr:nth-of-type(5) input")
    private WebElement vinFromPopup;

    @FindBy(css = "input[type='button']")
    private WebElement closeButton;

    private static final String CLASS_OF_SELECTED_ROW = "rf-edt-r-act";

    public ExpectedCondition<Boolean> getWaitConditionOnSelectingRow(final WebElement row) {
        return new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver arg0) {
                return row.getAttribute("class").contains(CLASS_OF_SELECTED_ROW);
            }
        };

    }

    public void closePopup() {
        closeButton.click();

        Graphene.waitGui().withTimeout(2, TimeUnit.SECONDS).withMessage("The popup was not closed in a given timeout!")
            .until().element(closeButton).is().not().visible();
    }

    public List<WebElement> getPrices() {
        return prices;
    }

    public void setPrices(List<WebElement> prices) {
        this.prices = prices;
    }

    public RichFacesContextMenu getContextMenu() {
        return contextMenu;
    }

    public WebElement getPriceFromPopup() {
        return priceFromPopup;
    }

    public void setPriceFromPopup(WebElement priceFromPopup) {
        this.priceFromPopup = priceFromPopup;
    }
    
    public WebElement getVinFromPopup() {
        return vinFromPopup;
    }

    public void setVinFromPopup(WebElement vinFromPopup) {
        this.vinFromPopup = vinFromPopup;
    }

    public WebElement getCloseButton() {
        return closeButton;
    }

    public void setCloseButton(WebElement closeButton) {
        this.closeButton = closeButton;
    }
}

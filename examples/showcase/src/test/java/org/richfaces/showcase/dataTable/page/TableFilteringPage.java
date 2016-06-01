/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.showcase.dataTable.page;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;

import java.util.ArrayList;
import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.richfaces.showcase.AbstractWebDriverTest;
import org.richfaces.showcase.dataTable.AbstractDataIterationWithCars;
import org.richfaces.showcase.dataTable.AbstractDataIterationWithCars.Car;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TableFilteringPage {

    @Drone
    protected WebDriver browser;

    @FindBy(css = "span.rf-msgs-sum")
    private WebElement errorMessage;
    @FindBy(tagName = "select")
    private WebElement vendorSelect;
    @FindByJQuery("input[type=text]:first")
    private WebElement mileageInput;
    @FindByJQuery("input[type=text]:last")
    private WebElement vinInput;
    @FindBy(css = "*.rf-dt-b")
    private WebElement tBody;
    @FindBy(id = "footer")
    private WebElement toBlur;

    public boolean isNothingFound() {
        try {
            tBody.findElement(ByJQuery.selector("tr > td:contains('Nothing found')"));
            return true;
        } catch (NoSuchElementException ignored) {
            return false;
        }
    }

    public void filter(AbstractDataIterationWithCars.Field field, String value) {
        filter(field, value, true);
    }

    public void filter(AbstractDataIterationWithCars.Field field, String value, boolean valid) {
        switch (field) {
            case MILEAGE:
                filterTextInput(mileageInput, value, valid);
                break;
            case VIN:
                filterTextInput(vinInput, value, valid);
                break;
            case VENDOR:
                filterSelect(vendorSelect, value);
                break;
            default:
                throw new UnsupportedOperationException(field.name());
        }
    }

    public List<Car> getCars() {
        List<Car> result = new ArrayList<Car>();
        for (WebElement row : tBody.findElements(By.tagName("tr"))) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            result.add(new Car(cells.get(0).getText(), cells.get(1).getText(), cells.get(2).getText(), cells.get(3).getText(),
                cells.get(4).getText(), null));
        }
        return result;
    }

    protected void filterSelect(WebElement input, String value) {
        Graphene.guardAjax(new Select(input)).selectByVisibleText(value);
    }

    protected void filterTextInput(WebElement input, String value, boolean valid) {
        input.click();
        input.clear();
        input.sendKeys(value);

        // blur the actual input
        guardAjax((input == vinInput ? mileageInput : vinInput)).click();
    }

    protected Action fireEventAction(final WebElement element, final String event) {
        return new AbstractWebDriverTest.EventAction(browser, element, event);
    }

    public WebDriver getBrowser() {
        return browser;
    }

    public void setBrowser(WebDriver browser) {
        this.browser = browser;
    }

    public WebElement getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(WebElement errorMessage) {
        this.errorMessage = errorMessage;
    }

    public WebElement getVendorSelect() {
        return vendorSelect;
    }

    public void setVendorSelect(WebElement vendorSelect) {
        this.vendorSelect = vendorSelect;
    }

    public WebElement getMileageInput() {
        return mileageInput;
    }

    public void setMileageInput(WebElement mileageInput) {
        this.mileageInput = mileageInput;
    }

    public WebElement getVinInput() {
        return vinInput;
    }

    public void setVinInput(WebElement vinInput) {
        this.vinInput = vinInput;
    }

    public WebElement gettBody() {
        return tBody;
    }

    public void settBody(WebElement tBody) {
        this.tBody = tBody;
    }

    public WebElement getToBlur() {
        return toBlur;
    }

    public void setToBlur(WebElement toBlur) {
        this.toBlur = toBlur;
    }

}

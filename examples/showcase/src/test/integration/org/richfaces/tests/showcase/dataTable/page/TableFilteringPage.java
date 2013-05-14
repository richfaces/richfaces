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
package org.richfaces.tests.showcase.dataTable.page;

import java.util.ArrayList;
import java.util.List;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.enricher.findby.ByJQuery;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.support.ui.Select;
import org.richfaces.tests.showcase.AbstractWebDriverTest;
import org.richfaces.tests.showcase.dataTable.AbstractDataIterationWithCars;
import org.richfaces.tests.showcase.dataTable.AbstractDataIterationWithCars.Car;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TableFilteringPage {

    @Drone
    protected WebDriver browser;

    @FindBy(css="span.rf-msgs-sum")
    public WebElement errorMessage;

    @FindBy(tagName="select")
    private WebElement vendorSelect;
    @FindBy(jquery="input[type=text]:first")
    private WebElement mileageInput;
    @FindBy(jquery="input[type=text]:last")
    private WebElement vinInput;
    @FindBy(css="*.rf-dt-b")
    private WebElement tBody;

    @FindBy(id="footer")
    private WebElement toBlur;

    public boolean isNothingFound() {
        try {
            tBody.findElement(ByJQuery.jquerySelector("tr > td:contains('Nothing found')"));
            return true;
        } catch(NoSuchElementException ignored) {
            return false;
        }
    }

    public void filter(AbstractDataIterationWithCars.Field field, String value) {
        filter(field, value, true);
    }

    public void filter(AbstractDataIterationWithCars.Field field, String value, boolean valid) {
        switch(field) {
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
        for (WebElement row: tBody.findElements(By.tagName("tr"))) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            result.add(new Car(cells.get(0).getText(), cells.get(1).getText(), cells.get(2).getText(), cells.get(3).getText(), cells.get(4).getText(), null));
        }
        return result;
    }

    protected void filterSelect(WebElement input, String value) {
        Graphene.guardXhr(new Select(input)).selectByVisibleText(value);
    }

    protected void filterTextInput(WebElement input, String value, boolean valid) {
        input.click();
        input.clear();
        input.sendKeys(value);
        Action blur = valid ? Graphene.guardXhr(fireEventAction(input, "blur")) : fireEventAction(input, "blur");
        blur.perform();
    }

   protected Action fireEventAction(final WebElement element, final String event) {
        return new AbstractWebDriverTest.EventAction(browser, element, event);
    }
}

/*******************************************************************************
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
 *******************************************************************************/
package org.richfaces.showcase.extendedDataTable.page;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.showcase.dataTable.AbstractDataIterationWithCars;
import org.richfaces.showcase.dataTable.AbstractDataIterationWithCars.Field;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class EDTSortingPage {

    @Drone
    private WebDriver browser;

    @FindBy(css = "input[type=checkbox]")
    private WebElement multipleSorting;

    @FindByJQuery("*.rf-edt-b td div *.rf-edt-tbl")
    private List<WebElement> tableParts;

    public List<AbstractDataIterationWithCars.Car> getCars() {
        List<AbstractDataIterationWithCars.Car> result = new ArrayList<AbstractDataIterationWithCars.Car>();
        List<String> vendor = new ArrayList<String>();
        List<String> model = new ArrayList<String>();
        List<String> price = new ArrayList<String>();
        List<String> mileage = new ArrayList<String>();
        List<String> vin = new ArrayList<String>();
        for (WebElement row : tableParts.get(0).findElements(By.tagName("tr"))) {
            List<WebElement> cells = row.findElements(By.cssSelector("td > div > div"));
            vendor.add(cells.get(0).getText());
            model.add(cells.get(1).getText());
        }
        for (WebElement row : tableParts.get(1).findElements(By.tagName("tr"))) {
            List<WebElement> cells = row.findElements(By.cssSelector("td > div > div"));
            price.add(cells.get(0).getText());
            mileage.add(cells.get(1).getText());
            vin.add(cells.get(2).getText());
        }
        for (int i = 0; i < vendor.size(); i++) {
            result.add(new AbstractDataIterationWithCars.Car(vendor.get(i), model.get(i), price.get(i), mileage.get(i), vin.get(i), null));
        }
        return result;
    }

    public void setMultipleSorting(boolean value) {
        if (multipleSorting.isSelected() != value) {
            Graphene.guardAjax(multipleSorting).click();
        }
    }

    public void sort(Field field) {
        switch (field) {
            case VENDOR:
            case MODEL:
            case PRICE:
            case MILEAGE:
            case VIN:
                WebElement toggler = browser.findElement(ByJQuery.selector("a:contains(" + WordUtils.capitalize(field.name().toLowerCase()) + ")"));
                Graphene.guardAjax(toggler).click();
                break;
            default:
                throw new UnsupportedOperationException(field.name());

        }
    }

    public void resetSorting() {
    }

}

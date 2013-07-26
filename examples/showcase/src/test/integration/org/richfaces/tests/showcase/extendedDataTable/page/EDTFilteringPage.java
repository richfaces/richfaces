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
package org.richfaces.tests.showcase.extendedDataTable.page;

import java.util.ArrayList;
import java.util.List;
import org.jboss.arquillian.graphene.enricher.findby.ByJQuery;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.showcase.dataTable.AbstractDataIterationWithCars.Car;
import org.richfaces.tests.showcase.dataTable.page.TableFilteringPage;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class EDTFilteringPage extends TableFilteringPage {

    @FindBy(css="*.rf-edt-b")
    public WebElement tBody;

    @FindBy(jquery="*.rf-edt-b td div *.rf-edt-tbl")
    private List<WebElement> tableParts;


    @Override
    public boolean isNothingFound() {
        try {
            browser.findElement(ByJQuery.jquerySelector("div.rf-edt-ndt:contains('Nothing found')"));
            return true;
        } catch(NoSuchElementException ignored) {
            return false;
        }
    }

    @Override
    public List<Car> getCars() {
        List<Car> result = new ArrayList<Car>();
        List<String> vendor = new ArrayList<String>();
        List<String> model = new ArrayList<String>();
        List<String> mileage = new ArrayList<String>();
        List<String> vin = new ArrayList<String>();
        for (WebElement row: tableParts.get(0).findElements(By.tagName("tr"))) {
            List<WebElement> cells = row.findElements(By.cssSelector("td > div > div"));
            vendor.add(cells.get(0).getText());
            model.add(cells.get(1).getText());
        }
        for (WebElement row: tableParts.get(1).findElements(By.tagName("tr"))) {
            List<WebElement> cells = row.findElements(By.cssSelector("td > div > div"));
            mileage.add(cells.get(0).getText());
            vin.add(cells.get(1).getText());
        }
        for (int i=0; i < vendor.size(); i++) {
            result.add(new Car(vendor.get(i), model.get(i), null, mileage.get(i), vin.get(i), null));
        }
        return result;
    }

}

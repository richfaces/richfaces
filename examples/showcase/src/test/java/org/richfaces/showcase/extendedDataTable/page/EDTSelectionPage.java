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
package org.richfaces.showcase.extendedDataTable.page;

import java.util.ArrayList;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.showcase.dataTable.AbstractDataIterationWithCars;
import org.richfaces.showcase.dataTable.AbstractDataIterationWithCars.Car;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class EDTSelectionPage {

    @FindByJQuery("*.rf-edt-b td div *.rf-edt-tbl")
    private List<WebElement> tableParts;

    @FindByJQuery("tbody[id$=tbf] tr")
    private List<WebElement> rows;

    @FindBy(css = "ul.rf-ulst li")
    private List<WebElement> selected;

    @FindBy(css = "input[type='radio']")
    private List<WebElement> radios;

    @ArquillianResource
    private Actions actions;

    public Selection selectionWithMouse() {
        return new MultiSelectionModeWithMouse(rows);
    }

    public Selection selectionWithKeyboard() {
        return new MultiSelectionModeWithKeyboard(tableParts, actions);
    }

    public void setSelectionMode(SelectionMode mode) {
        WebElement radio;
        switch (mode) {
            case SINGLE:
                radio = radios.get(0);
                break;
            case MULTIPLE:
                radio = radios.get(1);
                break;
            case MULTIPLE_KEYBOARD_FREE:
                radio = radios.get(2);
                break;
            default:
                throw new UnsupportedOperationException();
        }
        if (!radio.isSelected()) {
            Graphene.guardAjax(radio).click();
        }
    }

    public List<Car> getSelectedCars() {
        List<Car> result = new ArrayList<Car>();
        for (WebElement element : selected) {
            String[] content = element.getText().trim().split(" - ");
            result.add(new AbstractDataIterationWithCars.Car(content[0], content[1], content[2], null, null, null));
        }
        return result;
    }

    public Car getCar(int rowIndex) {
        List<WebElement> cells1 = tableParts.get(0).findElements(By.tagName("tr")).get(rowIndex).findElements(By.cssSelector("td > div > div"));
        List<WebElement> cells2 = tableParts.get(1).findElements(By.tagName("tr")).get(rowIndex).findElements(By.cssSelector("td > div > div"));
        return new AbstractDataIterationWithCars.Car(
            cells1.get(0).getText(),
            cells1.get(1).getText(),
            cells2.get(0).getText(),
            cells2.get(1).getText(),
            cells2.get(2).getText(),
            null);
    }

    public List<AbstractDataIterationWithCars.Car> getCars() {
        return getCars(Integer.MAX_VALUE);
    }

    public List<AbstractDataIterationWithCars.Car> getCars(int numberOfRows) {
        List<AbstractDataIterationWithCars.Car> result = new ArrayList<AbstractDataIterationWithCars.Car>();
        List<String> vendor = new ArrayList<String>();
        List<String> model = new ArrayList<String>();
        List<String> price = new ArrayList<String>();
        List<String> mileage = new ArrayList<String>();
        List<String> vin = new ArrayList<String>();
        int index = 0;
        for (WebElement row : tableParts.get(0).findElements(By.tagName("tr"))) {
            index++;
            List<WebElement> cells = row.findElements(By.cssSelector("td > div > div"));
            vendor.add(cells.get(0).getText());
            model.add(cells.get(1).getText());
            if (index >= numberOfRows) {
                break;
            }
        }
        index = 0;
        for (WebElement row : tableParts.get(1).findElements(By.tagName("tr"))) {
            index++;
            List<WebElement> cells = row.findElements(By.cssSelector("td > div > div"));
            price.add(cells.get(0).getText());
            mileage.add(cells.get(1).getText());
            vin.add(cells.get(2).getText());
            if (index >= numberOfRows) {
                break;
            }
        }
        for (int i = 0; i < vendor.size(); i++) {
            result.add(new AbstractDataIterationWithCars.Car(vendor.get(i), model.get(i), price.get(i), mileage.get(i), vin.get(i), null));
        }
        return result;
    }

    public boolean isRowHighlighted(int rowIndex) {
        return rows.get(rowIndex).getAttribute("class").contains("act");
    }

    public static enum SelectionMode {

        SINGLE, MULTIPLE, MULTIPLE_KEYBOARD_FREE;
    }

    public interface Selection {

        void select(int... rows);
    }

    private class MultiSelectionModeWithKeyboard extends MultiSelectionModeWithMouse {

        private final Actions actions;

        public MultiSelectionModeWithKeyboard(List<WebElement> rowElements, Actions actions) {
            super(rowElements);
            this.actions = actions;
        }

        @Override
        public void select(int... rows) {
            actions.keyDown(Keys.CONTROL).perform();
            try {
                super.select(rows);
            } finally {
                actions.keyUp(Keys.CONTROL).perform();
            }
        }
    }

    private static class MultiSelectionModeWithMouse implements Selection {

        private final List<WebElement> rowElements;

        public MultiSelectionModeWithMouse(List<WebElement> rowElements) {
            this.rowElements = rowElements;
        }

        @Override
        public void select(int... rows) {
            for (int index : rows) {
                Graphene.guardAjax(rowElements.get(index)).click();
            }
        }
    }

}

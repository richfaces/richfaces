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
package org.richfaces.showcase.dataTable;

import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.richfaces.showcase.AbstractWebDriverTest;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @version $Revision$
 */
public class AbstractDataIterationWithCars extends AbstractWebDriverTest {

    // help class for saving data about car
    public static class Car {

        String vendor;
        String model;
        String price;
        String mileage;
        String vin;
        String stock;

        public Car() {
        }

        /**
         * Creates car with @vendor and @model fields set. Other fields are null. Equals to new Car(vendor, model, null, null,
         * null, null).
         * 
         * @param vendor
         * @param model
         */
        public Car(String vendor, String model) {
            this(vendor, model, null, null, null, null);
        }

        public Car(String vendor, String model, String price, String mileage, String vin, String stock) {
            this.vendor = vendor;
            this.model = model;
            this.price = price;
            this.mileage = mileage;
            this.vin = vin;
            this.stock = stock;
        }

        private void controlWhetherParameterIsNotEmptyOrNull(String parameter) {

            if (parameter == null || parameter.isEmpty()) {

                throw new IllegalArgumentException("Parameter can not be empty or null!");
            }
        }

        public String getStock() {

            return this.stock;
        }

        public Car setStock(String stock) {

            controlWhetherParameterIsNotEmptyOrNull(stock);
            this.stock = stock;
            return this;
        }

        public String getVendor() {
            return vendor;
        }

        public Car setVendor(String vendor) {

            controlWhetherParameterIsNotEmptyOrNull(vendor);
            this.vendor = vendor;
            return this;
        }

        public String getModel() {
            return model;
        }

        public Car setModel(String model) {

            controlWhetherParameterIsNotEmptyOrNull(model);
            this.model = model;
            return this;
        }

        public String getPrice() {
            return price;
        }

        public Car setPrice(String price) {

            controlWhetherParameterIsNotEmptyOrNull(price);
            this.price = price;
            return this;
        }

        public String getMileage() {
            return mileage;
        }

        public Car setMileage(String mileage) {

            controlWhetherParameterIsNotEmptyOrNull(mileage);
            this.mileage = mileage;
            return this;
        }

        public String getVin() {
            return vin;
        }

        public Car setVin(String vin) {

            controlWhetherParameterIsNotEmptyOrNull(vin);
            this.vin = vin;
            return this;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result;
            result = prime * result + ((mileage == null) ? 0 : mileage.hashCode());
            result = prime * result + ((model == null) ? 0 : model.hashCode());
            result = prime * result + ((price == null) ? 0 : price.hashCode());
            result = prime * result + ((vendor == null) ? 0 : vendor.hashCode());
            result = prime * result + ((vin == null) ? 0 : vin.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Car other = (Car) obj;
            if (mileage == null) {
                if (other.mileage != null)
                    return false;
            } else if (!mileage.equals(other.mileage))
                return false;
            if (model == null) {
                if (other.model != null)
                    return false;
            } else if (!model.equals(other.model))
                return false;
            if (price == null) {
                if (other.price != null)
                    return false;
            } else if (!price.equals(other.price))
                return false;
            if (vendor == null) {
                if (other.vendor != null)
                    return false;
            } else if (!vendor.equals(other.vendor))
                return false;
            if (vin == null) {
                if (other.vin != null)
                    return false;
            } else if (!vin.equals(other.vin))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "Car [vendor=" + vendor + ", model=" + model + ", price=" + price + ", mileage=" + mileage + ", vin=" + vin
                + "]";
        }
    }

    /**
     * retrieves info about car from the row, it starts collecting data on the row specified by startingIndexTd and ends on the
     * endIndexOfTd, other values from car lets uninitialized
     * 
     * @param row
     * @param startingIndexOfTd
     * @param endIndexOfTd
     * @return car
     */
    public Car retrieveCarFromRow(WebElement row, int startingIndexOfTd, int endIndexOfTd) {

        Car car = new Car();

        List<WebElement> tds = row.findElements(By.tagName("td"));

        int j = 0;
        for (Iterator<WebElement> i = tds.iterator(); i.hasNext();) {

            if (j < startingIndexOfTd) {
                i.next();
            }

            if (j == startingIndexOfTd) {
                car.setVendor(i.next().getText());
            }

            else if (j == (startingIndexOfTd + 1)) {
                car.setModel(i.next().getText());
            }

            else if (j == (startingIndexOfTd + 2)) {
                car.setPrice(i.next().getText());
            }

            else if (j == (startingIndexOfTd + 3)) {
                car.setMileage(i.next().getText());
            }

            else if (j == (startingIndexOfTd + 4)) {
                car.setVin(i.next().getText());
            }

            else if (j > endIndexOfTd) {
                break;
            }

            j++;
        }

        return car;
    }

    public Car parseSimplifiedCarFromListItem(WebElement listItem) {
        String[] partsOfContent = listItem.getText().split("-");
        return new Car(partsOfContent[0].trim(), partsOfContent[1].trim());
    }

    public static enum Field {
        VENDOR,
        MODEL,
        PRICE,
        MILEAGE,
        VIN,
        STOCK;
    }
}

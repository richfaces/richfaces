/**
 * JBoss, Home of Professional Open Source Copyright 2012, Red Hat, Inc. and
 * individual contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */
package org.richfaces.tests.showcase.extendedDataTable;

import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.StringUtils;
import org.jboss.arquillian.ajocado.format.SimplifiedFormat;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.junit.Test;
import org.richfaces.tests.showcase.extendedDataTable.page.EDTSortingPage;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class ITEDTSorting extends AbstractExtendedTableTest {

    @Page
    private EDTSortingPage page;

    @Override
    protected String getAdditionToContextRoot() {
        String sampleName = "edt-sorting";

        // demo name - takes last part of package name
        String demoName = this.getClass().getPackage().getName();
        demoName = StringUtils.substringAfterLast(demoName, ".");

        String addition = SimplifiedFormat.format("richfaces/component-sample.jsf?skin=blueSky&demo={0}&sample={1}", demoName,
            sampleName);

        return addition;
    }

    @Test
    public void testSingleSortingModel() {
        // first reset the sorting
        page.resetSorting();
        // sort by model
        page.sort(Field.MODEL);
        // check the price values
        Car previous = null;
        for (Car car : page.getCars()) {
            if (previous != null) {
                assertTrue("The model in previous row should be lesser or equal to the model in next row.", previous.getModel()
                    .compareTo(car.getModel()) <= 0);
            }
            previous = car;
        }
        // sort by model again (reverse order)
        page.sort(Field.MODEL);
        for (Car car : page.getCars()) {
            if (previous != null) {
                assertTrue("The model in previous row <" + previous.getModel()
                    + "> should be greater or equal to the model in next row <" + car.getModel() + ">.", previous.getModel()
                    .compareTo(car.getModel()) >= 0);
            }
            previous = car;
        }
    }

    @Test
    public void testSingleSortingPrice() {
        // first reset the sorting
        page.resetSorting();
        // sort by price
        page.sort(Field.PRICE);
        // check the price values
        Car previous = null;
        for (Car car : page.getCars()) {
            if (previous != null) {
                assertTrue("The price in previous row should be lesser or equal to the price in next row.",
                    Double.valueOf(previous.getPrice()) <= Double.valueOf(car.getPrice()));
            }
            previous = car;
        }
        // sort by price again (reverse order)
        page.sort(Field.PRICE);
        for (Car car : page.getCars()) {
            if (previous != null) {
                assertTrue("The price in previous row should be greater or equal to the price in next row.",
                    Double.valueOf(previous.getPrice()) >= Double.valueOf(car.getPrice()));
            }
            previous = car;
        }
    }

    /**
     * Tests EDT's multiple sorting by model and by price
     */
    @Test
    public void testMultipleSorting() {
        // set multiple sorting
        page.setMultipleSorting(true);
        // sort by model and price
        page.sort(Field.MODEL);
        page.sort(Field.PRICE);
        // check if table is sorted by model and price
        Car previous = null;
        for (Car car : page.getCars()) {
            if (previous != null) {
                int modelsCompared = previous.getModel().compareTo(car.getModel());
                assertTrue("The model in previous row should be lesser or equal to the model in next row.", previous.getModel()
                    .compareTo(car.getModel()) <= 0);
                if (modelsCompared == 0) {
                    assertTrue("The price in previous row should be lesser or equal to the price in next row.",
                        Double.valueOf(previous.getPrice()) <= Double.valueOf(car.getPrice()));
                }
            }
            previous = car;
        }
    }
}

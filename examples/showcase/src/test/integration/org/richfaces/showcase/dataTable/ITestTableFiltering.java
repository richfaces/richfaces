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
package org.richfaces.showcase.dataTable;

import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.richfaces.showcase.dataTable.page.TableFilteringPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @version $Revision$
 */
public class ITestTableFiltering extends AbstractDataIterationWithCars {

    @Page
    protected TableFilteringPage page;

    /*
     * Constants *****************************************************************
     * ***********************************************
     */

    private final String MILEAGE_FILTER = "40000";
    private final String VENDOR_FILTER = "Chevrolet";
    private final String VIN_FILTER = "A";
    private final String ERROR_MESSAGE = "is not a number";

    /*
     * Tests
     * 
     * tests conditions are depending on some non deterministic conditions, when are not satisfied the fail() is called
     * ************************************ ************************************** *****************************************
     */

    @Test
    public void testVendorFilter() {
        getPage().filter(Field.VENDOR, VENDOR_FILTER);
        assertTrue("There should be only rows which contains only " + VENDOR_FILTER,
            checkAllCarsAreCorrect(getPage().getCars(), Field.VENDOR, VENDOR_FILTER));
    }

    @Test
    public void testMileageFilter() {
        getPage().filter(Field.MILEAGE, MILEAGE_FILTER);
        assertTrue("There should be only rows with mileage less or equal to " + MILEAGE_FILTER,
            checkAllCarsAreCorrect(getPage().getCars(), Field.MILEAGE, MILEAGE_FILTER));
    }

    @Test
    public void testVinFilter() {
        getPage().filter(Field.VIN, VIN_FILTER);
        assertTrue("all vins of the car should contain " + VIN_FILTER,
            checkAllCarsAreCorrect(getPage().getCars(), Field.VIN, VIN_FILTER));
    }

    @Test
    public void testErrorMessage() {
        getPage().filter(Field.MILEAGE, "foo", false);

        Graphene.waitModel().until().element(getPage().getErrorMessage()).is().present();

        String actualErrorMessage = getPage().getErrorMessage().getText();
        assertTrue("There should be error message, since there is string in the mileage input!",
            actualErrorMessage.contains(ERROR_MESSAGE));
    }

    @Test
    public void testNoRowsSatisfyConditions() {
        getPage().filter(Field.MILEAGE, "1");
        assertTrue("No rows should satisfy the filter conditions", getPage().isNothingFound());
    }

    /* ********************************************************************************************************************
     * help methods **************************************************************
     * *****************************************************
     */

    protected boolean checkAllCarsAreCorrect(Collection<Car> cars, Field field, String filterValue) {
        for (Car car : cars) {
            switch (field) {
                case MILEAGE:
                    if (Double.valueOf(car.getMileage()) > Double.valueOf(filterValue)) {
                        return false;
                    }
                    break;
                case VENDOR:
                    if (!car.getVendor().equals(filterValue)) {
                        return false;
                    }
                    break;
                case VIN:
                    if (!car.getVin().contains(filterValue)) {
                        return false;
                    }
                    break;
                default:
                    throw new UnsupportedOperationException("Can't check " + field.name() + " field.");
            }
        }
        return true;
    }

    protected TableFilteringPage getPage() {
        return page;
    }

}

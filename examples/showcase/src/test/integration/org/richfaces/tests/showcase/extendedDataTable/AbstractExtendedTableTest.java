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
package org.richfaces.tests.showcase.extendedDataTable;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.showcase.dataTable.AbstractDataIterationWithCars;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
@Ignore
public class AbstractExtendedTableTest extends AbstractDataIterationWithCars {

    /**
     * Checks whether there is correct car in the row, according to given vendor and model strings
     * 
     * @param vendor
     * @param model
     * @param row
     */
    protected void checkTheRow(String vendor, String model, WebElement row) {

        Car expectedCar = new Car();
        expectedCar.setVendor(vendor);
        expectedCar.setModel(model);

        Car actualCar = retrieveCarFromRow(row, 0, 1);

        assertEquals("The car in the row should be different!", expectedCar, actualCar);
    }
}

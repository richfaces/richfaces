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
package org.richfaces.tests.showcase.dataGrid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.showcase.dataGrid.page.GridPage;
import org.richfaces.tests.showcase.dataTable.AbstractDataIterationWithCars;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @version $Revision$
 */
public class ITGrid extends AbstractDataIterationWithCars {

    /* *****************************************************************************************************
     * Locators*****************************************************************************************************
     */
    @Page
    private GridPage page;

    /* ********************************************************************************************************
     * Tests********************************************************************************************************
     */

    @Test
    public void testDataGridIsNotEmpty() {

        @SuppressWarnings("unused")
        Car pageFirstCar = retrieveCarFromThePanelGrid(page.firstPanelGridOnThePage);
        @SuppressWarnings("unused")
        Car pageLastCar = retrieveCarFromThePanelGrid(page.lastPanelGridOnThePage);

        Graphene.guardAjax(page.lastPageButton).click();

        pageFirstCar = retrieveCarFromThePanelGrid(page.firstPanelGridOnThePage);
        pageLastCar = retrieveCarFromThePanelGrid(page.lastPanelGridOnThePage);
    }

    @Test
    public void testDataGridIsScrollable() {

        int numberOfThePageBeforeClick;
        Car carBeforeClicking;

        try {
            page.firstPageButton.isDisplayed();
            Graphene.guardAjax(page.firstPageButton).click();

            numberOfThePageBeforeClick = page.getNumberOfCurrentPage();
            carBeforeClicking = retrieveCarFromThePanelGrid(page.firstPanelGridOnThePage);

            Graphene.guardAjax(page.nextButton).click();
        } catch (NoSuchElementException ignored) {
            numberOfThePageBeforeClick = page.getNumberOfCurrentPage();
            carBeforeClicking = retrieveCarFromThePanelGrid(page.firstPanelGridOnThePage);

            Graphene.guardAjax(page.nextButton).click();
        }

        int numberOfThePageAfterClick = page.getNumberOfCurrentPage();
        Car carAfterClicking = retrieveCarFromThePanelGrid(page.firstPanelGridOnThePage);

        assertEquals( "The next button does not works!", numberOfThePageAfterClick - 1,numberOfThePageBeforeClick);

        assertFalse("Data should be different on the different pages!", carBeforeClicking.equals(carAfterClicking));
    }

    /* *********************************************************************************************************
     * Help methods **********************************************************************************************************
     */
    private Car retrieveCarFromThePanelGrid(WebElement panelGrid) {

        Car car = new Car();

        String panelGridText = panelGrid.getText();

        String[] partsOfPanelGrid = panelGridText.split("[ \n]");

        try {

            car.setVendor(partsOfPanelGrid[0].trim());
            car.setModel(partsOfPanelGrid[1].trim());
            car.setPrice(partsOfPanelGrid[3].trim());
            car.setMileage(partsOfPanelGrid[5].trim());
            car.setVin(partsOfPanelGrid[7].trim());
            car.setStock(partsOfPanelGrid[9]);

        } catch (IllegalArgumentException ex) {

            fail("The table should not be empty");
        }

        return car;
    }

}

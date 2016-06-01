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
package org.richfaces.showcase.dataScroller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.richfaces.showcase.dataScroller.page.SimpleScrollingPage;
import org.richfaces.showcase.dataTable.AbstractDataIterationWithCars;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class ITestSimpleScrolling extends AbstractDataIterationWithCars {

    @Page
    private SimpleScrollingPage page;

    @Test
    public void testFirstPageButton() {
        // starting from the first page
        // click on the last page
        Graphene.guardAjax(page.getLastPageButton()).click();
        Car carBeforeClick = retrieveCarFromRow(page.getFirstRowOfTable(), 0, 4);
        // click on the first page
        Graphene.guardAjax(page.getFirstPageButton()).click();
        Car carAfterClick = retrieveCarFromRow(page.getFirstRowOfTable(), 0, 4);

        // check
        assertFalse("The data from table should be different, " + "when clicking on the on the the first page button",
            carBeforeClick.equals(carAfterClick));
        assertTrue("The first page button should be disabled", isElementPresent(page.getFirstPageButtonDis()));
        assertTrue("The fast previous page button should be disabled", isElementPresent(page.getFastPrevButtonDis()));
        assertTrue("The previous button should be disabled", isElementPresent(page.getPreviousButtonDis()));
    }

    @Test
    public void testLastPageButton() {
        // starting from the last page
        Graphene.guardAjax(page.getLastPageButton()).click();
        // click on the first page
        Graphene.guardAjax(page.getFirstPageButton()).click();
        Car carBeforeClick = retrieveCarFromRow(page.getFirstRowOfTable(), 0, 4);
        // click on the last page
        Graphene.guardAjax(page.getLastPageButton()).click();
        Car carAfterClick = retrieveCarFromRow(page.getFirstRowOfTable(), 0, 4);

        // check
        assertFalse("The data from table should be different, " + "when clicking on the on the the last page button",
            carBeforeClick.equals(carAfterClick));
        assertTrue("The last page button should be disabled", isElementPresent(page.getLastPageButtonDis()));
        assertTrue("The fast next page button should be disabled", isElementPresent(page.getFastNextButtonDis()));
        assertTrue("The next button should be disabled", isElementPresent(page.getNextButtonDis()));
    }

    @Test
    public void testPreviousAndNextPageButton() {
        testFastAndNormalButtons(page.getNextButton(), page.getPreviousButton(), false, 1);
    }

    // in this demo the fasts button have the same functionality as the next/previous buttons
    @Test
    public void testPreviousAndNextPageButtonFast() {
        testFastAndNormalButtons(page.getFastNextButton(), page.getFastPrevButton(), true, 3);
    }

    @Test
    public void testNumberOfPagesButtons() {
        try {
            page.getFirstPageButtonDis().isDisplayed();
        } catch (NoSuchElementException ignored) {
            Graphene.guardAjax(page.getFirstPageButton()).click();
        }
        checkNumberOfPagesButtons(3);
        checkNumberOfPagesButtons(5);
        checkNumberOfPagesButtons(6);
        checkNumberOfPagesButtons(4);
    }

    /**
     * Checking the buttons which have number of pages
     */
    private void checkNumberOfPagesButtons(int numberOfPage) {
        WebElement checkingButton = webDriver.findElement(ByJQuery.selector("a[class*='"
            + page.CLASS_OF_INACTIVE_BUTTON_WITH_NUMBER + "']:contains('" + numberOfPage + "'):first"));
        Car carBeforeClicking = retrieveCarFromRow(page.getFirstRowOfTable(), 0, 4);
        Graphene.guardAjax(checkingButton).click();

        Car carAfterClicking = retrieveCarFromRow(page.getFirstRowOfTable(), 0, 4);
        assertFalse("The data should be different on the different pages!", carBeforeClicking.equals(carAfterClicking));
        int actualCurrentNumberOfPage = page.getNumberOfCurrentPage();
        assertEquals("We should be on the " + numberOfPage + ". page", numberOfPage, actualCurrentNumberOfPage);
    }

    /**
     * Tests fast and normal buttons
     *
     * @param nextButton
     * @param previousButton
     * @param fast indicated whether it is fast step button or not
     * @param step indicated how many pages are switched at a time
     */
    private void testFastAndNormalButtons(WebElement nextButton, WebElement previousButton, boolean fast, int step) {
        // starting on the first page
        int numberOfPageAtBeginning = page.getNumberOfCurrentPage();
        Car carBeforeClick = retrieveCarFromRow(page.getFirstRowOfTable(), 0, 4);
        // click on the next button
        Graphene.guardAjax(nextButton).click();
        Car carAfterClick = retrieveCarFromRow(page.getFirstRowOfTable(), 0, 4);
        // check
        // -- car
        assertNotEquals("The data from table should be different, " + "when clicking on the on the " + (fast ? "fast " : "")
            + "next button", carBeforeClick, carAfterClick);
        // -- page
        int numberOfPageAfterClickOnNext = page.getNumberOfCurrentPage();
        assertEquals("Previous button or " + (fast ? "fast " : "") + " previous button does not work",
            numberOfPageAfterClickOnNext, numberOfPageAtBeginning + step);
        // click on the previous button
        Graphene.guardAjax(previousButton).click();
        // check
        // -- car
        carAfterClick = retrieveCarFromRow(page.getFirstRowOfTable(), 0, 4);
        assertEquals("The data from table should be the same as in the beginning, " + "when clicking on the on "
            + (fast ? "fast " : "") + " the previous", carAfterClick, carBeforeClick);
        // -- page
        int currentNumberOfPage = page.getNumberOfCurrentPage();
        assertEquals("The " + (fast ? "fast " : "") + " previous button does not work", numberOfPageAtBeginning,
            currentNumberOfPage);
    }
}

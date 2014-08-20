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
package org.richfaces.showcase.subTableToggleControl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.richfaces.showcase.dataTable.AbstractDataIterationWithCars;
import org.richfaces.showcase.subTableToggleControl.page.SubTableToggleControlPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class ITestSubTableToggleControl extends AbstractDataIterationWithCars {

    @Page
    private SubTableToggleControlPage page;

    /* *****************************************************************************************************************
     * Tests *****************************************************************************************************************
     */

    @Test
    public void testTogglers() {

        checkTogglersFunctionality(page.getChevroletToggler(), page.getBodyOfChevroletSubtable());

        checkTogglersFunctionality(page.getgMCToggler(), page.getBodyOfGMCSubtable());

        checkTogglersFunctionality(page.getNissanToggler(), page.getBodyOfNissanSubtable());
    }

    @Test
    public void testTotalCountOfCars() {

        checksCountingOfRows(page.getChevroletToggler(), page.getBodyOfChevroletSubtable());

        checksCountingOfRows(page.getgMCToggler(), page.getBodyOfGMCSubtable());

        checksCountingOfRows(page.getNissanToggler(), page.getBodyOfNissanSubtable());
    }

    /* *******************************************************************************************************************
     * Help methods ******************************************************************************************************
     * *************
     */

    /**
     * Checks whether there is more than 0 rows with some data, also checks whether the counting of rows works
     * 
     * @param toggler the toggler where user click in order to hide/show subtable
     * @param bodyOfTable the body of table
     */
    private void checksCountingOfRows(WebElement toggler, WebElement bodyOfTable) {

        toggler.click();

        String style = bodyOfTable.getAttribute("style");

        if (style.contains("display: none;")) {

            toggler.click();
        }

        List<WebElement> trs = bodyOfTable.findElements(By.tagName("tr"));

        int numberOfVisibleTrs = 0;

        for (WebElement element : trs) {

            if (element.isDisplayed()) {

                numberOfVisibleTrs++;
            }
        }

        String expectedNumberOfTrs = bodyOfTable.findElement(ByJQuery.selector("tr:visible:last")).getText().trim();

        String[] partsOfLine = expectedNumberOfTrs.split(":");
        int expectedNumberOfTrsInt = Integer.valueOf(partsOfLine[1].trim()).intValue();

        assertTrue("There should be some rows!", expectedNumberOfTrsInt > 0);

        // there is -1 since there is one row with information about total number
        assertEquals("The information about total rows is incorrect!", numberOfVisibleTrs - 1, expectedNumberOfTrsInt);
    }

    /**
     * Checks whether the subtable is displayed when it should be and otherwise
     * 
     * @param toggler the toggler on which user click the subtable is displayed/hidden
     */
    private void checkTogglersFunctionality(WebElement toggler, WebElement subtable) {

        toggler.click();

        String style = subtable.getAttribute("style");

        boolean isSubTableDisplayedAfterFirstClick = !(style.contains("display: none;"));

        toggler.click();

        style = subtable.getAttribute("style");

        boolean isSubTableDisplayedAfterSecondClick = !(style.contains("display: none;"));

        assertEquals("The subtable should be displayed, or hidden, "
            + "it depends on whether the subtable was diplayed before clicking on toggler or not!",
            !isSubTableDisplayedAfterSecondClick, isSubTableDisplayedAfterFirstClick);
    }

}

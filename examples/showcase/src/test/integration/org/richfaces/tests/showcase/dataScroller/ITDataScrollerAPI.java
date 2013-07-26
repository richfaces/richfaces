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
package org.richfaces.tests.showcase.dataScroller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.enricher.findby.ByJQuery;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.showcase.AbstractWebDriverTest;
import org.richfaces.tests.showcase.dataScroller.page.DataScrollerAPIPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class ITDataScrollerAPI extends AbstractWebDriverTest {

    @Page
    private DataScrollerAPIPage page;

    /* *************************************************************************************************************************
     * Tests *************************************************************************************************************
     * *************
     */

    @Test
    public void testNumberOfPagesButtons() {

        checkNumberOfPagesButtons(1);

        checkNumberOfPagesButtons(2);

        checkNumberOfPagesButtons(3);

    }

    @Test
    public void testAPINextPrevious() {

        int currentNumberOfThePage = page.getNumberOfCurrentPage();

        if (currentNumberOfThePage > 1) {
            Graphene.guardAjax(page.previousButton).click();
            Graphene.guardAjax(page.previousButton).click();
        }

        String srcBeforeClicking = getSrcOfFirstImage();

        Graphene.guardAjax(page.nextButton).click();

        String srcAfterClicking = getSrcOfFirstImage();

        assertFalse("The data should be different on he different pages!", srcBeforeClicking.equals(srcAfterClicking));

        int numberOfThePageAfterClicking = page.getNumberOfCurrentPage();

        assertEquals("The current number of the page should be higher", currentNumberOfThePage + 1,
            numberOfThePageAfterClicking);

        Graphene.guardAjax(page.previousButton).click();

        numberOfThePageAfterClicking = page.getNumberOfCurrentPage();

        assertEquals("The current number of the page should be less", currentNumberOfThePage, numberOfThePageAfterClicking);
    }

    /* ***************************************************************************************************************************************
     * Help methods ******************************************************************************************************
     * **********************************
     */

    /**
     * Checking the buttons which have number of pages
     */
    private void checkNumberOfPagesButtons(int numberOfPage) {

        String imgSrcBeforeClick = null;

        try {
            WebElement checkingButton = webDriver.findElement(ByJQuery.jquerySelector("a[class*='"
                + page.CLASS_OF_INACTIVE_BUTTON_WITH_NUMBER + "']:contains('" + numberOfPage + "')"));
            imgSrcBeforeClick = getSrcOfFirstImage();
            Graphene.guardAjax(checkingButton).click();
        } catch (NoSuchElementException ignored) {
            WebElement inactiveButton = webDriver.findElement(ByJQuery.jquerySelector("a[class*='"
                + page.CLASS_OF_INACTIVE_BUTTON_WITH_NUMBER + "']:first"));
            imgSrcBeforeClick = getSrcOfFirstImage();
            Graphene.guardAjax(inactiveButton).click();
            numberOfPage = page.getNumberOfCurrentPage();
        }

        String imgSrcAfterClick = getSrcOfFirstImage();

        assertFalse("The data should be different on the different pages!", imgSrcAfterClick.equals(imgSrcBeforeClick));

        int actualCurrentNumberOfPage = page.getNumberOfCurrentPage();

        assertEquals("We should be on the " + numberOfPage + ". page", numberOfPage, actualCurrentNumberOfPage);
    }

    /**
     * Gets the src attribute of the first image on the page
     */
    private String getSrcOfFirstImage() {
        return page.firstImgOnThePage.getAttribute("src");
    }

}

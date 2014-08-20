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

import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.richfaces.showcase.AbstractWebDriverTest;
import org.richfaces.showcase.dataTable.page.ArrangableModelPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class ITestArrangeableModel extends AbstractWebDriverTest {

    /* *******************************************************************************************
     * Locators ****************************************************************** *************************
     */

    @Page
    private ArrangableModelPage page;

    /* *******************************************************************************************
     * Tests ********************************************************************* **********************
     */

    @Test
    public void testFirstNameFilter() {

        filterAnColumn(page.getFirstNameFilterInput(), "as", ByJQuery.selector("td:eq(0)"));
        page.getFirstNameFilterInput().click();
        page.getFirstNameFilterInput().clear();
        fireEvent(page.getFirstNameFilterInput(), "keyup");
    }

    @Test
    public void testSurnameFilter() {
        filterAnColumn(page.getSecondNameFilterInput(), "al", ByJQuery.selector("td:eq(1)"));
        page.getSecondNameFilterInput().click();
        page.getSecondNameFilterInput().clear();
        fireEvent(page.getSecondNameFilterInput(), "keyup");
    }

    @Test
    public void testEmailFilter() {
        filterAnColumn(page.getEmailFilterInput(), "ac", ByJQuery.selector("td:eq(2)"));
        page.getEmailFilterInput().click();
        page.getEmailFilterInput().clear();
        fireEvent(page.getEmailFilterInput(), "keyup");

    }

    @Test
    public void testFirstNameSorting() {

        ascendingDescendingSortingOnColumn(0, "Z");
    }

    @Test
    public void testSurnameSorting() {

        ascendingDescendingSortingOnColumn(1, "Z");
    }

    @Test
    public void testEmailSorting() {

        ascendingDescendingSortingOnColumn(2, "v");
    }

    /* **********************************************************************************************
     * Help methods ************************************************************** ********************************
     */

    private boolean doesColumnContainsOnlyRowsWithData(By column, String data) {

        List<WebElement> table = page.getTable().findElements(By.tagName("tr"));

        for (Iterator<WebElement> i = table.iterator(); i.hasNext();) {

            WebElement td = i.next().findElement(column);

            String tdText = td.getText();

            if (!tdText.toLowerCase().contains(data)) {

                return false;
            }

        }

        return true;
    }

    private void filterAnColumn(WebElement filterInput, String filterValue, By column) {

        filterInput.click();
        filterInput.clear();
        for (char ch : filterValue.toCharArray()) {
            Graphene.guardAjax(filterInput).sendKeys(Character.toString(ch));
        }

        boolean result = doesColumnContainsOnlyRowsWithData(column, filterValue);

        assertTrue("The table should contains only rows, which column " + column + " contains only data '" + filterValue + "'",
            result);
    }

    private void ascendingDescendingSortingOnColumn(int column, String firstCharOfRowWhenDescending) {

        // ascending
        Graphene.guardAjax(page.getUnsortedLink(column)).click();

        String checkedValue = page.getFirstRowSomeColumn(column).getText();

        assertTrue("Rows should be sorted in an ascending order, by column " + page.getFirstRowSomeColumn(column), String
            .valueOf(checkedValue.charAt(0)).equalsIgnoreCase("A"));

        // descending
        Graphene.guardAjax(page.getAscendingLink()).click();

        checkedValue = page.getFirstRowSomeColumn(column).getText();

        assertTrue("Rows should be sorted in an descending order, by column " + page.getFirstRowSomeColumn(column).getText(),
            String.valueOf(checkedValue.charAt(0)).equalsIgnoreCase(firstCharOfRowWhenDescending));
    }

}

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

import java.text.MessageFormat;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.showcase.AbstractWebDriverTest;
import org.richfaces.showcase.dataTable.page.ArrangableModelPage;

import com.google.common.base.Predicate;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class ITestArrangeableModel extends AbstractWebDriverTest {

    @Page
    private ArrangableModelPage page;

    @Test
    public void testFirstNameFilter() {
        filterColumnAndCheck(page.getFirstNameFilterInput(), "as", ByJQuery.selector("td:eq(0)"), 8);
    }

    @Test
    public void testSurnameFilter() {
        filterColumnAndCheck(page.getSecondNameFilterInput(), "al", ByJQuery.selector("td:eq(1)"), 11);
    }

    @Test
    public void testEmailFilter() {
        filterColumnAndCheck(page.getEmailFilterInput(), "ab", ByJQuery.selector("td:eq(2)"), 8);
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

    private boolean doesColumnContainsOnlyRowsWithData(By column, String data, final int numberOfVisibleRows) {
        final List<WebElement> rows = page.getTable().findElements(By.tagName("tr"));
        Graphene.waitAjax().until(new Predicate<WebDriver>() {
            private int lastVisibleRowsCount;

            @Override
            public boolean apply(WebDriver t) {
                return (lastVisibleRowsCount = rows.size()) == numberOfVisibleRows;
            }

            @Override
            public String toString() {
                return MessageFormat.format("table to have <{0}> visible rows. Last visible rows count was <{1}>.", numberOfVisibleRows, lastVisibleRowsCount);
            }

        });
        for (WebElement row : rows) {
            String cellText = row.findElement(column).getText();
            if (!cellText.toLowerCase().contains(data)) {
                return false;
            }
        }
        return true;
    }

    private void filterColumnAndCheck(WebElement filterInput, String filterValue, By column, int numberOfVisibleRows) {
        filterInput.click();
        filterInput.clear();
        Graphene.guardAjax(filterInput).sendKeys(filterValue);

        boolean result = doesColumnContainsOnlyRowsWithData(column, filterValue, numberOfVisibleRows);
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

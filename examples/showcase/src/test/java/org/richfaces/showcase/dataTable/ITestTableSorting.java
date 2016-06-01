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

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.richfaces.showcase.dataTable.page.TableSortingPage;
import org.richfaces.showcase.repeat.AbstractDataIterationWithStates;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class ITestTableSorting extends AbstractDataIterationWithStates {

    @Page
    private TableSortingPage page;

    private final StateWithCapitalAndTimeZone FIRST_STATE_SORTED_BY_CAPITAL_ASCENDING_ORDER = new StateWithCapitalAndTimeZone(
        "New York", "Albany", "GMT-5");
    private final StateWithCapitalAndTimeZone FIRST_STATE_SORTED_BY_CAPITAL_DESCENDING_ORDER = new StateWithCapitalAndTimeZone(
        "New Jersey", "Trenton", "GMT-5");
    private final StateWithCapitalAndTimeZone FIRST_STATE_SORTED_BY_STATE_ASCENDING_ORDER = new StateWithCapitalAndTimeZone(
        "Alabama", "Montgomery", "GMT-6");
    private final StateWithCapitalAndTimeZone FIRST_STATE_SORTED_BY_STATE_DESCENDING_ORDER = new StateWithCapitalAndTimeZone(
        "Wyoming", "Cheyenne", "GMT-7");
    private final StateWithCapitalAndTimeZone FIRST_STATE_SORTED_BY_TIME_ZONE_ASCENDING_ORDER = new StateWithCapitalAndTimeZone(
        "Hawaii", "Honolulu", "GMT-10");
    private final StateWithCapitalAndTimeZone FIRST_STATE_SORTED_BY_TIME_ZONE_DESCENDING_ORDER = new StateWithCapitalAndTimeZone(
        "Connecticut", "Hartford", "GMT-5");

    @Test
    public void testSortByCapitalName() {
        clickOnParticularSortAnchorCheckFirstRow(page.getSortByCapitalName(), FIRST_STATE_SORTED_BY_CAPITAL_ASCENDING_ORDER,
            FIRST_STATE_SORTED_BY_CAPITAL_DESCENDING_ORDER, "The table should be ordered by capital name in ascending order",
            "The table should be ordered by capital name in descending order");
    }

    @Test
    public void testSortByStateName() {
        clickOnParticularSortAnchorCheckFirstRow(page.getSortByStateName(), FIRST_STATE_SORTED_BY_STATE_ASCENDING_ORDER,
            FIRST_STATE_SORTED_BY_STATE_DESCENDING_ORDER, "The table shoould be ordered by state name in ascending order",
            "The table should be ordered by state name in descending order");
    }

    @Test
    public void testSortByTimeZone() {
        clickOnParticularSortAnchorCheckFirstRow(page.getSortByTimeZone(), FIRST_STATE_SORTED_BY_TIME_ZONE_ASCENDING_ORDER,
            FIRST_STATE_SORTED_BY_TIME_ZONE_DESCENDING_ORDER, "The table sould be ordered by time zone in ascending order",
            "The table should be ordered by time zone in descending order");
    }

    /**
     * Sorts table and checks the first row according to expected first row
     * 
     * @param sortBy
     * @param ascendingState expected state
     * @param descendingState expected state
     * @param ascendingError
     * @param descendingError
     */
    private void clickOnParticularSortAnchorCheckFirstRow(WebElement sortBy, StateWithCapitalAndTimeZone ascendingState,
        StateWithCapitalAndTimeZone descendingState, String ascendingError, String descendingError) {
        Graphene.guardAjax(sortBy).click();

        StateWithCapitalAndTimeZone actualState = initializeStateDataFromRow();
        assertEquals(ascendingError, ascendingState, actualState);

        Graphene.guardAjax(sortBy).click();
        actualState = initializeStateDataFromRow();
        assertEquals(descendingError, descendingState, actualState);
    }

    /**
     * returns new StateWithCapitalAndTimeZone, which is initialized byt the data in the partilucal row
     * 
     * @param row
     * @return
     */
    private StateWithCapitalAndTimeZone initializeStateDataFromRow() {
        List<WebElement> tds = page.getFirstRow().findElements(By.tagName("td"));
        String capitalName = null;
        String stateName = null;
        String timeZone = null;
        int i = 0;
        for (WebElement currentTd : tds) {
            switch (i) {
                case 1:
                    capitalName = currentTd.getText();
                    break;
                case 2:
                    stateName = currentTd.getText();
                    break;
                case 3:
                    timeZone = currentTd.getText();
                    break;
                default:
                    break;
            }
            i++;
        }
        return new StateWithCapitalAndTimeZone(stateName, capitalName, timeZone);
    }
}

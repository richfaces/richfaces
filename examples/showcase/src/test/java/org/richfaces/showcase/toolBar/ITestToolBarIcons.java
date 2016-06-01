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
package org.richfaces.showcase.toolBar;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.richfaces.showcase.AbstractWebDriverTest;
import org.richfaces.showcase.toolBar.page.ToolbarIconsPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class ITestToolBarIcons extends AbstractWebDriverTest {

    private static final int NUMBER_OF_GROUP_SEP = 1;
    private static final int NUMBER_OF_ITEMS_SEP = 4;

    @Page
    private ToolbarIconsPage page;

    @Test
    public void testGroupSeparators() {
        Graphene.guardAjax(page.getNoneGroupSep()).click();
        Graphene.guardAjax(page.getNoneItemSep()).click();
        checkNumberOfAllGroupsOrItemsSeparators(0, page.getLineGroupSep(), page.getGridGroupSep(), page.getDiscGroupSep(), page.getSquareGroupSep());
        checkNumberOfAllGroupsOrItemsSeparators(NUMBER_OF_GROUP_SEP, page.getLineGroupSep(), page.getGridGroupSep(), page.getDiscGroupSep(),
            page.getSquareGroupSep());
    }

    @Test
    public void testItemSeparators() {
        Graphene.guardAjax(page.getNoneGroupSep()).click();
        Graphene.guardAjax(page.getNoneItemSep()).click();
        checkNumberOfAllGroupsOrItemsSeparators(0, page.getLineItemSeparator(), page.getGridItemSep(), page.getDiscItemSep(),
            page.getSquareItemSep());
        checkNumberOfAllGroupsOrItemsSeparators(NUMBER_OF_ITEMS_SEP, page.getLineItemSeparator(), page.getGridItemSep(),
            page.getDiscItemSep(), page.getSquareItemSep());
    }

    /**
     * Checks for number of all groups/items separators
     *
     * @param numberOfSeparators the expected number of all groups/items separators
     */
    private void checkNumberOfAllGroupsOrItemsSeparators(int numberOfSeparators, WebElement whichLineSep,
        WebElement whichGridSep, WebElement whichDicsSep, WebElement whichSquareSep) {
        if (numberOfSeparators != 0) {
            guardAjax(whichLineSep).click();
        }
        assertEquals("Wrong number of line groups/items separators", numberOfSeparators, page.getLineSep().size());
        if (numberOfSeparators != 0) {
            guardAjax(whichGridSep).click();
        }
        assertEquals("Wrong number of grid groups/items separators", numberOfSeparators, page.getGridSep().size());
        if (numberOfSeparators != 0) {
            guardAjax(whichDicsSep).click();
        }
        assertEquals("Wrong number of disc groups/items separators", numberOfSeparators, page.getDiscSep().size());
        if (numberOfSeparators != 0) {
            guardAjax(whichSquareSep).click();
        }
        assertEquals("Wrong number of square groups/items separators", numberOfSeparators, page.getSquareSep().size());
    }
}

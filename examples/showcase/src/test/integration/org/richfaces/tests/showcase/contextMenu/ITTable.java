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
package org.richfaces.tests.showcase.contextMenu;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.showcase.contextMenu.page.TableContextMenuPage;

import category.Failing;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class ITTable extends AbstractContextMenuTest {

    @Page
    private TableContextMenuPage page;

    public static final int NUMBER_OF_LINES_TO_TEST_ON = 3;

    @Test
    @Category(Failing.class) //need to wait for Graphene upgrade in Page Fragments
    public void testContextMenuOnSomeLines() {
        int step = 0;
        List<WebElement> prices = page.getPrices();

        for (WebElement rowPrice : prices) {
            if (step >= NUMBER_OF_LINES_TO_TEST_ON) {
                break;
            }

            rowPrice.click();
            Graphene.waitGui().until(page.getWaitConditionOnSelectingRow(rowPrice));

            String priceFromTable = rowPrice.getText();

            page.getContextMenu().selectItem(TableContextMenuPage.VIEW, rowPrice);

            Graphene.waitGui().until().element(page.getPriceFromPopup()).is().visible();

            String priceFromPopup = page.getPriceFromPopup().getAttribute("value");

            assertEquals("The price is different in the table and in the popup invoked from context menu!", priceFromTable,
                priceFromPopup);

            page.closePopup();
            step++;
        }
    }

    @Test
    @Category(Failing.class) //not stable
    public void testContextMenuRenderedOnTheCorrectPosition() {
        WebElement elementToTryOn = page.getPrices().get(5);

        checkContextMenuRenderedAtCorrectPosition(elementToTryOn, page.getContextMenu().getMenuPopup(),
            InvocationType.RIGHT_CLICK, page.getWaitConditionOnSelectingRow(elementToTryOn));
    }
}

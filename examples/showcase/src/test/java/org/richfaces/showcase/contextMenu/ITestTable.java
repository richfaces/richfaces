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
package org.richfaces.showcase.contextMenu;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.common.Event;
import org.richfaces.showcase.contextMenu.page.TableContextMenuPage;

import category.FailingOnPhantomJS;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class ITestTable extends AbstractContextMenuTest {

    @Page
    private TableContextMenuPage page;

    @Drone
    protected WebDriver webDriver;

    public static final int NUMBER_OF_LINES_TO_TEST_ON = 3;

    @Test
    public void testContextMenuOnSomeLines() {
        int step = 0;
        List<WebElement> prices = page.getPrices();

        for (WebElement rowPrice : prices) {
            if (step >= NUMBER_OF_LINES_TO_TEST_ON) {
                break;
            }

            Graphene.guardAjax(rowPrice).click();
            Graphene.waitGui().until(page.getWaitConditionOnSelectingRow(rowPrice));
            waitFor(1000);// stabilization wait time, the waitGui before does not suffice

            String priceFromTable = rowPrice.getText();

            page.getContextMenu().advanced().show(rowPrice);
            Graphene.guardAjax(page.getContextMenu()).selectItem(0);

            Graphene.waitGui().until().element(page.getPriceFromPopup()).is().visible();

            String priceFromPopup = page.getPriceFromPopup().getAttribute("value");

            assertEquals("The price is different in the table and in the popup invoked from context menu!", priceFromTable,
                priceFromPopup);

            page.closePopup();
            step++;
        }
    }

    @Test
    @Category(FailingOnPhantomJS.class)
    public void testContextMenuRenderedOnTheCorrectPosition() {
        // resize browser window to enforce same conditions on all machines
        // this is a workaround for Jenkins where this test was failing
        webDriver.manage().window().setSize(new Dimension(1280, 720));

        WebElement elementToTryOn = page.getPrices().get(5);

        checkContextMenuRenderedAtCorrectPosition(elementToTryOn, page.getContextMenu(),
            Event.CONTEXTCLICK, page.getWaitConditionOnSelectingRow(elementToTryOn), false, false);
    }
}

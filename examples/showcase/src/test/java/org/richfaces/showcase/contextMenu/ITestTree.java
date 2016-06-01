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

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.waitModel;
import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.contextMenu.RichFacesContextMenu;
import org.richfaces.showcase.contextMenu.page.TreeContextMenuPage;

import category.FailingOnPhantomJS;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class ITestTree extends AbstractContextMenuTest {

    @Page
    private TreeContextMenuPage page;

    @Test
    public void testViewNodesInfoByCtxMenu() {
        page.expandNodes(4);

        int counter = 0;
        for (WebElement leaf : page.getLeaves()) {
            if (counter == ITestTable.NUMBER_OF_LINES_TO_TEST_ON) {
                break;
            }

            String artistFromTree = leaf.getText();

            guardAjax(leaf).click();
            waitModel().until(page.getExpextedConditionOnNodeSelected(leaf));
            waitFor(1000);// stabilization wait time, the waitGui before does not suffice

            RichFacesContextMenu contextMenu = page.getContextMenu();
            contextMenu.advanced().setShowEvent(Event.CONTEXTMENU);
            if (webDriver instanceof PhantomJSDriver) {
                // the menu is invoked by JavaScript, which does not invoke any ajax request
                contextMenu.advanced().show(leaf);
            } else {
                guardAjax(contextMenu.advanced()).show(leaf);
            }
            guardAjax(contextMenu).selectItem(0);
            waitModel().until().element(page.getArtistFromPopup()).is().visible();

            String artistFromPopup = page.getArtistFromPopup().getText();

            assertTrue(
                "The context menu was not invoked correctly! The popup contains different artist name than the node in the tree!",
                artistFromTree.contains(artistFromPopup));

            page.getCloseButton().click();
            waitModel().until().element(page.getArtistFromPopup()).is().not().visible();
            counter++;
        }
    }

    @Test
    @Category(FailingOnPhantomJS.class)
    public void testContextMenuRenderedAtCorrectPosition() {
        page.expandNodes(4);
        WebElement elementToTryOn = page.getLeaves().get(0);
        waitModel().until().element(elementToTryOn).is().visible();

        checkContextMenuRenderedAtCorrectPosition(elementToTryOn, page.getContextMenu(),
            Event.CONTEXTCLICK, page.getExpextedConditionOnNodeSelected(elementToTryOn), true, true);
    }
}

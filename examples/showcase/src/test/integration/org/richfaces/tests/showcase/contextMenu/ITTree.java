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

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.showcase.contextMenu.page.TableContextMenuPage;
import org.richfaces.tests.showcase.contextMenu.page.TreeContextMenuPage;

import category.Failing;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class ITTree extends AbstractContextMenuTest {

    @Page
    private TreeContextMenuPage page;

    @Test
    @Category(Failing.class) //need to wait for Graphene upgrade in Page Fragments
    public void testViewNodesInfoByCtxMenu() {
        page.expandNodes(4);

        int counter = 0;
        for (WebElement leaf : page.getLeaves()) {
            if (counter == ITTable.NUMBER_OF_LINES_TO_TEST_ON) {
                break;
            }

            String artistFromTree = leaf.getText();

            leaf.click();
            waitGui().withTimeout(3, TimeUnit.SECONDS).until(page.getExpextedConditionOnNodeSelected(leaf));
            waitGui();

            page.getContextMenu().selectItem(TableContextMenuPage.VIEW, leaf);
            waitGui().withTimeout(3, TimeUnit.SECONDS).until().element(page.getArtistFromPopup()).is().visible();

            String artistFromPopup = page.getArtistFromPopup().getText();

            assertTrue(
                "The context menu was not invoked correctly! The popup contains different artist name than the node in the tree!",
                artistFromTree.contains(artistFromPopup));

            page.getCloseButton().click();
            waitGui().withTimeout(3, TimeUnit.SECONDS).until().element(page.getArtistFromPopup()).is().not().visible();
            counter++;
        }
    }

    @Test
    @Category(Failing.class) //not stable, need to stabilize
    public void testContextMenuRenderedAtCorrectPosition() {
        page.expandNodes(4);
        WebElement elementToTryOn = page.getLeaves().get(0);
        Graphene.waitGui().withTimeout(2, TimeUnit.SECONDS).until().element(elementToTryOn).is().visible();

        checkContextMenuRenderedAtCorrectPosition(elementToTryOn, page.getContextMenu().getMenuPopup(),
            InvocationType.RIGHT_CLICK, page.getExpextedConditionOnNodeSelected(elementToTryOn));
    }
}

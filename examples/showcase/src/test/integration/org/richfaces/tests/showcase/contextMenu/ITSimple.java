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

import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.junit.Test;
import org.richfaces.tests.showcase.contextMenu.page.SimpleContextMenuPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class ITSimple extends AbstractContextMenuTest {

    @Page
    private SimpleContextMenuPage page;

    @Test
    public void testZoomIn() {
        Graphene.waitGui().until().element(page.getPicture()).is().visible();
        double widthBeforeZoomIn = getTargetWidth(page.getPicture());

        page.getContextMenu().selectItem(SimpleContextMenuPage.ZOOM_IN);

        double widthAfterZoom = getTargetWidth(page.getPicture());

        assertTrue("The picture was not zoomed in!", (widthAfterZoom - widthBeforeZoomIn) > EPSILON);
    }

    @Test
    public void testZoomOut() {
        Graphene.waitGui().until().element(page.getPicture()).is().visible();
        double widthBeforeZoomOut = getTargetWidth(page.getPicture());

        page.getContextMenu().selectItem(SimpleContextMenuPage.ZOOM_OUT);

        double widthAfterZoomOut = getTargetWidth(page.getPicture());

        assertTrue("The picture was not zoomed out!", (widthBeforeZoomOut - widthAfterZoomOut) > EPSILON);
    }

    @Test
    public void testContextMenuRenderedAtCorrectPosition() {
        checkContextMenuRenderedAtCorrectPosition(page.getPicture(), page.getContextMenu().getMenuPopup(),
            InvocationType.LEFT_CLICK, null);
    }
}
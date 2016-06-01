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

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.common.Locations;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.contextMenu.RichFacesContextMenu;
import org.richfaces.showcase.AbstractWebDriverTest;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class AbstractContextMenuTest extends AbstractWebDriverTest {

    public static final double EPSILON = 0.000000001;

    public static final int TOLERANCE = 3;

    protected void checkContextMenuRenderedAtCorrectPosition(WebElement target, RichFacesContextMenu ctxMenuFragment,
        Event showEvent, ExpectedCondition<Boolean> conditionTargetIsFocused, boolean selectingTargetTriggersAjax,
        boolean invokingMenuTriggersAjax) {

        if (conditionTargetIsFocused != null) {
            if (selectingTargetTriggersAjax) {
                guardAjax(target).click();
            } else {
                target.click();
            }
            Graphene.waitAjax().until(conditionTargetIsFocused);
            waitFor(1000);// stabilization wait time, the wait before does not suffice
        }

        ctxMenuFragment.advanced().setShowEvent(showEvent);
        // using show() from fragment will make sure workaround is used for PhantomJS (RF-14034)
        // but it will still show the menu in upper left corner of the browser for PhantomJS
        if (invokingMenuTriggersAjax) {
            guardAjax(ctxMenuFragment.advanced()).show(target);
        } else {
            ctxMenuFragment.advanced().show(target);
        }

        Locations expected = Utils.getLocations(target);
        // move by height/2 and width/2 so the new top left corner will be right in the middle of the element
        expected = expected.moveAllBy(expected.getWidth() / 2, expected.getHeight() / 2);

        Locations actual = Utils.getLocations(ctxMenuFragment.advanced().getMenuPopup());

        // check the top left corners are the same (with tolerance)
        Utils.tolerantAssertPointEquals(expected.getTopLeft(), actual.getTopLeft(), TOLERANCE, TOLERANCE,
            "The context menu was not rendered on the correct position!");
    }

    public double getTargetWidth(WebElement target) {
        Dimension dimension = target.getSize();

        return dimension.getWidth();
    }

    public double getTargetHeight(WebElement target) {
        Dimension dimension = target.getSize();

        return dimension.getHeight();
    }
}

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
package org.richfaces.showcase.contextMenu;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.showcase.AbstractWebDriverTest;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class AbstractContextMenuTest extends AbstractWebDriverTest {

    public static final double EPSILON = 0.000000001;

    public static final double TOLERANCE = 3.0;

    @ArquillianResource
    private Actions actions;

    public enum InvocationType {
        RIGHT_CLICK,
        LEFT_CLICK
    }

    protected void checkContextMenuRenderedAtCorrectPosition(WebElement target, WebElement contextMenuPopup,
        InvocationType type, ExpectedCondition<Boolean> conditionTargetIsFocused) {

        if (conditionTargetIsFocused != null) {
            target.click();
            Graphene.waitGui(webDriver).withTimeout(2, TimeUnit.SECONDS).until(conditionTargetIsFocused);
        }
        waitGui();

        // clicks in the middle of the target
        switch (type) {
            case LEFT_CLICK:
                actions.click(target);
                break;
            case RIGHT_CLICK:
                actions.contextClick(target);
                break;
            default:
                throw new IllegalArgumentException("Wrong type of context menu invocation!");
        }
        actions.build().perform();

        Graphene.waitGui().withTimeout(2, TimeUnit.SECONDS).until().element(contextMenuPopup).is().visible();

        Point locationOfTarget = target.getLocation();
        Point locationOfCtxMenu = contextMenuPopup.getLocation();

        double witdth = getTargetWidth(target);
        double height = getTargetHeight(target);

        double halfOfDiagonal = Math.sqrt((height * height) + (witdth * witdth)) / 2.0;
        double distance = getDistance(locationOfTarget, locationOfCtxMenu);

        double result = halfOfDiagonal - distance;

        assertTrue("The context menu was not rendered on the correct position! The difference is: " + result, result >= 0
            && result < TOLERANCE);
    }

    public double getTargetWidth(WebElement target) {
        Dimension dimension = target.getSize();

        return dimension.getWidth();
    }

    public double getTargetHeight(WebElement target) {
        Dimension dimension = target.getSize();

        return dimension.getHeight();
    }

    private double getDistance(Point point1, Point point2) {
        java.awt.Point point3 = new java.awt.Point(point1.getX(), point1.getY());
        java.awt.Point point4 = new java.awt.Point(point2.getX(), point2.getY());

        return point3.distance(point4);
    }

}

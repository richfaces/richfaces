/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.fragment.common;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import com.google.common.collect.Sets;

/**
 * Extending org.openqa.selenium.interactions.Actions by some functionality.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class Actions extends org.openqa.selenium.interactions.Actions {

    private final WebDriver driver;
    private static final Set<Event> supportedEventsInTriggerEventByWD = Sets.newHashSet(Event.CLICK, Event.DBLCLICK, Event.MOUSEDOWN,
        Event.MOUSEDOWN, Event.CONTEXTCLICK, Event.CONTEXTMENU, Event.MOUSEOUT, Event.MOUSEOVER, Event.MOUSEUP);

    public Actions(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    public Actions addAction(Action a) {
        this.action.addAction(a);
        return this;
    }

    public Actions blurByJS(final WebElement element) {
        return triggerEventByJS(Event.BLUR, element);
    }

    @Override
    public Actions click(WebElement onElement) {
        super.click(onElement);
        return this;
    }

    @Override
    public Actions click() {
        super.click();
        return this;
    }

    @Override
    public Actions clickAndHold(WebElement onElement) {
        super.clickAndHold(onElement);
        return this;
    }

    @Override
    public Actions clickAndHold() {
        super.clickAndHold();
        return this;
    }

    @Override
    public Actions contextClick(WebElement onElement) {
        super.contextClick(onElement);
        return this;
    }

    @Override
    public Actions contextClick() {
        super.contextClick();
        return this;
    }

    @Override
    public Actions doubleClick(WebElement onElement) {
        super.doubleClick(onElement);
        return this;
    }

    @Override
    public Actions doubleClick() {
        super.doubleClick();
        return this;
    }

    @Override
    public Actions dragAndDrop(WebElement source, WebElement target) {
        super.dragAndDrop(source, target);
        return this;
    }

    @Override
    public Actions dragAndDropBy(WebElement source, int xOffset, int yOffset) {
        super.dragAndDropBy(source, xOffset, yOffset);
        return this;
    }

    private Point getPossibleCoordinationsForMouseOut(WebElement element) {
        Locations l = Utils.getLocations(element);
        Dimension size = driver.manage().window().getSize();
        // the mouse will be in the middle of the element, these values will be used for counting the final distance
        int halfWidth = l.getWidth() / 2;
        int halfHeight = l.getHeight() / 2;
        int movementDistance = 10;// distance from the element in pixels
        // check whether position left from the Element is valid
        Locations moved = l.moveAllBy(-movementDistance, 0);
        Point point = moved.getTopLeft();
        if (point.x > 0) {
            return new Point(-halfWidth - movementDistance, 0);
        }
        // check whether position right from the Element is valid
        moved = l.moveAllBy(movementDistance, 0);
        point = moved.getTopRight();
        if (point.x < size.getWidth()) {
            return new Point(halfWidth + movementDistance, 0);
        }
        // check whether position up from the Element is valid
        moved = l.moveAllBy(0, -movementDistance);
        point = moved.getTopRight();
        if (point.y > 0) {
            return new Point(0, -halfHeight - movementDistance);
        }
        // check whether position down from the Element is valid
        moved = l.moveAllBy(0, movementDistance);
        point = moved.getBottomRight();
        if (point.y > size.getHeight()) {
            return new Point(0, halfHeight + movementDistance);
        }
        throw new RuntimeException("Cannot find any suitable position for mouseout event.");
    }

    @Override
    public Actions keyDown(Keys theKey) {
        super.keyDown(theKey);
        return this;
    }

    @Override
    public Actions keyDown(WebElement element, Keys theKey) {
        super.keyDown(element, theKey);
        return this;
    }

    @Override
    public Actions keyUp(Keys theKey) {
        super.keyUp(theKey);
        return this;
    }

    @Override
    public Actions keyUp(WebElement element, Keys theKey) {
        super.keyUp(element, theKey);
        return this;
    }

    @Override
    public Actions moveByOffset(int xOffset, int yOffset) {
        super.moveByOffset(xOffset, yOffset);
        return this;
    }

    @Override
    public Actions moveToElement(WebElement toElement) {
        super.moveToElement(toElement);
        return this;
    }

    @Override
    public Actions moveToElement(WebElement toElement, int xOffset, int yOffset) {
        super.moveToElement(toElement, xOffset, yOffset);
        return this;
    }

    @Override
    public Actions release(WebElement onElement) {
        super.release(onElement);
        return this;
    }

    @Override
    public Actions release() {
        super.release();
        return this;
    }

    @Override
    public Actions sendKeys(CharSequence... keysToSend) {
        super.sendKeys(keysToSend);
        return this;
    }

    @Override
    public Actions sendKeys(WebElement element, CharSequence... keysToSend) {
        super.sendKeys(element, keysToSend);
        return this;
    }

    public Actions triggerEventByJS(final Event event, final WebElement element) {
        return addAction(new Action() {
            @Override
            public void perform() {
                Graphene.waitGui().until().element(element).is().present();
                Utils.triggerJQ((JavascriptExecutor) driver, event.getEventName(), element);
            }
        });
    }

    /**
     * Will try to trigger given event with webdriver standard API method - nativelly
     *
     * @param event
     * @param element
     * @return
     * @throws IllegalArgumentException when given event can not be triggered by webdriver API
     */
    public Actions triggerEventByWD(Event event, WebElement element) {
        if (event.equals(Event.CLICK)) {
            return click(element);
        } else if (event.equals(Event.DBLCLICK)) {
            return doubleClick(element);
        } else if (event.equals(Event.MOUSEDOWN)) {
            return clickAndHold(element);
        } else if (event.equals(Event.MOUSEMOVE)) {
            return moveToElement(element);
        } else if (event.equals(Event.CONTEXTCLICK) || event.equals(Event.CONTEXTMENU)) {
            // workaround for RF-14034 / RF-14273
            if (driver instanceof PhantomJSDriver) {
                return triggerEventByJS(event, element);
            } else {
                // all other browsers use former solution - simply invoke context click
                return contextClick(element);
            }
        } else if (event.equals(Event.MOUSEOUT)) {
            Point coords = getPossibleCoordinationsForMouseOut(element);
            return moveToElement(element).moveByOffset(coords.x, coords.y);
        } else if (event.equals(Event.MOUSEOVER)) {
            return moveToElement(element, 1, 1);
        } else if (event.equals(Event.MOUSEUP)) {
            return clickAndHold(element).release();
        } else {
            throw new IllegalArgumentException("Cannot trigger this event " + event + " with WebDriver. Try to use 'triggerEventByJS' instead.");
        }
    }

    public Actions triggerEventByWDOtherwiseByJS(Event event, WebElement element) {
        if (supportedEventsInTriggerEventByWD.contains(event)) {
            return triggerEventByWD(event, element);
        } else {
            return triggerEventByJS(event, element);
        }
    }

    public Actions waitAction(final long timeInMillis) {
        action.addAction(new WaitAction(timeInMillis));
        return this;
    }

    private static class WaitAction implements Action {

        private final long timeInMillis;

        public WaitAction(long timeInMillis) {
            this.timeInMillis = timeInMillis;
        }

        @Override
        public void perform() {
            try {
                Thread.sleep(timeInMillis);
            } catch (InterruptedException ex) {
                Logger.getLogger(Actions.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

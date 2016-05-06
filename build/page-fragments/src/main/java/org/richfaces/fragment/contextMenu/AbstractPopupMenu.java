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
package org.richfaces.fragment.contextMenu;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.graphene.wait.FluentWait;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.common.Actions;
import org.richfaces.fragment.common.AdvancedVisibleComponentIteractions;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.common.VisibleComponentInteractions;
import org.richfaces.fragment.common.WaitingWrapper;
import org.richfaces.fragment.common.WaitingWrapperImpl;
import org.richfaces.fragment.common.picker.ChoicePicker;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;

import com.google.common.base.Optional;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public abstract class AbstractPopupMenu implements PopupMenu, AdvancedVisibleComponentIteractions<AbstractPopupMenu.AdvancedPopupMenuInteractions> {

    @Drone
    private WebDriver browser;

    @Root
    private WebElement root;

    /**
     * Creates a page fragment for menu group.
     */
    protected AbstractPopupMenu createSubMenuFragment(WebElement itemElement) {
        return Graphene.createPageFragment(getClass(), itemElement);
    }

    /**
     * Returns the name of the actual page fragment.
     *
     * @return
     */
    protected String getNameOfFragment() {
        return getClass().getSimpleName();
    }

    protected WebElement getRootElement() {
        return root;
    }

    /* ************************************************************************************************
     * API
     */
    @Override
    public abstract AdvancedPopupMenuInteractions advanced();

    @Override
    public PopupMenuGroup expandGroup(ChoicePicker picker, WebElement target) {
        advanced().setTarget(target);
        return expandGroup(picker);
    }

    @Override
    public PopupMenuGroup expandGroup(String header, WebElement target) {
        return expandGroup(ChoicePickerHelper.byVisibleText().match(header), target);
    }

    @Override
    public PopupMenuGroup expandGroup(int index, WebElement target) {
        return expandGroup(ChoicePickerHelper.byIndex().index(index), target);
    }

    @Override
    public PopupMenuGroup expandGroup(ChoicePicker picker) {
        if (!advanced().isVisible()) {
            advanced().show();
        }
        WebElement item = picker.pick(advanced().getMenuItemElements());
        if (item == null) {
            throw new IllegalArgumentException("There is no such group to be expanded, which satisfied the given rules!");
        }
        // open the sub menu
        new Actions(browser).moveToElement(item).perform();
        // create fragment and wait until it is visible
        AbstractPopupMenu expandedGroup = createSubMenuFragment(item);
        expandedGroup.advanced().waitUntilIsVisible().withMessage("The menu group did not show in given timeout!").perform();
        // set target to sub menu root
        expandedGroup.advanced().setTarget(item);
        return expandedGroup;
    }

    @Override
    public PopupMenuGroup expandGroup(String header) {
        return expandGroup(ChoicePickerHelper.byVisibleText().match(header));
    }

    @Override
    public PopupMenuGroup expandGroup(int index) {
        return expandGroup(ChoicePickerHelper.byIndex().index(index));
    }

    @Override
    public void selectItem(ChoicePicker picker) {
        if (!advanced().isVisible()) {
            advanced().show();
        }
        WebElement item = picker.pick(advanced().getMenuItemElements());
        if (item == null) {
            throw new IllegalArgumentException("There is no such option to be selected, which satisfied the given rules!");
        }
        item.click();
    }

    @Override
    public void selectItem(String header) {
        selectItem(ChoicePickerHelper.byVisibleText().match(header));
    }

    @Override
    public void selectItem(int index) {
        selectItem(ChoicePickerHelper.byIndex().index(index));
    }

    @Override
    public void selectItem(ChoicePicker picker, WebElement target) {
        advanced().setTarget(target);
        selectItem(picker);
    }

    @Override
    public void selectItem(String header, WebElement target) {
        selectItem(ChoicePickerHelper.byVisibleText().match(header), target);
    }

    @Override
    public void selectItem(int index, WebElement target) {
        selectItem(ChoicePickerHelper.byIndex().index(index), target);
    }

    /* ****************************************************************************************************
     * Nested classes
     */
    public abstract class AdvancedPopupMenuInteractions implements VisibleComponentInteractions {

        private final Event DEFAULT_SHOW_EVENT = Event.CONTEXTMENU;
        private Event showEvent = DEFAULT_SHOW_EVENT;

        private static final int DEFAULT_HIDEDELAY = 300;
        private int hideDelay = DEFAULT_HIDEDELAY;

        private static final int DEFAULT_SHOWDELAY = 50;
        private int showDelay = DEFAULT_SHOWDELAY;

        private WebElement target;

        private long _timeoutForPopupMenuToBeNotVisible = -1;
        private long _timeoutForPopupMenuToBeVisible = -1;

        /**
         * Dismisses currently displayed popup menu. If no popup menu is currently displayed an exception is thrown.
         *
         * @throws IllegalStateException when no popup menu is displayed in the time of invoking
         */
        public void hide() {
            if (!getMenuPopup().isDisplayed()) {
                throw new IllegalStateException("You are attemting to dismiss the " + getNameOfFragment() + ", however, no "
                    + getNameOfFragment() + " is displayed at the moment!");
            }
            Utils.performUniversalBlur(browser);
            waitUntilIsNotVisible().perform();
        }

        /**
         * Returns menu items elements. One needs to invoke popup menu in order to work with them. Note that some of the
         * elements may not become visible by just invoking the popup menu (e.g. popup menu items with sub items)
         *
         * @return the popup menu items
         */
        public List<WebElement> getItemsElements() {
            return Collections.unmodifiableList(getMenuItemElements());
        }

        /**
         * Returns all elements of this menu
         *
         * @return
         */
        public abstract List<WebElement> getMenuItemElements();

        protected abstract WebElement getScriptElement();

        public abstract WebElement getMenuPopup();

        protected int getShowDelay() {
            return showDelay;
        }

        public WebElement getTargetElement() {
            if (target == null) {
                setTarget();
            }
            return target;
        }

        /**
         * Invokes popup menu in the middle of the currently set target. By default it is presumed that popup menu is invoked by
         * right click. To change this behavior use <code>setInvoker()</code> method. You have to have a target set before
         * invocation of this method.
         *
         * @see #setInvoker(PopupMenuInvoker)
         * @see #setTarget(WebElement)
         */
        public void show() {
            show(getTargetElement());
        }

        /**
         * Invokes popup menu in the middle of the given target. By default it is presumed that popup menu is invoked by right
         * click. To change this behavior use <code>setInvoker()</code> method. It also works with the default value of
         * <code>showDelay == 50ms</code>. Use <code>#setShowDelay</code> if this value is different for this menu.
         *
         * @param givenTarget
         * @see #setupInvoker(PopupMenuInvoker)
         * @see #setShowDelay(int)
         */
        public void show(WebElement givenTarget) {
            new Actions(browser)
                .moveToElement(givenTarget)
                .triggerEventByWD(getShowEvent(), givenTarget).perform();

            advanced().waitUntilIsVisible().perform();
        }

        /**
         * Invokes popup menu on a given point within the given target. By default it is presumed that popup menu is invoked by
         * right click. To change this behavior use <code>setInvoker()</code> method.
         *
         * @param givenTarget
         * @param location
         * @see #setupInvoker(PopupMenuInvoker)
         */
        public void show(WebElement givenTarget, Point location) {
            throw new UnsupportedOperationException("File a feature request to have this, or even better implement it:)");
//            actions
//                .moveToElement(givenTarget)
//                .moveByOffset(location.getX(), location.getY())
//                .triggerEventByWD(invokeEvent, givenTarget).perform();
//
//            advanced().waitUntilIsVisible().perform();
        }

        public void setHideDelay() {
            hideDelay = DEFAULT_HIDEDELAY;
        }

        /**
         * Delay (in ms) between losing focus and menu closing
         *
         * @param newHideDelayInMillis
         */
        public void setHideDelay(int newHideDelayInMillis) {
            if (newHideDelayInMillis < 0) {
                throw new IllegalArgumentException("Can not be negative!");
            }
            hideDelay = newHideDelayInMillis;
        }

        protected Event getDefaultShowEvent() {
            return DEFAULT_SHOW_EVENT;
        }

        protected Event getShowEvent() {
            return showEvent;
        }

        public void setShowEvent() {
            setShowEvent(getDefaultShowEvent());
        }

        public void setShowEvent(Event newShowEvent) {
            if (newShowEvent == null) {
                throw new IllegalArgumentException("Parameter newInvokeEvent can not be null!");
            }
            showEvent = newShowEvent;
        }

        public void setShowEventFromWidget() {
            Optional<String> event = Utils.getComponentOption(root, "showEvent");
            setShowEvent(new Event(event.or(getDefaultShowEvent().getEventName())));
        }

        public void setShowDelay() {
            showDelay = DEFAULT_SHOWDELAY;
        }

        /**
         * Sets the delay which is between showevent observing and the menu opening
         *
         * @param newShowDelayInMillis
         */
        public void setShowDelay(int newShowDelayInMillis) {
            if (newShowDelayInMillis < 0) {
                throw new IllegalArgumentException("Can not be negative!");
            }
            showDelay = newShowDelayInMillis;
        }

        public void setTarget() {
            target = getRootElement();
        }

        public void setTarget(WebElement target) {
            this.target = target;
        }

        public void setTargetFromWidget() {
            String targetId = Utils.<String>getComponentOption(getRootElement(), "target").orNull();
            if (targetId != null) {
                target = browser.findElement(By.id(targetId));
            } else {
                target = getRootElement();
            }
        }

        public void setTimeoutForPopupMenuToBeNotVisible(long timeoutInMilliseconds) {
            _timeoutForPopupMenuToBeNotVisible = timeoutInMilliseconds;
        }

        public long getTimeoutForPopupMenuToBeNotVisible() {
            return _timeoutForPopupMenuToBeNotVisible == -1 ? Utils.getWaitAjaxDefaultTimeout(browser) : _timeoutForPopupMenuToBeNotVisible;
        }

        public void setTimeoutForPopupMenuToBeVisible(long timeoutInMilliseconds) {
            _timeoutForPopupMenuToBeVisible = timeoutInMilliseconds;
        }

        public long getTimeoutForPopupMenuToBeVisible() {
            return _timeoutForPopupMenuToBeVisible == -1 ? Utils.getWaitAjaxDefaultTimeout(browser) : _timeoutForPopupMenuToBeVisible;
        }

        /**
         * Waits until the popup menu is visible. It takes into account the <code>showDelay</code> which has default value 50ms.
         *
         * @see #setShowDelay(int)
         */
        public WaitingWrapper waitUntilIsNotVisible() {
            return new WaitingWrapperImpl() {

                @Override
                protected void performWait(FluentWait<WebDriver, Void> wait) {
                    wait.until().element(getMenuPopup()).is().not().visible();
                }
            }.withMessage("Waiting for menu to hide.")
                .withTimeout(hideDelay + getTimeoutForPopupMenuToBeNotVisible(), TimeUnit.MILLISECONDS);
        }

        public WaitingWrapper waitUntilIsVisible() {
            return new WaitingWrapperImpl() {

                @Override
                protected void performWait(FluentWait<WebDriver, Void> wait) {
                    wait.until().element(getMenuPopup()).is().visible();
                }
            }.withMessage("The " + getNameOfFragment() + " did not show in the given timeout!")
                .withTimeout(showDelay + getTimeoutForPopupMenuToBeVisible(), TimeUnit.MILLISECONDS);
        }

        @Override
        public boolean isVisible() {
            return Utils.isVisible(getMenuPopup());
        }
    }
}

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
package org.richfaces.fragment.panelMenu;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.wait.FluentWait;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.common.AdvancedVisibleComponentIteractions;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.common.VisibleComponentInteractions;
import org.richfaces.fragment.common.WaitingWrapper;
import org.richfaces.fragment.common.WaitingWrapperImpl;
import org.richfaces.fragment.common.picker.ChoicePicker;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;

import com.google.common.base.Predicate;

public abstract class AbstractPanelMenu implements PanelMenu, PanelMenuGroup, AdvancedVisibleComponentIteractions<AbstractPanelMenu.AdvancedAbstractPanelMenuInteractions> {

    @ArquillianResource
    private JavascriptExecutor executor;
    @Drone
    private WebDriver browser;

    @Override
    public PanelMenuItem selectItem(ChoicePicker picker) {
        WebElement itemRoot = picker.pick(getMenuItems());
        ensureElementExist(itemRoot);
        ensureElementIsEnabledAndVisible(itemRoot);
        RichFacesPanelMenuItem panelMenuItem = Graphene.createPageFragment(RichFacesPanelMenuItem.class, itemRoot);
        panelMenuItem.select();
        return panelMenuItem;
    }

    @Override
    public PanelMenuItem selectItem(String header) {
        return selectItem(ChoicePickerHelper.byVisibleText().match(header));
    }

    @Override
    public PanelMenuItem selectItem(int index) {
        return selectItem(ChoicePickerHelper.byIndex().index(index));
    }

    @Override
    public PanelMenuGroup expandGroup(ChoicePicker picker) {
        WebElement groupRoot = picker.pick(getMenuGroups());
        ensureElementExist(groupRoot);
        ensureElementIsEnabledAndVisible(groupRoot);
        WebElement groupHeader = getHeaderElementDynamically(groupRoot);
        if (isGroupExpanded(groupHeader)) {
            return Graphene.createPageFragment(RichFacesPanelMenuGroup.class, groupRoot);
        }
        executeEventOn(advanced().getExpandEvent(), groupHeader);
        advanced().waitUntilMenuGroupExpanded(groupHeader).perform();
        return Graphene.createPageFragment(RichFacesPanelMenuGroup.class, groupRoot);
    }

    @Override
    public PanelMenuGroup expandGroup(String header) {
        return expandGroup(ChoicePickerHelper.byVisibleText().startsWith(header));
    }

    @Override
    public PanelMenuGroup expandGroup(int index) {
        return expandGroup(ChoicePickerHelper.byIndex().index(index));
    }

    @Override
    public void collapseGroup(ChoicePicker picker) {
        WebElement groupRoot = picker.pick(getMenuGroups());
        ensureElementExist(groupRoot);
        ensureElementIsEnabledAndVisible(groupRoot);
        WebElement groupHeader = getHeaderElementDynamically(groupRoot);
        if (!isGroupExpanded(groupHeader)) {
            return;
        }
        executeEventOn(advanced().getCollapseEvent(), groupHeader);
        advanced().waitUntilMenuGroupCollapsed(groupHeader).perform();
    }

    @Override
    public void collapseGroup(String header) {
        collapseGroup(ChoicePickerHelper.byVisibleText().startsWith(header));
    }

    @Override
    public void collapseGroup(int index) {
        collapseGroup(ChoicePickerHelper.byIndex().index(index));
    }

    @Override
    public PanelMenu expandAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public PanelMenu collapseAll() {
        throw new UnsupportedOperationException();
    }

    public abstract List<WebElement> getMenuItems();

    public abstract List<WebElement> getMenuGroups();

    public abstract AdvancedAbstractPanelMenuInteractions advanced();

    public abstract class AdvancedAbstractPanelMenuInteractions implements VisibleComponentInteractions {

        private static final String CSS_EXPANDED_SUFFIX = "-exp";
        private static final String CSS_TRANSPARENT_SUFFIX = "-transparent";
        private static final String CSS_SELECTED_SUFFIX = "-sel";
        private static final String CSS_HOVERED_SUFFIX = "-hov";
        private static final String CSS_DISABLED_SUFFIX = "-dis";
        private static final String CSS_COLLAPSED_SUFFIX = "-colps";
        private static final String HEADER_SELECTOR_TO_INVOKE_EVENT_ON = "div[class*=rf-pm-][class*=-gr-hdr]";

        private Event expandEvent = Event.CLICK;
        private Event collapseEvent = Event.CLICK;
        private long _timoutForMenuGroupToBeExpanded = -1;
        private long _timeoutForMenuGroupToBeCollapsed = -1;

        protected String getHeaderSelectorToInovkeEventOn() {
            return HEADER_SELECTOR_TO_INVOKE_EVENT_ON;
        }

        protected String getCssExpandedSuffix() {
            return CSS_EXPANDED_SUFFIX;
        }

        protected String getCssTransparentSuffix() {
            return CSS_TRANSPARENT_SUFFIX;
        }

        protected String getCssSelectedSuffix() {
            return CSS_SELECTED_SUFFIX;
        }

        protected String getCssHoveredSuffix() {
            return CSS_HOVERED_SUFFIX;
        }

        protected String getCssDisabledSuffix() {
            return CSS_DISABLED_SUFFIX;
        }

        protected String getCssCollapsedSuffix() {
            return CSS_COLLAPSED_SUFFIX;
        }

        protected Event getExpandEvent() {
            return expandEvent;
        }

        protected Event getCollapseEvent() {
            return collapseEvent;
        }

        public void setExpandEvent(Event event) {
            expandEvent = event;
        }

        public void setCollapseEvent(Event event) {
            collapseEvent = event;
        }

        public boolean isGroupExpanded(WebElement groupRoot) {
            return AbstractPanelMenu.this.isGroupExpanded(getHeaderElementDynamically(groupRoot));
        }

        public WaitingWrapper waitUntilMenuGroupExpanded(final WebElement groupHeader) {
            return new WaitingWrapperImpl() {

                @Override
                protected void performWait(FluentWait<WebDriver, Void> wait) {
                    wait.ignoring(org.openqa.selenium.remote.ErrorHandler.UnknownServerException.class).until(
                        new Predicate<WebDriver>() {
                            @Override
                            public boolean apply(WebDriver input) {
                                return AbstractPanelMenu.this.isGroupExpanded(groupHeader);
                            }
                        });
                }
            }.withMessage("Waiting for Panel Menu group to be expanded!")
                .withTimeout(getTimoutForMenuGroupToBeExpanded(), TimeUnit.MILLISECONDS);
        }

        public WaitingWrapper waitUntilMenuGroupCollapsed(final WebElement groupHeader) {
            return new WaitingWrapperImpl() {

                @Override
                protected void performWait(FluentWait<WebDriver, Void> wait) {
                    wait.ignoring(org.openqa.selenium.remote.ErrorHandler.UnknownServerException.class).until(
                        new Predicate<WebDriver>() {
                            @Override
                            public boolean apply(WebDriver input) {
                                return !AbstractPanelMenu.this.isGroupExpanded(groupHeader);
                            }
                        });
                }
            }.withMessage("Waiting for Panel Menu group to be expanded!")
                .withTimeout(getTimeoutForMenuGroupToBeCollapsed(), TimeUnit.MILLISECONDS);
        }

        public void setTimoutForMenuGroupToBeExpanded(long timeoutInMilliseconds) {
            _timoutForMenuGroupToBeExpanded = timeoutInMilliseconds;
        }

        public long getTimoutForMenuGroupToBeExpanded() {
            return _timoutForMenuGroupToBeExpanded == -1 ? Utils.getWaitAjaxDefaultTimeout(browser) : _timoutForMenuGroupToBeExpanded;
        }

        public void setTimeoutForMenuGroupToBeCollapsed(long timeoutInMilliseconds) {
            _timeoutForMenuGroupToBeCollapsed = timeoutInMilliseconds;
        }

        public long getTimeoutForMenuGroupToBeCollapsed() {
            return _timeoutForMenuGroupToBeCollapsed == -1 ? Utils.getWaitAjaxDefaultTimeout(browser) : _timeoutForMenuGroupToBeCollapsed;
        }

        protected abstract WebElement getRootElement();

        @Override
        public boolean isVisible() {
            return Utils.isVisible(getRootElement());
        }
    }

    private boolean isGroupExpanded(WebElement groupHeader) {
        return groupHeader.getAttribute("class").contains(advanced().getCssExpandedSuffix());
    }

    private void ensureElementIsEnabledAndVisible(WebElement element) {
        checkElementIsVisible(element);
        if (isDisabled(element)) {
            throw new IllegalArgumentException("Element " + element + " can not be interacted with, as it is disabled.");
        }
    }

    private boolean isDisabled(WebElement group) {
        return group.getAttribute("class").contains(advanced().getCssDisabledSuffix());
    }

    private void checkElementIsVisible(WebElement element) {
        if (!new WebElementConditionFactory(element).isVisible().apply(browser)) {
            throw new IllegalArgumentException("Element: " + element + " must be visible before interacting with it!");
        }
    }

    private void executeEventOn(Event event, WebElement element) {
        Utils.triggerJQ(executor, event.getEventName(), element);
    }

    private WebElement getHeaderElementDynamically(WebElement element) {
        return element.findElement(By.cssSelector(advanced().getHeaderSelectorToInovkeEventOn()));
    }

    private void ensureElementExist(WebElement element) {
        if (element == null) {
            throw new IllegalArgumentException("Group/item must exist!");
        }
    }
}

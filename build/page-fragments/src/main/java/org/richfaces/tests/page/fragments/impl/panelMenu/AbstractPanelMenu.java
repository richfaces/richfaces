package org.richfaces.tests.page.fragments.impl.panelMenu;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.wait.FluentWait;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.common.AdvancedInteractions;
import org.richfaces.tests.page.fragments.impl.utils.Event;
import org.richfaces.tests.page.fragments.impl.utils.WaitingWrapper;
import org.richfaces.tests.page.fragments.impl.utils.WaitingWrapperImpl;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePicker;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;

import com.google.common.base.Predicate;

public abstract class AbstractPanelMenu implements PanelMenu, PanelMenuGroup, AdvancedInteractions<AbstractPanelMenu.AdvancedAbstractPanelMenuInteractions> {

    public static final String CSS_EXPANDED_SUFFIX = "-exp";
    public static final String CSS_TRANSPARENT_SUFFIX = "-transparent";
    public static final String CSS_SELECTED_SUFFIX = "-sel";
    public static final String CSS_HOVERED_SUFFIX = "-hov";
    public static final String CSS_DISABLED_SUFFIX = "-dis";
    public static final String CSS_COLLAPSED_SUFFIX = "-colps";
    private static final String HEADER_SELECTOR_TO_INVOKE_EVENT_ON = "> div[class*=rf-pm-][class*=-gr-hdr]";

    @ArquillianResource
    private JavascriptExecutor executor;
    @Drone
    private WebDriver browser;

    private AdvancedAbstractPanelMenuInteractions advancedInteractions = new AdvancedAbstractPanelMenuInteractions();

    private Event expandEvent = Event.CLICK;
    private Event collapseEvent = Event.CLICK;

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
        executeEventOn(expandEvent, groupHeader);
        // can not work with already picked element as it can be stale
        advanced().waitUntilMenuGroupExpanded(getHeaderElementDynamically(picker.pick(getMenuGroups())))
                  .perform();
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
        executeEventOn(collapseEvent, groupHeader);
        // can not work with already picked element as it can be stale
        advanced().waitUntilMenuGroupCollapsed(getHeaderElementDynamically(picker.pick(getMenuGroups()))).perform();
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

    @Override
    public AdvancedAbstractPanelMenuInteractions advanced() {
        return advancedInteractions;
    }

    public class AdvancedAbstractPanelMenuInteractions {

        private long _timoutForMenuGroupToBeExpanded = -1;
        private long _timeoutForMenuGroupToBeCollapsed = -1;

        public void setupExpandEvent(Event event) {
            expandEvent = event;
        }

        public void setupCollapseEvent(Event event) {
            collapseEvent = event;
        }

        public boolean isGroupExpanded(WebElement groupRoot) {
            return AbstractPanelMenu.this.isGroupExpanded(getHeaderElementDynamically(groupRoot));
        }

        public WaitingWrapper waitUntilMenuGroupExpanded(final WebElement group) {
            return new WaitingWrapperImpl() {

                @Override
                protected void performWait(FluentWait<WebDriver, Void> wait) {
                    wait.ignoring(org.openqa.selenium.remote.ErrorHandler.UnknownServerException.class).until(
                        new Predicate<WebDriver>() {
                            @Override
                            public boolean apply(WebDriver input) {
                                return AbstractPanelMenu.this.isGroupExpanded(group);
                            }
                        });
                }
            }.withMessage("Waiting for Panel Menu group to be expanded!")
             .withTimeout(getTimoutForMenuGroupToBeExpanded(), TimeUnit.MILLISECONDS);
        }

        public WaitingWrapper waitUntilMenuGroupCollapsed(final WebElement group) {
            return new WaitingWrapperImpl() {

                @Override
                protected void performWait(FluentWait<WebDriver, Void> wait) {
                    wait.ignoring(org.openqa.selenium.remote.ErrorHandler.UnknownServerException.class).until(
                        new Predicate<WebDriver>() {
                            @Override
                            public boolean apply(WebDriver input) {
                                return !AbstractPanelMenu.this.isGroupExpanded(group);
                            }
                        });
                }
            }.withMessage("Waiting for Panel Menu group to be expanded!")
             .withTimeout(getTimeoutForMenuGroupToBeCollapsed(), TimeUnit.MILLISECONDS);
        }

        public void setupTimoutForMenuGroupToBeExpanded(long timeoutInMilliseconds) {
            _timoutForMenuGroupToBeExpanded = timeoutInMilliseconds;
        }

        public long getTimoutForMenuGroupToBeExpanded() {
            return _timoutForMenuGroupToBeExpanded == -1 ? Utils.getWaitAjaxDefaultTimeout(browser) : _timoutForMenuGroupToBeExpanded;
        }

        public void setupTimeoutForMenuGroupToBeCollapsed(long timeoutInMilliseconds) {
            _timeoutForMenuGroupToBeCollapsed = timeoutInMilliseconds;
        }

        public long getTimeoutForMenuGroupToBeCollapsed() {
            return _timeoutForMenuGroupToBeCollapsed == -1 ? Utils.getWaitAjaxDefaultTimeout(browser) : _timeoutForMenuGroupToBeCollapsed;
        }
    }

    private boolean isGroupExpanded(WebElement group) {
        return group.getAttribute("class").contains(CSS_EXPANDED_SUFFIX);
    }

    private void ensureElementIsEnabledAndVisible(WebElement element) {
        checkElementIsVisible(element);
        if (isDisabled(element)) {
            throw new IllegalArgumentException("Element " + element + " can not be interacted with, as it is disabled.");
        }
    }

    private boolean isDisabled(WebElement group) {
        return group.getAttribute("class").contains(CSS_DISABLED_SUFFIX);
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
        return element.findElement(ByJQuery.selector(HEADER_SELECTOR_TO_INVOKE_EVENT_ON));
    }

    private void ensureElementExist(WebElement element) {
        if (element == null) {
            throw new IllegalArgumentException("Group/item must exist!");
        }
    }
}

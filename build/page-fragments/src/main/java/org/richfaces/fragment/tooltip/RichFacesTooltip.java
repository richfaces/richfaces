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
package org.richfaces.fragment.tooltip;

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.graphene.wait.FluentWait;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.common.Actions;
import org.richfaces.fragment.common.AdvancedVisibleComponentIteractions;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.common.TypeResolver;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.common.VisibleComponentInteractions;
import org.richfaces.fragment.common.WaitingWrapper;
import org.richfaces.fragment.common.WaitingWrapperImpl;

import com.google.common.base.Predicate;

/**
 * The root of this tooltip will be used for invoking this tooltip. In other words:
 * set root of this toolTip to the panel on which can be the toolTip invoked so you don't have to set the target before invoking.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @param <CONTENT>
 */
public abstract class RichFacesTooltip<CONTENT> implements Tooltip<CONTENT>, AdvancedVisibleComponentIteractions<RichFacesTooltip<CONTENT>.AdvancedTooltipInteractions> {

    @Root
    private WebElement root;

    @ArquillianResource
    private WebDriver driver;

    private final Class<CONTENT> contentClass = (Class<CONTENT>) TypeResolver.resolveRawArguments(RichFacesTooltip.class, getClass())[0];
    private final AdvancedTooltipInteractions interactions = new AdvancedTooltipInteractions();

    @Override
    public AdvancedTooltipInteractions advanced() {
        return interactions;
    }

    @Override
    public CONTENT getContent() {
        return Graphene.createPageFragment(contentClass, advanced().getTooltipElement());
    }

    @Override
    public RichFacesTooltip<CONTENT> hide() {
        advanced().initiateTooltipsBefore();
        new Actions(driver)
            .triggerEventByWD(advanced().getHideEvent(), advanced().getTarget())
            .perform();
        advanced().waitUntilTooltipIsNotVisible().perform();
        return this;
    }

    @Override
    public RichFacesTooltip<CONTENT> hide(WebElement target) {
        advanced().setTarget(target);
        return hide();
    }

    @Override
    public RichFacesTooltip<CONTENT> show() {
        advanced().initiateTooltipsBefore();
        new Actions(driver)
            .moveToElement(advanced().getTarget())
            .triggerEventByWD(advanced().getShowEvent(), advanced().getTarget())
            .perform();
        advanced().waitUntilTooltipIsVisible().perform();
        advanced().acquireLastVisibleTooltipIDIfNotSet();
        return this;
    }

    @Override
    public RichFacesTooltip<CONTENT> show(WebElement target) {
        advanced().setTarget(target);
        return show();
    }

    public class AdvancedTooltipInteractions implements VisibleComponentInteractions {

        private final ByJQuery tooltipsSelector = ByJQuery.selector(".rf-tt:visible");
        private final Event DEFAULT_SHOW_EVENT = Event.MOUSEOVER;
        private Event showEvent = DEFAULT_SHOW_EVENT;
        private final Event DEFAULT_HIDE_EVENT = Event.MOUSEOUT;
        private Event hideEvent = DEFAULT_HIDE_EVENT;
        private WebElement target;
        private String idOfTooltip;
        private int tooltipsBefore;

        private long _timoutForTooltipToBeNotVisible = -1;
        private long _timeoutForTooltipToBeVisible = -1;

        protected void acquireLastVisibleTooltipIDIfNotSet() {
            if (getIdOfTooltip() == null) {
                setIdOfTooltip(driver.findElement(ByJQuery.selector(".rf-tt:last:visible")).getAttribute("id"));
            }
        }

        protected Event getHideEvent() {
            return hideEvent;
        }

        protected String getIdOfTooltip() {
            return idOfTooltip;
        }

        public WebElement getRootElement() {
            return root;
        }

        protected void setIdOfTooltip(String id) {
            this.idOfTooltip = id;
        }

        protected Event getShowEvent() {
            return showEvent;
        }

        protected WebElement getTarget() {
            if (target == null) {
                return getRootElement();
            }
            return target;
        }

        protected int getTooltipsBefore() {
            return tooltipsBefore;
        }

        protected ByJQuery getTooltipsSelector() {
            return tooltipsSelector;
        }

        /**
         * Show the tooltip before this method. It will return actual tooltip element (element depends on the tooltip's visible state).
         */
        public WebElement getTooltipElement() {
            if (getIdOfTooltip() == null) {
                throw new IllegalStateException("Cannot obtain tooltip element. You have to show it first.");
            }
            return driver.findElement(By.id(getIdOfTooltip()));
        }

        protected void initiateTooltipsBefore() {
            tooltipsBefore = driver.findElements(getTooltipsSelector()).size();
        }

        @Override
        public boolean isVisible() {
            return Utils.isVisible(getRootElement());
        }

        public void setHideEvent() {
            this.hideEvent = DEFAULT_HIDE_EVENT;
        }

        public void setHideEvent(Event event) {
            this.hideEvent = event;
        }

        public void setShowEvent() {
            this.showEvent = DEFAULT_SHOW_EVENT;
        }

        public void setShowEvent(Event event) {
            this.showEvent = event;
        }

        public void setTarget() {
            setTarget(null);
        }

        public void setTarget(WebElement target) {
            this.target = target;
        }

        public WaitingWrapper waitUntilTooltipIsNotVisible() {
            return getIdOfTooltip() == null
                ? new WaitingWrapperImpl() {
                    @Override
                    protected void performWait(FluentWait<WebDriver, Void> wait) {
                        wait.until(new Predicate<WebDriver>() {
                            @Override
                            public boolean apply(WebDriver input) {
                                if (getTooltipsBefore() == 0) {
                                    return driver.findElements(getTooltipsSelector()).isEmpty();
                                } else {
                                    return driver.findElements(getTooltipsSelector()).size() < getTooltipsBefore();
                                }
                            }
                        });
                    }
                }.withTimeout(getTimoutForTooltipToBeNotVisible(), TimeUnit.MILLISECONDS)
                .withMessage("Waiting until some tooltip disappears. There were " + getTooltipsBefore() + " tooltips before, now there are: " + driver.findElements(getTooltipsSelector()).size())
                : new WaitingWrapperImpl() {
                    @Override
                    protected void performWait(FluentWait<WebDriver, Void> wait) {
                        wait.until().element(driver, By.id(getIdOfTooltip())).is().not().visible();
                    }
                }.withTimeout(getTimoutForTooltipToBeNotVisible(), TimeUnit.MILLISECONDS)
                .withMessage("Waiting until tooltip is not visible.");
        }

        public WaitingWrapper waitUntilTooltipIsVisible() {
            return getIdOfTooltip() == null
                ? new WaitingWrapperImpl() {
                    @Override
                    protected void performWait(FluentWait<WebDriver, Void> wait) {
                        wait.until(new Predicate<WebDriver>() {
                            @Override
                            public boolean apply(WebDriver input) {
                                return driver.findElements(getTooltipsSelector()).size() > getTooltipsBefore();
                            }
                        });
                    }
                }.withTimeout(getTimeoutForTooltipToBeVisible(), TimeUnit.MILLISECONDS)
                .withMessage("Waiting until a new tooltip appears. There were " + getTooltipsBefore() + " tooltips before, now there are: " + driver.findElements(getTooltipsSelector()).size())
                : new WaitingWrapperImpl() {
                    @Override
                    protected void performWait(FluentWait<WebDriver, Void> wait) {
                        wait.until().element(driver, By.id(getIdOfTooltip())).is().visible();
                    }
                }.withTimeout(getTimeoutForTooltipToBeVisible(), TimeUnit.MILLISECONDS)
                .withMessage("Waiting until tooltip is visible.");
        }

        public void setTimoutForTooltipToBeNotVisible(long timeoutInMilliseconds) {
            _timoutForTooltipToBeNotVisible = timeoutInMilliseconds;
        }

        public long getTimoutForTooltipToBeNotVisible() {
            return _timoutForTooltipToBeNotVisible == -1 ? Utils.getWaitAjaxDefaultTimeout(driver) : _timoutForTooltipToBeNotVisible;
        }

        public void setTimeoutForTooltipToBeVisible(long timeoutInMilliseconds) {
            _timeoutForTooltipToBeVisible = timeoutInMilliseconds;
        }

        public long getTimeoutForTooltipToBeVisible() {
            return _timeoutForTooltipToBeVisible == -1 ? Utils.getWaitAjaxDefaultTimeout(driver) : _timeoutForTooltipToBeVisible;
        }
    }
}

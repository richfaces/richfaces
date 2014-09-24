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
package org.richfaces.fragment.collapsiblePanel;

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.wait.FluentWait;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.AdvancedVisibleComponentIteractions;
import org.richfaces.fragment.common.Icon;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.common.VisibleComponentInteractions;
import org.richfaces.fragment.common.WaitingWrapper;
import org.richfaces.fragment.common.WaitingWrapperImpl;
import org.richfaces.fragment.panel.AbstractPanel;

import com.google.common.base.Predicate;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class RichFacesCollapsiblePanel<HEADER, BODY> extends AbstractPanel<HEADER, BODY> implements CollapsiblePanel<HEADER, BODY>, AdvancedVisibleComponentIteractions<RichFacesCollapsiblePanel<HEADER, BODY>.AdvancedCollapsiblePanelInteractions> {

    @ArquillianResource
    private WebDriver browser;

    @FindBy(className = "rf-cp-hdr")
    private GrapheneElement headerElement;
    @FindBy(className = "rf-cp-ico")
    private Icon leftIconElement;
    @FindBy(className = "rf-cp-exp-ico")
    private Icon rightIconElement;
    @FindBy(className = "rf-cp-lbl")
    private GrapheneElement labelElement;

    @FindBy(className = "rf-cp-b")
    private GrapheneElement bodyElement;
    @FindBy(className = "rf-cp-empty")
    private GrapheneElement emptyBodyElement;

    private final AdvancedCollapsiblePanelInteractions interactions = new AdvancedCollapsiblePanelInteractions();

    @Override
    public AdvancedCollapsiblePanelInteractions advanced() {
        return interactions;
    }

    @Override
    public CollapsiblePanel<HEADER, BODY> collapse() {
        if (!advanced().isCollapsed()) {
            advanced().getHeaderElement().click();
            advanced().waitUntilPanelIsCollapsed().perform();
        }
        return this;
    }

    @Override
    public CollapsiblePanel<HEADER, BODY> expand() {
        if (advanced().isCollapsed()) {
            advanced().getHeaderElement().click();
            advanced().waitUntilPanelIsExpanded().perform();
        }
        return this;
    }

    public class AdvancedCollapsiblePanelInteractions extends AdvancedPanelInteractions implements VisibleComponentInteractions {

        private static final String COLLAPSED_HEADER_CLASS = "rf-cp-hdr-colps";

        private long _timeoutForPanelIsSwitched = -1;

        protected GrapheneElement getEmptyBodyElement() {
            return emptyBodyElement;
        }

        @Override
        public WebElement getBodyElement() {
            return (Utils.isVisible(bodyElement) ? bodyElement : getEmptyBodyElement());
        }

        protected String getCollapsedHeaderClass() {
            return COLLAPSED_HEADER_CLASS;
        }

        @Override
        public GrapheneElement getHeaderElement() {
            return headerElement;
        }

        public WebElement getLabelElement() {
            return labelElement;
        }

        public Icon getLeftIcon() {
            return leftIconElement;
        }

        public Icon getRightIcon() {
            return rightIconElement;
        }

        public long getTimeoutForPanelIsSwitched() {
            return _timeoutForPanelIsSwitched == -1 ? Utils.getWaitAjaxDefaultTimeout(browser) : _timeoutForPanelIsSwitched;
        }

        public boolean isCollapsed() {
            return getHeaderElement().getAttribute("class").contains(getCollapsedHeaderClass());
        }

        /**
         * Set timeout for panel to collapse or expand
         *
         * @param timeoutInMillis
         */
        public void setTimeoutForPanelIsSwitched(long timeoutInMillis) {
            this._timeoutForPanelIsSwitched = timeoutInMillis;
        }

        public WaitingWrapper waitUntilPanelIsCollapsed() {
            return new WaitingWrapperImpl() {
                @Override
                protected void performWait(FluentWait<WebDriver, Void> wait) {
                    wait.until(new Predicate<WebDriver>() {
                        @Override
                        public boolean apply(WebDriver t) {
                            return isCollapsed();
                        }
                    });
                }
            }.withMessage("Waiting for panel to collapse.")
                .withTimeout(getTimeoutForPanelIsSwitched(), TimeUnit.MILLISECONDS);
        }

        public WaitingWrapper waitUntilPanelIsExpanded() {
            return new WaitingWrapperImpl() {
                @Override
                protected void performWait(FluentWait<WebDriver, Void> wait) {
                    wait.until(new Predicate<WebDriver>() {
                        @Override
                        public boolean apply(WebDriver t) {
                            return !isCollapsed();
                        }
                    });
                }
            }.withMessage("Waiting for panel to expand.")
                .withTimeout(getTimeoutForPanelIsSwitched(), TimeUnit.MILLISECONDS);
        }

        @Override
        public boolean isVisible() {
            return Utils.isVisible(getRootElement());
        }
    }
}

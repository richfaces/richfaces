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
package org.richfaces.tests.page.fragments.impl.tree;

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.wait.FluentWait;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.utils.Actions;
import org.richfaces.tests.page.fragments.impl.utils.WaitingWrapper;
import org.richfaces.tests.page.fragments.impl.utils.WaitingWrapperImpl;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePicker;

import com.google.common.base.Predicate;

public class RichFacesTreeNode extends RichFacesTree implements Tree.TreeNode {

    @FindBy(className = "rf-trn")
    private WebElement infoElement;

    @FindBy(css = ".rf-trn > .rf-trn-hnd")
    private WebElement handleElement;

    @FindBy(css = ".rf-trn > .rf-trn-hnd-ldn-fct")
    private WebElement handleLoadingElement;

    @FindBy(css = ".rf-trn > .rf-trn-cnt")
    private WebElement containerElement;

    @FindByJQuery(".rf-trn > .rf-trn-cnt > .rf-trn-ico:visible")
    private WebElement iconElement;
    @FindBy(css = ".rf-trn > .rf-trn-cnt > .rf-trn-lbl")
    private WebElement labelElement;
    @FindByJQuery(".rf-trn > .rf-trn-cnt > .rf-trn-lbl[onclick]")
    private WebElement elementForSelection;
    @FindByJQuery(".rf-trn > .rf-trn-cnt > .rf-trn-lbl >*[onclick]")
    private WebElement innerElementForSelection;

    @Drone
    private WebDriver driver;

    private final AdvancedNodeInteractionsImpl interactions = new AdvancedNodeInteractionsImpl();

    @Override
    public AdvancedNodeInteractionsImpl advanced() {
        return interactions;
    }

    @Override
    protected int getIndexOfPickedElement(ChoicePicker picker) {
        // because the treeNode has an extra element at first index, we have to decrease the index by 1
        return super.getIndexOfPickedElement(picker) - 1;
    }

    public class AdvancedNodeInteractionsImpl extends AdvancedTreeInteractionsImpl implements AdvancedTreeNodeInteractions {

        private long _timeoutForNodeToBeCollapsed = -1;
        private long _timeoutForNodeToBeExpanded = -1;
        private long _timeoutForNodeToBeSelected = -1;

        @Override
        public TreeNode collapse() {
            if (!isCollapsed()) {
                if (isToggleByHandle()) {
                    getHandleElement().click();
                } else {
                    new Actions(driver).triggerEventByWD(getToggleNodeEvent(), getCorrectElementForInteraction()).perform();
                }
            }
            waitUntilNodeIsCollapsed().perform();
            return RichFacesTreeNode.this;
        }

        @Override
        public TreeNode expand() {
            if (!isExpanded()) {
                if (isToggleByHandle()) {
                    getHandleElement().click();
                } else {
                    new Actions(driver).triggerEventByWD(getToggleNodeEvent(), getCorrectElementForInteraction()).perform();
                }
            }
            waitUntilNodeIsExpanded().perform();
            return RichFacesTreeNode.this;
        }

        @Override
        public WebElement getContainerElement() {
            return containerElement;
        }

        /**
         * We have to get correct element, which is by default the label element,
         * but when there is a panel inside the node, the interaction point is moved to that panel.
         * When the label element has onclick action (unchangeable event for selection), then return the label element,
         * otherwise return a child (with onclick action) of that label.
         */
        private WebElement getCorrectElementForInteraction() {
            return Utils.isVisible(elementForSelection) ? elementForSelection : innerElementForSelection;
        }

        @Override
        public WebElement getHandleElement() {
            return handleElement;
        }

        @Override
        public WebElement getHandleLoadingElement() {
            return handleLoadingElement;
        }

        @Override
        public WebElement getIconElement() {
            return iconElement;
        }

        @Override
        public WebElement getLabelElement() {
            return labelElement;
        }

        @Override
        public WebElement getNodeInfoElement() {
            return infoElement;
        }

        @Override
        public boolean isCollapsed() {
            return getRootElement().getAttribute("class").contains("rf-tr-nd-colps")
                && getHandleElement().getAttribute("class").contains("rf-trn-hnd-colps")
                && getIconElement().getAttribute("class").contains("rf-trn-ico-colps");
        }

        @Override
        public boolean isExpanded() {
            return getRootElement().getAttribute("class").contains("rf-tr-nd-exp")
                && getHandleElement().getAttribute("class").contains("rf-trn-hnd-exp")
                && getIconElement().getAttribute("class").contains("rf-trn-ico-exp");
        }

        @Override
        public boolean isLeaf() {
            return getRootElement().getAttribute("class").contains("rf-tr-nd-lf")
                && getHandleElement().getAttribute("class").contains("rf-trn-hnd-lf")
                && getIconElement().getAttribute("class").contains("rf-trn-ico-lf");
        }

        @Override
        public boolean isSelected() {
            return getContainerElement().getAttribute("class").contains("rf-trn-sel");
        }

        @Override
        public TreeNode select() {
            if (!isSelected()) {
                getCorrectElementForInteraction().click();
            }
            waitUntilNodeIsSelected().perform();
            return RichFacesTreeNode.this;
        }

        public void setuptimeoutForNodeToBeExpanded(long timeoutInMilliseconds) {
            _timeoutForNodeToBeExpanded = timeoutInMilliseconds;
        }

        public long getTimeoutForNodeToBeExpanded() {
            return _timeoutForNodeToBeExpanded == -1 ? Utils.getWaitAjaxDefaultTimeout(driver) : _timeoutForNodeToBeExpanded;
        }

        public void setupTimeoutForNodeToBeCollapsed(long timeoutInMilliseconds) {
            _timeoutForNodeToBeCollapsed = timeoutInMilliseconds;
        }

        public long getTimeoutForNodeToBeCollapsed() {
            return _timeoutForNodeToBeCollapsed == -1 ? Utils.getWaitAjaxDefaultTimeout(driver) : _timeoutForNodeToBeCollapsed;
        }

        public void setupTimeoutForNodeToBeSelected(long timeoutInMilliseconds) {
            _timeoutForNodeToBeSelected = timeoutInMilliseconds;
        }

        public long getTimeoutForNodeToBeSelected() {
            return _timeoutForNodeToBeSelected == -1 ? Utils.getWaitAjaxDefaultTimeout(driver) : _timeoutForNodeToBeSelected;
        }

        @Override
        public WaitingWrapper waitUntilNodeIsCollapsed() {
            return new WaitingWrapperImpl() {

                @Override
                protected void performWait(FluentWait<WebDriver, Void> wait) {
                    wait.until(new Predicate<WebDriver>() {

                        @Override
                        public boolean apply(WebDriver input) {
                            return isCollapsed();
                        }
                    });
                }
            }.withMessage("Waiting for node to be collapsed")
             .withTimeout(getTimeoutForNodeToBeCollapsed(), TimeUnit.MILLISECONDS);
        }

        @Override
        public WaitingWrapper waitUntilNodeIsExpanded() {
            return new WaitingWrapperImpl() {

                @Override
                protected void performWait(FluentWait<WebDriver, Void> wait) {
                    wait.until(new Predicate<WebDriver>() {

                        @Override
                        public boolean apply(WebDriver input) {
                            return isExpanded();
                        }
                    });
                }
            }.withMessage("Waiting for node to be expanded")
             .withTimeout(getTimeoutForNodeToBeExpanded(), TimeUnit.MILLISECONDS);
        }

        @Override
        public WaitingWrapper waitUntilNodeIsNotSelected() {
            return new WaitingWrapperImpl() {

                @Override
                protected void performWait(FluentWait<WebDriver, Void> wait) {
                    wait.until(new Predicate<WebDriver>() {

                        @Override
                        public boolean apply(WebDriver input) {
                            return !isSelected();
                        }
                    });
                }
            }.withMessage("Waiting for node to be not selected");
        }

        @Override
        public WaitingWrapper waitUntilNodeIsSelected() {
            return new WaitingWrapperImpl() {

                @Override
                protected void performWait(FluentWait<WebDriver, Void> wait) {
                    wait.until(new Predicate<WebDriver>() {

                        @Override
                        public boolean apply(WebDriver input) {
                            return isSelected();
                        }
                    });
                }
            }.withMessage("Waiting for node to be selected")
             .withTimeout(getTimeoutForNodeToBeSelected(), TimeUnit.MILLISECONDS);
        }
    }
}

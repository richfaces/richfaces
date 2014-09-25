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
package org.richfaces.fragment.orderingList;

import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.richfaces.fragment.common.Actions;
import org.richfaces.fragment.common.AdvancedVisibleComponentIteractions;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.common.VisibleComponentInteractions;
import org.richfaces.fragment.common.picker.ChoicePicker;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.fragment.common.picker.MultipleChoicePicker;
import org.richfaces.fragment.list.ListComponent;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractOrderingList implements OrderingList, AdvancedVisibleComponentIteractions<AbstractOrderingList.AdvancedOrderingListInteractions> {

    @Root
    private WebElement root;

    @Drone
    private WebDriver driver;

    private final OrderingInteraction orderingInteraction = new OrderingInteractionImpl();
    private final PuttingSelectedItem puttingSelectedItem = new PuttingSelectedItemImpl();

    @Override
    public abstract AdvancedOrderingListInteractions advanced();

    @Override
    public PuttingSelectedItem select(String visibleText) {
        return select(ChoicePickerHelper.byVisibleText().match(visibleText));
    }

    @Override
    public PuttingSelectedItem select(Integer index) {
        return select(ChoicePickerHelper.byIndex().index(index));
    }

    @Override
    public PuttingSelectedItem select(ChoicePicker picker) {
        unselectAll();
        selectItem(picker.pick(advanced().getItemsElements()));
        return getPuttingSelectedItem();
    }

    protected void selectItem(final WebElement item) {
        new Actions(driver).keyDown(Keys.CONTROL).click(item).keyUp(Keys.CONTROL).addAction(new Action() {
            @Override
            public void perform() {
                Graphene.waitGui().until().element(item).attribute("class").contains(advanced().getStyleForSelectedItem());
            }
        }).perform();
    }

    protected void selectItems(List<WebElement> list) {
        for (WebElement webElement : list) {
            selectItem(webElement);
        }
    }

    protected void unselectAll() {
        if (!advanced().getSelectedItemsElements().isEmpty()) {
            new Actions(driver)
                .click(advanced().getItemsElements().get(0))
                .keyDown(Keys.CONTROL).click(advanced().getItemsElements().get(0))
                .keyUp(Keys.CONTROL)
                .addAction(new Action() {
                    @Override
                    public void perform() {
                        Graphene.waitGui().until().element(advanced().getItemsElements().get(0)).attribute("class").not().contains("rf-ord-sel");
                    }
                }).perform();
            if (!advanced().getSelectedItemsElements().isEmpty()) {
                throw new RuntimeException("The unselection was not successfull.");
            }
        }
    }

    protected OrderingInteraction getOrderingInteraction() {
        return orderingInteraction;
    }

    protected PuttingSelectedItem getPuttingSelectedItem() {
        return puttingSelectedItem;
    }

    private class PuttingSelectedItemImpl implements PuttingSelectedItem {

        private OrderingList putAction(int positionSource, int positionTarget, int differenceToEnd) {
            if (positionSource != positionTarget) {
                int differenceBetween = positionTarget - positionSource;
                int absBetween = Math.abs(differenceBetween);
                int min = Math.min(absBetween, Math.min(positionTarget, differenceToEnd));
                if (min == absBetween) {
                    singleStepMove(differenceBetween);
                } else if (min == positionTarget) {
                    getOrderingInteraction().top();
                    if (positionTarget != 0) {
                        singleStepMove(positionTarget);
                    }
                } else {
                    getOrderingInteraction().bottom();
                    if (differenceToEnd != 0) {
                        singleStepMove(-differenceToEnd);
                    }
                }
            }
            return AbstractOrderingList.this;
        }

        @Override
        public OrderingList putItAfter(ChoicePicker picker) {
            int indexOfTargetItem = Utils.getIndexOfElement(picker.pick(advanced().getItemsElements())) + 1;
            return putAction(Utils.getIndexOfElement(advanced().getSelectedItemsElements().get(0)), indexOfTargetItem, advanced().getItemsElements().size() - indexOfTargetItem);
        }

        @Override
        public OrderingList putItAfter(int index) {
            return putItAfter(ChoicePickerHelper.byIndex().index(index));
        }

        @Override
        public OrderingList putItAfter(String match) {
            return putItAfter(ChoicePickerHelper.byVisibleText().match(match));
        }

        @Override
        public OrderingList putItBefore(ChoicePicker picker) {
            int indexOfTargetItem = Utils.getIndexOfElement(picker.pick(advanced().getItemsElements()));
            return putAction(Utils.getIndexOfElement(advanced().getSelectedItemsElements().get(0)), indexOfTargetItem, advanced().getItemsElements().size() - indexOfTargetItem);
        }

        @Override
        public OrderingList putItBefore(int index) {
            return putItBefore(ChoicePickerHelper.byIndex().index(index));
        }

        @Override
        public OrderingList putItBefore(String match) {
            return putItBefore(ChoicePickerHelper.byVisibleText().match(match));
        }

        private void singleStepMove(int difference) {
            if (difference == 0) {// no operation
            } else if (difference > 0) {
                getOrderingInteraction().down(Math.abs(difference));
            } else {
                getOrderingInteraction().up(Math.abs(difference));
            }
        }
    }

    private class OrderingInteractionImpl implements OrderingInteraction {

        private void checkIfActionPosibleAndPerform(WebElement button, int times) {
            if (!button.isEnabled()) {// button is enabled only when some items are selected.
                throw new RuntimeException("No items are selected or button is disabled.");
            }
            for (int i = 0; i < times; i++) {
                button.click();
            }
        }

        @Override
        public void bottom() {
            checkIfActionPosibleAndPerform(advanced().getBottomButtonElement(), 1);
        }

        @Override
        public void down(int times) {
            checkIfActionPosibleAndPerform(advanced().getDownButtonElement(), times);
        }

        @Override
        public void top() {
            checkIfActionPosibleAndPerform(advanced().getTopButtonElement(), 1);
        }

        @Override
        public void up(int times) {
            checkIfActionPosibleAndPerform(advanced().getUpButtonElement(), times);
        }
    }

    public abstract class AdvancedOrderingListInteractions implements VisibleComponentInteractions {

        public abstract WebElement getBottomButtonElement();

        public abstract WebElement getCaptionElement();

        public abstract WebElement getDownButtonElement();

        public abstract WebElement getHeaderElement();

        public abstract List<WebElement> getItemsElements();

        public abstract ListComponent<? extends SelectableListItem> getList();

        public abstract WebElement getContentAreaElement();

        public WebElement getRootElement() {
            return root;
        }

        public abstract List<WebElement> getSelectedItemsElements();

        protected abstract String getStyleForSelectedItem();

        public abstract WebElement getTopButtonElement();

        public abstract WebElement getUpButtonElement();

        @Override
        public boolean isVisible() {
            return Utils.isVisible(getRootElement());
        }

        public OrderingInteraction select(String visibleText, String... otherTexts) {
            ChoicePickerHelper.ByVisibleTextChoicePicker picker = ChoicePickerHelper.byVisibleText().allRulesMustPass(false).match(visibleText);
            for (String string : otherTexts) {
                picker.match(string);
            }
            return select(picker);
        }

        public OrderingInteraction select(Integer index, Integer... otherIndexes) {
            ChoicePickerHelper.ByIndexChoicePicker picker = ChoicePickerHelper.byIndex().index(index);
            for (Integer integer : otherIndexes) {
                picker.index(integer);
            }
            return select(picker);
        }

        public OrderingInteraction select(MultipleChoicePicker picker) {
            unselectAll();
            selectItems(picker.pickMultiple(advanced().getItemsElements()));
            return getOrderingInteraction();
        }
    }
}

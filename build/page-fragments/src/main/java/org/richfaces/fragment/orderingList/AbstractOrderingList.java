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

import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.common.AdvancedInteractions;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.common.picker.ChoicePicker;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.fragment.common.picker.MultipleChoicePicker;
import org.richfaces.fragment.list.ListComponent;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractOrderingList implements OrderingList, AdvancedInteractions<AbstractOrderingList.AdvancedOrderingListInteractions> {

    @Root
    private WebElement root;

    private final OrderingInteraction orderingInteraction = new OrderingInteractionImpl();
    private final PuttingSelectedItem puttingSelectedItem = new PuttingSelectedItemImpl();

    protected WebElement getRoot() {
        return root;
    }

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
        advanced().getList().getItem(picker).select(true);
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
                    orderingInteraction.top();
                    if (positionTarget != 0) {
                        singleStepMove(positionTarget);
                    }
                } else {
                    orderingInteraction.bottom();
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
                orderingInteraction.down(Math.abs(difference));
            } else {
                orderingInteraction.up(Math.abs(difference));
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

    public abstract class AdvancedOrderingListInteractions {

        public abstract WebElement getBottomButtonElement();

        public abstract WebElement getCaptionElement();

        public abstract WebElement getDownButtonElement();

        public abstract WebElement getHeaderElement();

        public abstract List<WebElement> getItemsElements();

        public abstract ListComponent<? extends SelectableListItem> getList();

        public abstract WebElement getListAreaElement();

        public abstract WebElement getRootElement();

        public abstract WebElement getScrollBoxElement();

        public abstract List<WebElement> getSelectedItemsElements();

        public abstract WebElement getTopButtonElement();

        public abstract WebElement getUpButtonElement();

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
            List<? extends SelectableListItem> items = advanced().getList().getItems(picker);
            if (!items.isEmpty()) {
                items.get(0).select(true);// deselect all and select first item
                for (int i = 1; i < items.size(); i++) {
                    items.get(i).select();// select all other items
                }
            }
            return orderingInteraction;
        }
    }
}
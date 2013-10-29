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
package org.richfaces.tests.page.fragments.impl.orderingList;

import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.common.AdvancedInteractions;
import org.richfaces.tests.page.fragments.impl.list.ListComponent;
import org.richfaces.tests.page.fragments.impl.utils.Actions;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePicker;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;
import org.richfaces.tests.page.fragments.impl.utils.picker.MultipleChoicePicker;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractOrderingList implements OrderingList, AdvancedInteractions<AbstractOrderingList.AdvancedOrderingListInteractions> {

    @Root
    private WebElement root;

    @Drone
    private WebDriver driver;

    private final AdvancedOrderingListInteractions interactions = new AdvancedOrderingListInteractions();
    private final OrderingInteraction orderingInteraction = new OrderingInteractionImpl();
    private final PuttingSelectedItem puttingSelectedItem = new PuttingSelectedItemImpl();

    @Override
    public AdvancedOrderingListInteractions advanced() {
        return interactions;
    }

    protected abstract OrderingListBodyElements getBody();

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
        unselectAll();
        selectItem(picker.pick(getBody().getItemsElements()));
        return puttingSelectedItem;
    }

    protected void selectItem(final WebElement item) {
        new Actions(driver).keyDown(Keys.CONTROL).click(item).keyUp(Keys.CONTROL).addAction(new Action() {
            @Override
            public void perform() {
                Graphene.waitGui().until().element(item).attribute("class").contains(getBody().getStyleForSelectedItem());
            }
        }).perform();
    }

    protected void selectItems(List<WebElement> list) {
        for (WebElement webElement : list) {
            selectItem(webElement);
        }
    }

    protected void unselectAll() {
        if (!getBody().getSelectedItems().isEmpty()) {
            new Actions(driver)
                .click(getBody().getItemsElements().get(0))
                .keyDown(Keys.CONTROL).click(getBody().getItemsElements().get(0))
                .keyUp(Keys.CONTROL)
                .addAction(new Action() {
                    @Override
                    public void perform() {
                        Graphene.waitGui().until().element(getBody().getItemsElements().get(0)).attribute("class").not().contains("rf-ord-sel");
                    }
                }).perform();
            if (!getBody().getSelectedItems().isEmpty()) {
                throw new RuntimeException("The unselection was not successfull.");
            }
        }
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
            int indexOfTargetItem = Utils.getIndexOfElement(picker.pick(getBody().getItemsElements())) + 1;
            return putAction(Utils.getIndexOfElement(getBody().getSelectedItems().get(0)), indexOfTargetItem, getBody().getItemsElements().size() - indexOfTargetItem);
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
            int indexOfTargetItem = Utils.getIndexOfElement(picker.pick(getBody().getItemsElements()));
            return putAction(Utils.getIndexOfElement(getBody().getSelectedItems().get(0)), indexOfTargetItem, getBody().getItemsElements().size() - indexOfTargetItem);
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
            checkIfActionPosibleAndPerform(getBody().getBottomButtonElement(), 1);
        }

        @Override
        public void down(int times) {
            checkIfActionPosibleAndPerform(getBody().getDownButtonElement(), times);
        }

        @Override
        public void top() {
            checkIfActionPosibleAndPerform(getBody().getTopButtonElement(), 1);
        }

        @Override
        public void up(int times) {
            checkIfActionPosibleAndPerform(getBody().getUpButtonElement(), times);
        }
    }

    public class AdvancedOrderingListInteractions {

        public WebElement getBottomButtonElement() {
            return getBody().getBottomButtonElement();
        }

        public WebElement getCaptionElement() {
            return getBody().getCaptionElement();
        }

        public WebElement getDownButtonElement() {
            return getBody().getDownButtonElement();
        }

        public WebElement getHeaderElement() {
            return getBody().getHeaderElement();
        }

        public List<WebElement> getItemsElements() {
            return getBody().getItemsElements();
        }

        public ListComponent<? extends SelectableListItem> getList() {
            return getBody().getList();
        }

        public WebElement getListAreaElement() {
            return getBody().getListAreaElement();
        }

        public WebElement getRootElement() {
            return getBody().getRootElement();
        }

        public List<WebElement> getSelectedItemsElements() {
            return getBody().getSelectedItems();
        }

        public WebElement getTopButtonElement() {
            return getBody().getTopButtonElement();
        }

        public WebElement getUpButtonElement() {
            return getBody().getUpButtonElement();
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
            selectItems(picker.pickMultiple(getBody().getItemsElements()));
            return orderingInteraction;
        }
    }

    public interface OrderingListBodyElements {

        WebElement getBottomButtonElement();

        WebElement getCaptionElement();

        WebElement getDownButtonElement();

        WebElement getHeaderElement();

        List<WebElement> getItemsElements();

        ListComponent<? extends SelectableListItem> getList();

        WebElement getListAreaElement();

        WebElement getRootElement();

        List<WebElement> getSelectedItems();

        WebElement getTopButtonElement();

        WebElement getUpButtonElement();

        String getStyleForSelectedItem();
    }
}

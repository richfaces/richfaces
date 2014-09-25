/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.fragment.pickList;

import java.util.Collections;
import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Actions;
import org.richfaces.fragment.common.AdvancedVisibleComponentIteractions;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.common.VisibleComponentInteractions;
import org.richfaces.fragment.common.picker.ChoicePicker;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.fragment.common.picker.MultipleChoicePicker;
import org.richfaces.fragment.list.AbstractListComponent;
import org.richfaces.fragment.list.ListComponent;
import org.richfaces.fragment.orderingList.AbstractOrderingList;
import org.richfaces.fragment.orderingList.AbstractSelectableListItem;
import org.richfaces.fragment.orderingList.OrderingList;
import org.richfaces.fragment.orderingList.SelectableListItem;

public class RichFacesPickList implements PickList, AdvancedVisibleComponentIteractions<RichFacesPickList.AdvancedPickListInteractions> {

    @Root
    private WebElement root;
    @Drone
    private WebDriver driver;

    @FindBy(className = "rf-pick-add-all")
    private WebElement addAllButtonElement;
    @FindBy(className = "rf-pick-add")
    private WebElement addButtonElement;
    @FindBy(className = "rf-pick-rem-all")
    private WebElement removeAllButtonElement;
    @FindBy(className = "rf-pick-rem")
    private WebElement removeButtonElement;
    @FindBy(css = "[id$='SourceItems'] > .rf-pick-sel")
    private List<WebElement> selectedSourceListItems;
    @FindBy(css = "[id$='SourceItems'] > *")
    private List<WebElement> sourceListItems;
    @FindBy(css = "[id$='SourceItems']")
    private SelectableListImpl sourceList;
    @FindBy(css = "[id$='Target']")
    private OrderingListInPickList targetList;
    @FindBy(className = "rf-pick-lst-scrl")
    private WebElement contentAreaElement;
    @FindBy(className = "rf-pick-src-cptn")
    private WebElement sourceCaptionElement;
    @FindBy(css = "thead.rf-pick-lst-hdr > tr.rf-pick-hdr")
    private WebElement sourceHeaderElement;

    private final AdvancedPickListInteractions interactions = new AdvancedPickListInteractions();

    @Override
    public AdvancedPickListInteractions advanced() {
        return interactions;
    }

    private void clickAddAllButton() {
        advanced().getAddAllButtonElement().click();
    }

    private void clickAddButton() {
        advanced().getAddButtonElement().click();
    }

    private void clickRemoveAllButton() {
        advanced().getRemoveAllButtonElement().click();
    }

    private void clickRemoveButton() {
        advanced().getRemoveButtonElement().click();
    }

    @Override
    public PickList add(ChoicePicker picker) {
        selectAndAdd(picker);
        return this;
    }

    @Override
    public PickList add(String match) {
        return add(ChoicePickerHelper.byVisibleText().match(match));
    }

    @Override
    public PickList add(int index) {
        return add(ChoicePickerHelper.byIndex().index(index));
    }

    @Override
    public PickList addMultiple(MultipleChoicePicker picker) {
        selectAndAddMultiple(picker);
        return this;
    }

    @Override
    public PickList addAll() {
        clickAddAllButton();
        return this;
    }

    @Override
    public PickList remove(ChoicePicker picker) {
        selectAndRemove(picker);
        return this;
    }

    @Override
    public PickList remove(String match) {
        return remove(ChoicePickerHelper.byVisibleText().match(match));
    }

    @Override
    public PickList remove(int index) {
        return remove(ChoicePickerHelper.byIndex().index(index));
    }

    @Override
    public PickList removeMultiple(MultipleChoicePicker picker) {
        selectAndRemoveMultiple(picker);
        return this;
    }

    @Override
    public PickList removeAll() {
        clickRemoveAllButton();
        return this;
    }

    private void selectAndAdd(ChoicePicker picker) {
        unselectAll(advanced().getSelectedSourceListItemsElements());
        selectItem(picker.pick(advanced().getSourceListItemsElements()));
        clickAddButton();
    }

    private void selectAndAddMultiple(MultipleChoicePicker picker) {
        unselectAll(advanced().getSelectedSourceListItemsElements());
        for (WebElement item : picker.pickMultiple(advanced().getSourceListItemsElements())) {
            selectItem(item);
        }
        clickAddButton();
    }

    private void selectAndRemove(ChoicePicker picker) {
        unselectAll(advanced().getSelectedTargetListItemsElements());
        selectItem(picker.pick(advanced().getTargetListItemsElements()));
        clickRemoveButton();
    }

    private void selectAndRemoveMultiple(MultipleChoicePicker picker) {
        unselectAll(advanced().getSelectedTargetListItemsElements());
        for (WebElement item : picker.pickMultiple(advanced().getTargetListItemsElements())) {
            selectItem(item);
        }
        clickRemoveButton();
    }

    private void selectItem(final WebElement item) {
        new Actions(driver)
            .keyDown(Keys.CONTROL).click(item).keyUp(Keys.CONTROL)
            .addAction(new Action() {
                @Override
                public void perform() {
                    Graphene.waitGui().until().element(item).attribute("class").contains(OrderingListInPickList.SELECTED_ITEM_CLASS);
                }
            })
            .perform();
    }

    protected void unselectAll(List<WebElement> list) {
        if (!list.isEmpty()) {
            new Actions(driver)
                .click(list.get(0))
                .keyDown(Keys.CONTROL).click(list.get(0)).keyUp(Keys.CONTROL)
                .perform();
            if (!list.isEmpty()) {
                throw new RuntimeException("The unselection was not successfull.");
            }
        }
    }

    public static class OrderingListInPickList extends AbstractOrderingList {

        private static final String SELECTED_ITEM_CLASS = "rf-pick-sel";

        @FindBy(css = "button.rf-ord-dn")
        private WebElement downButtonElement;
        @FindBy(css = "button.rf-ord-up-tp")
        private WebElement topButtonElement;
        @FindBy(css = "button.rf-ord-dn-bt")
        private WebElement bottomButtonElement;
        @FindBy(css = "button.rf-ord-up")
        private WebElement upButtonElement;
        @FindBy(css = "thead.rf-pick-lst-hdr > tr.rf-pick-hdr")
        private WebElement headerElement;
        @FindBy(className = "rf-pick-tgt-cptn")
        private WebElement captionElement;

        @FindBy(className = "rf-pick-opt")
        private List<WebElement> items;
        @FindBy(className = SELECTED_ITEM_CLASS)
        private List<WebElement> selectedItems;
        @FindBy(className = "rf-pick-lst-scrl")
        private WebElement contentAreaElement;
        @FindBy(css = "[id$='TargetItems']")
        private SelectableListImpl list;

        private final AdvancedOrderingListInPickListInteractions interactions = new AdvancedOrderingListInPickListInteractions();

        @Override
        public AdvancedOrderingListInteractions advanced() {
            return interactions;
        }

        public class AdvancedOrderingListInPickListInteractions extends AdvancedOrderingListInteractions {

            @Override
            public WebElement getBottomButtonElement() {
                return bottomButtonElement;
            }

            @Override
            public WebElement getCaptionElement() {
                return captionElement;
            }

            @Override
            public WebElement getDownButtonElement() {
                return downButtonElement;
            }

            @Override
            public WebElement getHeaderElement() {
                return headerElement;
            }

            @Override
            public List<WebElement> getItemsElements() {
                return Collections.unmodifiableList(items);
            }

            @Override
            public ListComponent<? extends SelectableListItem> getList() {
                return list;
            }

            @Override
            public WebElement getContentAreaElement() {
                return contentAreaElement;
            }

            @Override
            public List<WebElement> getSelectedItemsElements() {
                return Collections.unmodifiableList(selectedItems);
            }

            @Override
            protected String getStyleForSelectedItem() {
                return SELECTED_ITEM_CLASS;
            }

            @Override
            public WebElement getTopButtonElement() {
                return topButtonElement;
            }

            @Override
            public WebElement getUpButtonElement() {
                return upButtonElement;
            }
        }
    }

    public static class SelectableListItemImpl extends AbstractSelectableListItem {

        private static final String SELECTED_ITEM_CLASS = "rf-pick-sel";

        @Override
        protected String getStyleClassForSelectedItem() {
            return SELECTED_ITEM_CLASS;
        }
    }

    public class AdvancedPickListInteractions implements VisibleComponentInteractions {

        public WebElement getAddAllButtonElement() {
            return addAllButtonElement;
        }

        public WebElement getAddButtonElement() {
            return addButtonElement;
        }

        public WebElement getBottomButtonElement() {
            return _getOrderTargetList().advanced().getBottomButtonElement();
        }

        public WebElement getSourceCaptionElement() {
            return sourceCaptionElement;
        }

        public WebElement getTargetCaptionElement() {
            return _getOrderTargetList().advanced().getCaptionElement();
        }

        public WebElement getDownButtonElement() {
            return _getOrderTargetList().advanced().getDownButtonElement();
        }

        public WebElement getSourceHeaderElement() {
            return sourceHeaderElement;
        }

        public WebElement getTargetHeaderElement() {
            return _getOrderTargetList().advanced().getHeaderElement();
        }

        public WebElement getSourceListContentAreaElement() {
            return contentAreaElement;
        }

        public WebElement getTargetListContentAreaElement() {
            return _getOrderTargetList().advanced().getContentAreaElement();
        }

        public WebElement getRemoveAllButtonElement() {
            return removeAllButtonElement;
        }

        public WebElement getRemoveButtonElement() {
            return removeButtonElement;
        }

        public List<WebElement> getSelectedSourceListItemsElements() {
            return selectedSourceListItems;
        }

        public List<WebElement> getSourceListItemsElements() {
            return sourceListItems;
        }

        public List<WebElement> getSelectedTargetListItemsElements() {
            return _getOrderTargetList().advanced().getSelectedItemsElements();
        }

        public List<WebElement> getTargetListItemsElements() {
            return _getOrderTargetList().advanced().getItemsElements();
        }

        public WebElement getRootElement() {
            return root;
        }

        public ListComponent<? extends SelectableListItem> getSourceList() {
            return sourceList;
        }

        public ListComponent<? extends SelectableListItem> getTargetList() {
            return _getOrderTargetList().advanced().getList();
        }

        public WebElement getTopButtonElement() {
            return _getOrderTargetList().advanced().getTopButtonElement();
        }

        public WebElement getUpButtonElement() {
            return _getOrderTargetList().advanced().getUpButtonElement();
        }

        protected OrderingListInPickList _getOrderTargetList() {
            return targetList;
        }

        /**
         * This method return element which has operations over the orderable target list.
         * If the list is not orderable than some Exception is thrown.
         */
        public OrderingList getOrderTargetList() {
            return _getOrderTargetList();
        }

        public boolean isVisible() {
            return Utils.isVisible(getRootElement());
        }
    }

    public static class SelectableListImpl extends AbstractListComponent<SelectableListItemImpl> {
    }
}

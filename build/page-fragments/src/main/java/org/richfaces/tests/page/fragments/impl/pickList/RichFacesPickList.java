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
package org.richfaces.tests.page.fragments.impl.pickList;

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
import org.richfaces.tests.page.fragments.impl.common.AdvancedInteractions;
import org.richfaces.tests.page.fragments.impl.list.AbstractListComponent;
import org.richfaces.tests.page.fragments.impl.list.ListComponent;
import org.richfaces.tests.page.fragments.impl.orderingList.AbstractOrderingList;
import org.richfaces.tests.page.fragments.impl.orderingList.AbstractOrderingList.OrderingListBodyElements;
import org.richfaces.tests.page.fragments.impl.orderingList.AbstractSelectableListItem;
import org.richfaces.tests.page.fragments.impl.orderingList.OrderingList;
import org.richfaces.tests.page.fragments.impl.orderingList.SelectableListItem;
import org.richfaces.tests.page.fragments.impl.utils.Actions;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePicker;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;
import org.richfaces.tests.page.fragments.impl.utils.picker.MultipleChoicePicker;

public class RichFacesPickList implements PickList, AdvancedInteractions<RichFacesPickList.AdvancedPickListInteractions> {

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
    @FindBy(css = "[id$='TargetItems'] > .rf-pick-sel")
    private List<WebElement> selectedTargetListItems;
    @FindBy(css = "[id$='TargetItems'] > *")
    private List<WebElement> targetListItems;
    @FindBy(css = "[id$='SourceItems']")
    private SelectableListImpl sourceList;
    @FindBy(css = "[id$='Target']")
    private OrderingListInPickList targetList;
    @FindBy(className = "rf-pick-lst-scrl")
    private WebElement listAreaElement;
    @FindBy(className = "rf-pick-src-cptn")
    private WebElement captionElement;
    @FindBy(css = "thead.rf-pick-lst-hdr > tr.rf-pick-hdr")
    private WebElement headerElement;

    private final AdvancedPickListInteractions interactions = new AdvancedPickListInteractions();

    @Override
    public AdvancedPickListInteractions advanced() {
        return interactions;
    }

    private void clickAddAllButton() {
        addAllButtonElement.click();
    }

    private void clickAddButton() {
        addButtonElement.click();
    }

    private void clickRemoveAllButton() {
        removeAllButtonElement.click();
    }

    private void clickRemoveButton() {
        removeButtonElement.click();
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
        unselectAll(selectedSourceListItems);
        selectItem(picker.pick(sourceListItems));
        clickAddButton();
    }

    private void selectAndAddMultiple(MultipleChoicePicker picker) {
        unselectAll(selectedSourceListItems);
        for (WebElement item : picker.pickMultiple(sourceListItems)) {
            selectItem(item);
        }
        clickAddButton();
    }

    private void selectAndRemove(ChoicePicker picker) {
        unselectAll(selectedTargetListItems);
        selectItem(picker.pick(targetListItems));
        clickRemoveButton();
    }

    private void selectAndRemoveMultiple(MultipleChoicePicker picker) {
        unselectAll(selectedTargetListItems);
        for (WebElement item : picker.pickMultiple(targetListItems)) {
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

    public static class OrderingListInPickList extends AbstractOrderingList implements OrderingListBodyElements {

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
        private WebElement listAreaElement;
        @FindBy(css = "[id$='TargetItems']")
        private SelectableListImpl list;

        @Override
        protected OrderingListBodyElements getBody() {
            return this;
        }

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
        public WebElement getListAreaElement() {
            return listAreaElement;
        }

        @Override
        public WebElement getRootElement() {
            return getRoot();
        }

        @Override
        public List<WebElement> getSelectedItems() {
            return Collections.unmodifiableList(selectedItems);
        }

        @Override
        public String getStyleForSelectedItem() {
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

    public static class SelectableListItemImpl extends AbstractSelectableListItem {

        private static final String SELECTED_ITEM_CLASS = "rf-pick-sel";

        @Override
        protected String getStyleClassForSelectedItem() {
            return SELECTED_ITEM_CLASS;
        }
    }

    public class AdvancedPickListInteractions {

        public WebElement getAddAllButtonElement() {
            return addAllButtonElement;
        }

        public WebElement getAddButtonElement() {
            return addButtonElement;
        }

        public WebElement getBottomButtonElement() {
            return targetList.getBottomButtonElement();
        }

        public WebElement getSourceCaptionElement() {
            return captionElement;
        }

        public WebElement getTargetCaptionElement() {
            return targetList.getCaptionElement();
        }

        public WebElement getDownButtonElement() {
            return targetList.getDownButtonElement();
        }

        public WebElement getSourceHeaderElement() {
            return headerElement;
        }

        public WebElement getTargetHeaderElement() {
            return targetList.getHeaderElement();
        }

        public WebElement getSourceListAreaElement() {
            return listAreaElement;
        }

        public WebElement getTargetListAreaElement() {
            return targetList.getListAreaElement();
        }

        public WebElement getRemoveAllButtonElement() {
            return removeAllButtonElement;
        }

        public WebElement getRemoveButtonElement() {
            return removeButtonElement;
        }

        public WebElement getRootElement() {
            return root;
        }

        public ListComponent<? extends SelectableListItem> getSourceList() {
            return sourceList;
        }

        public ListComponent<? extends SelectableListItem> getTargetList() {
            return targetList.getList();
        }

        public WebElement getTopButtonElement() {
            return targetList.getTopButtonElement();
        }

        public WebElement getUpButtonElement() {
            return targetList.getUpButtonElement();
        }

        /**
         * Operations over the orderable target list.
         * If the list is not orderable than some Exception is thrown.
         */
        public OrderingList orderTargetList() {
            return targetList;
        }
    }

    public static class SelectableListImpl extends AbstractListComponent<SelectableListItemImpl> {
    }
}

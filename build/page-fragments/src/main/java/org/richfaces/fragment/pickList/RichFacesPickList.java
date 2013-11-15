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
package org.richfaces.fragment.pickList;

import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.AdvancedInteractions;
import org.richfaces.fragment.common.picker.ChoicePicker;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.fragment.common.picker.MultipleChoicePicker;
import org.richfaces.fragment.list.ListComponent;
import org.richfaces.fragment.list.ListItem;
import org.richfaces.fragment.orderingList.OrderingList;
import org.richfaces.fragment.orderingList.RichFacesOrderingList;
import org.richfaces.fragment.orderingList.RichFacesOrderingList.SelectableListImpl;
import org.richfaces.fragment.orderingList.SelectableListItem;

public class RichFacesPickList implements PickList, AdvancedInteractions<RichFacesPickList.AdvancedPickListInteractions> {

    @Root
    private WebElement root;
    @Drone
    private WebDriver driver;

    @FindBy(className = "btn-add-all")
    private WebElement addAllButtonElement;
    @FindBy(className = "btn-add")
    private WebElement addButtonElement;
    @FindBy(className = "btn-remove-all")
    private WebElement removeAllButtonElement;
    @FindBy(className = "btn-remove")
    private WebElement removeButtonElement;
    @FindBy(css = ".source .ui-selected")
    private List<WebElement> selectedSourceListItems;
    @FindBy(css = ".source .ui-selectee")
    private List<WebElement> sourceListItems;
    @FindBy(css = ".target ." + SELECTED_ITEM_CLASS)
    private List<WebElement> selectedTargetListItems;
    @FindBy(css = ".target .ui-selectee")
    private List<WebElement> targetListItems;
    @FindBy(css = ".source-wrapper .ui-sortable.ui-selectable")
    private SelectableListImpl sourceList;
    @FindBy(className = "target-wrapper")
    private RichFacesOrderingList targetList;
    @FindBy(css = ".source")
    private WebElement listAreaElement;
    @FindBy(css = ".source.header")
    private WebElement sourceCaptionElement;
    @FindBy(css = ".target.header")
    private WebElement targetCaptionElement;
    @FindBy(tagName = "thead")
    private WebElement headerElement;
    @FindByJQuery(".scroll-box:eq(0)")
    private WebElement scrollBoxWithSourceList;
    @FindBy(className = "inner")
    private WebElement pickListInteractionPartElement;

    private static final String DISABLED_ITEM_CLASS = "ui-disabled";
    private static final String SELECTED_ITEM_CLASS = "ui-selected";

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
        select(picker, sourceList);
        clickAddButton();
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
        select(picker, sourceList);
        clickAddButton();
        return this;
    }

    @Override
    public PickList addAll() {
        clickAddAllButton();
        return this;
    }

    @Override
    public PickList remove(ChoicePicker picker) {
        select(picker, targetList.advanced().getList());
        clickRemoveButton();
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
        select(picker, targetList.advanced().getList());
        clickRemoveButton();
        return this;
    }

    @Override
    public PickList removeAll() {
        clickRemoveAllButton();
        return this;
    }

    private void select(ChoicePicker picker, ListComponent<? extends SelectableListItem> list) {
        list.getItem(picker).select(true);
    }

    private void select(MultipleChoicePicker picker, ListComponent<? extends SelectableListItem> list) {
        List<? extends SelectableListItem> items = targetList.advanced().getList().getItems(picker);
        if (!items.isEmpty()) {
            items.get(0).select(true);// deselect all and select first item
            for (int i = 1; i < items.size(); i++) {
                items.get(i).select();// select all other items
            }
        }
    }

    public class AdvancedPickListInteractions {

        public WebElement getPickListInteractionPartElement() {
            return pickListInteractionPartElement;
        }

        public WebElement getAddAllButtonElement() {
            return addAllButtonElement;
        }

        public WebElement getAddButtonElement() {
            return addButtonElement;
        }

        public WebElement getBottomButtonElement() {
            return targetList.advanced().getBottomButtonElement();
        }

        public WebElement getSourceCaptionElement() {
            return sourceCaptionElement;
        }

        public WebElement getTargetCaptionElement() {
            return targetCaptionElement;
        }

        public WebElement getDownButtonElement() {
            return targetList.advanced().getDownButtonElement();
        }

        public WebElement getSourceHeaderElement() {
            return headerElement;
        }

        public WebElement getTargetHeaderElement() {
            return targetList.advanced().getHeaderElement();
        }

        public WebElement getSourceListAreaElement() {
            return listAreaElement;
        }

        public WebElement getTargetListAreaElement() {
            return targetList.advanced().getListAreaElement();
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
            return targetList.advanced().getList();
        }

        public WebElement getTopButtonElement() {
            return targetList.advanced().getTopButtonElement();
        }

        public WebElement getUpButtonElement() {
            return targetList.advanced().getUpButtonElement();
        }

        /**
         * Figures out whether the whole pickList is disabled. All required elements has to be disabled.
         *
         * @return true if whole pickList is disabled, false otherwise
         */
        public boolean isDisabled() {
            return areAllButtonsDisabled() && areAllGivenItemsDisabled(getSourceList().getItems())
                && areAllGivenItemsDisabled(getTargetList().getItems());
        }

        /**
         * Figures out whether the given item is selected.
         *
         * @param item
         * @return true if the given item is currently selected, false otherwise
         */
        public boolean isItemSelected(ListItem item) {
            String classAttr = item.getRootElement().getAttribute("class");
            return classAttr != null && classAttr.contains(SELECTED_ITEM_CLASS);
        }

        /**
         * Gets the current computed height of the this pickList sourceList/targetList (they are equal).
         *
         * @return
         * @throws NumberFormatException when the height property is not set
         */
        public double getHeight() {
            return Double.valueOf(scrollBoxWithSourceList.getCssValue("height").replace("px", ""));
        }

        /**
         * Gets the current computed max-height of the this pickList sourceList/targetList (they are equal).
         *
         * @return
         * @throws NumberFormatException when the max-height property is not set
         */
        public double getMaxHeight() {
            return Double.valueOf(scrollBoxWithSourceList.getCssValue("max-height").replace("px", ""));
        }

        /**
         * Gets the current computed min-height of the this pickList sourceList/targetList (they are equal).
         *
         * @return
         * @throws NumberFormatException when the min-height property is not set
         */
        public double getMinHeight() {
            return Double.valueOf(scrollBoxWithSourceList.getCssValue("min-height").replace("px", ""));
        }

        private boolean areAllGivenItemsDisabled(List<? extends SelectableListItem> items) {
            boolean result = true;
            for (SelectableListItem sourceItem : items) {
                String classAttr = sourceItem.getRootElement().getAttribute("class");
                if (classAttr == null || !classAttr.contains(DISABLED_ITEM_CLASS)) {
                    result = false;
                }
            }
            return result;
        }

        private boolean areAllButtonsDisabled() {
            boolean result = true;
            if (getAddAllButtonElement().isEnabled()) {
                result = false;
            } else if (getAddButtonElement().isEnabled()) {
                result = false;
            } else if (getRemoveAllButtonElement().isEnabled()) {
                result = false;
            } else if (getRemoveButtonElement().isEnabled()) {
                result = false;
            } else if (getUpButtonElement().isEnabled()) {
                result = false;
            } else if (getTopButtonElement().isEnabled()) {
                result = false;
            } else if (getDownButtonElement().isEnabled()) {
                result = false;
            } else if (getBottomButtonElement().isEnabled()) {
                result = false;
            }
            return result;
        }

        /**
         * Operations over the orderable target list. If the list is not orderable than some Exception is thrown.
         */
        public OrderingList orderTargetList() {
            return targetList;
        }
    }
}

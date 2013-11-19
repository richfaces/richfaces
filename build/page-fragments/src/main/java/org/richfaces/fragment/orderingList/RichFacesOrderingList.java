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

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.list.AbstractListComponent;
import org.richfaces.fragment.list.ListComponent;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesOrderingList extends AbstractOrderingList {

    private static final String SELECTED_ITEM_CLASS = "ui-selected";

    @FindBy(className = "btn-down")
    private WebElement downButtonElement;
    @FindBy(className = "btn-first")
    private WebElement topButtonElement;
    @FindBy(className = "btn-last")
    private WebElement bottomButtonElement;
    @FindBy(className = "btn-up")
    private WebElement upButtonElement;

    @FindBy(className = "header")
    private WebElement captionElement;
    @FindBy(tagName = "thead")
    private WebElement headerElement;
    @FindBy(className = "list")
    private WebElement listAreaElement;
    @FindBy(className = "scroll-box")
    private WebElement scrollBoxElement;

    @FindBy(className = "ui-selectee")
    private List<WebElement> itemsElements;
    @FindBy(className = "ui-disabled")
    private List<WebElement> disabledItemsElements;
    @FindBy(className = SELECTED_ITEM_CLASS)
    private List<WebElement> selectedItemsElements;

    @FindBy(css = ".ui-sortable.ui-selectable")
    private SelectableListImpl list;

    private final AdvancedOrderingListInteractions interactions = new AdvancedOrderingListInteractionsImpl();

    @Override
    public AdvancedOrderingListInteractions advanced() {
        return interactions;
    }

    private class AdvancedOrderingListInteractionsImpl extends AdvancedOrderingListInteractions {

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
            return (itemsElements.isEmpty() ? disabledItemsElements : itemsElements);
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
        public WebElement getScrollBoxElement() {
            return scrollBoxElement;
        }

        @Override
        public List<WebElement> getSelectedItemsElements() {
            return selectedItemsElements;
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

        @Override
        protected String getStyleClassForSelectedItem() {
            return SELECTED_ITEM_CLASS;
        }
    }

    public static class SelectableListImpl extends AbstractListComponent<SelectableListItemImpl> {
    }
}
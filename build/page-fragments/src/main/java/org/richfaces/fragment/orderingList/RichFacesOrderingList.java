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

    @FindBy(css = "button.rf-ord-dn")
    private WebElement downButtonElement;
    @FindBy(css = "button.rf-ord-up-tp")
    private WebElement topButtonElement;
    @FindBy(css = "button.rf-ord-dn-bt")
    private WebElement bottomButtonElement;
    @FindBy(css = "button.rf-ord-up")
    private WebElement upButtonElement;

    @FindBy(className = "rf-ord-cptn")
    private WebElement captionElement;
    @FindBy(css = "thead.rf-ord-lst-hdr > tr.rf-ord-hdr")
    private WebElement headerElement;
    @FindBy(className = "rf-ord-lst-scrl")
    private WebElement contentAreaElement;

    @FindBy(className = "rf-ord-opt")
    private List<WebElement> items;
    @FindBy(className = "rf-ord-sel")
    private List<WebElement> selectedItemsElements;

    @FindBy(css = "div.rf-ord-lst-scrl [id$=Items]")
    private SelectableListImpl list;

    private final AdvancedRichOrderingListInteractions interactions = new AdvancedRichOrderingListInteractions();

    @Override
    public AdvancedRichOrderingListInteractions advanced() {
        return interactions;
    }

    public class AdvancedRichOrderingListInteractions extends AdvancedOrderingListInteractions {

        private static final String SELECTED_ITEM_CLASS = "rf-ord-sel";

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
            return items;
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
            return selectedItemsElements;
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

    public static class SelectableListItemImpl extends AbstractSelectableListItem {

        private static final String styleClass = "rf-ord-sel";

        @Override
        protected String getStyleClassForSelectedItem() {
            return styleClass;
        }
    }

    public static class SelectableListImpl extends AbstractListComponent<SelectableListItemImpl> {
    }
}

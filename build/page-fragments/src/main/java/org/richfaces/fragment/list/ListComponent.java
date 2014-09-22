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
package org.richfaces.fragment.list;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.richfaces.fragment.common.picker.ChoicePicker;
import org.richfaces.fragment.common.picker.MultipleChoicePicker;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @param <T> type extending ListItem
 */
public interface ListComponent<T extends ListItem> {

    /**
     * Gets the item on the given <code>index</code>.
     *
     * @param index index of the item to be returned
     * @return      item at index or null
     */
    T getItem(int index);

    /**
     * Gets the item which text representation is exact match with the given param <code>text</code>
     *
     * @param  text  the text according to which the item will be returned
     * @return       first item which matches given text or null
     */
    T getItem(String text);

    /**
     * Gets item found by <code>picker</code>.
     *
     * @param picker for picking from the items
     * @return found item or null
     * @see org.richfaces.fragment.common.picker.ChoicePickerHelper.ByIndexChoicePicker
     * @see org.richfaces.fragment.common.picker.ChoicePickerHelper.ByVisibleTextChoicePicker

     */
    T getItem(ChoicePicker picker);

    /**
     * Gets all items from this list.
     *
     * @return all items in list
     */
    List<T> getItems();

    /**
     * Gets multiple items accoriding to the rules given in <code>picker</code> param.
     *
     * @param picker for picking from list
     * @return all items that returns picker
     * @see MultipleChoicePicker
     */
    List<T> getItems(MultipleChoicePicker picker);

    /**
     * Gets the root of this component.
     *
     * @return the root element of this component
     */
    WebElement getRootElement();

    /**
     * Founds out whether the list is empty or not.
     *
     * @return true if size() == 0, false otherwise
     */
    boolean isEmpty();

    /**
     * Gets the size of this list component.
     *
     * @return size of the list
     */
    int size();
}

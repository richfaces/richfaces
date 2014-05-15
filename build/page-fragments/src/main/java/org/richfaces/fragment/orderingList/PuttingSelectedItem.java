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
package org.richfaces.fragment.orderingList;

import org.richfaces.fragment.common.picker.ChoicePicker;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface PuttingSelectedItem {

    /**
     * Puts previously selected item before another item at @index.
     *
     * @param index index of the item
     */
    OrderingList putItBefore(int index);

    /**
     * Puts previously selected item before another item which text matches @match.
     *
     * @param match text to match
     */
    OrderingList putItBefore(String match);

    /**
     * Puts previously selected item before another item picked by @picker.
     *
     * @param picker for picking from the choices
     * @see org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper.ByIndexChoicePicker
     * @see org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper.ByVisibleTextChoicePicker
     */
    OrderingList putItBefore(ChoicePicker picker);

    /**
     * Puts previously selected item after another item at @index.
     *
     * @param index index of the item
     */
    OrderingList putItAfter(int index);

    /**
     * Puts previously selected item after another item which text matches @match.
     *
     * @param match text to match
     */
    OrderingList putItAfter(String match);

    /**
     * Puts previously selected item after another item picked by @picker.
     *
     * @param picker for picking from the choices
     * @see org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper.ByIndexChoicePicker
     * @see org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper.ByVisibleTextChoicePicker
     */
    OrderingList putItAfter(ChoicePicker picker);
}

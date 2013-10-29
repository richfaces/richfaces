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
package org.richfaces.tests.page.fragments.impl.contextMenu;

import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePicker;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface PopupMenu {

    /**
     * Selects the menu item according to the rules defined in the <code>picker</code> param.
     *
     * @param picker for defining the rules by which the item will be selected
     * @throws IllegalArgumentException if there is no such element which satisfies the rules given by <code>picker</code>
     */
    void selectItem(ChoicePicker picker);

    /**
     * Selects the menu item according to the exact match of its text representation with passed <code>header</code> param.
     *
     * @param header text representation of the menu item to be selected
     * @throws IllegalArgumentException if there is no such element with text representation matching the given <code>text</code>
     */
    void selectItem(String header);

    /**
     * Selects the menu item according to its index in the list of all menu items.
     *
     * Items are indexed from 0. A top menu item (the one which is the closest to the root of the popup menu component) has index 0.
     *
     * @param index zero based index of the item to be selected
     * @throws IllegalArgumentException if there is no such element with index <code>index</code>
     */
    void selectItem(int index);

    /**
     * Selects the menu item according to the rules defined in the <code>picker</code> param.
     *
     * @param picker for defining the rules by which the item will be selected
     * @param target for defining the popup menu which will be invoked and from which the item will be selected
     * @throws IllegalArgumentException if there is no such element which satisfies the rules given by <code>picker</code>
     */
    void selectItem(ChoicePicker picker, WebElement target);

    /**
     * Selects the menu item according to the exact match of its text representation with passed <code>header</code> param.
     *
     * @param header text representation of the menu item to be selected
     * @param target for defining the popup menu which will be invoked and from which the item will be selected
     * @throws IllegalArgumentException if there is no such element with text representation matching the given <code>text</code>
     */
    void selectItem(String header, WebElement target);

    /**
     * Selects the menu item according to its index in the list of all menu items.
     *
     * Items are indexed from 0. A top menu item (the one which is the closest to the root of the popup menu component) has index 0.
     *
     * @param index zero based index of the item to be selected
     * @param target for defining the popup menu which will be invoked and from which the item will be selected
     * @throws IllegalArgumentException if there is no such element with index <code>index</code>
     */
    void selectItem(int index, WebElement target);
}

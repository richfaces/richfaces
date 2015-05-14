/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
package org.richfaces.fragment.contextMenu;

import org.richfaces.fragment.common.picker.ChoicePicker;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface PopupMenuGroup {

    /**
     * Selects the menu item from this menu or menu group according to the rules defined in the <code>picker</code> param.
     * Opens menu or sub menu from visible menu, if it is not visible.
     *
     * @param picker for defining the rules by which the item will be selected
     * @throws IllegalArgumentException if there is no such element which satisfies the rules given by <code>picker</code>
     */
    void selectItem(ChoicePicker picker);

    /**
     * Selects the menu item from this menu or menu group according to the exact match of its text representation with passed <code>header</code> param.
     * Opens menu or sub menu from visible menu, if it is not visible.
     *
     * @param header text representation of the menu item to be selected
     * @throws IllegalArgumentException if there is no such element with text representation matching the given <code>header</code>
     */
    void selectItem(String header);

    /**
     * Selects the menu item from this menu or menu group according to its index in the list of all menu items.
     * Opens menu or sub menu from visible menu, if it is not visible.
     *
     * Items are indexed from 0. A top menu item (the one which is the closest to the root of the popup menu component) has index 0.
     *
     * @param index zero based index of the item to be selected
     * @throws IllegalArgumentException if there is no such element with index <code>index</code>
     */
    void selectItem(int index);

    /**
     * Expands group or sub group from this menu, which satisfies the rules given in <code>picker</code> param.
     * Opens menu or sub menu from visible menu, if it is not visible.
     *
     * @param picker object to define which group should be expanded
     * @throws IllegalArgumentException if there is no such element which satisfies the rules given by <code>picker</code>
     * @return the group which was expanded
     */
    PopupMenuGroup expandGroup(ChoicePicker picker);

    /**
     * Expands group or sub group from this menu, which text representation matches the given <code>header</code> param.
     * Opens menu or sub menu from visible menu, if it is not visible.
     *
     * @param header the header of the group which should be expanded
     * @throws IllegalArgumentException if there is no such element with text representation matching the given <code>header</code>
     * @return the group which was expanded
     */
    PopupMenuGroup expandGroup(String header);

    /**
     * Expands group or sub group from this menu, which index equals the given <code>index</code> param.
     * Opens menu or sub menu from visible menu, if it is not visible.
     *
     * @param index zero based index of the group to expand
     * @throws IllegalArgumentException if there is no such element with index <code>index</code>
     * @return the group which was expanded
     */
    PopupMenuGroup expandGroup(int index);
}

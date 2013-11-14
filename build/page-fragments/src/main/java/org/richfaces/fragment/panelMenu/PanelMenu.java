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

package org.richfaces.fragment.panelMenu;

import org.richfaces.fragment.common.picker.ChoicePicker;

public interface PanelMenu {

    /**
     * Expands group which satisfies the rules given in <code>picker</code> object.
     *
     * Note that group need to be enabled and visible before calling this method.
     *
     * @param  picker                   object to define which group should be expanded
     * @return                          the group which was expanded
     * @throws IllegalArgumentException if group is not enabled or visible, or does not exist
     */
    PanelMenuGroup expandGroup(ChoicePicker picker);

    /**
     * Expands group which text representation matches the given <code>header</code>.
     *
     * Note that group need to be enabled and visible before calling this method.
     *
     * @param  header                   the header of the group which should be expanded
     * @return                          the group which was expanded
     * @throws IllegalArgumentException if group is not enabled or visible, or does not exist
     */
    PanelMenuGroup expandGroup(String header);

    /**
     * Expands group which index equals the given <code>index</code>.
     *
     * Note that group need to be enabled and visible before calling this method.
     *
     * @param  index                    0 based index of the group to expand
     * @return                          the group which was expanded
     * @throws IllegalArgumentException if group is not enabled or visible, or does not exist
     */
    PanelMenuGroup expandGroup(int index);

    /**
     * Collapses group which satisfies the rules given in <code>picker</code> object.
     *
     * Note that group need to be enabled and visible before calling this method.
     *
     * @param  picker                   object to define which group should be collapsed
     * @throws IllegalArgumentException if group is not enabled or visible, or does not exist
     */
    void collapseGroup(ChoicePicker picker);

    /**
     * Collapses group which text representation matches the given <code>header</code>.
     *
     * Note that group need to be enabled and visible before calling this method.
     *
     * @param  header the header of the group which should be collapsed
     * @throws IllegalArgumentException if group is not enabled or visible, or does not exist
     */
    void collapseGroup(String header);

    /**
     * Collapses group which index equals the given <code>index</code>.
     *
     * Note that group needs to be enabled and visible before calling this method.
     *
     * @param  index                    0 based index of the group to collapse
     * @throws IllegalArgumentException if group is not enabled or visible, or does not exist
     */
    void collapseGroup(int index);

    /**
     * Selects a menu item which satisfies the rules given in <code>picker</code> object.
     *
     * Note that item needs to be enabled and visible before calling this method.
     *
     * @param  picker                   object to define which item should be selected
     * @return                          the item which was selected
     * @throws IllegalArgumentException if menu item is not enabled or visible, or does not exist
     */
    PanelMenuItem selectItem(ChoicePicker picker);

    /**
     * Selects a menu item which text representation matches the given <code>header</code>.
     *
     * Note that item needs to be enabled and visible before calling this method.
     *
     * @param  header                   the header of the item which should be selected
     * @return                          the item which was selected
     * @throws IllegalArgumentException if menu item is not enabled or visible, or does not exist
     */
    PanelMenuItem selectItem(String header);

    /**
     * Selects a menu item which index equals the given <code>index</code>.
     *
     * Note that item need to be enabled and visible before calling this method.
     *
     * @param  index                    0 based index of the item to be selected
     * @return                          the item which was selected
     * @throws IllegalArgumentException if group is not enabled or visible, or does not exist
     */
    PanelMenuItem selectItem(int index);

    /**
     * Expands all enabled and visible groups.
     *
     * @return the panel menu itself
     */
    PanelMenu expandAll();

    /**
     * Collapses all enabled and visible groups.
     *
     * @return the panel menu itself
     */
    PanelMenu collapseAll();
}

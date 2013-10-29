package org.richfaces.tests.page.fragments.impl.panelMenu;

import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePicker;

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

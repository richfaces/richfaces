package org.richfaces.fragment.panelMenu;

import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;

public class PanelMenuShowcase {

    @FindBy
    private RichFacesPanelMenu panelMenu;

    public void showcase_panel_menu() {
        panelMenu.expandGroup(4).selectItem("Item 4.2.");

        panelMenu.expandGroup(2).selectItem("Item 2.1.");

        panelMenu.selectItem(ChoicePickerHelper.byIndex().beforeLast(4));
    }
}

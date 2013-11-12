package org.richfaces.fragment.contextMenu;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;

import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;

public class ContextMenuShowcase {

    @FindBy
    private RichFacesContextMenu contextMenu;

    public void showcase_context_menu() {
        contextMenu.selectItem("Open");

        guardAjax(contextMenu).selectItem(6);

        contextMenu.selectItem(ChoicePickerHelper.byVisibleText().startsWith("Clo"));
    }
}

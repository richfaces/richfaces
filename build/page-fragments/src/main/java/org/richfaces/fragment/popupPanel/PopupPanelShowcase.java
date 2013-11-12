package org.richfaces.fragment.popupPanel;

import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.panel.TextualFragmentPart;

public class PopupPanelShowcase {

    @FindBy
    private RichFacesPopupPanel<TextualFragmentPart, TextualFragmentPart, TextualFragmentPart> popupPanel;

    @FindBy
    private TextualRichFacesPopupPanel textPopupPanel;

    public void showcase_popup() {
        popupPanel.getBodyContent().getText();

        textPopupPanel.getBodyContent().getText();
    }
}

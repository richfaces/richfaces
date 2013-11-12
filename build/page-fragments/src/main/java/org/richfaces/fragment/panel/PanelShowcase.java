package org.richfaces.fragment.panel;

import org.openqa.selenium.support.FindBy;

public class PanelShowcase {

    @FindBy
    private RichFacesPanel<TextualFragmentPart, TextualFragmentPart> panel;

    @FindBy
    private TextualRichFacesPanel textualPanel;

    public void showcase_panel() {
        panel.getHeaderContent().getText();

        textualPanel.getHeaderText();
    }
}

package org.richfaces.fragment.tabPanel;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class TabPanelShowcase {

    @FindBy
    private RichFacesTabPanel tabPanel;

    public void showcase_tab_panel() {
        RichFacesTab tab3 = tabPanel.switchTo("Tab Header 3");
        tab3.getContent(MyThridTab.class).getParagraph().getText();
    }

    public class MyThridTab {

        @FindBy
        private WebElement paragraph;

        public WebElement getParagraph() {
            return paragraph;
        }
    }
}

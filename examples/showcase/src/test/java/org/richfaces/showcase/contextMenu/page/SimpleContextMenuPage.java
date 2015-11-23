package org.richfaces.showcase.contextMenu.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.contextMenu.RichFacesContextMenu;

public class SimpleContextMenuPage {

    @FindBy(css = "img[id$='pic']")
    private WebElement picture;

    @FindBy(className = "rf-ctx-lbl")
    private RichFacesContextMenu contextMenu;

    public WebElement getPicture() {
        return picture;
    }

    public RichFacesContextMenu getContextMenu() {
        contextMenu.advanced().setShowEventFromWidget();
        contextMenu.advanced().setTargetFromWidget();
        return contextMenu;
    }
}

package org.richfaces.tests.showcase.contextMenu.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.contextMenu.RichFacesContextMenu;
import org.richfaces.tests.page.fragments.impl.contextMenu.PopupMenuItem;

public class SimpleContextMenuPage {

    @FindBy(css = "img[id$='pic']")
    private WebElement picture;

    @FindBy(className = "rf-ctx-pos")
    private RichFacesContextMenu contextMenu;

    public static final PopupMenuItem ZOOM_IN = new PopupMenuItem("Zoom In");
    public static final PopupMenuItem ZOOM_OUT = new PopupMenuItem("Zoom Out");

    public WebElement getPicture() {
        return picture;
    }

    public RichFacesContextMenu getContextMenu() {
        contextMenu.setInvoker(RichFacesContextMenu.LEFT_CLICK);

        if (contextMenu.getTarget() == null) {
            contextMenu.setTarget(picture);
        }

        return contextMenu;
    }
}
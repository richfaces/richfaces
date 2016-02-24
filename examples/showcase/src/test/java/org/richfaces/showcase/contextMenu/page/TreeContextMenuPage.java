package org.richfaces.showcase.contextMenu.page;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.fragment.contextMenu.RichFacesContextMenu;

public class TreeContextMenuPage {

    @FindBy(css = ".rf-tr-nd-lf .rf-trn-cnt")
    private List<WebElement> leaves;

    @FindBy(className = "rf-trn-hnd-exp")
    private List<WebElement> collapseSigns;

    @FindBy(className = "rf-trn-hnd-colps")
    private List<WebElement> expandSigns;

    @FindBy(className = "rf-ctx-lbl")
    private RichFacesContextMenu contextMenu;

    @FindBy(css = "input[type='button']")
    private WebElement closeButton;

    @FindBy(css = "#popupContent tr:nth-of-type(3) td:nth-of-type(2)")
    private WebElement artistFromPopup;

    private static final String CLASS_OF_SELECTED_NODE = "rf-trn-sel";

    public ExpectedCondition<Boolean> getExpextedConditionOnNodeSelected(final WebElement node) {
        return new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver arg0) {
                return node.getAttribute("class").contains(CLASS_OF_SELECTED_NODE);
            }

        };
    }

    public RichFacesContextMenu getContextMenu() {
        return contextMenu;
    }

    public void collapseAllNodes() {
        int size = collapseSigns.size();
        for (int i = 0; i < size; i++) {
            collapseSigns.get(0).click();
        }
    }

    public void expandNodes(int... howMuch) {
        int size = expandSigns.size();
        for (int i = 0; i < size; i++) {
            if (i == howMuch[0]) {
                break;
            }
            expandSigns.get(0).click();
        }
    }

    public List<WebElement> getLeaves() {
        return leaves;
    }

    public void setLeaves(List<WebElement> leaves) {
        this.leaves = leaves;
    }

    public WebElement getCloseButton() {
        return closeButton;
    }

    public void setCloseButton(WebElement closeButton) {
        this.closeButton = closeButton;
    }

    public WebElement getArtistFromPopup() {
        return artistFromPopup;
    }

    public void setArtistFromPopup(WebElement artistFromPopup) {
        this.artistFromPopup = artistFromPopup;
    }
}

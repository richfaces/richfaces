package org.richfaces.tests.showcase.contextMenu.page;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.tests.page.fragments.impl.contextMenu.PopupMenuItem;
import org.richfaces.tests.page.fragments.impl.contextMenu.RichFacesContextMenu;

public class TableContextMenuPage {

    @FindBy(css = ".rf-edt-b > div > table > tbody > tr > td:nth-of-type(2) tr")
    private List<WebElement> prices;

    @FindBy(css = "form[name='form']")
    private RichFacesContextMenu contextMenu;

    @FindBy(css = "#popupContent tr:nth-of-type(3) input")
    private WebElement priceFromPopup;

    @FindBy(css = "input[type='button']")
    private WebElement closeButton;

    public static final PopupMenuItem VIEW = new PopupMenuItem("View");
    private static final String CLASS_OF_SELECTED_ROW = "rf-edt-r-act";

    public ExpectedCondition<Boolean> getWaitConditionOnSelectingRow(final WebElement row) {
        return new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver arg0) {
                return row.getAttribute("class").contains(CLASS_OF_SELECTED_ROW);
            }
        };

    }

    public void closePopup() {
        closeButton.click();

        Graphene.waitGui().withTimeout(2, TimeUnit.SECONDS).withMessage("The popup was not closed in a given timeout!").until()
            .element(closeButton).is().not().visible();
    }

    public List<WebElement> getPrices() {
        return prices;
    }

    public void setPrices(List<WebElement> prices) {
        this.prices = prices;
    }

    public RichFacesContextMenu getContextMenu() {
        return contextMenu;
    }

    public WebElement getPriceFromPopup() {
        return priceFromPopup;
    }

    public void setPriceFromPopup(WebElement priceFromPopup) {
        this.priceFromPopup = priceFromPopup;
    }

    public WebElement getCloseButton() {
        return closeButton;
    }

    public void setCloseButton(WebElement closeButton) {
        this.closeButton = closeButton;
    }
}

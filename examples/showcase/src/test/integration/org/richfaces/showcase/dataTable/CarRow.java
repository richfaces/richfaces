package org.richfaces.showcase.dataTable;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CarRow {

    @FindBy(css = "td:nth-of-type(1)")
    private WebElement vendorCell;

    @FindBy(css = "td:nth-of-type(2)")
    private WebElement modelCell;

    @FindBy(css = "td:nth-of-type(3)")
    private WebElement priceCell;

    @FindBy(css = "td:nth-of-type(4)")
    private WebElement mileageCell;

    @FindBy(css = "td:nth-of-type(5)")
    private WebElement vinCell;

    public CarRow() {
    }

    public WebElement getVendorCell() {
        return vendorCell;
    }

    public void setVendorCell(WebElement vendorCell) {
        this.vendorCell = vendorCell;
    }

    public WebElement getModelCell() {
        return modelCell;
    }

    public void setModelCell(WebElement modelCell) {
        this.modelCell = modelCell;
    }

    public WebElement getPriceCell() {
        return priceCell;
    }

    public void setPriceCell(WebElement priceCell) {
        this.priceCell = priceCell;
    }

    public WebElement getMileageCell() {
        return mileageCell;
    }

    public void setMileageCell(WebElement mileageCell) {
        this.mileageCell = mileageCell;
    }

    public WebElement getVinCell() {
        return vinCell;
    }

    public void setVinCell(WebElement vinCell) {
        this.vinCell = vinCell;
    }
}
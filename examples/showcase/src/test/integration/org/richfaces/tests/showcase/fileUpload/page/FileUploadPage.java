/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richfaces.tests.showcase.fileUpload.page;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;

/**
 *
 * @author pmensik
 */
public class FileUploadPage {

    @FindBy(className = "rf-fu-btn-add")
    private WebElement addButton;

    @FindBy(css = "div[id$='upload']")
    private WebElement uploadArea;

    @FindBy(css = "div[id$='info']")
    private WebElement uploadFilesInfo;

    @FindBy(css = "div[class='rf-p-b info']")
    private WebElement divWithUploadFilesMessage;

    public WebElement getAddButton() {
        return addButton;
    }

    public void setAddButton(WebElement addButton) {
        this.addButton = addButton;
    }

    public WebElement getUploadArea() {
        return uploadArea;
    }

    public void setUploadArea(WebElement uploadArea) {
        this.uploadArea = uploadArea;
    }

    public WebElement getUploadFilesInfo() {
        return uploadFilesInfo;
    }

    public void setUploadFilesInfo(WebElement uploadFilesInfo) {
        this.uploadFilesInfo = uploadFilesInfo;
    }

    public WebElement getDivWithUploadFilesMessage() {
        return divWithUploadFilesMessage;
    }

    public void setDivWithUploadFilesMessage(WebElement divWithUploadFilesMessage) {
        this.divWithUploadFilesMessage = divWithUploadFilesMessage;
    }
}

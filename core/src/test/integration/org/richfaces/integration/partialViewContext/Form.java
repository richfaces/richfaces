package org.richfaces.integration.partialViewContext;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class Form {

    @FindBy(name = "javax.faces.ViewState")
    private WebElement viewState;

    @FindBy(css = "input[type=submit]")
    private WebElement button;

    @FindBy(css = "input[type=text]")
    private WebElement input;

    public String getViewState() {
        return viewState.getAttribute("value");
    }

    public void setInput(String text) {
        input.clear();
        input.sendKeys(text);
    }

    public String getInput() {
        return input.getAttribute("value");
    }

    public void submit() {
        guardAjax(button).click();
    }
}
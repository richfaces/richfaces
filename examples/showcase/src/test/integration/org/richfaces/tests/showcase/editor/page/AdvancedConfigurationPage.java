package org.richfaces.tests.showcase.editor.page;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;

public class AdvancedConfigurationPage {

    public static final String NEW_PAGE_ENG = "New Page";
    public static final String NEW_PAGE_FR = "Nouvelle page";
    public static final String NEW_PAGE_DE = "Neue Seite";

    @FindBy(jquery = "input[type=radio]:eq(0)")
    public WebElement englishRadio;

    @FindBy(jquery = "input[type=radio]:eq(1)")
    public WebElement frenchRadio;

    @FindBy(jquery = "input[type=radio]:eq(2)")
    public WebElement germanRadio;

    @FindBy(jquery = ".cke_button_newpage")
    public WebElement newPageButton;
}

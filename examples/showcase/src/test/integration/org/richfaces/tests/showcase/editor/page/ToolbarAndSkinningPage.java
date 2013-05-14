/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richfaces.tests.showcase.editor.page;

import java.util.List;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;

/**
 *
 * @author pmensik
 */
public class ToolbarAndSkinningPage {

    @FindBy(className = "cke_button")
    public List<WebElement> buttonsOfEditor;

    @FindBy(jquery = "input[id*='toolbarSelection:0']")
    public WebElement basicEditorCheckbox;

    @FindBy(jquery = "input[id*='toolbarSelection:1']")
    public WebElement fullEditorCheckbox;

    @FindBy(jquery = "input[id*='toolbarSelection:2']")
    public WebElement customEditorCheckbox;
}

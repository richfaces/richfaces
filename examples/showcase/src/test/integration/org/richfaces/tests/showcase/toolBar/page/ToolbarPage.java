/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richfaces.tests.showcase.toolBar.page;

import java.util.List;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;

/**
 *
 * @author <a href="mailto:pmensik@redhat.com">Petr Mensik</a>
 */
public class ToolbarPage {

    @FindBy(css = "div[class='rf-tb-sep-grid']")
    public WebElement separator;

    @FindBy(css = "input[class='barsearch']")
    public WebElement searchInput;

    @FindBy(css = "input[class='barsearchbutton']")
    public WebElement searchButton;

    @FindBy(css = "tr[class='rf-tb-cntr'] td img")
    public List<WebElement> toolbarImages;
}

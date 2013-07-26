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
public class ToolbarIconsPage {

    @FindBy(jquery = "a:contains('Line'):eq(0)")
    public WebElement lineGroupSep;

    @FindBy(jquery = "a:contains('Grid'):eq(0)")
    public WebElement gridGroupSep;

    @FindBy(jquery = "a:contains('Disc'):eq(0)")
    public WebElement discGroupSep;

    @FindBy(jquery = "a:contains('Square'):eq(0)")
    public WebElement squareGroupSep;

    @FindBy(jquery = "a:contains('None'):eq(0)")
    public WebElement noneGroupSep;

    @FindBy(jquery = "a:contains('Line'):eq(1)")
    public WebElement lineItemSeparator;

    @FindBy(jquery = "a:contains('Grid'):eq(1)")
    public WebElement gridItemSep;

    @FindBy(jquery = "a:contains('Disc'):eq(1)")
    public WebElement discItemSep;

    @FindBy(jquery = "a:contains('Square'):eq(1)")
    public WebElement squareItemSep;

    @FindBy(jquery = "a:contains('None'):eq(1)")
    public WebElement noneItemSep;

    @FindBy(css = "div[class='rf-tb-sep-line']")
    public List<WebElement> lineSep;

    @FindBy(css = "div[class='rf-tb-sep-grid']")
    public List<WebElement> gridSep;

    @FindBy(css = "div[class='rf-tb-sep-disc']")
    public List<WebElement> discSep;

    @FindBy(css = "div[class='rf-tb-sep-square']")
    public List<WebElement> squareSep;
}

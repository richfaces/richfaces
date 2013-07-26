/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richfaces.tests.showcase.tooltip.page;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;

/**
 *
 * @author <a href="mailto:pmensik@redhat.com">Petr Mensik</a>
 */
public class TooltipPage {

    @FindBy(jquery = "div[class*='rf-p tooltip-text']:eq(0)")
    public WebElement clientTooltipActivatingArea;

    @FindBy(jquery = "div[class*='rf-p tooltip-text']:eq(1)")
    public WebElement clientWithDelayTooltipActivatingArea;

    @FindBy(jquery = "div[class*='rf-p tooltip-text']:eq(2)")
    public WebElement ajaxTooltipActivatingArea;

    @FindBy(jquery = "div[class*='rf-p tooltip-text']:eq(3)")
    public WebElement ajaxClickTooltipActivatingArea;

    @FindBy(jquery = "div[class*='rf-tt tooltip']:visible")
    public WebElement tooltip;
}

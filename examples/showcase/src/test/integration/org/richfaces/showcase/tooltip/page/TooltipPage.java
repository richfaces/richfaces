/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *******************************************************************************/
package org.richfaces.showcase.tooltip.page;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:pmensik@redhat.com">Petr Mensik</a>
 */
public class TooltipPage {

    @FindByJQuery("div[class*='rf-p tooltip-text']:eq(0)")
    private WebElement clientTooltipActivatingArea;

    @FindByJQuery("div[class*='rf-p tooltip-text']:eq(1)")
    private WebElement clientWithDelayTooltipActivatingArea;

    @FindByJQuery("div[class*='rf-p tooltip-text']:eq(2)")
    private WebElement ajaxTooltipActivatingArea;

    @FindByJQuery("div[class*='rf-p tooltip-text']:eq(3)")
    private WebElement ajaxClickTooltipActivatingArea;

    @FindByJQuery("div[class*='rf-tt tooltip']:visible")
    private WebElement tooltip;

    public WebElement getClientTooltipActivatingArea() {
        return clientTooltipActivatingArea;
    }

    public void setClientTooltipActivatingArea(WebElement clientTooltipActivatingArea) {
        this.clientTooltipActivatingArea = clientTooltipActivatingArea;
    }

    public WebElement getClientWithDelayTooltipActivatingArea() {
        return clientWithDelayTooltipActivatingArea;
    }

    public void setClientWithDelayTooltipActivatingArea(WebElement clientWithDelayTooltipActivatingArea) {
        this.clientWithDelayTooltipActivatingArea = clientWithDelayTooltipActivatingArea;
    }

    public WebElement getAjaxTooltipActivatingArea() {
        return ajaxTooltipActivatingArea;
    }

    public void setAjaxTooltipActivatingArea(WebElement ajaxTooltipActivatingArea) {
        this.ajaxTooltipActivatingArea = ajaxTooltipActivatingArea;
    }

    public WebElement getAjaxClickTooltipActivatingArea() {
        return ajaxClickTooltipActivatingArea;
    }

    public void setAjaxClickTooltipActivatingArea(WebElement ajaxClickTooltipActivatingArea) {
        this.ajaxClickTooltipActivatingArea = ajaxClickTooltipActivatingArea;
    }

    public WebElement getTooltip() {
        return tooltip;
    }

    public void setTooltip(WebElement tooltip) {
        this.tooltip = tooltip;
    }
}

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
package org.richfaces.showcase.toolBar.page;

import java.util.List;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:pmensik@redhat.com">Petr Mensik</a>
 */
public class ToolbarIconsPage {

    @FindByJQuery("a:contains('Line'):eq(0)")
    private WebElement lineGroupSep;

    @FindByJQuery("a:contains('Grid'):eq(0)")
    private WebElement gridGroupSep;

    @FindByJQuery("a:contains('Disc'):eq(0)")
    private WebElement discGroupSep;

    @FindByJQuery("a:contains('Square'):eq(0)")
    private WebElement squareGroupSep;

    @FindByJQuery("a:contains('None'):eq(0)")
    private WebElement noneGroupSep;

    @FindByJQuery("a:contains('Line'):eq(1)")
    private WebElement lineItemSeparator;

    @FindByJQuery("a:contains('Grid'):eq(1)")
    private WebElement gridItemSep;

    @FindByJQuery("a:contains('Disc'):eq(1)")
    private WebElement discItemSep;

    @FindByJQuery("a:contains('Square'):eq(1)")
    private WebElement squareItemSep;

    @FindByJQuery("a:contains('None'):eq(1)")
    private WebElement noneItemSep;

    @FindBy(css = "div[class='rf-tb-sep-line']")
    private List<WebElement> lineSep;

    @FindBy(css = "div[class='rf-tb-sep-grid']")
    private List<WebElement> gridSep;

    @FindBy(css = "div[class='rf-tb-sep-disc']")
    private List<WebElement> discSep;

    @FindBy(css = "div[class='rf-tb-sep-square']")
    private List<WebElement> squareSep;

    public WebElement getLineGroupSep() {
        return lineGroupSep;
    }

    public WebElement getGridGroupSep() {
        return gridGroupSep;
    }

    public WebElement getDiscGroupSep() {
        return discGroupSep;
    }

    public WebElement getSquareGroupSep() {
        return squareGroupSep;
    }

    public WebElement getNoneGroupSep() {
        return noneGroupSep;
    }

    public WebElement getLineItemSeparator() {
        return lineItemSeparator;
    }

    public WebElement getGridItemSep() {
        return gridItemSep;
    }

    public WebElement getDiscItemSep() {
        return discItemSep;
    }

    public WebElement getSquareItemSep() {
        return squareItemSep;
    }

    public WebElement getNoneItemSep() {
        return noneItemSep;
    }

    public List<WebElement> getLineSep() {
        return lineSep;
    }

    public List<WebElement> getGridSep() {
        return gridSep;
    }

    public List<WebElement> getDiscSep() {
        return discSep;
    }

    public List<WebElement> getSquareSep() {
        return squareSep;
    }

}

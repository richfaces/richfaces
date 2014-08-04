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
    public WebElement lineGroupSep;

    @FindByJQuery("a:contains('Grid'):eq(0)")
    public WebElement gridGroupSep;

    @FindByJQuery("a:contains('Disc'):eq(0)")
    public WebElement discGroupSep;

    @FindByJQuery("a:contains('Square'):eq(0)")
    public WebElement squareGroupSep;

    @FindByJQuery("a:contains('None'):eq(0)")
    public WebElement noneGroupSep;

    @FindByJQuery("a:contains('Line'):eq(1)")
    public WebElement lineItemSeparator;

    @FindByJQuery("a:contains('Grid'):eq(1)")
    public WebElement gridItemSep;

    @FindByJQuery("a:contains('Disc'):eq(1)")
    public WebElement discItemSep;

    @FindByJQuery("a:contains('Square'):eq(1)")
    public WebElement squareItemSep;

    @FindByJQuery("a:contains('None'):eq(1)")
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

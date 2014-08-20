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
package org.richfaces.showcase.popup.page;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author pmensik
 */
public class PopupPage {

    @FindByJQuery("input[type='submit']:eq(0)")
    private WebElement callthePopupButton;

    @FindByJQuery("td[class*='gutter']:visible")
    private WebElement sourceOfPage;

    @FindBy(css = "a[class*='show']")
    private WebElement anchorOfSource;

    @FindByJQuery("div[id$='popup_content']")
    private WebElement popupPanelContent;

    public WebElement getCallthePopupButton() {
        return callthePopupButton;
    }

    public void setCallthePopupButton(WebElement callthePopupButton) {
        this.callthePopupButton = callthePopupButton;
    }

    public WebElement getSourceOfPage() {
        return sourceOfPage;
    }

    public void setSourceOfPage(WebElement sourceOfPage) {
        this.sourceOfPage = sourceOfPage;
    }

    public WebElement getAnchorOfSource() {
        return anchorOfSource;
    }

    public void setAnchorOfSource(WebElement anchorOfSource) {
        this.anchorOfSource = anchorOfSource;
    }

    public WebElement getPopupPanelContent() {
        return popupPanelContent;
    }

    public void setPopupPanelContent(WebElement popupPanelContent) {
        this.popupPanelContent = popupPanelContent;
    }

}

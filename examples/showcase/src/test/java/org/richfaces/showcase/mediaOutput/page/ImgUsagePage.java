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
package org.richfaces.showcase.mediaOutput.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class ImgUsagePage {

    public static final int INDEX_RED = 0;
    public static final int INDEX_DARK_BLUE = 1;
    public static final int INDEX_GREEN = 2;
    public static final int INDEX_YELLOW = 3;
    public static final int INDEX_BLUE = 4;

    @FindBy(css = ".example-cnt img")
    private WebElement image;

    @FindBy(xpath = "//*[@class='example-cnt']//td[contains(text(), 'Color 1')]/../td[position()=2]/select")
    private WebElement selectLeftColor;

    @FindBy(xpath = "//*[@class='example-cnt']//td[contains(text(), 'Color 1')]/../td[position()=4]/select")
    private WebElement selectRightColor;

    @FindBy(xpath = "//*[@class='example-cnt']//td[contains(text(), 'Color 1')]/../td[position()=6]/select")
    private WebElement selectTextColor;

    @FindBy(css = ".example-cnt input[type='submit']")
    private WebElement submitButton;

    public WebElement getImage() {
        return image;
    }

    public WebElement getSelectLeftColor() {
        return selectLeftColor;
    }

    public WebElement getSelectRightColor() {
        return selectRightColor;
    }

    public WebElement getSelectTextColor() {
        return selectTextColor;
    }

    public WebElement getSubmitButton() {
        return submitButton;
    }

}

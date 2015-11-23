/*
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
 */
package org.richfaces.showcase.dataScroller.page;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public abstract class AbstractDataScrollerPage {

    public static final String CLASS_OF_BUTTON_FIRST = "rf-ds-btn rf-ds-btn-first";
    public static final String CLASS_OF_BUTTON_FAST_RWD = "rf-ds-btn rf-ds-btn-fastrwd";
    public static final String CLASS_OF_BUTTON_PREV = "rf-ds-btn rf-ds-btn-prev";
    public static final String CLASS_OF_BUTTON_NEXT = "rf-ds-btn rf-ds-btn-next";
    public static final String CLASS_OF_BUTTON_FAST_FWD = "rf-ds-btn rf-ds-btn-fastfwd";
    public static final String CLASS_OF_BUTTON_LAST = "rf-ds-btn rf-ds-btn-last";
    public static final String CLASS_OF_BUTTON_DIS = "rf-ds-dis";

    public static final String CLASS_OF_INACTIVE_BUTTON_WITH_NUMBER = "rf-ds-nmb-btn";
    public static final String CLASS_OF_ACTIVE_BUTTON_WITH_NUMBER = CLASS_OF_INACTIVE_BUTTON_WITH_NUMBER + " rf-ds-act";

    @FindByJQuery("a[class='" + CLASS_OF_BUTTON_NEXT + "']")
    private WebElement nextButton;
    @FindByJQuery("span[class='" + CLASS_OF_BUTTON_NEXT + " " + CLASS_OF_BUTTON_DIS + "']")
    private WebElement nextButtonDis;

    @FindByJQuery("a[class='" + CLASS_OF_BUTTON_PREV + "']")
    private WebElement previousButton;
    @FindByJQuery("span[class='" + CLASS_OF_BUTTON_PREV + " " + CLASS_OF_BUTTON_DIS + "']")
    private WebElement previousButtonDis;

    @FindByJQuery("a[class='" + CLASS_OF_BUTTON_FIRST + "']")
    private WebElement firstPageButton;
    @FindByJQuery("span[class='" + CLASS_OF_BUTTON_FIRST + " " + CLASS_OF_BUTTON_DIS + "']")
    private WebElement firstPageButtonDis;

    @FindByJQuery("a[class='" + CLASS_OF_BUTTON_LAST + "']")
    private WebElement lastPageButton;
    @FindByJQuery("span[class='" + CLASS_OF_BUTTON_LAST + " " + CLASS_OF_BUTTON_DIS + "']")
    private WebElement lastPageButtonDis;

    @FindByJQuery("a[class='" + CLASS_OF_BUTTON_FAST_RWD + "']")
    private WebElement fastPrevButton;
    @FindByJQuery("span[class='" + CLASS_OF_BUTTON_FAST_RWD + " " + CLASS_OF_BUTTON_DIS + "']")
    private WebElement fastPrevButtonDis;

    @FindByJQuery("a[class='" + CLASS_OF_BUTTON_FAST_FWD + "']")
    private WebElement fastNextButton;
    @FindByJQuery("span[class='" + CLASS_OF_BUTTON_FAST_FWD + " " + CLASS_OF_BUTTON_DIS + "']")
    private WebElement fastNextButtonDis;

    @FindByJQuery("span[class*='" + CLASS_OF_ACTIVE_BUTTON_WITH_NUMBER + "']")
    private WebElement buttonWithNumberOfPageActive;

    public int getNumberOfCurrentPage() {
        String currentPage = buttonWithNumberOfPageActive.getText().trim();
        return Integer.valueOf(currentPage).intValue();
    }

    public WebElement getNextButton() {
        return nextButton;
    }

    public WebElement getNextButtonDis() {
        return nextButtonDis;
    }

    public WebElement getPreviousButton() {
        return previousButton;
    }

    public WebElement getPreviousButtonDis() {
        return previousButtonDis;
    }

    public WebElement getFirstPageButton() {
        return firstPageButton;
    }

    public WebElement getFirstPageButtonDis() {
        return firstPageButtonDis;
    }

    public WebElement getLastPageButton() {
        return lastPageButton;
    }

    public WebElement getLastPageButtonDis() {
        return lastPageButtonDis;
    }

    public WebElement getFastPrevButton() {
        return fastPrevButton;
    }

    public WebElement getFastPrevButtonDis() {
        return fastPrevButtonDis;
    }

    public WebElement getFastNextButton() {
        return fastNextButton;
    }

    public WebElement getFastNextButtonDis() {
        return fastNextButtonDis;
    }

    public WebElement getButtonWithNumberOfPageActive() {
        return buttonWithNumberOfPageActive;
    }

}

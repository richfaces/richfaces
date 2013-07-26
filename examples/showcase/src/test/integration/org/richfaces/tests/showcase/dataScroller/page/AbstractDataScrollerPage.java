/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.showcase.dataScroller.page;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public abstract class AbstractDataScrollerPage {

    public final String CLASS_OF_BUTTON_FIRST = "rf-ds-btn rf-ds-btn-first";
    public final String CLASS_OF_BUTTON_FAST_RWD = "rf-ds-btn rf-ds-btn-fastrwd";
    public final String CLASS_OF_BUTTON_PREV = "rf-ds-btn rf-ds-btn-prev";
    public final String CLASS_OF_BUTTON_NEXT = "rf-ds-btn rf-ds-btn-next";
    public final String CLASS_OF_BUTTON_FAST_FWD = "rf-ds-btn rf-ds-btn-fastfwd";
    public final String CLASS_OF_BUTTON_LAST = "rf-ds-btn rf-ds-btn-last";
    public final String CLASS_OF_BUTTON_DIS = "rf-ds-dis";

    public final String CLASS_OF_INACTIVE_BUTTON_WITH_NUMBER = "rf-ds-nmb-btn";
    public final String CLASS_OF_ACTIVE_BUTTON_WITH_NUMBER = CLASS_OF_INACTIVE_BUTTON_WITH_NUMBER + " rf-ds-act";


    @FindBy(jquery="a[class='" + CLASS_OF_BUTTON_NEXT + "']")
    public WebElement nextButton;
    @FindBy(jquery="span[class='" + CLASS_OF_BUTTON_NEXT + " " + CLASS_OF_BUTTON_DIS + "']")
    public WebElement nextButtonDis;

    @FindBy(jquery="a[class='" + CLASS_OF_BUTTON_PREV + "']")
    public WebElement previousButton;
    @FindBy(jquery="span[class='" + CLASS_OF_BUTTON_PREV + " " + CLASS_OF_BUTTON_DIS + "']")
    public WebElement previousButtonDis;

    @FindBy(jquery="a[class='" + CLASS_OF_BUTTON_FIRST + "']")
    public WebElement firstPageButton;
    @FindBy(jquery="span[class='" + CLASS_OF_BUTTON_FIRST + " " + CLASS_OF_BUTTON_DIS + "']")
    public WebElement firstPageButtonDis;

    @FindBy(jquery="a[class='" + CLASS_OF_BUTTON_LAST + "']")
    public WebElement lastPageButton;
    @FindBy(jquery="span[class='" + CLASS_OF_BUTTON_LAST + " " + CLASS_OF_BUTTON_DIS + "']")
    public WebElement lastPageButtonDis;

    @FindBy(jquery="a[class='" + CLASS_OF_BUTTON_FAST_RWD + "']")
    public WebElement fastPrevButton;
    @FindBy(jquery="span[class='" + CLASS_OF_BUTTON_FAST_RWD + " " + CLASS_OF_BUTTON_DIS + "']")
    public WebElement fastPrevButtonDis;

    @FindBy(jquery="a[class='" + CLASS_OF_BUTTON_FAST_FWD + "']")
    public WebElement fastNextButton;
    @FindBy(jquery="span[class='" + CLASS_OF_BUTTON_FAST_FWD + " " + CLASS_OF_BUTTON_DIS + "']")
    public WebElement fastNextButtonDis;

    @FindBy(jquery="span[class*='" + CLASS_OF_ACTIVE_BUTTON_WITH_NUMBER + "']")
    public WebElement buttonWithNumberOfPageActive;

    public int getNumberOfCurrentPage() {
        String currentPage = buttonWithNumberOfPageActive.getText().trim();
        return Integer.valueOf(currentPage).intValue();
    }

}

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
package org.richfaces.showcase.dataTable.page;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class DataTableEditPage {

    @FindByJQuery("input[value=Delete]:visible")
    private WebElement deleteButtonInpopup;

    @FindByJQuery("input[value=Cancel]:visible")
    private WebElement cancelButtonInpopup;

    @FindByJQuery("input[value=Store]:visible")
    private WebElement storeButtonInpopup;

    @FindByJQuery("table[id$=editGrid] tbody tr:eq(0) td:eq(1)")
    private WebElement vendorpopup;

    @FindByJQuery("table[id$=editGrid] tbody tr:eq(1) td:eq(1)")
    private WebElement modelpopup;

    @FindByJQuery("input[id$=price]")
    private WebElement priceInputpopup;

    @FindByJQuery("input[id$=mage]")
    private WebElement mileageInputpopup;

    @FindByJQuery("input[id$=vin]")
    private WebElement vinInputpopup;

    @FindByJQuery("span[id$=price] span")
    private WebElement errorMsgPrice;

    @FindByJQuery("span[id$=mage] span")
    private WebElement errorMsgMileage;

    @FindByJQuery("span[id$=vin] span")
    private WebElement errorMsgVin;

    @FindByJQuery("tbody[class=rf-dt-b]")
    private WebElement table;

    public WebElement getDeleteButtonInpopup() {
        return deleteButtonInpopup;
    }

    public WebElement getCancelButtonInpopup() {
        return cancelButtonInpopup;
    }

    public WebElement getStoreButtonInpopup() {
        return storeButtonInpopup;
    }

    public WebElement getVendorpopup() {
        return vendorpopup;
    }

    public WebElement getModelpopup() {
        return modelpopup;
    }

    public WebElement getPriceInputpopup() {
        return priceInputpopup;
    }

    public WebElement getMileageInputpopup() {
        return mileageInputpopup;
    }

    public WebElement getVinInputpopup() {
        return vinInputpopup;
    }

    public WebElement getErrorMsgPrice() {
        return errorMsgPrice;
    }

    public WebElement getErrorMsgMileage() {
        return errorMsgMileage;
    }

    public WebElement getErrorMsgVin() {
        return errorMsgVin;
    }

    public WebElement getTable() {
        return table;
    }

}

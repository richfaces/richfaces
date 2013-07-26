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
package org.richfaces.tests.showcase.dataTable.page;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class DataTableEditPage {

    @FindBy(jquery="input[value=Delete]:visible")
    public WebElement deleteButtonInpopup;
    @FindBy(jquery="input[value=Cancel]:visible")
    public WebElement cancelButtonInpopup;
    @FindBy(jquery="input[value=Store]:visible")
    public WebElement storeButtonInpopup;

    @FindBy(jquery="table[id$=editGrid] tbody tr:eq(0) td:eq(1)")
    public WebElement vendorpopup;
    @FindBy(jquery="table[id$=editGrid] tbody tr:eq(1) td:eq(1)")
    public WebElement modelpopup;
    @FindBy(jquery="input[id$=price]")
    public WebElement priceInputpopup;
    @FindBy(jquery="input[id$=mage]")
    public WebElement mileageInputpopup;
    @FindBy(jquery="input[id$=vin]")
    public WebElement vinInputpopup;

    @FindBy(jquery="span[id$=price] span")
    public WebElement errorMsgPrice;
    @FindBy(jquery="span[id$=mage] span")
    public WebElement errorMsgMileage;
    @FindBy(jquery="span[id$=vin] span")
    public WebElement errorMsgVin;

    @FindBy(jquery="tbody[class=rf-dt-b]")
    public WebElement table;

}

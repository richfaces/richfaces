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

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class SimpleTablePage {

    // Subtables
    @FindBy(xpath = "//tbody[@class='rf-cst'][1]/tr[4]")
    private WebElement sanJoseSubtable;
    
    @FindBy(xpath = "//tbody[@class='rf-cst'][2]/tr[4]")
    private WebElement seattleSubtable;
    
    @FindBy(xpath = "//tfoot/tr")
    private WebElement footSubtable;

    public String getSanJoseHotels() {
        return getHotels(sanJoseSubtable);
    }

    public String getSanJoseMeals() {
        return getMeals(sanJoseSubtable);
    }

    public String getSanJoseSubtotals() {
        return getSubtotals(sanJoseSubtable);
    }

    public String getSanJoseTransport() {
        return getTransport(sanJoseSubtable);
    }

    public String getSeattleHotels() {
        return getHotels(seattleSubtable);
    }

    public String getSeattleMeals() {
        return getMeals(seattleSubtable);
    }

    public String getSeattleSubtotals() {
        return getSubtotals(seattleSubtable);
    }

    public String getSeattleTransport() {
        return getTransport(seattleSubtable);
    }

    public String getTotalsOfHotels() {
        return getHotels(footSubtable);
    }

    public String getTotalsOfMeals() {
        return getMeals(footSubtable);
    }

    public String getTotalsOfSubtotals() {
        return getSubtotals(footSubtable);
    }

    public String getTotalsOfTransport() {
        return getTransport(footSubtable);
    }

    private String getHotels(WebElement subtable) {
        return subtable.findElement(By.xpath("td[3]")).getText();
    }

    private String getMeals(WebElement subtable) {
        return subtable.findElement(By.xpath("td[2]")).getText();
    }

    private String getSubtotals(WebElement subtable) {
        return subtable.findElement(By.xpath("td[5]")).getText();
    }

    private String getTransport(WebElement subtable) {
        return subtable.findElement(By.xpath("td[4]")).getText();
    }

}

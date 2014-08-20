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

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class ArrangableModelPage {

    @Drone
    private WebDriver webDriver;

    @FindByJQuery("input[type=text]:eq(0)")
    private WebElement firstNameFilterInput;

    @FindByJQuery("input[type=text]:eq(1)")
    private WebElement secondNameFilterInput;

    @FindByJQuery("input[type=text]:eq(2)")
    private WebElement emailFilterInput;

    @FindBy(css = "tbody.rf-dt-b")
    private WebElement table;

    @FindByJQuery("a:contains(ascending)")
    private WebElement ascendingLink;

    @FindByJQuery("a:contains(descending)")
    private WebElement descendingLink;

    @FindBy(id = "footer")
    private WebElement toBlur;

    public WebElement getFirstRowSomeColumn(int column) {
        return table.findElement(ByJQuery.selector(String.format("tr:eq(0) > td:eq(%s)", column)));
    }

    public WebElement getUnsortedLink(int column) {
        // these are links for filtering rows in a ascending, descending way
        // 0 is for first name column, 1 surname, 2 email
        return webDriver.findElement(ByJQuery.selector(String.format("a[onClick*='RichFaces']:eq(%s)", column)));
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public WebElement getFirstNameFilterInput() {
        return firstNameFilterInput;
    }

    public WebElement getSecondNameFilterInput() {
        return secondNameFilterInput;
    }

    public WebElement getEmailFilterInput() {
        return emailFilterInput;
    }

    public WebElement getTable() {
        return table;
    }

    public WebElement getAscendingLink() {
        return ascendingLink;
    }

    public WebElement getDescendingLink() {
        return descendingLink;
    }

    public WebElement getToBlur() {
        return toBlur;
    }

}

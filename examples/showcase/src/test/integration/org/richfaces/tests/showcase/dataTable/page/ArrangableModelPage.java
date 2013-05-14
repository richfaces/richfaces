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

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.enricher.findby.ByJQuery;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class ArrangableModelPage {

    @Drone
    private WebDriver webDriver;

    @FindBy(jquery="input[type=text]:eq(0)")
    public WebElement firstNameFilterInput;
    @FindBy(jquery="input[type=text]:eq(1)")
    public WebElement secondNameFilterInput;
    @FindBy(jquery="input[type=text]:eq(2)")
    public WebElement emailFilterInput;
    @FindBy(css="tbody.rf-dt-b")
    public WebElement table;
    @FindBy(jquery="a:contains(ascending)")
    public WebElement ascendingLink;
    @FindBy(jquery="a:contains(descending)")
    public WebElement descendingLink;

    @FindBy(id="footer")
    public WebElement toBlur;

    public WebElement getFirstRowSomeColumn(int column) {
        return table.findElement(ByJQuery.jquerySelector(String.format("tr:eq(0) > td:eq(%s)", column)));
    }

    public WebElement getUnsortedLink(int column) {
        // these are links for filtering rows in a ascending, descending way
        // 0 is for first name column, 1 surname, 2 email
        return webDriver.findElement(ByJQuery.jquerySelector(String.format("a[onClick*='RichFaces']:eq(%s)", column)));
    }

}

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
package org.richfaces.showcase.subTableToggleControl.page;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class SubTableToggleControlPage {

    @FindByJQuery("tbody.rf-cst:eq(0)")
    private WebElement bodyOfChevroletSubtable;
    @FindByJQuery("tbody.rf-cst:eq(1)")
    private WebElement bodyOfFordSubtable;
    @FindByJQuery("tbody.rf-cst:eq(2)")
    private WebElement bodyOfNissanSubtable;

    @FindByJQuery(value = "span.rf-csttg:eq(0)")
    private WebElement chevroletToggler;
    @FindByJQuery(value = "span.rf-csttg:eq(1)")
    private WebElement fordToggler;
    @FindByJQuery(value = "span.rf-csttg:eq(2)")
    private WebElement nissanToggler;

    public WebElement getBodyOfChevroletSubtable() {
        return bodyOfChevroletSubtable;
    }

    public WebElement getBodyOfFordSubtable() {
        return bodyOfFordSubtable;
    }

    public WebElement getBodyOfNissanSubtable() {
        return bodyOfNissanSubtable;
    }

    public WebElement getChevroletToggler() {
        return chevroletToggler;
    }

    public WebElement getFordToggler() {
        return fordToggler;
    }

    public WebElement getNissanToggler() {
        return nissanToggler;
    }

}

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
package org.richfaces.showcase.region.page;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class RegionPage {

    @FindByJQuery("input[type='text']:nth(1)")
    public WebElement brokenEmailInput;

    @FindByJQuery("input[type='text']:nth(0)")
    public WebElement brokenNameInput;

    @FindByJQuery("table[id$='echopanel1'] td:nth(3)")
    public WebElement brokenEmailOutput;

    @FindByJQuery("table[id$='echopanel1'] td:nth(1)")
    public WebElement brokenNameOutput;

    @FindByJQuery("input[type='submit']:nth(0)")
    public WebElement brokenSubmit;

    @FindByJQuery("input[type='text']:nth(3)")
    public WebElement emailInput;

    @FindByJQuery("input[type='text']:nth(2)")
    public WebElement nameInput;

    @FindByJQuery("table[id$='echopanel2'] td:nth(3)")
    public WebElement emailOutput;

    @FindByJQuery("table[id$='echopanel2'] td:nth(1)")
    public WebElement nameOutput;

    @FindByJQuery("input[type='submit']:nth(1)")
    public WebElement submit;

}

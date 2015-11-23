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
package org.richfaces.showcase.outputPanel.page;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class SimplePage {

    @FindByJQuery("span:contains('text1')")
    private WebElement firstError;

    @FindBy(css = "input[id$='text1']")
    private WebElement firstInput;

    @FindBy(css = "span[id$='out1']")
    private WebElement firstOutput;

    @FindByJQuery("span:contains('text2')")
    private WebElement secondError;

    @FindBy(css = "input[id$='text2']")
    private WebElement secondInput;

    @FindBy(css = "div[id$='out2']")
    private WebElement secondOutput;

    public WebElement getFirstError() {
        return firstError;
    }

    public WebElement getFirstInput() {
        return firstInput;
    }

    public WebElement getFirstOutput() {
        return firstOutput;
    }

    public WebElement getSecondError() {
        return secondError;
    }

    public WebElement getSecondInput() {
        return secondInput;
    }

    public WebElement getSecondOutput() {
        return secondOutput;
    }

}

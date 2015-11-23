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
package org.richfaces.showcase.focus.page;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.fragment.message.RichFacesMessage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class FocusPage {

    @FindByJQuery("*[type=text]:eq(0)")
    private TextInputComponentImpl nameInput;

    @FindByJQuery("*[type=text]:eq(1)")
    private TextInputComponentImpl jobInput;

    @FindByJQuery("*[type=text]:eq(2)")
    private TextInputComponentImpl addressnput;

    @FindByJQuery("*[type=text]:eq(3)")
    private TextInputComponentImpl zipInput;

    @FindByJQuery("*[type=submit]")
    private WebElement submitButton;

    @FindByJQuery(".rf-msg-err:eq(0)")
    private RichFacesMessage validationMessage;

    @ArquillianResource
    private Actions actions;

    public void typeSomethingAndDoNotCareAboutFocus(String keys) {
        actions.sendKeys(keys).build().perform();
    }

    public TextInputComponentImpl getNameInput() {
        return nameInput;
    }

    public TextInputComponentImpl getJobInput() {
        return jobInput;
    }

    public TextInputComponentImpl getAddressnput() {
        return addressnput;
    }

    public TextInputComponentImpl getZipInput() {
        return zipInput;
    }

    public WebElement getSubmitButton() {
        return submitButton;
    }

    public RichFacesMessage getValidationMessage() {
        return validationMessage;
    }

    public Actions getActions() {
        return actions;
    }

}

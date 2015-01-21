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
package org.richfaces.photoalbum.ftest.webdriver.fragments.view;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.calendar.RichFacesCalendar;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.fragment.fileUpload.RichFacesFileUpload;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class EditUserProfileView {

    @FindBy(css = ".fb-comm-button > input")
    private WebElement bindWithFBButton;
    @FindBy(className = "rf-fu")
    private RichFacesFileUpload avatarUpload;

    @FindBy(css = "[id$='userFirstname']")
    private TextInputComponentImpl firstNameInput;
    @FindBy(css = "[id$='userSecondname']")
    private TextInputComponentImpl secondNameInput;
    @FindBy(css = "input[value=MALE]")
    private WebElement maleRadio;
    @FindBy(css = "input[value=FEMALE]")
    private WebElement femaleRadio;
    @FindBy(className = "rf-cal")
    private RichFacesCalendar birthDayCalendar;
    @FindBy(css = "[id$='userEmail']")
    private TextInputComponentImpl emailInput;
    @FindBy(css = ".user-prefs-button div:contains(Save) + input")
    private WebElement saveButton;
    @FindBy(css = ".user-prefs-button div:contains(Cancel) + input")
    private WebElement cancelButton;

    @FindBy(css = ".reg-table a")
    private WebElement validationHelpLink;

    public WebElement getBindWithFBButton() {
        return bindWithFBButton;
    }

    public RichFacesFileUpload getAvatarUpload() {
        return avatarUpload;
    }

    public TextInputComponentImpl getFirstNameInput() {
        return firstNameInput;
    }

    public TextInputComponentImpl getSecondNameInput() {
        return secondNameInput;
    }

    public WebElement getMaleRadio() {
        return maleRadio;
    }

    public WebElement getFemaleRadio() {
        return femaleRadio;
    }

    public RichFacesCalendar getBirthDayCalendar() {
        return birthDayCalendar;
    }

    public TextInputComponentImpl getEmailInput() {
        return emailInput;
    }

    public WebElement getSaveButton() {
        return saveButton;
    }

    public WebElement getCancelButton() {
        return cancelButton;
    }

    public WebElement getValidationHelpLink() {
        return validationHelpLink;
    }

}

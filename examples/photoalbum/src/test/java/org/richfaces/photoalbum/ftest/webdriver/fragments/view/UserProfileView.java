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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class UserProfileView {

    @FindBy(css = "input[value='Edit']")
    private WebElement editProfileButton;

    @FindBy(css = "[id$='overForm:avatar']")
    private WebElement avatarImage;
    @FindBy(css = "[id$='overForm:userLoginName']")
    private WebElement loginName;
    @FindBy(css = "[id$='overForm:userFirstname']")
    private WebElement firstName;
    @FindBy(css = "[id$='overForm:userSecondname']")
    private WebElement surname;
    @FindBy(css = "[id$='overForm:userSex']")
    private WebElement sex;
    @FindBy(css = "[id$='overForm:calendar']")
    private WebElement birthDate;
    @FindBy(css = "[id$='overForm:email']")
    private WebElement email;

    public void checkAll(String imgName, String loginName, String firstName, String secondName, String Sex, String birthday,
        String email) {
        checkAvatarImage(imgName);
        checkBirthday(birthday);
        checkEmail(email);
        checkFirstName(firstName);
        checkLoginName(loginName);
        checkSex(Sex);
        checkSurname(secondName);
    }

    public void checkAvatarImage(String imageName) {
        assertTrue(avatarImage.getAttribute("src").endsWith(imageName));
    }

    public void checkLoginName(String value) {
        assertEquals(value, loginName.getText());
    }

    public void checkFirstName(String value) {
        assertEquals(value, firstName.getText());
    }

    public void checkSurname(String value) {
        assertEquals(value, surname.getText());
    }

    public void checkSex(String value) {
        assertEquals(value, sex.getText());
    }

    public void checkBirthday(String value) {
        assertEquals(value, birthDate.getText());
    }

    public void checkEmail(String value) {
        assertEquals(value, email.getText());
    }

    public void openEditProfile() {
        Graphene.guardAjax(editProfileButton).click();
    }
}

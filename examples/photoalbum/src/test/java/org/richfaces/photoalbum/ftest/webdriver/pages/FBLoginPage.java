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
package org.richfaces.photoalbum.ftest.webdriver.pages;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.ClearType;
import org.richfaces.fragment.common.TextInputComponentImpl;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class FBLoginPage implements SocialLoginPage {

    @FindBy(css = "input[id$=email]")
    private TextInputComponentImpl emailInput;
    @FindBy(css = "input[id$=pass]")
    private TextInputComponentImpl passwordInput;
    @FindBy(css = "input[name='login']")
    private WebElement loginButton;

    @Override
    public void login(String email, String password) {
        Graphene.waitModel().until().element(emailInput.advanced().getInputElement()).is().visible();
        emailInput.advanced().clear(ClearType.DELETE).sendKeys(email);
        passwordInput.advanced().clear(ClearType.DELETE).sendKeys(password);
        loginButton.click();
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException();
    }
}

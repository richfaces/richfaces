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
package org.richfaces.showcase.commandLink;

import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.showcase.commandButton.AbstractTestA4jCommand;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @version $Revision$
 */
public class ITestCommandLink extends AbstractTestA4jCommand {

    /* *****************************************************************************
     * Locators*****************************************************************************
     */
    @FindBy(css = "form a")
    protected WebElement commandLink;

    /* ******************************************************************************
     * Tests******************************************************************************
     */
    @Test
    public void testClickOnTheButtonWhileInputIsEmpty() {
        checkClickOnTheButtonWhileInputIsEmpty("Hello !");
    }

    @Test
    public void testTypeSomeCharactersAndClickOnTheButton() {
        checkTypeSomeCharactersAndClickOnTheButton();
    }

    @Test
    public void testEraseSomeStringAndClickOnTheButton() {
        checkEraseSomeStringAndClickOnTheButton("Hello !");
    }

    @Override
    protected WebElement getCommand() {
        return commandLink;
    }

}

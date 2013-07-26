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
package org.richfaces.tests.showcase.focus.page;

import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.page.fragments.impl.input.TextInputComponentImpl;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class FocusPage {

    @FindBy(jquery = "*[type=text]:eq(0)")
    public TextInputComponentImpl nameInput;

    @FindBy(jquery = "*[type=text]:eq(1)")
    public TextInputComponentImpl jobInput;

    @FindBy(jquery = "*[type=text]:eq(2)")
    public TextInputComponentImpl addressnput;

    @FindBy(jquery = "*[type=text]:eq(3)")
    public TextInputComponentImpl zipInput;

    @FindBy(jquery = "*[type=submit]")
    public WebElement submitButton;

    public static void typeSomethingAndDoNotCareAboutFocus(String keys) {
        Actions builder = new Actions(GrapheneContext.getProxy());

        builder.sendKeys(keys);
        builder.build().perform();
    }
}

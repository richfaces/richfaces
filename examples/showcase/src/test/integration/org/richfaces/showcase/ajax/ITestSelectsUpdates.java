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
package org.richfaces.showcase.ajax;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.richfaces.showcase.AbstractWebDriverTest;

/**
 * 
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @version $Revision$
 */
public class ITestSelectsUpdates extends AbstractWebDriverTest {

    /* *************************************************************************************
     * Locators*************************************************************************************
     */

    @FindByJQuery("fieldset form select:eq(0)")
    protected WebElement firstSelect;
    @FindByJQuery("div[id$='second'] select")
    protected WebElement secondSelect;

    /* ***************************************************************************************
     * Tests***************************************************************************************
     */

    @Test
    public void testDynamicSelects() {

        List<String> fruitsExpected = new ArrayList<String>();
        fruitsExpected.add("");
        fruitsExpected.add("Banana");
        fruitsExpected.add("Cranberry");
        fruitsExpected.add("Blueberry");
        fruitsExpected.add("Orange");

        List<String> vegetablesExpected = new ArrayList<String>();
        vegetablesExpected.add("");
        vegetablesExpected.add("Potatoes");
        vegetablesExpected.add("Broccoli");
        vegetablesExpected.add("Garlic");
        vegetablesExpected.add("Carrot");

        assertTrue("First select should be displayed!", firstSelect.isDisplayed());

        // assertFalse(secondSelect.isDisplayed(), "Second select should be dispayed");

        Graphene.guardAjax(new Select(firstSelect)).selectByVisibleText("Fruits");

        List<String> fruitsActual = new ArrayList<String>();
        List<WebElement> fruitsOptions = new Select(secondSelect).getOptions();
        for (WebElement option : fruitsOptions) {
            fruitsActual.add(option.getText().trim());
        }

        assertTrue("Second select should be dispayed", secondSelect.isDisplayed());

        assertEquals("When selected fruits in first select, in the second " + "should be some examples of Fruits",
            fruitsExpected, fruitsActual);

        Graphene.guardAjax(new Select(firstSelect)).selectByVisibleText("Vegetables");

        List<String> vegetablesActual = new ArrayList<String>();
        List<WebElement> vegetablesOptions = new Select(secondSelect).getOptions();
        for (WebElement option : vegetablesOptions) {
            vegetablesActual.add(option.getText().trim());
        }

        assertEquals("When selected vegetables in first select, in the second " + "should be some examples of vegetables",
            vegetablesExpected, vegetablesActual);

    }

}

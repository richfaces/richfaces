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
package org.richfaces.tests.showcase.jsFunction;

import static org.junit.Assert.assertEquals;

import java.util.Map.Entry;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.android.AndroidDriver;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.showcase.AbstractWebDriverTest;
import org.richfaces.tests.showcase.jsFunction.page.JsFunctionPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @version $Revision$
 */
public class ITJsFunction extends AbstractWebDriverTest {

    /* *****************************************************************************
     * Locators*****************************************************************************
     */

    @Page
    private JsFunctionPage page;

    @ArquillianResource
    private Actions actions;

    /* ******************************************************************************
     * Tests******************************************************************************
     */

    @Test
    public void testInitialState() {
        assertEquals("The text in output should be empty", page.output.getText().trim(), "");
    }

    @Test
    public void testMouseOverSpecificTdElement() {
        /*
         * Move mouse over all td elements and check whether the showName is same as text of particular td element then
         * move mouse out of td element and check whether the output is empty
         */

        for (Entry<String, WebElement> entry: page.getNames().entrySet()) {
            String name = entry.getKey();
            WebElement element = entry.getValue();
            activate(element);
            Graphene.waitGui()
                    .until("The text in output should be same as in the active td!")
                    .element(page.output)
                    .text()
                    .equalTo(name);
        }
    }

    private void activate(WebElement element) {
        if (webDriver instanceof AndroidDriver) {
            element.click();
        } else {
            actions.moveToElement(element).perform();
        }
    }

}

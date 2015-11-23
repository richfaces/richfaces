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
package org.richfaces.showcase.select;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.select.RichFacesSelect;
import org.richfaces.showcase.AbstractWebDriverTest;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class ITestSelect extends AbstractWebDriverTest {

    @FindByJQuery("div.rf-sel:eq(0)")
    private RichFacesSelect first;
    @FindByJQuery("div.rf-sel:eq(1)")
    private RichFacesSelect second;

    @Test
    public void testSelectManualInputByMouse() {
        selectSomethingFromCapitalsSelectAndCheck("Arizona");
        selectSomethingFromCapitalsSelectAndCheck("Florida");
        selectSomethingFromCapitalsSelectAndCheck("California");
    }

    @Test
    public void testSimpleSelectMouseSelect() {
        for (int i = 0; i < 5; i++) {
            first.openSelect();
            assertTrue(first.advanced().isPopupPresent());
            assertFalse(second.advanced().isPopupPresent());
            Graphene.guardNoRequest(first.openSelect()).select(i);
            assertEquals("Option " + (i + 1), first.advanced().getInput().getStringValue());
        }
    }

    /* *******************************************************************************
     * Help methods ************************************************************** *****************
     */

    /**
     * Types the beginning of capital and then selects from popup and check whether right option was selected
     */
    private void selectSomethingFromCapitalsSelectAndCheck(String capital) {
        second.type(capital.substring(0, 2));

        assertTrue(second.advanced().isPopupPresent());

        for (WebElement option : second.advanced().getSuggestionsElements()) {
            assertTrue("The option '" + option.getText() + "' doesn't start with '" + capital.substring(0, 2) + "'.", option
                .getText().startsWith(capital.substring(0, 2)));
        }

        Graphene.guardNoRequest(second.openSelect()).select(0);
        assertEquals(capital, second.advanced().getInput().getStringValue());
    }
}
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
package org.richfaces.tests.showcase.select;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.junit.Assert;
import org.junit.Test;
import org.richfaces.tests.page.fragments.impl.input.TextInputComponent;
import org.richfaces.tests.page.fragments.impl.input.select.Option;
import org.richfaces.tests.page.fragments.impl.input.select.OptionList;
import org.richfaces.tests.page.fragments.impl.input.select.RichFacesSelect;
import org.richfaces.tests.page.fragments.impl.input.select.Select;
import org.richfaces.tests.showcase.AbstractWebDriverTest;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @version $Revision$
 */
public class ITSelect extends AbstractWebDriverTest {

    @FindBy(jquery = "div.rf-sel:eq(0)")
    private RichFacesSelect first;
    @FindBy(jquery = "div.rf-sel:eq(1)")
    private RichFacesSelect second;

    @Test
    public void testSimpleSelectMouseSelect() {
        for (int i = 0; i < 5; i++) {
            OptionList popup = first.callPopup();
            Assert.assertTrue(first.isPopupPresent());
            Assert.assertFalse(second.isPopupPresent());
            popup.selectByIndex(i);
            int selected = first.getSelectedOption().getIndex();
            Assert.assertEquals("The option with index <" + i + "> should be selected.", selected, i);
        }
    }

    @Test
    public void testSelectManualInputByMouse() {
        selectSomethingFromCapitalsSelectAndCheck(second, "Arizona");
        selectSomethingFromCapitalsSelectAndCheck(second, "Florida");
        selectSomethingFromCapitalsSelectAndCheck(second, "California");
    }

    /* *******************************************************************************
     * Help methods ************************************************************** *****************
     */

    /**
     * Types the beginning of capital and then selects from poppup and check whether right option was selected
     */
    private void selectSomethingFromCapitalsSelectAndCheck(Select select, String capital) {
        select.getInput().clear(TextInputComponent.ClearType.BACKSPACE);
        select.getInput().fillIn(capital.substring(0, 2));

        Assert.assertTrue(select.isPopupPresent());

        OptionList popup = select.callPopup();
        Assert.assertTrue(select.isPopupPresent());

        for (Option option : popup.getOptions()) {
            Assert.assertTrue("The option '" + option.getVisibleText() + "' doesn't start with '" + capital.substring(0, 2)
                + "'.", option.getVisibleText().startsWith(capital.substring(0, 2)));
        }

        Option expected = popup.selectByIndex(0);
        Assert.assertEquals(select.getSelectedOption().getVisibleText(), expected.getVisibleText());

    }

}

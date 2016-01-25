/*
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
 */
package org.richfaces.showcase.autocomplete;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.richfaces.fragment.autocomplete.RichFacesAutocomplete;
import org.richfaces.fragment.autocomplete.SelectOrConfirm;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.showcase.AbstractWebDriverTest;
import org.richfaces.showcase.autocomplete.page.CustomLayoutsPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class ITestCustomLayouts extends AbstractWebDriverTest {

    @Page
    public CustomLayoutsPage page;

    private final String EXP_SUGG_AFTER_V_FST_INPUT = "Vermont Montpelier";
    private final String EXP_SUGG_AFTER_ALA_FST_INPUT = "Alabama Montgomery";
    private final String EXP_SUGG_AFTER_V_SC_INPUT = "Vermont - (Montpelier)";
    private final String EXP_SUGG_AFTER_ALA_SC_INPUT = "Alabama - (Montgomery)";

    @Test
    public void testAutocompletionOfInputWithTableLayout() {
        typeSomethingToInput_checkThePopup_selectFirst_checkTheInputValue(page.getAutocomplete1(), "v",
            EXP_SUGG_AFTER_V_FST_INPUT, EXP_SUGG_AFTER_V_FST_INPUT.split(" ")[0]);

        typeSomethingToInput_checkThePopup_selectFirst_checkTheInputValue(page.getAutocomplete1(), "ala",
            EXP_SUGG_AFTER_ALA_FST_INPUT, EXP_SUGG_AFTER_ALA_FST_INPUT.split(" ")[0]);
    }

    @Test
    public void testAutocompletionOfInputWithDivLayput() {
        typeSomethingToInput_checkThePopup_selectFirst_checkTheInputValue(page.getAutocomplete2(), "v", EXP_SUGG_AFTER_V_SC_INPUT,
            EXP_SUGG_AFTER_V_SC_INPUT.split(" ")[0]);

        typeSomethingToInput_checkThePopup_selectFirst_checkTheInputValue(page.getAutocomplete2(), "ala",
            EXP_SUGG_AFTER_ALA_SC_INPUT, EXP_SUGG_AFTER_ALA_SC_INPUT.split(" ")[0]);

    }

    private void typeSomethingToInput_checkThePopup_selectFirst_checkTheInputValue(RichFacesAutocomplete autocomplete,
        String whatTotype, String expectedValueInPopup, String expectedValueInInputAfterEnter) {

        SelectOrConfirm suggestions = autocomplete.type(whatTotype);

        autocomplete.advanced().waitForSuggestionsToBeVisible().perform();
        String firstSuggestion = autocomplete.advanced().getSuggestionsElements().get(0).getText();
        assertTrue("The first row of popup should suggest " + expectedValueInPopup + " when " + whatTotype
            + " is typed in input", expectedValueInPopup.equals(firstSuggestion));

        suggestions.select(ChoicePickerHelper.byIndex().first());

        String valueInInput = autocomplete.advanced().getInput().getStringValue();
        assertEquals("The value in input should be different!", expectedValueInInputAfterEnter, valueInInput);

        autocomplete.advanced().getInput().clear();
    }

}

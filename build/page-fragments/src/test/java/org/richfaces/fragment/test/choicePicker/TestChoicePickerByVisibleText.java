/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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

package org.richfaces.fragment.test.choicePicker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.fragment.common.picker.ChoicePickerHelper.ByVisibleTextChoicePicker;

public class TestChoicePickerByVisibleText extends AbstractChoicePickerTest {

    @Test
    public void testMultipleChoicePickerNonExistingElement() {
        ByVisibleTextChoicePicker picker = ChoicePickerHelper.byVisibleText().match("non existing");
        List<WebElement> elements = picker.pickMultiple(myFragment.getDivs());
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testPickExistingElement() {
        ByVisibleTextChoicePicker picker = ChoicePickerHelper.byVisibleText().match("1");
        WebElement element = picker.pick(myFragment.getDivs());
        assertNotNull(element);
        assertEquals("1", element.getText());
    }

    @Test
    public void testPickNotExistingElement() {
        ByVisibleTextChoicePicker picker = ChoicePickerHelper.byVisibleText().match("non existing");
        WebElement element = picker.pick(myFragment.getDivs());
        assertNull(element);
    }
}

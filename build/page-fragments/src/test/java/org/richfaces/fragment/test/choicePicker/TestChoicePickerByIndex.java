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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.fragment.common.picker.ChoicePickerHelper.ByIndexChoicePicker;

import com.google.common.collect.Lists;

public class TestChoicePickerByIndex extends AbstractChoicePickerTest {

    @Test
    public void testPickEvery2nd() {
        ByIndexChoicePicker picker = ChoicePickerHelper.byIndex().everyNth(2);
        List<WebElement> pickMultiple = picker.pickMultiple(myFragment.getDivs());
        assertEquals(Lists.newArrayList("1", "3", "5"), getStringsFromElements(pickMultiple));
    }

    @Test
    public void testPickEvery2ndFromSecond() {
        ByIndexChoicePicker picker = ChoicePickerHelper.byIndex().everyNth(2, 1);
        List<WebElement> pickMultiple = picker.pickMultiple(myFragment.getDivs());
        assertEquals(Lists.newArrayList("2", "4", "6"), getStringsFromElements(pickMultiple));
    }

    @Test
    public void testPickEvery2ndFromTooHighIndex() {
        ByIndexChoicePicker picker = ChoicePickerHelper.byIndex().everyNth(2, 6);
        List<WebElement> pickMultiple = picker.pickMultiple(myFragment.getDivs());
        assertTrue(pickMultiple.isEmpty());
    }

    @Test
    public void testPickMultiple() {
        ByIndexChoicePicker picker = ChoicePickerHelper.byIndex().indexes(1, 2).first().last().beforeLast(1);
        List<WebElement> pickMultiple = picker.pickMultiple(myFragment.getDivs());
        assertEquals(Lists.newArrayList("2", "3", "1", "6", "5"), getStringsFromElements(pickMultiple));
    }

    @Test
    public void testPickMultipleTimesSameIndexes() {
        ByIndexChoicePicker picker = ChoicePickerHelper.byIndex()
            .first().index(0)// the first element
            .index(1).indexes(0, 1)// the first two elements
            .last().beforeLast(0).index(5);// the last element

        List<WebElement> pickMultiple = picker.pickMultiple(myFragment.getDivs());
        assertEquals(Lists.newArrayList("1", "2", "6"), getStringsFromElements(pickMultiple));
    }

    @Test
    public void testPickNotExistingElement() {
        ByIndexChoicePicker picker = ChoicePickerHelper.byIndex().index(15);
        assertNull(picker.pick(myFragment.getDivs()));
    }
}
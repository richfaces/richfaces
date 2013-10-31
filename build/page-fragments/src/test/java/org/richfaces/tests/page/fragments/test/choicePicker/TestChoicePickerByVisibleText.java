package org.richfaces.tests.page.fragments.test.choicePicker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper.ByVisibleTextChoicePicker;

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

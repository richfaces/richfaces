package org.richfaces.tests.page.fragments.test.choicePicker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper.ByVisibleTextChoicePicker;
import org.richfaces.tests.page.fragments.test.choicePicker.fragments.MyPageFragment;

@RunWith(Arquillian.class)
public class TestChoicePickerByVisibleText {

    @Drone
    private WebDriver browser;

    @FindBy(tagName = "body")
    private MyPageFragment myFragment;

    @Test
    public void testMultipleChoicePickerNonExistingElement() {
        browser.get(TestChoicePickerByVisibleText.class.getResource("choicePickerByVisibleText.html").toExternalForm());

        ByVisibleTextChoicePicker picker = ChoicePickerHelper.byVisibleText().match("non existing");
        List<WebElement> elements = picker.pickMultiple(myFragment.getDivs());
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testPickExistingElement() {
        browser.get(TestChoicePickerByVisibleText.class.getResource("choicePickerByVisibleText.html").toExternalForm());

        ByVisibleTextChoicePicker picker = ChoicePickerHelper.byVisibleText().match("1");
        WebElement element = picker.pick(myFragment.getDivs());
        assertNotNull(element);
        assertEquals("1", element.getText());
    }

    @Test
    public void testPickNotExistingElement() {
        browser.get(TestChoicePickerByVisibleText.class.getResource("choicePickerByVisibleText.html").toExternalForm());

        ByVisibleTextChoicePicker picker = ChoicePickerHelper.byVisibleText().match("non existing");
        WebElement element = picker.pick(myFragment.getDivs());
        assertNull(element);
    }
}

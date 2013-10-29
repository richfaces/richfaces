package org.richfaces.tests.page.fragments.test.choicePicker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper.ByIndexChoicePicker;
import org.richfaces.tests.page.fragments.test.choicePicker.fragments.MyPageFragment;

import com.google.common.collect.Lists;

@RunWith(Arquillian.class)
public class TestChoicePickerByIndex {

    @Drone
    private WebDriver browser;

    @FindBy(tagName = "body")
    private MyPageFragment myFragment;

    private List<String> getStringsFromElements(List<WebElement> list) {
        List<String> result = Lists.newArrayList();
        for (WebElement webElement : list) {
            result.add(webElement.getText());
        }
        return result;
    }

    @Test
    public void testPickEvery2nd() {
        browser.get(TestChoicePickerByVisibleText.class.getResource("choicePickerByVisibleText.html").toExternalForm());

        ByIndexChoicePicker picker = ChoicePickerHelper.byIndex().everyNth(2);
        List<WebElement> pickMultiple = picker.pickMultiple(myFragment.getDivs());
        assertEquals(Lists.newArrayList("1", "3", "5"), getStringsFromElements(pickMultiple));
    }

    @Test
    public void testPickEvery2ndFromSecond() {
        browser.get(TestChoicePickerByVisibleText.class.getResource("choicePickerByVisibleText.html").toExternalForm());

        ByIndexChoicePicker picker = ChoicePickerHelper.byIndex().everyNth(2, 1);
        List<WebElement> pickMultiple = picker.pickMultiple(myFragment.getDivs());
        assertEquals(Lists.newArrayList("2", "4", "6"), getStringsFromElements(pickMultiple));
    }

    @Test
    public void testPickEvery2ndFromTooHighIndex() {
        browser.get(TestChoicePickerByVisibleText.class.getResource("choicePickerByVisibleText.html").toExternalForm());

        ByIndexChoicePicker picker = ChoicePickerHelper.byIndex().everyNth(2, 6);
        List<WebElement> pickMultiple = picker.pickMultiple(myFragment.getDivs());
        assertTrue(pickMultiple.isEmpty());
    }

    @Test
    public void testPickMultiple() {
        browser.get(TestChoicePickerByVisibleText.class.getResource("choicePickerByVisibleText.html").toExternalForm());

        ByIndexChoicePicker picker = ChoicePickerHelper.byIndex().indexes(1, 2).first().last().beforeLast(1);
        List<WebElement> pickMultiple = picker.pickMultiple(myFragment.getDivs());
        assertEquals(Lists.newArrayList("2", "3", "1", "6", "5"), getStringsFromElements(pickMultiple));
    }

    @Test
    public void testPickMultipleTimesSameIndexes() {
        browser.get(TestChoicePickerByVisibleText.class.getResource("choicePickerByVisibleText.html").toExternalForm());

        ByIndexChoicePicker picker = ChoicePickerHelper.byIndex()
            .first().index(0)// the first element
            .index(1).indexes(0, 1)// the first two elements
            .last().beforeLast(0).index(5);// the last element

        List<WebElement> pickMultiple = picker.pickMultiple(myFragment.getDivs());
        assertEquals(Lists.newArrayList("1", "2", "6"), getStringsFromElements(pickMultiple));
    }

    @Test
    public void testPickNotExistingElement() {
        browser.get(TestChoicePickerByVisibleText.class.getResource("choicePickerByVisibleText.html").toExternalForm());

        ByIndexChoicePicker picker = ChoicePickerHelper.byIndex().index(15);
        assertNull(picker.pick(myFragment.getDivs()));
    }
}

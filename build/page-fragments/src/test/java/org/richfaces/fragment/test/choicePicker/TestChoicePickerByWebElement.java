package org.richfaces.fragment.test.choicePicker;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.fragment.common.picker.MultipleChoicePicker;

import com.google.common.collect.Lists;

public class TestChoicePickerByWebElement {

    @Mock
    protected MyPageFragment myFragment;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        List<WebElement> divs = new ArrayList<WebElement>();
        for (int i = 1; i <= 6; i++) {
            WebElement elem = mock(WebElement.class);
            when(elem.getText()).thenReturn("" + i);
            switch(i) {
                case 1 : when(elem.getAttribute("class")).thenReturn("odd first"); break;
                case 2 : when(elem.getAttribute("class")).thenReturn("even second"); break;
                case 3 : when(elem.getAttribute("class")).thenReturn("odd"); break;
                case 4 : when(elem.getAttribute("class")).thenReturn("even"); break;
                case 5 : when(elem.getAttribute("class")).thenReturn("odd"); break;
                case 6 : when(elem.getAttribute("class")).thenReturn("even last"); break;
            }
            divs.add(elem);
        }
        when(myFragment.getDivs()).thenReturn(divs);
    }

    protected List<String> getStringsFromElements(List<WebElement> list) {
        List<String> result = Lists.newArrayList();
        for (WebElement webElement : list) {
            result.add(webElement.getText());
        }
        return result;
    }

    @Test
    public void testPickByStyleClassEvenANDOddElements() {
        MultipleChoicePicker picker = ChoicePickerHelper.byWebElement().attribute("class").contains("even").and()
            .attribute("class").contains("odd");
        List<WebElement> elements = picker.pickMultiple(myFragment.getDivs());
        assertEquals(Collections.EMPTY_LIST, getStringsFromElements(elements));
    }

    @Test
    public void testPickByStyleClassEvenElements() {
        MultipleChoicePicker evenPicker = ChoicePickerHelper.byWebElement().attribute("class").contains("even");
        List<WebElement> elements = evenPicker.pickMultiple(myFragment.getDivs());
        assertEquals(Lists.newArrayList("2", "4", "6"), getStringsFromElements(elements));
    }

    @Test
    public void testPickByStyleClassEvenElementsIsEqualToPickNotOdd() {
        MultipleChoicePicker evenPicker = ChoicePickerHelper.byWebElement().attribute("class").contains("even");
        MultipleChoicePicker notOddPicker = ChoicePickerHelper.byWebElement().attribute("class").contains("odd").not();
        assertEquals(Lists.newArrayList("2", "4", "6"), getStringsFromElements(evenPicker.pickMultiple(myFragment.getDivs())));
        assertEquals(Lists.newArrayList("2", "4", "6"), getStringsFromElements(notOddPicker.pickMultiple(myFragment.getDivs())));
        assertEquals(getStringsFromElements(evenPicker.pickMultiple(myFragment.getDivs())),
            getStringsFromElements(notOddPicker.pickMultiple(myFragment.getDivs())));
    }

    @Test
    public void testPickByStyleClassFirstAndLastElements() {
        MultipleChoicePicker oddPicker = ChoicePickerHelper.byWebElement().attribute("class").contains("first").and()
            .attribute("class").contains("last");
        List<WebElement> elements = oddPicker.pickMultiple(myFragment.getDivs());
        assertEquals(Lists.<String>newArrayList(), getStringsFromElements(elements));
    }

    @Test
    public void testPickByStyleClassOddElements() {
        MultipleChoicePicker oddPicker = ChoicePickerHelper.byWebElement().attribute("class").contains("odd");
        List<WebElement> elements = oddPicker.pickMultiple(myFragment.getDivs());
        assertEquals(Lists.newArrayList("1", "3", "5"), getStringsFromElements(elements));
    }

    @Test
    public void testPickByStyleClassOddOrEvenElements() {
        MultipleChoicePicker oddPicker = ChoicePickerHelper.byWebElement().attribute("class").contains("odd").or()
            .attribute("class").contains("even");
        List<WebElement> elements = oddPicker.pickMultiple(myFragment.getDivs());
        assertEquals(Lists.newArrayList("1", "2", "3", "4", "5", "6"), getStringsFromElements(elements));
    }

    @Test
    public void testPickByStyleClassOddOrFirstOrSecondOrLastElements() {
        MultipleChoicePicker picker = ChoicePickerHelper.byWebElement().attribute("class").contains("odd").or()
            .attribute("class").contains("first").or().attribute("class").contains("second").or().attribute("class")
            .contains("last");
        List<WebElement> elements = picker.pickMultiple(myFragment.getDivs());
        assertEquals(Lists.newArrayList("1", "2", "3", "5", "6"), getStringsFromElements(elements));
    }

    @Test
    public void testPickByStyleClassOddWithoutFirstElements() {
        MultipleChoicePicker oddPicker = ChoicePickerHelper.byWebElement().attribute("class").contains("odd").and()
            .attribute("class").contains("first").not();
        List<WebElement> elements = oddPicker.pickMultiple(myFragment.getDivs());
        assertEquals(Lists.newArrayList("3", "5"), getStringsFromElements(elements));
    }

}

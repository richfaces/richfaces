package org.richfaces.fragment.test.choicePicker;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.WebElement;

import com.google.common.collect.Lists;

public class AbstractChoicePickerTest {

    @Mock
    protected MyPageFragment myFragment;

    @Before
    public void setUp() {
      MockitoAnnotations.initMocks(this);

      List<WebElement> divs = new ArrayList<WebElement>();
      for(int i = 1; i <= 6; i++) {
          WebElement elem = mock(WebElement.class);
          when(elem.getText()).thenReturn("" + i);
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
}
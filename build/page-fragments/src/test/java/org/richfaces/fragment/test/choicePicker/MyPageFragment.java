package org.richfaces.fragment.test.choicePicker;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

class MyPageFragment {

    @FindBy(tagName = "div")
    private List<WebElement> divs;

    public List<WebElement> getDivs() {
        return divs;
    }
}
package org.richfaces.tests.page.fragments.test.choicePicker.fragments;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MyPageFragment {

    @FindBy(tagName = "div")
    private List<WebElement> divs;

    public List<WebElement> getDivs() {
        return divs;
    }
}
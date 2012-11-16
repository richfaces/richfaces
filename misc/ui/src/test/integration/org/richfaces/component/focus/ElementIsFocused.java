package org.richfaces.component.focus;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.base.Predicate;

public class ElementIsFocused implements Predicate<WebDriver> {

    private WebElement element;

    /**
     * Provide element to wait to gain focus or null if you want to fail for no element having focus
     */
    public ElementIsFocused(WebElement element) {
        this.element = element;
    }

    @Override
    public boolean apply(WebDriver browser) {
        try {
            if (element == null) {
                return FocusRetriever.retrieveActiveElement() == null;
            }
            return element.equals(FocusRetriever.retrieveActiveElement());
        } catch (StaleElementReferenceException e) {
            return false;
        }
    }
}

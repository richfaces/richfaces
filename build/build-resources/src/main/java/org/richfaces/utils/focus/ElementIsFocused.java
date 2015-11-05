package org.richfaces.utils.focus;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.base.Predicate;

public class ElementIsFocused implements Predicate<WebDriver> {

    private WebElement activeElement;
    private final WebElement element;

    /**
     * Provide element to wait to gain focus or null if you want to fail for no element having focus
     */
    public ElementIsFocused(WebElement element) {
        this.element = element;
    }

    @Override
    public boolean apply(WebDriver browser) {
        try {
            activeElement = FocusRetriever.retrieveActiveElement();
            if (element == null) {
                return activeElement == null;
            }
            return activeElement.equals(element);
        } catch (StaleElementReferenceException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        String focused = (activeElement == null) ? null : ((activeElement.getAttribute("id") != null) ? activeElement.getAttribute("id") : activeElement.toString());
        return String.format("waiting for the focus on '%s' failed - last focused element: %s", element, focused);
    }
}

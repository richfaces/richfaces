package org.richfaces.showcase;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.junit.Before;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;

public class AbstractWebDriverTest extends AbstractShowcaseTest {

    @Drone
    protected WebDriver webDriver;

    @Before
    public void loadPage() {
        String addition = getAdditionToContextRoot();
        this.contextRoot = getContextRoot();
        if (runInPortalEnv) {
            webDriver.get(String.format("%s://%s:%s/%s", contextRoot.getProtocol(), contextRoot.getHost(),
                contextRoot.getPort(), "portal/classic/showcase"));
            JavascriptExecutor js = (JavascriptExecutor) webDriver;
            String setTextQuery = "document.querySelector(\"input[id$='portalForm:%s']\").value = '%s';";
            js.executeScript(String.format(setTextQuery, "seleniumTestDemo", getDemoName()));
            js.executeScript(String.format(setTextQuery, "seleniumTestSample", getSampleName()));
            js.executeScript("document.querySelector(\"a[id$='portalForm:redirectToPortlet']\").click()");
        } else {
            webDriver.manage().deleteAllCookies();// remove state from previous test
            webDriver.get(contextRoot.toExternalForm() + addition);
        }
    }

    protected Action fireEventAction(final WebElement element, final String event) {
        return new EventAction(webDriver, element, event);
    }

    protected void fireEvent(WebElement element, String event) {
        fireEventAction(element, event).perform();
    }

    public static class EventAction implements Action {

        private final WebDriver driver;
        private final String event;
        private final WebElement element;

        public EventAction(WebDriver driver, WebElement element, String event) {
            this.driver = driver;
            this.element = element;
            this.event = event;
        }

        @Override
        public void perform() {
            String jQueryCmd = String.format("jQuery(arguments[0]).trigger('%s')", event);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(jQueryCmd, element);
        }
    }

    /**
     * Works only with injected elements.
     */
    public boolean isElementPresent(WebElement element) {
        try {
            element.isDisplayed();
            return true;
        } catch (NoSuchElementException ignored) {
            return false;
        }
    }

    public void waitFor(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
        }
    }
}

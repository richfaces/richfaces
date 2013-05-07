package org.richfaces.arquillian.browser;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

class BrowserUtils {

    static boolean isPhantomjs(WebDriver browser) {

        if (browser instanceof PhantomJSDriver) {
            return true;
        }

        if (browser instanceof RemoteWebDriver) {
            Capabilities capabilities = ((RemoteWebDriver) browser).getCapabilities();
            return DesiredCapabilities.phantomjs().getBrowserName().equals(capabilities.getBrowserName());
        }

        return false;
    }
}

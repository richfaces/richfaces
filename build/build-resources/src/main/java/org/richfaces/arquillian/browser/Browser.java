package org.richfaces.arquillian.browser;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.opera.core.systems.OperaDriver;

public enum Browser {

    FIREFOX(FirefoxDriver.class, BrowserType.FIREFOX),
    INTERNET_EXPLORER(InternetExplorerDriver.class),
    CHROME(ChromeDriver.class, BrowserType.CHROME),
    OPERA(OperaDriver.class),
    HTML_UNIT(HtmlUnitDriver.class),
    PHANTOMJS(PhantomJSDriver.class, BrowserType.PHANTOMJS);

    private final Class<?> clazz;
    private final Set<String> browserNames;

    private Browser(Class<?> clazz, String... browserNames) {
        this.clazz = clazz;
        this.browserNames = new HashSet<String>(Arrays.asList(browserNames));
    }

    public static Browser getCurrentType(WebDriver wd) {
        for (Browser type : values()) {
            if (type.clazz.isInstance(wd)) {
                return type;
            }
            if (wd instanceof RemoteWebDriver) {
                String browserName = ((RemoteWebDriver) wd).getCapabilities().getBrowserName();
                if (type.browserNames.contains(browserName)) {
                    return type;
                }
            }
        }
        throw new IllegalArgumentException("Unknown Driver");
    }

    public boolean is(WebDriver wd) {
        Browser type = getCurrentType(wd);
        return type == this;
    }
}
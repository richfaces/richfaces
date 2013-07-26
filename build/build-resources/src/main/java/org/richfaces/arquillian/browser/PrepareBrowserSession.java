package org.richfaces.arquillian.browser;

import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.drone.impl.DroneContext;
import org.jboss.arquillian.drone.spi.DroneReady;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

/**
 * <p>
 * Prepares PhantomJS browser to run effectively with fundamental tests
 * </p>
 * 
 * <ul>
 * <li>resizes the browser window to 1280x1024</li>
 * </ul>
 * 
 * @author Lukas Fryc
 */
public class PrepareBrowserSession {

    public void prepare(@Observes DroneReady event, DroneContext ctx) {

        WebDriver browser = (WebDriver) event.getInstance();
        if (BrowserUtils.isPhantomjs(browser)) {
            browser.manage().window().setSize(new Dimension(1280, 1024));
        }
    }

}

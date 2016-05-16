package org.richfaces.arquillian.browser;

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.drone.spi.DroneContext;
import org.jboss.arquillian.drone.spi.DronePoint;
import org.jboss.arquillian.drone.spi.event.AfterDronePrepared;
import org.openqa.selenium.WebDriver;

/**
 * Sets default page load timeout of WebDriver instance to 20 seconds.
 * See https://issues.jboss.org/browse/RF-14290
 */
public class PageLoadTimeoutSetter {

    @Inject
    Instance<DroneContext> droneContext;

    public void setPageLoadTimeout(@Observes AfterDronePrepared event) {
        DronePoint<?> dronePoint = event.getDronePoint();

        if (!dronePoint.conformsTo(WebDriver.class)) {
            // This Drone is not instance of WebDriver, we will not set the page load timeout
            return;
        }

        DroneContext context = droneContext.get();
        if (context == null) {
            throw new IllegalArgumentException("DroneContext must not be null");
        }

        WebDriver driver = context.get(dronePoint).getInstanceAs(WebDriver.class);
        driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
    }
}

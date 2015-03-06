package org.richfaces.arquillian.browser;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.drone.spi.DroneContext;
import org.jboss.arquillian.drone.spi.DronePoint;
import org.jboss.arquillian.drone.spi.event.AfterDroneEnhanced;
import org.jboss.arquillian.drone.spi.event.DroneEvent;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

/**
 * Resizes browser window to 1280x1024.
 *
 * @author Lukas Fryc
 * @author <a href="https://developer.jboss.org/people/ppitonak">Pavol Pitonak</a>
 */
public class WindowResizer {

    @Inject
    Instance<DroneContext> droneContext;

    public void resizeBrowserWindow(@Observes AfterDroneEnhanced event) {
        resizeWindow(event);
    }

    private void resizeWindow(DroneEvent event) {
        DronePoint<?> dronePoint = event.getDronePoint();

        if (!dronePoint.conformsTo(WebDriver.class)) {
            // This Drone is not instance of WebDriver, we will not resize the window
            return;
        }

        DroneContext context = droneContext.get();
        if (context == null) {
            throw new IllegalArgumentException("DroneContext must not be null");
        }

        WebDriver driver = context.get(dronePoint).getInstanceAs(WebDriver.class);
        driver.manage().window().setSize(new Dimension(1280, 1024));
    }
}


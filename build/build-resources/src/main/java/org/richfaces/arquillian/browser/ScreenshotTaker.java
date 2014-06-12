package org.richfaces.arquillian.browser;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.drone.spi.DroneContext;
import org.jboss.arquillian.drone.spi.event.AfterDroneEnhanced;
import org.jboss.arquillian.drone.webdriver.factory.remote.reusable.ReusableRemoteWebDriver;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.proxy.Interceptor;
import org.jboss.arquillian.graphene.proxy.InvocationContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.richfaces.arquillian.configuration.FundamentalTestConfiguration;
import org.richfaces.arquillian.configuration.FundamentalTestConfigurationContext;

/**
 * Takes screenshot for each interaction with WebDriver API which allows debugging of headless browsers capable of screenshot
 * taking
 *
 * @author Lukas Fryc
 */
public class ScreenshotTaker {

    private final Logger log = Logger.getLogger(ScreenshotTaker.class.getName());

    public void registerInterceptor(@Observes AfterDroneEnhanced event, DroneContext ctx) {

        FundamentalTestConfiguration configuration = FundamentalTestConfigurationContext.getProxy();

        if (configuration.isDebug()) {

            WebDriver browser = (WebDriver) event.getInstance();
            if (BrowserUtils.isPhantomjs(browser)) {

                WebDriver proxy = GrapheneContext.getContextFor(Default.class).getWebDriver();

                Interceptor interceptor = new Interceptor() {
                    @Override
                    public Object intercept(InvocationContext ctx) throws Throwable {
                        Object result = ctx.invoke();
                        if (TakesScreenshot.class != ctx.getMethod().getDeclaringClass()) {
                            try {
                                TakesScreenshot takesScreenshot = (TakesScreenshot) getTakingScreenshotsBrowser();
                                File tempFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
                                FileUtils.copyFile(tempFile, new File("target/screenshot.png"));
                            } catch (Exception e) {
                                log.log(Level.WARNING,
                                        String.format("Wasn't able to take screenshot before action '%s'", ctx.getMethod(), e));
                            }
                        }
                        return result;
                    }
                };

                ((GrapheneProxyInstance) proxy).registerInterceptor(interceptor);
            }
        }
    }

    public static WebDriver getTakingScreenshotsBrowser() {
        WebDriver result = ((GrapheneProxyInstance) (GrapheneContext.getContextFor(Default.class)
            .getWebDriver(TakesScreenshot.class))).unwrap();
        if (result instanceof ReusableRemoteWebDriver) {
            result = new Augmenter().augment(result);
        }
        return result;
    }
}

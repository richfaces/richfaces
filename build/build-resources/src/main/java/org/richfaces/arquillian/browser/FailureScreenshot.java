package org.richfaces.arquillian.browser;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.graphene.GrapheneContext;
import org.jboss.arquillian.test.spi.TestResult;
import org.jboss.arquillian.test.spi.TestResult.Status;
import org.jboss.arquillian.test.spi.event.suite.After;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

/**
 * Takes a screenshot on each test failure
 *
 * @author Lukas Fryc
 */
public class FailureScreenshot {

    private final File screenshotDir = new File("target/screenshots/");

    public void takeScreenshotOnFailure(@Observes(precedence = 500) After event, TestResult result) throws IOException {
        if (result.getStatus() == Status.FAILED) {
            TakesScreenshot takesScreenshot = (TakesScreenshot) GrapheneContext.getContextFor(Default.class).getWebDriver(TakesScreenshot.class);
            File tempFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
            String screenshotName = event.getTestClass().getJavaClass().getName() + "." + event.getTestMethod().getName();

            screenshotDir.mkdirs();

            File screenshotFile = new File(screenshotDir, screenshotName + ".png");

            FileUtils.copyFile(tempFile, screenshotFile);
        }
    }
}

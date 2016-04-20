package org.richfaces.component.fileUpload;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URL;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.runner.RunWith;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.fileUpload.RichFacesFileUpload;

import com.google.common.base.Predicate;

@RunWith(Arquillian.class)
@RunAsClient
public abstract class IT_RF14257 {

    protected static final long ONE_KB = 1024;

    @Drone
    protected WebDriver browser;

    @ArquillianResource
    protected URL contextPath;

    @ArquillianResource
    protected JavascriptExecutor executor;

    @FindBy(id = "fileUpload")
    protected RichFacesFileUpload fileUpload;

    public static File createFileWithSize(long lengthInBytes) {
        if (lengthInBytes > 0) {
            try {
                File f = new File("target/generatedFile-" + lengthInBytes + "-B.bin");
                if (f.exists()) {
                    return f;// return already generated file
                }
                // create file with needed length
                RandomAccessFile ra = new RandomAccessFile(f, "rw");
                ra.setLength(lengthInBytes);
                ra.close();
                return f;
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    protected void assertOnsizeRejectedTriggered(final int times) {
        Graphene.waitGui().until(new Predicate<WebDriver>() {
            private int onsizerejectedTriggeredTimes = 0;

            @Override
            public boolean apply(WebDriver t) {
                onsizerejectedTriggeredTimes = Integer.valueOf(String.valueOf(executor.executeScript("return window.onsizerejected")));
                return onsizerejectedTriggeredTimes == times;
            }

            @Override
            public String toString() {
                return "onsizerejected to be triggered <" + times + "> times. In the last check, it was triggered <" + onsizerejectedTriggeredTimes + "> times.";
            }
        });
    }
}

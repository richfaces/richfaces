package test.demo;

import java.io.File;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(Arquillian.class)
@RunAsClient
public class TestRF13250 {

    @ArquillianResource
    protected URL contextRoot;

    @Drone
    @Browser1
    protected WebDriver browser1;

    @Drone
    @Browser2
    protected WebDriver browser2;

    @Drone
    @Browser3
    protected WebDriver browser3;

    @Drone
    @Browser4
    protected WebDriver browser4;

    @Deployment
    public static WebArchive createTestArchive() {

        WebArchive war = ShrinkWrap.createFromZipFile(WebArchive.class, new File("target/push.war"));
        return war;
    }

    @Test
    public void should_not_cause_OOM_exception() {
        long timeout = Long.parseLong(System.getProperty("test.timeout"));
        long start = System.currentTimeMillis();

        while (start + timeout > System.currentTimeMillis()) {
            makePushAndThenMalforedRequest(Browser1.class);
            makePushAndThenMalforedRequest(Browser2.class);
            makePushAndThenMalforedRequest(Browser3.class);
            makePushAndThenMalforedRequest(Browser4.class);
        }
    }

    private void makePushAndThenMalforedRequest(Class<?> browserQualifier) {
        ConsumerPage consumerPage = Graphene.goTo(ConsumerPage.class, browserQualifier);
        consumerPage.makeCorrectPush();
//        consumerPage.makeMalformedPushRequest();
    }
}
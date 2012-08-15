package org.richfaces.integration.push;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.junit.Assert.assertEquals;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.ClientAction;
import org.jboss.arquillian.warp.HttpRequest;
import org.jboss.arquillian.warp.RequestFilter;
import org.jboss.arquillian.warp.ServerAssertion;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.extension.servlet.AfterServlet;
import org.jboss.arquillian.warp.extension.servlet.BeforeServlet;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.richfaces.application.push.MessageException;
import org.richfaces.application.push.PushContext;
import org.richfaces.application.push.PushContextFactory;
import org.richfaces.application.push.Session;
import org.richfaces.application.push.TopicKey;
import org.richfaces.application.push.TopicsContext;
import org.richfaces.integration.CoreDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;
import org.richfaces.wait.Condition;
import org.richfaces.wait.Wait;
import org.richfaces.webapp.PushHandlerFilter;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

@WarpTest
public class AbstractPushTest {

    @Drone
    WebDriver driver;

    @ArquillianResource
    URL contextPath;
    
    public static CoreDeployment createBasicDeployment() {

        CoreDeployment deployment = new CoreDeployment(null);


        deployment.addMavenDependency(
                "org.richfaces.ui.common:richfaces-ui-common-api",
                "org.richfaces.ui.common:richfaces-ui-common-ui",
                "org.richfaces.ui.core:richfaces-ui-core-api",
                "org.richfaces.ui.core:richfaces-ui-core-ui");

        FaceletAsset pushPage = new FaceletAsset().body("<script>document.title = 'waiting-for-message'; RichFaces.Push.logLevel = \"debug\";</script>"
                + "<a4j:push address=\"" + Commons.TOPIC + "\" ondataavailable=\"console.log('a4j:push message: ' + event.rf.data); document.title = 'message-received: ' + event.rf.data\" />");

        deployment.archive()
                .addClass(AbstractPushTest.class)
                /** ROOT */
                .addAsWebResource(pushPage, "index.xhtml")
                .addAsResource("META-INF/resources/richfaces-event.js")
                .addAsResource("META-INF/resources/jquery.js")
                .addAsResource("META-INF/resources/richfaces.js")
                .addAsResource("META-INF/resources/richfaces-queue.js")
                .addAsResource("META-INF/resources/richfaces-base-component.js");

        deployment.withResourceHandler();
        deployment.withDependencyInjector();
        deployment.withResourceCodec();
        deployment.withResourceLibraries();
        deployment.withPush();

        return deployment;
    }

    
    public void testSimplePush() {
        Warp.filter(new UriRequestFilter("__richfacesPushAsync")).execute(new ClientAction() {

            @Override
            public void action() {
                driver.navigate().to(contextPath);
            }
        }).verify(new PushServletAssertion());
        
        waitAjax().withTimeout(5,  SECONDS).until(titleIs("message-received: 1"));
    }

    // TODO should be part of Phaser
    public static class UriRequestFilter implements RequestFilter<HttpRequest> {

        private String uriPart;

        public UriRequestFilter(String uriPart) {
            this.uriPart = uriPart;
        }

        @Override
        public boolean matches(HttpRequest httpRequest) {
            String uri = httpRequest.getUri();
            return uri.contains(uriPart);
        }
    }

    public static class PushServletAssertion extends ServerAssertion {

        private static final long serialVersionUID = 1L;

        @ArquillianResource
        HttpServletRequest request;

        @ArquillianResource
        HttpServletResponse response;

        @ArquillianResource
        PushContextFactory pushContextFactory;

        @BeforeServlet
        public void beforeServlet() throws Exception {
            Session session = getCurrentSession();
            assertEquals("messages for current session must be empty before pushing", 0, session.getMessages().size());

            // TODO should be invokable by separate session
            sendMessage("1");
        }

        @AfterServlet
        public void afterServlet() {
            // TODO instead of waiting, we should be able intercept Atmosphere's onBroaddcast/.. methods
            new Wait()
                .failWith("messages for current session must be cleared (empty) after pushing")
                .until(new MessagesAreEmpty());
        }

        private class MessagesAreEmpty implements Condition {
            @Override
            public boolean isTrue() {
                final Session session = getCurrentSession();
                return session.getMessages().size() == 0;
            }
        }

        private Session getCurrentSession() {
            PushContext pushContext = pushContextFactory.getPushContext();
            String pushSessionId = request.getParameter(PushHandlerFilter.PUSH_SESSION_ID_PARAM);
            Session session = pushContext.getSessionManager().getPushSession(pushSessionId);
            return session;
        }

        private void sendMessage(String message) throws MessageException {
            TopicsContext topicsContext = TopicsContext.lookup();
            TopicKey topicKey = new TopicKey(Commons.TOPIC);
            topicsContext.getOrCreateTopic(topicKey);
            topicsContext.publish(topicKey, message);
        }
    }

    public static class Commons {
        public static final String TOPIC = "testingTopic";
    }
}

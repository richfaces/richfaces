package org.richfaces.integration.push;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.integration.CoreDeployment;
import org.richfaces.webapp.PushServlet;

import com.google.common.base.Function;

@RunWith(Arquillian.class)
@WarpTest
public class CustomPushServletMappingTest extends AbstractPushTest {

    @Deployment
    public static WebArchive createDeployment() {
        CoreDeployment deployment = createBasicDeployment();
        
        deployment.webXml(new Function<WebAppDescriptor, WebAppDescriptor>() {
            public WebAppDescriptor apply(WebAppDescriptor webXml) {
                return webXml
                        .getOrCreateServlet()
                            .servletName(PushServlet.class.getSimpleName())
                            .servletClass(PushServlet.class.getName())
                            .asyncSupported(true)
                        .up()
                        .getOrCreateServletMapping()
                            .servletName(PushServlet.class.getSimpleName())
                            .urlPattern("/__custom_mapping")
                        .up()
                        .getOrCreateContextParam()
                            .paramName("org.richfaces.push.handlerMapping")
                            .paramValue("/__custom_mapping")
                        .up();
            }
        });
        
        return deployment.getFinalArchive();
    }

    @Test
    @RunAsClient
    public void test() {
        super.testSimplePush();
    }
}

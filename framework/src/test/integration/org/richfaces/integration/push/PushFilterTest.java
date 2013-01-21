package org.richfaces.integration.push;

import javax.faces.webapp.FacesServlet;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.integration.CoreDeployment;
import org.richfaces.webapp.PushFilter;

import com.google.common.base.Function;

@RunWith(Arquillian.class)
@WarpTest
public class PushFilterTest extends AbstractPushTest {

    @Deployment
    public static WebArchive createDeployment() {
        CoreDeployment deployment = createBasicDeployment();
        
        deployment.webXml(new Function<WebAppDescriptor, WebAppDescriptor>() {
            public WebAppDescriptor apply(WebAppDescriptor webXml) {
                return webXml
                        .getOrCreateFilter()
                            .filterName(PushFilter.class.getSimpleName())
                            .filterClass(PushFilter.class.getName())
                            .asyncSupported(true)
                        .up()
                        .getOrCreateFilterMapping()
                            .filterName(PushFilter.class.getSimpleName())
                            .servletName(FacesServlet.class.getSimpleName())
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

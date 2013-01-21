package org.richfaces.integration.push;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.integration.CoreDeployment;

@RunWith(Arquillian.class)
@WarpTest
public class AutoRegisteredPushServletTest extends AbstractPushTest {

    @Deployment
    public static WebArchive createDeployment() {
        CoreDeployment deployment = createBasicDeployment();
        return deployment.getFinalArchive();
    }

    @Test
    @RunAsClient
    public void test() {
        super.testSimplePush();
    }
}

package org.richfaces.integration.ui.validation;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;
import org.richfaces.integration.FrameworkDeployment;
import org.richfaces.ui.validation.GraphBean;
import org.richfaces.ui.validation.Group;

@RunWith(Arquillian.class)
@RunAsClient
public class GraphValidationTest extends GraphValidationTestBase {

    @Deployment
    public static WebArchive deployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(GraphValidationTest.class);

        deployment.archive().addClasses(GraphBean.class, Group.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }
}

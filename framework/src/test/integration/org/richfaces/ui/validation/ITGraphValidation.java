package org.richfaces.ui.validation;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;
import org.richfaces.deployment.FrameworkDeployment;

@RunWith(Arquillian.class)
@RunAsClient
public class ITGraphValidation extends GraphValidationTestBase {

    @Deployment
    public static WebArchive deployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(ITGraphValidation.class);

        deployment.archive().addClasses(GraphBean.class, Group.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }
}

package org.richfaces.ui.validation;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.junit.runner.RunWith;
import org.richfaces.deployment.FrameworkDeployment;

import com.google.common.base.Function;

@RunWith(Arquillian.class)
@RunAsClient
public class ITFacesBeanValidator extends GraphValidationTestBase {

    @Deployment
    public static WebArchive deployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(ITFacesBeanValidator.class);

        deployment.archive().addClasses(GraphBean.class, Group.class);

        deployment.webXml(new Function<WebAppDescriptor, WebAppDescriptor>() {
            public WebAppDescriptor apply(WebAppDescriptor webXml) {
                 webXml.createContextParam()
                     .paramName("javax.faces.validator.DISABLE_DEFAULT_BEAN_VALIDATOR")
                     .paramValue("true");
                 return webXml;
            }
        });

        ITGraphValidation.addIndexPage(deployment);

        return deployment.getFinalArchive();
    }
}

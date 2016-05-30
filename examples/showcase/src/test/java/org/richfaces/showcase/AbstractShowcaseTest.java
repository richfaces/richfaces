package org.richfaces.showcase;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.eu.ingwar.tools.arquillian.extension.suite.annotations.ArquillianSuiteDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;

@RunAsClient
@RunWith(Arquillian.class)
@ArquillianSuiteDeployment
public class AbstractShowcaseTest {

    @ArquillianResource
    protected URL contextRoot;
    protected static final Boolean runInPortalEnv = Boolean.getBoolean("runInPortalEnv");

    @Deployment(testable = false)
    @OverProtocol("Servlet 3.0")
    public static WebArchive deploy() {
        File warFile2deploy;
        if (runInPortalEnv) {
            warFile2deploy = new File("target/showcase-portlet.war");
        } else {
            warFile2deploy = new File("target/richfaces-showcase-" + System.getProperty("container.classifier") + ".war");
        }
        WebArchive war = ShrinkWrap.createFromZipFile(WebArchive.class, warFile2deploy);
        return war;
    }

    protected String getAdditionToContextRoot() {
        return String.format("richfaces/component-sample.jsf?skin=blueSky&demo=%s&sample=%s", getDemoName(), getSampleName());
    }

    protected URL getContextRoot() {

        String isHTTPTesting = System.getenv("HTTPS_TESTING");

        if (isHTTPTesting != null && isHTTPTesting.equals("true")) {
            try {
                URL httpsUrl = new URL(contextRoot.toExternalForm().replace("http", "https").replace("8080", "8443"));
                return httpsUrl;
            } catch (MalformedURLException e) {
                // it is not malformed URL for sure
            }
        }

        return this.contextRoot;
    }

    protected String getDemoName() {
        // demo name - takes last part of package name
        String demoName = this.getClass().getPackage().getName();
        return demoName.substring(demoName.lastIndexOf(".") + 1);
    }

    protected String getSampleName() {
        // sample name - removes ITest- prefix from class name and uncapitalize
        // first letter
        String sampleName = this.getClass().getSimpleName().substring(5);
        sampleName = ("" + sampleName.charAt(0)).toLowerCase() + sampleName.substring(1);
        return sampleName;
    }

}

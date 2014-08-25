package org.richfaces.showcase;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;
import org.jboss.arquillian.junit.Arquillian;

@RunAsClient
@RunWith(Arquillian.class)
public class AbstractShowcaseTest {
        
    @ArquillianResource
    protected URL contextRoot;   
    protected static final Boolean runInPortalEnv = Boolean.getBoolean("runInPortalEnv");
    
    @Deployment(testable = false)
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
        ShowcaseLayout layout = loadLayout();
        String addition;
        if (layout == ShowcaseLayout.COMMON) {
            addition = String.format("richfaces/component-sample.jsf?skin=blueSky&demo=%s&sample=%s", getDemoName(),
                getSampleName());
        } else {
            addition = String.format("mobile/#%s:%s", getDemoName(), getSampleName());
        }

        return addition;
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

    protected ShowcaseLayout loadLayout() {
        String fromProperties = System.getProperty("showcase.layout", "common");
        return ShowcaseLayout.valueOf(fromProperties.toUpperCase());
    }

    protected static enum ShowcaseLayout {
        COMMON, MOBILE;
    }

}

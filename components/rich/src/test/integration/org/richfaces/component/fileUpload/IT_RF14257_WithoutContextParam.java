package org.richfaces.component.fileUpload;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunWith(Arquillian.class)
@RunAsClient
public class IT_RF14257_WithoutContextParam extends IT_RF14257 {

    private static final long attributeSizeLimit_2KB = 2 * ONE_KB;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITFileUpload.class);

        deployment.archive().addClass(FileUploadBean.class);

        addPageWithAttribute(deployment);
        return deployment.getFinalArchive();
    }

    @Test
    public void testOnsizerejectedTriggering_whenAttributeIsSetUp() {
        browser.get(contextPath.toString() + "withAttribute.jsf");
        // check initial state
        assertOnsizeRejectedTriggered(0);

        // add file passing attribute size limit
        fileUpload.addFile(createFileWithSize(attributeSizeLimit_2KB - ONE_KB));
        assertOnsizeRejectedTriggered(0);

        // add file not passing attribute size limit
        fileUpload.addFile(createFileWithSize(attributeSizeLimit_2KB + ONE_KB));
        assertOnsizeRejectedTriggered(1);
    }

    private static void addPageWithAttribute(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.head("<script type='text/javascript'>window.onsizerejected=0;</script>");

        p.form("<rich:fileUpload id='fileUpload' fileUploadListener='#{fileUploadBean.listener}' onsizerejected='window.onsizerejected++;' maxFileSize='" + attributeSizeLimit_2KB + "' />");

        deployment.archive().addAsWebResource(p, "withAttribute.xhtml");
    }
}

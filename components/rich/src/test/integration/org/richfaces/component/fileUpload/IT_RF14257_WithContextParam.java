package org.richfaces.component.fileUpload;

import javax.annotation.Nullable;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import com.google.common.base.Function;

@RunWith(Arquillian.class)
@RunAsClient
public class IT_RF14257_WithContextParam extends IT_RF14257 {

    private static final long attributeSizeLimit_6KB = 6 * ONE_KB;
    private static final long attributeSizeLimit_2KB = 2 * ONE_KB;
    private static final long contextParamSizeLimit_4KB = 4 * ONE_KB;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITFileUpload.class);

        deployment.archive().addClass(FileUploadBean.class);

        addPageWithAttributeSizeLimitGreaterThanContextParamLimit(deployment);
        addPageWithAttributeSizeLimitLesserThanContextParamLimit(deployment);
        addPageWithoutAttribute(deployment);

        deployment.webXml(new Function<WebAppDescriptor, WebAppDescriptor>() {
            @Override
            public WebAppDescriptor apply(@Nullable WebAppDescriptor input) {
                input
                    .getOrCreateContextParam()
                    .paramName("org.richfaces.fileUpload.maxRequestSize")
                    .paramValue(String.valueOf(contextParamSizeLimit_4KB));
                return input;
            }
        });
        return deployment.getFinalArchive();
    }

    @Test
    public void testOnsizerejectedTriggering_whenAttributeIsGreaterThanContextParam() {
        browser.get(contextPath.toString() + "withAttributeLimit6KB.jsf");
        // check initial state
        assertOnsizeRejectedTriggered(0);

        // add file passing both context param size limit and attribute size limit
        fileUpload.addFile(createFileWithSize(contextParamSizeLimit_4KB - ONE_KB));
        assertOnsizeRejectedTriggered(0);

        // add file not passing context param size limit, but passing attribute size limit
        fileUpload.addFile(createFileWithSize(contextParamSizeLimit_4KB + ONE_KB));
        assertOnsizeRejectedTriggered(0);

        // add file not passing both size limits
        fileUpload.addFile(createFileWithSize(attributeSizeLimit_6KB + ONE_KB));
        assertOnsizeRejectedTriggered(1);
    }

    @Test
    public void testOnsizerejectedTriggering_whenAttributeIsLesserThanContextParam() {
        browser.get(contextPath.toString() + "withAttributeLimit2KB.jsf");
        // check initial state
        assertOnsizeRejectedTriggered(0);

        // add file passing both context param size limit and attribute size limit
        fileUpload.addFile(createFileWithSize(attributeSizeLimit_2KB - ONE_KB));
        assertOnsizeRejectedTriggered(0);

        // add file not passing attribute size limit, but passing context param size limit
        fileUpload.addFile(createFileWithSize(attributeSizeLimit_2KB + ONE_KB));
        assertOnsizeRejectedTriggered(1);

        // add file not passing both size limits
        fileUpload.addFile(createFileWithSize(contextParamSizeLimit_4KB + ONE_KB));
        assertOnsizeRejectedTriggered(2);
    }

    @Test
    public void testOnsizerejectedTriggering_whenAttributeIsNotDefined() {
        browser.get(contextPath.toString() + "withoutAttribute.jsf");
        // check initial state
        assertOnsizeRejectedTriggered(0);

        // add file passing context param size
        fileUpload.addFile(createFileWithSize(contextParamSizeLimit_4KB - ONE_KB));
        assertOnsizeRejectedTriggered(0);

        // add file not passing context param size limit
        fileUpload.addFile(createFileWithSize(contextParamSizeLimit_4KB + ONE_KB));
        assertOnsizeRejectedTriggered(1);
    }

    private static void addPageWithAttributeSizeLimitGreaterThanContextParamLimit(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.head("<script type='text/javascript'>window.onsizerejected=0;</script>");

        p.form("<rich:fileUpload id='fileUpload' fileUploadListener='#{fileUploadBean.listener}' onsizerejected='window.onsizerejected++;' maxFileSize='" + attributeSizeLimit_6KB + "' />");

        deployment.archive().addAsWebResource(p, "withAttributeLimit6KB.xhtml");
    }

    private static void addPageWithAttributeSizeLimitLesserThanContextParamLimit(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.head("<script type='text/javascript'>window.onsizerejected=0;</script>");

        p.form("<rich:fileUpload id='fileUpload' fileUploadListener='#{fileUploadBean.listener}' onsizerejected='window.onsizerejected++;' maxFileSize='" + attributeSizeLimit_2KB + "' />");

        deployment.archive().addAsWebResource(p, "withAttributeLimit2KB.xhtml");
    }

    private static void addPageWithoutAttribute(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.head("<script type='text/javascript'>window.onsizerejected=0;</script>");

        p.form("<rich:fileUpload id='fileUpload' fileUploadListener='#{fileUploadBean.listener}' onsizerejected='window.onsizerejected++;' />");

        deployment.archive().addAsWebResource(p, "withoutAttribute.xhtml");
    }
}

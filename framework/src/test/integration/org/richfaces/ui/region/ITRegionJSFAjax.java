package org.richfaces.ui.region;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@RunWith(Arquillian.class)
@WarpTest
public class ITRegionJSFAjax extends AbstractRegionTest {

    @Deployment
    public static WebArchive createDeployment() {
        RegionTestDeployment deployment = new RegionTestDeployment(ITRegionJSFAjax.class);

        FaceletAsset page = deployment.baseFacelet("index.xhtml");
        page.form("<r:region>");
        page.form("    <h:commandButton id='button'>");
        page.form("        <f:ajax execute='#{regionBean.execute}' />");
        page.form("    </h:commandButton>");
        page.form("</r:region>");

        return deployment.getFinalArchive();
    }

    @Test
    public void testDefaults() {
        setupExecute(null);
        verifyExecutedIds(BUTTON_ID, BUTTON_ID);
    }

    @Test
    public void testExecuteThis() {
        setupExecute("@this");
        verifyExecutedIds(BUTTON_ID);
    }

    @Test
    public void testExecuteAll() {
        setupExecute("@all");
        verifyExecutedIds("@all");
    }

    @Test
    public void testExecuteForm() {
        setupExecute("@form");
        verifyExecutedIds(BUTTON_ID, FORM_ID);
    }
}

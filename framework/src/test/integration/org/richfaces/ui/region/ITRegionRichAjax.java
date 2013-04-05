package org.richfaces.ui.region;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import category.Smoke;

@RunAsClient
@RunWith(Arquillian.class)
@WarpTest
@Category(Smoke.class)
public class ITRegionRichAjax extends AbstractRegionTest {

    @Deployment
    public static WebArchive createDeployment() {
        RegionTestDeployment deployment = new RegionTestDeployment(ITRegionRichAjax.class);

        FaceletAsset page = deployment.baseFacelet("index.xhtml");
        page.form("<r:region id='region'>");
        page.form("    <h:commandButton id='button'>");
        page.form("        <r:ajax execute='#{regionBean.execute}' />");
        page.form("    </h:commandButton>");
        page.form("</r:region>");

        return deployment.getFinalArchive();
    }

    @Test
    public void testDefaults() {
        setupExecute(null);
        verifyExecutedIds("region");
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
        verifyExecutedIds(FORM_ID, BUTTON_ID);
    }

    @Test
    public void testExecuteRegion() {
        setupExecute("@region");
        verifyExecutedIds("region");
    }
}

package org.richfaces.component.focus;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.jsf.AfterPhase;
import org.jboss.arquillian.warp.jsf.Phase;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.richfaces.component.AbstractFocus;
import org.richfaces.integration.MiscDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import category.Smoke;

@RunAsClient
@WarpTest
@RunWith(Arquillian.class)
@Category(Smoke.class)
public class TestFocusDefaults {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @Deployment
    public static WebArchive createDeployment() {
        MiscDeployment deployment = new MiscDeployment(TestFocusValidationAware.class);

        deployment.archive()
            .addClasses(ComponentBean.class, VerifyFocusCandidates.class, AbstractComponentAssertion.class)
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    private static void addIndexPage(MiscDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.xmlns("rich", "http://richfaces.org/rich");
        p.xmlns("a4j", "http://richfaces.org/a4j");

        p.body("<h:form id='form'>");
        p.body("    <rich:focus id='focus' binding='#{componentBean.component}' />");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

    @Test
    public void testDefaultAttributes() {
        Warp.initiate(new Activity() {
            public void perform() {
                browser.get(contextPath.toExternalForm());
            }
        }).inspect(new AbstractComponentAssertion() {
            private static final long serialVersionUID = 1L;

            @AfterPhase(Phase.RENDER_RESPONSE)
            public void verify_default_attributes() {
                AbstractFocus component = bean.getComponent();
                assertTrue("Component is ajaxRenderer='true' by default", component.isAjaxRendered());
                assertTrue("Component is validationAware='true' by default", component.isValidationAware());
                assertFalse("Component is preserve='false' by default", component.isPreserve());
                assertFalse("Component is delayed='false' by default", component.isDelayed());
            }
        });
    }

    @Test
    public void testDefaultFocusCandidates() {
        Warp.initiate(new Activity() {
            public void perform() {
                browser.get(contextPath.toExternalForm());
            }
        }).inspect(new VerifyFocusCandidates("There are no invalid components, whole form is candidate", null, "form"));
    }
}

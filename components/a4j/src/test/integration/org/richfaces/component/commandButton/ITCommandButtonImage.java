package org.richfaces.component.commandButton;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.A4JDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunWith(Arquillian.class)
@RunAsClient
public class ITCommandButtonImage {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "buttonEL")
    private WebElement imageButtonEL;

    @FindBy(id = "buttonELNoValue")
    private WebElement imageButtonELNoValue;

    @FindBy(id = "buttonNoEL")
    private WebElement imageButtonNoEL;

    @FindBy(id = "hCommandButton")
    private WebElement hCommandImageButtonEL;

    @FindBy(id = "hCommandButtonNoValue")
    private WebElement hCommandImageButtonNoValue;

    private static final String EXPETED_ROOT_CONTEXT_PATH = ITCommandButtonImage.class.getSimpleName();

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        A4JDeployment deployment = new A4JDeployment(ITCommandButtonImage.class);
        deployment.archive().addAsWebResource(new File("src/test/resources/images/square.jpg"), "resources/square.jpg");
        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void should_generate_correct_src_when_image_is_referenced_by_el_resource() {
        browser.get(contextPath.toExternalForm());

        assertSrcValueOfImageButton(imageButtonEL);
    }

    @Test
    public void should_generate_correct_src_when_image_is_referenced_by_el_but_no_value_attribute_is_defined() {
        browser.get(contextPath.toExternalForm());

        assertSrcValueOfImageButton(imageButtonELNoValue);
    }

    @Test
    public void should_generate_correct_src_when_image_is_referenced_by_no_el() {
        browser.get(contextPath.toExternalForm());

        assertSrcValueOfImageButton(imageButtonNoEL);
    }

    @Test
    public void should_generate_correct_src_for_h_command_button_when_image_is_referenced_by_el() {
        browser.get(contextPath.toExternalForm());

        assertSrcValueOfImageButton(hCommandImageButtonEL);
    }

    @Test
    public void should_generate_correct_src_for_h_command_button_when_image_is_referenced_by_el_but_no_value_attribute() {
        browser.get(contextPath.toExternalForm());

        assertSrcValueOfImageButton(hCommandImageButtonNoValue);
    }

    private void assertSrcValueOfImageButton(WebElement imageButton) {
        String src = imageButton.getAttribute("src");
        assertEquals("The root context should be once in the URL of the button picture!", 1,
            countMatches(src, EXPETED_ROOT_CONTEXT_PATH));
    }

    private int countMatches(String str, String sub) {
        if (isEmpty(str) || isEmpty(sub)) {
            return 0;
        }
        int count = 0;
        int idx = 0;
        while ((idx = str.indexOf(sub, idx)) != -1) {
            count++;
            idx += sub.length();
        }
        return count;
    }

    private boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    private static void addIndexPage(A4JDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<h:form prependId='false'>");
        p.body("  <a4j:commandButton id=\"buttonEL\" image=\"#{resource['square.jpg']}\" value=\"#{resource['square.jpg']}\" /> ");
        p.body("  <a4j:commandButton id=\"buttonELNoValue\" image=\"#{resource['square.jpg']}\" /> ");
        p.body("  <a4j:commandButton id=\"buttonNoEL\" image=\"resources/square.jpg\" value=\"resources/square.jpg\"/> ");
        p.body("  <h:commandButton id=\"hCommandButton\" image=\"#{resource['square.jpg']}\" value=\"#{resource['square.jpg']}\" />");
        p.body("  <h:commandButton id=\"hCommandButtonNoValue\" image=\"#{resource['square.jpg']}\" />");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}
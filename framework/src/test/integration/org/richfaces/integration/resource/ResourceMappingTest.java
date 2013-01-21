package org.richfaces.integration.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.List;

import org.ajax4jsf.util.base64.Codec;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.integration.CoreDeployment;
import org.richfaces.resource.ResourceHandlerImpl;
import org.richfaces.resource.external.ExternalStaticResourceFactory;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;
import org.richfaces.shrinkwrap.descriptor.PropertiesAsset;

@RunWith(Arquillian.class)
@WarpTest
public class ResourceMappingTest {

    @Drone
    WebDriver driver;

    @ArquillianResource
    URL contextPath;

    @Deployment
    public static WebArchive createDeployment() {

        CoreDeployment deployment = new CoreDeployment(null);

        PropertiesAsset staticResourceMapping = new PropertiesAsset()
                .key(":original.css").value("relocated.css")
                .key(":part1.css").value("aggregated.css")
                .key(":part2.css").value("aggregated.css")
                .key("part1.js").value("aggregated.js")
                .key("part2.js").value("aggregated.js");

        EmptyAsset emptyResource = EmptyAsset.INSTANCE;

        FaceletAsset relocationPage = new FaceletAsset().head("<h:outputStylesheet name=\"original.css\" />");

        FaceletAsset aggregationPage = new FaceletAsset().head("<h:outputStylesheet name=\"part1.css\" />"
                + "<h:outputStylesheet name=\"part2.css\" />");
        
        FaceletAsset javaScriptAggregationPage = new FaceletAsset().head("<h:outputScript name=\"part1.js\" />"
                + "<h:outputScript name=\"part2.js\" />");

        deployment.withResourceHandler();

        deployment.archive()
                /** classes */
                .addPackage(ResourceHandlerImpl.class.getPackage())
                .addPackage(ExternalStaticResourceFactory.class.getPackage())
                .addClasses(Codec.class)
                /** META-INF */
                .addAsResource(staticResourceMapping, "META-INF/richfaces/static-resource-mappings.properties")
                /** ROOT */
                .addAsWebResource(relocationPage, "relocation.xhtml")
                .addAsWebResource(aggregationPage, "aggregation.xhtml")
                .addAsWebResource(javaScriptAggregationPage, "javaScriptAggregation.xhtml")
                .addAsWebResource(emptyResource, "resources/original.css")
                .addAsWebResource(emptyResource, "resources/part1.css")
                .addAsWebResource(emptyResource, "resources/part2.css")
                .addAsWebResource(emptyResource, "resources/relocated.css")
                .addAsWebResource(emptyResource, "resources/aggregated.css")
                .addAsWebResource(emptyResource, "resources/part1.js")
                .addAsWebResource(emptyResource, "resources/part2.js")
                .addAsWebResource(emptyResource, "resources/aggregated.js");

        return deployment.getFinalArchive();
    }

    @Test
    @RunAsClient
    public void test_stylesheet_resource_relocation() {

        driver.navigate().to(contextPath + "relocation.jsf");

        WebElement element = driver.findElement(By.cssSelector("head > link[rel=stylesheet]"));
        String href = element.getAttribute("href");

        assertTrue("href must contain relocated.css resource path: " + href, href.contains("/javax.faces.resource/relocated.css"));
    }

    @Test
    @RunAsClient
    public void test_stylesheet_resource_aggregation() {

        driver.navigate().to(contextPath + "aggregation.jsf");

        List<WebElement> elements = driver.findElements(By.cssSelector("head > link[rel=stylesheet]"));

        assertEquals("There must be exactly one resource link rendered", 1, elements.size());

        WebElement element = elements.get(0);
        String href = element.getAttribute("href");

        assertTrue("href must contain aggregated.css resource path: " + href, href.contains("/javax.faces.resource/aggregated.css"));
    }
    
    @Test
    @RunAsClient
    public void test_javascript_resource_aggregation() {

        driver.navigate().to(contextPath + "javaScriptAggregation.jsf");
        
        List<WebElement> elements = driver.findElements(By.cssSelector("head > script"));

        assertEquals("There must be exactly one resource link rendered", 1, elements.size());

        WebElement element = elements.get(0);
        String src = element.getAttribute("src");

        assertTrue("src must contain aggregated.js resource path: " + src, src.contains("/javax.faces.resource/aggregated.js"));
    }
}

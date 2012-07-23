package org.richfaces.resource;

import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.Collection;

import javax.faces.webapp.FacesServlet;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.utils.URLUtils;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.facesconfig20.FacesConfigVersionType;
import org.jboss.shrinkwrap.descriptor.api.facesconfig20.WebFacesConfigDescriptor;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.jboss.shrinkwrap.resolver.api.maven.MavenImporter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

        Collection<GenericArchive> dependencyLibs = DependencyResolvers.use(MavenDependencyResolver.class)
                .loadEffectivePom("pom.xml")
                .artifacts("org.richfaces.core:richfaces-core-api", "com.google.guava:guava", "net.sourceforge.cssparser:cssparser:0.9.5", "org.w3c.css:sac:1.3")
                .resolveAs(GenericArchive.class);

        JavaArchive coreLib = ShrinkWrap
                .create(MavenImporter.class, "richfaces-core-impl.jar")
                .loadEffectivePom("pom.xml")
                .importBuildOutput().as(JavaArchive.class);

        WebFacesConfigDescriptor facesConfig = Descriptors
                .create(WebFacesConfigDescriptor.class)
                .version(FacesConfigVersionType._2_0)
                .createApplication()
                    .resourceHandler(ResourceHandlerImpl.class.getName())
                .up();
        
        WebAppDescriptor webXml = Descriptors.create(WebAppDescriptor.class)
                .getOrCreateWelcomeFileList()
                    .welcomeFile("faces/index.xhtml")
                .up()
                .getOrCreateContextParam()
                    .paramName("org.richfaces.enableControlSkinning")
                    .paramValue("false")
                .up()
                .getOrCreateServlet()
                    .servletName(FacesServlet.class.getSimpleName())
                    .servletClass(FacesServlet.class.getName())
                    .loadOnStartup(1)
                .up()
                .getOrCreateServletMapping()
                    .servletName(FacesServlet.class.getSimpleName())
                    .urlPattern("*.jsf")
                .up()
                .getOrCreateServletMapping()
                    .servletName(FacesServlet.class.getSimpleName())
                    .urlPattern("/faces/*")
                .up();
        
        PropertiesAsset staticResourceMapping = new PropertiesAsset()
                .key("lib:original.js").value("routed.js");
        
        StringAsset reroutedJavascript = new StringAsset("content");
        
        FaceletAsset indexPage = new FaceletAsset().head("<h:outputStylesheet library=\"lib\" name=\"original.js\" />");

        return ShrinkWrap
                .create(WebArchive.class, ResourceMappingTest.class.getSimpleName() + ".war")
                /** libraries **/
                .addAsLibrary(coreLib)
                .addAsLibraries(dependencyLibs)
                /** WEB-INF */
                .addAsWebInfResource(new StringAsset(facesConfig.exportAsString()), "faces-config.xml")
                .addAsWebInfResource(new StringAsset(webXml.exportAsString()), "web.xml")
                /** META-INF */
                .addAsResource(staticResourceMapping, "META-INF/richfaces/static-resource-mappings.properties")
                /** ROOT */
                .addAsWebResource(indexPage, "index.xhtml")
                .addAsWebResource(reroutedJavascript, "resources/lib/original.js")
                .addAsWebResource(reroutedJavascript, "resources/routed.js");
    }

    @Test
    @RunAsClient
    public void test() {

        driver.navigate().to(contextPath);

        WebElement element = driver.findElement(By.cssSelector("head > link[rel=stylesheet]"));
        String href = element.getAttribute("href");
        URL url = URLUtils.buildUrl(href);
        String path = url.getPath();
        
        
        
        assertTrue("href must end with the routed.js resource path", path.contains("/javax.faces.resource/routed.js"));
    }
}

package org.richfaces.component.extendedDataTable;

import java.net.URL;
import java.util.List;

import javax.annotation.Nullable;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import com.google.common.base.Function;

@RunAsClient
@RunWith(Arquillian.class)
public class ITDisablingBuiltinSortingAndFiltering {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITDisablingBuiltinSortingAndFiltering.class);
        deployment.archive().addClass(IterationBuiltInBean.class);
        addIndexPage(deployment);
        deployment.webXml(new Function<WebAppDescriptor, WebAppDescriptor>() {
            @Override
            public WebAppDescriptor apply(@Nullable WebAppDescriptor input) {
                input
                    .createContextParam()
                        .paramName("org.richfaces.builtin.sort.enabled")
                        .paramValue("false")
                    .up()
                    .createContextParam()
                        .paramName("org.richfaces.builtin.filter.enabled")
                        .paramValue("false")
                    .up();
                return input;
            }
        });


        return deployment.getFinalArchive();
    }

    @Test
    public void check_for_no_sort_control() throws InterruptedException {
        browser.get(contextPath.toExternalForm());
        List<WebElement> sortHandles = browser.findElements(By.cssSelector(".rf-edt-srt"));
        Assert.assertEquals(0, sortHandles.size());
    }

    @Test
    public void check_for_no_filter_control() throws InterruptedException {
        browser.get(contextPath.toExternalForm());
        List<WebElement> filterInputs = browser.findElements(By.cssSelector(".rf-edt-flt-i"));
        Assert.assertEquals(0, filterInputs.size());
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<script type='text/javascript'>");
        p.body("function sortEdt(currentSortOrder) { ");
        p.body("  var edt = RichFaces.component('myForm:edt'); ");
        p.body("  var sortOrder = currentSortOrder == 'ascending' ? 'descending' : 'ascending'; ");
        p.body("  edt.sort('column2', sortOrder, true); ");
        p.body("} ");
        p.body("function filterEdt(filterValue) { ");
        p.body("  var edt = RichFaces.component('myForm:edt'); ");
        p.body("  edt.filter('column2', filterValue, true); ");
        p.body("} ");
        p.body("</script>");
        p.body("<h:form id='myForm'> ");
        p.body("    <rich:extendedDataTable id='edt' value='#{iterationBuiltInBean.values}' var='bean' filterVar='fv' > ");
        p.body("        <rich:column id='column1' width='150px' > ");
        p.body("            <f:facet name='header'>Column 1</f:facet> ");
        p.body("            <h:outputText value='Bean:' /> ");
        p.body("        </rich:column> ");
        p.body("        <rich:column id='column2' width='150px' ");
        p.body("                         sortBy='#{bean}' ");
        p.body("                         sortOrder='#{iterationBuiltInBean.sortOrder}' ");
        p.body("                         filterValue='#{iterationBuiltInBean.filterValue}' ");
        p.body("                         filterExpression='#{bean le fv}' > ");
        p.body("            <f:facet name='header'>Column 2</f:facet> ");
        p.body("            <h:outputText value='#{bean}' /> ");
        p.body("        </rich:column> ");
        p.body("        <rich:column id='column3' width='150px'" );
        p.body("                     sortBy='#{bean}' ");
        p.body("                     sortOrder='#{iterationBuiltInBean.sortOrder2}' > ");
        p.body("            <f:facet name='header'>Column 3</f:facet> ");
        p.body("            <h:outputText value='Row #{bean}, Column 3' /> ");
        p.body("        </rich:column> ");
        p.body("    </rich:extendedDataTable> ");
        p.body("    <a4j:commandButton id='ajax' execute='edt' render='edt' value='Ajax' /> ");
        p.body("</h:form> ");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

}

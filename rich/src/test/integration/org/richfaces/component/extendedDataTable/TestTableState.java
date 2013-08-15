package org.richfaces.component.extendedDataTable;

import static org.jboss.arquillian.graphene.Graphene.guardXhr;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.jsf.AfterPhase;
import org.jboss.arquillian.warp.jsf.Phase;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.component.AbstractExtendedDataTable;
import org.richfaces.component.ExtendedDataTableState;
import org.richfaces.component.UIColumn;
import org.richfaces.integration.IterationDeployment;
import org.richfaces.json.JSONException;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@WarpTest
@RunWith(Arquillian.class)
public class TestTableState {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "myForm:edt")
    private WebElement edt;

    @FindBy(id = "myForm:edt:0:n")
    private WebElement firstRow;

    @FindBy(id = "myForm:ajax")
    private WebElement button;

    @FindBy(id = "myForm:edt:header")
    private WebElement header;

    @FindBy(id = "myForm:edt:sort")
    private WebElement sortLink;

    @Deployment
    public static WebArchive createDeployment() {
        IterationDeployment deployment = new IterationDeployment(TestTableState.class);
        deployment.archive().addClass(IterationTableStateBean.class);
        addIndexPage(deployment);
        addWidthPage(deployment);
        addOrderPage(deployment);
        addSortPage(deployment);
        addFilterPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void table_width() {
        browser.get(contextPath.toExternalForm() + "width.jsf");
        // assert the columns widths (selectors are independent of the column order)
        Assert.assertEquals("210px", firstRow.findElement(By.cssSelector("td .rf-edt-c-column1")).getCssValue("width"));
        Assert.assertEquals("75px", firstRow.findElement(By.cssSelector("td .rf-edt-c-column2")).getCssValue("width"));
    }

    @Test
    public void table_width_resize() throws InterruptedException {
        // given
        browser.get(contextPath.toExternalForm() + "width.jsf");
        WebElement column1ResizeHandle = header.findElement(By.cssSelector(".rf-edt-hdr .rf-edt-td-column1 .rf-edt-rsz"));

        Actions builder = new Actions(browser);
        final Action dragAndDrop = builder.dragAndDropBy(column1ResizeHandle, 60, 0).build();

        // when / then
        Warp.initiate(new Activity() {
            @Override
            public void perform() {
                dragAndDrop.perform();
            }
        }).inspect(new Inspection() {
            private static final long serialVersionUID = 1L;

            @Inject
            IterationTableStateBean bean;

            @AfterPhase(Phase.INVOKE_APPLICATION)
            public void verify_table_state_updated() throws JSONException {
                ExtendedDataTableState beanState = new ExtendedDataTableState(bean.getWidthState());
                Assert.assertEquals("Backing bean table state should be updated", "270px", beanState.toJSON().getJSONObject("columnsWidthState").getString("column1"));

                FacesContext facesContext = FacesContext.getCurrentInstance();
                AbstractExtendedDataTable edtComponent = (AbstractExtendedDataTable) facesContext.getViewRoot().findComponent("myForm").findComponent("edt");
                ExtendedDataTableState tableState = new ExtendedDataTableState(edtComponent);
                Assert.assertEquals("EDT tableState should be updated", "270px", tableState.toJSON().getJSONObject("columnsWidthState").getString("column1"));
            }
        });

        Assert.assertEquals("270px", firstRow.findElement(By.cssSelector("td .rf-edt-c-column1")).getCssValue("width"));
    }

    @Test
    public void table_order() {
        browser.get(contextPath.toExternalForm() + "order.jsf");
        Assert.assertEquals("Column 2", header.findElement(By.cssSelector("td")).getText());
    }

    @Test
    @Ignore("RF-12814")
    public void table_order_server_side() throws InterruptedException {
        // given
        browser.get(contextPath.toExternalForm());
        WebElement column1 = header.findElement(By.cssSelector(".rf-edt-hdr-c.rf-edt-c-column1"));
        WebElement column3 = header.findElement(By.cssSelector(".rf-edt-c-column3 .rf-edt-hdr-c-cnt"));

        Actions builder = new Actions(browser);

        final Action dragAndDrop = builder.clickAndHold(column3)
                .moveToElement(column1)
                .release(column1)
                .build();

        // when / then
        Warp.initiate(new Activity() {

            @Override
            public void perform() {
                guardXhr(dragAndDrop).perform();
            }
        }).inspect(new Inspection() {
            private static final long serialVersionUID = 1L;

            @Inject
            IterationTableStateBean bean;

            @AfterPhase(Phase.INVOKE_APPLICATION)
            public void verify_bean_executed() {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                AbstractExtendedDataTable edtComponent = (AbstractExtendedDataTable) facesContext.getViewRoot().findComponent("myForm").findComponent("edt");
                ExtendedDataTableState tableState = new ExtendedDataTableState(edtComponent);
                String[] expectedOrder = {"column3", "column1", "column2"};
                Assert.assertArrayEquals(expectedOrder, tableState.getColumnsOrder());
            }
        });

        List<WebElement> columns = browser.findElements(By.cssSelector(".rf-edt-hdr-c"));
        assertTrue(columns.get(0).getAttribute("class").contains("rf-edt-c-column3"));
        assertTrue(columns.get(1).getAttribute("class").contains("rf-edt-c-column1"));
        assertTrue(columns.get(2).getAttribute("class").contains("rf-edt-c-column2"));
    }

    @Test
    public void table_sort() throws InterruptedException {
        // given
        browser.get(contextPath.toExternalForm() + "sort.jsf");
        WebElement cell = browser.findElements(By.cssSelector(".rf-edt-c-column2 .rf-edt-c-cnt")).get(0);
        Assert.assertEquals("9", cell.getText());

        guardXhr(sortLink).click();
        Thread.sleep(500);
        cell = browser.findElements(By.cssSelector(".rf-edt-c-column2 .rf-edt-c-cnt")).get(0);
        Assert.assertEquals("0", cell.getText());

        // when / then
        Warp.initiate(new Activity() {

            @Override
            public void perform() {
                guardXhr(button).click();
            }
        }).inspect(new Inspection() {
            private static final long serialVersionUID = 1L;

            @Inject
            IterationTableStateBean bean;

            @AfterPhase(Phase.INVOKE_APPLICATION)
            public void verify_bean_executed() {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                AbstractExtendedDataTable edtComponent = (AbstractExtendedDataTable) facesContext.getViewRoot().findComponent("myForm").findComponent("edt");
                ExtendedDataTableState tableState = new ExtendedDataTableState(edtComponent.getTableState());
                UIColumn column = new UIColumn();
                column.setId("column2");
                Assert.assertEquals("ascending", tableState.getColumnSort(column));
            }
        });

    }

    @Test
    public void table_observe() throws InterruptedException {
        // given
        browser.get(contextPath.toExternalForm() + "filter.jsf");

        List<WebElement> cells = browser.findElements(By.cssSelector(".rf-edt-c-column2 .rf-edt-c-cnt"));
        WebElement cell = cells.get(cells.size() - 1);
        Assert.assertEquals("6", cell.getText());

        WebElement filterInput = browser.findElement(By.id("myForm:edt:filterInput"));
        filterInput.clear();
        filterInput.sendKeys("3");
        filterInput.sendKeys(Keys.TAB);

        Thread.sleep(500);
        cells = browser.findElements(By.cssSelector(".rf-edt-c-column2 .rf-edt-c-cnt"));
        cell = cells.get(cells.size() - 1);
        Assert.assertEquals("3", cell.getText());

        // when / then
        Warp.initiate(new Activity() {

            @Override
            public void perform() {
                guardXhr(button).click();
            }
        }).inspect(new Inspection() {
            private static final long serialVersionUID = 1L;

            @Inject
            IterationTableStateBean bean;

            @AfterPhase(Phase.INVOKE_APPLICATION)
            public void verify_bean_executed() {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                AbstractExtendedDataTable edtComponent = (AbstractExtendedDataTable) facesContext.getViewRoot().findComponent("myForm").findComponent("edt");
                ExtendedDataTableState tableState = new ExtendedDataTableState(edtComponent.getTableState());
                UIColumn column = new UIColumn();
                column.setId("column2");
                Assert.assertEquals("3", tableState.getColumnFilter(column));
            }
        });

    }

    private static FaceletAsset getPage(String edtAttributes) {
        FaceletAsset p = new FaceletAsset();
        p.xmlns("rich", "http://richfaces.org/rich");
        p.xmlns("a4j", "http://richfaces.org/a4j");

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
        p.body("    <rich:extendedDataTable " + edtAttributes + " filterVar='fv' > ");
        p.body("        <rich:column id='column1' width='150px' > ");
        p.body("            <f:facet name='header'>Column 1</f:facet> ");
        p.body("            <h:outputText value='Bean:' /> ");
        p.body("        </rich:column> ");
        p.body("        <rich:column id='column2' width='150px' ");
        p.body("                         sortBy='#{bean}' ");
        p.body("                         sortOrder='#{iterationTableStateBean.sortOrder}' ");
        p.body("                         filterValue='#{iterationTableStateBean.filterValue}' ");
        p.body("                         filterType='custom' ");
        p.body("                         sortType='custom' ");
        p.body("                         filterExpression='#{bean le fv}' > ");
        p.body("            <f:facet name='header'> ");
        p.body("                <h:panelGrid columns='1'> ");
        p.body("                    <h:link id='sort' onclick=\"sortEdt('#{iterationTableStateBean.sortOrder}'); return false;\">Column 2</h:link> ");
        p.body("                    <h:inputText id='filterInput' value='#{iterationTableStateBean.filterValue}' label='Filter' ");
        p.body("                                 onblur='filterEdt(this.value); return false; ' style='width:80%' > ");
        p.body("                        <f:convertNumber /> ");
        p.body("                        <f:validateLongRange minimum='0' maximum='10' /> ");
        p.body("                    </h:inputText> ");
        p.body("                </h:panelGrid> ");
        p.body("            </f:facet> ");
        p.body("            <h:outputText value='#{bean}' /> ");
        p.body("        </rich:column> ");
        p.body("        <rich:column id='column3' width='150px' > ");
        p.body("            <f:facet name='header'>Column 3</f:facet> ");
        p.body("            <h:outputText value='R#{bean}C3' /> ");
        p.body("        </rich:column> ");
        p.body("    </rich:extendedDataTable> ");
        p.body("    <a4j:commandButton id='ajax' execute='edt' render='edt' value='Ajax' /> ");
        p.body("</h:form> ");
        return p;
    }

    private static void addIndexPage(IterationDeployment deployment) {
        String edtAttributes =
               "            id='edt' value='#{iterationTableStateBean.values}' var='bean' ";
        FaceletAsset p = getPage(edtAttributes);

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

    private static void addWidthPage(IterationDeployment deployment) {
        String edtAttributes =
               "            id='edt' value='#{iterationTableStateBean.values}' var='bean' " +
               "            tableState='#{iterationTableStateBean.widthState}'";
        FaceletAsset p = getPage(edtAttributes);

        deployment.archive().addAsWebResource(p, "width.xhtml");
    }

    private static void addSortPage(IterationDeployment deployment) {
        String edtAttributes =
               "            id='edt' value='#{iterationTableStateBean.values}' var='bean' " +
               "            tableState='#{iterationTableStateBean.sortState}'";
        FaceletAsset p = getPage(edtAttributes);

        deployment.archive().addAsWebResource(p, "sort.xhtml");
    }

    private static void addFilterPage(IterationDeployment deployment) {
        String edtAttributes =
               "            id='edt' value='#{iterationTableStateBean.values}' var='bean' " +
               "            tableState='#{iterationTableStateBean.filterState}'";
        FaceletAsset p = getPage(edtAttributes);

        deployment.archive().addAsWebResource(p, "filter.xhtml");
    }

    private static void addOrderPage(IterationDeployment deployment) {
        String edtAttributes =
               "            id='edt' value='#{iterationTableStateBean.values}' var='bean' " +
               "            columnsOrder='#{iterationTableStateBean.columnsOrder}'" +
               "            tableState='#{iterationTableStateBean.orderState}'";
        FaceletAsset p = getPage(edtAttributes);

        deployment.archive().addAsWebResource(p, "order.xhtml");
    }
}

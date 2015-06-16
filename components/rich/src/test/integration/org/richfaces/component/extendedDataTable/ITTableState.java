/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.component.extendedDataTable;

import static org.hamcrest.CoreMatchers.containsString;
import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.net.URL;
import java.util.EnumSet;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@RunWith(Arquillian.class)
public class ITTableState {

    @Drone
    private WebDriver browser;
    @ArquillianResource
    private URL contextPath;

    @FindBy(css = "[id$=checkAscendingSortOrder]")
    private WebElement checkAscendingSortOrderButton;
    @FindBy(css = "[id$=checkAscendingSortOrderResult]")
    private WebElement checkAscendingSortOrderResultElement;
    @FindBy(css = "[id$=checkColumnFilterEquals3]")
    private WebElement checkColumnFilterEquals3Button;
    @FindBy(css = "[id$=checkColumnFilterEquals3Result]")
    private WebElement checkColumnFilterEquals3ResultElement;
    @FindBy(css = "[id$=checkColumnOrderAfterDnd]")
    private WebElement checkColumnOrderAfterDndButton;
    @FindBy(css = "[id$=checkColumnOrderAfterDndResult]")
    private WebElement checkColumnOrderAfterDndResultElement;
    @FindBy(css = "[id$=checkWidthResizedAfterDnD]")
    private WebElement checkWidthResizedAfterDnDButton;
    @FindBy(css = "[id$=checkWidthResizedAfterDnDResult]")
    private WebElement checkWidthResizedAfterDnDResultElement;

    @FindBy(css = "input[id$='blur']")
    private WebElement blurButton;
    @FindBy(id = "myForm:ajax")
    private WebElement button;
    @FindBy(id = "myForm:edt:0:n")
    private WebElement firstRow;
    @FindBy(id = "myForm:edt:header")
    private WebElement header;
    @FindBy(id = "myForm:edt:sort")
    private WebElement sortLink;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITTableState.class);
        deployment.archive().addClass(IterationTableStateBean.class);
        addIndexPage(deployment);
        addWidthPage(deployment);
        addOrderPage(deployment);
        addSortPage(deployment);
        addFilterPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void table_observe() {
        browser.get(contextPath.toExternalForm() + "filter.jsf");

        WebElement cell = browser.findElement(ByJQuery.selector(".rf-edt-c-column2 .rf-edt-c-cnt:last"));
        assertEquals("6", cell.getText());

        WebElement filterInput = browser.findElement(By.id("myForm:edt:filterInput"));
        filterInput.clear();
        filterInput.sendKeys("3");
        guardAjax(blurButton).click();
        Graphene.waitAjax().until().element(ByJQuery.selector(".rf-edt-c-column2 .rf-edt-c-cnt:last")).text().equalTo("3");

        guardAjax(button).click();

        guardAjax(checkColumnFilterEquals3Button).click();
        assertEquals(IterationTableStateBean.PASSED, checkColumnFilterEquals3ResultElement.getText());
    }

    @Test
    public void table_order() {
        browser.get(contextPath.toExternalForm() + "order.jsf");
        assertEquals("Column 2", header.findElement(By.cssSelector("td")).getText());
    }

    @Test
    public void table_order_server_side() {
        browser.get(contextPath.toExternalForm());
        WebElement column1 = header.findElement(By.cssSelector(".rf-edt-hdr-c.rf-edt-c-column1"));
        WebElement column3 = header.findElement(By.cssSelector(".rf-edt-c-column3 .rf-edt-hdr-c-cnt"));

        Actions builder = new Actions(browser);

        final Action dragAndDrop = builder.clickAndHold(column3)
            .moveToElement(column1)
            .release(column1)
            .build();

        guardAjax(dragAndDrop).perform();

        guardAjax(checkColumnOrderAfterDndButton).click();
        assertEquals(IterationTableStateBean.PASSED, checkColumnOrderAfterDndResultElement.getText());

        List<WebElement> columns = browser.findElements(By.cssSelector(".rf-edt-hdr-c"));
        assertThat(columns.get(0).getAttribute("class"), containsString("rf-edt-c-column3"));
        assertThat(columns.get(1).getAttribute("class"), containsString("rf-edt-c-column1"));
        assertThat(columns.get(2).getAttribute("class"), containsString("rf-edt-c-column2"));
    }

    @Test
    public void table_sort() {
        browser.get(contextPath.toExternalForm() + "sort.jsf");
        WebElement cell = browser.findElement(ByJQuery.selector(".rf-edt-c-column2 .rf-edt-c-cnt:first"));
        assertEquals("9", cell.getText());

        guardAjax(sortLink).click();
        Graphene.waitAjax().until().element(browser.findElement(ByJQuery.selector(".rf-edt-c-column2 .rf-edt-c-cnt:first"))).text().equalTo("0");

        guardAjax(button).click();

        guardAjax(checkAscendingSortOrderButton).click();
        assertEquals(IterationTableStateBean.PASSED, checkAscendingSortOrderResultElement.getText());
    }

    @Test
    public void table_width() {
        browser.get(contextPath.toExternalForm() + "width.jsf");
        // assert the columns widths (selectors are independent of the column order)
        assertEquals("210px", firstRow.findElement(By.cssSelector("td .rf-edt-c-column1")).getCssValue("width"));
        assertEquals("75px", firstRow.findElement(By.cssSelector("td .rf-edt-c-column2")).getCssValue("width"));
    }

    @Test
    public void table_width_resize() {
        browser.get(contextPath.toExternalForm() + "width.jsf");
        WebElement column1ResizeHandle = header.findElement(By.cssSelector(".rf-edt-hdr .rf-edt-td-column1 .rf-edt-rsz"));

        Actions builder = new Actions(browser);
        final Action dragAndDrop = builder.dragAndDropBy(column1ResizeHandle, 60, 0).build();
        guardAjax(dragAndDrop).perform();

        guardAjax(checkWidthResizedAfterDnDButton).click();
        assertEquals(IterationTableStateBean.PASSED, checkWidthResizedAfterDnDResultElement.getText());

        assertEquals("270px", firstRow.findElement(By.cssSelector("td .rf-edt-c-column1")).getCssValue("width"));
    }

    private static FaceletAsset getPage(String edtAttributes, EnumSet<UsingCheckAction> checks) {
        FaceletAsset p = new FaceletAsset();

        p.body("<script type='text/javascript'>");
        p.body("function sortEdt(currentSortOrder) {");
        p.body("  var edt = RichFaces.component('myForm:edt');");
        p.body("  var sortOrder = currentSortOrder == 'ascending' ? 'descending' : 'ascending';");
        p.body("  edt.sort('column2', sortOrder, true);");
        p.body("}");
        p.body("function filterEdt(filterValue) {");
        p.body("  var edt = RichFaces.component('myForm:edt');");
        p.body("  edt.filter('column2', filterValue, true);");
        p.body("}");
        p.body("</script>");
        p.body("<h:form id='myForm'>");
        p.body("    <rich:extendedDataTable id='edt' value='#{iterationTableStateBean.values}' var='bean' " + edtAttributes + " filterVar='fv' >");
        p.body("        <rich:column id='column1' width='150px' >");
        p.body("            <f:facet name='header'>Column 1</f:facet>");
        p.body("            <h:outputText value='Bean:' />");
        p.body("        </rich:column>");
        p.body("        <rich:column id='column2' width='150px'");
        p.body("                         sortBy='#{bean}'");
        p.body("                         sortOrder='#{iterationTableStateBean.sortOrder}'");
        p.body("                         filterValue='#{iterationTableStateBean.filterValue}'");
        p.body("                         filterType='custom'");
        p.body("                         sortType='custom'");
        p.body("                         filterExpression='#{bean le fv}' >");
        p.body("            <f:facet name='header'>");
        p.body("                <h:panelGrid columns='1'>");
        p.body("                    <h:link id='sort' onclick=\"sortEdt('#{iterationTableStateBean.sortOrder}'); return false;\">Column 2</h:link>");
        p.body("                    <h:inputText id='filterInput' value='#{iterationTableStateBean.filterValue}' label='Filter'");
        p.body("                                 onblur='filterEdt(this.value); return false; ' style='width:80%' >");
        p.body("                        <f:convertNumber />");
        p.body("                        <f:validateLongRange minimum='0' maximum='10' />");
        p.body("                    </h:inputText>");
        p.body("                </h:panelGrid>");
        p.body("            </f:facet> ");
        p.body("            <h:outputText value='#{bean}' />");
        p.body("        </rich:column>");
        p.body("        <rich:column id='column3' width='150px' >");
        p.body("            <f:facet name='header'>Column 3</f:facet>");
        p.body("            <h:outputText value='R#{bean}C3' />");
        p.body("        </rich:column>");
        p.body("    </rich:extendedDataTable>");
        p.body("    <a4j:commandButton id='ajax' execute='edt' render='edt' value='Ajax' />");
        p.body("    <br/>");
        p.body("    <input id='blur' value='blur' type='button' />");
        if (checks.contains(UsingCheckAction.checkAscendingSortOrder)) {
            p.body("    <br/>");
            p.body("    <a4j:commandButton id='checkAscendingSortOrder' value='checkAscendingSortOrder' action='${iterationTableStateBean.checkAscendingSortOrder}' render='checkAscendingSortOrderResult' />");
            p.body("    <h:outputText id='checkAscendingSortOrderResult' value='${iterationTableStateBean.checkAscendingSortOrderResult}' />");
        }
        if (checks.contains(UsingCheckAction.checkColumnFilterEquals3)) {
            p.body("    <br/>");
            p.body("    <a4j:commandButton id='checkColumnFilterEquals3' value='checkColumnFilterEquals3' action='${iterationTableStateBean.checkColumnFilterEquals3}' render='checkColumnFilterEquals3Result' />");
            p.body("    <h:outputText id='checkColumnFilterEquals3Result' value='${iterationTableStateBean.checkColumnFilterEquals3Result}' />");
        }
        if (checks.contains(UsingCheckAction.checkColumnOrderAfterDnd)) {
            p.body("    <br/>");
            p.body("    <a4j:commandButton id='checkColumnOrderAfterDnd' value='checkColumnOrderAfterDnd' action='${iterationTableStateBean.checkColumnOrderAfterDnd}' render='checkColumnOrderAfterDndResult' />");
            p.body("    <h:outputText id='checkColumnOrderAfterDndResult' value='${iterationTableStateBean.checkColumnOrderAfterDndResult}' />");
        }
        if (checks.contains(UsingCheckAction.checkWidthResizedAfterDnD)) {
            p.body("    <br/>");
            p.body("    <a4j:commandButton id='checkWidthResizedAfterDnD' value='checkWidthResizedAfterDnD' action='${iterationTableStateBean.checkWidthResizedAfterDnD}' render='checkWidthResizedAfterDnDResult' />");
            p.body("    <h:outputText id='checkWidthResizedAfterDnDResult' value='${iterationTableStateBean.checkWidthResizedAfterDnDResult}' />");
        }
        p.body("</h:form> ");
        return p;
    }

    private static void addFilterPage(RichDeployment deployment) {
        String edtAttributes = "tableState='#{iterationTableStateBean.filterState}'";
        FaceletAsset p = getPage(edtAttributes, EnumSet.of(UsingCheckAction.checkColumnFilterEquals3));
        deployment.archive().addAsWebResource(p, "filter.xhtml");
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = getPage("", EnumSet.of(UsingCheckAction.checkColumnOrderAfterDnd));
        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

    private static void addOrderPage(RichDeployment deployment) {
        String edtAttributes = "columnsOrder='#{iterationTableStateBean.columnsOrder}' tableState='#{iterationTableStateBean.orderState}'";
        FaceletAsset p = getPage(edtAttributes, EnumSet.noneOf(UsingCheckAction.class));
        deployment.archive().addAsWebResource(p, "order.xhtml");
    }

    private static void addSortPage(RichDeployment deployment) {
        String edtAttributes = "tableState='#{iterationTableStateBean.sortState}'";
        FaceletAsset p = getPage(edtAttributes, EnumSet.of(UsingCheckAction.checkAscendingSortOrder));
        deployment.archive().addAsWebResource(p, "sort.xhtml");
    }

    private static void addWidthPage(RichDeployment deployment) {
        String edtAttributes = "tableState='#{iterationTableStateBean.widthState}'";
        FaceletAsset p = getPage(edtAttributes, EnumSet.of(UsingCheckAction.checkWidthResizedAfterDnD));
        deployment.archive().addAsWebResource(p, "width.xhtml");
    }

    private enum UsingCheckAction {

        checkWidthResizedAfterDnD, checkColumnOrderAfterDnd, checkColumnFilterEquals3, checkAscendingSortOrder
    }
}

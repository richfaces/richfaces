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
package org.richfaces.component.chart;

import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import category.Smoke;

@RunWith(Arquillian.class)
public class ITChartBasic {

    @Drone
    WebDriver browser;

    @ArquillianResource
    URL deploymentUrl;

    @JavaScript
    ChartJs chtestjs;

    @FindBy(xpath = "//div[@id='frm:chartChart']/canvas[@class='flot-overlay']")
    WebElement chartCanvas;

    @FindBy(id = "clickInfo")
    WebElement clickInfo;

    @FindBy(id = "hoverInfo")
    WebElement hoverInfo;

    @FindBy(id = "frm:msg")
    WebElement serverSideInfo;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITChartBasic.class);
        deployment.archive().addClasses(ChartBean.class);
        addIndexPage(deployment);
        return deployment.getFinalArchive();
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.head("<style type='text/css'>");
        p.head(".richfaces-chart {");
        p.head("width: 600px;");
        p.head("height: 400px;");
        p.head("}");
        p.head("</style>");
        p.head("    <script type='text/javascript'>");
        p.head("//<![CDATA[");
        p.head("function logClick(event){");
        p.head("    $('#clickInfo').text(event.data.x+','+event.data.y);");
        p.head("}");
        p.head("function hover(e){");
        p.head("    $('#hoverInfo').text(e.data.item.series.label+' ['+e.data.x+','+e.data.y+']');");
        p.head("}");
        p.head("function clear(){");
        p.head("    $('#hoverInfo').text('');");
        p.head("}");
        p.head("//]]>");
        p.head("</script>");

        p.body("<h:form id='frm'>");
        p.body("<rich:chart id='chart' zoom ='true' title='ChartTitle' onplotclick='logClick(event)' onplothover='hover(event)' onmouseout='clear()' plotClickListener='#{chartBean.handler}' >");
        p.body("    <a4j:repeat value='#{chartBean.countries}' var='country'>");
        p.body("                <rich:chartSeries label='#{country.name}' type='line'>");
        p.body("                    <a4j:repeat value='#{country.data}' var='record'>");
        p.body("                        <rich:chartPoint x='#{record.year}' y='#{record.tons}' />");
        p.body("                    </a4j:repeat>");
        p.body("                </rich:chartSeries>");
        p.body("            </a4j:repeat>");
        p.body("    <a4j:ajax event='plotclick' render='msg' execute='msg' />");
        p.body("    <rich:chartXAxis label='year' />");
        p.body("    <rich:chartYAxis label='metric tons of CO2 per capita' />");
        p.body("</rich:chart>");
        p.body("<h:outputText id='msg' value='#{chartBean.msg}' />");
        p.body("</h:form>");
        p.body("<span id='clickInfo'></span>");
        p.body("<br />");
        p.body("<span id='hoverInfo'></span>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

    @Test
    @Category(Smoke.class)
    public void testChartRendered() {
        browser.get(deploymentUrl.toExternalForm());
        // asserts that the chart, canvas and title were rendered
        assertNotNull("Chart should be present.", browser.findElement(By.id("frm:chartChart")));
        assertNotNull("Canvas should be created.",
            browser.findElement(By.xpath("//div[@id='frm:chartChart']/canvas[@class='flot-base']")));
        assertEquals("ChartTitle", browser.findElement(By.xpath("//div[@id='frm:chart']/div[@class='chart-title']")).getText());
    }

    @Test
    public void testChartEvents() {
        // does not work on FF - Webdriver cannot click into position on canvas hence cannot fire event
        final String serverSide = "Server's speaking:Point with index 0 within series 0 was clicked. Point coordinates: [1990,19.1].";
        final String plotClick = "1990,19.1";
        final String plotHover = "USA [1990,19.1]";
        browser.get(deploymentUrl.toExternalForm());

        // retrieve plot offset in canvas via JS (coordinates differ based on browser)
        int x = chtestjs.pointXPos("frm:chart", 0, 0);
        int y = chtestjs.pointYPos("frm:chart", 0, 0);

        // onplotclick client side
        new Actions(browser).moveToElement(chartCanvas, x, y).click().build().perform();
        waitAjax(browser).until().element(clickInfo).text().contains(plotClick);

        // onplotclick server side, since the element was already clicked only assertion is needed
        waitAjax(browser).until().element(serverSideInfo).text().contains(serverSide);

        // onplothover, first move away to clear the text from previous events
        new Actions(browser).moveToElement(serverSideInfo).click().build().perform();
        new Actions(browser).moveToElement(chartCanvas, x, y).build().perform();
        waitAjax(browser).until().element(hoverInfo).text().contains(plotHover);
    }

    @Test
    public void testZoom() {
        // does not work on FF - Webdriver cannot click into position on canvas
        browser.get(deploymentUrl.toExternalForm());

        // get plot coordinates on screen before zooming
        int xBeforeZoom = chtestjs.pointXPos("frm:chart", 0, 0);

        // zoom some area
        new Actions(browser)
            .moveToElement(chartCanvas, chtestjs.pointXPos("frm:chart", 1, 1), chtestjs.pointYPos("frm:chart", 1, 1))
            .clickAndHold()
            .moveToElement(chartCanvas, chtestjs.pointXPos("frm:chart", 2, 2), chtestjs.pointYPos("frm:chart", 2, 2)).release()
            .build().perform();
        // new Actions(browser).clickAndHold().moveByOffset(200, 50).release().build().perform();

        // get the coordinates after zooming, they should be different
        int xAfterZoom = chtestjs.pointXPos("frm:chart", 0, 0);

        // assert coordinates do not equal after zoom, only x axis is zoomed
        assertNotEquals(xBeforeZoom, xAfterZoom);

        // perform zoom reset by JS
        chtestjs.resetZoom("frm:chart");

        // get the coordinates again
        int xZoomReset = chtestjs.pointXPos("frm:chart", 0, 0);

        // assert that this equals initial state, only x axis is zoomed
        assertEquals(xBeforeZoom, xZoomReset);
    }
}

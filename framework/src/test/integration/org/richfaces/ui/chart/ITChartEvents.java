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
package org.richfaces.ui.chart;

import static org.jboss.arquillian.graphene.Graphene.waitAjax;

import java.io.File;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.deployment.FrameworkDeployment;

import category.FailingOnFirefox;

@RunWith(Arquillian.class)
public class ITChartEvents {

    private static final String WEBAPP_PATH = "src/test/integration/org/richfaces/ui/chart";

    private int seriesCount;

    @Drone
    WebDriver browser;

    @ArquillianResource
    URL deploymentUrl;

    Actions builder;

    @Deployment
    public static WebArchive createDeployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(
                ITChartEvents.class);
        deployment
                .archive()
                .addClasses(ChartBean.class, ChartParticularBean.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource(
                        new StringAsset("<faces-config version=\"2.0\"/>"),
                        "faces-config.xml");
        deployment.archive().addAsWebResource(
                new File(WEBAPP_PATH, "chart.xhtml"), "index.xhtml");
        deployment.archive().addAsWebResource(
                new File(WEBAPP_PATH, "particular.xhtml"), "particular.xhtml");

        return deployment.getFinalArchive();

    }

    @JavaScript
    ChartJs chtestjs;

    @Before
    public void setExpectedValues() {
        // eventBean.getCountries().size();
        seriesCount = 4;
    }

    @Before
    public void init() {
        builder = new Actions(browser);
    }

    @RunAsClient
    @Test
    public void ChartCreated() {
        browser.get(deploymentUrl.toExternalForm());

        Assert.assertNotNull("Chart should be on page.",
                browser.findElement(By.id("frm:chartChart")));

        Assert.assertNotNull(
                "Plot canvas created.",
                browser.findElement(By
                        .xpath("//div[@id='frm:chartChart']/canvas[@class='flot-base']")));

        Assert.assertEquals(seriesCount, chtestjs.seriesLength("frm:chart"));
    }

    @FindBy(id = "clickInfo")
    WebElement clickSpan;

    @FindBy(xpath = "//div[@id='frm:chartChart']/canvas[@class='flot-overlay']")
    WebElement chartCanvas;

    @RunAsClient
    @Test
    @Category(FailingOnFirefox.class)
    public void ClientSideClick() {
        browser.get(deploymentUrl.toExternalForm());

        // click the first point in the first series of the chart
        Action click = builder
                .moveToElement(chartCanvas,
                        chtestjs.pointXPos("frm:chart", 0, 0),
                        chtestjs.pointYPos("frm:chart", 0, 0)).click().build();

        click.perform();

        // crop decimal places
        double xVal = chtestjs.pointX("frm:chart", 0, 0);
        int xValInt = (int) xVal;

        String expected = Integer.toString(xValInt) + ','
                + Double.toString(chtestjs.pointY("frm:chart", 0, 0));

        Assert.assertEquals(expected, clickSpan.getText());
    }

    @FindBy(id = "frm:msg")
    WebElement msg;

    @RunAsClient
    @Test
    @Category(FailingOnFirefox.class)
    public void ServerSideClick() {
        browser.get(deploymentUrl.toExternalForm());

        String before = msg.getText();

        // click the first point in the first series of the chart
        int x = chtestjs.pointXPos("frm:chart", 0, 0);
        int y = chtestjs.pointYPos("frm:chart", 0, 0);

        Action click = builder.moveToElement(chartCanvas, x, y).click().build();

        click.perform();

        waitAjax().until().element(msg).text().not().equalTo(before);

        int seriesIndex = 0;
        int pointIndex = 0;
        // crop decimal places
        double xVal = chtestjs.pointX("frm:chart", seriesIndex, pointIndex);
        int xValInt = (int) xVal;

        String expected = "Server's speaking:Point with index " + pointIndex
                + "within series " + seriesIndex
                + " was clicked. Point coordinates ["
                + Integer.toString(xValInt) + ','
                + Double.toString(chtestjs.pointY("frm:chart", 0, 0)) + "]";

        Assert.assertEquals(expected, msg.getText());
    }

    @RunAsClient
    @Test
    @Category(FailingOnFirefox.class)
    public void particularSeriesServerSideClick() {
        browser.get(deploymentUrl.toExternalForm() + "faces/particular.xhtml");

        String before = msg.getText();

        // click the first point in the first series of the chart
        int x = chtestjs.pointXPos("frm:chart", 1, 0);
        int y = chtestjs.pointYPos("frm:chart", 1, 0);

        Action click = builder.moveToElement(chartCanvas, x, y).click().build();

        click.perform();

        waitAjax();
        // click on the second series should not change the text
        Assert.assertEquals(before, msg.getText());

        click = builder
                .moveToElement(chartCanvas,
                        chtestjs.pointXPos("frm:chart", 0, 0),
                        chtestjs.pointYPos("frm:chart", 0, 0)).click().build();

        click.perform();
        String expected = "a-series";

        // the first series should fire an event and change the text
        waitAjax().until().element(msg).text().not().equalTo(before);
        Assert.assertEquals(expected, msg.getText());

    }

}
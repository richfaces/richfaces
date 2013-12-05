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

package org.richfaces.ui.extendedDataTable;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.deployment.FrameworkDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@RunWith(Arquillian.class)
public class ITColumnWidth {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "myForm:edt")
    private WebElement edt;

    @FindBy(id = "myForm:edt:header")
    private WebElement header;

    @FindBy(id = "myForm:edt:0:n")
    private WebElement firstRow;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(ITColumnWidth.class);
        deployment.archive().addClass(IterationBean.class);
        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void setting_column_width() {
        browser.get(contextPath.toExternalForm());
        Assert.assertEquals("200px", firstRow.findElement(By.cssSelector("td .rf-edt-c-column1")).getCssValue("width"));
    }

    @Test
    public void column_resize_smaller() {
        browser.get(contextPath.toExternalForm());
        WebElement column1ResizeHandle = header.findElement(By.cssSelector(".rf-edt-hdr .rf-edt-td-column1 .rf-edt-rsz"));

        Actions builder = new Actions(browser);
        final Action dragAndDrop = builder.dragAndDropBy(column1ResizeHandle, -20, 0).build();
        dragAndDrop.perform();

        Assert.assertEquals("181px", firstRow.findElement(By.cssSelector("td")).getCssValue("width"));
        Assert.assertEquals("180px", firstRow.findElement(By.cssSelector("td .rf-edt-c-column1")).getCssValue("width"));
    }

    @Test
    public void column_resize_bigger() {
        browser.get(contextPath.toExternalForm());
        WebElement column1ResizeHandle = header.findElement(By.cssSelector(".rf-edt-hdr .rf-edt-td-column1 .rf-edt-rsz"));

        Actions builder = new Actions(browser);
        final Action dragAndDrop = builder.dragAndDropBy(column1ResizeHandle, 20, 0).build();
        dragAndDrop.perform();

        Assert.assertEquals("221px", firstRow.findElement(By.cssSelector("td")).getCssValue("width"));
        Assert.assertEquals("220px", firstRow.findElement(By.cssSelector("td .rf-edt-c-column1")).getCssValue("width"));
    }

    private static void addIndexPage(FrameworkDeployment deployment) {
        FaceletAsset p = new FaceletAsset();



        p.body("<h:form id='myForm'>");
        p.body("    <r:extendedDataTable id='edt' value='#{iterationBean.values}' var='bean'>");
        p.body("        <r:column id='column1' width='200px'>");
        p.body("            <f:facet name='header'> ");
        p.body("                <h:outputText value='Long header 1'/> ");
        p.body("             </f:facet> ");
        p.body("            <h:outputText value='Bean:' />");
        p.body("        </r:column>");
        p.body("        <r:column id='column2'>");
        p.body("            <f:facet name='header'> ");
        p.body("                <h:outputText value='Long header 2'/> ");
        p.body("             </f:facet> ");
        p.body("            <h:outputText value='#{bean}' />");
        p.body("        </r:column>");
        p.body("        <r:column id='column3'>");
        p.body("            <f:facet name='header'> ");
        p.body("                <h:outputText value='Long header 3'/> ");
        p.body("             </f:facet> ");
        p.body("            <h:outputText value='#{bean}' />");
        p.body("        </r:column>");
        p.body("    </r:extendedDataTable>");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

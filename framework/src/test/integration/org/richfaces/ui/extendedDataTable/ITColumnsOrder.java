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
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.deployment.FrameworkDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@RunWith(Arquillian.class)
public class ITColumnsOrder {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "button")
    private WebElement button;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(ITColumnsOrder.class);
        deployment.archive().addClass(IterationBean.class);
        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void check_columns_order() throws InterruptedException {
        browser.get(contextPath.toExternalForm());

        List<WebElement> cells = browser.findElements(By.cssSelector("#edt\\:0\\:n td"));

        Assert.assertTrue(cells.get(0).getAttribute("class").contains("d2"));
        Assert.assertTrue(cells.get(1).getAttribute("class").contains("d1"));
        Assert.assertTrue(cells.get(2).getAttribute("class").contains("d3"));

        Graphene.guardAjax(button).click();

        cells = browser.findElements(By.cssSelector("#edt\\:0\\:n td"));

        Assert.assertTrue(cells.get(0).getAttribute("class").contains("d3"));
        Assert.assertTrue(cells.get(1).getAttribute("class").contains("d2"));
        Assert.assertTrue(cells.get(2).getAttribute("class").contains("d1"));
    }

    private static void addIndexPage(FrameworkDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.form("<r:extendedDataTable id='edt' ");
        p.form("                        value='#{iterationBean.values}' ");
        p.form("                        var='bean' ");
        p.form("                        columnsOrder='${iterationBean.columnsOrder}' ");
        p.form("> ");
        p.form("    <r:column id='column1' styleClass='d1' > ");
        p.form("        <f:facet name='header'>Column 1</f:facet> ");
        p.form("        <h:outputText value='Bean:' /> ");
        p.form("    </r:column> ");
        p.form("    <r:column id='column2' styleClass='d2'>");
        p.form("        <f:facet name='header'>Column 2</f:facet> ");
        p.form("        <h:outputText value='#{bean}' /> ");
        p.form("    </r:column> ");
        p.form("    <r:column id='column3' styleClass='d3'>" );
        p.form("        <f:facet name='header'>Column 3</f:facet> ");
        p.form("        <h:outputText value='Row #{bean}, Column 3' /> ");
        p.form("    </r:column> ");
        p.form("</r:extendedDataTable> ");
        p.form("Columns order: <h:outputText id='output' value='#{iterationBean.columnsOrderString}' /> " );
        p.form("<br /> " );
        p.form("<r:commandButton id= 'button' " );
        p.form("     render='edt,output' ");
        p.form("     action='#{iterationBean.setColumnsOrder(\"column3,column2,column1\")}' />");


        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

}

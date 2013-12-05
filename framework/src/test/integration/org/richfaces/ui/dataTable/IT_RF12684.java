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

package org.richfaces.ui.dataTable;

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
import org.richfaces.ui.extendedDataTable.IterationBean;

@RunAsClient
@RunWith(Arquillian.class)
public class IT_RF12684 {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(className = "rf-ds-btn-last")
    private WebElement lastButton;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(IT_RF12684.class);
        deployment.archive().addClass(IterationBean.class);
        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void check_number_of_items_on_last_page() throws InterruptedException {
        // given
        browser.get(contextPath.toExternalForm());
        Graphene.guardAjax(lastButton).click();
        Thread.sleep(200);
        List<WebElement> cells = browser.findElements(By.cssSelector(".rf-dt-c"));
        Assert.assertEquals(1, cells.size());
    }

    private static void addIndexPage(FrameworkDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.form("<r:dataTable id='tableId' value='#{iterationBean.values}' var='bean' rows='3'> ");
        p.form("    <r:collapsibleSubTable id='collapsibleTableId' rows='3' /> ");
        p.form("    <r:column> ");
        p.form("        <f:facet name='header'> ");
        p.form("            <h:outputText value='Header' styleClass='tableHeader' /> ");
        p.form("        </f:facet> ");
        p.form("        <h:outputText value='#{bean}' /> ");
        p.form("    </r:column> ");
        p.form("    <f:facet name='footer'> ");
        p.form("        <r:dataScroller id='datascrollerId' for='tableId' fastControls='hide' /> ");
        p.form("    </f:facet> ");
        p.form("</r:dataTable> ");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

}

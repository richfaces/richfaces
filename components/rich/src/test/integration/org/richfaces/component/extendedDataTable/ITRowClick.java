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

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
@RunWith(Arquillian.class)
public class ITRowClick {

    @Drone
    private WebDriver browser;
    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "myForm:edt:0:n")
    private WebElement firstRow;
    @FindBy(id = "myForm:output")
    private WebElement selectedNodeIdElement;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITRowClick.class);
        deployment.archive().addClass(IterationBean.class);
        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    // RF-13165
    public void row_click() {
        browser.get(contextPath.toExternalForm());

        assertEquals("", selectedNodeIdElement.getText());

        guardAjax(firstRow).click();
        assertEquals("5", selectedNodeIdElement.getText());
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<script type='text/javascript'>");
        p.body("function rowClicked(event) {");
        p.body("  console.log(event);");
        p.body("}");
        p.body("</script>");
        p.body("<h:form id='myForm'>");
        p.body("    <rich:extendedDataTable id='edt' value='#{iterationBean.nodes}' var='node' rowKeyVar='rowKey' onrowclick='rowClicked(event)' >");
        p.body("        <a4j:ajax event='rowclick' render='myForm' listener='#{iterationBean.setNodeId(5)}' />");
        p.body("        <rich:column id='column1' width='150px' >");
        p.body("            <f:facet name='header'>Column 1</f:facet>");
        p.body("            Node: ");
        p.body("        </rich:column> ");
        p.body("        <rich:column id='column2' width='150px' >");
        p.body("            <f:facet name='header'>Node id</f:facet>");
        p.body("            #{node.id} ");
        p.body("        </rich:column> ");
        p.body("        <rich:column id='column3' width='150px' >");
        p.body("            <f:facet name='header'>Node label</f:facet>");
        p.body("            #{node.label} ");
        p.body("        </rich:column>");
        p.body("    </rich:extendedDataTable>");
        p.body("    <br/>");
        p.body("    selected node id: <h:outputText id='output' value='#{iterationBean.nodeId}' />");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

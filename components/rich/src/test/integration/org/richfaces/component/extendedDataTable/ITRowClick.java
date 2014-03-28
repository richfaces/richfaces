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

import category.Failing;
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
import org.jboss.arquillian.warp.jsf.BeforePhase;
import org.jboss.arquillian.warp.jsf.Phase;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.IterationDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import javax.inject.Inject;
import java.net.URL;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.warp.client.filter.http.HttpFilters.request;

@RunAsClient
@WarpTest
@RunWith(Arquillian.class)
public class ITRowClick {

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

    @FindBy(className = "rf-edt-srt")
    private WebElement sortHandle;

    @Deployment
    public static WebArchive createDeployment() {
        IterationDeployment deployment = new IterationDeployment(ITRowClick.class);
        deployment.archive().addClass(IterationBean.class);
        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    @Category(Failing.class)
    // RF-13165
    public void row_click() throws InterruptedException {
        // given
        browser.get(contextPath.toExternalForm());
        
        Warp.initiate(new Activity() {
            public void perform() {
                guardAjax(firstRow).click();
            }
        }).inspect(new Inspection() {
            private static final long serialVersionUID = 1L;

            @Inject
            IterationBean bean;

            @BeforePhase(Phase.INVOKE_APPLICATION)
            public void verify_param_not_yet_assigned() {
                Assert.assertEquals(null, bean.getNodeId());
            }

            @AfterPhase(Phase.INVOKE_APPLICATION)
            public void verify_param_assigned() {
                Assert.assertEquals((Integer) 1, bean.getNodeId());
            }
        });

    }

    private static void addIndexPage(IterationDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.xmlns("rich", "http://richfaces.org/rich");
        p.xmlns("a4j", "http://richfaces.org/a4j");

        p.body("<script type='text/javascript'>");
        p.body("function rowClicked(event) { ");
        p.body("  console.log(event) ");
        p.body("} ");
        p.body("</script> ");
        p.body("<h:form id='myForm'> ");
        p.body("    <rich:extendedDataTable id='edt' value='#{iterationBean.nodes}' var='node' rowKeyVar='rowKey' onrowclick='rowClicked' > ");
        p.body("        <a4j:ajax event='rowclick' render='myForm' listener='#{iterationBean.setNodeId(5)}' /> ");
        p.body("        <rich:column id='column1' width='150px' > ");
        p.body("            <f:facet name='header'>Column 1</f:facet> ");
        p.body("            Node: ");
        p.body("        </rich:column> ");
        p.body("        <rich:column id='column2' width='150px' > ");
        p.body("            <f:facet name='header'>Node id</f:facet> ");
        p.body("            #{node.id} ");
        p.body("        </rich:column> ");
        p.body("        <rich:column id='column3' width='150px' > ");
        p.body("            <f:facet name='header'>Node label</f:facet> ");
        p.body("            #{node.label} ");
        p.body("        </rich:column> ");
        p.body("    </rich:extendedDataTable> ");
        p.body("</h:form> ");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

}

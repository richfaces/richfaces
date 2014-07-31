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

package org.richfaces.component.repeat;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.A4JDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunWith(Arquillian.class)
@WarpTest
@RunAsClient
public class ITCollectionModel {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(css = "#list > li")
    private List<WebElement> items;

    @Deployment
    public static WebArchive deployment() {
        A4JDeployment deployment = new A4JDeployment(ITCollectionModel.class);

        deployment.archive()
            .addClasses(CollectionModelBean.class)
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void test_that_list_can_be_rendered_from_generic_collection() {
        browser.get(contextPath.toExternalForm());

        assertEquals("there are 3 items rendered", 3, items.size());

        assertEquals("1", items.get(0).getText());
        assertEquals("2", items.get(1).getText());
        assertEquals("3", items.get(2).getText());
    }

    private static void addIndexPage(A4JDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<ul id='list'>");
        p.body("<a4j:repeat id='list' var='item' value='#{collectionModelBean.collection}'>");
        p.body("    <li>#{item}</li>");
        p.body("</a4j:repeat>");
        p.body("</ul>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

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

package org.richfaces.integration.resource;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.List;

import javax.faces.application.ProjectStage;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.javaee6.ParamValueType;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.richfaces.deployment.FrameworkDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import category.Smoke;

import com.google.common.base.Function;

@RunWith(Arquillian.class)
@WarpTest
@RunAsClient
@Category(Smoke.class)
public class ITResourceOptimization {

    @Drone
    WebDriver driver;

    @ArquillianResource
    URL contextPath;

    @Deployment
    public static WebArchive createDeployment() {

        FrameworkDeployment deployment = new FrameworkDeployment(ITResourceOptimization.class);

        FaceletAsset p = deployment.baseFacelet("script.xhtml");
        p.head("<h:outputScript library='javax.faces' name='jsf.js' />");
        p.head("<h:outputScript name='jquery.js' />");
        p.head("<h:outputScript name='richfaces.js' />");

        p = deployment.baseFacelet("stylesheet.xhtml");
        p.head("<h:outputStylesheet library='org.richfaces' name='ajax/log.ecss' />");

        deployment.webXml(new Function<WebAppDescriptor, WebAppDescriptor>() {
        	public WebAppDescriptor apply(WebAppDescriptor input) {

        		List<ParamValueType<WebAppDescriptor>> allContextParam = input.getAllContextParam();
        		for (ParamValueType<WebAppDescriptor> contextParam : allContextParam) {
        		    if (ProjectStage.PROJECT_STAGE_PARAM_NAME.equals(contextParam.getParamName())) {
        		        contextParam.paramValue(ProjectStage.Production.name());
        		    }
        		}

        		input.getOrCreateContextParam()
        		    .paramName("org.richfaces.resourceOptimization.enabled")
        		    .paramValue("true");

        		return input;
        	};
		});

        return deployment.getFinalArchive();
    }

    @Test
    public void test_script_packaging() {

        driver.navigate().to(contextPath.toExternalForm() + "script.jsf");

        assertEquals(1, driver.findElements(By.cssSelector("script[src*='core.js']")).size());
        assertEquals(0, driver.findElements(By.cssSelector("script[src*='richfaces.js']")).size());
    }

    @Test
    public void test_stylesheet_packaging() {

        driver.navigate().to(contextPath.toExternalForm() + "stylesheet.jsf");

        assertEquals(1, driver.findElements(By.cssSelector("link[href*='ui.css']")).size());
        assertEquals(0, driver.findElements(By.cssSelector("link[href*='log.ecss']")).size());
    }
}

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
package org.richfaces.component.region;

import java.net.URL;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.junit.After;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.A4JDeployment;

public abstract class AbstractRegionTest {

    protected static final String BUTTON_ID = "button";
    protected static final String FORM_ID = "form";

    @Drone
    protected WebDriver browser;

    @ArquillianResource
    protected URL contextPath;

    @FindBy(id = "output")
    protected WebElement output;
    
    protected static class RegionTestDeployment extends A4JDeployment {

        RegionTestDeployment(Class<?> baseClass) {
            super(baseClass);
            this.archive().addClasses(RegionBean.class);
        }
    }

    protected void openPage(String execute) {
        if (execute != null) {
            Graphene.guardHttp(browser).get(contextPath.toString() + "?execute=" + execute);
        } else {
            Graphene.guardHttp(browser).get(contextPath.toString() + "?execute=");
        }
    }

    /**
     * Removes all cookies so it effectively destroys session so that RegionBean will be recreated.
     */
    @After
    public void cleanUpSession() {
        browser.manage().deleteAllCookies();
    }
}

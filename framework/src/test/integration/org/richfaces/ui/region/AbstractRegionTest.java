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

package org.richfaces.ui.region;

import static org.jboss.arquillian.warp.jsf.Phase.RENDER_RESPONSE;
import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.jsf.AfterPhase;
import org.jboss.arquillian.warp.servlet.BeforeServlet;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.deployment.FrameworkDeployment;

public abstract class AbstractRegionTest {

    protected static final String BUTTON_ID = "button";
    protected static final String FORM_ID = "form";

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = BUTTON_ID)
    private WebElement button;

    protected static class RegionTestDeployment extends FrameworkDeployment {
        RegionTestDeployment(Class<?> baseClass) {
            super(baseClass);
            this.archive().addClass(RegionBean.class);
            this.archive().addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        }
    }

    protected void setupExecute(String execute) {
        Warp
            .initiate(new Activity() {

                @Override
                public void perform() {
                    browser.get(contextPath.toString());
                }
            })
            .inspect(new SetupExecute(execute));
    }

    protected void verifyExecutedIds(String... expectedExecutedIds) {
        Warp
            .initiate(new Activity() {
                public void perform() {
                    button.click();
                }
            })
            .inspect(new VerifyExecutedIds(expectedExecutedIds));
    }

    public static class SetupExecute extends Inspection {
        private static final long serialVersionUID = 1L;
        private String execute;

        public SetupExecute(String execute) {
            this.execute = execute;
        }

        @Inject
        private RegionBean region;

        @BeforeServlet
        public void setupExecute() {
            region.setExecute(execute);
        }
    }

    public static class VerifyExecutedIds extends Inspection {
        private static final long serialVersionUID = 1L;
        private List<String> expectedExecutedIds;

        public VerifyExecutedIds(String... expectedExecutedIds) {
            this.expectedExecutedIds = new LinkedList<String>(Arrays.asList(expectedExecutedIds));
        }

        @AfterPhase(RENDER_RESPONSE)
        public void validaExecutedIds(@ArquillianResource FacesContext facesContext) {
            List<String> executedIds = new LinkedList<String>(facesContext.getPartialViewContext().getExecuteIds());
            assertEquals(expectedExecutedIds, executedIds);
        }
    }
}

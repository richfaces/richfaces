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

package org.richfaces.ui.focus;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.jsf.AfterPhase;
import org.jboss.arquillian.warp.jsf.Phase;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.richfaces.deployment.FrameworkDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;
import org.richfaces.ui.misc.focus.AbstractFocus;

import category.Smoke;

@RunAsClient
@WarpTest
@RunWith(Arquillian.class)
@Category(Smoke.class)
public class ITFocusDefaults {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @Deployment
    public static WebArchive createDeployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(ITFocusValidationAware.class);

        deployment.archive()
            .addClasses(ComponentBean.class, VerifyFocusCandidates.class, AbstractComponentAssertion.class)
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    private static void addIndexPage(FrameworkDeployment deployment) {
        FaceletAsset p = new FaceletAsset();



        p.body("<h:form id='form'>");
        p.body("    <r:focus id='focus' binding='#{componentBean.component}' />");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }

    @Test
    public void testDefaultAttributes() {
        Warp.initiate(new Activity() {
            public void perform() {
                browser.get(contextPath.toExternalForm());
            }
        }).inspect(new AbstractComponentAssertion() {
            private static final long serialVersionUID = 1L;

            @AfterPhase(Phase.RENDER_RESPONSE)
            public void verify_default_attributes() {
                AbstractFocus component = bean.getComponent();
                assertTrue("Component is ajaxRenderer='true' by default", component.isAjaxRendered());
                assertTrue("Component is validationAware='true' by default", component.isValidationAware());
                assertFalse("Component is preserve='false' by default", component.isPreserve());
                assertFalse("Component is delayed='false' by default", component.isDelayed());
            }
        });
    }

    @Test
    public void testDefaultFocusCandidates() {
        Warp.initiate(new Activity() {
            public void perform() {
                browser.get(contextPath.toExternalForm());
            }
        }).inspect(new VerifyFocusCandidates("There are no invalid components, whole form is candidate", null, "form"));
    }
}

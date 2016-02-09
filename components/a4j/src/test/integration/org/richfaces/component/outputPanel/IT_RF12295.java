/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc. and individual contributors
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
package org.richfaces.component.outputPanel;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import javax.annotation.Nullable;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.log.Log;
import org.richfaces.fragment.log.RichFacesLog;
import org.richfaces.integration.A4JDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import com.google.common.base.Function;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@RunAsClient
@RunWith(Arquillian.class)
public class IT_RF12295 {

    @Drone
    private WebDriver browser;
    @ArquillianResource
    private URL contextPath;

    @FindBy(css = "[id$=button]")
    private WebElement button;
    @FindBy(css = "[id$=log]")
    private RichFacesLog log;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        A4JDeployment deployment = new A4JDeployment(IT_RF12295.class);
        addIndexPage(deployment);
        deployment.webXml(new Function<WebAppDescriptor, WebAppDescriptor>() {
            @Override
            public WebAppDescriptor apply(@Nullable WebAppDescriptor webXML) {
                webXML
                    .createContextParam()
                    .paramName("javax.faces.PARTIAL_STATE_SAVING")
                    .paramValue("false")
                    .up();
                return webXML;
            }
        });
        return deployment.getFinalArchive();
    }

    @Test
    public void testNoExceptionsOnPostback() {
        browser.get(contextPath.toExternalForm());

        assertEquals(0, log.getLogEntries().size());

        // set watched level
        log.changeLevel(Log.LogEntryLevel.ERROR);
        assertEquals(0, log.getLogEntries().size());

        for (int i = 0; i < 3; i++) {
            // send ajax request
            Graphene.guardAjax(button).click();
            // assert that there is no exception on postback
            assertEquals(0, log.getLogEntries().size());
        }
    }

    private static void addIndexPage(A4JDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.form("<a4j:outputPanel>");
        p.form("    <span></span>");
        p.form("</a4j:outputPanel>");
        p.form("<a4j:commandButton id=\"button\" value=\"Click me\"/>");
        p.form("<a4j:log id=\"log\"/>");
        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

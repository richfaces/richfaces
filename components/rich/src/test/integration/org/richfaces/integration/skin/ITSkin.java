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
package org.richfaces.integration.skin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.Map;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import com.google.common.base.Function;

@RunAsClient
@RunWith(Arquillian.class)
public class ITSkin extends AbstractSkinTestBase {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(className = "rf-p-hdr")
    WebElement panel;

    @FindBy(id = "input")
    WebElement input;

    @FindBy(id = "buttonSkin1")
    WebElement buttonSkin1;

    @FindBy(id = "buttonSkin2")
    WebElement buttonSkin2;

    @FindBy(id = "buttonSkin3")
    WebElement buttonSkin3;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        RichDeployment deployment = new RichDeployment(ITSkin.class);
        deployment.archive().addClass(SkinBean.class);
        deployment.archive().addAsResource("bindedtest.skin.properties");
        deployment.webXml(new Function<WebAppDescriptor, WebAppDescriptor>() {
            public WebAppDescriptor apply(WebAppDescriptor input) {

                input.getOrCreateContextParam()
                    .paramName("org.richfaces.skin")
                    .paramValue("#{skinBean.skin}");

                return input;
            }
        ;
        });


        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void changing_skins() throws InterruptedException {

        URL url;
        Map<String, String> parameters;

        // given
        browser.get(contextPath.toExternalForm());
        url = getBackgroundUrl(buttonSkin1);
        parameters = parseQueryParameters(url);
        assertTrue(url.getPath().endsWith("org.richfaces.resources/rfRes/buttonBackgroundImage.png"));
        assertEquals("eAFjZGBkZOBm!P-f8f!bV88Y!185f5yBCQBPWAk3", parameters.get("db"));
        assertEquals("org.richfaces.images", parameters.get("ln"));

        Graphene.guardHttp(buttonSkin1).click();
        // button stays focused after click (url will be different for focused button), move mouse out
        panel.click();
        url = getBackgroundUrl(buttonSkin1);
        parameters = parseQueryParameters(url);
        assertTrue(url.getPath().endsWith("org.richfaces.resources/rfRes/buttonBackgroundImage.png"));
        assertEquals("eAFjZGBkZOBm!P-f8f-n70Bi37UfDEwAUQgJhA__", parameters.get("db"));
        assertEquals("org.richfaces.images", parameters.get("ln"));

        Graphene.guardHttp(buttonSkin2).click();
        url = getBackgroundUrl(buttonSkin1);
        parameters = parseQueryParameters(url);
        assertTrue(url.getPath().endsWith("org.richfaces.resources/rfRes/buttonBackgroundImage.png"));
        assertEquals("eAFjZGBkZOBm!P-f8f!9iAjG!xMYGBiYAD5VBi8_", parameters.get("db"));
        assertEquals("org.richfaces.images", parameters.get("ln"));

        Graphene.guardHttp(buttonSkin3).click();
        Assert.assertEquals("plain button background-url is incorrect", "none", buttonSkin1.getCssValue("background-image"));
    }

    private static void addIndexPage(RichDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.form("<rich:panel id='panel' header='Header Text'>Some content ");
        p.form("    <h:inputText id='input' /> ");
        p.form("</rich:panel> ");
        p.form("<h:commandButton id='buttonSkin1' actionListener='#{skinBean.setSkin(\"blueSky\")}' value = 'Select skin 1' /> ");
        p.form("<h:commandButton id='buttonSkin2' actionListener='#{skinBean.setSkin(\"ruby\")}' value = 'Select skin 2' /> ");
        p.form("<h:commandButton id='buttonSkin3' actionListener='#{skinBean.setSkin(\"plain\")}' value = 'Select skin 3' /> ");
        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

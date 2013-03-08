/**
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
 **/
package org.richfaces.component.validator;

import java.net.URL;

import javax.annotation.Nullable;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.facesconfig20.WebFacesConfigDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.integration.MiscDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import com.google.common.base.Function;

@RunAsClient
@RunWith(Arquillian.class)
public class TestValidatorMessageWithLabel {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @Deployment
    public static WebArchive createDeployment() {
        MiscDeployment deployment = new MiscDeployment(TestValidatorMessageWithLabel.class);
        deployment.archive().addClass(ValidatorBean.class);
        addIndexPage(deployment);
        deployment.archive().addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        deployment.archive().addAsResource("org/richfaces/component/validator/MessagesWithLabels.properties");

        deployment.facesConfig(new Function<WebFacesConfigDescriptor, WebFacesConfigDescriptor>() {
            @Override
            public WebFacesConfigDescriptor apply(@Nullable WebFacesConfigDescriptor input) {
                return input.getOrCreateApplication()
                    .messageBundle("org.richfaces.component.validator.MessagesWithLabels").up();
            }
        });

        return deployment.getFinalArchive();
    }

    /**
     * Tests that validation message contains a label, i.e. content of "label" attribute of h:inputText component. This
     * is not default behavior, there is following setting in file MessagesWithLabels.properties:
     * javax.faces.validator.BeanValidator.MESSAGE={1}: {0}
     *
     * {@link https://issues.jboss.org/browse/RF-12754}
     */
    @Test
    public void client_side_validation_msg() {
        // given
        browser.get(contextPath.toExternalForm());
        WebElement input = browser.findElement(By.id("myForm:input1"));

        input.sendKeys("RichFaces");
        Graphene.guardNoRequest(input).sendKeys(Keys.TAB);

        WebElement message = browser.findElement(By.id("myForm:msg1"));
        Assert.assertEquals("Validation message", "Input 1: max 4 characters", message.getText());
    }

    @Test
    public void msg_after_clicking_h_command_button() throws InterruptedException {
        // given
        browser.get(contextPath.toExternalForm());
        WebElement input = browser.findElement(By.id("myForm:input2"));
        WebElement button = browser.findElement(By.id("myForm:hButton"));

        input.sendKeys("RichFaces");
        Graphene.guardHttp(button).click();

        WebElement message = browser.findElement(By.id("myForm:msg2"));
        Assert.assertEquals("Validation message", "Input 2: max 4 characters", message.getText());
    }

    @Test
    public void msg_after_clicking_r_command_button() throws InterruptedException {
        // given
        browser.get(contextPath.toExternalForm());
        WebElement input = browser.findElement(By.id("myForm:input2"));
        WebElement button = browser.findElement(By.id("myForm:rButton"));

        input.sendKeys("RichFaces");
        Graphene.guardXhr(button).click();

        WebElement message = browser.findElement(By.id("myForm:msg2"));
        Assert.assertEquals("Validation message", "Input 2: max 4 characters", message.getText());
    }

    private static void addIndexPage(MiscDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<h:form id='myForm'>");
        p.body("    <h:panelGrid columns='3'>");
        p.body("        <h:outputLabel for='input1' value='String size, from 2 to 4' />");
        p.body("        <h:inputText id='input1' value='#{validatorBean.value}' label='Input 1'>");
        p.body("            <r:validator/>");
        p.body("        </h:inputText>");
        p.body("        <r:message id='msg1' for='input1' />");
        p.body("        <h:outputLabel for='input2' value='String size, from 2 to 4' />");
        p.body("        <h:inputText id='input2' value='#{validatorBean.value}' label='Input 2' />");
        p.body("        <r:message id='msg2' for='input2' />");
        p.body("    </h:panelGrid>");
        p.body("    <br />");
        p.body("    <h:commandButton id='hButton' value='h:commandButton' style='margin-right: 10px;' />");
        p.body("    <r:commandButton id='rButton' value='r:commandButton' />");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}

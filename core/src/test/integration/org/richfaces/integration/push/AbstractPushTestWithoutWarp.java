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
package org.richfaces.integration.push;

import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Assert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.deployment.CoreDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

public class AbstractPushTestWithoutWarp {

    public static final String TOPIC = "testingTopic";

    @Drone
    WebDriver driver;

    @FindBy(id = "sendMessage")
    WebElement sendButton;

    @ArquillianResource
    URL contextPath;

    @ArquillianResource
    JavascriptExecutor executor;

    public static CoreDeployment createBasicDeployment(Class<?> testClass) {

        CoreDeployment deployment = new CoreDeployment(testClass);
        deployment.withA4jComponents();

        FaceletAsset p = new FaceletAsset();
        p.body("<script>document.title = 'waiting-for-message';</script>");
        p.body("<a4j:push address=\"" + TOPIC + "\" ondataavailable=\"console.log('a4j:push message: ' + event.rf.data); document.title = 'message-received: ' + event.rf.data;\" />");
        p.form("<a4j:commandButton id=\"sendMessage\" value=\"send message\" action=\"#{pushBean.sendMessage}\" />");

        deployment.addMavenDependency(
            "org.atmosphere:atmosphere-runtime");
        deployment.archive().addClass(PushBean.class);

        deployment.archive().addAsWebResource(p, "index.xhtml");

        return deployment;
    }

    public void testSimplePush() {
        driver.navigate().to(contextPath);
        int numberOfTestedRequests = 5;
        Assert.assertEquals("waiting-for-message", driver.getTitle());
        for (int i = 1; i <= numberOfTestedRequests; i++) {
            sendButton.click();
            waitAjax().withTimeout(5, TimeUnit.SECONDS).until(titleIs(String.format("message-received: %d", i)));
        }
    }
}

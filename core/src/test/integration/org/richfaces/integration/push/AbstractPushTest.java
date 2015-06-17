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

import static java.text.MessageFormat.format;

import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.junit.Assert.assertEquals;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.richfaces.deployment.CoreDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunAsClient
public class AbstractPushTest {

    private static final String MESSAGE_RECEIVED = "message-received:";

    public static final String TOPIC = "testingTopic";

    @ArquillianResource
    private URL contextPath;
    @Drone
    private WebDriver driver;
    @FindBy(css = "[id$='sendMessage']")
    private WebElement sendButton;

    public static CoreDeployment createBasicDeployment(Class<?> testClass) {

        CoreDeployment deployment = new CoreDeployment(testClass);
        deployment.withA4jComponents();
        deployment.archive().addClasses(PushBean.class);

        FaceletAsset p = new FaceletAsset();
        p.body("<script type='text/javascript'>document.title = 'waiting-for-message';</script>");
        p.body("<a4j:push address='" + TOPIC + "' onsubscribed=\"console.log('a4j:push subscribed')\" ondataavailable=\"console.log('a4j:push message: ' + event.rf.data); document.title = 'message-received: ' + event.rf.data;\" />");
        p.form("<a4j:commandButton id='sendMessage' value='send message' action='#{pushBean.sendMessage}' />");

        deployment.addMavenDependency("org.atmosphere:atmosphere-runtime");

        deployment.archive().addAsWebResource(p, "index.xhtml");

        return deployment;
    }

    private void loadPage() {
        driver.get(contextPath.toString());
    }

    private void printDebugMessageAboutPageReloads(int pushMessagesSent) {
        System.out.println(format("### Page was reloaded <{0}> times before the push started to work.", pushMessagesSent - 1));
    }

    public void testSimplePush() {
        loadPage();
        assertEquals("waiting-for-message", driver.getTitle());
        int pushMessagesSent = waitForPushIsInitialized();
        printDebugMessageAboutPageReloads(pushMessagesSent);
        pushMessagesSent++;// will continue from position + 1
        int numberOfTestedRequests = 5 + pushMessagesSent;
        for (int i = pushMessagesSent; i < numberOfTestedRequests; i++) {
            sendButton.click();
            waitAjax().withTimeout(2, TimeUnit.SECONDS).until(titleIs(format("message-received: {0}", i)));
        }
    }

    // https://issues.jboss.org/browse/RF-13888
    protected int waitForPushIsInitialized() {
        int numberOfTries = 40;
        RuntimeException t = null;
        for (int i = 1; i <= numberOfTries; i++) {
            try {
                sendButton.click();
                waitGui().until(ExpectedConditions.titleContains(MESSAGE_RECEIVED));
                return Integer.parseInt(driver.getTitle().replaceAll(MESSAGE_RECEIVED, "").trim());
            } catch (TimeoutException exception) {
                t = exception;
                loadPage();
            }
        }
        throw new RuntimeException(format("The push did not intialize within <{0}> page reloads.", numberOfTries), t);
    }
}

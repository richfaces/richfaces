/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
 *******************************************************************************/
package org.richfaces.showcase.push;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.richfaces.showcase.AbstractWebDriverTest;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class ITestPushCdi extends AbstractWebDriverTest {

    /* **********************************************************************
     * Locators **********************************************************************
     */

    private final By MESSAGE_CONSUMER_INVOKE_LINK = By.className("popupLink");
    private final By INPUT_FOR_MESSAGES = By.className("message");
    private final By CONSUMER_MESSAGE = By.id("messages");
    private final By SUBMIT = By.xpath("//input[@type='submit']");

    /* ************************************************************************
     * Constants ************************************************************************
     */

    private final Map<Integer, String> EXPECTED_MESSAGES_ON_CONSUMERS = new HashMap<Integer, String>() {
        // just for avoiding of warning about serialVersionUID declaration
        private static final long serialVersionUID = -1113582265865921787L;

        {
            put(4, "Test string 5");
            put(3, "Test string 5\nTest string 4");
            put(2, "Test string 5\nTest string 4\nTest string 3");
            put(1, "Test string 5\nTest string 4\nTest string 3\nTest string 2");
            put(0, "Test string 5\nTest string 4\nTest string 3\nTest string 2\nTest string 1");
        }
    };

    /* ************************************************************************
     * Tests ************************************************************************
     */

    @Test
    public void testSendMessagesToSequentiallyOpenedConsumers() {

        String firstWindow = closeAllpreviouslyOpenedWindows();

        Map<Integer, String> consumersWindows = new HashMap<Integer, String>();

        WebElement input = webDriver.findElement(INPUT_FOR_MESSAGES);

        Set<String> windows = null;

        // 5 times will be new consumer invoked
        for (int i = 0; i < 5; i++) {

            windows = webDriver.getWindowHandles();

            String newConsumer = waitForConsumerWindowLoadingAfterInvocation(i, windows);

            windows = webDriver.getWindowHandles();
            windows.remove(firstWindow);

            consumersWindows.put(i, newConsumer);

            String message = "Test string " + (i + 1);

            input.sendKeys(message);

            webDriver.findElement(SUBMIT).click();

            assertTrue("The input should be empty after submiting!", input.getText().equals(""));

            webDriver.switchTo().window(newConsumer);

            String messagesAfterPush = webDriver.findElement(CONSUMER_MESSAGE).getText();

            long end = System.currentTimeMillis() + 5000;
            while (System.currentTimeMillis() < end) {

                boolean isPushedToAllConsumers = true;

                for (String window : windows) {

                    webDriver.switchTo().window(window);

                    messagesAfterPush = webDriver.findElement(CONSUMER_MESSAGE).getText();

                    if (!messagesAfterPush.contains(message)) {
                        isPushedToAllConsumers = false;
                    }

                }

                if (isPushedToAllConsumers)
                    break;
            }

            webDriver.switchTo().window(firstWindow);
        }

        checkExpectedMessagesOnConsumers(consumersWindows);

    }

    /* ***************************************************************************************************
     * Help methods ************************************************************** *************************************
     */

    private String closeAllpreviouslyOpenedWindows() {

        String firstWindow = webDriver.getWindowHandle();

        Set<String> windows = webDriver.getWindowHandles();
        windows.remove(firstWindow);

        for (String i : windows) {

            webDriver.switchTo().window(i);
            webDriver.close();
        }

        webDriver.switchTo().window(firstWindow);

        return firstWindow;
    }

    /**
     * 
     * @param currentNumberOfConsumerWindows
     * @param oldWindows
     * @return
     */
    private String waitForConsumerWindowLoadingAfterInvocation(final int currentNumberOfConsumerWindows, Set<String> oldWindows) {

        webDriver.findElement(MESSAGE_CONSUMER_INVOKE_LINK).click();

        (new WebDriverWait(webDriver, 4)).until(new ExpectedCondition<Boolean>() {

            public Boolean apply(WebDriver d) {

                return (d.getWindowHandles().size() - 1) > currentNumberOfConsumerWindows;
            }
        });

        Set<String> currentWindows = webDriver.getWindowHandles();
        currentWindows.removeAll(oldWindows);

        return Collections.list(Collections.enumeration(currentWindows)).get(0);
    }

    /**
     * 
     * @param consumersWindows
     */
    private void checkExpectedMessagesOnConsumers(Map<Integer, String> consumersWindows) {

        for (Map.Entry<Integer, String> entry : consumersWindows.entrySet()) {

            webDriver.switchTo().window(entry.getValue());

            String actualMessage = webDriver.findElement(CONSUMER_MESSAGE).getText();

            Integer key = entry.getKey();
            String expectedMessages = EXPECTED_MESSAGES_ON_CONSUMERS.get(key);
            assertEquals("The window invoked in the order as " + key + " should contains different messages", expectedMessages,
                actualMessage);
        }
    }

}

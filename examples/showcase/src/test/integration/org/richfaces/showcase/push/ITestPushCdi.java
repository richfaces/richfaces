/*
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
 */
package org.richfaces.showcase.push;

import static java.text.MessageFormat.format;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.showcase.AbstractWebDriverTest;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class ITestPushCdi extends AbstractWebDriverTest {

    private static final By MESSAGES_BY_ID = By.id("messages");
    private static final String TEST_STRING_TEMPLATE = "Test string {0}";

    private final List<String> EXPECTED_MESSAGES_ON_CONSUMERS = Lists.newArrayList(
        "Test string 5\nTest string 4\nTest string 3\nTest string 2\nTest string 1",
        "Test string 5\nTest string 4\nTest string 3\nTest string 2",
        "Test string 5\nTest string 4\nTest string 3",
        "Test string 5\nTest string 4",
        "Test string 5"
    );

    @FindBy(className = "message")
    private WebElement messageInputElement;
    @FindBy(className = "popupLink")
    private WebElement openNewConsumerWindowButton;
    @FindBy(css = "input[type=submit]")
    private WebElement submitButton;

    /**
     * @param consumersWindows
     */
    private void checkExpectedMessagesOnConsumers(List<String> consumersWindows) {
        String entry;
        for (int i = 0; i < consumersWindows.size(); i++) {
            entry = consumersWindows.get(i);
            webDriver.switchTo().window(entry);
            Graphene.waitAjax().until(new ConsumerHasMessagePredicate(i));
        }
    }

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
     * @param currentNumberOfConsumerWindows
     * @param oldWindows
     * @return
     */
    private String openNewConsumerWindowAndGetItsHandle(final int currentNumberOfConsumerWindows) {
        Set<String> oldWindows = webDriver.getWindowHandles();
        openNewConsumerWindowButton.click();
        Graphene.waitModel().withTimeout(5, TimeUnit.SECONDS).until(new WindowsHandlesSizeIncreasePredicate(currentNumberOfConsumerWindows));
        Set<String> currentWindows = webDriver.getWindowHandles();
        currentWindows.removeAll(oldWindows);
        return currentWindows.iterator().next();
    }

    private void submitNewMessage(int i) {
        messageInputElement.sendKeys(format(TEST_STRING_TEMPLATE, (i + 1)));
        Graphene.guardAjax(submitButton).click();
        Graphene.waitGui().withMessage("The input should be empty after submiting!")
            .until().element(messageInputElement).value().equalTo("");
    }

    @Test
    public void testSendMessagesToSequentiallyOpenedConsumers() {
        String publisherWindowHandle = closeAllpreviouslyOpenedWindows();
        final int testedConsumers = 5;
        List<String> consumersWindows = new ArrayList<String>(testedConsumers);

        for (int i = 0; i < testedConsumers; i++) {
            String newConsumerWindowHandle = openNewConsumerWindowAndGetItsHandle(i);

            webDriver.switchTo().window(newConsumerWindowHandle).switchTo().window(publisherWindowHandle);

            consumersWindows.add(newConsumerWindowHandle);

            submitNewMessage(i);
        }
        checkExpectedMessagesOnConsumers(consumersWindows);
    }

    private class WindowsHandlesSizeIncreasePredicate implements Predicate<WebDriver> {

        private final int previousWindowHandlesSize;

        private WindowsHandlesSizeIncreasePredicate(int previousWindowHandlesSize) {
            this.previousWindowHandlesSize = previousWindowHandlesSize;
        }

        @Override
        public boolean apply(WebDriver d) {
            return (d.getWindowHandles().size() - 1) > previousWindowHandlesSize;
        }
    };

    private class ConsumerHasMessagePredicate implements Predicate<WebDriver> {

        private final int index;
        private String message, actualText, expectedText;

        public ConsumerHasMessagePredicate(int index) {
            this.index = index;
        }

        @Override
        public boolean apply(WebDriver t) {
            actualText = webDriver.findElement(MESSAGES_BY_ID).getText();
            expectedText = EXPECTED_MESSAGES_ON_CONSUMERS.get(index);
            boolean result = actualText.equals(expectedText);
            if (!result) {
                message = format("The window invoked in the order as <{0}> should contain different message. Expected: <{1}>, has: <{2}>", index, expectedText, actualText);
            }
            return result;
        }

        @Override
        public String toString() {
            return message;
        }
    }
}

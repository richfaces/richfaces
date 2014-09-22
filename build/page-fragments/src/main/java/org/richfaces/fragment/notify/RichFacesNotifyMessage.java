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
package org.richfaces.fragment.notify;

import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.message.AbstractMessage;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesNotifyMessage extends AbstractMessage implements NotifyMessage {

    @Drone
    private WebDriver driver;

    @FindBy(className = "rf-ntf-det")
    private WebElement messageDetailElement;
    @FindBy(className = "rf-ntf-sum")
    private WebElement messageSummaryElement;
    @FindBy(className = "rf-ntf-cls")
    private WebElement closeElement;
    @FindBy(className = "rf-ntf-cls-ico")
    private WebElement closeIconElement;
    @FindBy(className = "rf-ntf-shdw")
    private WebElement shadowElement;

    private final AdvancedNotifyMessageInteractionsImpl interactions = new AdvancedNotifyMessageInteractionsImpl();

    @Override
    public AdvancedNotifyMessageInteractionsImpl advanced() {
        return interactions;
    }

    @Override
    public void close() {
        new Actions(driver).moveToElement(advanced().getRootElement()).perform();
        Graphene.waitModel().until().element(advanced().getCloseIconElement()).is().visible();
        final List<WebElement> messages = driver.findElements(By.cssSelector("div.rf-ntf-cnt"));
        final int sizeBefore = messages.size();
        new Actions(driver).click(advanced().getCloseIconElement()).perform();
        Graphene.waitModel().withMessage("The message did not disappear.").until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return messages.size() == (sizeBefore - 1);
            }
        });
    }

    @Override
    protected String getCssClass(MessageType type) {
        return getStyleClassForMessageType(type);
    }

    public static String getStyleClassForMessageType(MessageType type) {
        switch (type) {
            case ERROR:
                return "rf-ntf-err";
            case FATAL:
                return "rf-ntf-ftl";
            case INFORMATION:
                return "rf-ntf-inf";
            case OK:
                return "rf-ntf-ok";
            case WARNING:
                return "rf-ntf-wrn";
            default:
                throw new UnsupportedOperationException("Unknown message type " + type);
        }
    }

    public class AdvancedNotifyMessageInteractionsImpl extends AdvancedMessageInteractionsImpl implements NotifyMessage.AdvancedNotifyMessageIteractions {

        @Override
        public WebElement getDetailElement() {
            return messageDetailElement;
        }

        @Override
        public WebElement getSummaryElement() {
            return messageSummaryElement;
        }

        @Override
        public WebElement getCloseElement() {
            return closeElement;
        }

        @Override
        public WebElement getCloseIconElement() {
            return closeIconElement;
        }

        @Override
        public NotifyMessagePosition getPosition() {
            return RichFacesNotifyMessagePosition.getPositionFromElement(getRootElement());
        }

        @Override
        public WebElement getShadowElement() {
            return shadowElement;
        }

        @Override
        public boolean isVisible() {
            return Utils.isVisible(getRootElement());
        }
    }

    private enum RichFacesNotifyMessagePosition {

        BOTTOM_LEFT(NotifyMessagePosition.BOTTOM_LEFT, "rf-ntf-pos-bl"),
        BOTTOM_RIGHT(NotifyMessagePosition.BOTTOM_RIGHT, "rf-ntf-pos-br"),
        TOP_LEFT(NotifyMessagePosition.TOP_LEFT, "rf-ntf-pos-tl"),
        TOP_RIGHT(NotifyMessagePosition.TOP_RIGHT, "rf-ntf-pos-tr");

        private final String containsClass;
        private final NotifyMessagePosition position;

        private RichFacesNotifyMessagePosition(NotifyMessagePosition position, String containsClass) {
            this.position = position;
            this.containsClass = containsClass;
        }

        static NotifyMessagePosition getPositionFromElement(WebElement element) {
            String styleClasses = element.getAttribute("class");
            for (RichFacesNotifyMessagePosition messagePosition : values()) {
                if (styleClasses.contains(messagePosition.containsClass)) {
                    return messagePosition.position;
                }
            }
            throw new RuntimeException("Cannot obtain position from element: " + element);
        }
    }
}

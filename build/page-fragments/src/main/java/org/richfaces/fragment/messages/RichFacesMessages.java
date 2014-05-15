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
package org.richfaces.fragment.messages;

import java.util.Collections;
import java.util.List;

import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.wait.FluentWait;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.AdvancedInteractions;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.common.WaitingWrapper;
import org.richfaces.fragment.common.WaitingWrapperImpl;
import org.richfaces.fragment.list.AbstractListComponent;
import org.richfaces.fragment.list.ListItem;
import org.richfaces.fragment.message.AbstractMessage;
import org.richfaces.fragment.message.Message;
import org.richfaces.fragment.message.Message.MessageType;
import org.richfaces.fragment.messages.RichFacesMessages.MessageImpl;

import com.google.common.base.Predicate;

/**
 * Component for rich:messages.
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesMessages extends AbstractListComponent<MessageImpl> implements Messages<MessageImpl>, AdvancedInteractions<Messages.AdvancedMessagesInteractions> {

    @FindBy(css = "span.rf-msgs-err")
    private List<MessageImpl> errorMessages;
    @FindBy(css = "span.rf-msgs-ftl")
    private List<MessageImpl> fatalMessages;
    @FindBy(css = "span.rf-msgs-inf")
    private List<MessageImpl> infoMessages;
    @FindBy(css = "span.rf-msgs-ok")
    private List<MessageImpl> okMessages;
    @FindBy(css = "span.rf-msgs-wrn")
    private List<MessageImpl> warnMessages;

    private final AdvancedMessagesInteractionsImpl interactions = new AdvancedMessagesInteractionsImpl();

    @Override
    public AdvancedMessagesInteractionsImpl advanced() {
        return interactions;
    }

    @Override
    public List<? extends Message> getItems(MessageType type) {
        switch (type) {
            case OK:
                return Collections.unmodifiableList(okMessages);
            case INFORMATION:
                return Collections.unmodifiableList(infoMessages);
            case WARNING:
                return Collections.unmodifiableList(warnMessages);
            case ERROR:
                return Collections.unmodifiableList(errorMessages);
            case FATAL:
                return Collections.unmodifiableList(fatalMessages);
            default:
                throw new UnsupportedOperationException("Unknown type " + type);
        }
    }

    public static class MessageImpl extends AbstractMessage implements Message, ListItem {

        @FindBy(className = "rf-msgs-det")
        private WebElement messageDetailElement;
        @FindBy(className = "rf-msgs-sum")
        private WebElement messageSummaryElement;

        @Override
        protected String getCssClass(MessageType type) {
            return getCssClassForMessageType(type);
        }

        public static String getCssClassForMessageType(MessageType type) {
            switch (type) {
                case ERROR:
                    return "rf-msgs-err";
                case FATAL:
                    return "rf-msgs-ftl";
                case INFORMATION:
                    return "rf-msgs-inf";
                case OK:
                    return "rf-msgs-ok";
                case WARNING:
                    return "rf-msgs-wrn";
                default:
                    throw new UnsupportedOperationException("Unknown message type " + type);
            }
        }

        @Override
        protected WebElement getMessageDetailElement() {
            return messageDetailElement;
        }

        @Override
        protected WebElement getMessageSummaryElement() {
            return messageSummaryElement;
        }

        @Override
        public GrapheneElement getRootElement() {
            return super.getRootElement();
        }

        @Override
        public String getText() {
            return getRootElement().getText();
        }
    }

    public class AdvancedMessagesInteractionsImpl implements AdvancedMessagesInteractions {

        public WebElement getRootElement() {
            return RichFacesMessages.this.getRoot();
        }

        @Override
        public boolean isVisible() {
            return Utils.isVisible(getRootElement()) && !getItems().isEmpty();
        }

        @Override
        public WaitingWrapper waitUntilMessagesAreNotVisible() {
            return new WaitingWrapperImpl() {

                @Override
                protected void performWait(FluentWait<WebDriver, Void> wait) {
                    wait.until(new Predicate<WebDriver>() {

                        @Override
                        public boolean apply(WebDriver input) {
                            return !isVisible();
                        }
                    });
                }
            }.withMessage("Waiting for message to be not visible.");
        }

        @Override
        public WaitingWrapper waitUntilMessagesAreVisible() {
            return new WaitingWrapperImpl() {

                @Override
                protected void performWait(FluentWait<WebDriver, Void> wait) {
                    wait.until(new Predicate<WebDriver>() {

                        @Override
                        public boolean apply(WebDriver input) {
                            return isVisible();
                        }
                    });
                }
            }.withMessage("Waiting for message to be visible.");
        }
    }
}

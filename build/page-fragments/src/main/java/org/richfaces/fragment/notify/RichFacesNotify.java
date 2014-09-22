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
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.GrapheneElementImpl;
import org.jboss.arquillian.graphene.wait.FluentWait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.common.WaitingWrapper;
import org.richfaces.fragment.common.WaitingWrapperImpl;
import org.richfaces.fragment.list.AbstractListComponent;
import org.richfaces.fragment.list.ListItem;
import org.richfaces.fragment.message.Message.MessageType;
import org.richfaces.fragment.messages.Messages;
import org.richfaces.fragment.notify.RichFacesNotify.NotifyMessageItemImpl;

import com.google.common.base.Predicate;

/**
 * This fragment ignores its findBy. Global component.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesNotify extends AbstractListComponent<NotifyMessageItemImpl> implements Notify<NotifyMessageItemImpl> {

    @Drone
    private WebDriver driver;

    private String styleClass = "";

    private final AdvancedNotifyInteractionsImpl interactions = new AdvancedNotifyInteractionsImpl();

    @Override
    public AdvancedNotifyInteractionsImpl advanced() {
        return interactions;
    }

    private By getByForNotifyWithStyleClass(String additionalStyleClass) {
        return By.cssSelector("div"
            + getSelectorForStyleClassOrEmpty(advanced().getNotifyMessageStyleClass())
            + getSelectorForStyleClassOrEmpty(this.getStyleClass())
            + getSelectorForStyleClassOrEmpty(additionalStyleClass));
    }

    private String getSelectorForStyleClassOrEmpty(String styleClass) {
        return (styleClass == null || styleClass.isEmpty() ? "" : "." + styleClass);
    }

    @Override
    public List<? extends NotifyMessage> getItems(MessageType type) {
        switch (type) {
            case OK:
                throw new UnsupportedOperationException("Notify messages does not support messages of type 'OK'.");
            case INFORMATION:
                return advanced().getInfoMessages();
            case WARNING:
                return advanced().getWarnMessages();
            case ERROR:
                return advanced().getErrorMessages();
            case FATAL:
                return advanced().getFatalMessages();
            default:
                throw new UnsupportedOperationException("Unknown type " + type);
        }
    }

    @Override
    protected List<WebElement> getItemsElements() {
        return driver.findElements(getByForNotifyWithStyleClass(null));
    }

    protected String getStyleClass() {
        return this.styleClass;
    }

    protected void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public class AdvancedNotifyInteractionsImpl implements Messages.AdvancedMessagesInteractions {

        private static final String NOTIFY_MSG_STYLECLASS = "rf-ntf";
        private static final String NOTIFY_FATAL_MSG_STYLECLASS = "rf-ntf-ftl";
        private static final String NOTIFY_ERROR_MSG_STYLECLASS = "rf-ntf-err";
        private static final String NOTIFY_WARN_MSG_STYLECLASS = "rf-ntf-wrn";
        private static final String NOTIFY_INFO_MSG_STYLECLASS = "rf-ntf-inf";

        protected String getNotifyMessageStyleClass() {
            return NOTIFY_MSG_STYLECLASS;
        }

        protected String getNotifyMessageFatalStyleClass() {
            return NOTIFY_FATAL_MSG_STYLECLASS;
        }

        protected String getNotifyMessageErrorStyleClass() {
            return NOTIFY_ERROR_MSG_STYLECLASS;
        }

        protected String getNotifyMessageWarnStyleClass() {
            return NOTIFY_WARN_MSG_STYLECLASS;
        }

        protected String getNotifyMessageInfoStyleClass() {
            return NOTIFY_INFO_MSG_STYLECLASS;
        }

        public List<NotifyMessageItemImpl> getWarnMessages() {
            return instantiateFragments(NotifyMessageItemImpl.class,
                driver.findElements(getByForNotifyWithStyleClass(getNotifyMessageWarnStyleClass())));
        }

        public List<NotifyMessageItemImpl> getErrorMessages() {
            return instantiateFragments(NotifyMessageItemImpl.class,
                driver.findElements(getByForNotifyWithStyleClass(getNotifyMessageErrorStyleClass())));
        }

        public List<NotifyMessageItemImpl> getFatalMessages() {
            return instantiateFragments(NotifyMessageItemImpl.class,
                driver.findElements(getByForNotifyWithStyleClass(getNotifyMessageFatalStyleClass())));
        }

        public List<NotifyMessageItemImpl> getInfoMessages() {
            return instantiateFragments(NotifyMessageItemImpl.class,
                driver.findElements(getByForNotifyWithStyleClass(getNotifyMessageInfoStyleClass())));
        }

        public void setStyleClassToContain(String styleClass) {
            RichFacesNotify.this.setStyleClass(styleClass);
        }

        @Override
        public boolean isVisible() {
            return !getItemsElements().isEmpty();
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
            }.withMessage("Waiting for notify to be not visible.");
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
            }.withMessage("Waiting for notify to be visible.");
        }
    }

    public static class NotifyMessageItemImpl extends RichFacesNotifyMessage implements NotifyMessage, ListItem {

        @Override
        public GrapheneElement getRootElement() {
            return new GrapheneElementImpl(advanced().getRootElement());
        }

        @Override
        public String getText() {
            return getRootElement().getText();
        }
    }

}

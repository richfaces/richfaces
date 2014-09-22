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
package org.richfaces.fragment.message;

import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.graphene.wait.FluentWait;
import org.openqa.selenium.WebDriver;
import org.richfaces.fragment.common.AdvancedInteractions;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.common.WaitingWrapper;
import org.richfaces.fragment.common.WaitingWrapperImpl;

import com.google.common.base.Predicate;

/**
 * Abstract base for message component.
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractMessage implements Message, AdvancedInteractions<Message.AdvancedMessageInteractions> {

    @Root
    private GrapheneElement root;

    protected abstract String getCssClass(MessageType type);

    @Override
    public String getDetail() {
        return advanced().getDetailElement().getText();
    }

    @Override
    public String getSummary() {
        return advanced().getSummaryElement().getText();
    }

    @Override
    public MessageType getType() {
        String attribute = advanced().getRootElement().getAttribute("class");
        for (MessageType type : MessageType.values()) {
            if (attribute.contains(getCssClass(type))) {
                return type;
            }
        }
        return null;
    }

    public abstract class AdvancedMessageInteractionsImpl implements AdvancedMessageInteractions {

        @Override
        public GrapheneElement getRootElement() {
            return root;
        }

        @Override
        public boolean isVisible() {
            return Utils.isVisible(getSummaryElement()) || Utils.isVisible(getDetailElement());
        }

        @Override
        public WaitingWrapper waitUntilMessageIsNotVisible() {
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
        public WaitingWrapper waitUntilMessageIsVisible() {
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

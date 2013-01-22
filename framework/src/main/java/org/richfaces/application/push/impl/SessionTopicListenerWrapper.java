/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
package org.richfaces.application.push.impl;

import org.richfaces.application.push.EventAbortedException;
import org.richfaces.application.push.SessionPreSubscriptionEvent;
import org.richfaces.application.push.SessionSubscriptionEvent;
import org.richfaces.application.push.SessionTopicListener;
import org.richfaces.application.push.SessionTopicListener2;
import org.richfaces.application.push.SessionUnsubscriptionEvent;
import org.richfaces.application.push.SubscriptionFailureException;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * @author Nick Belaevski
 *
 */
@SuppressWarnings("deprecation")
final class SessionTopicListenerWrapper implements SessionTopicListener2 {
    private static final Logger LOGGER = RichfacesLogger.APPLICATION.getLogger();
    private SessionTopicListener listener;

    public SessionTopicListenerWrapper(SessionTopicListener listener) {
        super();
        this.listener = listener;
    }

    public void processPreSubscriptionEvent(SessionPreSubscriptionEvent event) throws SubscriptionFailureException {
        try {
            listener.processPreSubscriptionEvent(event);
        } catch (EventAbortedException e) {
            throw new SubscriptionFailureException(e.getMessage());
        }
    }

    public void processSubscriptionEvent(SessionSubscriptionEvent event) {
        try {
            listener.processSubscriptionEvent(event);
        } catch (EventAbortedException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void processUnsubscriptionEvent(SessionUnsubscriptionEvent event) {
        try {
            listener.processUnsubscriptionEvent(event);
        } catch (EventAbortedException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    SessionTopicListener getWrappedListener() {
        return listener;
    }
}

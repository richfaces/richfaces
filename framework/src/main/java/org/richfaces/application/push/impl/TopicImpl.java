/*
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
 */
package org.richfaces.application.push.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.richfaces.application.push.MessageException;
import org.richfaces.application.push.Session;
import org.richfaces.application.push.SessionSubscriptionEvent;
import org.richfaces.application.push.SessionUnsubscriptionEvent;
import org.richfaces.application.push.TopicEvent;
import org.richfaces.application.push.TopicKey;

/**
 * @author Nick Belaevski
 *
 */
public class TopicImpl extends AbstractTopic {
    private static final class PublishTask implements Runnable {
        private final TopicContext topicContext;

        public PublishTask(TopicContext topicContext) {
            super();
            this.topicContext = topicContext;
        }

        public void run() {
            topicContext.publishMessages();
        }
    }

    private final class TopicContext {
        private final List<Session> sessions = new CopyOnWriteArrayList<Session>();
        private final Queue<String> serializedMessages = new ConcurrentLinkedQueue<String>();
        private final TopicKey key;
        private boolean submittedForPublishing;

        public TopicContext(TopicKey key) {
            super();
            this.key = key;
        }

        public void addSession(Session session) {
            sessions.add(session);
        }

        public void removeSession(Session session) {
            sessions.remove(session);
        }

        public void addMessage(String serializedMessageData) {
            serializedMessages.add(serializedMessageData);

            submitForPublishing();
        }

        public void publishMessages() {
            Iterator<String> itr = serializedMessages.iterator();
            while (itr.hasNext()) {
                String message = itr.next();

                for (Session session : sessions) {
                    session.push(key, message);
                }

                itr.remove();
            }

            synchronized (this) {
                submittedForPublishing = false;

                if (!serializedMessages.isEmpty()) {
                    submitForPublishing();
                }
            }
        }

        private synchronized void submitForPublishing() {
            if (!submittedForPublishing) {
                submittedForPublishing = true;

                topicsContext.getPublisherService().submit(new PublishTask(this));
            }
        }
    }

    private ConcurrentMap<TopicKey, TopicContext> sessions = new ConcurrentHashMap<TopicKey, TopicContext>();
    private TopicsContextImpl topicsContext;

    public TopicImpl(TopicKey key, TopicsContextImpl topicsContext) {
        super(key);

        this.topicsContext = topicsContext;
    }

    private TopicContext getTopicContext(TopicKey key) {
        return sessions.get(key);
    }

    private TopicContext getOrCreateTopicContext(TopicKey key) {
        TopicContext result = sessions.get(key);
        if (result == null) {
            TopicContext freshContext = new TopicContext(key);
            result = sessions.putIfAbsent(key, freshContext);
            if (result == null) {
                result = freshContext;
            }
        }
        return result;
    }

    @Override
    public void publish(TopicKey key, Object messageData) throws MessageException {
        String serializedData = getMessageDataSerializer().serialize(messageData);

        if (serializedData != null) {
            TopicContext topicContext = getTopicContext(key);
            if (topicContext != null) {
                topicContext.addMessage(serializedData);
            }
        }
    }

    @Override
    public void publishEvent(TopicEvent event) {
        super.publishEvent(event);

        if (event instanceof SessionSubscriptionEvent) {
            SessionSubscriptionEvent subscriptionEvent = (SessionSubscriptionEvent) event;

            getOrCreateTopicContext(subscriptionEvent.getTopicKey()).addSession(subscriptionEvent.getSession());
        } else if (event instanceof SessionUnsubscriptionEvent) {
            SessionUnsubscriptionEvent unsubscriptionEvent = (SessionUnsubscriptionEvent) event;

            getTopicContext(unsubscriptionEvent.getTopicKey()).removeSession(unsubscriptionEvent.getSession());
        }
    }
}

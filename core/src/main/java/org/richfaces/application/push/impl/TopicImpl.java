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
 */
public class TopicImpl extends AbstractTopic {

    private ConcurrentMap<TopicKey, PublishingContext> sessions = new ConcurrentHashMap<TopicKey, PublishingContext>();
    private TopicsContextImpl topicsContext;

    public TopicImpl(TopicKey key, TopicsContextImpl topicsContext) {
        super(key);

        this.topicsContext = topicsContext;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.application.push.AbstractTopic#publish(java.lang.Object)
     */
    @Override
    public void publish(Object messageData) throws MessageException {
        publish(messageData, null);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.application.push.AbstractTopic#publish(java.lang.Object, java.lang.String)
     */
    @Override
    public void publish(Object messageData, String subtopicName) throws MessageException {
        String serializedData = getMessageDataSerializer().serialize(messageData);

        if (serializedData != null) {
            PublishingContext topicContext = getPublishingContext(getKey());
            if (topicContext != null) {
                topicContext.addMessage(serializedData);
            }
            // support publishing to contexts that are only interested in specific subtopics
            if (subtopicName != null && getKey().getSubtopicName() == null) {
                topicContext = getPublishingContext(new TopicKey(getKey().getTopicName(), subtopicName));
                if (topicContext != null) {
                    topicContext.addMessage(serializedData);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.application.push.AbstractTopic#publishEvent(org.richfaces.application.push.TopicEvent)
     */
    @Override
    public void publishEvent(TopicEvent event) {
        super.publishEvent(event);

        if (event instanceof SessionSubscriptionEvent) {
            SessionSubscriptionEvent subscriptionEvent = (SessionSubscriptionEvent) event;

            getOrCreatePublishingContext(subscriptionEvent.getTopicKey()).addSession(subscriptionEvent.getSession());
        } else if (event instanceof SessionUnsubscriptionEvent) {
            SessionUnsubscriptionEvent unsubscriptionEvent = (SessionUnsubscriptionEvent) event;

            getPublishingContext(unsubscriptionEvent.getTopicKey()).removeSession(unsubscriptionEvent.getSession());
        }
    }

    /**
     * Returns existing {@link PublishingContext} for given key or creates a null if there is no such {@link PublishingContext}.
     */
    private PublishingContext getPublishingContext(TopicKey key) {
        return sessions.get(key);
    }

    /**
     * Returns existing {@link PublishingContext} for given key or creates a new one if there is no such
     * {@link PublishingContext} yet.
     */
    private PublishingContext getOrCreatePublishingContext(TopicKey key) {
        PublishingContext result = sessions.get(key);
        if (result == null) {
            PublishingContext freshContext = new PublishingContext(key);
            result = sessions.putIfAbsent(key, freshContext);
            if (result == null) {
                result = freshContext;
            }
        }
        return result;
    }

    /**
     * Binds a {@link TopicKey} with list of {@link Session}s subscribed to given topic.
     */
    private final class PublishingContext {
        private final List<Session> sessions = new CopyOnWriteArrayList<Session>();
        private final Queue<String> serializedMessages = new ConcurrentLinkedQueue<String>();
        private final TopicKey key;
        private boolean submittedForPublishing;

        public PublishingContext(TopicKey key) {
            super();
            this.key = key;
        }

        /**
         * Subscribe session for listening for new messages in associated {@link TopicKey}
         */
        public void addSession(Session session) {
            sessions.add(session);
        }

        /**
         * Removes session from listening for new messages in associated {@link TopicKey}
         */
        public void removeSession(Session session) {
            sessions.remove(session);
        }

        /**
         * Adds new message and submits this context for publishing
         */
        public void addMessage(String serializedMessageData) {
            serializedMessages.add(serializedMessageData);

            submitForPublishing();
        }

        /**
         * Publishes messages that are scheduled for publishing.
         *
         * If there are any messages in the queue once finished publishing,
         * a new round of publishing is scheduled.
         */
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

    /**
     * A task used for scheduling publishing of messages on given {@link TopicsContext}.
     */
    private static final class PublishTask implements Runnable {
        private final PublishingContext topicContext;

        public PublishTask(PublishingContext topicContext) {
            super();
            this.topicContext = topicContext;
        }

        @Override
        public void run() {
            topicContext.publishMessages();
        }
    }
}

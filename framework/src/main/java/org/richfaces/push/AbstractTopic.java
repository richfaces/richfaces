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
package org.richfaces.push;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * Topic encapsulates particular endpoint for sending/receiving messages.
 *
 * @author Nick Belaevski
 *
 * @see Topic
 */
public abstract class AbstractTopic implements Topic {
    private static final Logger LOGGER = RichfacesLogger.APPLICATION.getLogger();
    private TopicKey key;
    private volatile MessageDataSerializer serializer;
    private volatile boolean allowSubtopics;
    private List<TopicListener> listeners = new CopyOnWriteArrayList<TopicListener>();

    public AbstractTopic(TopicKey key) {
        super();
        this.key = key;
    }

    /*
     * (non-Javadoc)
     * @see org.richfaces.push.Topic#getMessageDataSerializer()
     */
    @Override
    public MessageDataSerializer getMessageDataSerializer() {
        if (serializer == null) {
            return DefaultMessageDataSerializer.instance();
        }

        return serializer;
    }

    /*
     * (non-Javadoc)
     * @see org.richfaces.push.Topic#setMessageDataSerializer(org.richfaces.push.MessageDataSerializer)
     */
    @Override
    public void setMessageDataSerializer(MessageDataSerializer serializer) {
        this.serializer = serializer;
    }

    /**
     * Returns true if this topic allow to use subtopics
     */
    public boolean isAllowSubtopics() {
        return allowSubtopics;
    }

    /**
     * Allow or disallow use of topics
     */
    public void setAllowSubtopics(boolean allowSubtopics) {
        this.allowSubtopics = allowSubtopics;
    }

    /*
     * (non-Javadoc)
     * @see org.richfaces.push.Topic#getKey()
     */
    @Override
    public TopicKey getKey() {
        return key;
    }

    /*
     * (non-Javadoc)
     * @see org.richfaces.push.Topic#addTopicListener(org.richfaces.push.TopicListener)
     */
    @Override
    public void addTopicListener(TopicListener topicListener) {
        TopicListener listener = topicListener;

        listeners.add(listener);
    }

    /*
     * (non-Javadoc)
     * @see org.richfaces.push.Topic#removeTopicListener(org.richfaces.push.TopicListener)
     */
    @Override
    public void removeTopicListener(TopicListener topicListener) {
        listeners.remove(topicListener);
    }

    /*
     * (non-Javadoc)
     * @see org.richfaces.push.Topic#checkSubscription(org.richfaces.push.Session)
     */
    @Override
    public void checkSubscription(Session session) throws SubscriptionFailureException {
        SessionPreSubscriptionEvent event = new SessionPreSubscriptionEvent(this, getKey(), session);
        for (TopicListener listener : listeners) {
            if (event.isAppropriateListener(listener)) {
                try {
                    event.invokeListener(listener);
                } catch (SubscriptionFailureException e) {
                    throw e;
                } catch (Exception e) {
                    logError(e);
                }
            }
        }
    }

    private void logError(Exception e) {
        LOGGER.error(MessageFormat.format("Exception invoking listener: {0}", e.getMessage()), e);
    }

    /*
     * (non-Javadoc)
     * @see org.richfaces.push.Topic#publishEvent(org.richfaces.push.TopicEvent)
     */
    @Override
    public void publishEvent(TopicEvent event) {
        for (TopicListener listener : listeners) {
            if (event.isAppropriateListener(listener)) {
                try {
                    event.invokeListener(listener);
                } catch (Exception e) {
                    logError(e);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see org.richfaces.push.Topic#publish(java.lang.Object)
     */
    @Override
    public abstract void publish(Object messageData) throws MessageException;
}

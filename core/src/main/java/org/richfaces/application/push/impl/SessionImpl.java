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

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

import javax.faces.context.FacesContext;

import org.richfaces.application.configuration.ConfigurationServiceHelper;
import org.richfaces.application.push.DestroyableSession;
import org.richfaces.application.push.MessageData;
import org.richfaces.application.push.Request;
import org.richfaces.application.push.Session;
import org.richfaces.application.push.SessionManager;
import org.richfaces.application.push.SessionSubscriptionEvent;
import org.richfaces.application.push.SessionUnsubscriptionEvent;
import org.richfaces.application.push.SubscriptionFailureException;
import org.richfaces.application.push.Topic;
import org.richfaces.application.push.TopicKey;
import org.richfaces.application.push.TopicsContext;
import org.richfaces.application.CoreConfiguration;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * Session represents user’s subscription to a set of topics
 *
 * @author Nick Belaevski
 *
 * @see Session
 */
public class SessionImpl implements Session, DestroyableSession {
    private static final Logger LOGGER = RichfacesLogger.APPLICATION.getLogger();
    private final int maxInactiveInterval;
    private final String id;
    private final SessionManager sessionManager;
    private volatile long lastAccessedTime;
    private volatile Request request;
    private volatile boolean active = true;
    private final Queue<MessageData> messagesQueue = new ConcurrentLinkedQueue<MessageData>();
    private final Set<TopicKey> successfulSubscriptions = Sets.newHashSet();
    private final Map<TopicKey, String> failedSubscriptions = Maps.newHashMap();
    private TopicsContext topicsContext;
    private AtomicLong sequenceCounter = new AtomicLong();

    public SessionImpl(String id, SessionManager sessionManager, TopicsContext topicsContext) {
        super();

        this.id = id;
        this.sessionManager = sessionManager;
        this.topicsContext = topicsContext;

        FacesContext facesContext = FacesContext.getCurrentInstance();
        this.maxInactiveInterval = ConfigurationServiceHelper.getIntConfigurationValue(facesContext,
                CoreConfiguration.Items.pushSessionMaxInactiveInterval);

        resetLastAccessedTimeToCurrent();
    }

    private void resetLastAccessedTimeToCurrent() {
        lastAccessedTime = System.currentTimeMillis();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.application.push.Session#connect(org.richfaces.application.push.Request)
     */
    @Override
    public synchronized void connect(Request request) throws Exception {
        releaseRequest();

        if (active) {
            processConnect(request);
        } else {
            request.resume();
        }
    }

    /**
     * Returns {@link Request} associated with this session
     */
    protected Request getRequest() {
        return request;
    }

    /**
     * Process connecting of given request to this session and try to post messages if there are any
     */
    protected void processConnect(Request request) throws Exception {
        this.request = request;
        sessionManager.requeue(this);

        request.postMessages();
    }

    private void releaseRequest() {
        Request localRequestCopy = this.request;

        if (localRequestCopy != null) {
            resetLastAccessedTimeToCurrent();
            this.request = null;

            localRequestCopy.resume();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.application.push.Session#disconnect()
     */
    @Override
    public synchronized void disconnect() throws Exception {
        releaseRequest();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.application.push.Session#getLastAccessedTime()
     */
    @Override
    public long getLastAccessedTime() {
        if (!active) {
            return -1;
        }

        if (this.request != null) {
            // being accessed right now
            return System.currentTimeMillis();
        } else {
            return lastAccessedTime;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.application.push.Session#getMaxInactiveInterval()
     */
    @Override
    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.application.push.Session#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.application.push.Session#invalidate()
     */
    @Override
    public void invalidate() {
        active = false;

        sessionManager.requeue(this);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.application.push.DestroyableSession#destroy()
     */
    @Override
    public synchronized void destroy() {
        active = false;

        for (TopicKey key : successfulSubscriptions) {
            Topic topic = topicsContext.getTopic(key);
            topic.publishEvent(new SessionUnsubscriptionEvent(topic, key, this));
        }

        try {
            disconnect();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.application.push.Session#getFailedSubscriptions()
     */
    @Override
    public Map<TopicKey, String> getFailedSubscriptions() {
        return failedSubscriptions;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.application.push.Session#getSuccessfulSubscriptions()
     */
    @Override
    public Collection<TopicKey> getSuccessfulSubscriptions() {
        return successfulSubscriptions;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.application.push.Session#subscribe(java.lang.String[])
     */
    @Override
    public void subscribe(String[] topics) {
        Iterable<TopicKey> topicKeys = Iterables.transform(Lists.newLinkedList(Arrays.asList(topics)), TopicKey.factory());

        createSubscriptions(topicKeys);
    }

    private void createSubscriptions(Iterable<TopicKey> topicKeys) {
        for (TopicKey topicKey : topicKeys) {
            Topic pushTopic = topicsContext.getOrCreateTopic(topicKey);

            String errorMessage = null;

            if (pushTopic == null) {
                errorMessage = MessageFormat.format("Topic ''{0}'' is not configured", topicKey.getTopicAddress());
            } else {
                try {
                    // TODO - publish another events
                    pushTopic.checkSubscription(topicKey, this);
                } catch (SubscriptionFailureException e) {
                    if (e.getMessage() != null) {
                        errorMessage = e.getMessage();
                    } else {
                        errorMessage = MessageFormat.format("Unknown error connecting to ''{0}'' topic",
                                topicKey.getTopicAddress());
                    }
                }
            }

            if (errorMessage != null) {
                failedSubscriptions.put(topicKey, errorMessage);
            } else {
                pushTopic.publishEvent(new SessionSubscriptionEvent(pushTopic, topicKey, this));
                successfulSubscriptions.add(topicKey);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.application.push.Session#getMessages()
     */
    @Override
    public Collection<MessageData> getMessages() {
        return messagesQueue;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.application.push.Session#clearBroadcastedMessages(long)
     */
    @Override
    public void clearBroadcastedMessages(long sequenceNumber) {
        Queue<MessageData> queue = messagesQueue;
        while (true) {
            MessageData message = queue.peek();
            if (message == null || sequenceNumber < message.getSequenceNumber()) {
                break;
            }

            queue.remove();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.application.push.Session#push(org.richfaces.application.push.TopicKey, java.lang.String)
     */
    @Override
    public void push(TopicKey topicKey, String serializedData) {
        MessageData serializedMessage = new MessageData(topicKey, serializedData, sequenceCounter.getAndIncrement());
        messagesQueue.add(serializedMessage);
        synchronized (this) {
            if (request != null) {
                request.postMessages();
            }
        }
    }
}

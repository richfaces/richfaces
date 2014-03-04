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

import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * Session represents user’s subscription to a set of topics. This set is immutable, so new session should be
 * created if user wants to add/remove subscriptions to some topics.
 * </p>
 *
 * <p>
 * Session does multiplexing for messages from several topics, so that only a single client connection is required to for data
 * transfer.
 * </p>
 *
 * <p>
 * When session is created, it is getting unique UUID identifier, that client uses to communicate to that session.
 * </p>
 *
 * <p>
 * When client is subscribed to a set of topics associated with some session, session fires SessionPreSubscriptionEvent, so that
 * application developer can control subscriptions according to access rights.
 * </p>
 *
 * <p>
 * Session is kept alive for ({@link #getMaxInactiveInterval()}) minutes since the last time it has been accessed. Note that push can work in either polling (long
 * polling) and persistent connection (WebSocket) modes, so session should be kept-alive correctly in both cases.
 * </p>
 *
 * @author Nick Belaevski
 *
 */
public interface Session {

    /**
     * Returns unique identifier of this session.
     */
    String getId();

    /**
     * How much minutes is session kept alive since the last time it has been accessed before it is destroyed
     */
    int getMaxInactiveInterval();

    /**
     * Returns the last time when the session was accessed
     */
    long getLastAccessedTime();

    /**
     * Get a list of topic keys this session is successfully subscribed to
     */
    Collection<TopicKey> getSuccessfulSubscriptions();

    /**
     * Get a map of topic keys this session failed to be subscribed to including the message why the subscription failed
     */
    Map<TopicKey, String> getFailedSubscriptions();

    /**
     * Subscribe this session to given topics
     */
    void subscribe(String[] topics);

    /**
     * Connects given request to this session
     */
    void connect(Request request) throws Exception;

    /**
     * Releases/disconnects associated request from this session
     */
    void disconnect() throws Exception;

    /**
     * Invalidates this session, making it non-active
     */
    void invalidate();

    /**
     * Pushes given data to given topic key
     */
    void push(TopicKey topicKey, String serializedData);

    /**
     * Returns list of messages associated with this push session
     */
    Collection<MessageData> getMessages();

    /**
     * Clears all the queues messages associated with this session that has sequence number lower than provided number
     */
    void clearBroadcastedMessages(long sequenceNumber);
}

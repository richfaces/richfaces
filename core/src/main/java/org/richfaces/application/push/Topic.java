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
package org.richfaces.application.push;

/**
 * <p>
 * Topic encapsulates particular endpoint for sending/receiving messages.
 * </p>
 *
 * <p>
 * It is associated with TopicKey and in current implementation several Topic instances can be associated with the particular
 * JMS Topic.
 * </p>
 *
 * <p>
 * Topic holds collection of TopicListener and one MessageDataSerializer.
 * </p>
 *
 * @author Nick Belaevski
 */
public interface Topic {

    /**
     * Returns the key which this topic is associated with
     */
    TopicKey getKey();

    /**
     * Returns the {@link MessageDataSerializer} that is associated with this topic
     */
    MessageDataSerializer getMessageDataSerializer();

    /**
     * Associated this topic with given {@link MessageDataSerializer}
     *
     * @param serializer
     */
    void setMessageDataSerializer(MessageDataSerializer serializer);

    /**
     * Adds listener through that topic events will be published (by calling {@link #publishEvent(TopicEvent)}).
     */
    void addTopicListener(TopicListener topicListener);

    /**
     * Removes topic listener
     */
    void removeTopicListener(TopicListener topicListener);

    /**
     * Checks that the given session can be subscribed to this topic
     *
     * @throws SubscriptionFailureException when given session can't be subscribed to this topic
     */
    void checkSubscription(TopicKey topicKey, Session session) throws SubscriptionFailureException;

    /**
     * Publishes topic event to all subscribed TopicListeners
     */
    void publishEvent(TopicEvent event);

    /**
     * Publish data to the subscribed clients
     *
     * @param messageData data that will be serialized by MessageDataSerializer
     *
     * @throws MessageException
     */
    void publish(Object messageData) throws MessageException;

    /**
     * Publish data to the subscribed clients
     *
     * @param messageData data that will be serialized by MessageDataSerializer
     * @param subtopicName optional subtopic, used to publish to clients that are only interested in specific subtopics
     *
     * @throws MessageException
     */
    void publish(Object messageData, String subtopicName) throws MessageException;
}
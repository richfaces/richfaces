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
package org.richfaces.application.push;

import java.text.MessageFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.richfaces.application.ServiceTracker;

/**
 * @author Nick Belaevski
 *
 */
// TODO annotations for declarative topics registration
public abstract class TopicsContext {
    private ConcurrentMap<String, Topic> topics = new ConcurrentHashMap<String, Topic>();

    protected abstract Topic createTopic(TopicKey key);

    public Topic getOrCreateTopic(TopicKey key) {
        Topic result = topics.get(key.getTopicName());

        if (result == null) {
            Topic newTopic = createTopic(key);
            result = topics.putIfAbsent(key.getTopicName(), newTopic);
            if (result == null) {
                result = newTopic;
            }
        }

        return result;
    }

    public Topic getTopic(TopicKey key) {
        return topics.get(key.getTopicName());
    }

    public void removeTopic(TopicKey key) {
        topics.remove(key.getTopicName());
    }

    public void publish(TopicKey key, Object data) throws MessageException {
        Topic topic = getTopic(key);

        if (topic == null) {
            throw new MessageException(MessageFormat.format("Topic {0} not found", key.getTopicName()));
        }

        topic.publish(key, data);
    }

    public static TopicsContext lookup() {
        return ServiceTracker.getService(PushContextFactory.class).getPushContext().getTopicsContext();
    }
}

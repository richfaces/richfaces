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

import javax.el.ELContext;
import javax.faces.context.FacesContext;

import org.richfaces.application.ServiceTracker;
import org.richfaces.el.util.ELUtils;

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
        TopicKey resolvedKey = getTopicKeyWithResolvedExpressions(key);

        Topic topic = getTopic(resolvedKey);

        if (topic == null) {
            throw new MessageException(MessageFormat.format("Topic {0} not found", resolvedKey.getTopicName()));
        }

        topic.publish(resolvedKey, data);
    }

    public static TopicsContext lookup() {
        return ServiceTracker.getService(PushContextFactory.class).getPushContext().getTopicsContext();
    }

    protected TopicKey getTopicKeyWithResolvedExpressions(TopicKey key) {
        String topicName = key.getTopicName();
        String subtopicName = key.getSubtopicName();
        String topicAddress = key.getTopicAddress();

        if (isExpression(topicName) || isExpression(subtopicName)) {
            topicName = evaluateExpression(topicName);
            subtopicName = evaluateExpression(subtopicName);
            return new TopicKey(topicName, subtopicName);
        } else if (isExpression(topicAddress)) {
            topicAddress = evaluateExpression(topicAddress);
            return new TopicKey(topicAddress);
        } else {
            return key;
        }
    }

    private boolean isExpression(String expression) {
        return ELUtils.isValueReference(expression);
    }

    private String evaluateExpression(String expression) {
        if (isExpression(expression)) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ELContext elContext = facesContext.getELContext();
            Object evaluated = ELUtils.evaluateValueExpression(ELUtils.createValueExpression(expression), elContext);
            if (evaluated == null) {
                throw new NullPointerException("expression '" + expression + "' was evaluated to null");
            }
            return evaluated.toString();
        }
        return expression;
    }
}

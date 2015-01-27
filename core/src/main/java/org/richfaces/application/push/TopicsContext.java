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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.el.ELContext;
import javax.faces.context.FacesContext;

import org.richfaces.application.ServiceTracker;
import org.richfaces.el.util.ELUtils;

/**
 * <p>
 * TopicsContext is a per-application singleton tracking all registered topics.
 *
 * <p>
 * It is erroneous to communicate with the Topic not registered via TopicsContext.
 * </p>
 *
 * <p>
 * Application developer obtains instance of TopicsContext via static lookup() method.
 * </p>
 *
 * <p>
 * When TopicsContext is being looked up for the first time, it triggers creation of {@link PushContext} via
 * {@link PushContextFactory} that is configured as RichFaces service.
 * </p>
 *
 * @author Nick Belaevski
 */
public abstract class TopicsContext {

    private ConcurrentMap<String, Topic> topics = new ConcurrentHashMap<String, Topic>();

    /**
     * Creates topic for given topic key
     */
    protected abstract Topic createTopic(TopicKey key);

    /**
     * Returns a TopicKey without a subtopic for given TopicKey.
     */
    private TopicKey createTopicKeyWithoutSubTopic(TopicKey key) {
        return key.getSubtopicName() == null ? key : new TopicKey(key.getTopicName(), null);
    }

    /**
     * <p>
     * Creates topic for given key or returns existing one when it was already created.
     * </p>
     *
     * <p>
     * This method is thread-safe.
     * </p>
     */
    public Topic getOrCreateTopic(TopicKey key) {
        Topic result = topics.get(key.getTopicName());

        if (result == null) {
            Topic newTopic = createTopic(createTopicKeyWithoutSubTopic(key));
            result = topics.putIfAbsent(key.getTopicName(), newTopic);
            if (result == null) {
                result = newTopic;
            }
        }

        return result;
    }

    /**
     * Returns topic for given key or null if no such topic was created yet.
     */
    public Topic getTopic(TopicKey key) {
        return topics.get(key.getTopicName());
    }

    /**
     * Removes topic with given key or does nothing when no such topic was created yet.
     */
    public void removeTopic(TopicKey key) {
        topics.remove(key.getTopicName());
    }

    /**
     * <p>
     * Publishes data through the topic with given key.
     * </p>
     *
     * <p>
     * The provided topic key can contain expressions as the name of topic or its subtopic. In such case, the topic name or
     * subtopic name will be first evaluated, which will form actual topic key that will be used to publish a message.
     * </p>
     *
     * @throws MessageException when topic with given key fails to publish given data object.
     */
    public void publish(TopicKey key, Object data) throws MessageException {
        TopicKey resolvedKey = getTopicKeyWithResolvedExpressions(key);

        Topic topic = getOrCreateTopic(resolvedKey);

        topic.publish(data, resolvedKey.getSubtopicName());
    }

    /**
     * Look-ups per-application singleton of {@link TopicsContext} tracking all registered topics.
     */
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

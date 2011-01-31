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
package org.richfaces.application.push.impl.jms;

import java.util.concurrent.ExecutorService;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.atmosphere.cpr.AtmosphereResource;
import org.richfaces.application.push.MessageDataSerializer;
import org.richfaces.application.push.MessageException;
import org.richfaces.application.push.Session;
import org.richfaces.application.push.TopicKey;
import org.richfaces.application.push.TopicsContext;
import org.richfaces.application.push.impl.AbstractRequest;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * @author Nick Belaevski
 * 
 */
public class RequestImpl extends AbstractRequest implements org.richfaces.application.push.MessageListener {

    private static final Logger LOGGER = RichfacesLogger.APPLICATION.getLogger();
    
    private TopicsContext topicsContext;
    
    public RequestImpl(AtmosphereResource<HttpServletRequest, HttpServletResponse> atmosphereResource, Session session,
        ExecutorService executorService, TopicsContext topicsContext) {

        super(atmosphereResource, session, executorService);
        
        this.topicsContext = topicsContext;
    }

    private String serializeMessage(org.richfaces.application.push.Topic topic, Message message) {
        String serializedMessageData = null;
        Object messageData = null;
        
        try {
            if (message instanceof ObjectMessage) {
                messageData = ((ObjectMessage) message).getObject();
            } else if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                
                if (message.getBooleanProperty(TopicImpl.SERIALIZED_DATA_INDICATOR)) {
                    serializedMessageData = textMessage.getText();
                } else {
                    messageData = textMessage.getText();
                }
            }
        } catch (JMSException e) {
            LOGGER.error(e.getMessage(), e);
        }
        
        if (serializedMessageData != null) {
            return serializedMessageData;
        }
        
        if (messageData != null) {
            MessageDataSerializer messageDataSerializer = topic.getMessageDataSerializer();
            try {
                return messageDataSerializer.serialize(messageData);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        
        return null;
    }

    public void onMessage(Object message) throws MessageException {
        Message jmsMessage = (Message) message;
        try {
            String topicName = ((Topic) jmsMessage.getJMSDestination()).getTopicName();
            
            org.richfaces.application.push.Topic topic = topicsContext.getTopic(new TopicKey(topicName));
            if (topic == null) {
                //TODO log
                return;
            }
            
            String serializedMessageData = serializeMessage(topic, jmsMessage);
            if (serializedMessageData == null) {
                //TODO log
                return;
            }
        
            postMessage(new TopicKey(topicName, jmsMessage.getStringProperty(MessagingContext.SUBTOPIC_ATTRIBUTE_NAME)), serializedMessageData);
        } catch (JMSException e) {
            throw new MessageException(e.getMessage(), e);
        }
    }
    
    
    public org.richfaces.application.push.MessageListener getMessageListener() {
        return this;
    }
}

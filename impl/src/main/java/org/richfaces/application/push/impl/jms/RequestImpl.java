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

import java.io.IOException;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.atmosphere.cpr.AtmosphereResource;
import org.richfaces.application.push.MessageDataSerializer;
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
public class RequestImpl extends AbstractRequest implements MessageListener {

    private static final Logger LOGGER = RichfacesLogger.APPLICATION.getLogger();
    
    private javax.jms.Session jmsSession;
    
    private MessagingContext messagingContext;
    
    private TopicsContext topicsContext;
    
    public RequestImpl(AtmosphereResource<HttpServletRequest, HttpServletResponse> atmosphereResource, Session session,
        ExecutorService executorService, MessagingContext messagingContext, TopicsContext topicsContext) {

        super(atmosphereResource, session, executorService);
        
        this.messagingContext = messagingContext;
        this.topicsContext = topicsContext;
    }

    private void closeSession() {
        if (jmsSession != null) {
            try {
                jmsSession.close();
            } catch (JMSException e) {
                LOGGER.error(e.getMessage(), e);
            }
            
            jmsSession = null;
        }
    }
    
    @Override
    public void onSuspend() {
        super.onSuspend();

        try {
            jmsSession = messagingContext.createSession();
            
            //TODO - remove this case
            SessionImpl sessionImpl = (SessionImpl) getSession();
            
            for (Entry<TopicKey, Collection<TopicKey>> entry: sessionImpl.getSuccessfulSubscriptions().asMap().entrySet()) {
                messagingContext.createTopicSubscriber(sessionImpl, jmsSession, entry).setMessageListener(this);
            }
            
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        
    }
    
    @Override
    public void flushMessages() throws IOException {
        if (isPolling()) {
            closeSession();
        }
        
        super.flushMessages();
    }
    
    @Override
    public void onDisconnect() {
        closeSession();
        super.onDisconnect();
    }

    @Override
    public void onResume() {
        closeSession();
        super.onResume();
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

    /* (non-Javadoc)
     * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
     */
    public void onMessage(Message message) {
        try {
            String topicName = ((Topic) message.getJMSDestination()).getTopicName();
            
            org.richfaces.application.push.Topic topic = topicsContext.getTopic(new TopicKey(topicName));
            if (topic == null) {
                //TODO log
                return;
            }
            
            String serializedMessageData = serializeMessage(topic, message);
            if (serializedMessageData == null) {
                //TODO log
                return;
            }
        
            postMessage(new TopicKey(topicName, message.getStringProperty(MessagingContext.SUBTOPIC_ATTRIBUTE_NAME)), serializedMessageData);

            message.acknowledge();
        } catch (JMSException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }    
}

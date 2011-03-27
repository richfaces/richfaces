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

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TopicSubscriber;
import javax.naming.NamingException;

import org.richfaces.application.push.EventAbortedException;
import org.richfaces.application.push.Request;
import org.richfaces.application.push.SessionManager;
import org.richfaces.application.push.SessionPreSubscriptionEvent;
import org.richfaces.application.push.Topic;
import org.richfaces.application.push.TopicKey;
import org.richfaces.application.push.TopicsContext;
import org.richfaces.application.push.impl.AbstractSession;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

/**
 * @author Nick Belaevski
 * 
 */
public class SessionImpl extends AbstractSession {

    private static final class JMSToPushListenerAdaptor implements MessageListener {

        private final org.richfaces.application.push.MessageListener messageListener;

        private JMSToPushListenerAdaptor(org.richfaces.application.push.MessageListener messageListener) {
            this.messageListener = messageListener;
        }

        public void onMessage(Message message) {
            try {
                messageListener.onMessage(message);
                message.acknowledge();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    private static final Logger LOGGER = RichfacesLogger.APPLICATION.getLogger();

    private final MessagingContext messagingContext;

    private final TopicsContext topicsContext;

    private final Multimap<TopicKey, TopicKey> successfulSubscriptions = ArrayListMultimap.<TopicKey, TopicKey>create();
    
    private final Map<TopicKey, String> failedSubscriptions = Maps.newHashMap();

    private Session jmsSession;

    private Collection<TopicSubscriber> subscribers = Lists.newArrayListWithCapacity(1);
    
    public SessionImpl(String id, SessionManager sessionManager, MessagingContext messagingContext, TopicsContext topicsContext) {
        super(id, sessionManager);

        this.messagingContext = messagingContext;
        this.topicsContext = topicsContext;
    }

    public Map<TopicKey, String> getFailedSubscriptions() {
        return failedSubscriptions;
    }
    
    public Multimap<TopicKey, TopicKey> getSuccessfulSubscriptions() {
        return successfulSubscriptions;
    }
    
    private void createSubscriptions(Iterable<TopicKey> topicKeys) {
        javax.jms.Session jmsSession = null;
        try {
            Multimap<TopicKey, TopicKey> rootTopicsMap = createRootTopicsKeysMap(topicKeys);
            
            jmsSession = messagingContext.createSession();

            for (Entry<TopicKey, Collection<TopicKey>> entry: rootTopicsMap.asMap().entrySet()) {
                TopicSubscriber subscriber = null;
                
                try {
                    subscriber = messagingContext.createTopicSubscriber(this, jmsSession, entry);
                    successfulSubscriptions.putAll(entry.getKey(), entry.getValue());
                } finally {
                    if (subscriber != null) {
                        subscriber.close();
                    }
                }
                
            }
        } catch (JMSException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (NamingException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (jmsSession != null) {
                try {
                    jmsSession.close();
                } catch (JMSException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }
    
    private Multimap<TopicKey, TopicKey> createRootTopicsKeysMap(Iterable<TopicKey> topicKeys) {
        Multimap<TopicKey, TopicKey> rootTopicKeys = ArrayListMultimap.<TopicKey, TopicKey>create();

        for (TopicKey topicKey : topicKeys) {
            rootTopicKeys.put(topicKey.getRootTopicKey(), topicKey);
        }
        
        return rootTopicKeys;
    }
    
    private void processFailedSubscriptions(Iterable<TopicKey> topicKeys) {
        for (Iterator<TopicKey> itr = topicKeys.iterator(); itr.hasNext(); ) {
            TopicKey topicKey = itr.next();

            TopicKey rootTopicKey = topicKey.getRootTopicKey();
            Topic pushTopic = topicsContext.getTopic(rootTopicKey);
            
            String errorMessage = null;
            
            if (pushTopic == null) {
                errorMessage = MessageFormat.format("Topic ''{0}'' is not configured", topicKey.getTopicAddress()); 
            } else {
                try {
                    //TODO - publish another events
                    pushTopic.publishEvent(new SessionPreSubscriptionEvent(pushTopic, topicKey, this));
                } catch (EventAbortedException e) {
                    if (e.getMessage() != null) {
                        errorMessage = e.getMessage();
                    } else {
                        errorMessage = MessageFormat.format("Unknown error connecting to ''{0}'' topic", topicKey.getTopicAddress());
                    }
                }
            }

            if (errorMessage != null) {
                itr.remove();
                failedSubscriptions.put(topicKey, errorMessage);
            }
        }
    }
    
    @Override
    protected void processConnect(Request request) throws Exception {
        super.processConnect(request);
        
        jmsSession = messagingContext.createSession();
        
        MessageListener jmsListener = new JMSToPushListenerAdaptor(request.getMessageListener());
        
        for (Entry<TopicKey, Collection<TopicKey>> entry: getSuccessfulSubscriptions().asMap().entrySet()) {
            TopicSubscriber subscriber = messagingContext.createTopicSubscriber(this, jmsSession, entry);
            subscribers.add(subscriber);
            subscriber.setMessageListener(jmsListener);
        }
    }
    
    private void clearSubscribers() {
        if (jmsSession != null) {
            for (TopicSubscriber subscriber : subscribers) {
                try {
                    subscriber.close();
                } catch (JMSException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
            subscribers.clear();

            try {
                jmsSession.close();
            } catch (JMSException e) {
                LOGGER.error(e.getMessage(), e);
            }
            
            jmsSession = null;
        }
    }
    
    @Override
    protected void processDisconnect() throws Exception {
        try {
            clearSubscribers();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        
        super.processDisconnect();
    }
    
    public void subscribe(String[] topics) {
        Iterable<TopicKey> topicKeys = Iterables.transform(Lists.newLinkedList(Arrays.asList(topics)), TopicKey.factory());

        processFailedSubscriptions(topicKeys);
        createSubscriptions(topicKeys);
    }

    @Override
    public synchronized void destroy() {
        super.destroy();
        
        //we need to create new JMS session, as this method can be called from another thread - see javax.jms.Session JavaDoc
        //for multi-threading limitations
        Session localJMSSession = null;
        try {
            localJMSSession = messagingContext.createSession();
            messagingContext.removeTopicSubscriber(this, localJMSSession, successfulSubscriptions.keySet());
            
        } catch (JMSException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (localJMSSession != null) {
                try {
                    localJMSSession.close();
                } catch (JMSException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }
}

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

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.richfaces.application.push.EventAbortedException;
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

    private static final Logger LOGGER = RichfacesLogger.APPLICATION.getLogger();

    private final MessagingContext messagingContext;

    private final TopicsContext topicsContext;

    private final Multimap<TopicKey, TopicKey> successfulSubscriptions = ArrayListMultimap.<TopicKey, TopicKey>create();
    
    private final Map<TopicKey, String> failedSubscriptions = Maps.newHashMap();

    public SessionImpl(String id, SessionManager sessionManager, MessagingContext messagingContext, TopicsContext topicsContext) {
        super(id, sessionManager);

        this.messagingContext = messagingContext;
        this.topicsContext = topicsContext;
    }

    public void onRequestSuspended() {
        // TODO Auto-generated method stub
    }

    public void onRequestDisconnected() {
        // TODO Auto-generated method stub
        // TODO Auto-generated method stub
        try {
            disconnect();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void onRequestResumed() {
        // TODO Auto-generated method stub
        try {
            disconnect();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
                messagingContext.createTopicSubscriber(this, jmsSession, entry);
                
                successfulSubscriptions.putAll(entry.getKey(), entry.getValue());
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
            
            try {
                //TODO - publish another events
                pushTopic.publishEvent(new SessionPreSubscriptionEvent(pushTopic, topicKey, this));
            } catch (EventAbortedException e) {
                itr.remove();
                failedSubscriptions.put(topicKey, e.getMessage());
            }
        }
    }
    
    public void subscribe(String[] topics) {
        Iterable<TopicKey> topicKeys = Iterables.transform(Lists.newLinkedList(Arrays.asList(topics)), TopicKey.factory());

        processFailedSubscriptions(topicKeys);
        createSubscriptions(topicKeys);
    }

}

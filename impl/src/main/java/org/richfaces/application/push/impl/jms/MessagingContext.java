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

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.faces.context.FacesContext;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import org.richfaces.application.push.Session;
import org.richfaces.application.push.TopicKey;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;

/**
 * @author Nick Belaevski
 * 
 */
public class MessagingContext {

    static final String SUBTOPIC_ATTRIBUTE_NAME = "rf_push_subtopic";
    
    private static final Joiner OR_JOINER = Joiner.on(" OR ").skipNulls();

    private static final Logger LOGGER = RichfacesLogger.APPLICATION.getLogger();

    private static final Function<TopicKey, String> TOPIC_KEY_TO_MESSAGE_SELECTOR = new Function<TopicKey, String>() {
        public String apply(TopicKey from) {
            if (Strings.isNullOrEmpty(from.getSubtopicName())) {
                return null;
            }
            
            return SUBTOPIC_ATTRIBUTE_NAME + " = '" + from.getSubtopicName() + "'";
        }
    };
    
    private static final String SHARED_INSTANCE_KEY = MessagingContext.class.getName();

    private final InitialContext initialContext;

    private final Name connectionFactoryName;

    private final Name topicsNamespace;

    private final String applicationName;

    private final String username;

    private final String password;

    private Connection connection;

    public MessagingContext(InitialContext initialContext, Name connectionFactoryName, Name topicsNamespace,
        String applicationName, String username, String password) {

        super();
        this.initialContext = initialContext;
        this.connectionFactoryName = connectionFactoryName;
        this.topicsNamespace = topicsNamespace;
        this.applicationName = applicationName;
        this.username = username;
        this.password = password;
    }

    private Name appendToName(Name name, String comp) throws NamingException {
        Name clonedName = (Name) name.clone();
        return clonedName.add(comp);
    }

    public void start() throws Exception {
        ConnectionFactory connectionFactory = (ConnectionFactory) initialContext.lookup(connectionFactoryName);

        connection = connectionFactory.createConnection(username, password);

        //TODO - review
        try {
            //durable subscription requires ClientID to be set
            connection.setClientID(UUID.randomUUID().toString());
        } catch (IllegalStateException e) {
            //ignore - clientId has already been set
        }

        connection.start();
    }

    public void stop() throws Exception {
        connection.close();
        connection = null;
    }

    public Connection getConnection() {
        if (connection == null) {
            throw new IllegalStateException("connection is absent");
        }

        return connection;
    }

    public Topic lookup(TopicKey topicKey) throws NamingException {
        Name topicName = appendToName(topicsNamespace, topicKey.getTopicName());

        return (Topic) initialContext.lookup(topicName);
    }

    public javax.jms.Session createSession() throws JMSException {
        return connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
    }

    public String getSubscriptionClientId(Session session, TopicKey topicKey) {
        //here TopicKey#topicName should be used, not TopicKey#topicAddress
        return "rf-push:" + applicationName + ":" + topicKey.getTopicName() + ":" + session.getId();
    }

    public void shareInstance(FacesContext facesContext) {
        Map<String, Object> applicationMap = facesContext.getExternalContext().getApplicationMap();
        applicationMap.put(SHARED_INSTANCE_KEY, this);
    }

    public static MessagingContext getSharedInstance(ServletContext servletContext) {
        return (MessagingContext) servletContext.getAttribute(SHARED_INSTANCE_KEY);
    }

    private String createMessageSelector(Iterable<TopicKey> topicKeys) {
        Iterable<String> sqlStrings = Iterables.transform(topicKeys, TOPIC_KEY_TO_MESSAGE_SELECTOR);
        return OR_JOINER.join(sqlStrings) + " OR false" /* workaround for HornetQ */;
    }

    public TopicSubscriber createTopicSubscriber(Session pushSession, javax.jms.Session jmsSession, 
        Entry<TopicKey, Collection<TopicKey>> entry) 
        throws JMSException, NamingException {

        TopicKey rootTopicKey = entry.getKey();

        String subscriptionClientId = getSubscriptionClientId(pushSession, rootTopicKey);

        javax.jms.Topic jmsTopic = lookup(rootTopicKey);

        return jmsSession.createDurableSubscriber(jmsTopic, subscriptionClientId, createMessageSelector(entry.getValue()), true);
    }

    /**
     * @param session
     * @param jmsSession
     * @param rootTopicKeys 
     */
    public void removeTopicSubscriber(Session session, javax.jms.Session jmsSession, Collection<TopicKey> rootTopicKeys) {
        for (TopicKey rootTopicKey : rootTopicKeys) {
            try {
                jmsSession.unsubscribe(getSubscriptionClientId(session, rootTopicKey));
            } catch (JMSException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

}

/**
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.richfaces.demo.push;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

/**
 * Sends message to JMS topic.
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
public class JMSMessageProducer implements MessageProducer {

    public static final String PUSH_JMS_TOPIC = "pushJms";

    private static final Logger LOGGER = Logger.getLogger(JMSMessageProducer.class.getName());

    private Topic topic;
    private TopicConnection connection = null;
    private TopicSession session = null;
    private TopicPublisher publisher = null;

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.demo.push.MessageProducer#sendMessage()
     */
    public void sendMessage() throws Exception {
        try {
            initializeMessaging();
            ObjectMessage message = session.createObjectMessage(createMessage());
            publisher.publish(message);
        } catch (NameNotFoundException e) {
            LOGGER.fine(e.getMessage());
        } catch (JMSException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private Serializable createMessage() {
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String dateMessage = dateFormat.format(new Date());
        return dateMessage;
    }

    private void initializeMessaging() throws JMSException, NamingException {
        if (connection == null) {
            TopicConnectionFactory tcf = getTopicConnectionFactory();
            connection = tcf.createTopicConnection();
        }
        if (session == null) {
            session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        }
        if (topic == null) {
            topic = InitialContext.doLookup("topic/" + PUSH_JMS_TOPIC);
        }
        if (publisher == null) {
            publisher = session.createPublisher(topic);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.demo.push.MessageProducer#getInterval()
     */
    public int getInterval() {
        return 5000;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.demo.push.MessageProducer#finalizeProducer()
     */
    public void finalizeProducer() {
        if (publisher != null) {
            try {
                publisher.close();
            } catch (JMSException e) {
                LOGGER.severe("unable to close publisher");
            }
        }
        if (session != null) {
            try {
                session.close();
            } catch (JMSException e) {
                LOGGER.severe("unable to close session");
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException e) {
                LOGGER.severe("unable to close connection");
            }
        }
    }

    private TopicConnectionFactory getTopicConnectionFactory() {
        try {
            return (TopicConnectionFactory) InitialContext.doLookup("java:/ConnectionFactory");
        } catch (NamingException e) {
            try {
                return (TopicConnectionFactory) InitialContext.doLookup("ConnectionFactory");
            } catch (NamingException e2) {
                throw new IllegalStateException("Can't find registered ConnectionFactory");
            }
        }
    }
}

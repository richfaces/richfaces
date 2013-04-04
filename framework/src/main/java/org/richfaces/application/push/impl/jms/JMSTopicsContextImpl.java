/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

import static org.richfaces.application.CoreConfiguration.Items.pushJMSConnectionFactory;
import static org.richfaces.application.CoreConfiguration.Items.pushJMSConnectionPassword;
import static org.richfaces.application.CoreConfiguration.Items.pushJMSConnectionPasswordEnvRef;
import static org.richfaces.application.CoreConfiguration.Items.pushJMSConnectionUsername;
import static org.richfaces.application.CoreConfiguration.Items.pushJMSConnectionUsernameEnvRef;
import static org.richfaces.application.CoreConfiguration.Items.pushJMSTopicsNamespace;
import static org.richfaces.application.CoreConfiguration.PushPropertiesItems.pushPropertiesJMSConnectionFactory;
import static org.richfaces.application.CoreConfiguration.PushPropertiesItems.pushPropertiesJMSConnectionPassword;
import static org.richfaces.application.CoreConfiguration.PushPropertiesItems.pushPropertiesJMSConnectionUsername;
import static org.richfaces.application.CoreConfiguration.PushPropertiesItems.pushPropertiesJMSTopicsNamespace;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadFactory;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;

import org.richfaces.application.ServiceTracker;
import org.richfaces.application.configuration.ConfigurationService;
import org.richfaces.application.push.TopicKey;
import org.richfaces.application.push.impl.TopicsContextImpl;
import org.richfaces.javascript.JSLiteral;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * @author Nick Belaevski
 *
 */
public class JMSTopicsContextImpl extends TopicsContextImpl {
    private static final Logger LOGGER = RichfacesLogger.APPLICATION.getLogger();

    private class JMSTopicContext {
        /**
         *
         */
        private static final String SUBTOPIC_PROPERTY = "rf_push_subtopic";
        private static final String SERIALIZED_DATA_INDICATOR = "org_richfaces_push_SerializedData";
        private final String name;
        private Connection connection;
        private Session session;
        private Thread pollingThread;
        private MessageConsumer consumer;

        public JMSTopicContext(String name) {
            super();

            this.name = name;
        }

        private Topic lookupTopic() throws NamingException {
            Name topicName = appendToName(topicsNamespace, name);

            return (Topic) initialContext.lookup(topicName);
        }

        private Connection createConnection() throws JMSException, NamingException {
            ConnectionFactory connectionFactory = (ConnectionFactory) initialContext.lookup(connectionFactoryName);
            Connection connection = connectionFactory.createConnection(username, password);
            connection.start();
            return connection;
        }

        private Object getMessageData(Message message) throws JMSException {
            Object messageData = null;

            if (message instanceof ObjectMessage) {
                messageData = ((ObjectMessage) message).getObject();
            } else if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;

                if (message.getBooleanProperty(SERIALIZED_DATA_INDICATOR)) {
                    messageData = new JSLiteral(textMessage.getText());
                } else {
                    messageData = textMessage.getText();
                }
            }

            return messageData;
        }

        public synchronized void start() throws NamingException, JMSException {
            connection = createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            consumer = session.createConsumer(lookupTopic(), null, false);

            pollingThread = getThreadFactory().newThread(new Runnable() {
                public void run() {
                    try {
                        while (true) {
                            Message message = consumer.receive();

                            if (message != null) {
                                String subtopicName = message.getStringProperty(SUBTOPIC_PROPERTY);
                                TopicKey topicKey = new TopicKey(name, subtopicName);

                                org.richfaces.application.push.Topic pushTopic = getOrCreateTopic(topicKey);
                                if (pushTopic != null) {
                                    try {
                                        Object messageData = getMessageData(message);
                                        pushTopic.publish(topicKey, messageData);
                                    } catch (Exception e) {
                                        LOGGER.error(e.getMessage(), e);
                                    }
                                }
                            } else {
                                break;
                            }
                        }
                    } catch (JMSException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                }
            });

            pollingThread.start();
        }

        public synchronized void stop() {
            if (consumer != null) {
                try {
                    consumer.close();
                    consumer = null;
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }

            if (session != null) {
                try {
                    session.close();
                    session = null;
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                    connection = null;
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }

    private final InitialContext initialContext;
    private final Name connectionFactoryName;
    private final Name topicsNamespace;
    private final String username;
    private final String password;

    private final LoadingCache<String, JMSTopicContext> contextsCache = CacheBuilder.newBuilder().build(
            CacheLoader.from(new Function<String, JMSTopicContext>() {
                public JMSTopicContext apply(String name) {
                    JMSTopicContext topicContext = new JMSTopicContext(name);
                    try {
                        topicContext.start();
                    } catch (Exception e) {
                        try {
                            topicContext.stop();
                        } catch (Exception e1) {
                            LOGGER.error(e1.getMessage(), e1);
                        }

                        throw new FacesException(e.getMessage(), e);
                    }
                    return topicContext;
                }
            }));

    public JMSTopicsContextImpl(ThreadFactory threadFactory, InitialContext initialContext, Name connectionFactoryName,
            Name topicsNamespace, String username, String password) {
        super(threadFactory);
        this.initialContext = initialContext;
        this.connectionFactoryName = connectionFactoryName;
        this.topicsNamespace = topicsNamespace;
        this.username = username;
        this.password = password;
    }

    public static JMSTopicsContextImpl getInstanceInitializedFromContext(ThreadFactory threadFactory, FacesContext facesContext)
            throws NamingException {
        ConfigurationService configurationService = ServiceTracker.getService(ConfigurationService.class);

        InitialContext initialContext = new InitialContext();

        NameParser nameParser = initialContext.getNameParser("");

        Name connectionFactoryName = nameParser.parse(getConnectionFactory(facesContext, configurationService));
        Name topicsNamespace = nameParser.parse(getTopicsNamespace(facesContext, configurationService));

        String username = getUserName(facesContext, configurationService);
        String password = getPassword(facesContext, configurationService);

        return new JMSTopicsContextImpl(threadFactory, initialContext, connectionFactoryName, topicsNamespace, username,
                password);
    }

    private static String getConnectionFactory(FacesContext facesContext, ConfigurationService configurationService) {
        return getFirstNonEmptyConfgirutationValue(facesContext, configurationService, pushPropertiesJMSConnectionFactory,
                pushJMSConnectionFactory);
    }

    private static String getTopicsNamespace(FacesContext facesContext, ConfigurationService configurationService) {
        return getFirstNonEmptyConfgirutationValue(facesContext, configurationService, pushPropertiesJMSTopicsNamespace,
                pushJMSTopicsNamespace);
    }

    private static String getPassword(FacesContext facesContext, ConfigurationService configurationService) {
        return getFirstNonEmptyConfgirutationValue(facesContext, configurationService, pushPropertiesJMSConnectionPassword,
                pushJMSConnectionPasswordEnvRef, pushJMSConnectionPassword);
    }

    private static String getUserName(FacesContext facesContext, ConfigurationService configurationService) {
        return getFirstNonEmptyConfgirutationValue(facesContext, configurationService, pushPropertiesJMSConnectionUsername,
                pushJMSConnectionUsernameEnvRef, pushJMSConnectionUsername);
    }

    private static String getFirstNonEmptyConfgirutationValue(FacesContext facesContext, ConfigurationService service,
            Enum<?>... keys) {
        for (Enum<?> key : keys) {
            String value = service.getStringValue(facesContext, key);
            if (!Strings.isNullOrEmpty(value)) {
                return value;
            }
        }

        return "";
    }

    private Name appendToName(Name name, String comp) throws NamingException {
        Name clonedName = (Name) name.clone();
        return clonedName.add(comp);
    }

    @Override
    protected org.richfaces.application.push.Topic createTopic(TopicKey key) {
        org.richfaces.application.push.Topic topic = super.createTopic(key);
        try {
            contextsCache.get(key.getTopicName());
        } catch (ExecutionException e) {
            throw new FacesException(String.format("Can't create a JMS topic %s", key), e);
        }
        return topic;
    }

    @Override
    public void destroy() {
        for (JMSTopicContext topicContext : contextsCache.asMap().values()) {
            try {
                topicContext.stop();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        super.destroy();
    }
}

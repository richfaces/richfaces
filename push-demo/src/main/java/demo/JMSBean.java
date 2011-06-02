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
package demo;

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

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;

import org.richfaces.application.ServiceTracker;
import org.richfaces.application.configuration.ConfigurationService;
import org.richfaces.application.push.TopicKey;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

import com.google.common.base.Strings;

/**
 * @author Nick Belaevski
 *
 */
@ManagedBean(name = "jmsBean")
@ApplicationScoped
public class JMSBean {
    private static final Logger LOGGER = RichfacesLogger.APPLICATION.getLogger();
    private static final PublishTask SHUTDOWN_TASK = new PublishTask(null, null);
    private Connection connection;

    private final class PublishRunnable implements Runnable {
        private final String topicsNamespaceString;

        private PublishRunnable(String topicsNamespaceString) {
            this.topicsNamespaceString = topicsNamespaceString;
        }

        public void run() {
            Session session = null;
            try {
                InitialContext initialContext = new InitialContext();
                NameParser nameParser = initialContext.getNameParser("");

                session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                while (true) {
                    PublishTask task = tasks.take();

                    if (task == SHUTDOWN_TASK) {
                        break;
                    }

                    Name topicsNamespace = nameParser.parse(topicsNamespaceString);
                    Name topicName = appendToName(topicsNamespace, task.getTopicKey().getTopicName());
                    Topic topic = (Topic) initialContext.lookup(topicName);

                    MessageProducer producer = null;
                    try {
                        producer = session.createProducer(topic);
                        ObjectMessage objectMessage = session.createObjectMessage(task.getMessage());
                        objectMessage.setStringProperty("rf_push_subtopic", task.getTopicKey().getSubtopicName());

                        producer.send(objectMessage);
                    } finally {
                        if (producer != null) {
                            try {
                                producer.close();
                            } catch (Exception e) {
                                LOGGER.error(e.getMessage(), e);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            } finally {
                if (session != null) {
                    try {
                        session.close();
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                }
            }
        }
    }

    private static final class PublishTask {
        private TopicKey topicKey;
        private Serializable message;

        public PublishTask(TopicKey topicKey, Serializable message) {
            super();
            this.topicKey = topicKey;
            this.message = message;
        }

        public TopicKey getTopicKey() {
            return topicKey;
        }

        public Serializable getMessage() {
            return message;
        }
    }

    private Thread workerThread;
    private BlockingQueue<PublishTask> tasks = new LinkedBlockingQueue<PublishTask>();

    private static String getConnectionFactory(FacesContext facesContext, ConfigurationService configurationService) {
        return getFirstNonEmptyConfgirutationValue(facesContext, configurationService, pushPropertiesJMSConnectionFactory,
                pushJMSConnectionFactory);
    }

    private static String getTopicsNamespace(FacesContext facesContext, ConfigurationService configurationService) {
        return getFirstNonEmptyConfgirutationValue(facesContext, configurationService, pushPropertiesJMSTopicsNamespace,
                pushJMSTopicsNamespace);
    }

    private static String getJMSPassword(FacesContext facesContext, ConfigurationService configurationService) {
        return getFirstNonEmptyConfgirutationValue(facesContext, configurationService, pushPropertiesJMSConnectionPassword,
                pushJMSConnectionPasswordEnvRef, pushJMSConnectionPassword);
    }

    private static String getJMSUserName(FacesContext facesContext, ConfigurationService configurationService) {
        return getFirstNonEmptyConfgirutationValue(facesContext, configurationService, pushPropertiesJMSConnectionUsername,
                pushJMSConnectionUsernameEnvRef, pushJMSConnectionUsername);
    }

    private static Name appendToName(Name name, String comp) throws NamingException {
        Name clonedName = (Name) name.clone();
        return clonedName.add(comp);
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

    private Connection createConnection() throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ConfigurationService configurationService = ServiceTracker.getService(ConfigurationService.class);

        InitialContext initialContext = new InitialContext();
        NameParser nameParser = initialContext.getNameParser("");

        Name cnfName = nameParser.parse(getConnectionFactory(facesContext, configurationService));

        ConnectionFactory connectionFactory = (ConnectionFactory) initialContext.lookup(cnfName);
        return connectionFactory.createConnection(getJMSUserName(facesContext, configurationService),
                getJMSPassword(facesContext, configurationService));
    }

    public void publish(TopicKey topicKey, Serializable message) {
        tasks.add(new PublishTask(topicKey, message));
    }

    @PostConstruct
    public void initialize() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ConfigurationService configurationService = ServiceTracker.getService(ConfigurationService.class);
            final String topicsNamespaceString = getTopicsNamespace(facesContext, configurationService);
            connection = createConnection();
            connection.start();

            this.workerThread = new Thread(new PublishRunnable(topicsNamespaceString));

            this.workerThread.setDaemon(true);
            this.workerThread.start();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @PreDestroy
    public void destroy() {
        tasks.add(SHUTDOWN_TASK);
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
}

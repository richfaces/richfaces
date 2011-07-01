/*
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

import static org.richfaces.demo.push.JMSMessageProducer.PUSH_JMS_TOPIC;
import static org.richfaces.demo.push.PushEventObserver.PUSH_CDI_TOPIC;
import static org.richfaces.demo.push.TopicsContextMessageProducer.PUSH_TOPICS_CONTEXT_TOPIC;

import java.io.Closeable;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.MBeanServer;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.jms.management.JMSServerControl;
import org.hornetq.core.config.Configuration;
import org.hornetq.core.config.impl.ConfigurationImpl;
import org.hornetq.core.remoting.impl.netty.NettyAcceptorFactory;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.core.server.HornetQServer;
import org.hornetq.core.server.HornetQServers;
import org.hornetq.jms.management.impl.JMSServerControlImpl;
import org.hornetq.jms.server.JMSServerManager;
import org.hornetq.jms.server.config.ConnectionFactoryConfiguration;
import org.hornetq.jms.server.config.impl.ConnectionFactoryConfigurationImpl;
import org.hornetq.jms.server.impl.JMSServerManagerImpl;
import org.jboss.as.controller.client.MessageSeverity;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.OperationMessageHandler;
import org.jboss.dmr.ModelNode;
import org.jboss.threads.AsyncFuture;

/**
 * Initializes JMS server and creates requested topics.
 *
 * @author Nick Belaevski
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
public class JMSInitializer extends AbstractInitializer {

    private static final Logger LOGGER = Logger.getLogger(JMSInitializer.class.getName());

    ModelControllerClient client;
    private HornetQServer jmsServer;
    private JMSServerManager jmsServerManager;
    private JMSServerControl jmsServerControl;
    private MBeanServer mBeanServer;

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.demo.push.Initializer#initialize()
     */
    public void initialize() throws Exception {
        if (isJMSInitialized()) {
            kickOffJMSConfiguration();
        } else {
            startJMSServer();
            startJMSServerManager();
            createJMSConnectionFactory();
            createJMSServerControlViaServerManager();
        }

        createTopic(PUSH_JMS_TOPIC, "/topic/" + PUSH_JMS_TOPIC);
        createTopic(PUSH_TOPICS_CONTEXT_TOPIC, "/topic/" + PUSH_TOPICS_CONTEXT_TOPIC);
        createTopic(PUSH_CDI_TOPIC, "/topic/" + PUSH_CDI_TOPIC);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.demo.push.Initializer#unload()
     */
    public void unload() throws Exception {
        unloadTopics();
        stopJMSServerManager();
        stopJMSServer();
    }

    private void kickOffJMSConfiguration() throws Exception {
        client = ModelControllerClient.Factory.create(InetAddress.getLocalHost(), 9999);

        // ModelNode operation = new ModelNode();
        // operation.get("operation").set("read-resource");
        // operation.get("address").setEmptyList();
        // ModelNode result = client.execute(operation);
        //
        //
        // if (!"success".equals(result.get("outcome").asString())) {
        // throw new IllegalStateException("Adding topic unsuccessfull");
        // }

    }

    private void startJMSServer() throws Exception {
        jmsServer = HornetQServers.newHornetQServer(createHornetQConfiguration());
    }

    private void startJMSServerManager() throws Exception {
        jmsServerManager = new JMSServerManagerImpl(jmsServer);

        InitialContext context = new InitialContext();
        jmsServerManager.setContext(context);
        jmsServerManager.start();
    }

    private void createJMSConnectionFactory() throws Exception {
        List<String> connectors = Arrays.asList(new String[] { "netty" });

        ConnectionFactoryConfiguration connectionFactoryConfiguration = new ConnectionFactoryConfigurationImpl(
            "ConnectionFactory", false, connectors, (String) null);
        connectionFactoryConfiguration.setUseGlobalPools(false);

        jmsServerManager.createConnectionFactory(false, connectionFactoryConfiguration, "ConnectionFactory");
    }

    private void createJMSServerControlViaServerManager() throws Exception {
        jmsServerControl = new JMSServerControlImpl(jmsServerManager);
    }

    private Set<String> deployedTopics = new HashSet<String>();

    private void createTopic(String topicName, String jndiName) throws Exception {
        jndiName = jndiName.replaceFirst("/", "");

        ModelNode operation = new ModelNode();
        operation.get("operation").set("read-resource");
        operation.get("address").add("subsystem", "messaging");
        ModelNode result = client.execute(operation, emptyOperationMessageHandler);

        if (!result.get("result").get("jms-topic").toString().contains(topicName)) {
            operation = new ModelNode();
            operation.get("operation").set("add");
            operation.get("address").add("subsystem", "messaging");
            operation.get("address").add("jms-topic", topicName);
            operation.get("entries").add("topic/" + topicName);
            AsyncFuture<ModelNode> executeAsync = client.executeAsync(operation, emptyOperationMessageHandler);
            executeAsync.addListener(new TopicStartupListener(), topicName);
        }
    }

    private static class TopicStartupListener implements AsyncFuture.Listener<ModelNode, String> {
        public void handleComplete(AsyncFuture<? extends ModelNode> future, String attachment) {
            LOGGER.severe("handleComplete");
        }

        public void handleFailed(AsyncFuture<? extends ModelNode> future, Throwable cause, String attachment) {
            LOGGER.severe("handleFailed");
        }

        public void handleCancelled(AsyncFuture<? extends ModelNode> future, String attachment) {
            LOGGER.severe("handleCancelled");
        }
    }

    private void isTopicDeployed(String topicName) throws Exception {

    }

    private ModelNode createOperationOnTopic(String operationName, String topicName) {
        final ModelNode operation = new ModelNode();
        operation.get("operation").set(operationName);
        operation.get("address").add("subsystem", "messaging");
        operation.get("address").add("jms-topic", topicName);
        return operation;
    }

    private Configuration createHornetQConfiguration() {
        Configuration configuration = new ConfigurationImpl();
        configuration.setPersistenceEnabled(false);
        configuration.setSecurityEnabled(false);

        TransportConfiguration transportationConfiguration = new TransportConfiguration(
            NettyAcceptorFactory.class.getName());
        HashSet<TransportConfiguration> setTransp = new HashSet<TransportConfiguration>();
        setTransp.add(transportationConfiguration);
        configuration.setAcceptorConfigurations(setTransp);
        configuration.getConnectorConfigurations().put("netty",
            new TransportConfiguration(NettyConnectorFactory.class.getName()));

        return configuration;
    }

    public static void safeClose(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable t) {
                LOGGER.log(Level.SEVERE, "Failed to close resource %s", closeable);
            }
        }
    }

    private void unloadTopics() throws Exception {
        for (String topicName : deployedTopics) {
            unloadTopic(topicName);
        }
    }

    private OperationMessageHandler emptyOperationMessageHandler = new OperationMessageHandler() {

        public void handleReport(MessageSeverity severity, String message) {
            LOGGER.log(Level.SEVERE, message);
        }
    };

    private void unloadTopic(String topicName) {
        LOGGER.severe("unloading topic " + topicName);
        final ModelNode operation = new ModelNode();
        operation.get("operation").set("remove");
        operation.get("address").add("subsystem", "messaging");
        operation.get("address").add("jms-topic", topicName);
        client.executeAsync(operation, emptyOperationMessageHandler);
    }

    /**
     * Returns true if JMS server is already running.
     *
     * @return true if JMS server is already running.
     */
    private boolean isJMSInitialized() {
        try {
            return null != InitialContext.doLookup("java:/ConnectionFactory");
        } catch (NamingException e) {
            if (!(e instanceof NameNotFoundException)) {
                LOGGER.log(Level.SEVERE, "Can't access naming context", e);
            }
            return false;
        }
    }

    private void stopJMSServer() throws Exception {
        jmsServer.stop();
        jmsServer = null;
    }

    private void stopJMSServerManager() throws Exception {
        jmsServerManager.stop();
        jmsServerManager = null;
    }
}
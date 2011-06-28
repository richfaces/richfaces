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
import static org.richfaces.demo.push.TopicsContextMessageProducer.PUSH_TOPICS_CONTEXT_TOPIC;
import static org.richfaces.demo.push.PushEventObserver.PUSH_CDI_TOPIC;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.core.config.Configuration;
import org.hornetq.core.config.impl.ConfigurationImpl;
import org.hornetq.core.remoting.impl.netty.NettyAcceptorFactory;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.core.server.HornetQServer;
import org.hornetq.core.server.HornetQServers;
import org.hornetq.jms.server.JMSServerManager;
import org.hornetq.jms.server.config.ConnectionFactoryConfiguration;
import org.hornetq.jms.server.config.impl.ConnectionFactoryConfigurationImpl;
import org.hornetq.jms.server.impl.JMSServerManagerImpl;
import org.richfaces.application.ServiceTracker;
import org.richfaces.application.push.PushContextFactory;
import org.richfaces.demo.arrangeablemodel.PersistenceService;

/**
 * Initializes JMS server and creates requested topics.
 *
 * @author Nick Belaevski
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
public class JMSInitializer extends AbstractInitializer {

    private static final Logger LOGGER = Logger.getLogger(PersistenceService.class.getName());

    private HornetQServer jmsServer;
    private JMSServerManager jmsServerManager;

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.demo.push.Initializer#initialize()
     */
    public void initialize() throws Exception {
        if (!isJMSInitialized()) {
            startJMSServer();
            startJMSServerManager();
            createConnectionFactory();
            createTopic(PUSH_JMS_TOPIC, "/topic/" + PUSH_JMS_TOPIC);
            createTopic(PUSH_TOPICS_CONTEXT_TOPIC, "/topic/" + PUSH_TOPICS_CONTEXT_TOPIC);
            createTopic(PUSH_CDI_TOPIC, "/topic/" + PUSH_CDI_TOPIC);
        }

        ServiceTracker.getService(PushContextFactory.class).getPushContext();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.demo.push.Initializer#unload()
     */
    public void unload() throws Exception {
        stopJMSServerManager();
        stopJMSServer();
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

    private void createConnectionFactory() throws Exception {
        ConnectionFactoryConfiguration connectionFactoryConfiguration = new ConnectionFactoryConfigurationImpl(
            "ConnectionFactory", new TransportConfiguration(NettyConnectorFactory.class.getName()), (String) null);
        connectionFactoryConfiguration.setUseGlobalPools(false);

        jmsServerManager.createConnectionFactory(false, connectionFactoryConfiguration, "ConnectionFactory");
    }

    private void createTopic(String topicName, String... jndiNames) throws Exception {
        jmsServerManager.createTopic(false, topicName, jndiNames);
    }

    private Configuration createHornetQConfiguration() {
        Configuration configuration = new ConfigurationImpl();
        configuration.setPersistenceEnabled(false);
        configuration.setSecurityEnabled(false);

        TransportConfiguration transportationConfiguration = new TransportConfiguration(NettyAcceptorFactory.class.getName());
        HashSet<TransportConfiguration> setTransp = new HashSet<TransportConfiguration>();
        setTransp.add(transportationConfiguration);
        configuration.setAcceptorConfigurations(setTransp);

        return configuration;
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
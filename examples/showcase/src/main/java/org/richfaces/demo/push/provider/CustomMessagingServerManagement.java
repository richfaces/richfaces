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
package org.richfaces.demo.push.provider;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;

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

/**
 * Starts HornetQ, binds ConnectionFactory to the context and create topics.
 *
 * @author Nick Belaevski
 * @author <a href="http://community.jboss.org/people/lfryc">Lukas Fryc</a>
 */
public class CustomMessagingServerManagement implements MessagingProviderManagement {

    private static final Logger LOGGER = Logger.getLogger(CustomMessagingServerManagement.class.getName());

    private HornetQServer jmsServer;
    private JMSServerManager jmsServerManager;

    public void initializeProvider() throws InitializationFailedException {
        try {
            startJMSServer();
            startJMSServerManager();
            createJMSConnectionFactory();
        } catch (Exception e) {
            throw new InitializationFailedException();
        }
    }

    public void createTopic(String topicName, String jndiName) throws Exception {
        jmsServerManager.createTopic(false, topicName, jndiName);
    }

    public void finalizeProvider() {
        try {
            stopJMSServerManager();
            stopJMSServer();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "wasn't able to finalize custom messaging");
        }
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

    private void stopJMSServer() throws Exception {
        jmsServer.stop();
        jmsServer = null;
    }

    private void stopJMSServerManager() throws Exception {
        jmsServerManager.stop();
        jmsServerManager = null;
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

}
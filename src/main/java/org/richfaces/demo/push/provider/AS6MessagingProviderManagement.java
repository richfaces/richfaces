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

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.hornetq.api.core.management.ObjectNameBuilder;
import org.hornetq.api.jms.management.JMSServerControl;

/**
 * Connects to JMS RMI interface and creates topics using {@link JMSServerControl}.
 *
 * @author <a href="http://community.jboss.org/people/lfryc">Lukas Fryc</a>
 */
public class AS6MessagingProviderManagement implements MessagingProviderManagement {

    private JMSServerControl serverControl;

    public void initializeProvider() throws InitializationFailedException {
        //determine bound IP address
        String ipAddr = System.getProperty("jboss.bind.address");

        if(ipAddr.isEmpty()){
           ipAddr = "localhost";
        }

        try {
            ObjectName on = ObjectNameBuilder.DEFAULT.getJMSServerObjectName();
            JMXConnector connector = JMXConnectorFactory.connect(new JMXServiceURL(
                "service:jmx:rmi:///jndi/rmi://" + ipAddr + ":1090/jmxrmi"), new HashMap<String, Object>());
            MBeanServerConnection mbsc = connector.getMBeanServerConnection();
            serverControl = (JMSServerControl) MBeanServerInvocationHandler.newProxyInstance(mbsc, on,
                JMSServerControl.class, false);
        } catch (Exception e) {
            throw new InitializationFailedException();
        }
    }

    public void finalizeProvider() {
    }

    public void createTopic(String topicName, String jndiName) throws Exception {
        if (!getAvailableTopics().contains(topicName)) {
            serverControl.createTopic(topicName, jndiName);
        }
    }

    private Set<String> getAvailableTopics() {
        String[] topicNames = serverControl.getTopicNames();
        return new HashSet<String>(Arrays.asList(topicNames));
    }
}

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

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;

/**
 * Manages AS7 to create JMS topics using ModelControllerClient.
 *
 * @author <a href="http://community.jboss.org/people/lfryc">Lukas Fryc</a>
 */
public class AS7MessagingProviderManagement implements MessagingProviderManagement {

    private static final Logger LOGGER = Logger.getLogger(AS7MessagingProviderManagement.class.getName());

    private List<ModelControllerClient> clients = new LinkedList<ModelControllerClient>();

    public void initializeProvider() throws InitializationFailedException {
        try {
            // tries to connect - when creating topic, own client will be created (RF-11695)
            createClient().close();
        } catch (Exception e) {
            throw new InitializationFailedException();
        } catch (NoClassDefFoundError e) {
            throw new InitializationFailedException();
        }
    }

    private ModelControllerClient createClient() throws UnknownHostException {
        return ModelControllerClient.Factory.create("127.0.0.1", 9999);
    }

    public void finalizeProvider() {
        try {
            for (ModelControllerClient client : clients) {
                client.close();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "wasn't able to finalize AS7 messaging management");
        }
    }

    public void createTopic(String topicName, String jndiName) throws Exception {

        // create own client for each topic creation (RF-11695)
        ModelControllerClient client = createClient();
        clients.add(client);

        boolean as71 = false;

        jndiName = jndiName.replaceFirst("/", "");

        ModelNode operation = new ModelNode();
        operation.get("operation").set("read-resource");
        operation.get("address").add("subsystem", "messaging");
        ModelNode result = client.execute(operation, null);

        // AS 7.1 or higher (hornetq-server=default address node added)
        if (result.get("result").toString().contains("hornetq-server")) {
            as71 = true;
            operation = new ModelNode();
            operation.get("operation").set("read-resource");
            operation.get("address").add("subsystem", "messaging");
            operation.get("address").add("hornetq-server", "default");
            result = client.execute(operation, null);
        }

        if (!result.get("result").get("jms-topic").toString().contains("\"" + topicName + "\"")) {
            operation = new ModelNode();
            operation.get("operation").set("add");
            operation.get("address").add("subsystem", "messaging");
            if (as71) {
                operation.get("address").add("hornetq-server", "default");
            }
            operation.get("address").add("jms-topic", topicName);
            operation.get("entries").add("topic/" + topicName);
            client.executeAsync(operation, null);
        }
    }
}

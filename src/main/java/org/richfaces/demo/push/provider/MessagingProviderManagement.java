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

/**
 * Interface for management of messaging provider.
 *
 * Is able to initialize, createTopic and finalize.
 *
 * @author <a href="http://community.jboss.org/people/lfryc">Lukas Fryc</a>
 */
public interface MessagingProviderManagement {
    /**
     * Initializes messaging management, called at application startup.
     *
     * @throws InitializationFailedException
     *             when initialize of provider fails
     */
    void initializeProvider() throws InitializationFailedException;

    /**
     * Creates JMS topic using this provider
     *
     * @param topicName
     *            the name of the topic
     * @param jndiName
     *            the JNDI binding to use for given topic
     * @throws Exception
     *             when creating of topic fails
     */
    void createTopic(String topicName, String jndiName) throws Exception;

    /**
     * Finalizes messaging provider management at on application tear down.
     */
    void finalizeProvider();
}

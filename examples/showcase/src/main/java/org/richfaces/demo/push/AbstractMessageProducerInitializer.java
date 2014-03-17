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

/**
 * Initializer which manages start of associated MessageProducer.
 *
 * @author <a href="http://community.jboss.org/people/lfryc">Lukas Fryc</a>
 *
 */
public abstract class AbstractMessageProducerInitializer extends AbstractCapabilityInitializer {

    private Thread messageProducerThread;
    private MessageProducer messageProducer;
    private MessageProducerRunnable messageProducerRunnable;

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.demo.push.Initializer#initialize()
     */
    public void initializeCapability() throws Exception {
        messageProducer = createMessageProducer();
        messageProducerRunnable = new MessageProducerRunnable(messageProducer);
        messageProducerThread = new Thread(messageProducerRunnable, "MessageProducerThread");
        messageProducerThread.setDaemon(false);
        messageProducerThread.start();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.demo.push.Initializer#unload()
     */
    @Override
    public void finalizeCapability() throws Exception {
        if (messageProducer != null) {
            messageProducer.finalizeProducer();
        }
        if (messageProducerRunnable != null) {
            messageProducerRunnable.stop();
        }
        if (messageProducerThread != null) {
            messageProducerThread.interrupt();
        }
    }

    public abstract MessageProducer createMessageProducer();
}

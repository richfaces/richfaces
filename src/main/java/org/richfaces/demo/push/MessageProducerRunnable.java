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

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Runnable which are periodically sending messages until it is stopped or underlying thread is interrupted.
 *
 * @author <a href="http://community.jboss.org/people/lfryc">Lukas Fryc</a>
 */
public class MessageProducerRunnable implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(MessageProducerRunnable.class.getName());

    private AtomicBoolean runFlag = new AtomicBoolean(true);
    private MessageProducer messageProducer;

    /**
     * Creates runnable with associated message producer.
     *
     * @param messageProducer will be associated with this runnable
     */
    public MessageProducerRunnable(MessageProducer messageProducer) {
        this.messageProducer = messageProducer;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Runnable#run()
     */
    public void run() {
        while (runFlag.get()) {
            try {
                Thread.sleep(messageProducer.getInterval());
            } catch (InterruptedException e) {
                LOGGER.log(Level.INFO, "MessageProducer has been interrupted");
                break;
            }

            try {
                messageProducer.sendMessage();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }

    /**
     * Stops the cycle with body sending messages.
     */
    public void stop() {
        runFlag.set(false);
    }
}

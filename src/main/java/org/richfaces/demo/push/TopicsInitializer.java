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

import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.richfaces.application.push.Topic;
import org.richfaces.application.push.TopicKey;
import org.richfaces.application.push.TopicsContext;
import org.richfaces.application.push.impl.DefaultMessageDataSerializer;

/**
 * Registers topics in RichFaces subsytem.
 *
 * @author Nick Belaevski
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
public class TopicsInitializer extends AbstractInitializer {

    private static final Logger LOGGER = Logger.getLogger(TopicsInitializer.class.getName());

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.demo.push.Initializer#initialize()
     */
    public void initialize() throws Exception {
        TopicsContext.lookup();

        new RegisterTopicThread(PUSH_JMS_TOPIC).start();
        new RegisterTopicThread(PUSH_TOPICS_CONTEXT_TOPIC).start();
        new RegisterTopicThread(PUSH_CDI_TOPIC).start();
    }

    private class RegisterTopicThread extends Thread {

        public RegisterTopicThread(final String topicName) {
            super(new Runnable() {

                public void run() {
                    waitForJmsTopicReady();

                    TopicsContext topicsContext = TopicsContext.lookup();
                    Topic pushJmsTopic = topicsContext.getOrCreateTopic(new TopicKey(PUSH_JMS_TOPIC));
                    pushJmsTopic.setMessageDataSerializer(DefaultMessageDataSerializer.instance());
                }

                private void waitForJmsTopicReady() {
                    while (!isJmsTopicReady()) {
                        try {
                            LOGGER.severe("topic " + topicName + " not ready yet, sleeping");
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            throw new IllegalStateException(e);
                        }
                    }
                }

                private boolean isJmsTopicReady() {
                    try {
                        InitialContext.doLookup("topic/" + topicName);
                        return true;
                    } catch (NameNotFoundException e) {
                        return false;
                    } catch (NamingException e) {
                        LOGGER.severe(e.getMessage());
                    }
                    return false;
                }
            }, "RegisterTopicThread");
        }
    }
}

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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.richfaces.application.push.TopicKey;
import org.richfaces.application.push.TopicsContext;

/**
 * Observes CDI Events annotated by Push and pass event pay-loads to topic specific to current user identifier.
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
public class PushEventObserver {

    private static final Logger LOGGER = Logger.getLogger(PushEventObserver.class.getName());

    public static final String PUSH_CDI_TOPIC = "pushCdi";

    @Inject
    PushBean pushBean;

    public void onPushEvent(@Observes @Push Object message) {
        try {
            TopicKey topicKey = new TopicKey(PUSH_CDI_TOPIC, pushBean.getUserIdentifier());
            TopicsContext topicsContext = TopicsContext.lookup();
            topicsContext.publish(topicKey, message);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "push failed", e);
        }
    }
}

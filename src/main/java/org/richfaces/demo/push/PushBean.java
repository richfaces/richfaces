/**
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

import java.io.Serializable;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
@Named
@SessionScoped
public class PushBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userIdentifier;

    @Inject
    @Push
    Event<String> pushEvent;

    @PostConstruct
    public void initialize() {
        if (userIdentifier == null) {
            userIdentifier = getUUID().replace("-", "");
        }
    }

    /**
     * Sends message.
     *
     * @param message to send
     */
    public void sendMessage(String message) {
        pushEvent.fire(message);
    }

    /**
     * Returns current user identifier.
     *
     * @return current user identifier.
     */
    public String getUserIdentifier() {
        return userIdentifier;
    }

    /**
     * Generates unique ID as string.
     *
     * @return unique ID as string.
     */
    public String getUUID() {
        return UUID.randomUUID().toString();
    }
}

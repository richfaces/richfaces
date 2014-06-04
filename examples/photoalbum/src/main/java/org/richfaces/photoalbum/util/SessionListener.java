/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.richfaces.photoalbum.util;

/**
 * This class is session listener that observes <code>"org.jboss.seam.sessionExpired"</code> event to delete in production systems users when it's session is expired
 * to prevent flood. Used only on livedemo server. If you don't want this functionality simply delete this class from distributive.
 *
 * @author Andrey Markhel
 */
import java.io.Serializable;
import java.util.List;

import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.richfaces.photoalbum.manager.LoggedUserTracker;
import org.richfaces.photoalbum.model.Comment;
import org.richfaces.photoalbum.model.User;
import org.richfaces.photoalbum.model.actions.IImageAction;
import org.richfaces.photoalbum.model.event.EventType;
import org.richfaces.photoalbum.model.event.Events;
import org.richfaces.photoalbum.model.event.SimpleEvent;

@SessionScoped
public class SessionListener implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Inject
    private User user;

    @Inject
    private IImageAction imageAction;
    @Inject
    private EntityManager em;
    @Inject
    LoggedUserTracker userTracker;

    @Inject
    @EventType(Events.USER_DELETED_EVENT)
    Event<SimpleEvent> event;

    @PreDestroy
    public void onDestroy() {
        if (!Environment.isInProduction()) {
            return;
        }

        if (user.getId() != null && !user.isPreDefined() && !userTracker.containsUserId(user.getId())) {
            user = em.merge(user);
            final List<Comment> userComments = imageAction.findAllUserComments(user);
            for (Comment c : userComments) {
                em.remove(c);
            }
            em.remove(user);
            em.flush();

            event.fire(new SimpleEvent());
        }
    }

}

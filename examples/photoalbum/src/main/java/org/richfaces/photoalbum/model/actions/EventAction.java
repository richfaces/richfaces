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

package org.richfaces.photoalbum.model.actions;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.richfaces.photoalbum.model.Event;
import org.richfaces.photoalbum.model.EventCategory;
import org.richfaces.photoalbum.util.PhotoAlbumException;

@Stateless
public class EventAction implements IEventAction {

    @Inject
    private EntityManager em;

    /**
     * Persist event entity to database
     * 
     * @param event - event to add
     * @throws PhotoAlbumException
     */
    public void addEvent(Event event) throws PhotoAlbumException {
        try {
            em.persist(event);
            em.flush();
        } catch (Exception e) {
            throw new PhotoAlbumException(e.getMessage());
        }
    }

    /**
     * Remove event entity from database
     * 
     * @param event - event to delete
     * @throws PhotoAlbumException
     */
    public void deleteEvent(Event event) throws PhotoAlbumException {
        try {
            em.remove(em.merge(event));
            em.flush();
        } catch (Exception e) {
            throw new PhotoAlbumException(e.getMessage());
        }
    }

    /**
     * Synchronize state of event entity with database
     * 
     * @param event - event to Synchronize
     * @throws PhotoAlbumException
     */
    public void editEvent(Event event) throws PhotoAlbumException {
        try {
            em.merge(event);
            em.flush();
        } catch (Exception e) {
            throw new PhotoAlbumException(e.getMessage());
        }
    }

    /**
     * Refresh state of given event
     * 
     * @param event - event to Synchronize
     */
    public void resetEvent(Event event) {
        em.refresh(event);
    }

    @SuppressWarnings("unchecked")
    public List<Event> getAllEvents() {
        return em.createNamedQuery("all-events").getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<EventCategory> getEventCategories() {
        return em.createNamedQuery("event-categories").getResultList();
    }

    public Event getEventById(long id) {
        Event event = null;
        try {
            event = (Event) em.createNamedQuery("event-by-id").setParameter("id", id).getSingleResult();
        } catch (NoResultException nre) {
            // do nothing
        }
        return event;
    }

    public EventCategory getEventCategoryById(long id) {
        EventCategory ec = null;
        try {
            ec = (EventCategory) em.createNamedQuery("event-category").setParameter("id", id).getSingleResult();
        } catch (NoResultException nre) {
            // do nothing
        }
        return ec;
    }

    @Override
    public Event getEventByName(String name) {
        Event event = null;
        try {
            event = (Event) em.createNamedQuery("event-by-name").setParameter("name", name).getSingleResult();
        } catch (NoResultException nre) {
            // do nothing
        }
        return event;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Event> getEventsByCategory(EventCategory ec) {
        return em.createNamedQuery("events-by-category").setParameter("cat", ec).getResultList();
    }
}

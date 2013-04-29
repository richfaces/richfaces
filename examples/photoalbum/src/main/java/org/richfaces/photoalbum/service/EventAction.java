package org.richfaces.photoalbum.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.richfaces.photoalbum.domain.Event;
import org.richfaces.photoalbum.domain.EventCategory;

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

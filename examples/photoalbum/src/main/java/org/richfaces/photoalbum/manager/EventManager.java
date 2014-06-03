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

package org.richfaces.photoalbum.manager;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.inject.Named;

import org.richfaces.photoalbum.model.Event;
import org.richfaces.photoalbum.model.EventCategory;
import org.richfaces.photoalbum.model.Shelf;
import org.richfaces.photoalbum.model.User;
import org.richfaces.photoalbum.model.actions.IEventAction;
import org.richfaces.photoalbum.model.actions.IShelfAction;
import org.richfaces.photoalbum.model.event.ErrorEvent;
import org.richfaces.photoalbum.model.event.EventType;
import org.richfaces.photoalbum.model.event.EventTypeQualifier;
import org.richfaces.photoalbum.model.event.Events;
import org.richfaces.photoalbum.model.event.ShelfEvent;
import org.richfaces.photoalbum.util.Constants;
import org.richfaces.photoalbum.util.Preferred;

@Named
@RequestScoped
public class EventManager implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private boolean validationSuccess = false;

    @Inject
    IEventAction eventAction;

    @Inject
    IShelfAction shelfAction;

    @Inject
    @Preferred
    User user;

    @Inject
    @EventType(Events.ADD_ERROR_EVENT)
    javax.enterprise.event.Event<ErrorEvent> error;

    @Inject
    @Any
    javax.enterprise.event.Event<ShelfEvent> shelfEvent;

    private Event newEvent = new Event();
    private Shelf newShelf = new Shelf();

    private long ecId;

    public Event getEvent() {
        return newEvent;
    }

    public void setEvent(Event event) {
        newEvent = event;
    }

    public void createEvent() {
        if (user == null) {
            return;
        }
        newEvent = new Event();
        ecId = 1;

        newShelf = new Shelf();
    }

    public void addEvent() {
        if (user == null) {
            return;
        }
        validationSuccess = true;
        Logger logger = Logger.getLogger("EventManager");

        try {
            EventCategory ec = eventAction.getEventCategoryById(ecId);
            newEvent.setCategory(ec);

            newShelf.setName(newEvent.getName());
            newShelf.setShared(true);

            newShelf.setEvent(newEvent);
            newEvent.setShelf(newShelf);

            eventAction.addEvent(newEvent);
            shelfAction.addShelf(newShelf);
        } catch (Exception e) {
            error.fire(new ErrorEvent("Error", Constants.EVENT_SAVING_ERROR + " <br /> " + e.getMessage()));
            logger.log(Level.SEVERE, "exception occured", e);
            return;
        }
        shelfEvent.select(new EventTypeQualifier(Events.EVENT_ADDED_EVENT)).fire(new ShelfEvent(newEvent));
    }

    /**
     * Method, that invoked when user click 'Edit event' button or by inplaceInput component. Only registered users can edit
     * shelves.
     * 
     * @param event - event to edit
     * @param editFromInplace - indicate whether edit process was initiated by inplaceInput component
     * 
     */
    public void editEvent(Event event, boolean editFromInplace) {
        if (user == null) {
            return;
        }
        try {
            eventAction.editEvent(event);
        } catch (Exception e) {
            error.fire(new ErrorEvent("Error", Constants.EVENT_SAVING_ERROR + " <br /> " + e.getMessage()));
            eventAction.resetEvent(event);
            return;
        }
        shelfEvent.select(new EventTypeQualifier(Events.EVENT_EDITED_EVENT)).fire(new ShelfEvent(event));
    }

    /**
     * Method, that invoked when user click 'Delete event' button. Only registered users can delete shelves.
     * 
     * @param image - event to delete
     * 
     */
    public void deleteEvent(Event event) {
        if (user == null) {
            return;
        }
        try {
            eventAction.deleteEvent(event);
        } catch (Exception e) {
            error.fire(new ErrorEvent("Error", Constants.EVENT_DELETING_ERROR + " <br /> " + e.getMessage()));
            return;
        }
        shelfEvent.select(new EventTypeQualifier(Events.EVENT_DELETED_EVENT)).fire(new ShelfEvent(event));
    }

    public boolean isValidationSuccess() {
        return validationSuccess;
    }

    public void setValidationSuccess(boolean validationSuccess) {
        this.validationSuccess = validationSuccess;
    }

    public List<Event> getAllEvents() {
        return eventAction.getAllEvents();
    }

    public List<EventCategory> getEventCategories() {
        return eventAction.getEventCategories();
    }

    public Event getEventById(long id) {
        return eventAction.getEventById(id);
    }

    public EventCategory getEventCategoryById(long id) {
        return eventAction.getEventCategoryById(id);
    }

    public List<Event> getEventsByCategory(EventCategory ec) {
        return eventAction.getEventsByCategory(ec);
    }

    public long getEcId() {
        return ecId;
    }

    public void setEcId(long ecId) {
        this.ecId = ecId;
    }
}

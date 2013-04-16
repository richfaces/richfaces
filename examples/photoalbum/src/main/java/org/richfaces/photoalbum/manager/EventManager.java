package org.richfaces.photoalbum.manager;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.solder.logging.Logger;
import org.richfaces.photoalbum.domain.Event;
import org.richfaces.photoalbum.domain.EventCategory;
import org.richfaces.photoalbum.domain.User;
import org.richfaces.photoalbum.event.EventType;
import org.richfaces.photoalbum.event.EventTypeQualifier;
import org.richfaces.photoalbum.event.Events;
import org.richfaces.photoalbum.event.ShelfEvent;
import org.richfaces.photoalbum.event.SimpleEvent;
import org.richfaces.photoalbum.service.Constants;
import org.richfaces.photoalbum.service.IEventAction;
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
    @Preferred
    User user;

    @Inject
    @EventType(Events.ADD_ERROR_EVENT)
    javax.enterprise.event.Event<SimpleEvent> error;

    @Inject
    @Any
    javax.enterprise.event.Event<ShelfEvent> shelfEvent;

    private Event newEvent = new Event();
    
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
    }

    public void addEvent() {
        if (user == null) {
            return;
        }
        validationSuccess = true;
        Logger logger = Logger.getLogger(EventManager.class);
        
        try {
            EventCategory ec = eventAction.getEventCategoryById(ecId);
            newEvent.setCategory(ec);
            eventAction.addEvent(newEvent);
        } catch (Exception e) {
            error.fire(new SimpleEvent(Constants.EVENT_SAVING_ERROR + " <br /> " + e.getMessage()));
            logger.error("exception occured", e);
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
            error.fire(new SimpleEvent(Constants.EVENT_SAVING_ERROR));
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
            error.fire(new SimpleEvent(Constants.EVENT_DELETING_ERROR));
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

    public long getEcId() {
        return ecId;
    }

    public void setEcId(long ecId) {
        this.ecId = ecId;
    }
}

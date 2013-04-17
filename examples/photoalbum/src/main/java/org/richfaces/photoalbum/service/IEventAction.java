package org.richfaces.photoalbum.service;

import java.util.List;

import javax.ejb.Local;

import org.richfaces.photoalbum.domain.Event;
import org.richfaces.photoalbum.domain.EventCategory;

@Local
public interface IEventAction {

    void addEvent(Event event) throws PhotoAlbumException;

    void deleteEvent(Event event) throws PhotoAlbumException;

    void editEvent(Event event) throws PhotoAlbumException;

    void resetEvent(Event event);
    
    List<Event> getAllEvents();
    
    List<EventCategory> getEventCategories();
    
    Event getEventById(long id);
    
    Event getEventByName(String name);
    
    EventCategory getEventCategoryById(long id);
    
    List<Event> getEventsByCategory(EventCategory ec);
    
    
}
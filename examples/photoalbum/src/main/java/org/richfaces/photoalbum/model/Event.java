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

package org.richfaces.photoalbum.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.richfaces.photoalbum.util.converters.ListConverter;

//import org.jboss.errai.common.client.api.annotations.Portable;

/**
 * <p>
 * Represents an event, which may have multiple performances with different dates and venues.
 * </p>
 *
 * <p>
 * Event's principle members are it's relationship to {@link EventCategory} - specifying the type of event it is - and
 * {@link MediaItem} - providing the ability to add media (such as a picture) to the event for display. It also contains
 * meta-data about the event, such as it's name and a description.
 * </p>
 *
 * @author Shane Bryzak
 * @author Marius Bogoevici
 * @author Pete Muir
 */
/*
 * We suppress the warning about not specifying a serialVersionUID, as we are still developing this app, and want the JVM to
 * generate the serialVersionUID for us. When we put this app into production, we'll generate and embed the serialVersionUID
 */
@SuppressWarnings("serial")
@NamedQueries({
        @NamedQuery(name = "event-categories", query = "select ec from EventCategory ec"),
        @NamedQuery(name = "event-category", query = "select ec from EventCategory ec where ec.id = :id"),
        @NamedQuery(name = "event-by-id", query = "select e from Event e where e.id = :id"),
        @NamedQuery(name = "all-events", query = "select e from Event e"),
        @NamedQuery(name = "event-by-name", query = "select e from Event e where e.name = :name"),
        @NamedQuery(name = "events-by-category", query = "select e from Event e where e.category = :cat")})
@Entity
// @Portable
public class Event implements Serializable {

    /* Declaration of fields */

    /**
     * The synthetic ID of the object.
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    /**
     * <p>
     * The name of the event.
     * </p>
     *
     * <p>
     * The name of the event forms it's natural identity and cannot be shared between events.
     * </p>
     *
     * <p>
     * Two constraints are applied using Bean Validation
     * </p>
     *
     * <ol>
     * <li><code>@NotNull</code> &mdash; the name must not be null.</li>
     * <li><code>@Size</code> &mdash; the name must be at least 5 characters and no more than 50 characters. This allows for
     * better formatting consistency in the view layer.</li>
     * </ol>
     */
    @Column(unique = true)
    @NotNull
    @Size(min = 5, max = 50, message = "An event's name must contain between 5 and 50 characters")
    private String name;

    /**
     * <p>
     * A description of the event.
     * </p>
     *
     * <p>
     * Two constraints are applied using Bean Validation
     * </p>
     *
     * <ol>
     * <li><code>@NotNull</code> &mdash; the description must not be null.</li>
     * <li><code>@Size</code> &mdash; the name must be at least 20 characters and no more than 1000 characters. This allows for
     * better formatting consistency in the view layer, and also ensures that event organisers provide at least some description
     * - a classic example of a business constraint.</li>
     * </ol>
     */
    @NotNull
    @Size(min = 20, max = 1000, message = "An event's description must contain between 20 and 1000 characters")
    private String description;

    /**
     * <p>
     * A media item, such as an image, which can be used to entice a browser to book a ticket.
     * </p>
     *
     * <p>
     * Media items can be shared between events, so this is modeled as a <code>@ManyToOne</code> relationship.
     * </p>
     *
     * <p>
     * Adding a media item is optional, and the view layer will adapt if none is provided.
     * </p>
     *
     */
    @ManyToOne
    private MediaItem mediaItem;

    /**
     * <p>
     * The category of the event
     * </p>
     *
     * <p>
     * Event categories are used to ease searching of available of events, and hence this is modeled as a relationship
     * </p>
     *
     * <p>
     * The Bean Validation constraint <code>@NotNull</code> indicates that the event category must be specified.
     */
    @ManyToOne
    @NotNull
    private EventCategory category;

    /* Photoalbum additions */
    @OneToOne
    @JoinColumn
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Shelf shelf;

    @ElementCollection(fetch=FetchType.EAGER)
    private List<String> remoteAlbumIds = new ArrayList<String>();

    /* Boilerplate getters and setters */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MediaItem getMediaItem() {
        return mediaItem;
    }

    public void setMediaItem(MediaItem picture) {
        this.mediaItem = picture;
    }

    public EventCategory getCategory() {
        return category;
    }

    public void setCategory(EventCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public List<String> getRemoteAlbumIds() {
        return remoteAlbumIds;
    }

    public void setRemoteAlbumIds(List<String> remoteAlbumIds) {
        this.remoteAlbumIds = remoteAlbumIds;
    }

    private List<String> getFilteredAlbumIds(String prefix) {
        List<String> filteredIds = new ArrayList<String>();
        int prefLength = prefix.length();
        
        for (String id : remoteAlbumIds) {
            if (id.startsWith(prefix)) {
                filteredIds.add(id.substring(prefLength));
            }
        }
        
        return filteredIds;
    }
    
    public List<String> getFacebookAlbumIds() {
        return getFilteredAlbumIds("F");
    }
    
    public List<String> getGooglePlusAlbumIds() {
        return getFilteredAlbumIds("G");
    }
    
    
    public String getFbAlbumIdString() {
        return ListConverter.sListToString(getFacebookAlbumIds());
    }

    /* toString(), equals() and hashCode() for Event, using the natural identity of the object */

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Event event = (Event) o;

        if (name != null ? !name.equals(event.name) : event.name != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return name;
    }

    public Shelf getShelf() {
        return shelf;
    }

    public void setShelf(Shelf shelf) {
        this.shelf = shelf;
    }
}
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

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.OrderBy;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Shelf implements Serializable {

    private static final long serialVersionUID = -7042878411608396483L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    @NotEmpty
    @Length(min = 3, max = 50)
    private String name;

    @Column(length = 1024)
    private String description;

    @ManyToOne
    @JoinColumn
    private User owner;
    
    @OneToOne
    @JoinColumn
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Event event;

    @OneToMany(mappedBy = "shelf", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OrderBy(clause = "NAME asc")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Album> albums = new ArrayList<Album>();

    @NotNull
    private boolean shared;

    @Temporal(TemporalType.DATE)
    private Date created;

    /**
     * Getter for property preDefined
     *
     * @return is this shelf is predefined
     */
    public boolean isPreDefined() {
        return getOwner() != null && getOwner().isPreDefined();
    }

    public Long getId() {
        return id;
    }

    /**
     * Getter for property name
     *
     * @return name of album
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public boolean isOwner(User user) {
        return getOwner() != null && getOwner().equals(user);
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * Getter for property shared. If true - all users can view this shelves and albums and images, contained in this shelf,
     * otherwise this shelf can view only owner.
     *
     * @return shared
     */
    public boolean isShared() {
        return shared;
    }

    /**
     * Setter for property shared
     *
     * @param shared - determine is this shelf will be accessible by other users.
     */
    public void setShared(boolean shared) {
        this.shared = shared;
    }

    // ********************** Business Methods ********************** //

    /**
     * @return List of unvisited images
     */
    public List<Image> getUnvisitedImages() {
        final List<Image> unvisitedImages = new ArrayList<Image>();
        for (Album a : getAlbums()) {
            unvisitedImages.addAll(a.getUnvisitedImages());
        }
        return unvisitedImages;
    }

    /**
     * @return List of images, belongs to this shelf
     */
    public List<Image> getImages() {
        final List<Image> images = new ArrayList<Image>();
        for (Album a : getAlbums()) {
            images.addAll(a.getImages());
        }
        return images;
    }

    /**
     * This method add album to collection of albums of current shelf
     *
     * @param album - album to add
     */
    public void addAlbum(Album album) {
        if (album == null) {
            throw new IllegalArgumentException("Null album!");
        }

        if (album.getShelf() != null && !album.getShelf().getAlbums().contains(this)) {
            // remove from previous shelf
            album.getShelf().removeAlbum(album);
            album.setShelf(this);
            albums.add(album);
        }
    }

    /**
     * This method remove album from collection of albums of album
     *
     * @param album - album to remove
     */
    public void removeAlbum(Album album) {
        if (album == null) {
            throw new IllegalArgumentException("Null album!");
        }

        if (!album.getShelf().equals(this)) {
            throw new IllegalArgumentException("This Shelf not contain this album!");
        }

        albums.remove(album);
    }

    /**
     * This method return first album of current shelf or null if shelf haven't albums.
     *
     * @return first album of shelf or null
     */
    public Album getFirstAlbum() {
        if (this.albums.isEmpty()) {
            return null;
        }

        return this.albums.get(0);
    }

    /**
     * Return relative path of this shelf in file-system(relative to uploadRoot parameter)
     */
    public String getPath() {
        if (getOwner().getPath() == null) {
            return null;
        }
        return getOwner().getPath() + this.getId().toString() + File.separator;
    }
    
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final Shelf shelf = (Shelf) obj;

        return (id != null ? id.equals(shelf.getId()) : shelf.getId() == null)
            && (owner != null ? owner.equals(shelf.getOwner()) : shelf.getOwner() == null) && name.equals(shelf.getName());
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + name.hashCode();
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{id : " + getId() + ", name : " + getName() + "}";
    }
}
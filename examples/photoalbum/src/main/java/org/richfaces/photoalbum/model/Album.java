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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Class for representing Album Entity. EJB3 Entity Bean
 *
 * @author Andrey Markhel
 */
@Entity
@JsonAutoDetect(fieldVisibility=Visibility.NONE, getterVisibility=Visibility.NONE, isGetterVisibility=Visibility.NONE)
public class Album implements Serializable {

    private static final long serialVersionUID = -7042878411608396483L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private Long id = null;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "album", orphanRemoval = true)
    @Fetch(FetchMode.SELECT)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonProperty
    private List<Image> images = new ArrayList<Image>();

    @NotNull
    @ManyToOne
    @JoinColumn
    private Shelf shelf;

    @OneToOne(fetch = FetchType.EAGER)
    @JsonProperty
    private Image coveringImage;

    @Transient
    private boolean showAfterCreate;

    @Temporal(TemporalType.DATE)
    private Date created;

    @Column(length = 255, nullable = false)
    @NotNull
    @NotEmpty
    @Length(min = 3, max = 50)
    @JsonProperty
    private String name;

    @Column(length = 1024)
    @JsonProperty
    private String description;

    // ********************** Accessor Methods ********************** //

    /**
     * Getter for property shelf
     *
     * @return Shelf object, that contains this album
     */
    public Shelf getShelf() {
        return shelf;
    }

    public void setShelf(Shelf parent) {
        this.shelf = parent;
    }

    public Long getId() {
        return id;
    }

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

    /**
     * Getter for property images
     *
     * @return List if images, belongs to this album
     */
    public List<Image> getImages() {
        return images;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * @return List of unvisited images
     */
    public List<Image> getUnvisitedImages() {
        final List<Image> unvisitedImages = new ArrayList<Image>(this.getImages().size());
        for (Image i : this.getImages()) {
            if (i.isNew()) {
                unvisitedImages.add(i);
            }
        }
        return unvisitedImages;
    }

    /**
     * @param coveringImage - Image for covering album
     */
    public void setCoveringImage(Image coveringImage) {
        this.coveringImage = coveringImage;
    }

    // ********************** Business Methods ********************** //

    /**
     * This method add image to collection of images of current album
     *
     * @param image - image to add
     */
    public void addImage(Image image) { // TODO
        if (image == null) {
            throw new IllegalArgumentException("Null image!");
        }
        if (this.getImages().contains(image)) {
            // If album contain this image already
            return;
        }
        if (image.getAlbum() != null && !this.equals(image.getAlbum())) {
            // Remove from previous album
            image.getAlbum().removeImage(image);
        }
        image.setAlbum(this);
        images.add(image);
    }

    /**
     * This method remove image from collection of images of album
     *
     * @param image - image to remove
     */
    public void removeImage(Image image) {
        if (image == null) {
            throw new IllegalArgumentException("Null image");
        }
        if (!image.getAlbum().equals(this)) {
            throw new IllegalArgumentException("This album not contain this image!");
        }

        if (getCoveringImage().equals(image)) {
            setCoveringImage(null);
        }

        images.remove(image);
    }

    /**
     * Getter for property owner
     *
     * @return User object, owner of this album
     */
    public User getOwner() {
        return getShelf() != null ? getShelf().getOwner() : null;
    }

    public boolean isOwner(User user) {
        return user != null && user.equals(getOwner());
    }

    /**
     * This method determine index of specified image in collection of images, belongs to this album. Used in slideshow etc...
     *
     * @return index of specified image
     */
    public int getIndex(Image image) {
        if (isEmpty()) {
            return -1;
        }

        return images.indexOf(image);
    }

    /**
     * This method determine covering image of this album
     *
     * @return covering image
     */
    public Image getCoveringImage() {
        if (coveringImage == null && !isEmpty()) {
            coveringImage = images.get(0);
        }

        return coveringImage;
    }

    /**
     * This method determine is album empty or not
     *
     */
    public boolean isEmpty() {
        return images == null || images.isEmpty();
    }

    /**
     * Getter for property preDefined
     *
     * @return is this shelf is predefined
     */
    public boolean isPreDefined() {
        return getOwner().isPreDefined();
    }

    /**
     * Return relative path of this album in file-system(relative to uploadRoot parameter)
     *
     */
    public String getPath() {
        if (getShelf() != null) {
            if (getShelf().getPath() == null) {
                return null;
            }
        }
        else {
            return File.separator + "event" + File.separator + this.getId() + File.separator;
        }
        return getShelf().getPath() + this.getId() + File.separator;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final Album album = (Album) obj;

        if (id != null ? !id.equals(album.id) : album.id != null) {
            return false;
        }
        if (!shelf.equals(album.shelf)) {
            return false;
        }
        if (!name.equals(album.name)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + name.hashCode();
        result = 31 * result + shelf.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "{id : " + getId() + ", name : " + getName() + "}";
    }

    public boolean isShowAfterCreate() {
        return showAfterCreate;
    }

    public void setShowAfterCreate(boolean showAfterCreate) {
        this.showAfterCreate = showAfterCreate;
    }
}
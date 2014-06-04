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
/*
 * Image.java
 * Last modified by: $Author$
 * $Revision$	$Date$
 */
package org.richfaces.photoalbum.model;

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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.OrderBy;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.richfaces.photoalbum.util.DateUtils;

@NamedQueries({
        @NamedQuery(name = "tag-byName", query = "select m from MetaTag m where m.tag =:tag"),
        @NamedQuery(name = "tag-popular", query = "select new org.richfaces.photoalbum.model.MetaTag(m.id, m.tag) from MetaTag m join m.images img group by m.id, m.tag order by count(img) desc"),
        @NamedQuery(name = "user-shelves", query = "select distinct s from Shelf s where (s.shared = true and s.owner.preDefined = true and s.event = null) order by s.name"),
        @NamedQuery(name = "image-exist", query = "select i from Image i where i.path = :path and i.album = :album"),
        @NamedQuery(name = "image-countIdenticalImages", query = "select count(i) from Image i where i.path like :path and i.album = :album"),
        @NamedQuery(name = "tag-suggest", query = "select m from MetaTag m where lower(m.tag) like :tag") }) // cannot use "... like lower(:tag)"
/**
 * Class for representing Image Entity
 *  EJB3 Entity Bean
 *
 * @author Andrey Markhel
 */
@Entity
@JsonAutoDetect(fieldVisibility=Visibility.NONE, getterVisibility=Visibility.NONE, isGetterVisibility=Visibility.NONE)
public class Image implements Serializable {

    private static final long serialVersionUID = -7042878411608396483L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private Long id;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonProperty
    private List<MetaTag> imageTags = new ArrayList<MetaTag>();

    @OrderBy(clause = "date asc")
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "image")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Comment> comments = new ArrayList<Comment>();

    @NotNull
    @ManyToOne
    @JoinColumn
    private Album album;

    @NotNull
    @NotEmpty
    @Length(min = 3, max = 200)
    @Column(length = 255, nullable = false)
    @JsonProperty
    private String name;

    @Transient
    private boolean covering;

    @NotNull
    @NotEmpty
    @Length(min = 3)
    @Column(length = 1024, nullable = false)
    private String path;

    @Column(length = 255)
    @JsonProperty
    private String cameraModel;

    private int height;

    private double size;

    private int width;

    @Temporal(TemporalType.TIMESTAMP)
    private Date uploaded;

    @NotNull
    @NotEmpty
    @Length(min = 3)
    @Column(length = 1024)
    @JsonProperty
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @NotNull
    private boolean allowComments;

    private Boolean showMetaInfo = true;

    @Transient
    private boolean visited;

    /*
     * Comma separated tags value
     */
    @Transient
    private String meta = "";

    /**
     * Getter for property preDefined
     *
     * @return is this shelf is predefined
     */
    public boolean isPreDefined() {
        return getAlbum().isPreDefined();
    }

    // ********************** Accessor Methods ********************** //

    public Boolean getShowMetaInfo() {
        return showMetaInfo;
    }

    public void setShowMetaInfo(final Boolean showMetaInfo) {
        this.showMetaInfo = showMetaInfo;
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
     * Getter for property path. Represent file-system structure, relative at uploadRoot dir(determined at startup, by default
     * is system temp dir) Usually is user.GetLogin() + SLASH + image.getAlbum().getId() + SLASH + fileName, for example
     * "amarkhel/15/coolPicture.jpg"
     *
     * @return relative path of image
     */
    public String getPath() {
        return path;
    }

    /**
     * Setter for property path
     *
     * @param path - relative path to image
     */
    public void setPath(String path) {
        this.path = path;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public List<MetaTag> getImageTags() {
        return imageTags;
    }

    /**
     * Setter for property meta
     *
     * @param meta - string representation of metatags, associated to image. Used at jsf page.
     */
    public void setMeta(String meta) {
        this.meta = meta;
    }

    /**
     * Getter for property meta
     *
     * @return string representation of metatags, associated to image. Used at jsf page.
     */
    public String getMetaString() {
        return meta;
    }

    public String getCameraModel() {
        return cameraModel;
    }

    public void setCameraModel(String cameraModel) {
        this.cameraModel = cameraModel;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Getter for property size
     *
     * @return size of image in KB
     */
    public double getSize() {
        return size;
    }

    /**
     * setter for property size
     *
     * @param size - size of image in KB
     */
    public void setSize(double size) {
        this.size = size;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Getter for property uploaded
     *
     * @return date of upload to site of this image
     */
    public Date getUploaded() {
        return uploaded;
    }

    /**
     * setter for property uploaded
     *
     * @param uploaded - date of upload
     */
    public void setUploaded(Date uploaded) {
        this.uploaded = uploaded;
    }

    /**
     * Getter for property allowComments. If true, other user may comment this image.
     *
     * @return is other users may comment this image
     */
    public boolean isAllowComments() {
        return allowComments;
    }

    /**
     * @param allowComments the allowComments to set
     */
    public void setAllowComments(boolean allowComments) {
        this.allowComments = allowComments;
    }

    /**
     * @return if this image is covering for containing album
     */
    public boolean isCovering() {
        return covering;
    }

    public void setImageTags(final List<MetaTag> imageTags) {
        this.imageTags = imageTags;
    }

    /**
     * @param covering - determine if this image is covering for containing album
     */
    public void setCovering(boolean covering) {
        this.covering = covering;
    }

    /**
     * Getter for property visited
     *
     * @return boolean value, that indicated is user visit this image already
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * Setter for property visited
     *
     * @param visited - boolean value, that indicated is user visit this image already
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * Determine if this image should be marked as new(on jsf page, for example in tree)
     *
     * @return boolean value, that indicated is this image should be marked as new
     */
    public boolean isNew() {
        if (!visited) {
            return this.getUploaded().after(DateUtils.getRecentlyDate());
        }
        return false;
    }

    // ---------------------------Business methods

    /**
     * Add comment to this image.
     *
     * @param comment - comment to add
     */
    public void addComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("Null comment!");
        }
        comment.setImage(this);
        comments.add(comment);
    }

    /**
     * Remove comment from list of comments, belongs to that image.
     *
     * @param comment - comment to delete
     */
    public void removeComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("Null comment");
        }
        if (comment.getImage().equals(this)) {
            //comment.setImage(null);
            comments.remove(comment);
        } else {
            throw new IllegalArgumentException("Comment not belongs to this image");
        }
    }

    /**
     * Add metatag to this image.
     *
     * @param metatag - metatag to add
     */
    public void addMetaTag(MetaTag metatag) {
        if (metatag == null) {
            throw new IllegalArgumentException("Null metatag!");
        }
        if (!imageTags.contains(metatag)) {
            metatag.addImage(this);
            imageTags.add(metatag);
        }
    }

    /**
     * Remove metatag from list of metatag, associated to that image.
     *
     * @param metatag - metatag to delete
     */
    public void removeMetaTag(MetaTag metatag) {
        if (metatag == null) {
            throw new IllegalArgumentException("Null metatag!");
        }
        if (imageTags.contains(metatag)) {
            metatag.removeImage(this);
            imageTags.remove(metatag);
        }
    }

    /**
     * Return MetaTag object by string representation
     *
     * @param s - string representation of metatag
     */
    public MetaTag getTagByName(String s) {
        for (MetaTag t : imageTags) {
            if (t.getTag().equals(s)) {
                return t;
            }
        }
        return null;
    }

    /**
     * Return Comma separated tag value for presentation in view
     */
    public String getMeta() {
        final StringBuilder s = new StringBuilder();
        for (MetaTag tag : this.imageTags) {
            s.append(tag.getTag()).append(", ");
        }
        // Remove ',' from end
        if (s.length() >= 2) {
            s.delete(s.length() - 2, s.length());
        }
        return s.toString();
    }

    /**
     * Return relative path of this image in file-system(relative to uploadRoot parameter)
     */
    public String getFullPath() {
        if (getAlbum().getPath() == null) {
            return null;
        }

        return getAlbum().getPath() + this.path;
    }

    public User getOwner() {
        return getAlbum().getOwner();
    }

    public boolean isOwner(User user) {
        return user != null && user.equals(getOwner());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        final Image image = (Image) obj;

        return (id == null ? image.getId() == null : id.equals(image.getId()))
            && (path == null ? image.getPath() == null : path.equals(image.getPath()));
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{id : " + getId() + ", name : " + getName() + "}";
    }
}

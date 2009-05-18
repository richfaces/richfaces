/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */
package org.richfaces.photoalbum.domain;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class for representing Album Entity. EJB3 Entity Bean
 *
 * @author Andrey Markhel
 */
@Entity
@Name("album")
@Scope(ScopeType.CONVERSATION)
@AutoCreate
public class Album implements Serializable {

	private static final long serialVersionUID = -7042878411608396483L;

	@Id
	@GeneratedValue
	private Long id = null;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "album")
	@Fetch(FetchMode.SELECT)
	private List<Image> images = new ArrayList<Image>();

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Shelf shelf;

	@OneToOne(fetch = FetchType.LAZY)
	private Image coveringImage;

	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@Column(length = 255, nullable = false)
	@NotNull
	@NotEmpty
	@Length(min = 3, max = 50)
	private String name;

	@Column(length = 1024)
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
	 * @param image -
	 *              image to add
	 */
	public void addImage(Image image) { // TODO
		if (image == null) {
			throw new IllegalArgumentException("Null image!");
		}
		if (this.getImages().contains(image)) {
			//If album contain this image already
			return;
		}
		if (image.getAlbum() != null && !this.equals(image.getAlbum())) {
			//Remove from previous album
			image.getAlbum().removeImage(image);
		}
		image.setAlbum(this);
		images.add(image);
	}

	/**
	 * This method remove image from collection of images of album
	 *
	 * @param image -
	 *              image to remove
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
		return getShelf() != null ?  getShelf().getOwner() : null ;
	}

	public boolean isOwner(User user) {
		return user != null && user.equals(getOwner());
	}

	/**
	 * This method determine index of specified image in collection of images, belongs to this album.
	 * Used in slideshow etc...
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
		if (getShelf().getPath() == null) {
			return null;
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

		if (id != null ? !id.equals(album.id) : album.id != null) return false;
		if (!shelf.equals(album.shelf)) return false;
		if (!name.equals(album.name)) return false;

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
		return "{id : "+getId()+", name : "+getName()+"}";
	}
}
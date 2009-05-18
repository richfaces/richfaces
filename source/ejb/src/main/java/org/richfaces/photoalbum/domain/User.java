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

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.OrderBy;
import org.hibernate.validator.Email;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * Class for representing User Entity
 * EJB3 Entity Bean
 *
 * @author Andrey Markhel
 */

@NamedQueries({
	@NamedQuery(
			name = "user-login",
			query = "select u from User u where u.login = :username and u.passwordHash = :password"
	),
	@NamedQuery(
			name = "user-comments",
			query = "select c from Comment c where c.author = :author"
	),
	@NamedQuery(
			name = "user-exist",
			query = "select u from User u where u.login = :login"
	),
	@NamedQuery(
			name = "email-exist",
			query = "select u from User u where u.email = :email"
	),
	@NamedQuery(
			name = "user-user",
			query = "select u from User u where u.login = :login"
	)
		})

@Entity
@Scope(ScopeType.SESSION)
@Name("user")
@AutoCreate
@Table(name="User", uniqueConstraints = {
	@UniqueConstraint(columnNames = "login"),
	@UniqueConstraint(columnNames = "email")
		}
)
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	private String passwordHash;

	@NotNull
	@NotEmpty
	@Length(min = 3)
	@Column(length = 255, nullable = false)
	private String firstName;

	@NotNull
	@NotEmpty
	@Length(min = 3)
	@Column(length = 255, nullable = false)
	private String secondName;

	@Column(length = 255, nullable = false)
	@NotNull
	@NotEmpty
	@Email
	private String email;

	@Column(length = 255, nullable = false)
	@NotNull
	@NotEmpty
	@Length(min = 3)
	private String login;

	@Transient
	@NotNull
	@NotEmpty
	@Length(min = 3)
	private String password;

	@Transient
	@NotNull
	@NotEmpty
	@Length(min = 3)
	private String confirmPassword;

	@Temporal(TemporalType.TIMESTAMP)
	private Date birthDate;

	@NotNull
	private Sex sex = Sex.MALE;

	private Boolean hasAvatar;

	@OrderBy(clause = "NAME asc")
	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.EXTRA)
	private List<Shelf> shelves = new ArrayList<Shelf>();

	private boolean preDefined;

	public boolean isPreDefined() {
		return preDefined;
	}

	public void setPreDefined(boolean preDefined) {
		this.preDefined = preDefined;
	}

	//----------------Getters, Setters
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Long getId() {
		return id;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public List<Shelf> getShelves() {
		return shelves;
	}

	public void setShelves(List<Shelf> shelves) {
		this.shelves = shelves;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public Boolean getHasAvatar() {
		return hasAvatar;
	}

	public void setHasAvatar(Boolean hasAvatar) {
		this.hasAvatar = hasAvatar;
	}

	//---------------------------Business methods

	/**
	 * This method add shelf to collection of shelves, belongs to user
	 *
	 * @param shelf -
	 *              shelf to add
	 */
	public void addShelf(Shelf shelf) {
		if (shelf == null) {
			throw new IllegalArgumentException("Null shelf!");
		}
		if (!shelves.contains(shelf)) {
			shelf.setOwner(this);
			shelves.add(shelf);
		}
	}

	/**
	 * This method remove shelf from collection of shelves, belongs to user
	 *
	 * @param shelf -
	 *              shelf to remove
	 */
	public void removeShelf(Shelf shelf) {
		if (shelf == null) {
			throw new IllegalArgumentException("Null shelf");
		}
		if (shelf.getOwner().getLogin().equals(this.getLogin())) {
			shelf.setOwner(null);
			shelves.remove(shelf);
		} else {
			throw new IllegalArgumentException("Shelf not belongs to this user!");
		}
	}

	/**
	 * Return relative path of folder with user's images in file-system(relative to uploadRoot parameter)
	 */
	public String getPath() {
		if (this.getId() == null) {
			return null;
		}
		return File.separator + this.getLogin() + File.separator;
	}

	/**
	 * This method return all images, belongs to user
	 *
	 * @return images, belongs to user
	 */
	public List<Image> getImages() {
		final List<Image> images = new ArrayList<Image>();
		for (Shelf s : getShelves()) {
			images.addAll(s.getImages());
		}
		return images;
	}

	/**
	 * This method return all albums, belongs to user
	 *
	 * @return albums, belongs to user
	 */
	public List<Album> getAlbums() {
		final List<Album> albums = new ArrayList<Album>();
		for (Shelf s : getShelves()) {
			albums.addAll(s.getAlbums());
		}
		return albums;
	}

	/**
	 * This method return all images, belongs to user
	 *
	 * @return images, belongs to user
	 */
	public List<Image> getSharedImages() {
		final List<Image> images = new ArrayList<Image>();
		for (Shelf s : getShelves()) {
			if (!s.isShared()) {
				continue;
			}
			for (Album a : s.getAlbums()) {
				images.addAll(a.getImages());
			}
		}
		return images;
	}

	/**
	 * This method return all albums, belongs to user
	 *
	 * @return albums, belongs to user
	 */
	public List<Album> getSharedAlbums() {
		final List<Album> albums = new ArrayList<Album>();
		for (Shelf s : getShelves()) {
			if (!s.isShared()) {
				continue;
			}
			albums.addAll(s.getAlbums());
		}
		return albums;
	}

	/**
	 * This method check, if user already have shelf with given name
	 * 
	 * @param shelf - shelf to check
	 * @return boolean value, that indicated if shelf with the same name exist
	 */
	public boolean hasShelfWithName(Shelf shelf) {
		for(Shelf s : getShelves()){
			if(!s.equals(shelf) && s.getName().equals(shelf.getName())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This method check, if parent shelf contain album with the same name as given album
	 * 
	 * @param album - album to check
	 * @return boolean value, that indicate if album with the same name exist
	 */
	public boolean hasAlbumWithName(Album album) {
		for(Album a : album.getShelf().getAlbums()){
			if(!a.equals(album) && a.getName().equals(album.getName())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This method check, if containing album already have image with the same name
	 * 
	 * @param image - image to check
	 * @return boolean value, that indicate if image with the same name exist
	 */
	public boolean hasImageWithName(Image image) {
		for(Image i : image.getAlbum().getImages()){
			if(!i.equals(image) &&  i.getName().equals(image.getName())){
				return true;
			}
		}
		return false;
	}
	
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		final User user = (User) o;

		if (id != null ? !id.equals(user.id) : user.id != null) return false;
		if (login != null ? !login.equals(user.login) : user.login != null)
			return false;

		return true;
	}

	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (login != null ? login.hashCode() : 0);
		return result;
	}
}
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

import javax.enterprise.context.SessionScoped;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.OrderBy;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Class for representing User Entity EJB3 Entity Bean
 *
 * @author Andrey Markhel
 */

@NamedQueries({
    @NamedQuery(name = "user-login", query = "select u from User u where u.login = :username and u.passwordHash = :password"),
    @NamedQuery(name = "user-comments", query = "select c from Comment c where c.author = :author"),
    @NamedQuery(name = "user-exist", query = "select u from User u where u.login = :login"),
    @NamedQuery(name = "email-exist", query = "select u from User u where u.email = :email"),
    @NamedQuery(name = "user-user", query = "select u from User u where u.login = :login"),
    @NamedQuery(name = "user-fb-login", query = "select u from User u where u.fbId = :fbId"),
    @NamedQuery(name = "user-gplus-login", query = "select u from User u where u.gPlusId = :gPlusId") })
@Entity
@SessionScoped
@Table(name = "User", uniqueConstraints = {
    @UniqueConstraint(columnNames = "login"),
    @UniqueConstraint(columnNames = "email") })
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    @Column(length = 30)
    private String fbId = "1";
    
    @NotNull
    @NotEmpty
    @Column(length = 30)
    private String gPlusId = "1";

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
    private String password;

    @Transient
    private String confirmPassword;

    @Temporal(TemporalType.TIMESTAMP)
    private Date birthDate;

    @NotNull
    private Sex sex = Sex.MALE;

    private Boolean hasAvatar;

    @OrderBy(clause = "NAME asc")
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    // @LazyCollection(LazyCollectionOption.EXTRA)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Shelf> shelves = new ArrayList<Shelf>();

    private boolean preDefined;

    public boolean isPreDefined() {
        return preDefined;
    }

    public void setPreDefined(boolean preDefined) {
        this.preDefined = preDefined;
    }

    // ----------------Getters, Setters
    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getgPlusId() {
        return gPlusId;
    }

    public void setgPlusId(String gPlusId) {
        this.gPlusId = gPlusId;
    }

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
        // return only shelves that aren't bound to events
        List<Shelf> shelvesWithoutEvents = new ArrayList<Shelf>();

        for (Shelf shelf : shelves) {
            if (shelf.getEvent() == null) {
                shelvesWithoutEvents.add(shelf);
            }
        }

        return shelvesWithoutEvents;
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

    // ---------------------------Business methods

    /**
     * This method add shelf to collection of shelves, belongs to user
     *
     * @param shelf - shelf to add
     */
    public void addShelf(Shelf shelf) {
        if (shelf == null) {
            throw new IllegalArgumentException("Null album group!");
        }
        if (!shelves.contains(shelf)) {
            shelf.setOwner(this);
            shelves.add(shelf);
        }
    }

    /**
     * This method remove shelf from collection of shelves, belongs to user
     *
     * @param shelf - shelf to remove
     */
    public void removeShelf(Shelf shelf) {
        if (shelf == null) {
            throw new IllegalArgumentException("Null album group");
        }
        if (shelf.getOwner().getLogin().equals(this.getLogin())) {
            shelves.remove(shelf);
        } else {
            throw new IllegalArgumentException("Album Groups does not belong to this user!");
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
        for (Shelf s : getShelves()) {
            if (!s.equals(shelf) && s.getName().equals(shelf.getName())) {
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
        for (Album a : album.getShelf().getAlbums()) {
            if (!a.equals(album) && a.getName().equals(album.getName())) {
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
        for (Image i : image.getAlbum().getImages()) {
            if (!i.equals(image) && i.getName().equals(image.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) {
            return false;
        }
        if (login != null ? !login.equals(user.login) : user.login != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" + "firstName=" + firstName + ", secondName=" + secondName + ", email=" + email + ", login=" + login + ", sex=" + sex + ", hasAvatar=" + hasAvatar + '}';
    }
}

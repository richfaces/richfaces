package org.richfaces.photoalbum.bean;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.richfaces.photoalbum.domain.Album;
import org.richfaces.photoalbum.domain.Image;
import org.richfaces.photoalbum.domain.Shelf;
import org.richfaces.photoalbum.domain.User;
import org.richfaces.photoalbum.service.Constants;

/**
 * This bean was created to provide User entity in places where it is needed (UserAction and ShelfAction).
 * This is a temporary solution.
 *
 * @author mpetrov
 */

@Singleton
public class UserBean {

    @Inject
    EntityManager em;

    private User user;

    private Shelf currentShelf;
    private Album currentAlbum;
    private Image currentImage;

    private boolean logged;

    // -- getters and setters

    public User logIn(String username, String passwordHash) throws Exception {
        user = (User) em.createNamedQuery(Constants.USER_LOGIN_QUERY).setParameter(Constants.USERNAME_PARAMETER, username)
            .setParameter(Constants.PASSWORD_PARAMETER, passwordHash).getSingleResult();
        logged = user != null;

        return user;
    }

    public User getUser() {
        if (!logged) {
            return null;
        }
        return user;
    }

    public void refreshUser() {
        if (logged) {
            user = (User) em.createNamedQuery(Constants.USER_LOGIN_QUERY).setParameter(Constants.USERNAME_PARAMETER, user.getLogin())
                .setParameter(Constants.PASSWORD_PARAMETER, user.getPasswordHash()).getSingleResult();
            logged = user != null;
        }
    }

    // public void setUser(User user) {
    // this.user = user;
    // }
    public Shelf getCurrentShelf() {
        return currentShelf;
    }

    public void setCurrentShelf(Shelf currentShelf) {
        this.currentShelf = currentShelf;
    }

    public Album getCurrentAlbum() {
        return currentAlbum;
    }

    public void setCurrentAlbum(Album currentAlbum) {
        this.currentAlbum = currentAlbum;
    }

    public Image getCurrentImage() {
        return currentImage;
    }

    public void setCurrentImage(Image currentImage) {
        this.currentImage = currentImage;
    }

    // -- management classes

}

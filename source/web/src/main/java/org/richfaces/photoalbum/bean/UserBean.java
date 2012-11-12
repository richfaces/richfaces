package org.richfaces.photoalbum.bean;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.logging.Logger;
//import org.richfaces.photoalbum.domain.Album;
//import org.richfaces.photoalbum.domain.Image;
//import org.richfaces.photoalbum.domain.Shelf;
import org.richfaces.photoalbum.domain.User;
import org.richfaces.photoalbum.service.Constants;
import org.richfaces.photoalbum.util.Preferred;

/**
 * This bean will work as a part of a simple security checking
 * 
 * @author mpetrov
 */

@Named
@SessionScoped
public class UserBean {

    @Inject
    EntityManager em;

    private User user;

    // private Shelf currentShelf;
    // private Album currentAlbum;
    // private Image currentImage;

    private boolean logged;

    // -- getters and setters

    private boolean autolog = true; //

    private Logger logger = Logger.getLogger(UserBean.class);

    @SuppressWarnings("unused")
    @PostConstruct
    private void logUser() {
        if (autolog) {
            try {
                user = logIn("amarkhel", "8cb2237d0679ca88db6464eac60da96345513964");
            } catch (Exception e) {
                logger.info("Autolog unsuccessful:" + e.getMessage());
            }
        }
    }

    public User logIn(String username, String passwordHash) throws Exception {
        user = (User) em.createNamedQuery(Constants.USER_LOGIN_QUERY).setParameter(Constants.USERNAME_PARAMETER, username)
            .setParameter(Constants.PASSWORD_PARAMETER, passwordHash).getSingleResult();
        logged = user != null;

        return user;
    }

    @Produces
    @Preferred
    public User getUser() {
        if (!logged) {
            return null;
        }
        return user;
    }

    public void refreshUser() {
        if (logged) {
            user = (User) em.createNamedQuery(Constants.USER_LOGIN_QUERY)
                .setParameter(Constants.USERNAME_PARAMETER, user.getLogin())
                .setParameter(Constants.PASSWORD_PARAMETER, user.getPasswordHash()).getSingleResult();
            logged = user != null;
        }
    }
    
    public boolean isLoggedIn() {
        return logged;
    }

    public boolean isUserAdmin() {
        if (user == null) return false;
        return user.getLogin().equals("amarkhel") || user.getLogin().equals("Viking");
    }

    // public void setUser(User user) {
    // this.user = user;
    // }
    /*
     * public Shelf getCurrentShelf() { return currentShelf; }
     * 
     * public void setCurrentShelf(Shelf currentShelf) { this.currentShelf = currentShelf; }
     * 
     * public Album getCurrentAlbum() { return currentAlbum; }
     * 
     * public void setCurrentAlbum(Album currentAlbum) { this.currentAlbum = currentAlbum; }
     * 
     * public Image getCurrentImage() { return currentImage; }
     * 
     * public void setCurrentImage(Image currentImage) { this.currentImage = currentImage; }
     */
    // -- management classes
}

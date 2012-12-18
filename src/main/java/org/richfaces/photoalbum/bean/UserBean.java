package org.richfaces.photoalbum.bean;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.richfaces.photoalbum.domain.User;
import org.richfaces.photoalbum.service.Constants;
import org.richfaces.photoalbum.util.Preferred;

/**
 * This bean will work as a part of a simple security checking
 *
 * @author mpetrov
 */

@Named
@ApplicationScoped
public class UserBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Inject
    EntityManager em;

    private User user;

    private String username;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    private boolean logged = false;

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

    public void logout() {
        user = null;
        logged = false;
    }

    public void reset() {
        username = "";
        password = "";
    }
}

package org.richfaces.photoalbum.manager;

import javax.inject.Inject;

import org.jboss.seam.security.Identity;
import org.jboss.seam.security.annotations.Secures;
import org.richfaces.photoalbum.bean.UserBean;

public class Restrictions {
    @Inject
    UserBean uBean;

    public @Secures
    @AdminRestricted
    boolean isAdmin(Identity identity) {
        //return identity.hasRole(Constants.ADMIN_ROLE, "Users", "GROUP");
        return uBean.getUser().getLogin().equals("amarkhel");
    }
}

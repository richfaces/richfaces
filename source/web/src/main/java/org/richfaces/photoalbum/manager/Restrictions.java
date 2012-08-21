package org.richfaces.photoalbum.manager;

import org.jboss.seam.security.Identity;
import org.jboss.seam.security.annotations.Secures;
import org.richfaces.photoalbum.service.Constants;

public class Restrictions {
    public @Secures
    @AdminRestricted
    boolean isAdmin(Identity identity) {
        return identity.hasRole(Constants.ADMIN_ROLE, "Users", "GROUP");
    }
}

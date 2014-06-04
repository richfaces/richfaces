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
package org.richfaces.photoalbum.ui;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.richfaces.photoalbum.util.ApplicationUtils;

/**
 * Special <code>PhaseListener</code> for check is the user session was expired or user were login in another browser. By
 * default phaseListener works on <code>PhaseId.RESTORE_VIEW</code> JSF lifecycle phase.
 *
 * @author Andrey Markhel
 */
@RequestScoped
public class UserExpiredPhaseListener implements PhaseListener {

    private static final long serialVersionUID = 1L;
    private PhaseId phase = PhaseId.RESTORE_VIEW;

    public void beforePhase(PhaseEvent e) {
        ApplicationUtils utils = getUtils();
        utils.fireCheckUserExpiredEvent();
    }

    public void afterPhase(PhaseEvent e) {
    }

    public void setPhase(PhaseId phase) {
        this.phase = phase;
    }

    public PhaseId getPhaseId() {
        return phase;
    }

    public BeanManager getBeanManager() {
        BeanManager beanManager = null;
        try {
            InitialContext initialContext = new InitialContext();
            beanManager = (BeanManager) initialContext.lookup("java:comp/BeanManager");
        } catch (NamingException e) {
            Logger.getLogger("UserPhaseExpired").log(Level.SEVERE, "Couldn't get BeanManager through JNDI", e);
        }
        return beanManager;
    }

    private ApplicationUtils getUtils() {
        BeanManager bm = getBeanManager();
        Bean<ApplicationUtils> bean = (Bean<ApplicationUtils>) bm.getBeans(ApplicationUtils.class).iterator().next();
        CreationalContext<ApplicationUtils> ctx = bm.createCreationalContext(bean);
        ApplicationUtils utils = (ApplicationUtils) bm.getReference(bean, ApplicationUtils.class, ctx);
            // this could be inlined, but intentionally left this way
        return utils;
    }
}

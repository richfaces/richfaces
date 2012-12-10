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
package org.richfaces.photoalbum.ui;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.jboss.solder.beanManager.BeanManagerLocator;
import org.richfaces.photoalbum.util.Utils;

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
        Utils utils = getUtils();
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

    private BeanManager getBeanManager() {
        return new BeanManagerLocator().getBeanManager();
    }

    private Utils getUtils() {
        BeanManager bm = getBeanManager();
        Bean<Utils> bean = (Bean<Utils>) bm.getBeans(Utils.class).iterator().next();
        CreationalContext<Utils> ctx = bm.createCreationalContext(bean);
        Utils utils = (Utils) bm.getReference(bean, Utils.class, ctx); // this could be inlined, but intentionally left this way
        return utils;
    }
}

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

package org.richfaces.photoalbum.util.converters;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.richfaces.photoalbum.manager.UserBean;
import org.richfaces.photoalbum.model.Shelf;

@Named
@ApplicationScoped
@FacesConverter("shelfConverter")
public class ShelfConverter implements Converter {

    public BeanManager getBeanManager() {
        BeanManager beanManager = null;
        try {
            InitialContext initialContext = new InitialContext();
            beanManager = (BeanManager) initialContext.lookup("java:comp/BeanManager");
        } catch (NamingException e) {
            Logger.getLogger("ShelfConverter").log(Level.SEVERE, "Couldn't get BeanManager through JNDI", e);
        }
        return beanManager;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        BeanManager bm = getBeanManager();
        Bean<UserBean> bean = (Bean<UserBean>) bm.getBeans(UserBean.class).iterator().next();
        CreationalContext<UserBean> ctx = bm.createCreationalContext(bean);
        UserBean userBean = (UserBean) bm.getReference(bean, UserBean.class, ctx); // this could be inlined, but intentionally left this way


        for(Shelf s : userBean.getUser().getShelves()) {
            if (s.getName().equals(value)) {
                return s;
            }
        }

        return new Shelf();
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }

        return ((Shelf) value).getName();
    }

}

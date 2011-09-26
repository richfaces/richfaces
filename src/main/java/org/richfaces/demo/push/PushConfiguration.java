/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
 */package org.richfaces.demo.push;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

/**
 * Initializes JMS server and creates requested topics.
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
@ApplicationScoped
@ManagedBean
public class PushConfiguration {

    public boolean isJmsEnabled() {
        return JMSInitializer.isJmsEnabled();
    }

    public boolean isPushEnabled() {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String pushDisabled = servletContext.getInitParameter("org.richfaces.showcase.pushDisabled");
        if (pushDisabled == null) {
            return true;
        }
        return Boolean.valueOf(pushDisabled);
    }
}

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
package org.richfaces.push;

import javax.faces.context.FacesContext;

/**
 * <p>PushContext serves as container class holding references to {@link TopicsContext}, {@link SessionFactory} and {@link SessionManager}.</p>
 *
 * <p>It has been introduced to isolate details of particular messaging bus integration from using classes.</p>
 *
 * @author Nick Belaevski
 */
public interface PushContext {

    String INSTANCE_KEY_NAME = PushContext.class.getName();

    TopicsContext getTopicsContext();

    SessionFactory getSessionFactory();

    SessionManager getSessionManager();

    /**
     * Initializes {@link PushContext} instance once the application is started.
     */
    void init(FacesContext facesContext);

    /**
     * Destroys this {@link PushContext} instance once the application is teared down.
     */
    void destroy();

    /**
     * Returns the URL on which listens Push handler
     */
    String getPushHandlerUrl();
}

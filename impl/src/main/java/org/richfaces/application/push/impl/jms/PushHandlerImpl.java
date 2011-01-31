/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.application.push.impl.jms;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.atmosphere.cpr.AtmosphereResource;
import org.richfaces.application.push.Request;
import org.richfaces.application.push.Session;
import org.richfaces.application.push.SessionFactory;
import org.richfaces.application.push.SessionManager;
import org.richfaces.application.push.TopicsContext;
import org.richfaces.application.push.impl.AtmospherePushHandler;

/**
 * @author Nick Belaevski
 * 
 */
public class PushHandlerImpl extends AtmospherePushHandler implements SessionFactory {

    private MessagingContext messagingContext;

    private TopicsContext topicsContext;

    public PushHandlerImpl(MessagingContext messagingContext, TopicsContext topicsContext) {
        super();
        this.messagingContext = messagingContext;
        this.topicsContext = topicsContext;
    }

    public Session createSession(String key) {
        SessionManager sessionManager = getSessionManager();
        Session session = new SessionImpl(key, sessionManager, messagingContext, topicsContext);
        sessionManager.putPushSession(session);

        return session;
    }

    @Override
    protected Request createRequest(AtmosphereResource<HttpServletRequest, HttpServletResponse> resource,
        Session session) {
        return new RequestImpl(resource, session, getWorker(), topicsContext);
    }
}

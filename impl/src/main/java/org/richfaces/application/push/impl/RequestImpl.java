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
package org.richfaces.application.push.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.AtmosphereResourceEventListener;
import org.atmosphere.cpr.Meteor;
import org.atmosphere.websocket.WebSocketSupport;
import org.richfaces.application.push.Request;
import org.richfaces.application.push.Session;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * @author Nick Belaevski
 *
 */
public class RequestImpl implements Request, AtmosphereResourceEventListener {
    private static final Logger LOGGER = RichfacesLogger.APPLICATION.getLogger();
    private static final int SUSPEND_TIMEOUT = 30 * 1000;
    private Session session;
    private final Meteor meteor;
    private boolean hasActiveBroadcaster = false;

    public RequestImpl(Meteor meteor, Session session) {
        super();

        this.meteor = meteor;
        meteor.addListener(this);

        this.session = session;
    }

    public void suspend() {
        meteor.suspend(SUSPEND_TIMEOUT, isPolling());
    }

    public void resume() {
        meteor.resume();
    }

    public boolean isPolling() {
        HttpServletRequest req = meteor.getAtmosphereResource().getRequest();
        boolean isWebsocket = req.getAttribute(WebSocketSupport.WEBSOCKET_SUSPEND) != null
            || req.getAttribute(WebSocketSupport.WEBSOCKET_RESUME) != null;

        // TODO how to detect non-polling transports?
        return !isWebsocket;
    }

    public Session getSession() {
        return session;
    }

    public synchronized void postMessages() {
        if (!hasActiveBroadcaster && !session.getMessages().isEmpty()) {
            hasActiveBroadcaster = true;
            meteor.getBroadcaster().broadcast(new MessageDataScriptString(getSession().getMessages()));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.atmosphere.cpr.AtmosphereResourceEventListener#onSuspend(org.atmosphere.cpr.AtmosphereResourceEvent)
     */
    public void onSuspend(AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) {
        try {
            getSession().connect(this);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void disconnect() {
        try {
            getSession().disconnect();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void onResume(AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) {
        disconnect();
    }

    public void onDisconnect(AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) {
        disconnect();
    }

    public synchronized void onBroadcast(AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) {
        MessageDataScriptString serializedMessages = (MessageDataScriptString) event.getMessage();
        getSession().clearBroadcastedMessages(serializedMessages.getLastSequenceNumber());

        hasActiveBroadcaster = false;

        if (isPolling()) {
            event.getResource().resume();
        } else {
            postMessages();
        }
    }

    public void onThrowable(AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) {
        // TODO Auto-generated method stub
        Throwable throwable = event.throwable();
        LOGGER.error(throwable.getMessage(), throwable);
    }
}

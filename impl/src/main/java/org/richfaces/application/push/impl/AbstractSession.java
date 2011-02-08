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

import java.io.IOException;

import org.richfaces.application.push.Request;
import org.richfaces.application.push.Session;
import org.richfaces.application.push.SessionManager;
import org.richfaces.application.push.impl.SessionManagerImpl.DestroyableSession;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * @author Nick Belaevski
 * 
 */
public abstract class AbstractSession implements Session, DestroyableSession {

    private static final Logger LOGGER = RichfacesLogger.APPLICATION.getLogger();

    private static final int MAX_INACTIVE_INTERVAL = 5 * 60 * 1000;

    private final String id;

    private final SessionManager sessionManager;
    
    private volatile long lastAccessedTime;
    
    private volatile Request request;

    private volatile boolean active = true;
    
    public AbstractSession(String id, SessionManager sessionManager) {
        super();
        
        this.id = id;
        this.sessionManager = sessionManager;

        resetLastAccessedTimeToCurrent();
    }

    private void resetLastAccessedTimeToCurrent() {
        lastAccessedTime = System.currentTimeMillis();
    }
    
    public synchronized void connect(Request request) throws Exception {
        releaseRequest();

        if (active) {
            processConnect(request);
        } else {
            request.resume();
        }
    }

    protected Request getRequest() {
        return request;
    }
    
    protected void processConnect(Request request) throws Exception {
        this.request = request;
        
        sessionManager.requeue(this);
    }
    
    private void releaseRequest() {
        Request localRequestCopy = this.request;
        
        if (localRequestCopy != null) {
            resetLastAccessedTimeToCurrent();
            this.request = null;

            try {
                localRequestCopy.resume();
            } catch (IOException e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }
    
    public synchronized void disconnect() throws Exception {
        processDisconnect();
    }
    
    protected void processDisconnect() throws Exception {
        releaseRequest();
    }
    
    public long getLastAccessedTime() {
        if (!active) {
            return -1;
        }
        
        if (this.request != null) {
            //being accessed right now
            return System.currentTimeMillis();
        } else {
            return lastAccessedTime;
        }
    }
    
    public int getMaxInactiveInterval() {
        return MAX_INACTIVE_INTERVAL;
    }
    
    public String getId() {
        return id;
    }
    
    public void invalidate() {
        active = false;
        
        sessionManager.requeue(this);
    }
    
    public synchronized void destroy() {
        active = false;

        try {
            disconnect();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}

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

/**
 * @author Nick Belaevski
 * 
 */
public abstract class AbstractSession implements Session {

    private static final int MAX_INACTIVE_INTERVAL = 5 * 60 * 1000;

    private final String id;

    private final SessionManager sessionManager;
    
    private volatile long lastAccessedTime;
    
    private volatile Request request;

    public AbstractSession(String id, SessionManager sessionManager) {
        super();
        this.id = id;
        this.sessionManager = sessionManager;

        resetLastAccessedTimeToCurrent();
    }

    private void resetLastAccessedTimeToCurrent() {
        lastAccessedTime = System.currentTimeMillis();
    }
    
    private void requeue() {
        resetLastAccessedTimeToCurrent();
        sessionManager.requeue(this);
    }
    
    public void connect(Request request) throws Exception {
        if (this.request != null) {
            this.request.resume();
        }
        
        this.request = request;

        requeue();

        request.suspend();
    }
    
    public void disconnect() throws Exception {
        this.request = null;
    }
    
    public long getLastAccessedTime() {
        if (request != null) {
            return System.currentTimeMillis();
        }

        return lastAccessedTime;
    }
    
    public int getMaxInactiveInterval() {
        return MAX_INACTIVE_INTERVAL;
    }
    
    public String getId() {
        return id;
    }
    
    public Request getRequest() {
        return request;
    }

    public void destroy() {
        if (request != null) {
            try {
                request.resume();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            request = null;
            //TODO - clean up request
        }

        sessionManager.removePushSession(this);
        // TODO Auto-generated method stub
        
    }
}

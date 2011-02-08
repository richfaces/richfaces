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

import java.util.Iterator;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadFactory;

import org.richfaces.application.push.Session;
import org.richfaces.application.push.SessionManager;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

import com.google.common.collect.MapMaker;

/**
 * @author Nick Belaevski
 * 
 */
public class SessionManagerImpl implements SessionManager {

    private static final Logger LOGGER = RichfacesLogger.APPLICATION.getLogger();
    
    interface DestroyableSession {
        
        public void destroy();
        
    }
    
    private final class SessionsExpirationRunnable implements Runnable {
        public void run() {
            while (true) {
                try {
                    Session session = sessionQueue.take();
                    sessionMap.remove(session.getId());
                    if (session instanceof DestroyableSession) {
                        ((DestroyableSession) session).destroy();
                    }
                } catch (InterruptedException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }

        }
    }
    
    private ConcurrentMap<String, Session> sessionMap = new MapMaker().makeMap();
    
    private SessionQueue sessionQueue = new SessionQueue();
    
    public SessionManagerImpl(ThreadFactory threadFactory) {
        threadFactory.newThread(new SessionsExpirationRunnable()).start();
    }
    
    public Session getPushSession(String id) {
        return sessionMap.get(id);
    }

    public void destroy() {
        //TODO notify all session
        sessionQueue.clear();
        
        while (!sessionMap.isEmpty()) {
            for (Iterator<Session> sessionsItr = sessionMap.values().iterator(); sessionsItr.hasNext(); ) {
                Session session = sessionsItr.next();
                
                if (session instanceof DestroyableSession) {
                    ((DestroyableSession) session).destroy();
                }
            }
        }
        
        sessionMap.clear();
    }

    public void putPushSession(Session session) throws IllegalStateException {
        Session existingSession = sessionMap.putIfAbsent(session.getId(), session);
        if (existingSession != null) {
            throw new IllegalStateException();
        }
        
        sessionQueue.requeue(session, true);
    }

    public void requeue(Session session) {
        sessionQueue.requeue(session, false);
    }
}

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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.atmosphere.cpr.AtmosphereHandler;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.richfaces.application.push.Request;
import org.richfaces.application.push.Session;
import org.richfaces.application.push.SessionManager;

/**
 * @author Nick Belaevski
 * 
 */
public abstract class AtmospherePushHandler implements AtmosphereHandler<HttpServletRequest, HttpServletResponse> {

    private static final ThreadFactory DAEMON_THREADS_FACTORY = new ThreadFactory() {

        private final AtomicInteger threadsCounter = new AtomicInteger();

        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "rf-push-worker-thread-" + threadsCounter.getAndIncrement());
            t.setDaemon(true);

            return t;
        }
    };

    private static final String PUSH_SESSION_ID_PARAM = "pushSessionId";
    
    private SessionManager sessionManager;
    
    private ExecutorService worker;
    
    public AtmospherePushHandler() {
        super();
        
        sessionManager = new SessionManagerImpl(DAEMON_THREADS_FACTORY);
        worker = Executors.newCachedThreadPool(DAEMON_THREADS_FACTORY);
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }
    
    public void onRequest(AtmosphereResource<HttpServletRequest, HttpServletResponse> resource) throws IOException {
        // TODO Auto-generated method stub

        HttpServletRequest req = resource.getRequest();
        HttpServletResponse resp = resource.getResponse();
        
        String pushSessionId = req.getParameter(PUSH_SESSION_ID_PARAM);

        Session session = null;
        
        if (pushSessionId != null) {
            session = getSessionManager().getPushSession(pushSessionId);
        }
        
        if (session == null) {
            //TODO - debug log
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        resp.setContentType("text/plain");
        
        try {
            Request request = createRequest(resource, session);
            request.suspend();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void onStateChange(AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event)
        throws IOException {
        //do nothing
    }

    protected abstract Request createRequest(AtmosphereResource<HttpServletRequest, HttpServletResponse> resource, Session session);
 
    protected ExecutorService getWorker() {
        return worker;
    }
    
    public void init(ServletConfig servletConfig) throws Exception {
    }
    
    public void destroy() throws Exception {
        sessionManager.destroy();
    }
    
}

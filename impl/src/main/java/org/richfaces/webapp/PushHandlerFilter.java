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
package org.richfaces.webapp;

import java.io.IOException;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.atmosphere.cpr.BroadcastFilter;
import org.atmosphere.cpr.Broadcaster.SCOPE;
import org.atmosphere.cpr.Meteor;
import org.richfaces.application.push.PushContext;
import org.richfaces.application.push.Request;
import org.richfaces.application.push.Session;
import org.richfaces.application.push.SessionManager;
import org.richfaces.application.push.impl.RequestImpl;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * Serves as delegate for Atmposphere servlets - should not be used directly
 *
 * @author Nick Belaevski
 *
 */
public class PushHandlerFilter implements Filter, Serializable {
    public static final String SESSION_ATTRIBUTE_NAME = Session.class.getName();
    public static final String REQUEST_ATTRIBUTE_NAME = Request.class.getName();
    private static final long serialVersionUID = 5724886106704391903L;
    private static final String PUSH_SESSION_ID_PARAM = "pushSessionId";
    private static final Logger LOGGER = RichfacesLogger.WEBAPP.getLogger();
    private AtomicReference<SessionManager> sessionManagerReference = new AtomicReference<SessionManager>();

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            HttpServletRequest httpReq = (HttpServletRequest) request;
            HttpServletResponse httpResp = (HttpServletResponse) response;

            chain.doFilter(request, response);

            if ("GET".equals(httpReq.getMethod())) {
                Meteor meteor = Meteor.build(httpReq, SCOPE.REQUEST, Collections.<BroadcastFilter>emptyList(), null);

                String pushSessionId = httpReq.getParameter(PUSH_SESSION_ID_PARAM);

                Session session = null;

                if (pushSessionId != null) {
                    session = getSessionManager(request).getPushSession(pushSessionId);
                }

                if (session == null) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug(MessageFormat.format("Session {0} was not found", pushSessionId));
                    }
                    httpResp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                httpResp.setContentType("text/plain");

                try {
                    Request pushRequest = new RequestImpl(meteor, session);

                    httpReq.setAttribute(SESSION_ATTRIBUTE_NAME, session);
                    httpReq.setAttribute(REQUEST_ATTRIBUTE_NAME, pushRequest);

                    pushRequest.suspend();
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }

                return;
            }
        }
    }

    /**
     * Get instance of {@link SessionManager}.
     *
     * Instance of {@link SessionManager} is initialized from Current {@link SessionManager} is stored in
     * {@link #sessionManagerReference}, since it needs to be initialized lazily.
     *
     * It can be initialized by providing {@link ServletRequest}.
     */
    private SessionManager getSessionManager(ServletRequest request) {
        if (sessionManagerReference.get() == null) {
            ServletContext servletContext = request.getServletContext();
            PushContext pushContext = (PushContext) servletContext.getAttribute(PushContext.INSTANCE_KEY_NAME);
            sessionManagerReference.compareAndSet(null, pushContext.getSessionManager());
        }
        return sessionManagerReference.get();
    }

    public void destroy() {
        sessionManagerReference.set(null);
    }
}

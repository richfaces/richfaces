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
package org.richfaces.application.push.impl;

import java.util.concurrent.ThreadFactory;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PreDestroyApplicationEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import org.richfaces.application.push.PushContext;
import org.richfaces.application.push.SessionFactory;
import org.richfaces.application.push.SessionManager;
import org.richfaces.application.push.TopicsContext;
import org.richfaces.application.push.impl.jms.JMSTopicsContextImpl;
import org.richfaces.configuration.ConfigurationServiceHelper;
import org.richfaces.configuration.CoreConfiguration;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * {@inheritDoc}
 *
 * @author Nick Belaevski
 *
 * @see PushContext
 */
public class PushContextImpl implements PushContext, SystemEventListener {

    private static final ThreadFactory PUBLISH_THREAD_FACTORY = new ThreadFactoryBuilder().setDaemon(true)
            .setNameFormat("push-publish-thread-%1$s").build();
    private static final ThreadFactory SESSION_MANAGER_THREAD_FACTORY = new ThreadFactoryBuilder().setDaemon(true)
            .setNameFormat("push-session-manager-thread-%1$s").build();
    private static final Logger LOGGER = RichfacesLogger.APPLICATION.getLogger();

    private String pushHandlerUrl;
    private TopicsContextImpl topicsContext;
    private SessionManager sessionManager;
    private SessionFactory sessionFactory;

    public PushContextImpl(String pushHandlerUrl) {
        super();
        this.pushHandlerUrl = pushHandlerUrl;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.application.push.PushContext#init(javax.faces.context.FacesContext)
     */
    @Override
    public void init(FacesContext facesContext) {
        try {
            facesContext.getApplication().subscribeToEvent(PreDestroyApplicationEvent.class, this);

            boolean isJmsEnabled = isJmsEnabled(facesContext);

            if (isJmsEnabled) {
                topicsContext = JMSTopicsContextImpl.getInstanceInitializedFromContext(PUBLISH_THREAD_FACTORY, facesContext);
            } else {
                topicsContext = new TopicsContextImpl(PUBLISH_THREAD_FACTORY);
            }

            sessionManager = new SessionManagerImpl(SESSION_MANAGER_THREAD_FACTORY);

            sessionFactory = new SessionFactoryImpl(sessionManager, topicsContext);

            facesContext.getExternalContext().getApplicationMap().put(INSTANCE_KEY_NAME, this);
        } catch (Exception e) {
            throw new FacesException(e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.application.push.PushContext#destroy()
     */
    @Override
    public void destroy() {
        try {
            sessionManager.destroy();
            sessionManager = null;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        try {
            topicsContext.destroy();
            topicsContext = null;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Destroyes this push context before the application is destroyed
     */
    @Override
    public void processEvent(SystemEvent event) throws AbortProcessingException {
        if (event instanceof PreDestroyApplicationEvent) {
            destroy();
        } else {
            throw new IllegalArgumentException(event.getClass().getName());
        }
    }

    @Override
    public boolean isListenerForSource(Object source) {
        return true;
    }

    private boolean isJmsEnabled(FacesContext facesContext) {
        Boolean jmsEnabled = ConfigurationServiceHelper.getBooleanConfigurationValue(facesContext,
                CoreConfiguration.Items.pushJMSEnabled);
        jmsEnabled = (jmsEnabled == null) ? false : jmsEnabled;
        return jmsEnabled;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.application.push.PushContext#getTopicsContext()
     */
    @Override
    public TopicsContext getTopicsContext() {
        return topicsContext;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.application.push.PushContext#getSessionFactory()
     */
    @Override
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.application.push.PushContext#getSessionManager()
     */
    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    /*
     * (non-Javadoc)
     * @see org.richfaces.application.push.PushContext#getPushHandlerUrl()
     */
    @Override
    public String getPushHandlerUrl() {
        return pushHandlerUrl;
    }
}

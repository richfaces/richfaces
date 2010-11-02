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

import static org.richfaces.application.CoreConfiguration.Items.pushJMSConnectionFactory;
import static org.richfaces.application.CoreConfiguration.Items.pushJMSConnectionPassword;
import static org.richfaces.application.CoreConfiguration.Items.pushJMSConnectionUsername;
import static org.richfaces.application.CoreConfiguration.Items.pushJMSTopicsNamespace;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PreDestroyApplicationEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.naming.CompositeName;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.atmosphere.cpr.AtmosphereHandler;
import org.richfaces.application.ServiceTracker;
import org.richfaces.application.configuration.ConfigurationService;
import org.richfaces.application.push.PushContext;
import org.richfaces.application.push.SessionFactory;
import org.richfaces.application.push.SessionManager;
import org.richfaces.application.push.TopicsContext;
import org.richfaces.application.push.impl.AtmosphereHandlerProvider;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * @author Nick Belaevski
 * 
 */
public class PushContextImpl implements PushContext, SystemEventListener, AtmosphereHandlerProvider {

    private static final Logger LOGGER = RichfacesLogger.APPLICATION.getLogger();

    private MessagingContext messagingContext;

    private TopicsContext topicsContext;

    private PushHandlerImpl pushHandlerImpl;

    public TopicsContext getTopicsContext() {
        return topicsContext;
    }

    private String getApplicationName(FacesContext facesContext) {
        ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
        return servletContext.getContextPath();
    }

    public void init(FacesContext facesContext) {
        try {
            facesContext.getApplication().subscribeToEvent(PreDestroyApplicationEvent.class, this);
            facesContext.getExternalContext().getApplicationMap().put(PushContext.INSTANCE_KEY_NAME, this);

            ConfigurationService configurationService = ServiceTracker.getService(ConfigurationService.class);
            
            InitialContext initialContext = new InitialContext();
            Name cnfName = new CompositeName(configurationService.getStringValue(facesContext, pushJMSConnectionFactory));
            Name topicsNamespace = new CompositeName(configurationService.getStringValue(facesContext, pushJMSTopicsNamespace));

            messagingContext = new MessagingContext(initialContext, cnfName, topicsNamespace, 
                getApplicationName(facesContext),
                configurationService.getStringValue(facesContext, pushJMSConnectionUsername),
                configurationService.getStringValue(facesContext, pushJMSConnectionPassword));

            messagingContext.shareInstance(facesContext);

            messagingContext.start();

            topicsContext = new TopicsContextImpl(messagingContext);

            pushHandlerImpl = new PushHandlerImpl(messagingContext, topicsContext);
        } catch (Exception e) {
            throw new FacesException(e.getMessage(), e);
        }        
    }

    public void destroy() {
        if (messagingContext != null) {
            try {
                messagingContext.stop();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        if (pushHandlerImpl != null) { 
            try {
                pushHandlerImpl.destroy();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    public void processEvent(SystemEvent event) throws AbortProcessingException {
        if (event instanceof PreDestroyApplicationEvent) {
            destroy();
        } else {
            throw new IllegalArgumentException(event.getClass().getName());
        }
    }

    public boolean isListenerForSource(Object source) {
        return true;
    }

    public SessionFactory getSessionFactory() {
        return pushHandlerImpl;
    }

    public AtmosphereHandler<HttpServletRequest, HttpServletResponse> getHandler() {
        return pushHandlerImpl;
    }
    
    public SessionManager getSessionManager() {
        return pushHandlerImpl.getSessionManager();
    }
}

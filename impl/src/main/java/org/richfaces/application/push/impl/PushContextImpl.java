/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

import static org.richfaces.application.CoreConfiguration.Items.pushJMSConnectionFactory;
import static org.richfaces.application.CoreConfiguration.Items.pushJMSConnectionPassword;
import static org.richfaces.application.CoreConfiguration.Items.pushJMSConnectionPasswordEnvRef;
import static org.richfaces.application.CoreConfiguration.Items.pushJMSConnectionUsername;
import static org.richfaces.application.CoreConfiguration.Items.pushJMSConnectionUsernameEnvRef;
import static org.richfaces.application.CoreConfiguration.Items.pushJMSTopicsNamespace;
import static org.richfaces.application.CoreConfiguration.PushPropertiesItems.pushPropertiesJMSConnectionFactory;
import static org.richfaces.application.CoreConfiguration.PushPropertiesItems.pushPropertiesJMSConnectionPassword;
import static org.richfaces.application.CoreConfiguration.PushPropertiesItems.pushPropertiesJMSConnectionUsername;
import static org.richfaces.application.CoreConfiguration.PushPropertiesItems.pushPropertiesJMSTopicsNamespace;

import java.util.concurrent.ThreadFactory;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PreDestroyApplicationEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NameParser;

import org.richfaces.application.ServiceTracker;
import org.richfaces.application.configuration.ConfigurationService;
import org.richfaces.application.push.PushContext;
import org.richfaces.application.push.SessionFactory;
import org.richfaces.application.push.SessionManager;
import org.richfaces.application.push.TopicsContext;
import org.richfaces.application.push.impl.jms.JMSTopicsContextImpl;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

import com.google.common.base.Strings;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * @author Nick Belaevski
 *
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

    private String getFirstNonEmptyConfgirutationValue(FacesContext facesContext, ConfigurationService service, Enum<?>... keys) {
        for (Enum<?> key : keys) {
            String value = service.getStringValue(facesContext, key);
            if (!Strings.isNullOrEmpty(value)) {
                return value;
            }
        }

        return "";
    }

    private String getConnectionFactory(FacesContext facesContext, ConfigurationService configurationService) {
        return getFirstNonEmptyConfgirutationValue(facesContext, configurationService, pushPropertiesJMSConnectionFactory,
            pushJMSConnectionFactory);
    }

    private String getTopicsNamespace(FacesContext facesContext, ConfigurationService configurationService) {
        return getFirstNonEmptyConfgirutationValue(facesContext, configurationService, pushPropertiesJMSTopicsNamespace,
            pushJMSTopicsNamespace);
    }

    private String getPassword(FacesContext facesContext, ConfigurationService configurationService) {
        return getFirstNonEmptyConfgirutationValue(facesContext, configurationService, pushPropertiesJMSConnectionPassword,
            pushJMSConnectionPasswordEnvRef, pushJMSConnectionPassword);
    }

    private String getUserName(FacesContext facesContext, ConfigurationService configurationService) {
        return getFirstNonEmptyConfgirutationValue(facesContext, configurationService, pushPropertiesJMSConnectionUsername,
            pushJMSConnectionUsernameEnvRef, pushJMSConnectionUsername);
    }

    public void init(FacesContext facesContext) {
        try {
            facesContext.getApplication().subscribeToEvent(PreDestroyApplicationEvent.class, this);

            ConfigurationService configurationService = ServiceTracker.getService(ConfigurationService.class);

            InitialContext initialContext = new InitialContext();

            NameParser nameParser = initialContext.getNameParser("");

            Name cnfName = nameParser.parse(getConnectionFactory(facesContext, configurationService));
            Name topicsNamespace = nameParser.parse(getTopicsNamespace(facesContext, configurationService));

            sessionManager = new SessionManagerImpl(SESSION_MANAGER_THREAD_FACTORY);
            topicsContext = new JMSTopicsContextImpl(PUBLISH_THREAD_FACTORY, initialContext, cnfName, topicsNamespace,
                getUserName(facesContext, configurationService), getPassword(facesContext, configurationService));
            sessionFactory = new SessionFactoryImpl(sessionManager, topicsContext);

            facesContext.getExternalContext().getApplicationMap().put(INSTANCE_KEY_NAME, this);
        } catch (Exception e) {
            throw new FacesException(e.getMessage(), e);
        }
    }

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

    public TopicsContext getTopicsContext() {
        return topicsContext;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public String getPushHandlerUrl() {
        return pushHandlerUrl;
    }
}

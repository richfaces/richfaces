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
package org.richfaces.application;

import static org.richfaces.application.configuration.ConfigurationServiceHelper.getBooleanConfigurationValue;
import static org.richfaces.application.CoreConfiguration.Items.executeAWTInitializer;
import static org.richfaces.application.CoreConfiguration.Items.pushInitializePushContextOnStartup;
import static org.richfaces.application.CoreConfiguration.Items.pushJMSEnabled;

import java.awt.Toolkit;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostConstructApplicationEvent;
import javax.faces.event.PreDestroyApplicationEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.xml.rpc.ServiceFactory;

import org.richfaces.VersionBean;
import org.richfaces.application.push.PushContextFactory;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

/**
 * <p>Listens for application's {@link PostConstructApplicationEvent} and {@link PreDestroyApplicationEvent} events in order to initialize RichFaces services.</p>
 *
 * @author Nick Belaevski
 * @since 4.0
 */
public class InitializationListener implements SystemEventListener {
    private static final Logger LOGGER = RichfacesLogger.APPLICATION.getLogger();

    /**
     * Called on application startup
     */
    protected void onStart() {
        JsfVersionInspector enforcer = new JsfVersionInspector();
        if (!enforcer.verifyJsfImplVersion()) {
            throw new RuntimeException(String.format("The JSF implementation %s does not support the RichFaces ExtendedPartialViewContext.  Please upgrade to at least Mojarra 2.1.28 or 2.2.6", enforcer.getVersionString()));
        }
        createFactory();

        if (LOGGER.isInfoEnabled()) {
            String versionString = VersionBean.VERSION.toString();
            if (versionString != null && versionString.length() != 0) {
                LOGGER.info(versionString);
            }
        }

        if (getConfiguration(executeAWTInitializer)) {
            initializeAWT();
        }

        boolean jmsEnabled = getConfiguration(pushJMSEnabled) != null && getConfiguration(pushJMSEnabled);

        if (jmsEnabled || getConfiguration(pushInitializePushContextOnStartup)) {
            initializePushContext();
        }

        if (!jmsEnabled) {
            logWarningWhenConnectionFactoryPresent();
        }
    }

    /**
     * Called when application is being destroyed
     */
    protected void onStop() {
        ServiceTracker.release();
    }

    /**
     * Creates {@link ServiceFactory} that holds references to RichFaces application singletons
     */
    protected ServicesFactory createFactory() {
        ServicesFactoryImpl injector = new ServicesFactoryImpl();
        ServiceTracker.setFactory(injector);
        ArrayList<Module> modules = new ArrayList<Module>();
        addDefaultModules(modules);
        try {
            modules.addAll(ServiceLoader.loadServices(Module.class));
            injector.init(modules);
        } catch (ServiceException e) {
            throw new FacesException(e);
        }
        return injector;
    }

    /**
     * Adds default application modules
     */
    protected void addDefaultModules(List<Module> modules) {
        modules.add(new DefaultModule());
    }

    /**
     * Initializes AWT for dynamic image rendering
     */
    private void initializeAWT() {
        try {
            AWTInitializer.initialize();
        } catch (NoClassDefFoundError e) {
            LOGGER.warn(MessageFormat.format("There were problems loading class: {0} - AWTInitializer won't be run",
                    e.getMessage()));
        }
    }

    /**
     * Initializes {@link org.richfaces.application.push.PushContext} on startup as it is a time when {@link FacesContext} is available
     */
    private void initializePushContext() {
        try {
            LOGGER.info("Startup initialization of PushContext");
            ServiceTracker.getService(PushContextFactory.class).getPushContext();
        } catch (Exception e) {
            LOGGER.error(MessageFormat.format("There were problems initializing PushContext on startup: {0}", e.getMessage()));
        }
    }

    /**
     * Logs a warning that JMS API is available but Push/JMS integration is not enabled
     */
    private void logWarningWhenConnectionFactoryPresent() {
        try {
            Class.forName("javax.jms.ConnectionFactory");
            LOGGER.warn("JMS API was found on the classpath; if you want to enable RichFaces Push JMS integration, set context-param 'org.richfaces.push.jms.enabled' in web.xml");
        } catch (ClassNotFoundException e) {
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.event.SystemEventListener#processEvent(javax.faces.event.SystemEvent)
     */
    @Override
    public void processEvent(SystemEvent event) throws AbortProcessingException {
        if (event instanceof PostConstructApplicationEvent) {
            onStart();
        } else if (event instanceof PreDestroyApplicationEvent) {
            onStop();
        } else {
            throw new IllegalArgumentException(MessageFormat.format("Event {0} is not supported!", event));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.event.SystemEventListener#isListenerForSource(java.lang.Object)
     */
    @Override
    public boolean isListenerForSource(Object source) {
        return true;
    }

    private Boolean getConfiguration(Enum<?> configuration) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return getBooleanConfigurationValue(facesContext, configuration);
    }

    private static final class AWTInitializer {
        private AWTInitializer() {
        }

        private static boolean checkGetSystemClassLoaderAccess() {
            try {
                ClassLoader.getSystemClassLoader();

                return true;
            } catch (SecurityException e) {
                return false;
            }
        }

        public static void initialize() {
            if (!checkGetSystemClassLoaderAccess()) {
                LOGGER.warn("Access to system class loader restricted - AWTInitializer won't be run");
                return;
            }

            Thread thread = Thread.currentThread();
            ClassLoader initialTCCL = thread.getContextClassLoader();
            ImageInputStream testStream = null;

            try {
                ClassLoader systemCL = ClassLoader.getSystemClassLoader();
                thread.setContextClassLoader(systemCL);
                // set in-memory caching ImageIO
                ImageIO.setUseCache(false);

                // force Disposer/AWT threads initialization
                testStream = ImageIO.createImageInputStream(new ByteArrayInputStream(new byte[0]));
                Toolkit.getDefaultToolkit().getSystemEventQueue();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            } finally {
                if (testStream != null) {
                    try {
                        testStream.close();
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                }

                thread.setContextClassLoader(initialTCCL);
            }
        }
    }
}

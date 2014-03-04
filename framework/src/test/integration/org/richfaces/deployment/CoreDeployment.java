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
package org.richfaces.deployment;

import static org.richfaces.deployment.CoreFeature.CONFIGURATION_SERVICE;
import static org.richfaces.deployment.CoreFeature.DEPENDENCY_INJECTOR;
import static org.richfaces.deployment.CoreFeature.RESOURCE_CODEC;
import static org.richfaces.deployment.CoreFeature.RESOURCE_HANDLER;
import static org.richfaces.deployment.CoreFeature.SERVICE_LOADER;

import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.component.UIOutput;
import javax.faces.event.PostConstructApplicationEvent;
import javax.faces.event.PreDestroyApplicationEvent;
import javax.servlet.ServletContainerInitializer;

import org.jboss.arquillian.container.test.spi.RemoteLoadableExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.facesconfig20.WebFacesConfigDescriptor;
import org.richfaces.VersionBean;
import org.richfaces.base64.Codec;
import org.richfaces.cache.Cache;
import org.richfaces.configuration.ConfigurationItem;
import org.richfaces.configuration.ConfigurationItemSource;
import org.richfaces.configuration.ConfigurationItemsBundle;
import org.richfaces.configuration.ConfigurationService;
import org.richfaces.configuration.ConfigurationServiceHelper;
import org.richfaces.configuration.ConfigurationServiceImpl;
import org.richfaces.configuration.CoreConfiguration;
import org.richfaces.configuration.ValueExpressionHolder;
import org.richfaces.context.AjaxDataSerializer;
import org.richfaces.el.GenericsIntrospectionService;
import org.richfaces.javascript.JavaScriptService;
import org.richfaces.l10n.BundleLoader;
import org.richfaces.l10n.InterpolationException;
import org.richfaces.l10n.MessageBundle;
import org.richfaces.l10n.MessageFactory;
import org.richfaces.l10n.MessageInterpolator;
import org.richfaces.log.JavaLogger;
import org.richfaces.log.LogFactory;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.push.AbstractTopic;
import org.richfaces.push.DefaultMessageDataSerializer;
import org.richfaces.push.JMSTopicsContextImpl;
import org.richfaces.push.MessageDataScriptString;
import org.richfaces.push.MessageDataSerializer;
import org.richfaces.push.MessageException;
import org.richfaces.push.PushContext;
import org.richfaces.push.PushContextFactory;
import org.richfaces.push.PushContextFactoryImpl;
import org.richfaces.push.PushContextImpl;
import org.richfaces.push.PushContextInitializationException;
import org.richfaces.push.Request;
import org.richfaces.push.RequestImpl;
import org.richfaces.push.Session;
import org.richfaces.push.SessionFactory;
import org.richfaces.push.SessionFactoryImpl;
import org.richfaces.push.SessionImpl;
import org.richfaces.push.SessionManager;
import org.richfaces.push.SessionManagerImpl;
import org.richfaces.push.SessionQueue;
import org.richfaces.push.SessionSubscriptionEvent;
import org.richfaces.push.SessionUnsubscriptionEvent;
import org.richfaces.push.Topic;
import org.richfaces.push.TopicEvent;
import org.richfaces.push.TopicImpl;
import org.richfaces.push.TopicKey;
import org.richfaces.push.TopicListener;
import org.richfaces.push.TopicsContext;
import org.richfaces.push.TopicsContextImpl;
import org.richfaces.resource.DefaultResourceCodec;
import org.richfaces.resource.ResourceCodec;
import org.richfaces.resource.ResourceHandlerImpl;
import org.richfaces.resource.ResourceLibrary;
import org.richfaces.resource.ResourceLibraryFactory;
import org.richfaces.resource.ResourceLibraryFactoryImpl;
import org.richfaces.resource.StaticResourceLibrary;
import org.richfaces.resource.external.ResourceTracker;
import org.richfaces.resource.external.ResourceTrackerImpl;
import org.richfaces.resource.external.MappedResourceFactory;
import org.richfaces.resource.external.MappedResourceFactoryImpl;
import org.richfaces.services.DependencyInjectorImpl;
import org.richfaces.services.DependencyInjector;
import org.richfaces.services.Initializable;
import org.richfaces.services.InitializationListener;
import org.richfaces.services.Module;
import org.richfaces.services.ServiceException;
import org.richfaces.services.ServiceLoader;
import org.richfaces.services.ServiceTracker;
import org.richfaces.services.ServicesFactory;
import org.richfaces.services.ServicesFactoryImpl;
import org.richfaces.services.Uptime;
import org.richfaces.servlet.AbstractServletContainerInitializer;
import org.richfaces.servlet.PushFilter;
import org.richfaces.servlet.PushHandlerFilter;
import org.richfaces.servlet.PushServlet;
import org.richfaces.servlet.PushServletContainerInitializer;
import org.richfaces.servlet.ServletConfigDefaultsFacade;
import org.richfaces.shrinkwrap.descriptor.PropertiesAsset;
import org.richfaces.skin.SkinFactory;
import org.richfaces.ui.core.ResourceLibraryRenderer;
import org.richfaces.ui.core.ResourceRenderer;
import org.richfaces.ui.core.UIResource;
import org.richfaces.ui.core.UITransient;
import org.richfaces.util.PropertiesUtil;
import org.richfaces.wait.Condition;
import org.richfaces.wait.Wait;
import org.richfaces.wait.WaitTimeoutException;

import com.google.common.base.Function;

/**
 * Provides base for Core test deployment
 *
 * @author Lukas Fryc
 */
public class CoreDeployment extends Deployment {

    private static final String TESTING_MODULE = "META-INF/richfaces/testing-module.properties";

    private PropertiesAsset testingModules = new PropertiesAsset();
    private EnumSet<CoreFeature> addedFeatures = EnumSet.noneOf(CoreFeature.class);

    /**
     * Constructs base Core deployment with dependencies, base classes and utilities.
     */
    public CoreDeployment(Class<?> testClass) {
        super(testClass);

        this.withBaseClasses().withUtilities().withLogging();

        this.withArquillianExtensions().withWaiting();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.deployment.Deployment#getFinalArchive()
     */
    @Override
    public WebArchive getFinalArchive() {
        WebArchive finalArchive = super.getFinalArchive();

        finalArchive.addManifest();
        Asset manifest = finalArchive.get("META-INF/MANIFEST.MF").getAsset();
        finalArchive.addAsResource(manifest, "META-INF/MANIFEST.MF");

        // add testing modules list
        finalArchive.addAsResource(testingModules, TESTING_MODULE);

        return finalArchive;
    }

    /**
     * Avoids to load given feature several times.
     *
     * Adds given {@link CoreFeature} to the list of loaded features.
     *
     * Returns whether the given feature was already loaded.
     *
     * @param feature a feature to load
     * @return true if a given feature was already loaded; false otherwise
     */
    private boolean addFeature(CoreFeature feature) {
        boolean alreadyAdded = addedFeatures.contains(feature);
        addedFeatures.add(feature);
        return alreadyAdded;
    }

    /**
     * Adds base classes which are necessary for each Core test
     */
    public CoreDeployment withBaseClasses() {
        archive().addClasses(VersionBean.class);
        return this;
    }

    /**
     * Adds all utility classes
     */
    public CoreDeployment withUtilities() {
        archive().addPackages(true, "org.richfaces.util");
        return this;
    }

    /**
     * Adds expression language classes
     */
    public CoreDeployment withEL() {
        archive().addPackages(true, "org.richfaces.el");
        return this;
    }

    /**
     * Adds {@link ConfigurationService} service
     */
    public CoreDeployment withConfigurationService() {
        if (addFeature(CONFIGURATION_SERVICE)) {
            return this;
        }

        withService(ConfigurationService.class, ConfigurationServiceImpl.class);
        archive()
            .addClasses(ConfigurationService.class, ConfigurationServiceImpl.class)
            .addClasses(ConfigurationServiceHelper.class)
            .addClasses(ConfigurationItem.class, ConfigurationItemsBundle.class, ConfigurationItemSource.class)
            .addClasses(ValueExpressionHolder.class);
        return this;
    }

    /**
     * Adds {@link DependencyInjector} service
     */
    public CoreDeployment withDependencyInjector() {
        if (addFeature(DEPENDENCY_INJECTOR)) {
            return this;
        }

        withService(DependencyInjector.class, DependencyInjectorImpl.class);
        archive().addClasses(DependencyInjectorImpl.class);

        return this;
    }

    /**
     * Adds core localization classes
     */
    public CoreDeployment withLocalization() {
        if (addFeature(CoreFeature.LOCALIZATION)) {
            return this;
        }

        archive()
            .addClasses(BundleLoader.class, InterpolationException.class, MessageBundle.class, MessageInterpolator.class);


        return this;
    }

    /**
     * Adds {@link ResourceCodec} service
     * @return
     */
    public CoreDeployment withResourceCodec() {
        if (addFeature(RESOURCE_CODEC)) {
            return this;
        }

        withService(ResourceCodec.class, DefaultResourceCodec.class);
        archive().addClasses(ResourceCodec.class, DefaultResourceCodec.class);

        return this;
    }

    /**
     * Adds Push and all its dependencies
     */
    public CoreDeployment withPush() {
        if (addFeature(CoreFeature.PUSH)) {
            return this;
        }

        withService(PushContextFactory.class, PushContextFactoryImpl.class);

        StringAsset pushServletInitializer = new StringAsset(PushServletContainerInitializer.class.getName());

        // According to ServletContainerInitializer, the service must be defined in own JAR
        JavaArchive servletInitializer = ShrinkWrap.create(JavaArchive.class, "push-classes.jar")
                .addAsResource(pushServletInitializer, "META-INF/services/" + ServletContainerInitializer.class.getName())
                .addClasses(PushServlet.class, PushFilter.class, PushHandlerFilter.class, ServletConfigDefaultsFacade.class)
                .addClasses(PushServletContainerInitializer.class, AbstractServletContainerInitializer.class)
                .addClasses(TopicsContext.class, TopicsContextImpl.class)
                .addClasses(SessionManager.class, SessionManagerImpl.class, SessionQueue.class)
                .addClasses(SessionFactory.class, SessionFactoryImpl.class)
                .addClasses(Session.class, SessionImpl.class)
                .addClasses(Topic.class, TopicImpl.class, AbstractTopic.class, TopicKey.class, TopicEvent.class, TopicListener.class)
                .addClasses(MessageDataSerializer.class, DefaultMessageDataSerializer.class, MessageDataScriptString.class)
                .addClasses(Request.class, RequestImpl.class)
                .addClasses(PushContext.class, PushContextImpl.class)
                .addClasses(PushContextFactory.class, PushContextFactoryImpl.class)
                .addClasses(SessionSubscriptionEvent.class, SessionUnsubscriptionEvent.class)
                .addClasses(MessageException.class)
                .addClasses(JMSTopicsContextImpl.class)
                .addClasses(PushContextInitializationException.class);


        archive()
            .addAsLibrary(servletInitializer);

        addMavenDependency(
                "org.atmosphere:atmosphere-runtime");

        return this;
    }

    /**
     * Adds Resource Libraries feature
     * @return
     */
    public CoreDeployment withResourceLibraries() {
        if (addFeature(CoreFeature.RESOURCE_LIBRARIES)) {
            return this;
        }

        archive()
            .addClasses(ResourceLibrary.class, ResourceLibraryFactory.class, ResourceLibraryFactoryImpl.class)
            .addClasses(ResourceLibraryRenderer.class, StaticResourceLibrary.class)
            .addClasses(ResourceRenderer.class, UIResource.class, UITransient.class);

        withService(ResourceLibraryFactory.class, ResourceLibraryFactoryImpl.class);

        facesConfig(new Function<WebFacesConfigDescriptor, WebFacesConfigDescriptor>() {

            @Override
            public WebFacesConfigDescriptor apply(WebFacesConfigDescriptor input) {
                return input
                        .getOrCreateRenderKit()
                            .createRenderer()
                                .componentFamily(UIOutput.COMPONENT_FAMILY)
                                .rendererType(ResourceLibraryRenderer.RENDERER_TYPE)
                                .rendererClass(ResourceLibraryRenderer.class.getName())
                            .up()
                        .up();
            }
        });

        return this;
    }

    /**
     * Adds core logging classes
     */
    public CoreDeployment withLogging() {
        if (addFeature(CoreFeature.LOGGING)) {
            return this;
        }

        archive()
            // log
            .addClasses(RichfacesLogger.class, JavaLogger.class, LogFactory.class, Logger.class);

        this.withLocalization();

        return this;
    }

    /**
     * Adds {@link ResourceHandlerImpl} and its dependencies
     */
    public CoreDeployment withResourceHandler() {
        if (addFeature(RESOURCE_HANDLER)) {
            return this;
        }

        archive()
            // resource
            .addPackage("org.richfaces.resource")
            .addPackage("org.richfaces.resource.css")
            .addPackage("org.richfaces.resource.external")
            // codec
            .addClasses(Codec.class)
            // cache
            .addClasses(Cache.class);

        withService(MappedResourceFactory.class, MappedResourceFactoryImpl.class);
        withService(ResourceTracker.class, ResourceTrackerImpl.class);

        facesConfig(new Function<WebFacesConfigDescriptor, WebFacesConfigDescriptor>() {

            @Override
            public WebFacesConfigDescriptor apply(WebFacesConfigDescriptor facesConfig) {
                return facesConfig
                    .getOrCreateApplication()
                        .resourceHandler(ResourceHandlerImpl.class.getName())
                    .up();
            }
        });

        return this;
    }

    public CoreDeployment withWholeCore() {
        JavaArchive coreArchive = ShrinkWrap.create(JavaArchive.class, "richfaces.jar");
        coreArchive.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
            .importDirectory("target/classes/").as(GenericArchive.class),
            "/", Filters.includeAll());
        archive().addAsLibrary(coreArchive);

        return this;
    }

    /**
     * Adds {@link CoreTestingRemoteExtension} (for testing Core with Arquillian) and its dependencies
     * @return
     */
    private CoreDeployment withArquillianExtensions() {
        archive().addAsServiceProvider(RemoteLoadableExtension.class, CoreTestingRemoteExtension.class);
        archive().addClasses(CoreTestingRemoteExtension.class, CoreServicesEnricher.class);
        archive().addClasses(ConfigurationService.class, SkinFactory.class, AjaxDataSerializer.class,
                ResourceCodec.class, Cache.class, Uptime.class, DependencyInjector.class, MessageFactory.class,
                ResourceLibraryFactory.class, PushContextFactory.class, JavaScriptService.class,
                GenericsIntrospectionService.class, ResourceTracker.class, MappedResourceFactory.class);
        return this;
    }

    /**
     * Adds base classes for using waiting with timeouts
     */
    private CoreDeployment withWaiting() {
        archive().addClasses(Condition.class, Wait.class, WaitTimeoutException.class);
        return this;
    }

    /**
     * Adds service for given type and implementation
     */
    public CoreDeployment withService(Class<?> type, Class<?> implementation) {
        withServiceTracker();
        testingModules = testingModules.key(type.getName()).value(implementation.getName());
        return this;
    }

    /**
     * Adds {@link ServiceTracker}, its initializer and module which is able to load services added by
     * {@link #withService(Class, Class)}.
     */
    public CoreDeployment withServiceTracker() {
        if (addFeature(SERVICE_LOADER)) {
            return this;
        }

        // add base dependencies
        archive()
            .addClasses(InitializationListener.class, CoreConfiguration.class, ServicesFactory.class, ServicesFactoryImpl.class)
            .addClasses(ServiceLoader.class, ServiceException.class, ServiceTracker.class, Module.class, Initializable.class);

        withConfigurationService();

        // register testing module
        StringAsset moduleAsset = new StringAsset(TestingModule.class.getName());

        archive().addClass(TestingModule.class).addAsResource(moduleAsset, "META-INF/services/" + Module.class.getName());

        // register initialization listener
        archive().addClass(TestingInitializationListener.class);
        facesConfig(new Function<WebFacesConfigDescriptor, WebFacesConfigDescriptor>() {

            @Override
            public WebFacesConfigDescriptor apply(WebFacesConfigDescriptor facesConfig) {
                return facesConfig.getOrCreateApplication().createSystemEventListener()
                        .systemEventClass(PostConstructApplicationEvent.class.getName())
                        .systemEventListenerClass(TestingInitializationListener.class.getName()).up()
                        .createSystemEventListener().systemEventClass(PreDestroyApplicationEvent.class.getName())
                        .systemEventListenerClass(TestingInitializationListener.class.getName()).up().up();
            }
        });

        return this;
    }

    /**
     * Initialization listener which does not load any module by default, and delegates rather to modules added by
     * {@link TestingModule}.
     */
    public static class TestingInitializationListener extends InitializationListener {
        @Override
        protected void addDefaultModules(List<Module> modules) {
        }
    }

    /**
     * Loads the list of services from properties file.
     *
     * You can add those using {@link CoreDeployment#withService(Class, Class)}.
     */
    public static class TestingModule implements Module {
        @Override
        public void configure(ServicesFactory factory) {
            ClassLoader classLoader = Module.class.getClassLoader();

            Map<String, String> map = PropertiesUtil.loadProperties(TESTING_MODULE);

            for (Entry<String, String> entry : map.entrySet()) {
                try {
                    Class<?> factoryClass = classLoader.loadClass(entry.getKey());
                    Class<?> implementationClass = classLoader.loadClass(entry.getValue());
                    Object implementationInstance = implementationClass.newInstance();

                    Method method = factory.getClass().getMethod("setInstance", new Class<?>[] { Class.class, Object.class });
                    method.invoke(factory, factoryClass, implementationInstance);
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }
        }
    }
}
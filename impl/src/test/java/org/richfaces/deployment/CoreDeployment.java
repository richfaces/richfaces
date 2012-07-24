/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
import static org.richfaces.deployment.CoreFeature.SERVICE_LOADER;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.event.PostConstructApplicationEvent;
import javax.faces.event.PreDestroyApplicationEvent;

import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.facesconfig20.WebFacesConfigDescriptor;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.richfaces.VersionBean;
import org.richfaces.application.CoreConfiguration;
import org.richfaces.application.InitializationListener;
import org.richfaces.application.Module;
import org.richfaces.application.ServiceLoader;
import org.richfaces.application.ServiceTracker;
import org.richfaces.application.ServicesFactory;
import org.richfaces.application.ServicesFactoryImpl;
import org.richfaces.application.configuration.ConfigurationService;
import org.richfaces.application.configuration.ConfigurationServiceImpl;
import org.richfaces.shrinkwrap.descriptor.PropertiesAsset;
import org.richfaces.util.PropertiesUtil;

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

        Collection<GenericArchive> coreDependencies = DependencyResolvers
                .use(MavenDependencyResolver.class)
                .loadEffectivePom("pom.xml")
                .artifacts("org.richfaces.core:richfaces-core-api", "com.google.guava:guava",
                        "net.sourceforge.cssparser:cssparser:0.9.5", "org.w3c.css:sac:1.3").resolveAs(GenericArchive.class);

        archive().addAsLibraries(coreDependencies);

        this.withBaseClasses().withUtilities();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.deployment.Deployment#getFinalArchive()
     */
    @Override
    public WebArchive getFinalArchive() {
        return super.getFinalArchive().addAsResource(testingModules, TESTING_MODULE);
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
        archive().addPackage(PropertiesUtil.class.getPackage());
        return this;
    }

    /**
     * Adds configuration service
     */
    public CoreDeployment withConfigurationService() {
        if (addFeature(CONFIGURATION_SERVICE)) {
            return this;
        }

        withService(ConfigurationService.class, ConfigurationServiceImpl.class);
        archive().addPackage(ConfigurationServiceImpl.class.getPackage());
        return this;
    }

    /**
     * Adds service for given type and implementation
     */
    public CoreDeployment withService(Class<?> type, Class<?> implementation) {
        withServiceLoader();
        testingModules = testingModules.key(type.getName()).value(implementation.getName());
        return this;
    }

    /**
     * Adds {@link ServiceTracker}, its initializer and module which is able to load services added by
     * {@link #withService(Class, Class)}.
     */
    public CoreDeployment withServiceLoader() {
        if (addFeature(SERVICE_LOADER)) {
            return this;
        }

        // add base dependencies
        archive().addClasses(InitializationListener.class, CoreConfiguration.class, ServicesFactoryImpl.class,
                ServiceLoader.class);
        withServiceLoader().withConfigurationService();

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
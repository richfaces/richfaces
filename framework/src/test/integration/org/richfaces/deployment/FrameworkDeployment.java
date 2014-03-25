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

import java.io.File;
import java.lang.reflect.Method;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.spi.RemoteLoadableExtension;
import org.jboss.arquillian.core.spi.Validate;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.richfaces.application.push.PushContextFactory;
import org.richfaces.cache.Cache;
import org.richfaces.configuration.ConfigurationService;
import org.richfaces.context.AjaxDataSerializer;
import org.richfaces.el.GenericsIntrospectionService;
import org.richfaces.javascript.JavaScriptService;
import org.richfaces.l10n.MessageFactory;
import org.richfaces.resource.ResourceCodec;
import org.richfaces.resource.ResourceLibraryFactory;
import org.richfaces.resource.external.MappedResourceFactory;
import org.richfaces.resource.external.ResourceTracker;
import org.richfaces.services.DependencyInjector;
import org.richfaces.services.Uptime;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;
import org.richfaces.skin.SkinFactory;
import org.richfaces.wait.Condition;
import org.richfaces.wait.Wait;
import org.richfaces.wait.WaitTimeoutException;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

public class FrameworkDeployment extends org.richfaces.deployment.Deployment {

    private static Supplier<JavaArchive> RICHFACES_JAR = Suppliers.memoize(new Supplier<JavaArchive>() {
        @Override
        public JavaArchive get() {
            JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "richfaces.jar");
            jar.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
                    .importDirectory("target/classes/").as(GenericArchive.class),
                    "/", Filters.includeAll());
            return jar;
        }
    });

    public FrameworkDeployment(Class<?> testClass) {
        super(testClass);

        Validate.notNull(testClass, "testClass can't be null");

        withWholeFramework();
        withWaiting();

        if (hasTestableDeployment(testClass)) {
            withArquillianExtensions();
        }

        // prevents scanning of inner classes
        archive().addAsWebInfResource(new File("src/test/resources/beans.xml"));
    }

    public void withWholeFramework() {
        archive().addAsLibrary(RICHFACES_JAR.get());
    }

    /**
     * Adds {@link CoreTestingRemoteExtension} (for testing Core with Arquillian) and its dependencies
     * @return
     */
    private void withArquillianExtensions() {
        archive().addAsServiceProvider(RemoteLoadableExtension.class, CoreTestingRemoteExtension.class);
        archive().addClasses(CoreTestingRemoteExtension.class, CoreServicesEnricher.class);
        archive().addClasses(ConfigurationService.class, SkinFactory.class, AjaxDataSerializer.class,
                ResourceCodec.class, Cache.class, Uptime.class, DependencyInjector.class, MessageFactory.class,
                ResourceLibraryFactory.class, PushContextFactory.class, JavaScriptService.class,
                GenericsIntrospectionService.class, ResourceTracker.class, MappedResourceFactory.class);
    }

    private void withWaiting() {
        archive().addClasses(Condition.class, Wait.class, WaitTimeoutException.class);
    }

    public FaceletAsset baseFacelet(String name) {
        FaceletAsset p = new FaceletAsset();

        this.archive().add(p, name);

        return p;
    }

    private boolean hasTestableDeployment(Class<?> clazz) {
        TestClass testClass = new TestClass(clazz);
        Method method = testClass.getMethod(Deployment.class);
        Deployment deployment = method.getAnnotation(Deployment.class);
        return deployment.testable();
    }
}

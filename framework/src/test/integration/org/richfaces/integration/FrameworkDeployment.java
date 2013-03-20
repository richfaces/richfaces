package org.richfaces.integration;

import org.jboss.arquillian.container.test.spi.RemoteLoadableExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.richfaces.application.DependencyInjector;
import org.richfaces.application.MessageFactory;
import org.richfaces.application.Uptime;
import org.richfaces.application.configuration.ConfigurationService;
import org.richfaces.application.push.PushContextFactory;
import org.richfaces.cache.Cache;
import org.richfaces.el.GenericsIntrospectionService;
import org.richfaces.integration.wait.Condition;
import org.richfaces.integration.wait.Wait;
import org.richfaces.integration.wait.WaitTimeoutException;
import org.richfaces.javascript.JavaScriptService;
import org.richfaces.resource.ResourceCodec;
import org.richfaces.resource.ResourceLibraryFactory;
import org.richfaces.resource.external.ExternalResourceTracker;
import org.richfaces.resource.external.ExternalStaticResourceFactory;
import org.richfaces.skin.SkinFactory;
import org.richfaces.ui.ajax.AjaxDataSerializer;

public class FrameworkDeployment extends Deployment {

    public FrameworkDeployment(Class<?> testClass) {
        super(testClass);

        withWholeFramework();
        withArquillianExtensions();
        withWaiting();
    }

    public void withWholeFramework() {
        JavaArchive coreArchive = ShrinkWrap.create(JavaArchive.class, "richfaces-framework.jar");
        coreArchive.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
            .importDirectory("target/classes/").as(GenericArchive.class),
            "/", Filters.includeAll());
        archive().addAsLibrary(coreArchive);
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
                GenericsIntrospectionService.class, ExternalResourceTracker.class, ExternalStaticResourceFactory.class);
    }

    private void withWaiting() {
        archive().addClasses(Condition.class, Wait.class, WaitTimeoutException.class);
    }

}

package org.richfaces.deployment;

import java.io.File;

import org.jboss.arquillian.container.test.spi.RemoteLoadableExtension;
import org.richfaces.cache.Cache;
import org.richfaces.configuration.ConfigurationService;
import org.richfaces.context.AjaxDataSerializer;
import org.richfaces.el.GenericsIntrospectionService;
import org.richfaces.javascript.JavaScriptService;
import org.richfaces.l10n.MessageFactory;
import org.richfaces.push.PushContextFactory;
import org.richfaces.resource.ResourceCodec;
import org.richfaces.resource.ResourceLibraryFactory;
import org.richfaces.resource.external.ExternalResourceTracker;
import org.richfaces.resource.external.ExternalStaticResourceFactory;
import org.richfaces.services.DependencyInjector;
import org.richfaces.services.Uptime;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;
import org.richfaces.skin.SkinFactory;
import org.richfaces.wait.Condition;
import org.richfaces.wait.Wait;
import org.richfaces.wait.WaitTimeoutException;

public class FrameworkDeployment extends Deployment {

    public FrameworkDeployment(Class<?> testClass) {
        super(testClass);

        withWholeFramework();
        withArquillianExtensions();
        withWaiting();

        // prevents scanning of inner classes
        archive().addAsWebInfResource(new File("src/test/resources/beans.xml"));
    }

    public void withWholeFramework() {
        archive().addAsLibrary(new File("target/richfaces.jar"));
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

    public FaceletAsset baseFacelet(String name) {
        FaceletAsset p = new FaceletAsset();

        this.archive().add(p, name);

        return p;
    }
}

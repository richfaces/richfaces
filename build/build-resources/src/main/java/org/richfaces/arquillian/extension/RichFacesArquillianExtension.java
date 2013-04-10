package org.richfaces.arquillian.extension;

import org.jboss.arquillian.core.spi.LoadableExtension;
import org.jboss.arquillian.drone.spi.Configurator;
import org.jboss.arquillian.drone.spi.Destructor;
import org.jboss.arquillian.drone.spi.Instantiator;
import org.jboss.arquillian.test.spi.enricher.resource.ResourceProvider;
import org.richfaces.arquillian.browser.FailureScreenshot;
import org.richfaces.arquillian.browser.PrepareBrowserSession;
import org.richfaces.arquillian.browser.ScreenshotTaker;
import org.richfaces.arquillian.configuration.FundamentalTestConfiguratorObserver;
import org.richfaces.arquillian.container.installation.ContainerInitializationObserver;
import org.richfaces.arquillian.container.installation.ContainerInstaller;
import org.richfaces.arquillian.drone.ResolvingPhantomJSDriverFactory;
import org.richfaces.arquillian.page.source.SourceChecker;
import org.richfaces.arquillian.page.source.SourceCheckerProvider;
;

public class RichFacesArquillianExtension implements LoadableExtension {

    @Override
    public void register(ExtensionBuilder builder) {
        builder.service(SourceChecker.class, SourceChecker.class);
        builder.service(ResourceProvider.class, SourceCheckerProvider.class);
        builder.observer(FundamentalTestConfiguratorObserver.class);
        builder.observer(ContainerInitializationObserver.class);
        builder.observer(ContainerInstaller.class);
        builder.observer(PrepareBrowserSession.class);
        builder.observer(FailureScreenshot.class);
        builder.observer(ScreenshotTaker.class);

        // PhantomJS with binary resolution
        builder.service(Configurator.class, ResolvingPhantomJSDriverFactory.class);
        builder.service(Instantiator.class, ResolvingPhantomJSDriverFactory.class);
        builder.service(Destructor.class, ResolvingPhantomJSDriverFactory.class);
    }
}

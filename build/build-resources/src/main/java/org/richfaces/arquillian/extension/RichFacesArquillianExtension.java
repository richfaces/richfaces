package org.richfaces.arquillian.extension;

import org.jboss.arquillian.core.spi.LoadableExtension;
import org.jboss.arquillian.drone.spi.DroneInstanceEnhancer;
import org.jboss.arquillian.test.spi.enricher.resource.ResourceProvider;
import org.richfaces.arquillian.browser.PageLoadTimeoutSetter;
import org.richfaces.arquillian.browser.PageLoader;
import org.richfaces.arquillian.browser.WindowResizer;
import org.richfaces.arquillian.configuration.FundamentalTestConfiguratorObserver;
import org.richfaces.arquillian.container.installation.ContainerInitializationObserver;
import org.richfaces.arquillian.container.installation.ContainerInstaller;
import org.richfaces.arquillian.page.source.SourceChecker;
import org.richfaces.arquillian.page.source.SourceCheckerProvider;
import org.richfaces.arquillian.verification.VerifyDeploymentTestability;

public class RichFacesArquillianExtension implements LoadableExtension {

    @Override
    public void register(ExtensionBuilder builder) {
        builder.service(SourceChecker.class, SourceChecker.class);
        builder.service(ResourceProvider.class, SourceCheckerProvider.class);
        builder.service(DroneInstanceEnhancer.class, PageLoader.class);
        builder.observer(FundamentalTestConfiguratorObserver.class);
        builder.observer(ContainerInitializationObserver.class);
        builder.observer(ContainerInstaller.class);
        builder.observer(WindowResizer.class);
        builder.observer(VerifyDeploymentTestability.class);
        builder.observer(PageLoadTimeoutSetter.class);
    }
}

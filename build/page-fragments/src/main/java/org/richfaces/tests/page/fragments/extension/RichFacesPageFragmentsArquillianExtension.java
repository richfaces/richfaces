package org.richfaces.tests.page.fragments.extension;

import org.jboss.arquillian.core.spi.LoadableExtension;
import org.richfaces.tests.page.fragments.configuration.RichFacesPageFragmentsConfiguratorObserver;

public class RichFacesPageFragmentsArquillianExtension implements LoadableExtension {

    @Override
    public void register(ExtensionBuilder builder) {
        builder.observer(RichFacesPageFragmentsConfiguratorObserver.class);
    }
}

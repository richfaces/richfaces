package org.richfaces.fragment.extension;

import org.jboss.arquillian.core.spi.LoadableExtension;
import org.richfaces.fragment.configuration.RichFacesPageFragmentsConfiguratorObserver;

public class RichFacesFragmentsExtension implements LoadableExtension {

    @Override
    public void register(ExtensionBuilder builder) {
        builder.observer(RichFacesPageFragmentsConfiguratorObserver.class);
    }
}
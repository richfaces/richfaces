package org.richfaces.arquillian.extension;

import org.jboss.arquillian.core.spi.LoadableExtension;
import org.jboss.arquillian.test.spi.enricher.resource.ResourceProvider;
import org.richfaces.arquillian.page.source.SourceChecker;
import org.richfaces.arquillian.page.source.SourceCheckerProvider;

public class RichFacesArquillianExtension implements LoadableExtension {

    @Override
    public void register(ExtensionBuilder builder) {
        builder.service(SourceChecker.class, SourceChecker.class);
        builder.service(ResourceProvider.class, SourceCheckerProvider.class);
    }

}

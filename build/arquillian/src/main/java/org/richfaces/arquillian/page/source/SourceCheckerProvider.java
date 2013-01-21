package org.richfaces.arquillian.page.source;

import java.lang.annotation.Annotation;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.test.spi.enricher.resource.ResourceProvider;

public class SourceCheckerProvider implements ResourceProvider {

    @Inject
    private Instance<ServiceLoader> serviceLoader;

    @Override
    public boolean canProvide(Class<?> type) {
        return (type == SourceChecker.class);
    }

    @Override
    public Object lookup(ArquillianResource resource, Annotation... qualifiers) {
        return serviceLoader.get().onlyOne(SourceChecker.class);
    }

}

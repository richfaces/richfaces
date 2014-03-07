package org.richfaces.arquillian.warp;

import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.EventContext;
import org.jboss.arquillian.warp.spi.servlet.event.ProcessHttpRequest;

/**
 * Makes sure that requests are synchronized prevents RF-13530
 */
public class WarpRequestSynchronizer {

    public synchronized void synchronizedRequests(@Observes(precedence = 1000) EventContext<ProcessHttpRequest> context) {
        context.proceed();
    }

}

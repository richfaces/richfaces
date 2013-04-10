package org.richfaces.arquillian.container.installation;

import org.jboss.arquillian.core.api.Event;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.EventContext;
import org.jboss.arquillian.test.spi.event.suite.AfterSuite;
import org.jboss.arquillian.test.spi.event.suite.BeforeSuite;

public class ContainerInitializationObserver {

    @Inject
    private Event<InstallContainer> install;

    @Inject
    private Event<ConfigureContainer> configure;

    @Inject
    private Event<UninstallContainer> uninstall;

    public void installContainer(@Observes(precedence = 400) EventContext<BeforeSuite> ctx) {
        install.fire(new InstallContainer());
        configure.fire(new ConfigureContainer());
        ctx.proceed();
    }

    public void uninstallContainer(@Observes(precedence = 400) EventContext<AfterSuite> ctx) {
        uninstall.fire(new UninstallContainer());
    }
}
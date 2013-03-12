package org.richfaces.arquillian.drone;

import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.EventContext;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.test.spi.event.suite.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

public class PrepareBrowserSession {

    public void prepare(@Observes EventContext<Test> ctx) {
        WebDriver browser = GrapheneContext.getProxy();

        browser.manage().window().setSize(new Dimension(1280, 1024));

        ctx.proceed();
    }

}

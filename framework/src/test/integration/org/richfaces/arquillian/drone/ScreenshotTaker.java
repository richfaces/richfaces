package org.richfaces.arquillian.drone;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.EventContext;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.proxy.Interceptor;
import org.jboss.arquillian.graphene.proxy.InvocationContext;
import org.jboss.arquillian.test.spi.event.suite.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotTaker {

    private final Logger log = Logger.getLogger(ScreenshotTaker.class.getName());

    private final TakesScreenshot takesScreenshot = GrapheneContext.getProxyForInterfaces(TakesScreenshot.class);

    public void prepare(@Observes EventContext<Test> ctx) {
        WebDriver proxy = GrapheneContext.getProxy();

        Interceptor interceptor = new Interceptor() {
            @Override
            public Object intercept(InvocationContext ctx) throws Throwable {
                Object result = ctx.invoke();
                if (TakesScreenshot.class != ctx.getMethod().getDeclaringClass()) {
                    try {
                        File tempFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
                        FileUtils.copyFile(tempFile, new File("target/screenshot.png"));
                    } catch (Exception e) {
                        log.log(Level.WARNING,
                                String.format("Wasn't able to take screenshot before action '%s'", ctx.getMethod(), e));
                    }
                }
                return result;
            }
        };

        ((GrapheneProxyInstance) proxy).registerInterceptor(interceptor);

        ctx.proceed();
    }
}

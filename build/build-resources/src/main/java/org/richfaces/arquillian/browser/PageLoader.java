package org.richfaces.arquillian.browser;

import java.lang.annotation.Annotation;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.arquillian.drone.spi.DroneInstanceEnhancer;
import org.jboss.arquillian.drone.spi.InstanceOrCallableInstance;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.proxy.Interceptor;
import org.jboss.arquillian.graphene.proxy.InvocationContext;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

/**
 * Intercepts call of <code>WebDriver#get(url)</code> and tries to load the page up to 3 times, while catching TimeoutException.
 * Uses Graphene interceptors, so this enhancer needs to be called after GrapheneEnhancer.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class PageLoader implements DroneInstanceEnhancer<WebDriver> {

    private static final int DEFAULT_NUMBER_OF_REPEATS = 3;
    private static final String GET_METHOD_NAME = "get";
    private static final Logger LOG = Logger.getLogger(PageLoader.class.getName());
    private static final Interceptor interceptor = new PageLoadInterceptor();

    @Override
    public boolean canEnhance(InstanceOrCallableInstance instance, Class<?> droneType, Class<? extends Annotation> qualifier) {
        return WebDriver.class.isAssignableFrom(droneType);
    }

    @Override
    public WebDriver deenhance(WebDriver instance, Class<? extends Annotation> qualifier) {
        if (GrapheneProxy.isProxyInstance(instance)) {
            GrapheneProxyInstance proxy = (GrapheneProxyInstance) instance;
            proxy.unregisterInterceptor(interceptor);
        }
        return instance;
    }

    @Override
    public WebDriver enhance(final WebDriver instance, Class<? extends Annotation> qualifier) {
        if (GrapheneProxy.isProxyInstance(instance)) {
            GrapheneProxyInstance proxy = (GrapheneProxyInstance) instance;
            proxy.registerInterceptor(interceptor);
        }
        return instance;
    }

    @Override
    public int getPrecedence() {
        return -101;// run after Graphene (-100)
    }

    private static final class PageLoadInterceptor implements Interceptor {

        @Override
        public int getPrecedence() {
            return 10;// run before other Graphene's interceptors (all have 0)
        }

        @Override
        public Object intercept(InvocationContext context) throws Throwable {
            if (context.getMethod().getName().equals(GET_METHOD_NAME)) {
                tryToLoadPage((WebDriver) context.getTarget(), String.valueOf(context.getArguments()[0]));
                return null;// skip any other interceptors
            } else {
                return context.invoke();// invoke method normally
            }
        }

        private void tryToLoadPage(WebDriver webDriver, String url) {
            for (int i = 1; i <= DEFAULT_NUMBER_OF_REPEATS; i++) {
                try {
                    webDriver.get(url);
                    return;
                } catch (TimeoutException e) {
                    if (i == DEFAULT_NUMBER_OF_REPEATS) {
                        throw e;
                    }
                    LOG.log(Level.INFO, "Page was not loaded within timeout. Trying to load it again, attempt #{0}", (i + 1));
                }
            }
        }
    }
}

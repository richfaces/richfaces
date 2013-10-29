package org.richfaces.tests.page.fragments.configuration;

import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy.FutureTarget;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyHandler;
import java.lang.reflect.Method;

public class RichFacesPageFragmentsConfigurationContext {

    private static final ThreadLocal<RichFacesPageFragmentsConfiguration> REFERENCE = new ThreadLocal<RichFacesPageFragmentsConfiguration>();

    /**
     * Returns the context of configuration for current thread
     *
     * @return the context of configuration for current thread
     * @throws NullPointerException when context is null
     */
    static RichFacesPageFragmentsConfiguration get() {
        RichFacesPageFragmentsConfiguration configuration = REFERENCE.get();
        if (configuration == null) {
            throw new NullPointerException("configuration is null - it needs to be setup before starting to use it");
        }
        return configuration;
    }

    /**
     * Returns the instance of proxy to thread local context of configuration
     *
     * @return the instance of proxy to thread local context of configuration
     */
    public static RichFacesPageFragmentsConfiguration getProxy() {
        return GrapheneProxy.getProxyForHandler(new GrapheneProxyHandler(TARGET) {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return method.invoke(TARGET.getTarget(), args);
            }
        }, RichFacesPageFragmentsConfiguration.class);
    }

    /**
     * Returns true if the context is initialized
     *
     * @return true if the context is initialized
     */
    public static boolean isInitialized() {
        return REFERENCE.get() != null;
    }

    /**
     * Resets the WebDriver context for current thread
     */
    public static void reset() {
        REFERENCE.set(null);
    }

    /**
     * Sets the configuration context for current thread
     *
     * @param configuration the configuration instance
     * @throws IllegalArgumentException when provided configuration instance is null
     */
    public static void set(RichFacesPageFragmentsConfiguration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("configuration instance can't be null");
        }
        if (GrapheneProxy.isProxyInstance(configuration)) {
            throw new IllegalArgumentException("instance of the proxy can't be set to the configuration");
        }
        REFERENCE.set(configuration);
    }

    private static FutureTarget TARGET = new FutureTarget() {
        @Override
        public Object getTarget() {
            return get();
        }
    };
}

package org.richfaces.arquillian.configuration;

import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.graphene.GrapheneContext;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy.FutureTarget;

/**
 * <p>
 * Class for keeping thread local context of {@link FundamentalTestConfiguration}.
 * </p>
 *
 * <p>
 * Provides {@link #getProxy()} method for accessing that context in model of your tests.
 * </p>
 *
 * <p>
 * Proxy specifically handles the situations when no context is set - in this situation, runtime exception with
 * NullPointerException cause is thrown.
 * </p>
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class FundamentalTestConfigurationContext {

    private static final ThreadLocal<FundamentalTestConfiguration> REFERENCE = new ThreadLocal<FundamentalTestConfiguration>();

    /**
     * Returns the context of configuration for current thread
     *
     * @return the context of configuration for current thread
     * @throws NullPointerException when context is null
     */
    static FundamentalTestConfiguration get() {
        FundamentalTestConfiguration configuration = REFERENCE.get();
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
    public static FundamentalTestConfiguration getProxy() {
        return GrapheneProxy.getProxyForFutureTarget(GrapheneContext.getContextFor(Default.class), TARGET, FundamentalTestConfiguration.class);
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
    public static void set(FundamentalTestConfiguration configuration) {
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
package org.richfaces.webapp;

import java.util.Collection;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.Servlet;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

/**
 * <p>
 * Provides common functionality for {@link ServletContainerInitializer} implementatios.
 * </p>
 *
 * @author <a href="http://community.jboss.org/people/lfryc">Lukas Fryc</a>
 */
public abstract class GenericServletContainerInitializer implements ServletContainerInitializer {

    /**
     * Detects if given {@link Filter} class has been already registered.
     *
     * @param filterClass {@link Filter} implementation class
     * @param context to search for registration
     * @return true if given {@link Filter} class has been already registered.
     */
    protected boolean hasFilterMapping(Class<? extends Filter> filterClass, ServletContext context) {
        Collection<? extends FilterRegistration> filterRegistrations = context.getFilterRegistrations().values();
        for (FilterRegistration filterRegistration : filterRegistrations) {
            if (filterClass.getName().equals(filterRegistration.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>
     * Returns the servlet registration for given {@link Servlet} class, which has at least one mapping registered.
     * </p>
     *
     * <p>
     * Returns null otherwise.
     * </p>
     *
     * @param servletClass {@link Servlet} implementation class
     * @param context to search for registration
     * @return the servlet registration for given {@link Servlet} class, which has at least one mapping registered, null
     *         otherwise.
     */
    protected ServletRegistration getServletRegistration(Class<? extends Servlet> servletClass, ServletContext context) {
        Collection<? extends ServletRegistration> servletRegistrations = context.getServletRegistrations().values();
        for (ServletRegistration servletRegistration : servletRegistrations) {
            if (servletClass.getName().equals(servletRegistration.getClassName())) {
                if (servletRegistration.getMappings() != null && !servletRegistration.getMappings().isEmpty()) {
                    return servletRegistration;
                }
            }
        }
        return null;
    }
}

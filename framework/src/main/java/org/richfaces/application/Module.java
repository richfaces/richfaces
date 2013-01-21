package org.richfaces.application;

/**
 * <p class="changed_added_4_0">
 * {@link ServicesFactory} configuration module.
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public interface Module {
    /**
     * <p class="changed_added_4_0">
     * This method called from Initialization event listener. User can register their services there to override/extend base
     * functionality.
     * </p>
     *
     * @param factory
     */
    void configure(ServicesFactory factory);
}

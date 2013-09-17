package org.richfaces.validator;

import java.util.Map;

/**
 * <p class="changed_added_4_0">
 * Inmlementations of this interface describe JSF or JSR-303 validators. Because JSF supports 2 types of validators, this
 * interface unifies access to them.
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public interface FacesObjectDescriptor {
    /**
     * <p class="changed_added_4_0">
     * Returns JSF {@link javax.faces.validator.Validator} implementation class or JSR-303 annotation class.
     * </p>
     */
    Class<?> getImplementationClass();

    /**
     * <p class="changed_added_4_0">
     * Concrete validator parameters
     * </p>
     *
     * @return non null map with validator instance parameters.
     */
    Map<String, ? extends Object> getAdditionalParameters();

    /**
     * <p class="changed_added_4_0">
     * Localized validator message
     * </p>
     */
    Message getMessage();
}

package org.richfaces.component.behavior;

import java.util.Collection;

import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.convert.Converter;

import org.richfaces.validator.ConverterDescriptor;
import org.richfaces.validator.ValidatorDescriptor;

/**
 * <p class="changed_added_4_0">
 * Interface for JSF Behavior that creates scripts for client-side validation
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public interface ClientValidatorBehavior extends ClientBehavior {
    String BEHAVIOR_TYPE = "org.richfaces.behavior.ClientValidator";

    /**
     * <p class="changed_added_4_0">
     * Get JavaScript code for AJAX request.
     * </p>
     *
     * @param context
     */
    String getAjaxScript(ClientBehaviorContext context);

    /**
     * <p class="changed_added_4_0">
     * Look up for converter associated with target UIInput
     * </p>
     *
     * @param context
     * @return {@link Converter} instance or null if conversion not required.
     */
    ConverterDescriptor getConverter(ClientBehaviorContext context) throws ConverterNotFoundException;

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param context
     */
    Collection<ValidatorDescriptor> getValidators(ClientBehaviorContext context);

    /**
     * <p class="changed_added_4_0">
     * Returns array of classes that represents JSR-303 validation groups.
     * </p>
     *
     */
    Class<?>[] getGroups();

    void setGroups(Class<?>... groups);

    boolean isDisabled();

    boolean isImmediateSet();

    boolean isImmediate();

    String getOninvalid();

    String getOnvalid();
}
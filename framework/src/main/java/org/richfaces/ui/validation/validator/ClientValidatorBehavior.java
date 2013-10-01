/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.richfaces.ui.validation.validator;

import java.util.Collection;

import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.convert.Converter;

import org.richfaces.ui.behavior.ConverterNotFoundException;
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
    String BEHAVIOR_TYPE = "org.richfaces.ui.behavior.ClientValidator";

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
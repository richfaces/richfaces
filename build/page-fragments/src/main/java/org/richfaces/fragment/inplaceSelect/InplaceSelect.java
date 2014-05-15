/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
 *******************************************************************************/
package org.richfaces.fragment.inplaceSelect;

import org.richfaces.fragment.common.TextInputComponent;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.fragment.common.picker.ChoicePicker;
import org.richfaces.fragment.inplaceInput.ConfirmOrCancel;

public interface InplaceSelect {

    /**
     * Selects the first option according to the given <code>picker<code> param.
     *
     * In other words, it selects the first option which satisfies the choice picking rules defined in <code>picker</code>
     *
     * @param  picker for defining the rules by which the suggestion will be selected
     * @return        object by which a user can actually change the select value (confirm), or return to the previous input value (cancel)
     * @throws IllegalArgumentException if there is no such element which satisfies the rules given by <code>picker</code>
     */
    ConfirmOrCancel select(ChoicePicker picker);

    /**
     * Selects the option from the list of the options at index.
     *
     * @param  index index of option
     * @return       object by which a user can actually change the select value (confirm), or return to the previous input value (cancel)
     * @throws IllegalArgumentException if there is no such element with index <code>index</code>
     */
    ConfirmOrCancel select(int index);

    /**
     * Selects the first option, which matches the given <code>text</code>.
     *
     * @param  text  the text of the option to be selected
     * @return       object by which a user can actually change the select value (confirm), or return to the previous input value (cancel)
     * @throws IllegalArgumentException if there is no such element with text representation matching the given <code>text</code>
     */
    ConfirmOrCancel select(String text);

    /**
     * Gets the associated text input.
     *
     * @return the text input of the component
     * @see    TextInputComponentImpl
     */
    TextInputComponent getTextInput();
}
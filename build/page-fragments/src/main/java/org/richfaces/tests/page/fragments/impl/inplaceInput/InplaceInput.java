/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.page.fragments.impl.inplaceInput;

import org.richfaces.tests.page.fragments.impl.common.TextInputComponentImpl;

public interface InplaceInput {

    /**
     * Gets the associated text input.
     *
     * @return the text input of the component
     * @see TextInputComponentImpl
     */
    TextInputComponentImpl getTextInput();

    /**
     * Types given <code>text</code> to the input, thus switching the input into editing state.
     *
     * @param text text to be typed into the input
     * @return object by which a user can actually change the input value (confirm), or return to the previous text (cancel)
     * @throws IllegalStateException if typing to the input have switched the inplace input to an editing state
     */
    ConfirmOrCancel type(String text);
}

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
package org.richfaces.tests.page.fragments.impl.orderingList;

import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePicker;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface OrderingList {

    /**
     * Selects the first option, which matches given text.
     *
     * @param visibleText text to match
     * @return object by which it is possible to interact with ordering functions
     * @throws RuntimeException when no such option found
     * @see PuttingSelectedItem
     */
    PuttingSelectedItem select(String visibleText);

    /**
     * Selects the choice at index.
     *
     * @param index index of the choice
     * @return object by which it is possible to interact with ordering functions
     * @throws RuntimeException when no such option found
     */
    PuttingSelectedItem select(Integer index);

    /**
     * Selects a choice from suggestions.
     *
     * @param picker for picking from the choices
     * @return object by which it is possible to interact with ordering functions
     * @throws RuntimeException when no such option found
     * @see ByIndexChoicePicker
     * @see ByVisibleTextChoicePicker
     */
    PuttingSelectedItem select(ChoicePicker picker);
}

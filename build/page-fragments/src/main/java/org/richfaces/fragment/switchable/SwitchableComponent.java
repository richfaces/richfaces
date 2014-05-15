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
package org.richfaces.fragment.switchable;

import org.richfaces.fragment.common.picker.ChoicePicker;

/**
 * Represents a switchable component. That is component which consists from multiple panels,
 * and it is possible to switch among them. For example: tabPanel, accordion etc.
 *
 * @author jhuska
 *
 * @param <T> the container type, for example TabPanel has Tabs etc.
 *            Its purpose is to hold the actual content of the panel
 *            and provide it in form of initialized page object.
 */
public interface SwitchableComponent<T extends ComponentContainer> {

    /**
     * Switches to the container according to the given <code>picker</code> rules.
     *
     * @param  picker
     * @return initialized container of this switchable component, which was switched into
     * @throws IllegalArgumentException if there is no container which satisfies picker rules
     */
    T switchTo(ChoicePicker picker);

    /**
     * Switches to the container which text representation of header equals to given <code>header</code>.
     *
     * @param  header
     * @return initialized container of this switchable component, which was switched into
     * @throws IllegalArgumentException if there is no container with such a <code>header</code>
     */
    T switchTo(String header);

    /**
     * Switches to the container which index is equal to the given <code>index</code>
     *
     * @param index index of the container to be switched to
     * @return initialized container of this switchable component, which was switched into
     * @throws IllegalArgumentException if there is no container with such an <code>index</code>
     */
    T switchTo(int index);
}

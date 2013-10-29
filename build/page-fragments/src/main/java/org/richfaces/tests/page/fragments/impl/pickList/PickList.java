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
package org.richfaces.tests.page.fragments.impl.pickList;

import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePicker;
import org.richfaces.tests.page.fragments.impl.utils.picker.MultipleChoicePicker;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface PickList {

    /**
     * Picks and adds value from source list to target list.
     *
     * @param picker object which defines rules to pick the value
     * @return the pickList itself
     */
    PickList add(ChoicePicker picker);

    /**
     * Picks and adds value from source list to target list.
     *
     * @param match the textual representation of the value which should be picked and added to target list
     * @return the pickList itself
     */
    PickList add(String match);

    /**
     * Picks and adds value from source list to target list.
     *
     * @param index index of the value which should be picked and added to target list
     * @return the pickList itself
     */
    PickList add(int index);

    /**
     * Picks and adds multiple values from source list to target list.
     *
     * @param picker defines rules to pick the multiple values and add them to the target list
     * @return the pickList itself
     */
    PickList addMultiple(MultipleChoicePicker picker);

    /**
     * Picks and adds all values from source list to target list.
     *
     * @return the pickList itself
     */
    PickList addAll();

    /**
     * Removes the value from target list back to the source list.
     *
     * @param picker object which defines rules to pick the value
     * @return the pickList itself
     */
    PickList remove(ChoicePicker picker);

    /**
     * Removes the value from target list back to the source list.
     *
     * @param match the textual representation of the
     *              value which should be picked and removed from target list back to the source list
     * @return the pickList itself
     */
    PickList remove(String match);

    /**
     * Removes the value from target list back to the source list.
     *
     * @param index index of the value which should be picked and removed from target list back to the source list
     * @return the pickList itself
     */
    PickList remove(int index);

    /**
     * Picks and removes multiple values from target list back to source list.
     *
     * @param picker defines rules to pick the multiple values and remove them from target list back to the source list
     * @return the pickList itself
     */
    PickList removeMultiple(MultipleChoicePicker picker);

    /**
     * Removes all values from target list back to the source list.
     *
     * @return the pickList itself
     */
    PickList removeAll();
}
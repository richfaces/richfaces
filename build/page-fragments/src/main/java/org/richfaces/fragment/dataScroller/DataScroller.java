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
package org.richfaces.fragment.dataScroller;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface DataScroller {

    /**
     * Represents the buttons by which the dataScroller can be controlled.
     *
     * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
     *
     */
    enum DataScrollerSwitchButton {

        FIRST,
        FAST_REWIND,
        PREVIOUS,
        NEXT,
        FAST_FORWARD,
        LAST;
    }

    /**
     * Gets the index of the currently selected page. Indexed from 1.
     *
     * @return the position of the page at which the dataScroller currently is
     */
    int getActivePageNumber();

    /**
     * Direct switch to page. Indexed from 1. Page must exist. Without any waiting.
     *
     * @param pageNumber of the page which will be selected
     */
    void switchTo(int pageNumber);

    /**
     * Switch by buttons. Clicks on the chosen button. Without any waiting.
     *
     * @param btn the button which will be used to control the dataScroller in order to switch to a wished page
     * @see DataScrollerSwitchButton
     */
    void switchTo(DataScrollerSwitchButton btn);
}

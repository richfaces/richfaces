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
package org.richfaces.ui.iteration.dataScroller;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;

/**
 * @author Wesley Hales
 */
public class DataScrollEvent extends ActionEvent {
    /**
     *
     */
    private static final long serialVersionUID = 2657353903701932561L;
    private String oldScrolVal;
    private String newScrolVal;
    private int page;

    /**
     * Creates a new ScrollerEvent.
     *
     * @param component the source of the event
     * @param thisOldScrolVal the previously showing item identifier
     * @param thisNewScrolVal the currently showing item identifier
     */
    public DataScrollEvent(UIComponent component, String thisOldScrolVal, String thisNewScrolVal, int page) {
        super(component);
        this.setPhaseId(PhaseId.INVOKE_APPLICATION);
        oldScrolVal = thisOldScrolVal;
        newScrolVal = thisNewScrolVal;
        this.page = page;
    }

    public String getOldScrolVal() {
        return oldScrolVal;
    }

    public String getNewScrolVal() {
        return newScrolVal;
    }

    /**
     * @since 3.2
     * @return new page or <code>-1</code> if not applicable
     */
    public int getPage() {
        return page;
    }

    public boolean isAppropriateListener(FacesListener listener) {
        return super.isAppropriateListener(listener) || (listener instanceof DataScrollListener);
    }

    /**
     * Delivers this event to the SliderListener.
     *
     * @param listener the slider listener
     */
    public void processListener(FacesListener listener) {
        if (listener instanceof DataScrollListener) {
            DataScrollListener dataScrollerListener = (DataScrollListener) listener;
            dataScrollerListener.processDataScroll(this);
        }

        if (super.isAppropriateListener(listener)) {
            super.processListener(listener);
        }
    }
}

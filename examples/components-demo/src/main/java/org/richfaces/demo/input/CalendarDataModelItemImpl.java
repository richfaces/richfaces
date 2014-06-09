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
package org.richfaces.demo.input;

import org.richfaces.model.CalendarDataModelItem;

/**
 * @author Nick Belaevski - mailto:nbelaevski@exadel.com created 04.07.2007
 *
 */
public class CalendarDataModelItemImpl implements CalendarDataModelItem {
    private boolean enabled = true;
    private String styleClass = "";

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.component.CalendarDataModelItem#isEnabled()
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public Object getData() {
        return null;
    }

    public boolean hasToolTip() {
        return false;
    }

    public Object getToolTip() {
        return null;
    }

    public int getDay() {
        return 0;
    }
}

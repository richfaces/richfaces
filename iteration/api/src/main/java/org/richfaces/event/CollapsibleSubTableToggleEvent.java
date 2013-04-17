/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
package org.richfaces.event;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

/**
 * @author Anton Belevich
 *
 */
public class CollapsibleSubTableToggleEvent extends FacesEvent {
    private static final long serialVersionUID = 2916560585918250885L;
    private String togglerId;
    private boolean expanded;

    public CollapsibleSubTableToggleEvent(UIComponent source, boolean expanded) {
        this(source, expanded, null);
    }

    public CollapsibleSubTableToggleEvent(UIComponent source, boolean expanded, String toggerId) {
        super(source);
        this.expanded = expanded;
        this.togglerId = toggerId;
    }

    public String getTogglerId() {
        return togglerId;
    }

    public void setTogglerId(String togglerId) {
        this.togglerId = togglerId;
    }

    public boolean isExpanded() {
        return this.expanded;
    }

    public boolean isAppropriateListener(FacesListener listener) {
        return (listener instanceof CollapsibleSubTableToggleListener);
    }

    public void processListener(FacesListener listener) {
        ((CollapsibleSubTableToggleListener) listener).processCollapsibleSubTableToggle(this);
    }
}

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

package org.richfaces.component;

import javax.el.MethodExpression;
import javax.faces.component.UIComponent;

import org.richfaces.event.ChangeExpandSource;
import org.richfaces.event.ChangeExpandListener;

/**
 * @author akolonitsky
 * @since 2010-08-27
 */
public abstract class AbstractCollapsiblePanel extends UITogglePanel implements ChangeExpandSource {

    public static final String COMPONENT_TYPE = "org.richfaces.CollapsiblePanel";

    public static final String COMPONENT_FAMILY = "org.richfaces.CollapsiblePanel";
    private static final String STATE_EXPANDED = "expanded";
    private static final String STATE_COLLAPSED = "collapsed";

    protected AbstractCollapsiblePanel() {
        setRendererType("org.richfaces.CollapsiblePanel");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public String getActiveItem() {
        String ai = super.getActiveItem();
        return ai == null ? "true" : ai;
    }

    @Override
    protected boolean isActiveItem(UIComponent kid) {
        return isExpanded();
    }

    @Override
    protected boolean isActiveItem(UIComponent kid, String value) {
        return isExpanded();
    }
    

    public boolean isExpanded() {
        return Boolean.parseBoolean(getActiveItem());
    }

    public void setExpanded(boolean isExpanded) {
        setActiveItem(String.valueOf(isExpanded));
    }

    public abstract String getHeader();

    public abstract MethodExpression getChangeExpandListener();

    


    // ------------------------------------------------ Event Processing Methods

    public void addChangeExpandListener(ChangeExpandListener listener) {
        addFacesListener(listener);
    }

    public ChangeExpandListener[] getChangeExpandListeners() {
        return (ChangeExpandListener[]) getFacesListeners(ChangeExpandListener.class);
    }

    public void removeChangeExpandListener(ChangeExpandListener listener) {
        removeFacesListener(listener);
    }
}

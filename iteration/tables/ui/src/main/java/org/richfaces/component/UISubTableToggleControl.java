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

import javax.faces.component.UIComponentBase;

/**
 * @author Anton Belevich
 *
 */
public class UISubTableToggleControl extends UIComponentBase {

    public static final String COMPONENT_TYPE = "org.richfaces.SubTableToggleControl";

    public static final String COMPONENT_FAMILY = "org.richfaces.SubTableToggleControl";
    
    enum PropertyKeys {
        expandIcon, collapseIcon, expandLabel, collapseLabel, forId, event
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getExpandLabel() {
        return (String)getStateHelper().eval(PropertyKeys.expandLabel);
    }
    
    public void setExpandLabel(String expandLabel) {
        getStateHelper().put(PropertyKeys.expandLabel, expandLabel);
    }
    
    public String getCollapseLabel() {
        return (String)getStateHelper().eval(PropertyKeys.collapseLabel);
    }
    
    public void setCollapseLabel(String collapseLabel) {
        getStateHelper().put(PropertyKeys.collapseLabel, collapseLabel);
    }
    
    public String getExpandIcon() {
        return (String) getStateHelper().eval(PropertyKeys.expandIcon, null);
    }

    public void setExpandIcon(String expandIcon) {
        getStateHelper().put(PropertyKeys.expandIcon, expandIcon);
    }

    public String getCollapseIcon() {
        return (String) getStateHelper().eval(PropertyKeys.collapseIcon, null);
    }

    public void setCollapseIcon(String collapseIcon) {
        getStateHelper().put(PropertyKeys.collapseIcon, collapseIcon);
    }

    public String getFor() {
        return (String) getStateHelper().eval(PropertyKeys.forId, null);
    }

    public void setFor(String forId) {
        getStateHelper().put(PropertyKeys.forId, forId);
    }
    
    public String getEvent() {
        return (String) getStateHelper().eval(PropertyKeys.event, "onclick");

    }

    public void setEvent(String event) {
        getStateHelper().put(PropertyKeys.event, event);
    }
}

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

import org.richfaces.PanelMenuMode;

/**
 * @author akolonitsky
 * @since 2010-11-29
 */
public class UIPanelMenuItem extends AbstractPanelMenuItem {

    public enum PropertyKeys {
        mode,
        label,
        name,
        disabled,
        bypassUpdates,
        disableImplicitRender,
        data,
        status,
        execute,
        render
    }

    public PanelMenuMode getMode() {
        return (PanelMenuMode) getStateHelper().eval(PropertyKeys.mode, getPanelMenu().getItemMode());
    }

    public void setMode(PanelMenuMode mode) {
        getStateHelper().put(PropertyKeys.mode, mode);
    }

    public String getLabel() {
        return (String) getStateHelper().eval(PropertyKeys.label);
    }

    public void setLabel(String label) {
        getStateHelper().put(PropertyKeys.label, label);
    }

    public String getName() {
        return (String) getStateHelper().eval(PropertyKeys.name, getId());
    }

    public void setName(String name) {
        getStateHelper().put(PropertyKeys.name, name);
    }

    public boolean isDisabled() {
        return Boolean.valueOf(String.valueOf(getStateHelper().eval(PropertyKeys.disabled)));
    }

    public void setDisabled(boolean disabled) {
        getStateHelper().put(PropertyKeys.disabled, disabled);
    }

    public boolean isBypassUpdates() {
        return Boolean.valueOf(String.valueOf(getStateHelper().eval(PropertyKeys.bypassUpdates)));
    }

    public void setBypassUpdates(boolean bypassUpdates) {
        getStateHelper().put(PropertyKeys.bypassUpdates, bypassUpdates);
    }

    public boolean isDisableImplicitRender() {
        return Boolean.valueOf(String.valueOf(getStateHelper().eval(PropertyKeys.disableImplicitRender)));
    }

    public void setDisableImplicitRender(boolean disableImplicitRender) {
        getStateHelper().put(PropertyKeys.disableImplicitRender, disableImplicitRender);
    }

    public Object getData() {
        return getStateHelper().eval(PropertyKeys.data);
    }

    public void setData(Object data) {
        getStateHelper().put(PropertyKeys.data, data);
    }

    public String getStatus() {
        return (String) getStateHelper().eval(PropertyKeys.status);
    }

    public void setStatus(String status) {
        getStateHelper().put(PropertyKeys.status, status);
    }

    public Object getExecute() {
        return getStateHelper().eval(PropertyKeys.execute);
    }

    public void setExecute(Object execute) {
        getStateHelper().put(PropertyKeys.execute, execute);
    }

    public Object getRender() {
        return getStateHelper().eval(PropertyKeys.render);
    }

    public void setRender(Object render) {
        getStateHelper().put(PropertyKeys.render, render);
    }


}

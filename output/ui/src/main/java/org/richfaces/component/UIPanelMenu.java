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
public class UIPanelMenu extends AbstractPanelMenu {

    public enum PropertyKeys {
        disabled,
        expandEvent,
        collapseEvent,
        groupMode,
        expandSingle,
        itemMode,
        bubbleSelection,
        activeItem,
        itemChangeListener,
        bypassUpdates,
        limitToList,
        data,
        status,
        execute,
        render
    }

    public boolean isDisabled() {
        return Boolean.valueOf(String.valueOf(getStateHelper().eval(PropertyKeys.disabled)));
    }

    public void setDisabled(boolean disabled) {
        getStateHelper().put(PropertyKeys.disabled, disabled);
    }

    public String getExpandEvent() {
        return (String) getStateHelper().eval(PropertyKeys.expandEvent, "click");
    }

    public void setExpandEvent(String expandEvent) {
        getStateHelper().put(PropertyKeys.expandEvent, expandEvent);
    }

    public String getCollapseEvent() {
        return (String) getStateHelper().eval(PropertyKeys.collapseEvent, "click");
    }

    public void setCollapseEvent(String collapseEvent) {
        getStateHelper().put(PropertyKeys.collapseEvent, collapseEvent);
    }

    public PanelMenuMode getGroupMode() {
        return (PanelMenuMode) getStateHelper().eval(PropertyKeys.groupMode, PanelMenuMode.client);
    }

    public void setGroupMode(PanelMenuMode groupMode) {
        getStateHelper().put(PropertyKeys.groupMode, groupMode);
    }

    public boolean isExpandSingle() {
        return Boolean.valueOf(String.valueOf(getStateHelper().eval(PropertyKeys.expandSingle, true)));
    }

    public void setExpandSingle(boolean expandSingle) {
        getStateHelper().put(PropertyKeys.expandSingle, expandSingle);
    }

    public PanelMenuMode getItemMode() {
        return (PanelMenuMode) getStateHelper().eval(PropertyKeys.itemMode, PanelMenuMode.DEFAULT);
    }

    public void setItemMode(PanelMenuMode itemMode) {
        getStateHelper().put(PropertyKeys.itemMode, itemMode);
    }

    public boolean isBubbleSelection() {
        return Boolean.valueOf(String.valueOf(getStateHelper().eval(PropertyKeys.bubbleSelection, true)));
    }

    public void setBubbleSelection(boolean bubbleSelection) {
        getStateHelper().put(PropertyKeys.bubbleSelection, bubbleSelection);
    }

    public String getItemChangeListener() {
        return (String) getStateHelper().eval(PropertyKeys.itemChangeListener);
    }

    public void setItemChangeListener(String itemChangeListener) {
        getStateHelper().put(PropertyKeys.itemChangeListener, itemChangeListener);
    }

    public boolean isBypassUpdates() {
        return Boolean.valueOf(String.valueOf(getStateHelper().eval(PropertyKeys.bypassUpdates)));
    }

    public void setBypassUpdates(boolean bypassUpdates) {
        getStateHelper().put(PropertyKeys.bypassUpdates, bypassUpdates);
    }

    public boolean isLimitToList() {
        return Boolean.valueOf(String.valueOf(getStateHelper().eval(PropertyKeys.limitToList)));
    }

    public void setLimitToList(boolean limitToList) {
        getStateHelper().put(PropertyKeys.limitToList, limitToList);
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

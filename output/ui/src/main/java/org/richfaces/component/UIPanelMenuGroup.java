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

import javax.el.MethodExpression;

/**
 * @author akolonitsky
 * @since 2010-10-25
 */
public class UIPanelMenuGroup extends AbstractPanelMenuGroup {

    public enum PropertyKeys {
        expanded,
        expandSingle,
        collapseEvent,
        expandEvent,
        bubbleSelection,
        changeExpandListener
    }

    @Override
    public PanelMenuMode getMode() {
        return (PanelMenuMode) getStateHelper().eval(UIPanelMenuItem.PropertyKeys.mode, getPanelMenu().getGroupMode());
    }

    @Override
    public void setMode(PanelMenuMode mode) {
        super.setMode(mode);
    }

    public boolean isExpandSingle() {
        return Boolean.valueOf(String.valueOf(getStateHelper().eval(PropertyKeys.expandSingle, getPanelMenu().isExpandSingle())));
    }

    public void setExpandSingle(boolean expandSingle) {
        getStateHelper().put(PropertyKeys.expandSingle, expandSingle);
    }

    public String getCollapseEvent() {
        return (String) getStateHelper().eval(PropertyKeys.collapseEvent, "click");
    }

    public void setCollapseEvent(String collapseEvent) {
        getStateHelper().put(PropertyKeys.collapseEvent, collapseEvent);
    }

    public String getExpandEvent() {
        return (String) getStateHelper().eval(PropertyKeys.expandEvent, "click");
    }

    public void setExpandEvent(String expandEvent) {
        getStateHelper().put(PropertyKeys.expandEvent, expandEvent);
    }

    public boolean isBubbleSelection() {
        return Boolean.valueOf(String.valueOf(getStateHelper().eval(PropertyKeys.bubbleSelection, getPanelMenu().isBubbleSelection())));
    }

    public void setBubbleSelection(boolean bubbleSelection) {
        getStateHelper().put(PropertyKeys.bubbleSelection, bubbleSelection);
    }

    public MethodExpression getChangeExpandListener() {
        return (MethodExpression) getStateHelper().get(PropertyKeys.changeExpandListener);
    }

    public void setChangeExpandListener(MethodExpression changeExpandListener) {
        getStateHelper().put(PropertyKeys.changeExpandListener, changeExpandListener);
    }


}

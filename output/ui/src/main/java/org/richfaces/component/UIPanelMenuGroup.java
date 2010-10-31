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



/**
 * @author akolonitsky
 * @since 2010-10-25
 */
public class UIPanelMenuGroup extends AbstractPanelMenuGroup {

    public enum PropertyKeys {
        bubbleSelection,
        expanded,
        expandSingle,
        collapseEvent,
        expandEvent,
        iconCollapsed,
        iconExpanded,
        changeExpandListener
    }



    public boolean getBubbleSelection() {
        return Boolean.valueOf(String.valueOf(getStateHelper().eval(PropertyKeys.bubbleSelection, true)));
    }

    public void setBubbleSelection(boolean bubbleSelection) {
        getStateHelper().put(PropertyKeys.bubbleSelection, bubbleSelection);
    }

    public boolean isExpandSingle() {
        return Boolean.valueOf(String.valueOf(getStateHelper().eval(PropertyKeys.expandSingle, true)));
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

    public String getIconCollapsed() {
        return (String) getStateHelper().eval(PropertyKeys.iconCollapsed);
    }

    public void setIconCollapsed(String iconCollapsed) {
        getStateHelper().put(PropertyKeys.iconCollapsed, iconCollapsed);
    }

    public String getIconExpanded() {
        return (String) getStateHelper().eval(PropertyKeys.iconExpanded);
    }

    public void setIconExpanded(String iconExpanded) {
        getStateHelper().put(PropertyKeys.iconExpanded, iconExpanded);
    }

    public String getChangeExpandListener() {
        return (String) getStateHelper().eval(PropertyKeys.changeExpandListener);
    }

    public void setChangeExpandListener(String changeExpandListener) {
        getStateHelper().put(PropertyKeys.changeExpandListener, changeExpandListener);
    }


}

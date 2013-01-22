/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */
package org.richfaces.context;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.event.PreRemoveFromViewEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import org.ajax4jsf.component.AjaxOutput;

/**
 * @author Nick Belaevski
 */
public class PartialViewContextAjaxOutputTracker implements SystemEventListener {
    private static final String ATTRIBUTE_NAME = "org.richfaces.AjaxOutputTracker";

    private static Set<String> getTrackedChildrenSet(UIComponent component, boolean create) {
        Map<String, Object> attributes = component.getAttributes();

        @SuppressWarnings("unchecked")
        Set<String> trackedChildrenSet = (Set<String>) attributes.get(ATTRIBUTE_NAME);
        if (trackedChildrenSet == null && create) {
            trackedChildrenSet = new HashSet<String>();
            attributes.put(ATTRIBUTE_NAME, trackedChildrenSet);
        }

        return trackedChildrenSet;
    }

    private static void clearTrackedChildrenSet(UIComponent component) {
        component.getAttributes().remove(ATTRIBUTE_NAME);
    }

    static Collection<String> getDirectChildrenIds(UIComponent component) {
        if (!isContainerComponent(component)) {
            throw new IllegalArgumentException(component.toString());
        }

        return getTrackedChildrenSet(component, false);
    }

    static boolean hasNestedAjaxOutputs(UIComponent component) {
        Collection<String> childrenIds = getDirectChildrenIds(component);
        return childrenIds != null && !childrenIds.isEmpty();
    }

    private static String getId(UIComponent component) {
        String id = component.getId();
        if (id == null) {
            // TODO force clientId creation?
            component.getClientId();
            id = component.getId();
        }
        return id;
    }

    private static boolean isContainerComponent(UIComponent component) {
        return component instanceof NamingContainer || component instanceof UIViewRoot;
    }

    private UIComponent findParentContainerComponent(UIComponent component) {
        UIComponent c = component.getParent();
        while (c != null && !isContainerComponent(c)) {
            c = c.getParent();
        }

        return c;
    }

    private void componentAdded(UIComponent c) {
        UIComponent child = c;
        UIComponent parent;
        while ((parent = findParentContainerComponent(child)) != null) {
            Set<String> trackedChildrenSet = getTrackedChildrenSet(parent, true);
            boolean updateNextParent = trackedChildrenSet.isEmpty();
            trackedChildrenSet.add(getId(child));

            if (!updateNextParent) {
                break;
            }

            child = parent;
        }
    }

    private void componentRemoved(UIComponent c) {
        UIComponent child = c;
        UIComponent parent;
        while ((parent = findParentContainerComponent(child)) != null) {
            Set<String> trackingSet = getTrackedChildrenSet(parent, false);
            if (trackingSet != null) {
                trackingSet.remove(getId(child));

                if (trackingSet.isEmpty()) {
                    clearTrackedChildrenSet(parent);
                } else {
                    break;
                }
            }

            child = parent;
        }
    }

    public void processEvent(SystemEvent event) throws AbortProcessingException {
        if (event instanceof PostAddToViewEvent) {
            PostAddToViewEvent addToViewEvent = (PostAddToViewEvent) event;
            componentAdded(addToViewEvent.getComponent());
        } else if (event instanceof PreRemoveFromViewEvent) {
            PreRemoveFromViewEvent removeFromViewEvent = (PreRemoveFromViewEvent) event;
            componentRemoved(removeFromViewEvent.getComponent());
        } else {
            throw new IllegalArgumentException(event.toString());
        }
    }

    public boolean isListenerForSource(Object source) {
        return source instanceof AjaxOutput;
    }
}

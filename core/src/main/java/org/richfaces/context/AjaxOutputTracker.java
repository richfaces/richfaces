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
package org.richfaces.context;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.event.PreRemoveFromViewEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import org.ajax4jsf.component.AjaxOutput;

/**
 * Tracker that tracks all {@link AjaxOutput} components in the component tree and that can retrieve tracked {@link AjaxOutput}s
 * in O(n) where "n" is depth of the tree.
 *
 * The O(n) is ensured by tracking nested {@link AjaxOutput} in each {@link NamingContainer} component on a way from the
 * {@link UIViewRoot} to the given component.
 *
 * @author Nick Belaevski
 */
public class AjaxOutputTracker implements SystemEventListener {
    private static final String ATTRIBUTE_NAME = "org.richfaces.AjaxOutputTracker";

    /**
     * Tracks additions (resp. removals) of {@link AjaxOutput} to (resp. from) a component tree on {@link PostAddToViewEvent}
     * (resp. {@link PreRemoveFromViewEvent})
     */
    @Override
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

    /**
     * Tracks just {@link AjaxOutput} components
     */
    public boolean isListenerForSource(Object source) {
        return source instanceof AjaxOutput;
    }

    /**
     * Return a list of all {@link AjaxOutput} components in a tree under a given component.
     */
    static Collection<UIComponent> getAjaxOutputs(FacesContext facesContext, UIComponent component) {
        final Collection<String> childrenIds = getDirectChildrenIds(component);
        final Set<UIComponent> ajaxOutputs = new HashSet<UIComponent>();

        if (childrenIds != null) {
            for (String childId : childrenIds) {
                UIComponent child = component.findComponent(childId);

                if (child instanceof AjaxOutput) {
                    ajaxOutputs.add(child);
                } else if (isContainerComponent(child)) {
                    ajaxOutputs.addAll(getAjaxOutputs(facesContext, child));
                }
            }
        }

        return ajaxOutputs;
    }

    /**
     * Returns a list of {@link AjaxOutput} (or IDs of {@link NamingContainer} that contains at least one {@link AjaxOutput})
     * tracked in the given component subtree.
     *
     * Provided component must be Container Component as specified by {@link #isContainerComponent(UIComponent)}.
     *
     * @throws IllegalArgumentException when the provided component is not Container Component
     */
    static Collection<String> getDirectChildrenIds(UIComponent component) {
        if (!isContainerComponent(component)) {
            throw new IllegalArgumentException(component.toString());
        }

        return getTrackedChildrenSet(component, false);
    }

    /**
     * Detects whether given component has some {@link AjaxOutput} components tracked in its subtree.
     */
    static boolean hasNestedAjaxOutputs(UIComponent component) {
        Collection<String> childrenIds = getDirectChildrenIds(component);
        return childrenIds != null && !childrenIds.isEmpty();
    }

    /**
     * Returns list of {@link AjaxOutput} IDs tracked in the given component subtree
     */
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

    /**
     * Clears a list of tracked {@link AjaxOutput} IDs in a given component
     */
    private static void clearTrackedChildrenSet(UIComponent component) {
        component.getAttributes().remove(ATTRIBUTE_NAME);
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
}

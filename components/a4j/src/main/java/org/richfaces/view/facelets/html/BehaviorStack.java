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
package org.richfaces.view.facelets.html;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.Application;
import javax.faces.component.StateHolder;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHint;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;

/**
 * @author Nick Belaevski
 *
 *         This class is necessary to handle nesting wrapping behaviors properly and is created to work around this issue:
 *         https://javaserverfaces-spec-public.dev.java.net/issues/show_bug.cgi?id=655
 *
 */

// TODO - check for bug resolution
public final class BehaviorStack {
    private static final String BEHAVIOR_STACK = "org.richfaces.BehaviorStack";
    private LinkedList<BehaviorInfoImpl> behaviorStack = null;

    public BehaviorStack() {
        behaviorStack = new LinkedList<BehaviorInfoImpl>();
    }

    public static BehaviorStack getBehaviorStack(FacesContext context, boolean createIfNull) {
        Map<Object, Object> attributes = context.getAttributes();
        BehaviorStack behaviorStack = (BehaviorStack) attributes.get(BEHAVIOR_STACK);

        if (behaviorStack == null && createIfNull) {
            behaviorStack = new BehaviorStack();
            attributes.put(BEHAVIOR_STACK, behaviorStack);
        }

        return behaviorStack;
    }

    public boolean isEmpty() {
        return behaviorStack.isEmpty();
    }

    public void addBehaviors(FacesContext context, ClientBehaviorHolder behaviorHolder) {
        if (behaviorStack == null || behaviorStack.isEmpty()) {
            return;
        }

        for (BehaviorInfoImpl behaviorInfo : behaviorStack) {
            behaviorInfo.addBehavior(context, behaviorHolder);
        }
    }

    public void pushBehavior(FacesContext context, ClientBehavior clientBehavior, String behaviorId, String eventName) {

        Object behaviorState = ((StateHolder) clientBehavior).saveState(context);

        // closer behaviors are processed earlier
        behaviorStack.addFirst(new BehaviorInfoImpl(behaviorId, eventName, behaviorState));
    }

    public BehaviorInfo popBehavior() {
        if (!behaviorStack.isEmpty()) {
            return behaviorStack.removeFirst();
        } else {
            return null;
        }
    }

    public interface BehaviorInfo {
        List<ClientBehavior> getBehaviors();
    }

    private static class BehaviorInfoImpl implements BehaviorInfo {
        private String behaviorId;
        private Object behaviorState;
        private String eventName;
        private List<ClientBehavior> behaviors;

        BehaviorInfoImpl(String behaviorId, String eventName, Object behaviorState) {
            this.behaviorId = behaviorId;
            this.eventName = eventName;
            this.behaviorState = behaviorState;
        }

        private void addBehavior(FacesContext context, ClientBehaviorHolder behaviorHolder) {
            String eventName = this.eventName;

            if (eventName == null) {
                eventName = behaviorHolder.getDefaultEventName();

                if (eventName == null) {
                    return;
                }
            }

            if (shouldAddBehavior(behaviorHolder, eventName)) {
                ClientBehavior behavior = createBehavior(context);

                behaviorHolder.addClientBehavior(eventName, behavior);
            }
        }

        public List<ClientBehavior> getBehaviors() {
            return behaviors;
        }

        private boolean shouldAddBehavior(ClientBehaviorHolder behaviorHolder, String eventName) {
            if (!behaviorHolder.getEventNames().contains(eventName)) {
                return false;
            }

            Map<String, List<ClientBehavior>> clientBehaviorsMap = behaviorHolder.getClientBehaviors();
            List<ClientBehavior> clientBehaviors = clientBehaviorsMap.get(eventName);

            if (clientBehaviors == null || clientBehaviors.isEmpty()) {
                return true;
            }

            for (ClientBehavior behavior : clientBehaviors) {
                Set<ClientBehaviorHint> hints = behavior.getHints();

                if (hints.contains(ClientBehaviorHint.SUBMITTING)) {
                    return false;
                }
            }

            return true;
        }

        private ClientBehavior createBehavior(FacesContext context) {
            Application application = context.getApplication();
            ClientBehavior behavior = (ClientBehavior) application.createBehavior(this.behaviorId);

            ((StateHolder) behavior).restoreState(context, behaviorState);

            if (behaviors == null) {
                behaviors = new ArrayList<ClientBehavior>();
            }

            behaviors.add(behavior);

            return behavior;
        }
    }
}

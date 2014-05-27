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
import java.util.LinkedHashSet;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

import org.ajax4jsf.component.AjaxClientBehavior;
import org.richfaces.util.Sets;

/**
 * User: akolonitsky Date: Oct 13, 2009
 */
abstract class ActivatorComponentCallbackBase implements VisitCallback {
    protected final FacesContext facesContext;
    private final String behaviorEvent;

    ActivatorComponentCallbackBase(FacesContext facesContext, String behaviorEvent) {
        super();

        this.facesContext = facesContext;
        this.behaviorEvent = behaviorEvent;
    }

    private AjaxClientBehavior findBehavior(UIComponent target) {
        if ((behaviorEvent == null) || !(target instanceof ClientBehaviorHolder)) {
            return null;
        }

        ClientBehaviorHolder behaviorHolder = (ClientBehaviorHolder) target;
        List<ClientBehavior> behaviors = behaviorHolder.getClientBehaviors().get(behaviorEvent);

        if (behaviors == null) {
            return null;
        }

        for (ClientBehavior behavior : behaviors) {
            if ((behavior instanceof AjaxClientBehavior) && !((AjaxClientBehavior) behavior).isDisabled()) {

                // TODO need more reliable algorithm
                return (AjaxClientBehavior) behavior;
            }
        }

        return null;
    }

    protected Collection<String> toCollection(Object value) {
        // TODO - unit tests check for "@none" element
        Collection<String> result = Sets.asSet(value);
        if (result == null) {
            result = new LinkedHashSet<String>(1);
        }

        return result;
    }

    protected abstract void doVisit(UIComponent target, AjaxClientBehavior behavior);

    public final VisitResult visit(VisitContext visitContext, UIComponent target) {
        AjaxClientBehavior ajaxBehavior = null;

        if (behaviorEvent != null) {
            ajaxBehavior = findBehavior(target);
        }

        doVisit(target, ajaxBehavior);

        return VisitResult.COMPLETE;
    }
}

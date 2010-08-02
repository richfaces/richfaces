/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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

import static org.richfaces.component.MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR;

import java.util.Collection;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

/**
 * @author Nick Belaevski
 *
 */
public abstract class ExtendedVisitContext extends VisitContext {

    public static final String META_COMPONENT_ID = "org.richfaces.MetaComponentId";

    private final FacesContext facesContext;

    private final ExtendedVisitContextMode visitMode;

    protected ExtendedVisitContext(FacesContext facesContext, ExtendedVisitContextMode visitMode) {
        super();

        if (facesContext == null) {
            throw new NullPointerException();
        }

        this.facesContext = facesContext;
        this.visitMode = visitMode;
    }

    public ExtendedVisitContextMode getVisitMode() {
        return visitMode;
    }

    public VisitResult invokeMetaComponentVisitCallback(UIComponent component, VisitCallback callback,
        String metaComponentId) {

        if (metaComponentId != null) {
            Map<Object, Object> attributes = getFacesContext().getAttributes();
            try {
                attributes.put(META_COMPONENT_ID, metaComponentId);
                return invokeVisitCallback(component, callback);
            } finally {
                attributes.remove(META_COMPONENT_ID);
            }
        } else {
            return invokeVisitCallback(component, callback);
        }
    }

    public String buildExtendedClientId(UIComponent component) {
        String extendedClientId = component.getClientId(facesContext);
        String subComponentId = (String) facesContext.getAttributes().get(META_COMPONENT_ID);
        if (subComponentId != null) {
            StringBuilder sb = new StringBuilder(extendedClientId.length() + 1 /* separator length */ +
                subComponentId.length());

            sb.append(extendedClientId);
            sb.append(META_COMPONENT_SEPARATOR_CHAR);
            sb.append(subComponentId);

            extendedClientId = sb.toString();
        }

        return extendedClientId;
    }

    public String buildExtendedComponentId(UIComponent component) {
        String componentId = component.getId();
        String subComponentId = (String) facesContext.getAttributes().get(META_COMPONENT_ID);
        if (subComponentId != null) {
            StringBuilder sb = new StringBuilder(componentId.length() + 1 /* separator length */ +
                subComponentId.length());

            sb.append(componentId);
            sb.append(META_COMPONENT_SEPARATOR_CHAR);
            sb.append(subComponentId);

            componentId = sb.toString();
        }

        return componentId;
    }

    @Override
    public FacesContext getFacesContext() {
        return facesContext;
    }

    public abstract Collection<String>getDirectSubtreeIdsToVisit(UIComponent component);

    public abstract VisitContext createNamingContainerVisitContext(UIComponent component,
        Collection<String> directIds);
}

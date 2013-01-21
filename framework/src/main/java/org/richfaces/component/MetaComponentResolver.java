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
package org.richfaces.component;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * MetaComponentResolver interface is used to customize resolution of meta-components
 *
 * @author Nick Belaevski
 */
public interface MetaComponentResolver {
    // TODO - do we want to make this configurable in web.xml?
    char META_COMPONENT_SEPARATOR_CHAR = '@';

    /**
     * Resolves and returns string identifying supported meta-component or <code>null</code> if provided meta-component name is
     * not a supported one.
     *
     * @param facesContext - current instance of {@link FacesContext}
     * @param contextComponent - instance of {@link UIComponent} that requested resolution of meta-component
     * @param metaComponentId - name of meta-component (without leading '@' sign)
     * @return clientId, one of supported meta-names such as @all, @this, etc. or <code>null</code>
     */
    String resolveClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId);

    /**
     * Provides replacement for unresolved meta-component names. Returns identifier string for the chosen substitution or
     * <code>null</code>
     *
     * @param facesContext - current instance of {@link FacesContext}
     * @param contextComponent - instance of {@link UIComponent} that requested resolution of meta-component
     * @param metaComponentId - name of meta-component (without leading '@' sign)
     * @return clientId, one of supported meta-names such as @all, @this, etc. or <code>null</code>
     */
    String substituteUnresolvedClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId);
}

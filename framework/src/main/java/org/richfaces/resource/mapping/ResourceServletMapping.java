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
package org.richfaces.resource.mapping;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

import org.richfaces.el.ELUtils;
import org.richfaces.servlet.ResourceServlet;

/**
 * Maps resource with given key to RichFaces {@link ResourceServlet}.
 *
 * @author Lukas Fryc
 */
public class ResourceServletMapping implements ResourceMapping {

    private ResourcePath resourcePath;

    public ResourceServletMapping(ResourcePath location) {
        this.resourcePath = location;
    }

    @Override
    public ResourcePath getResourcePath(FacesContext context) {
        ValueExpression expression = ELUtils.createValueExpression(ResourceMappingFeature.getLocation());
        String contextPath = (String) expression.getValue(FacesContext.getCurrentInstance().getELContext());

        return new ResourcePath(contextPath + resourcePath.toExternalForm());
    }
}

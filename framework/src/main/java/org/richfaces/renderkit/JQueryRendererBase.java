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
package org.richfaces.renderkit;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import org.richfaces.component.util.HtmlUtil;

/**
 * @author nick
 *
 */
@ResourceDependencies({ @ResourceDependency(library = "org.richfaces", name = "base-component.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.component.js") })
public abstract class JQueryRendererBase extends Renderer {
    protected String getEscapedSelector(FacesContext context, UIComponent component) {
        String selector = (String) component.getAttributes().get("selector");

        if (selector != null) {
            selector = HtmlUtil.expandIdSelector(selector, component, context);
        }

        return selector;
    }
}

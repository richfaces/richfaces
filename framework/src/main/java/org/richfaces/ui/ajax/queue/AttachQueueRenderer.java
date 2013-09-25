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
package org.richfaces.ui.ajax.queue;

import org.richfaces.cdk.annotations.JsfRenderer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Nick Belaevski Renderer for attachedQueue component
 */
@JsfRenderer(type = "org.richfaces.ui.AttachQueueRenderer", family = "org.richfaces.AttachQueue")
public class AttachQueueRenderer extends QueueRendererBase {
    @Override
    protected String getQueueName(FacesContext context, UIComponent comp) {
        return comp.getClientId(context);
    }
}
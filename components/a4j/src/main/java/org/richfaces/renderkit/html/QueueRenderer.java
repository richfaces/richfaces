/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat, Inc. and individual contributors
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
package org.richfaces.renderkit.html;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractQueue;
import org.richfaces.renderkit.util.RendererUtils;

/**
 * @author Nick Belaevski Renderer for queue component
 */
@JsfRenderer(type = "org.richfaces.QueueRenderer", family = "org.richfaces.Queue")
public class QueueRenderer extends QueueRendererBase {
    private final RendererUtils utils = RendererUtils.getInstance();

    protected String getQueueName(FacesContext context, UIComponent comp) {
        String nameAttributeValue = (String) comp.getAttributes().get(NAME_ATTRIBBUTE);

        if (nameAttributeValue == null || nameAttributeValue.length() == 0) {
            UIComponent form = utils.getNestingForm(comp);
            if (form != null) {
                nameAttributeValue = form.getClientId(context);
            } else {
                nameAttributeValue = AbstractQueue.GLOBAL_QUEUE_NAME;
            }
        }

        return nameAttributeValue;
    }
}
/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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

import org.richfaces.event.ChangeExpandEvent;
import org.richfaces.event.ChangeExpandListener;
import org.richfaces.event.ChangeExpandSource;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagConfig;

/**
 *
 * @author akolonitsky
 * @version 1.0
 */
public final class ChangeExpandListenerHandler extends EventListenerHandler {

    private static class LazyChangeExpandListener extends LazyEventListener<ChangeExpandListener> implements ChangeExpandListener {
        private static final long serialVersionUID = -391020876192823200L;

        LazyChangeExpandListener(String type, ValueExpression binding) {
            super(type, binding);
        }

        public void processChangeExpand(ChangeExpandEvent event) throws AbortProcessingException {
            processEvent(event);
        }
    }

    public ChangeExpandListenerHandler(TagConfig config) {
        super(config);
    }

    public void applyAttachedObject(FacesContext context, UIComponent parent) {
        ValueExpression expression = null;
        if (this.binding != null) {
            FaceletContext ctx = (FaceletContext) context.getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);
            expression = this.binding.getValueExpression(ctx, ChangeExpandListener.class);
        }

        ChangeExpandSource source = (ChangeExpandSource) parent;
        source.addChangeExpandListener(new LazyChangeExpandListener(this.listenerType, expression));
    }
}


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
package org.richfaces.view.facelets.html;

import java.io.Serializable;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import org.richfaces.cdk.annotations.Event;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.view.facelets.TagHandlerUtils;

/**
 * @author Nick Belaevski
 *
 */
@Event(listener = ActionListener.class, source = "org.richfaces.event.DummyActionListenerSource", tag = { @Tag(name = "actionListener", handlerClass = ActionListenerHandler.class, generate = false, type = TagType.Facelets) })
class LazyActionListener implements ActionListener, Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6303879250524609909L;
    private final String type;
    private final ValueExpression binding;

    public LazyActionListener(String type, ValueExpression binding) {
        this.type = type;
        this.binding = binding;
    }

    public void processAction(ActionEvent event) throws AbortProcessingException {
        ActionListener instance = null;
        FacesContext faces = FacesContext.getCurrentInstance();
        if (faces == null) {
            return;
        }

        if (this.binding != null) {
            instance = (ActionListener) binding.getValue(faces.getELContext());
        }

        if (instance == null && this.type != null) {
            try {
                instance = TagHandlerUtils.loadClass(this.type, ActionListener.class).newInstance();
            } catch (Exception e) {
                throw new AbortProcessingException("Couldn't lazily instantiate ActionListener", e);
            }

            if (this.binding != null) {
                binding.setValue(faces.getELContext(), instance);
            }
        }

        if (instance != null) {
            instance.processAction(event);
        }
    }
}

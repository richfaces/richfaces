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
package org.richfaces.demo.actionListener;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.NoneScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

/**
 * @author Nick Belaevski
 *
 */
@ManagedBean
@NoneScoped
public class ActionListenerBean {
    public static final class ActionListenerImpl implements ActionListener {
        public void processAction(ActionEvent event) throws AbortProcessingException {
            addFacesMessage("Implementation of ActionListener created and called: " + this);
        }
    }

    private static final class BoundActionListener implements ActionListener {
        public void processAction(ActionEvent event) throws AbortProcessingException {
            addFacesMessage("Bound listener called");
        }
    }

    private ActionListener actionListener = new BoundActionListener();

    private static void addFacesMessage(String messageText) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(messageText));
    }

    public void handleActionMethod(ActionEvent event) throws AbortProcessingException {
        addFacesMessage("Method expression listener called");
    }

    public void handleActionMethodComposite(ActionEvent event) throws AbortProcessingException {
        addFacesMessage("Method expression listener called from composite component");
    }

    public ActionListener getActionListener() {
        return actionListener;
    }
}

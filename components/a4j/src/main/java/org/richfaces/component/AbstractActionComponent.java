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

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.component.attribute.BypassProps;

import javax.faces.component.UICommand;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;

/**
 * @author Nick Belaevski
 *
 */
public abstract class AbstractActionComponent extends UICommand implements BypassProps {

    @Override
    public void queueEvent(FacesEvent event) {
        if (event instanceof ActionEvent) {
            if (event.getComponent() == this) {
                if (isImmediate()) {
                    event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
                } else if (isBypassUpdates()) {
                    event.setPhaseId(PhaseId.PROCESS_VALIDATIONS);
                } else {
                    event.setPhaseId(PhaseId.INVOKE_APPLICATION);
                }
            }

            // UICommand set Phase ID for all ActionEvents - bypass it.
            getParent().queueEvent(event);
        } else {
            super.queueEvent(event);
        }
    }
}

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.validator;

import java.io.Serializable;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.richfaces.application.FacesMessages;
import org.richfaces.l10n.MessageFactory;
import org.richfaces.services.ServiceTracker;
import org.richfaces.component.AbstractSelectComponent;
import org.richfaces.component.util.MessageUtil;

/**
 * Validates that Select component has sent valid value
 *
 * @author Lukas Fryc
 *
 */
public class SelectLabelValueValidator implements Validator, Serializable {

    public static final String ID = "org.richfaces.validator.SelectLabelValueValidator";

    private static final long serialVersionUID = 2343817853927262660L;

    public void validate(FacesContext facesContext, UIComponent component, Object valueAsObject) throws ValidatorException {
        final AbstractSelectComponent select = (AbstractSelectComponent) component;

        final String clientId = component.getClientId(facesContext);
        final Map<String, String> requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();

        final String label = (String) requestParameterMap.get(clientId + "Input");
        final String value = (valueAsObject == null) ? "" : valueAsObject.toString();

        if (!valueWasDerivedOnClient(value, label, select)) {
            throw new ValidatorException(createMessage(value, label, select, facesContext));
        }
    }

    /**
     * Returns true when value was determined based on manual input of label
     */
    private boolean valueWasDerivedOnClient(String value, String label, AbstractSelectComponent select) {
        if (!isEmpty(value)) {
            return true;
        }

        if (isEmpty(label) || label.equals(select.getDefaultLabel())) {
            return true;
        }

        return false;
    }

    /**
     * Creates message for invalid select input
     */
    private FacesMessage createMessage(String value, String label, AbstractSelectComponent select, FacesContext facesContext) {

        String componentLabel = MessageUtil.getLabel(facesContext, select);

        MessageFactory messageFactory = ServiceTracker.getService(MessageFactory.class);

        return messageFactory.createMessage(facesContext, FacesMessage.SEVERITY_ERROR, FacesMessages.UISELECTONE_INVALID,
                componentLabel);
    }

    private static boolean isEmpty(String value) {
        return (value == null) || (value.length() == 0);
    }

}
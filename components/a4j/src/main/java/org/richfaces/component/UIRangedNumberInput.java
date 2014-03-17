/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */
package org.richfaces.component;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

import org.richfaces.component.util.MessageUtil;

/**
 * @author Nick - mailto:nbelaevski@exadel.com created 20.02.2007
 */
public abstract class UIRangedNumberInput extends UIInput {
    public abstract String getMaxValue();

    public abstract void setMaxValue(String value);

    public abstract String getMinValue();

    public abstract void setMinValue(String value);

    public abstract boolean isDisabled();

    public abstract void setDisabled(boolean disabled);

    @Override
    public void decode(FacesContext arg0) {
        if (this.isDisabled()) {
            return;
        }

        super.decode(arg0);
    }

    @Override
    protected void validateValue(FacesContext context, Object newValue) {
        if (isValid() && !isEmpty(newValue)) {
            String label = MessageUtil.getLabel(context, this).toString();
            Double minValue = null;
            Double maxValue = null;
            Double value = null;

            try {
                minValue = convert(getMinValue());
                maxValue = convert(getMaxValue());

                // convert value and check if it is in range
                value = convert(newValue);
            } catch (Exception e) {
                setValid(false);

                FacesMessage mess = new FacesMessage(label + ": " + e.getLocalizedMessage());

                mess.setSeverity(FacesMessage.SEVERITY_ERROR);
                context.addMessage(this.getClientId(context), mess);
            }

            if (value != null) {
                if ((null == minValue) || (null == maxValue)) {
                    setValid(false);

                    FacesMessage mess = new FacesMessage(label + ": conversation error, maxValue or minValue is null!");

                    mess.setSeverity(FacesMessage.SEVERITY_ERROR);
                    context.addMessage(this.getClientId(context), mess);
                } else if (minValue.doubleValue() > value.doubleValue()) {
                    setValid(false);

                    FacesMessage mess = new FacesMessage(label + ": input value is less than minimal value!");

                    mess.setSeverity(FacesMessage.SEVERITY_ERROR);
                    context.addMessage(this.getClientId(context), mess);
                } else if (maxValue.doubleValue() < value.doubleValue()) {
                    setValid(false);

                    FacesMessage mess = new FacesMessage(label + ": input value is more than maximum value!");

                    mess.setSeverity(FacesMessage.SEVERITY_ERROR);
                    context.addMessage(this.getClientId(context), mess);
                }
            } else {
                setValid(false);

                FacesMessage mess = new FacesMessage(label + ": input value can't be null!");

                mess.setSeverity(FacesMessage.SEVERITY_ERROR);
                context.addMessage(this.getClientId(context), mess);
            }
        }

        super.validateValue(context, newValue);
    }

    private Double convert(Object object) {
        if (object == null) {
            return null;
        }

        return new Double(object.toString());
    }
}

/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
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
package org.richfaces.validator;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * Implementation of the JSF validator to use with Bean Validation / Hibernate validator
 * 
 * @author asmirnov
 * 
 */
public class FacesBeanValidator implements Serializable, Validator, GraphValidator {

    public static final String BEAN_VALIDATOR_TYPE = "org.richfaces.BeanValidator";
    /**
	 * 
	 */
    private static final long serialVersionUID = -264568176252121853L;
    private ValueExpression summaryExpression = null;

    private String summary = null;

    private ValueExpression profilesExpression = null;

    private Set<String> profiles = null;

    /**
     * @return the summary
     */
    public String getSummary() {
        String summaryString = null;
        if (null != summaryExpression) {
            summaryString = (String) summaryExpression.getValue(FacesContext.getCurrentInstance().getELContext());
        } else {
            summaryString = this.summary;
        }
        return summaryString;
    }

    /**
     * @param summary
     *            the summary to set
     */
    public void setSummary(ValueExpression summary) {
        this.summaryExpression = summary;
    }

    /**
     * @param summary
     *            the summary to set
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.validator.Validator#validate(javax.faces.context.FacesContext,
     * javax.faces.component.UIComponent, java.lang.Object)
     */
    public void validate(FacesContext context, UIComponent component, Object convertedValue) throws ValidatorException {
        if (component instanceof EditableValueHolder) {
            // Validate input component
            EditableValueHolder input = (EditableValueHolder) component;
            try {
                ValueExpression valueExpression = component.getValueExpression("value");
                if (null != valueExpression) {
                    Collection<String> messages =
                        ObjectValidator.getInstance(context).validate(context, valueExpression, convertedValue,
                            getProfiles());
                    if (null != messages) {
                        input.setValid(false);
                        Object label = getLabel(context, component);
                        Locale locale = context.getViewRoot().getLocale();
                        // send all validation messages.
                        for (String msg : messages) {
                            // https://jira.jboss.org/jira/browse/RF-7636 -
                            // format values.
                            String formattedMessage = formatMessage(msg, locale, label, convertedValue); // create Summary message ?
                            String summaryString = getSummary() != null ? getSummary() : formattedMessage;
                            summaryString = formatMessage(summaryString, locale, label, convertedValue);
                            context.addMessage(component.getClientId(context), new FacesMessage(
                                FacesMessage.SEVERITY_ERROR, summaryString, formattedMessage));
                        }
                    }
                }
            } catch (ELException e) {
                throw new FacesException(e);
            }
        }
    }

    static String formatMessage(String msg, Locale locale, Object... messageParams) {
        if (msg.contains("{")) {
            try {
                MessageFormat messageFormat = new MessageFormat(msg, locale);
                msg = messageFormat.format(messageParams);
            } catch (IllegalArgumentException e) {
                // Do nothing, use original string unchanged.
            }
        }
        return msg;
    }

    static Object getLabel(FacesContext context, UIComponent component) {
        Object label = component.getAttributes().get("label");
        if (null == label || 0 == label.toString().length()) {
            label = component.getClientId(context);
        }
        return label;
    }

    public Collection<String> validateGraph(FacesContext context, UIComponent component, Object value,
        Set<String> profiles) throws ValidatorException {
        ObjectValidator beanValidator = ObjectValidator.getInstance(context);
        Collection<String> messages = beanValidator.validateGraph(context, value, profiles);
        return messages;
    }

    /**
     * @return the profiles
     */
    public Set<String> getProfiles() {
        Set<String> profiles;
        if (null != profilesExpression) {
            profiles = (Set<String>) profilesExpression.getValue(FacesContext.getCurrentInstance().getELContext());
        } else {
            profiles = this.profiles;
        }
        return profiles;
    }

    /**
     * @param profiles
     *            the profiles to set
     */
    public void setProfiles(Set<String> profiles) {
        this.profiles = profiles;
    }

    public void setProfiles(ValueExpression profilesExpression) {
        this.profilesExpression = profilesExpression;
    }
}
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

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.richfaces.services.ServiceTracker;
import org.richfaces.cdk.annotations.JsfValidator;

import com.google.common.base.Strings;

/**
 * Implementation of the JSF validator to use with Bean Validation / Hibernate validator TODO - implement partial state saving.
 *
 * @author asmirnov
 *
 */
@JsfValidator(id = FacesBeanValidator.BEAN_VALIDATOR_TYPE)
public class FacesBeanValidator implements Serializable, Validator, GraphValidator {
    public static final String BEAN_VALIDATOR_TYPE = "org.richfaces.BeanValidator";
    /**
     *
     */
    private static final long serialVersionUID = -264568176252121853L;
    private ValueExpression summaryExpression = null;
    private String summary = null;
    private ValueExpression groupsExpression = null;
    private Class<?>[] groups = null;
    private boolean validateFields = true;

    public FacesBeanValidator() {
    }

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
     * @param summary the summary to set
     */
    public void setSummary(ValueExpression summary) {
        this.summaryExpression = summary;
    }

    /**
     * @param summary the summary to set
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.validator.Validator#validate(javax.faces.context.FacesContext, javax.faces.component.UIComponent,
     * java.lang.Object)
     */
    public void validate(FacesContext context, UIComponent component, Object convertedValue) throws ValidatorException {
        if (component instanceof EditableValueHolder) {
            // Validate input component
            EditableValueHolder input = (EditableValueHolder) component;
            try {
                ValueExpression valueExpression = component.getValueExpression("value");
                if (null != valueExpression) {
                    BeanValidatorService validatorService = ServiceTracker.getService(BeanValidatorService.class);
                    Collection<String> messages = validatorService.validateExpression(context, valueExpression, convertedValue,
                        getGroups());
                    if (isValidateFields() && !messages.isEmpty()) {
                        input.setValid(false);
                        Object label = getLabel(context, component);
                        Locale locale = context.getViewRoot().getLocale();
                        // send all validation messages.
                        for (String msg : messages) {
                            // https://jira.jboss.org/jira/browse/RF-7636 -
                            // format values.
                            String formattedMessage = formatMessage(msg, locale, label, convertedValue); // create Summary
                                                                                                         // message ?
                            String summary = getSummary();
                            String formattedSummary = Strings.isNullOrEmpty(summary) ? formattedMessage : formatMessage(
                                summary, locale, label, convertedValue);
                            context.addMessage(component.getClientId(context), new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                formattedSummary, formattedMessage));
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

    public Collection<String> validateGraph(FacesContext context, UIComponent component, Object value, Class<?>[] groups)
        throws ValidatorException {
        BeanValidatorService validatorService = ServiceTracker.getService(BeanValidatorService.class);
        Collection<String> messages = validatorService.validateObject(context, value, groups);
        return messages;
    }

    /**
     * @return the profiles
     */
    public Class<?>[] getGroups() {
        Class<?>[] profiles;
        if (null != groupsExpression) {
            profiles = (Class<?>[]) groupsExpression.getValue(FacesContext.getCurrentInstance().getELContext());
        } else {
            profiles = this.groups;
        }
        return profiles;
    }

    /**
     * @param profiles the profiles to set
     */
    public void setGroups(Class<?>... profiles) {
        this.groups = profiles;
    }

    public void setGroups(ValueExpression profilesExpression) {
        this.groupsExpression = profilesExpression;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the ignoreErrors
     */
    public boolean isValidateFields() {
        return this.validateFields;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param ignoreErrors the ignoreErrors to set
     */
    public void setValidateFields(boolean ignoreErrors) {
        this.validateFields = ignoreErrors;
    }
}
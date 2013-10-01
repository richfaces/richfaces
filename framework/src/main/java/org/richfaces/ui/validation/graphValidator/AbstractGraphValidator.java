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
package org.richfaces.ui.validation.graphValidator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.services.ServiceTracker;
import org.richfaces.validator.BeanValidatorService;
import org.richfaces.validator.FacesBeanValidator;
import org.richfaces.validator.GraphValidatorState;

/**
 * <p>The &lt;r:graphValidator&gt; component is used to wrap a set of input components related to one object. The
 * object defined by the &lt;r:graphValidator&gt; component can then be completely validated. The validation includes
 * all object properties, even those which are not bound to the individual form components. Validation performed in this
 * way allows for cross-field validation in complex forms.</p>
 */
@JsfComponent(tag = @Tag(name = "graphValidator", type = TagType.Facelets, handlerClass = GraphValidatorHandler.class))
public abstract class AbstractGraphValidator extends UIComponentBase {
    public static final String COMPONENT_TYPE = "org.richfaces.ui.GraphValidator";
    public static final String COMPONENT_FAMILY = "org.richfaces.ui.GraphValidator";

    /**
     * Bean EL reference to be validated
     */
    @Attribute
    public abstract Object getValue();

    public abstract void setValue(Object newvalue);

    /**
     * Message summary will be used in case in case of validation error when no other message is provided.
     */
    @Attribute
    public abstract String getSummary();

    public abstract void setSummary(String newvalue);

    /**
     * The list of fully-qualified names (FQNs) of classes determining bean-validation groups to be validated.
     */
    @Attribute
    public abstract Class<?>[] getGroups();

    public abstract void setGroups(Class<?>[] newvalue);

    /**
     * The validator-id of validator used to process validation of the provided bean (Default value: org.richfaces.BeanValidator)
     */
    @Attribute(defaultValue = FacesBeanValidator.BEAN_VALIDATOR_TYPE)
    public abstract String getType();

    public abstract void setType(String newvalue);

    @Override
    public void processDecodes(FacesContext context) {
        GraphValidatorState validatorState = null;
        boolean wasActive = false;
        Object value = getValue();
        if (null != value) {
            validatorState = GraphValidatorState.getState(context, value);
            if (null != validatorState) {
                // Reuse old value, if any.
                wasActive = validatorState.isActive();
                validatorState.setActive(true);
            } else if (value instanceof Cloneable) {
                try {
                    Method method = getCloneMethod(value.getClass());
                    validatorState = new GraphValidatorState(method.invoke(value));
                    validatorState.setActive(true);
                    GraphValidatorState.setState(context, value, validatorState);
                } catch (NoSuchMethodException e) {
                    // do nothing, that is really not possible - clone() impemented in Object.
                } catch (InvocationTargetException e) {
                    throw new FacesException(e);
                } catch (IllegalArgumentException e) {
                    // do nothing, that is really not possible - method has no arguments.
                } catch (IllegalAccessException e) {
                    throw new FacesException(e);
                }
            }
        }
        super.processDecodes(context);
        if (null != validatorState) {
            validatorState.setActive(wasActive);
        }
    }

    private Method getCloneMethod(Class<?> clazz) throws NoSuchMethodException {
        try {
            return clazz.getDeclaredMethod("clone");
        } catch (NoSuchMethodException e) {
            if (null != clazz.getSuperclass()) {
                Method method = getCloneMethod(clazz.getSuperclass());
                if (!Modifier.isPublic(method.getModifiers())) {
                    // Method Object#clone() is protected by default. Make it public
                    // unless developer did it.
                    method.setAccessible(true);
                }
                return method;
            } else {
                throw e;
            }
        }
    }

    protected GraphValidatorState getValidatorState(FacesContext context) {
        Object value = getValue();
        if (null != value) {
            return GraphValidatorState.getState(context, value);
        } else {
            return null;
        }
    }

    @Override
    public void processValidators(FacesContext context) {
        GraphValidatorState validatorState = getValidatorState(context);
        if (null != validatorState) {
            boolean wasActive = validatorState.isActive();
            validatorState.setActive(true);
            super.processValidators(context);
            validatorState.setActive(wasActive);
            if (!context.isValidationFailed()) {
                validateObject(context, validatorState.getCloned());
            }
        } else {
            super.processValidators(context);
        }
    }

    @Override
    public void processUpdates(FacesContext context) {
        super.processUpdates(context);
        // Validate updated object if it was not done on clone.
        if (!context.isValidationFailed()) {
            Object value = getValue();
            if (null != value) {
                if (null == GraphValidatorState.getState(context, value)) {
                    validateObject(context, value);
                }
            }
        }
    }

    private void validateObject(FacesContext context, Object value) {
        if (null != value) {
            Collection<String> messages;
            BeanValidatorService validatorService = ServiceTracker.getService(BeanValidatorService.class);
            messages = validatorService.validateObject(context, value, getGroups());
            if (!messages.isEmpty()) {
                context.validationFailed();
                context.renderResponse();
                // send all validation messages.
                String clientId = getClientId(context);
                for (String msg : messages) {
                    String summary = null != getSummary() ? getSummary() : msg;
                    context.addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, msg));
                }
            }
        }
    }

    public Validator createChildrenValidator() {
        FacesBeanValidator validator = (FacesBeanValidator) getFacesContext().getApplication().createValidator(getType());
        validator.setSummary(getSummary());
        ValueExpression expression = getValueExpression("groups");
        if (null != expression) {
            validator.setGroups(expression);
        } else {
            validator.setGroups(getGroups());
        }
        return validator;
    }
}

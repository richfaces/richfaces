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
package org.richfaces.component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;

import org.richfaces.application.ServiceTracker;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.el.CapturingELResolver;
import org.richfaces.el.ELContextWrapper;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.validator.BeanValidatorService;
import org.richfaces.validator.FacesBeanValidator;
import org.richfaces.validator.GraphValidatorState;

/**
 * JSF component class
 * 
 */
@JsfComponent(tag=@Tag(name="graphValidator",type=TagType.Facelets, handler = "org.richfaces.view.facelets.html.GraphValidatorHandler"))
public abstract class AbstractGraphValidator extends UIComponentBase {

    public static final String COMPONENT_TYPE = "org.richfaces.GraphValidator";

    public static final String COMPONENT_FAMILY = "org.richfaces.GraphValidator";
    
    private static final Logger LOG = RichfacesLogger.COMPONENTS.getLogger();

    /**
     * Get object for validation
     * 
     * @return
     */
    @Attribute
    public abstract Object getValue();

    /**
     * Set object for validation
     * 
     * @param newvalue
     */
    public abstract void setValue(Object newvalue);

    /**
     * Get object for validation
     * 
     * @return
     */
    @Attribute
    public abstract String getSummary();

    /**
     * Set object for validation
     * 
     * @param newvalue
     */
    public abstract void setSummary(String newvalue);

    /**
     * Get set of profiles for validation
     * 
     * @return
     */
    @Attribute
    public abstract Class<?>[] getGroups();

    /**
     * Set set of profiles for validation
     * 
     * @param newvalue
     */
    public abstract void setGroups(Class<?>[] newvalue);

    /**
     * Get graph validator Id.
     * 
     * @return
     */
    @Attribute(defaultValue="org.richfaces.BeanValidator")
    public abstract String getType();

    /**
     * Set graph validator Id.
     * 
     * @param newvalue
     */
    public abstract void setType(String newvalue);

    @Override
    public void processDecodes(FacesContext context) {
        GraphValidatorState validatorState = null;
        // Detect value EL-expression.
        ValueExpression valueExpression = getValueExpression("value");
        if (null != valueExpression) {

            Object value = getValue();
            if (null != value && value instanceof Cloneable) {
                try {
                    ELContext initialELContext = context.getELContext();

                    CapturingELResolver capturingELResolver = new CapturingELResolver(initialELContext.getELResolver());
                    Class<?> type =
                        valueExpression.getType(new ELContextWrapper(initialELContext, capturingELResolver));
                    if (null != type) {
                        validatorState = new GraphValidatorState();
                        Method method = getCloneMethod(value.getClass());
                        if (!Modifier.isPublic(method.getModifiers())) {
                            // Method Object#clone() is protected by default. Make it public
                            // unless developer did it.
                            method.setAccessible(true);
                        }
                        validatorState.setCloned(method.invoke(value));
                        validatorState.setBase(capturingELResolver.getBase());
                        validatorState.setProperty(capturingELResolver.getProperty());
                        validatorState.setActive(true);
                        context.getExternalContext().getRequestMap().put(getStateId(context), validatorState);
                    }
                } catch (NoSuchMethodException e) {
                    // do nothing, that is really not possible.
                } catch (InvocationTargetException e) {
                    throw new FacesException(e);
                } catch (IllegalArgumentException e) {
                    // do nothing, that is really not possible.
                } catch (IllegalAccessException e) {
                    throw new FacesException(e);
                }
            }
        }
        super.processDecodes(context);
        if (null != validatorState) {
            validatorState.setActive(false);
        }
    }

    private Method getCloneMethod(Class<?> clazz) throws NoSuchMethodException {
        try {
            return clazz.getDeclaredMethod("clone");
        } catch (NoSuchMethodException e) {
            if (null != clazz.getSuperclass()) {
                return getCloneMethod(clazz.getSuperclass());
            } else {
                throw e;
            }
        }
    }

    protected String getStateId(FacesContext context) {
        String stateId = GraphValidatorState.STATE_ATTRIBUTE_PREFIX + getClientId(context);
        return stateId;
    }

    protected GraphValidatorState getValidatorState(FacesContext context) {
        return (GraphValidatorState) context.getExternalContext().getRequestMap().get(getStateId(context));
    }

    @Override
    public void processValidators(FacesContext context) {
        GraphValidatorState validatorState = getValidatorState(context);
        if (null != validatorState) {
            validatorState.setActive(true);
        }
        super.processValidators(context);
        if (null != validatorState) {
            validatorState.setActive(false);
            validateObject(context, validatorState.getCloned());
            context.getExternalContext().getRequestMap().remove(getStateId(context));
        }
    }

    @Override
    public void processUpdates(FacesContext context) {
        super.processUpdates(context);
        Object value = getValue();
        validateObject(context, value);
    }

    private void validateObject(FacesContext context, Object value) {
        if (null != value) {
            Collection<String> messages;
            BeanValidatorService validatorService = ServiceTracker.getService(BeanValidatorService.class);
            messages = validatorService.validateObject(context, value, getGroups());
            if (!messages.isEmpty()) {
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

    public Validator createChildrenValidator(FacesContext context) {
        FacesBeanValidator validator = (FacesBeanValidator) context.getApplication().createValidator(getType());
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

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

/**
 *
 */
package org.richfaces.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.ElementDescriptor.ConstraintFinder;
import javax.validation.metadata.PropertyDescriptor;

import org.richfaces.el.ValueDescriptor;
import org.richfaces.el.ValueExpressionAnalayser;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;

/**
 * @author asmirnov
 *
 */
public class BeanValidatorServiceImpl implements BeanValidatorService {
    private static final Collection<String> HIDDEN_PARAMS = ImmutableSet.of("message", "payload", "groups");
    private static final String FACES_CONTEXT_IS_NULL = "Faces context is null";
    private static final String INPUT_PARAMETERS_IS_NOT_CORRECT = "Input parameters is not correct.";
    private static final Class<?>[] DEFAULT_GROUP = {};
    private final ValueExpressionAnalayser analayser;
    private final BeanValidatorFactory validatorFactory;

    public BeanValidatorServiceImpl(ValueExpressionAnalayser analayser, BeanValidatorFactory validatorFactory) {
        this.analayser = analayser;
        this.validatorFactory = validatorFactory;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.validator.BeanValidatorService#getConstrains(javax.faces.context.FacesContext,
     * javax.el.ValueExpression, java.lang.Class<?>[])
     */
    public Collection<ValidatorDescriptor> getConstrains(FacesContext context, ValueExpression expression, String message,
        Class<?>... groups) {
        try {
            ValueDescriptor propertyDescriptor = analayser.getPropertyDescriptor(context, expression);

            if (propertyDescriptor == null) {
                return Collections.emptySet();
            }

            return processBeanAttribute(context, propertyDescriptor, message, groups);
        } catch (ELException e) {
            return Collections.emptySet();
        }
    }

    private Validator getValidator(FacesContext context) {
        return validatorFactory.getValidator(context);
    }

    Collection<ValidatorDescriptor> processBeanAttribute(FacesContext context, ValueDescriptor descriptor, String msg,
        Class<?>... groups) {
        PropertyDescriptor constraintsForProperty = getValidator(context).getConstraintsForClass(descriptor.getBeanType())
            .getConstraintsForProperty(descriptor.getName());
        if (null != constraintsForProperty) {
            ConstraintFinder propertyConstraints = constraintsForProperty.findConstraints();
            if (null != groups && groups.length > 0) {
                // Filter groups, if required
                propertyConstraints = propertyConstraints.unorderedAndMatchingGroups(groups);
            }
            Set<ConstraintDescriptor<?>> constraints = propertyConstraints // or the requested list of groups)
                .getConstraintDescriptors();

            // ContextHolder is an arbitrary object, it will depend on the implementation
            FacesMessage message = Strings.isNullOrEmpty(msg) ? null : new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
            return processConstraints(context, constraints, message);
        } else {
            return Collections.emptySet();
        }
    }

    Collection<ValidatorDescriptor> processConstraints(FacesContext context, Set<ConstraintDescriptor<?>> constraints,
        FacesMessage msg) {
        Set<ValidatorDescriptor> descriptors = new HashSet<ValidatorDescriptor>(constraints.size());
        for (ConstraintDescriptor<?> cd : constraints) {
            Annotation a = cd.getAnnotation();
            Map<String, Object> parameters = cd.getAttributes();
            // TODO if cd.isReportedAsSingleConstraint() make sure than only the root constraint raises an error message
            // if one or several of the composing constraints are invalid)
            FacesMessage message = null == msg ? validatorFactory.interpolateMessage(context, cd) : msg;
            Class<? extends Annotation> validatorClass = findAnnotationClass(a);
            BeanValidatorDescriptor beanValidatorDescriptor = new BeanValidatorDescriptor(validatorClass, message);
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                String key = entry.getKey();
                if (!HIDDEN_PARAMS.contains(key)) {
                    Object value = entry.getValue();
                    try {
                        Method method = validatorClass.getDeclaredMethod(key);
                        Object defaultValue = method.getDefaultValue();
                        if (!value.equals(defaultValue)) {
                            beanValidatorDescriptor.addParameter(key, value);
                        }
                    } catch (SecurityException e) {
                        beanValidatorDescriptor.addParameter(key, value);
                    } catch (NoSuchMethodException e) {
                        beanValidatorDescriptor.addParameter(key, value);
                    }
                }
            }
            beanValidatorDescriptor.makeImmutable();
            descriptors.add(beanValidatorDescriptor);
            descriptors.addAll(processConstraints(context, cd.getComposingConstraints(), msg)); // process the composing
                                                                                                // constraints
        }
        return descriptors;
    }

    private Class<? extends Annotation> findAnnotationClass(Annotation a) {
        Class<? extends Annotation> annotationClass = a.getClass();
        // RF-10311, Hibernate validator wraps annotation class with proxy;
        if (!annotationClass.isAnnotation()) {
            Class<?>[] interfaces = annotationClass.getInterfaces();
            for (Class<?> implemented : interfaces) {
                if (implemented.isAnnotation()) {
                    annotationClass = (Class<? extends Annotation>) implemented;
                }
            }
        }
        return annotationClass;
    }

    public Collection<String> validateExpression(FacesContext context, ValueExpression expression, Object newValue,
        Class<?>... groups) {

        if (null == context) {
            throw new FacesException(INPUT_PARAMETERS_IS_NOT_CORRECT);
        }

        Collection<String> validationMessages = null;
        if (null != expression) {
            ValueDescriptor valueDescriptor;
            try {
                valueDescriptor = analayser.updateValueAndGetPropertyDescriptor(context, expression, newValue);
            } catch (ELException e) {
                throw new FacesException(e);
            }

            if (valueDescriptor != null) {
                validationMessages = validate(context, valueDescriptor.getBeanType(), valueDescriptor.getName(), newValue,
                    groups);
            }
        }

        if (validationMessages == null) {
            validationMessages = Collections.emptySet();
        }

        return validationMessages;
    }

    /**
     * Class for identify validator instance by locale
     *
     * @author amarkhel
     *
     */
    protected static class ValidatorKey {
        private final Class<? extends Object> validatableClass;
        private final Locale locale;

        /**
         * Constructor for ValidatorKey object
         *
         * @param validatableClass - class to validate
         * @param locale - User locale to determine Resource bundle, used during validation process
         */
        public ValidatorKey(Class<? extends Object> validatableClass, Locale locale) {
            this.validatableClass = validatableClass;
            this.locale = locale;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((this.locale == null) ? 0 : this.locale.hashCode());
            result = prime * result + ((this.validatableClass == null) ? 0 : this.validatableClass.hashCode());
            return result;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            ValidatorKey other = (ValidatorKey) obj;
            if (this.locale == null) {
                if (other.locale != null) {
                    return false;
                }
            } else if (!this.locale.equals(other.locale)) {
                return false;
            }
            if (this.validatableClass == null) {
                if (other.validatableClass != null) {
                    return false;
                }
            } else if (!this.validatableClass.equals(other.validatableClass)) {
                return false;
            }
            return true;
        }
    }

    protected Collection<String> validate(FacesContext facesContext, Class<?> beanType, String property, Object value,
        Class<?>[] groups) {

        @SuppressWarnings("unchecked")
        Set<ConstraintViolation<Object>> constrains = getValidator(facesContext).validateValue((Class<Object>) beanType,
            property, value, getGroups(groups));
        return extractMessages(constrains);
    }

    private Class<?>[] getGroups(Class<?>[] groups) {
        return null == groups ? DEFAULT_GROUP : groups;
    }

    public Collection<String> validateObject(FacesContext context, Object value, Class<?>... groups) {
        Set<ConstraintViolation<Object>> violations = getValidator(context).validate(value, getGroups(groups));
        Collection<String> messages = extractMessages(violations);
        return messages;
    }

    private Collection<String> extractMessages(Set<ConstraintViolation<Object>> violations) {
        Collection<String> messages;
        if (null != violations && violations.size() > 0) {
            messages = new ArrayList<String>(violations.size());
            for (ConstraintViolation<? extends Object> constraintViolation : violations) {
                messages.add(constraintViolation.getMessage());
            }
        } else {
            messages = Collections.emptySet();
        }
        return messages;
    }
}

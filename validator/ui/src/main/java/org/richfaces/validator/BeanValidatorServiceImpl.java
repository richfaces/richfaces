/**
 * 
 */
package org.richfaces.validator;

import java.beans.FeatureDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.ElementDescriptor.ConstraintFinder;
import javax.validation.metadata.PropertyDescriptor;

import org.richfaces.el.ELContextWrapper;
import org.richfaces.el.ValueDescriptor;
import org.richfaces.el.ValueExpressionAnalayser;

import com.google.common.collect.ImmutableSet;

/**
 * @author asmirnov
 * 
 */
public class BeanValidatorServiceImpl implements BeanValidatorService {

    private static final Collection<String> HIDDEN_PARAMS = ImmutableSet.of("message","payload","groups");
    private static final String FACES_CONTEXT_IS_NULL = "Faces context is null";
    private static final String INPUT_PARAMETERS_IS_NOT_CORRECT = "Input parameters is not correct.";
    private static final Class<?>[] DEFAULT_GROUP={};
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
    public Collection<ValidatorDescriptor> getConstrains(FacesContext context, ValueExpression expression,
        Class<?>... groups) {
        try {
            ValueDescriptor propertyDescriptor = analayser.getPropertyDescriptor(context, expression);
            return processBeanAttribute(context, propertyDescriptor, groups);
        } catch (ELException e) {
            return Collections.emptySet();
        }
    }

    private Validator getValidator(FacesContext context) {
        return validatorFactory.getValidator(context);
    }

    Collection<ValidatorDescriptor> processBeanAttribute(FacesContext context, ValueDescriptor descriptor,
        Class<?>... groups) {
        PropertyDescriptor constraintsForProperty =
            getValidator(context).getConstraintsForClass(descriptor.getBeanType()).getConstraintsForProperty(
                descriptor.getName());
        if (null != constraintsForProperty) {
            ConstraintFinder propertyConstraints = constraintsForProperty.findConstraints();
            if (null != groups && groups.length > 0) {
                // Filter groups, if required
                propertyConstraints = propertyConstraints.unorderedAndMatchingGroups(groups);
            }
            Set<ConstraintDescriptor<?>> constraints = propertyConstraints // or the requested list of groups)
                .getConstraintDescriptors();

            // ContextHolder is an arbitrary object, it will depend on the implementation
            Set<ValidatorDescriptor> descriptors = new HashSet<ValidatorDescriptor>(constraints.size());
            processConstraints(context, constraints, descriptors);
            return descriptors;

        } else {
            return Collections.emptySet();
        }
    }

    void processConstraints(FacesContext context, Set<ConstraintDescriptor<?>> constraints,
        Collection<ValidatorDescriptor> descriptors) {
        for (ConstraintDescriptor<?> cd : constraints) {
            Annotation a = cd.getAnnotation();
            Map<String, Object> parameters = cd.getAttributes();
            // TODO if cd.isReportedAsSingleConstraint() make sure than only the root constraint raises an error message
            // if one or several of the composing constraints are invalid)
            FacesMessage message = validatorFactory.interpolateMessage(context, cd);
            Class<? extends Annotation> validatorClass = findAnnotationClass(a);
            BeanValidatorDescriptor beanValidatorDescriptor = new BeanValidatorDescriptor(validatorClass, message);
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                String key = entry.getKey();
                if (!HIDDEN_PARAMS.contains(key)) {
                    Object value = entry.getValue();
                    try {
                        Method method = validatorClass.getDeclaredMethod(key);
                        Object defaultValue = method.getDefaultValue();
                        if(!value.equals(defaultValue)){
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
            processConstraints(context, cd.getComposingConstraints(), descriptors); // process the composing constraints
        }
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
        Collection<String> validationMessages = Collections.emptySet();
        if (null != expression) {
            ELContext elContext = context.getELContext();
            ValidationResolver validationResolver =
                createValidationResolver(context, elContext.getELResolver(), groups);
            ELContextWrapper wrappedElContext = new ELContextWrapper(elContext, validationResolver);
            try {
                expression.setValue(wrappedElContext, newValue);
            } catch (ELException e) {
                throw new FacesException(e);
            }
            if (!validationResolver.isValid()) {
                validationMessages = validationResolver.getValidationMessages();
            }
        }
        return validationMessages;
    }


    protected ValidationResolver createValidationResolver(FacesContext context, ELResolver parent, Class<?>[] groups) {
        return new ValidationResolver(parent, context, groups);
    }

    /**
     * @author asmirnov
     * 
     */
    protected static class BasePropertyPair {
        private final Object base;
        private final Object property;

        /**
         * @param base
         * @param property
         */
        public BasePropertyPair(Object base, Object property) {
            this.base = base;
            this.property = property;
        }

        /**
         * @return the base
         */
        public Object getBase() {
            return base;
        }

        /**
         * @return the property
         */
        public Object getProperty() {
            return property;
        }

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
         * @param validatableClass
         *            - class to validate
         * @param locale
         *            - User locale to determine Resource bundle, used during validation process
         */
        public ValidatorKey(Class<? extends Object> validatableClass, Locale locale) {
            this.validatableClass = validatableClass;
            this.locale = locale;
        }

        /* (non-Javadoc)
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

        /* (non-Javadoc)
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

    /**
     * Wrapper class for a {@link ELResolver}. For a setValue method, perform validation instead of real assignment.
     * 
     * @author asmirnov
     * 
     */
    final class ValidationResolver extends ELResolver {

        /**
         * Original resolver.
         */
        private final ELResolver parent;

        private boolean valid = true;

        private Collection<String> validationMessages = null;

        private Stack<BasePropertyPair> valuesStack;

        private Class<?>[] groups;

        private FacesContext facesContext;

        private boolean clonedObject = false;

        /**
         * @param parent
         * @param context
         */
        public ValidationResolver(ELResolver parent, FacesContext context, Class<?>[] groups) {
            this.parent = parent;
            this.valuesStack = new Stack<BasePropertyPair>();
            this.groups = groups;
            this.facesContext = context;
        }

        public boolean isValid() {
            return valid;
        }

        /**
         * @param context
         * @param base
         * @return
         * @see javax.el.ELResolver#getCommonPropertyType(javax.el.ELContext, java.lang.Object)
         */
        public Class<?> getCommonPropertyType(ELContext context, Object base) {
            return parent.getCommonPropertyType(context, base);
        }

        /**
         * @param context
         * @param base
         * @return
         * @see javax.el.ELResolver#getFeatureDescriptors(javax.el.ELContext, java.lang.Object)
         */
        public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
            return parent.getFeatureDescriptors(context, base);
        }

        /**
         * @param context
         * @param base
         * @param property
         * @return
         * @see javax.el.ELResolver#getType(javax.el.ELContext, java.lang.Object, java.lang.Object)
         */
        public Class<?> getType(ELContext context, Object base, Object property) {
            return parent.getType(context, base, property);
        }

        /**
         * @param context
         * @param base
         * @param property
         * @return
         * @see javax.el.ELResolver#getValue(javax.el.ELContext, java.lang.Object, java.lang.Object)
         */
        public Object getValue(ELContext context, Object base, Object property) {
            Object value = ClonedObjectResolver.resolveCloned(context, base, property);
            if (null != value) {
                this.clonedObject = true;
                context.setPropertyResolved(true);
            } else {
                value = parent.getValue(context, base, property);
            }
            valuesStack.push(new BasePropertyPair(base, property));
            return value;
        }

        /**
         * @param context
         * @param base
         * @param property
         * @return
         * @see javax.el.ELResolver#isReadOnly(javax.el.ELContext, java.lang.Object, java.lang.Object)
         */
        public boolean isReadOnly(ELContext context, Object base, Object property) {
            return parent.isReadOnly(context, base, property);
        }

        /**
         * @param context
         * @param base
         * @param property
         * @param value
         * @see javax.el.ELResolver#setValue(javax.el.ELContext, java.lang.Object, java.lang.Object, java.lang.Object)
         */
        public void setValue(ELContext context, Object base, Object property, Object value) {
            if (null != base && null != property) {
                // TODO - detect value object from inderect references ( e.g. data table variables ).
                if (this.clonedObject) {
                    parent.setValue(context, base, property, value);
                }
                context.setPropertyResolved(true);
                // For Arrays, Collection or Map use parent base and property.
                BasePropertyPair basePropertyPair = lookupBeanProperty(new BasePropertyPair(base, property));
                base = basePropertyPair.getBase();
                property = basePropertyPair.getProperty();
                if (null != base && null != property) {
                    // https://jira.jboss.org/jira/browse/RF-4034
                    // apache el looses locale information during value
                    // resolution,
                    // so we use our own
                    validationMessages = validate(facesContext, base, property.toString(), value, groups);
                    valid = null == validationMessages || 0 == validationMessages.size();

                }
            }
        }

        private BasePropertyPair lookupBeanProperty(BasePropertyPair pair) {
            Object base = pair.getBase();
            if (null != base && (base instanceof Collection || base instanceof Map || base.getClass().isArray())) {
                try {
                    pair = lookupBeanProperty(valuesStack.pop());
                } catch (EmptyStackException e) {
                    // Do nothing, this is a first item.
                }
            }
            return pair;
        }

        /**
         * @return the validationMessages
         */
        public Collection<String> getValidationMessages() {
            return validationMessages;
        }

    }

    protected Collection<String> validate(FacesContext facesContext, Object base, String property, Object value,
        Class<?>[] groups) {
        @SuppressWarnings("rawtypes")
        Class beanType = base.getClass();
        @SuppressWarnings("unchecked")
        Set<ConstraintViolation<Object>> constrains =
            getValidator(facesContext).validateValue(beanType, property, value, getGroups(groups));
        return extractMessages(constrains);
    }

    private Class<?>[] getGroups(Class<?>[] groups) {
        return null==groups?DEFAULT_GROUP:groups;
    }

    public Collection<String> validateObject(FacesContext context, Object value, Class<?>... groups) {
        Set<ConstraintViolation<Object>> violations = getValidator(context).validate(value, getGroups(groups));
        Collection<String> messages = extractMessages(violations);
        return messages;
    }

    private Collection<String> extractMessages(Set<ConstraintViolation<Object>> violations) {
        Collection<String> messages = null;
        if (null != violations && violations.size() > 0) {
            messages = new ArrayList<String>(violations.size());
            for (ConstraintViolation<? extends Object> constraintViolation : violations) {
                messages.add(constraintViolation.getMessage());
            }

        }
        return messages;
    }

}

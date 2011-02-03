/**
 * 
 */
package org.richfaces.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.validation.Validator;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.ElementDescriptor.ConstraintFinder;
import javax.validation.metadata.PropertyDescriptor;

import org.richfaces.el.ValueDescriptor;
import org.richfaces.el.ValueExpressionAnalayser;

import com.google.common.collect.ImmutableSet;

/**
 * @author asmirnov
 * 
 */
public class BeanValidatorServiceImpl implements BeanValidatorService {

    private static final Collection<String> HIDDEN_PARAMS = ImmutableSet.of("message","payload","groups");
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
}

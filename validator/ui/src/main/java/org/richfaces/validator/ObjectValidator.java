package org.richfaces.validator;

import java.beans.FeatureDescriptor;
import java.util.Collection;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Stack;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import org.richfaces.application.ServiceTracker;
import org.richfaces.el.ELContextWrapper;

public abstract class ObjectValidator {

    private static final String RESOURCE_BUNDLE_IS_NOT_REGISTERED_FOR_CURRENT_LOCALE =
        "Resource bundle is not registered for current locale";

    private static final String FACES_CONTEXT_IS_NULL = "Faces context is null";
    private static final String INPUT_PARAMETERS_IS_NOT_CORRECT = "Input parameters is not correct.";
    private static final String LOCALE_IS_NOT_SET = "Locale is not set";
    private static final String VIEW_ROOT_IS_NOT_INITIALIZED = "ViewRoot is not initialized";

    protected final ObjectValidator parent;

    ObjectValidator() {
        this.parent = null;
    }

    ObjectValidator(ObjectValidator parent) {
        this.parent = parent;
    }

    /**
     * Return BeanValidator object from a ServletContext attribute. Create new instance if none is defined.
     * 
     * @param context
     * @return
     */
    public static ObjectValidator getInstance(FacesContext context) {
        return ServiceTracker.getService(context, ObjectValidator.class);
    }

    public abstract Collection<String> validateGraph(FacesContext context, Object value, Set<String> profiles);

    /**
     * Perform Validation for a new value.
     * 
     * @param context
     *            current faces context.
     * @param target
     *            {@link ValueExpression} for a value assignment.
     * @param value
     *            new value for validation
     * @param profiles
     *            TODO
     * @return null if no validation errors. Array of the validation messages otherwise.
     * @throws FacesException
     *             if locale or context not properly initialized
     */
    public Collection<String> validate(FacesContext context, ValueExpression target, Object value, Set<String> profiles) {
        if (null == context) {
            throw new FacesException(INPUT_PARAMETERS_IS_NOT_CORRECT);
        }
        Collection<String> validationMessages = null;
        if (null != target) {
            ELContext elContext = context.getELContext();
            ValidationResolver validationResolver =
                createValidationResolver(context, elContext.getELResolver(), profiles);
            ELContextWrapper wrappedElContext = new ELContextWrapper(elContext, validationResolver);
            try {
                target.setValue(wrappedElContext, value);
            } catch (ELException e) {
                throw new FacesException(e);
            }
            if (!validationResolver.isValid()) {
                validationMessages = validationResolver.getValidationMessages();
            }
            if (null != parent) {
                Collection<String> parentMessages = parent.validate(context, target, value, profiles);
                if (null != validationMessages) {
                    if (null != parentMessages) {
                        validationMessages.addAll(parentMessages);
                    }
                } else {
                    validationMessages = parentMessages;
                }
            }

        }
        return validationMessages;
    }

    /**
     * Validate bean property for a new value.
     * 
     * @param facesContext
     *            TODO
     * @param base
     *            - bean
     * @param property
     *            - bean property name.
     * @param value
     *            new value.
     * @param profiles
     *            TODO
     * 
     * @return null for a valid value, array of the validation messages othervise.
     */
    protected abstract Collection<String> validate(FacesContext facesContext, Object base, String property,
        Object value, Set<String> profiles);

    static Locale calculateLocale(FacesContext context) {
        if (null == context.getViewRoot()) {
            throw new FacesException(VIEW_ROOT_IS_NOT_INITIALIZED);
        } else if (null == context.getViewRoot().getLocale()) {
            throw new FacesException(LOCALE_IS_NOT_SET);
        }
        Locale locale = context.getViewRoot().getLocale();
        return locale;
    }

    static ResourceBundle getResourceBundle(FacesContext facesContext, String name) {
        ResourceBundle bundle = null;
        if (null != facesContext) {
            Application application = facesContext.getApplication();
            try {
                bundle = application.getResourceBundle(facesContext, name);

            } catch (Exception e) {
                // Let one more attempt to load resource
            }
        }
        if (null == bundle) {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (null == classLoader) {
                classLoader = ObjectValidator.class.getClassLoader();
            }
            try {
                bundle = ResourceBundle.getBundle(name, calculateLocale(facesContext), classLoader);

            } catch (MissingResourceException e) {
                // Do nothing, use default bundle.
            }
        }
        return bundle;
    }

    protected ValidationResolver createValidationResolver(FacesContext context, ELResolver parent, Set<String> profiles) {
        return new ValidationResolver(parent, context, profiles);
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

        private Set<String> profiles;

        private FacesContext facesContext;

        private boolean clonedObject = false;

        /**
         * @param parent
         * @param context
         */
        public ValidationResolver(ELResolver parent, FacesContext context, Set<String> profiles) {
            this.parent = parent;
            this.valuesStack = new Stack<BasePropertyPair>();
            this.profiles = profiles;
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
                    validationMessages = validate(facesContext, base, property.toString(), value, profiles);
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

}
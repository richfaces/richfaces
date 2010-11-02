/**
 * 
 */
package org.richfaces.el;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
import java.util.Locale;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.FunctionMapper;
import javax.el.VariableMapper;

/**
 * This class wraps original ELContext and capture whole call stack to the target object so it could be used to extract
 * semantic information like annotations or Jena Model properties.
 * 
 * @author asmirnov
 * 
 */
public class CapturingELContext extends ELContext {

    private final ELContext parent;

    private ValueReference reference = null;

    private final InterceptingResolver resolver;

    public CapturingELContext(ELContext parent) {
        this.parent = parent;
        resolver = new InterceptingResolver(parent.getELResolver());
    }

    public ValueReference getReference() {
        return reference;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.el.ELContext#getELResolver()
     */
    @Override
    public ELResolver getELResolver() {
        return resolver;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getContext(Class key) {
        return parent.getContext(key);
    }

    @Override
    public FunctionMapper getFunctionMapper() {
        return parent.getFunctionMapper();
    }

    @Override
    public Locale getLocale() {
        return parent.getLocale();
    }

    @Override
    public VariableMapper getVariableMapper() {
        return parent.getVariableMapper();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void putContext(Class key, Object contextObject) {
        parent.putContext(key, contextObject);
    }

    @Override
    public void setLocale(Locale locale) {
        parent.setLocale(locale);
    }

    /**
     * This resolver records all intermediate objects from the EL-expression that can be used to detect Semantic Beans
     * annotations or Jena Model properties.
     * 
     * @author asmirnov
     * 
     */
    private final class InterceptingResolver extends ELResolver {

        private ELResolver delegate;

        public InterceptingResolver(ELResolver delegate) {
            this.delegate = delegate;
        }

        // Capture the base and property rather than write the value
        @Override
        public void setValue(ELContext context, Object base, Object property, Object value) {
            if (base != null) {
                context.setPropertyResolved(true);
                reference = new ValueReference(base, property, reference);
            }
        }

        // The rest of the methods simply delegate to the existing context

        @Override
        public Object getValue(ELContext context, Object base, Object property) {
            reference = new ValueReference(base, property, reference);
            return delegate.getValue(context, base, property);
        }

        @Override
        public Class<?> getType(ELContext context, Object base, Object property) {
            if (base != null) {
                context.setPropertyResolved(true);
                reference = new ValueReference(base, property, reference);
            }
            return delegate.getType(context, base, property);
        }

        @Override
        public boolean isReadOnly(ELContext context, Object base, Object property) {
            return delegate.isReadOnly(context, base, property);
        }

        @Override
        public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
            return delegate.getFeatureDescriptors(context, base);
        }

        @Override
        public Class<?> getCommonPropertyType(ELContext context, Object base) {
            return delegate.getCommonPropertyType(context, base);
        }

    }

}

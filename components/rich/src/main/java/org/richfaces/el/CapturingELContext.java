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
package org.richfaces.el;

import java.beans.FeatureDescriptor;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.FunctionMapper;
import javax.el.ValueExpression;
import javax.el.VariableMapper;
import javax.faces.el.CompositeComponentExpressionHolder;

import org.richfaces.validator.GraphValidatorState;

/**
 * This class wraps original ELContext and capture whole call stack to the target object so it could be used to extract semantic
 * information like annotations or Jena Model properties.
 *
 * @author asmirnov
 *
 */
public class CapturingELContext extends ELContext {
    private final ELContext parent;
    private ValueReference reference = null;
    private final InterceptingResolver resolver;

    public CapturingELContext(ELContext parent, Map<Object, GraphValidatorState> states) {
        this.parent = parent;
        resolver = new InterceptingResolver(parent.getELResolver(), states);
    }

    public ValueReference getReference() {
        return reference;
    }

    private boolean isContainerObject(Object base) {
        return base instanceof Collection || base instanceof Map || base.getClass().isArray();
    }

    public boolean hasReferenceExpression() {
        return reference != null && reference.getBase() instanceof CompositeComponentExpressionHolder;
    }

    public ValueExpression getReferenceExpression() {
        CompositeComponentExpressionHolder expressionHolder = (CompositeComponentExpressionHolder) reference.getBase();
        return expressionHolder.getExpression(reference.getProperty().toString());
    }

    public ValueDescriptor getDescriptor() {
        ValueReference localReference = reference;

        while (true) {
            if (localReference == null || localReference.getBase() == null || localReference.getProperty() == null) {
                return null;
            }

            Object base = localReference.getBase();

            if (isContainerObject(base) && localReference.hasNext()) {
                localReference = localReference.next();
            } else {
                return new ValueDescriptor(base.getClass(), localReference.getProperty().toString());
            }
        }
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
        private final ELResolver delegate;
        private boolean clonedObject;
        private final Map<Object, GraphValidatorState> states;

        public InterceptingResolver(ELResolver delegate, Map<Object, GraphValidatorState> states) {
            this.delegate = delegate;
            this.states = states;
        }

        // Capture the base and property rather than write the value
        @Override
        public void setValue(ELContext context, Object base, Object property, Object value) {
            if (base != null) {
                // TODO - detect value object from inderect references ( e.g. data table variables ).
                if (this.clonedObject) {
                    delegate.setValue(context, base, property, value);
                }

                context.setPropertyResolved(true);
                reference = new ValueReference(base, property, reference);
            }
        }

        // The rest of the methods simply delegate to the existing context

        @Override
        public Object getValue(ELContext context, Object base, Object property) {
            reference = new ValueReference(base, property, reference);
            Object value = delegate.getValue(context, base, property);
            if (null != value && context.isPropertyResolved() && states.containsKey(value)) {
                GraphValidatorState graphValidatorState = states.get(value);
                if (graphValidatorState.isActive()) {
                    this.clonedObject = true;
                    Object clone = graphValidatorState.getCloned();
                    return clone;
                }
            }
            return value;
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

        @Override
        public Object convertToType(ELContext context, Object obj, Class<?> targetType) {
            return delegate.convertToType(context, obj, targetType);
        }
    }
}

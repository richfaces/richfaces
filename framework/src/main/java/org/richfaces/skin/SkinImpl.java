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
package org.richfaces.skin;

import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import org.ajax4jsf.Messages;

/**
 * Singleton ( in respect as collection of different skins ) for produce instances properties for all used skins.
 *
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:59:41 $
 */
final class SkinImpl extends AbstractSkin {
    private static final Operation RESOLVE = new Operation() {
        public Object executeLocal(FacesContext facesContext, SkinImpl skin, String name) {
            Object resolvedParameter = skin.getLocalParameter(facesContext, name);

            while (resolvedParameter instanceof String) {
                String string = (String) resolvedParameter;

                if ((string.length() > 0) && (string.charAt(0) == '&')) {
                    SkinFactory skinFactory = SkinFactory.getInstance(facesContext);
                    resolvedParameter = skinFactory.getSkin(facesContext).getParameter(facesContext, string.substring(1));

                    if (resolvedParameter == null) {
                        throw new FacesException(Messages.getMessage(Messages.SKIN_ILLEGAL_REFERENCE, name));
                    }
                } else {
                    break;
                }
            }

            return resolvedParameter;
        }

        @Override
        public Object executeBase(FacesContext facesContext, Skin skin, String name) {
            return skin.getParameter(facesContext, name);
        }
    };
    private static final Operation CONTAINS = new Operation() {
        public Object executeLocal(FacesContext facesContext, SkinImpl skin, String name) {
            return skin.localContainsProperty(facesContext, name) ? Boolean.TRUE : Boolean.FALSE;
        }

        @Override
        public Object executeBase(FacesContext facesContext, Skin skin, String name) {
            return skin.containsProperty(name);
        }
    };
    private final Map<Object, Object> skinParams;

    private static class MutableInteger {
        private int value;

        public int getAndIncrement() {
            return value++;
        }

        public int getAndDecrement() {
            return value--;
        }
    }

    private String name;

    /**
     * Skin can instantiate only by factory method.
     *
     * @param skinName
     */
    SkinImpl(Map<Object, Object> properties, String name) {
        this.skinParams = properties;
        this.name = name;
    }

    private MutableInteger getCounter(FacesContext context) {
        Map<Object, Object> attr = context.getAttributes();

        MutableInteger counter = (MutableInteger) attr.get(MutableInteger.class);
        if (counter == null) {
            counter = new MutableInteger();
            attr.put(MutableInteger.class, counter);
        }

        return counter;
    }

    private abstract static class Operation {
        public abstract Object executeLocal(FacesContext facesContext, SkinImpl skin, String name);

        public abstract Object executeBase(FacesContext facesContext, Skin skin, String name);
    }

    protected Map<Object, Object> getSkinParams() {
        return skinParams;
    }

    public Object getParameter(FacesContext context, String name) {
        return getValueReference(context, resolveSkinParameter(context, name));
    }

    public Object getParameter(FacesContext context, String name, Object defaultValue) {
        Object value = getValueReference(context, resolveSkinParameter(context, name));

        if (null == value) {
            value = defaultValue;
        }

        return value;
    }

    protected Object getLocalParameter(FacesContext context, String name) {
        return getValueReference(context, skinParams.get(name));
    }

    /**
     * Calculate concrete value for property - if it stored as @see ValueBinding , return interpreted value.
     *
     * @param context
     * @param property
     * @return
     */
    protected Object getValueReference(FacesContext context, Object property) {
        if (property instanceof ValueExpression) {
            ValueExpression value = (ValueExpression) property;

            return value.getValue(context.getELContext());
        }

        return property;
    }

    public String toString() {
        return this.getClass().getSimpleName() + ": " + skinParams.toString();
    }

    protected Skin getBaseSkin(FacesContext context) {
        String baseSkinName = (String) getLocalParameter(context, Skin.BASE_SKIN);
        if (baseSkinName != null) {
            SkinFactory skinFactory = SkinFactory.getInstance(context);
            return skinFactory.getSkin(context, baseSkinName);
        }
        return null;
    }

    protected Object localResolveSkinParameter(FacesContext context, String name) {
        return getSkinParams().get(name);
    }

    protected boolean localContainsProperty(FacesContext context, String name) {
        return getSkinParams().containsKey(name);
    }

    protected Object executeOperation(FacesContext context, Operation operation, String name) {
        MutableInteger counter = getCounter(context);

        try {
            if (counter.getAndIncrement() > 100) {
                throw new FacesException(Messages.getMessage(Messages.SKIN_CYCLIC_REFERENCE, name));
            }

            Object result = operation.executeLocal(context, this, name);
            if (result != null) {
                return result;
            }

            Skin baseSkin = getBaseSkin(context);
            if (baseSkin != null) {
                return operation.executeBase(context, baseSkin, name);
            }
        } finally {
            counter.getAndDecrement();
        }

        return null;
    }

    protected boolean containsProperty(FacesContext context, String name) {
        return Boolean.TRUE.equals(executeOperation(context, CONTAINS, name));
    }

    protected Object resolveSkinParameter(FacesContext context, String name) {
        return executeOperation(context, RESOLVE, name);
    }

    public boolean containsProperty(String name) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return containsProperty(facesContext, name);
    }

    public String getName() {
        return name;
    }

    public int hashCode(FacesContext context) {
        int hash = 0;
        for (Map.Entry<Object, Object> entry : skinParams.entrySet()) {
            String key = (String) entry.getKey();
            Object value = entry.getValue();

            Object parameter = getValueReference(context, value);

            hash = 31 * hash + key.hashCode();
            hash = 31 * hash + ((parameter != null) ? parameter.hashCode() : 0);
        }

        Skin baseSkin = getBaseSkin(context);

        if (baseSkin != null) {
            hash = 31 * hash + baseSkin.hashCode(context);
        }

        return hash;
    }
}

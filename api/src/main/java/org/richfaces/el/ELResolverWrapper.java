/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
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
package org.richfaces.el;

import java.beans.FeatureDescriptor;
import java.util.Iterator;

import javax.el.ELContext;
import javax.el.ELResolver;

/**
 * @author Nick Belaevski
 * @since 3.3.0
 */
public class ELResolverWrapper extends ELResolver {
    private ELResolver resolver;

    public ELResolverWrapper(ELResolver resolver) {
        super();
        this.resolver = resolver;
    }

    /**
     * @param context
     * @param base
     * @return
     * @see javax.el.ELResolver#getCommonPropertyType(javax.el.ELContext, java.lang.Object)
     */
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        return resolver.getCommonPropertyType(context, base);
    }

    /**
     * @param context
     * @param base
     * @return
     * @see javax.el.ELResolver#getFeatureDescriptors(javax.el.ELContext, java.lang.Object)
     */
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        return resolver.getFeatureDescriptors(context, base);
    }

    /**
     * @param context
     * @param base
     * @param property
     * @return
     * @see javax.el.ELResolver#getType(javax.el.ELContext, java.lang.Object, java.lang.Object)
     */
    public Class<?> getType(ELContext context, Object base, Object property) {
        return resolver.getType(context, base, property);
    }

    /**
     * @param context
     * @param base
     * @param property
     * @return
     * @see javax.el.ELResolver#getValue(javax.el.ELContext, java.lang.Object, java.lang.Object)
     */
    public Object getValue(ELContext context, Object base, Object property) {
        return resolver.getValue(context, base, property);
    }

    /**
     * @param context
     * @param base
     * @param property
     * @return
     * @see javax.el.ELResolver#isReadOnly(javax.el.ELContext, java.lang.Object, java.lang.Object)
     */
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        return resolver.isReadOnly(context, base, property);
    }

    /**
     * @param context
     * @param base
     * @param property
     * @param value
     * @see javax.el.ELResolver#setValue(javax.el.ELContext, java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public void setValue(ELContext context, Object base, Object property, Object value) {
        resolver.setValue(context, base, property, value);
    }
}

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

import javax.el.ELContext;
import javax.el.ELResolver;

/**
 * @author Nick Belaevski
 * @since 3.3.0
 */
public class CapturingELResolver extends ELResolverWrapper {
    private Object base;
    private Object property;

    public CapturingELResolver(ELResolver resolver) {
        super(resolver);
    }

    @Override
    public Object getValue(ELContext context, Object base, Object property) {
        if (property != null) {
            this.base = base;
            this.property = property;
        }

        return super.getValue(context, base, property);
    }

    @Override
    public Class<?> getType(ELContext context, Object base, Object property) {
        if (property != null) {
            this.base = base;
            this.property = property;
        }

        return super.getType(context, base, property);
    }

    public Object getBase() {
        return base;
    }

    public Object getProperty() {
        return property;
    }
}

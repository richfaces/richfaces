/*
 * JBoss, Home of Professional Open Source
 * Copyright , Red Hat, Inc. and individual contributors
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
package org.richfaces.view.facelets;

import javax.el.MethodExpression;
import javax.faces.application.Application;
import javax.faces.el.MethodBinding;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.TagAttribute;

/**
 * @author akolonitsky
 * @since Feb 24, 2010
 */
// TODO nick - handle return type
public abstract class MethodMetadata extends Metadata {
    protected final Class<?>[] signature;
    protected final TagAttribute attribute;

    public MethodMetadata(TagAttribute attribute, Class<?>... signature) {
        this.attribute = attribute;
        this.signature = signature;
    }

    protected MethodExpression getMethodExpression(FaceletContext ctx) {
        return this.attribute.getMethodExpression(ctx, null, signature);
    }

    protected MethodBinding getMethodBinding(FaceletContext ctx) {
        MethodExpression expression = this.attribute.getMethodExpression(ctx, null, signature);
        Application application = ctx.getFacesContext().getApplication();
        return application.createMethodBinding(expression.getExpressionString(), signature); // TODO
                                                                                             // expression.getExpressionString()
                                                                                             // ???
    }
}

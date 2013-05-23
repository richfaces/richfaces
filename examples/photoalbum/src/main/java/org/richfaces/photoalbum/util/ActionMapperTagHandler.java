/*
 * Copyright (C) 2006 - original idea by Andrew Robinson
 * http://drewdev.blogspot.ca/2006/06/creating-composite-controls-with-jsf.html
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

package org.richfaces.photoalbum.util;

import java.io.IOException;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.MethodInfo;
import javax.el.ValueExpression;
import javax.el.VariableMapper;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.FaceletException;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;

public class ActionMapperTagHandler extends TagHandler {

    private static final Class<?>[] ACTION_PARAM_TYPES = new Class[0];

    private static final Class<?>[] ACTION_LISTENER_PARAM_TYPES = new Class[] { ActionEvent.class };

    private static final MethodInfo NOOP_ACTION_INFO = new MethodInfo("$$$noOpAction", String.class, ACTION_PARAM_TYPES);

    private static final MethodExpression NOOP_ACTION_EXPRESSION = new MethodExpression() {

        /**
		 *
		 */
        private static final long serialVersionUID = 8901807727474303033L;

        @Override
        public MethodInfo getMethodInfo(ELContext context) {
            return NOOP_ACTION_INFO;
        }

        @Override
        public Object invoke(ELContext context, Object[] params) {
            return null;
        }

        @SuppressWarnings("all")
        @Override
        public String getExpressionString() {
            return "#{" + NOOP_ACTION_INFO.getName() + "}";
        }

        @Override
        public boolean isLiteralText() {
            return false;
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj;
        }

        @Override
        public int hashCode() {
            return NOOP_ACTION_INFO.hashCode();
        }

    };

    private static final MethodInfo NOOP_ACTION_LISTENER_INFO = new MethodInfo("$$$noOpActionListener", Void.class,
        ACTION_LISTENER_PARAM_TYPES);

    private static final MethodExpression NOOP_ACTION_LISTENER_EXPRESSION = new MethodExpression() {

        /**
		 *
		 */
        private static final long serialVersionUID = 6246200728401095532L;

        @Override
        public MethodInfo getMethodInfo(ELContext context) {
            return NOOP_ACTION_LISTENER_INFO;
        }

        @Override
        public Object invoke(ELContext context, Object[] params) {
            return null;
        }

        @SuppressWarnings("all")
        @Override
        public String getExpressionString() {
            return "#{" + NOOP_ACTION_LISTENER_INFO.getName() + "}";
        }

        @Override
        public boolean isLiteralText() {
            return false;
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj;
        }

        @Override
        public int hashCode() {
            return NOOP_ACTION_LISTENER_INFO.hashCode();
        }
    };

    private static final String ACTION = "action";

    private static final String ACTION_LISTENER = "actionListener";

    private static final String MAPPED_ACTION = "mappedAction";

    private static final String MAPPED_ACTION_LISTENER = "mappedActionListener";

    public ActionMapperTagHandler(TagConfig config) {
        super(config);
    }

    private MethodExpression remap(FaceletContext faceletContext, String varName, Class<?> expectedReturnType,
        Class<?>[] expectedParamTypes) {

        MethodExpression result = null;

        VariableMapper mapper = faceletContext.getVariableMapper();
        ValueExpression valueExpression = mapper.resolveVariable(varName);
        if (valueExpression != null) {
            ExpressionFactory ef = faceletContext.getExpressionFactory();
            ELContext elContext = faceletContext.getFacesContext().getELContext();

            result = ef.createMethodExpression(elContext, valueExpression.getExpressionString(), expectedReturnType,
                expectedParamTypes);
        }

        return result;
    }

    public void apply(FaceletContext ctx, UIComponent parent) throws IOException, FacesException, FaceletException, ELException {

        MethodExpression actionExpression = remap(ctx, ACTION, String.class, ACTION_PARAM_TYPES);
        MethodExpression actionListenerExpression = remap(ctx, ACTION_LISTENER, null, ACTION_LISTENER_PARAM_TYPES);

        VariableMapper initialVarMapper = ctx.getVariableMapper();
        try {
            if (actionExpression == null) {
                actionExpression = NOOP_ACTION_EXPRESSION;
            }

            initialVarMapper.setVariable(MAPPED_ACTION,
                ctx.getExpressionFactory().createValueExpression(actionExpression, MethodExpression.class));

            if (actionListenerExpression == null) {
                actionListenerExpression = NOOP_ACTION_LISTENER_EXPRESSION;
            }

            initialVarMapper.setVariable(MAPPED_ACTION_LISTENER,
                ctx.getExpressionFactory().createValueExpression(actionListenerExpression, MethodExpression.class));

            ctx.setVariableMapper(initialVarMapper);

            nextHandler.apply(ctx, parent);

        } finally {
            ctx.setVariableMapper(initialVarMapper);
        }
    }
}